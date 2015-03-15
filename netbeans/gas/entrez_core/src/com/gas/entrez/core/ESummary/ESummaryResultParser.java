package com.gas.entrez.core.ESummary;

import com.gas.entrez.core.ESummary.api.ESummaryResult;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.common.ui.util.XMLUtil;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

public class ESummaryResultParser {

    public static ESummaryResult parse(String xml) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        return parse(inputStream);
    }
    
    public static ESummaryResult parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    public static ESummaryResult parse(InputStream inputStream) {
        ESummaryResult ret = new ESummaryResult();

        Document doc = XMLUtil.getDocument(inputStream);

        NodeList docSumNodeList = XMLUtil.xPathNodeList(doc, "./eSummaryResult/DocSum");

        for (int i = 0; i < docSumNodeList.getLength(); i++) {
            Node node = docSumNodeList.item(i);
            ESummaryResult.DocSum docSum = parseDocSum(node);
            ret.getDocSums().add(docSum);
        }

        return ret;
    }

    private static ESummaryResult.DocSum parseDocSum(Node node) {
        if (!node.getNodeName().equals("DocSum")) {
            throw new IllegalArgumentException("Node name must be DocSum");
        }

        ESummaryResult.DocSum ret = new ESummaryResult.DocSum();
        String id = XMLUtil.xPathString(node, "./Id/text()");
        if (id != null) {
            ret.setId(id);
        }

        NodeList itemNodes = XMLUtil.xPathNodeList(node, "./Item");
        for (int i = 0; i < itemNodes.getLength(); i++) {
            ESummaryResult.Item item = parseItem(itemNodes.item(i));
            ret.getItems().add(item);
        }

        return ret;
    }

    private static ESummaryResult.Item parseItem(Node node) {
        if (!node.getNodeName().equals("Item")) {
            throw new IllegalArgumentException("Node name must be Item");
        }
        ESummaryResult.Item ret = new ESummaryResult.Item();
        String name = XMLUtil.xPathString(node, "@Name");
        if (name != null) {
            ret.setName(name);
        }
        String type = XMLUtil.xPathString(node, "@Type");
        if (type != null) {
            ret.setType(type);
        }

        if (type.equalsIgnoreCase("String") || type.equalsIgnoreCase("Date")) {
            String text = XMLUtil.xPathString(node, "./text()");
            if (text != null) {
                ret.setText(text);
            }
        } else if (type.equalsIgnoreCase("Structure")) {
            NodeList childNodes = XMLUtil.xPathNodeList(node, "./Item");
            for (int i = 0; i < childNodes.getLength(); i++) {
                ESummaryResult.Item item = parseItem(childNodes.item(i));
                ret.getItems().add(item);
            }
        }
        return ret;
    }
}
