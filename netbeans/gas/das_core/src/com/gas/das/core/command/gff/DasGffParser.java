package com.gas.das.core.command.gff;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.common.ui.util.XMLUtil;

public class DasGffParser {

    // for internal use only
    private XPath xpath;
    private Document doc;
    private Object result;
    private XPathExpression expr;

    public DasGff parse(InputStream inputStream) {
        DasGff ret = new DasGff();

        try {

            doc = XMLUtil.getDocument(inputStream);

            xpath = XPathFactory.newInstance().newXPath();
            // XPath Query for showing all nodes value
            expr = xpath.compile("DASGFF//GFF");

            // XPathConstants.NODE
            result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                DasGff.Gff gff = new DasGff.Gff();
                Node node = nodes.item(i);
                String href = node.getAttributes().getNamedItem("href").getTextContent();
                gff.setHref(href);

                expr = xpath.compile("//SEGMENT");
                result = expr.evaluate(node, XPathConstants.NODESET);
                NodeList segNodes = (NodeList) result;
                Set<DasGff.Segment> segments = parseSegments(segNodes);
                gff.setSegments(segments);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private Set<DasGff.Segment> parseSegments(NodeList nodeList) {
        Set<DasGff.Segment> ret = new HashSet<DasGff.Segment>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            DasGff.Segment seg = new DasGff.Segment();
            Node node = nodeList.item(i);
            String id = node.getAttributes().getNamedItem("id").getTextContent();
            Integer start = Integer.parseInt(node.getAttributes().getNamedItem("start").getTextContent());
            Integer stop = Integer.parseInt(node.getAttributes().getNamedItem("stop").getTextContent());

            seg.setId(id);
            seg.setStart(start);
            seg.setStop(stop);

            try {
                expr = xpath.compile("//FEATURE");
                result = expr.evaluate(node, XPathConstants.NODESET);
                NodeList segNodes = (NodeList) result;
                Set<DasGff.Feature> features = parseFeatures(segNodes);

            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }

            ret.add(seg);
        }
        return ret;
    }

    private Set<DasGff.Feature> parseFeatures(NodeList list) {
        Set<DasGff.Feature> ret = new HashSet<DasGff.Feature>();
        for (int i = 0; i < list.getLength(); i++) {
            DasGff.Feature feature = new DasGff.Feature();

            Node node = list.item(i);
            String id = node.getAttributes().getNamedItem("id").getTextContent();

            feature = parseFeatureInternal(feature, node);

            feature.setId(id);
            ret.add(feature);
        }
        return ret;
    }

    private DasGff.Feature parseFeatureInternal(DasGff.Feature feature, Node node) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            if (child.getNodeName().equals("START")) {
                Integer start = Integer.parseInt(child.getTextContent());
                feature.setStart(start);
            } else if (child.getNodeName().equals("END")) {
                Integer end = Integer.parseInt(child.getTextContent());
                feature.setEnd(end);
            } else if (child.getNodeName().equals("TYPE")) {
                DasGff.Type type = new DasGff.Type();

                String id = child.getAttributes().getNamedItem("id").getTextContent();
                String reference = child.getAttributes().getNamedItem("reference").getTextContent();
                String superparts = child.getAttributes().getNamedItem("superparts").getTextContent();
                String subparts = child.getAttributes().getNamedItem("subparts").getTextContent();
                String category = child.getAttributes().getNamedItem("category").getTextContent();

                String text = child.getTextContent();

                type.setId(id);
                type.setReference(reference);
                type.setSuperparts(superparts);
                type.setSubparts(subparts);
                type.setCategory(category);
                type.setText(text);

                feature.setType(type);
            } else if (child.getNodeName().equals("METHOD")) {
                String method = child.getTextContent();
                feature.setMethod(method);
            } else if (child.getNodeName().equals("SCORE")) {
                String score = child.getTextContent();
                feature.setScore(score);
            } else if (child.getNodeName().equals("ORIENTATION")) {
                String orientation = child.getTextContent();
                feature.setOrientation(orientation);
            } else if (child.getNodeName().equals("TARGET")) {
                DasGff.Target target = new DasGff.Target();

                String id = child.getAttributes().getNamedItem("id").getTextContent();
                Integer start = Integer.parseInt(child.getAttributes().getNamedItem("start").getTextContent());
                Integer stop = Integer.parseInt(child.getAttributes().getNamedItem("stop").getTextContent());

                target.setId(id);
                target.setStart(start);
                target.setStop(stop);

                feature.setTarget(target);
            }
        }
        return feature;
    }

    public DasGff parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    public static void main(String[] args) {
        DasGffParser parser = new DasGffParser();
        DasGff ret = parser.parse(DasGffParser.class, "features_response.xml");
        System.out.print("");
    }
}
