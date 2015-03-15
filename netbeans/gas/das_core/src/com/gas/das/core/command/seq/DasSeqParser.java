package com.gas.das.core.command.seq;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.XMLUtil;

public class DasSeqParser {

    public DasSeq parse(InputStream inputStream) {
        DasSeq dasSeq = new DasSeq();

        try {

            Document doc = XMLUtil.getDocument(inputStream);

            XPath xpath = XPathFactory.newInstance().newXPath();
            // XPath Query for showing all nodes value
            XPathExpression expr = xpath.compile("//SEQUENCE");

            // XPathConstants.NODE
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                DasSeq.Seq seq = new DasSeq.Seq();
                Node node = nodes.item(i);

                String id = node.getAttributes().getNamedItem("id").getTextContent();
                Integer start = Integer.parseInt(node.getAttributes().getNamedItem("start").getTextContent());
                Integer stop = Integer.parseInt(node.getAttributes().getNamedItem("stop").getTextContent());
                String data = node.getTextContent();

                seq.setId(id);
                seq.setStart(start);
                seq.setStop(stop);
                seq.setData(data);

                dasSeq.getSeqs().add(seq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dasSeq;
    }

    public DasSeq parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DasSeqParser parser = new DasSeqParser();
        DasSeq pts = parser.parse(DasSeqParser.class, "sequence_response.xml");
        
    }
}
