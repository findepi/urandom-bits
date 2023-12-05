/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package findepi.xml;

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
 * @author findepi <piotr.findeisen@syncron.com>
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
