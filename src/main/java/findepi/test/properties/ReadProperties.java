package findepi.test.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Sep 4, 2013
 */
public class ReadProperties {
	public static void main(String[] args) throws IOException {
		Properties p = new Properties();
		p.load(ReadProperties.class.getResourceAsStream("prop1.properties"));

		System.out.println("" + p);
	}
}
