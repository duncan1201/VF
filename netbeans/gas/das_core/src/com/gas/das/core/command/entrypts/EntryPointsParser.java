package com.gas.das.core.command.entrypts;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.common.ui.util.XMLUtil;

public class EntryPointsParser {

    public EntryPoints parse(InputStream inputStream) {
        EntryPoints entryPoints = new EntryPoints();

        try {

            Document doc = XMLUtil.getDocument(inputStream);

            XPath xpath = XPathFactory.newInstance().newXPath();
            // XPath Query for showing all nodes value
            XPathExpression expr = xpath.compile("//ENTRY_POINTS");

            //XPathConstants.NODE
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);

                Node href = node.getAttributes().getNamedItem("href");
                entryPoints.setHref(href.getTextContent());
            }

            expr = xpath.compile("//SEGMENT");
            result = expr.evaluate(doc, XPathConstants.NODESET);
            nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);


                EntryPoints.Segment segment = new EntryPoints.Segment();
                Integer index = Integer.parseInt(node.getTextContent());
                String type = node.getAttributes().getNamedItem("type").getTextContent();
                String id = node.getAttributes().getNamedItem("id").getTextContent();
                Integer start = Integer.parseInt(node.getAttributes().getNamedItem("start").getTextContent());
                Integer stop = Integer.parseInt(node.getAttributes().getNamedItem("stop").getTextContent());
                String orientation = node.getAttributes().getNamedItem("orientation").getTextContent();
                String subparts = node.getAttributes().getNamedItem("subparts").getTextContent();

                segment.setIndex(index);
                segment.setId(id);
                segment.setType(type);
                segment.setStart(start);
                segment.setStop(stop);
                segment.setOrientation(orientation);
                segment.setSubparts(subparts);

                entryPoints.getSegments().add(segment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entryPoints;
    }

    public EntryPoints parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    public static void main(String[] args) {
        EntryPointsParser parser = new EntryPointsParser();
        EntryPoints pts = parser.parse(EntryPointsParser.class, "entry_points_response.xml");
        
    }
}
