package utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlTools {
	private Document xmlDoc;
	private String xmlPath;
	
	public XmlTools(String xmlPath){
		this.xmlPath = xmlPath;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			xmlDoc = builder.parse(new File(xmlPath));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Document getXmlDoc(){
		return this.xmlDoc;
	}
	
	public String getXmlPath(){
		return xmlPath;
	}
	
	public boolean update(Map<String,String> map){
		
		for(Map.Entry<String, String> entry : map.entrySet()){
			NodeList nodes = xmlDoc.getElementsByTagName(entry.getKey());
			
			for(int i=0;i<nodes.getLength();i++){
				Node _node = nodes.item(i);
				_node.getFirstChild().setNodeValue(entry.getValue());
			}
		}
		
		return save();
	}
	
	public boolean save() {
		boolean flag = true;
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			StreamResult result = new StreamResult(new File(xmlPath));
			transformer.transform(source, result);
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}
}
