package findepi.git;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Oct 26, 2016
 */
public class GitAddedLinesStat {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.printf("Usage java %s <path to git repo> <files pattern>\n",
					GitAddedLinesStat.class.getName());
			System.exit(1);
		}

		String pathToGitRepo = args[0];
		Pattern interestingFilePattern = Pattern.compile(args[1]);

		Map<String, Long> linesByAuthor = new HashMap<>();

		ProcessBuilder processBuilder = new ProcessBuilder(
				"git", "-C", pathToGitRepo,
				"log", "--numstat", "--pretty=format:COMMIT %h:%an")
						.redirectOutput(Redirect.PIPE);

		Process process = processBuilder.start();
		try {

			Pattern commitLinePattern = Pattern.compile("COMMIT (?<hash>[a-f0-9]+):(?<author>.*)");
			Pattern fileStatPattern = Pattern.compile("(?<added>\\d+|-)\\s+(?<removed>\\d+|-)\\s+(?<path>.*)");

			// We really don't have to close reader as the input stream will be closed by Process.destroy
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {

				String currentAuthor = null;

				while (true) {
					String line = reader.readLine();
					if (line == null) {
						// EOF
						break;
					}

					if (line.trim().isEmpty()) {
						continue;

					}

					Matcher commitLineMatcher = commitLinePattern.matcher(line);
					if (commitLineMatcher.matches()) {
						currentAuthor = authorize(requireNonNull(commitLineMatcher.group("author"),
								"commitLineMatcher.group(author)"));
						continue;
					}

					Matcher fileStatMatcher = fileStatPattern.matcher(line);
					if (fileStatMatcher.matches()) {
						String path = fileStatMatcher.group("path");
						if (!interestingFilePattern.matcher(path).matches()) {
							// skip it
							continue;
						}
						if ("-".equals(fileStatMatcher.group("added"))
								|| "-".equals(fileStatMatcher.group("removed"))) {
							System.err.println("Binary: " + line);
							continue;
						}

						int added = parseInt(fileStatMatcher.group("added"));
						int removed = parseInt(fileStatMatcher.group("removed"));

						linesByAuthor.put(currentAuthor,
								added - removed + linesByAuthor.getOrDefault(currentAuthor, 0L));

						continue;
					}

					System.err.println("Unrecognized line: " + line);
				}
			}

		} finally {
			process.destroyForcibly();
		}

		linesByAuthor.entrySet().stream()
				.sorted(Comparator.comparing(Entry::getValue))
				.forEachOrdered(entry -> {
					System.out.printf("%s: %d\n", entry.getKey(), entry.getValue());
				});
	}

	/**
	 * Normalize author name.
	 */
	// http://stackoverflow.com/a/15190787
	private static String authorize(String authorName) {
		authorName = Normalizer.normalize(authorName, Normalizer.Form.NFD);
		authorName = authorName.replaceAll("\\p{InCombiningDiacriticalMarks}", "");
		authorName = authorName.replace("Ł", "L");
		authorName = authorName.replace("ł", "l");
		authorName = Normalizer.normalize(authorName, Normalizer.Form.NFC);
		return authorName;
	}
}
