package findepi.xml;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

/**
 * A test stub for testing against XML External Entities (XXE) vulnerability
 * (https://www.owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing).
 *
 * @author findepi <piotr.findeisen@syncron.com>
 * @since May 13, 2015
 */
public abstract class XXETestStub implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(XXETestStub.class);

	protected final ExecutorService executor = Executors.newCachedThreadPool();

	protected String getXxeEntityName() {
		return "xxe";
	}

	protected final String getXxeEntity() {
		return "&" + getXxeEntityName() + ";";
	}

	@Override
	public void run() {
		try {
			runTestAndCleanUp();

		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}

	private void runTestAndCleanUp() throws Throwable {

		Throwable thrown = null;
		try {

			runTest();

		} catch (Throwable e) {
			thrown = e;
			throw thrown;

		} finally {
			try {
				executor.shutdownNow();
				if (!executor.awaitTermination(10, SECONDS)) {
					Assert.fail("Some tasks did not terminate in expected time");
				}

			} catch (Throwable e) {
				if (thrown != null) {
					thrown.addSuppressed(e);
				}
				throw Objects.firstNonNull(thrown, e);
			}
		}

	}

	private void runTest() throws Exception {
		final AtomicBoolean accepted = new AtomicBoolean(false);
		Future<?> acceptFinish;

		/*
		 * We open real socket so that we can verify *no* connection is ever made.
		 */
		try (final ServerSocket listeningSocket = new ServerSocket(0)) {
			acceptFinish = executor.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					while (true) {
						try (Socket socket = listeningSocket.accept()) {
							log.error("Accepted: {}", socket);
							accepted.set(true);

							/*
							 * Must reply something not to hang test. HttpURLConnection has a "feature" where it
							 * re-connects when doing GET and first request did not return (enough) data. If we simply
							 * closed the socket, the HttpURLConnection would do this second attempt and it would block
							 * indefinitely.
							 */
							socket.getOutputStream().write((""
									+ "HTTP/1.0 200 OK\r\n"
									+ "Content-Type: text/xml\r\n"
									+ "Content-Length: 59\r\n"
									+ "\r\n"
									+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
									+ "<injected></injected>\n"
									).getBytes(US_ASCII));

							socket.getOutputStream().flush();
						}
					}
				}
			});

			// Given
			String xxeXml = makeXxeXml("localhost", listeningSocket.getLocalPort());

			try {
				// When
				tryInjectXml(xxeXml);

			} catch (Throwable e) {
				if (accepted.get()) {
					e.addSuppressed(new AssertionError("XXE testing socket should not have been connected to"));
				}
				throw e;
			}

			// Then
			Assert.assertFalse(accepted.get(), "XXE testing socket should not have been connected to");
		}

		// Cleanup
		waitAcceptFutureAfterClose(acceptFinish);
	}

	private void waitAcceptFutureAfterClose(Future<?> acceptFuture) throws InterruptedException, TimeoutException {
		try {
			acceptFuture.get(10, SECONDS);
			Assert.fail("Expected ExecutionException, since ServerSocket.close() concurred with ServerSocket.accept()");
		} catch (ExecutionException expected) {
		}
	}

	protected String makeXxeXml(String hostname, int port) {
		String xxeHeader = makeXmlHeaderWithXxeEntity(hostname, port, getXxeEntityName());

		return xxeHeader
				+ "  <foo>" + getXxeEntity() + "</foo>";
	}

	protected static String makeXmlHeaderWithXxeEntity(String hostname, int port, String xxeEntityName) {
		String xxeHeader = format(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ " <!DOCTYPE foo [  \n"
						+ "   <!ELEMENT foo ANY >\n"
						+ "   <!ENTITY %s SYSTEM \"http://%s:%s/\" >]>\n",
				xxeEntityName, hostname, port);
		return xxeHeader;
	}

	protected abstract void tryInjectXml(String xxeXml) throws Exception;

	protected static boolean isXmlExternalEntityParsingException(Throwable e, String xeeEntityName) {
		return Strings.nullToEmpty(e.getMessage()).matches(
				"(?s).*Undeclared general entity \"" + xeeEntityName + "\".*");
	}
}
