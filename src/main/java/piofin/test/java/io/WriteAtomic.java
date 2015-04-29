package piofin.test.java.io;

import static com.google.common.base.Objects.equal;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Apr 28, 2015
 */
public class WriteAtomic {

	Charset charset = StandardCharsets.ISO_8859_1;
	ExecutorService executor = Executors.newCachedThreadPool();
	private Path tmpDir;

	enum HowWrite {
		NIO, GUAVA,

		HAND,
		HAND_SYNC_LOCK,
		HAND_LOCK,

		UNSYNC,
	};

	enum HowRead {
		HAND, NIO, GUAVA
	};

	@DataProvider
	public Object[][] howRead() throws Exception {

		List<Object[]> options = newArrayList();
		for (HowWrite howWrite : HowWrite.values()) {
			for (HowRead howRead : HowRead.values()) {

				// if (howWrite != How.HAND) {
				// continue;
				// }

				// if (howRead == How.GUAVA) {
				// continue;
				// }

				options.add(new Object[] { howWrite, howRead });
			}
		}

		return Iterables.toArray(options, Object[].class);
	}

	@Test(dataProvider = "howRead")
	public void testWriteAtomic(HowWrite howWrite, HowRead howRead) throws Exception {

		tmpDir = Files.createTempDirectory("tmpdir");
		String content = Strings.repeat("a", 1024 * 1024);

		List<Path> filesToBeDeleted = newArrayList();
		filesToBeDeleted.add(tmpDir);

		try {
			for (int i = 0; i < 1000 * 5; ++i) {
				Path tmpFile = tmpDir.resolve("tmpFile" + i);
				Path finalFile = tmpDir.resolve("finalFile" + i);

				filesToBeDeleted.add(finalFile);
				filesToBeDeleted.add(tmpFile);

				Future<?> readAtomicallyFuture = readAtomically(finalFile, howRead);

				// write atomically
				boolean success = false;
				switch (howWrite) {
				case NIO:
					Files.copy(new ByteArrayInputStream(content.getBytes(charset)), tmpFile);
					Files.move(tmpFile, finalFile, StandardCopyOption.ATOMIC_MOVE);
					success = true;
					break;

				case GUAVA:
					com.google.common.io.Files.write(content, tmpFile.toFile(), charset);
					success = tmpFile.toFile().renameTo(finalFile.toFile());
					break;

				case HAND:
					try (FileOutputStream os = new FileOutputStream(tmpFile.toFile())) {
						try (Writer writer = new OutputStreamWriter(os, charset)) {
							writer.write(content);
						}
					}
					success = tmpFile.toFile().renameTo(finalFile.toFile());
					break;

				case HAND_LOCK:
					try (FileOutputStream os = new FileOutputStream(tmpFile.toFile())) {
						try (Writer writer = new OutputStreamWriter(os, charset)) {
							FileLock lock = os.getChannel().lock();
							try {
								writer.write(content);

							} finally {
								lock.release();
							}
						}
					}
					success = tmpFile.toFile().renameTo(finalFile.toFile());
					break;

				case HAND_SYNC_LOCK:
					try (FileOutputStream os = new FileOutputStream(tmpFile.toFile())) {
						try (Writer writer = new OutputStreamWriter(os, charset)) {
							FileLock lock = os.getChannel().lock();
							try {
								writer.write(content);

								os.getChannel().force(false);
								os.getFD().sync();
							} finally {
								lock.release();
							}
						}
					}
					success = tmpFile.toFile().renameTo(finalFile.toFile());
					break;

				case UNSYNC:
					try (FileOutputStream os = new FileOutputStream(finalFile.toFile())) {
						try (Writer writer = new OutputStreamWriter(os, charset)) {
							writer.write(content);
						}
					}
					success = true;
					break;
				}

				assertTrue(success, "Unsupported: " + howRead);

				assertTrue(success, "atomic write failed");
				assertEquals(readAtomicallyFuture.get(), content, "read content");
			}

		} finally {
			for (int i = filesToBeDeleted.size() - 1; i >= 0; --i) {
				Files.deleteIfExists(filesToBeDeleted.get(i));
			}
		}
	}

	/** read as soon as it exists */
	private Future<String> readAtomically(final Path finalFile, final HowRead howRead) {

		final File tempDirIo = tmpDir.toFile();
		final File finalFileIo = finalFile.toFile();
		final String finalName = finalFileIo.getName();

		return executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				switch (howRead) {
				case NIO:
					waitUntilExistsNio();
					return new String(Files.readAllBytes(finalFile), charset);

				case GUAVA:
					waitUntilExistsIo();
					return new String(com.google.common.io.Files.toByteArray(finalFile.toFile()), charset);

				case HAND:
					waitUntilListable();
					StringBuilder sb = new StringBuilder();
					try (InputStream is = new FileInputStream(finalFile.toFile())) {
						byte[] buf = new byte[8192];
						int n;
						while ((n = is.read(buf)) > 0) {
							sb.append(new String(buf, 0, n, charset));
						}
					}
					return sb.toString();
				}

				throw new IllegalArgumentException("Unsupported: " + howRead);
			}

			private void waitUntilExistsNio() throws InterruptedException {
				while (!Files.exists(finalFile)) {
					NANOSECONDS.sleep(1);
				}
			}

			private void waitUntilExistsIo() throws InterruptedException {
				while (!finalFileIo.exists()) {
					NANOSECONDS.sleep(1);
				}
			}

			private void waitUntilListable() throws InterruptedException {
				while (tempDirIo.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return equal(name, finalName);
					}
				}).length == 0) {
					NANOSECONDS.sleep(1);
				}
			}
		});
	}
}
