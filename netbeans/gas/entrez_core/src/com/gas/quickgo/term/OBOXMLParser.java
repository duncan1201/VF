package com.gas.quickgo.term;

import com.gas.common.ui.util.XMLUtil;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class OBOXMLParser {
	
	public static GOTerm parse(Class clazz, String resource){
		return parse(clazz.getResourceAsStream(resource));
	}
	
	public static GOTerm parse(InputStream inputStream){
		Document doc = XMLUtil.getDocument(inputStream);
		Node termNode = XMLUtil.xPathNode(doc, "./obo/term");
		GOTerm ret = new GOTerm();
		
		ret = parse(termNode, ret);
		
		return ret;
	}
	
	private static GOTerm parse(Node node, GOTerm term){
		if(!node.getNodeName().equals("term")){
			throw new IllegalArgumentException("Node name must be term");
		}
		String id = XMLUtil.xPathString(node, "./id/text()");
		String name = XMLUtil.xPathString(node, "./name/text()");
		String namespace = XMLUtil.xPathString(node, "./namespace/text()");
		String def = XMLUtil.xPathString(node, "./def/defstr/text()");
		String synonym = XMLUtil.xPathString(node, "./synonym/synonym_text/text()");
		
		term.setId(id);
		term.setName(name);
		term.setNamespace(namespace);
		term.setDef(def);
		term.setSynonym(synonym);
		NodeList xrefNodes = XMLUtil.xPathNodeList(node, "./xref");
		
		for(int i = 0; i < xrefNodes.getLength(); i++){
			Node xrefNode = xrefNodes.item(i);
			String acc = XMLUtil.xPathString(xrefNode, "./acc/text()");
			String dbname = XMLUtil.xPathString(xrefNode, "./dbname/text()");
			
			GOTerm.Xref xref = new GOTerm.Xref();
			xref.setAcc(acc);
			xref.setDbname(dbname);
			term.getRefs().add(xref);
		}
		
		return term;
	}
}
