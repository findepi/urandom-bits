package piofin.test.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since May 15, 2015
 */
public class Parse {

	public static void main(String[] args) {

		new XXETestStub() {

			@Override
			protected void tryInjectXml(String xxeXml) throws Exception {

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); // does not help in Java 7
				Transformer transformer = transformerFactory.newTransformer();

				// StreamSource is insecure by default:
				// Source source = new StreamSource(new StringReader(xxeXml));

				// Source configured to be secure:
				XMLInputFactory xif = XMLInputFactory.newFactory();
				xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				XMLEventReader xmlEventReader = xif.createXMLEventReader(new StringReader(xxeXml));
				Source source = new StAXSource(xmlEventReader);

				transformer.transform(
						source,
						new StreamResult(new ByteArrayOutputStream()));

			}

		}.run();

	}
}
