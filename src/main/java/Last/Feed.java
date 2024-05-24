package Last;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;


public class Feed {
	private String urlString;
	private Document document;
	public Feed(String urlString, String encoding) {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		this.urlString = urlString;
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			document = this.bulidDocument(inputStream,encoding);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Feed(String urlString) {
		this(urlString,"utf-8");
	}
	public String getURLString() {
		return urlString;
	}
	public ArrayList<Item> getItemList(String filterword){
		ArrayList<Item> itemList = new ArrayList<Item>();
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String xpathExpression = String.format("//item[contains(title, '%s')]",filterword);
			NodeList itemNodeList = (NodeList)xPath.evaluate(xpathExpression,
					document,XPathConstants.NODESET);
			if(itemNodeList.getLength() == 0) {
				itemNodeList = (NodeList)xPath.evaluate("/RDF/item",
						document, XPathConstants.NODESET);
			}
			for(int i = 0; i < itemNodeList.getLength(); i++) {
				Node itemNode= itemNodeList.item(i);
				String title = xPath.evaluate("title", itemNode);
				String link = xPath.evaluate("link", itemNode);
				String comments = xPath.evaluate("comments", itemNode);
				itemList.add(new Item(title, link,comments));
				}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}
	public Document bulidDocument(InputStream inputStream, String encoding) {
		Document document = null;
		try {
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementationLS implementation = (DOMImplementationLS)registry.getDOMImplementation("LS 3.0");
			
			LSInput input = implementation.createLSInput();
			input.setByteStream(inputStream);
			input.setEncoding(encoding);
			LSParser parser = implementation.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS,null);
			parser.getDomConfig().setParameter("namespaces", false);
			document = parser.parse(input);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return document;
	}
}
