package com.gas.das.core.command.registry;

import java.io.InputStream;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.common.ui.util.XMLUtil;
import com.gas.das.core.command.registry.DasSources.Version;

public class DasSourcesParser {

    // for internal use only
    private XPath xpath;
    private Document doc;

    public DasSources parse(InputStream inputStream) {
        DasSources ret = new DasSources();

        try {

            doc = XMLUtil.getDocument(inputStream);

            xpath = XPathFactory.newInstance().newXPath();
            // XPath Query for showing all nodes value
            XPathExpression expr = xpath.compile("SOURCES//SOURCE");

            // XPathConstants.NODE
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                DasSources.Source source = new DasSources.Source();
                Node node = nodes.item(i);

                Node uri = node.getAttributes().getNamedItem("uri");
                source.setUri(uri.getTextContent());

                Node title = node.getAttributes().getNamedItem("title");
                source.setTitle(title.getTextContent());

                Node docHref = node.getAttributes().getNamedItem("doc_href");
                if (docHref != null) {
                    source.setDocHref(docHref.getTextContent());
                }


                Node desc = node.getAttributes().getNamedItem("description");
                source.setDesc(desc.getTextContent());

                Node versionNode = XMLUtil.getNamedChildNode(node, "VERSION");
                DasSources.Version version = new DasSources.Version();
                version = parseVersion(version, versionNode);

                source.getVersions().add(version);

                ret.getSources().add(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private Version parseVersion(Version version, Node node) throws XPathExpressionException {
        String versioinUri = node.getAttributes().getNamedItem("uri").getTextContent();
        version.setUri(versioinUri);

        List<Node> nodeList = XMLUtil.getNamedChildNodes(node, "COORDINATES");
        for (int i = 0; i < nodeList.size(); i++) {
            DasSources.Coordinates coordinates = new DasSources.Coordinates();
            Node cNode = nodeList.get(i);
            String source = cNode.getAttributes().getNamedItem("source").getTextContent();
            String uri = cNode.getAttributes().getNamedItem("uri").getTextContent();
            String authority = cNode.getAttributes().getNamedItem("authority").getTextContent();
            coordinates.setSource(source);
            coordinates.setUri(uri);
            coordinates.setAuthority(authority);

            version.getCoordinates().add(coordinates);
        }

        nodeList = XMLUtil.getNamedChildNodes(node, "CAPABILITY");
        for (int i = 0; i < nodeList.size(); i++) {
            DasSources.Capability capability = new DasSources.Capability();
            Node cNode = nodeList.get(i);
            String type = cNode.getAttributes().getNamedItem("type").getTextContent();
            String query_uri = cNode.getAttributes().getNamedItem("query_uri").getTextContent();
            capability.setType(type);
            capability.setQueryURI(query_uri);

            version.getCapabilities().add(capability);
        }

        nodeList = XMLUtil.getNamedChildNodes(node, "PROP");
        for (int i = 0; i < nodeList.size(); i++) {
            Node cNode = nodeList.get(i);
            String name = cNode.getAttributes().getNamedItem("name").getTextContent();
            String value = cNode.getAttributes().getNamedItem("value").getTextContent();

            version.getProperties().put(name, value);
        }
        return version;
    }

    public DasSources parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }
}
