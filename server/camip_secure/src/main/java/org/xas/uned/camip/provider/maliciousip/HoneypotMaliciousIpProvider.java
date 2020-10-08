package org.xas.uned.camip.provider.maliciousip;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Service
public class HoneypotMaliciousIpProvider extends AbstractMaliciousIpProvider {

	private static final String PROJECTHONEYPOT_URL = "https://www.projecthoneypot.org/list_of_ips.php?rss=1&rf=238581";

	@Override
	protected String getUrl() {
		return PROJECTHONEYPOT_URL;
	};

	@Override
	protected Set<String> readResult(final InputStream inputStream) throws Exception {

		Set<String> ips = new HashSet<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(inputStream);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression xPathExpression = xpath.compile("/rss/channel/item/title/text()");
		NodeList nodes = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			ips.add(nodes.item(i).getNodeValue().split(" \\|")[0]);
		}

		return ips;

	};

}
