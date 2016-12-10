package findepi.git;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Oct 26, 2016
 */
public class GitAddedLinesStat {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.printf("Usage java %s <path to git repo>\n", GitAddedLinesStat.class.getName());
			System.exit(1);
		}

		String pathToGitRepo = args[0];

		ProcessBuilder processBuilder = new ProcessBuilder(
				"git", "-C", pathToGitRepo,
				"log", "--shortstat", "--pretty=format:%h:%an")
						.redirectOutput(Redirect.PIPE);

		Process process = processBuilder.start();
		try {

			Map<String, Integer> linesByAuthor = new HashMap<>();
			Pattern insertionsDeletionsPattern = Pattern.compile(
					" *\\d+ files? changed(, (?<insertions>\\d+) insertion\\S*)?(, (?<deletions>\\d+) deletion\\S*)?");
			Pattern hashAndAuthorPattern = Pattern.compile("(?<hash>[a-f0-9]+):(?<author>.*)");

			// We really don't have to close reader as the input stream will be closed by Process.destroy
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))) {
				while (true) {
					String hashAndAuthor = reader.readLine();
					checkState(!Strings.isNullOrEmpty(hashAndAuthor), "author empty");
					String author = hashAndAuthor.replaceFirst("^[^:]+:", "");

					reader.mark(1024);
					String stats = reader.readLine();
					checkState(!Strings.isNullOrEmpty(stats), "stats empty");

					Matcher matcher = insertionsDeletionsPattern.matcher(stats);
					if (!matcher.matches()) {
						// let's assume no stats here
						checkState(hashAndAuthorPattern.matcher(stats).matches(), "neither stats nor author: %s",
								stats);
						reader.reset();
						continue;
					}

					int insertions = parseInt(firstNonNull(matcher.group("insertions"), "0"));
					int deletions = parseInt(firstNonNull(matcher.group("deletions"), "0"));
					int delta = insertions - deletions;

					Integer authorSoFar = linesByAuthor.getOrDefault(author, 0);
					linesByAuthor.put(author, authorSoFar + delta);

					String next = reader.readLine();
					if (next == null) {
						// EOF
						break;
					}
					checkState("".equals(next), "unexpected: %s", next);
				}
			}

			linesByAuthor.entrySet().stream()
					.sorted(Comparator.comparing(Entry::getValue))
					.forEachOrdered(entry -> {
						System.out.printf("%s: %d\n", entry.getKey(), entry.getValue());
					});

		} finally {
			process.destroyForcibly();
		}
	}
}
