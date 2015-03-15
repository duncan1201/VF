package com.gas.common.ui.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.openide.util.Exceptions;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {

    private static XPathFactory factory = XPathFactory.newInstance();

    public static Document getDocument(Class clazz, String source) {
        return getDocument(clazz.getResourceAsStream(source));
    }

    public static void validate(Node node, String expectedNodeName) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        if (!node.getNodeName().equals(expectedNodeName)) {
            throw new IllegalArgumentException("Node name must be " + expectedNodeName + ".It is " + node.getNodeName() + " instead");
        }
    }

    /*
     *
     */
    public static String xPathString(Node node, String expr) {
        String ret = (String) xPath(node, expr, XPathConstants.STRING);
        return ret;
    }

    public static Integer xPathInteger(Node node, String expr) {
        String retStr = (String) xPath(node, expr, XPathConstants.STRING);
        Integer ret = CommonUtil.parse(retStr, Integer.class);
        return ret;
    }

    public static List<String> xPathStringList(Node node, String expr) {
        List<String> ret = new ArrayList<String>();
        NodeList nodeList = xPathNodeList(node, expr);
        for (int i = 0; i < nodeList.getLength(); i++) {
            String text = nodeList.item(i).getTextContent();
            ret.add(text);
        }
        return ret;
    }

    public static Double xPathNumber(Node node, String expr) {
        Double ret = (Double) xPath(node, expr, XPathConstants.NUMBER);
        return ret;
    }

    public static NodeList xPathNodeList(Node node, String expr) {
        NodeList ret = new IIOMetadataNode();
        if (node != null) {
            ret = (NodeList) xPath(node, expr, XPathConstants.NODESET);
        }
        return ret;
    }

    public static Node xPathNode(Node node, String expr) {
        Node ret = (Node) xPath(node, expr, XPathConstants.NODE);
        return ret;
    }

    public static Boolean xPathBoolean(Node node, String expr) {
        Boolean ret = null;
        String s = (String) xPath(node, expr, XPathConstants.STRING);
        if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("y")) {
            ret = true;
        } else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("n")) {
            ret = false;
        }
        return ret;
    }

    public static Object xPath(Node node, String expr, QName qname) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        Object result = null;

        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expression = xpath.compile(expr);

            result = expression.evaluate(node, qname);
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return result;
    }

    private synchronized static XPath createNewXPath() {
        return factory.newXPath();
    }

    public static Document getDocument(String string) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                string.getBytes());
        return getDocument(inputStream);
    }

    public static Document getDocument(File file) {
        Document document = null;
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            document = getDocument(stream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return document;
    }

    public static Document newDoc() {
        Document doc = null;
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = domFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return doc;
    }

    public static void transform(Document doc, File file) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static Document getDocument(InputStream inputStream) {
        Document doc = null;
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            //domFactory.setNamespaceAware(false);
            domFactory.setValidating(false);
            DocumentBuilder builder;

            builder = domFactory.newDocumentBuilder();
            doc = builder.parse(inputStream);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception io) {
            io.printStackTrace();
        }
        /*
         * builder.setEntityResolver(new EntityResolver() {
         *
         * @Override public InputSource resolveEntity(String publicId, String
         * systemId) throws SAXException, IOException {
         * System.out.println(systemId); System.out.println(publicId); if
         * (systemId.contains("dasep.dtd")) { return new InputSource(new
         * StringReader("")); } else { return null; } } });
         */

        return doc;
    }

    public static Node getNamedChildNode(Node node, String name) {
        if (node instanceof Document) {
            throw new IllegalArgumentException("Document is not supported");
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        if (!name.contains("/")) {

            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node child = list.item(i);
                if (child.getNodeName().equalsIgnoreCase(name)) {
                    return child;
                }
            }
            return null;
        } else {
            String[] nodeNames = name.split("/");
            String newPath = "";
            for (int i = 1; i < nodeNames.length; i++) {
                newPath += nodeNames[i];
                if (i < nodeNames.length - 1) {
                    newPath += "/";
                }
            }
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node child = list.item(i);
                if (child.getNodeName().equalsIgnoreCase(nodeNames[0])) {
                    Node ret = getNamedChildNode(child, newPath);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
            return getNamedChildNode(node, newPath);
        }
    }

    public static List<Node> getNamedChildNodes(Node node, String name) {
        if (node instanceof Document) {
            throw new IllegalArgumentException("Document is not supported");
        }
        List<Node> ret = new ArrayList<Node>();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            if (child.getNodeName().equalsIgnoreCase(name)) {
                ret.add(child);
            }
        }
        return ret;
    }
}
