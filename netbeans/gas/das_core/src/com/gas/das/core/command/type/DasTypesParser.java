package com.gas.das.core.command.type;

import java.io.InputStream;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.XMLUtil;
import com.gas.das.core.command.seq.DasSeq;
import com.gas.das.core.command.seq.DasSeq.Seq;

public class DasTypesParser {

    public DasTypes parse(InputStream inputStream) {
        DasTypes dasSeq = new DasTypes();

        try {

            Document doc = XMLUtil.getDocument(inputStream);

            NodeList dasTypesNodeList = doc.getElementsByTagName("DASTYPES");
            Node dasTypeNode = dasTypesNodeList.item(0);

            Node gffNode = XMLUtil.getNamedChildNode(dasTypeNode, "GFF");
            DasTypes.Gff gff = parseGff(gffNode);

            dasSeq.setGff(gff);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dasSeq;
    }

    private DasTypes.Gff parseGff(Node gffNode) {
        DasTypes.Gff gff = new DasTypes.Gff();
        Node gffHref = gffNode.getAttributes().getNamedItem("href");
        if (gffHref != null) {
            gff.setHref(gffHref.getTextContent());
        }
        Node gffVersion = gffNode.getAttributes().getNamedItem("version");
        if (gffVersion != null) {
            gff.setVersion(gffVersion.getTextContent());
        }

        List<Node> segmentNodes = XMLUtil.getNamedChildNodes(gffNode, "SEGMENT");
        for (Node segNode : segmentNodes) {
            DasTypes.Segment seg = new DasTypes.Segment();
            Node tmpNode = segNode.getAttributes().getNamedItem("version");
            if (tmpNode != null) {
                seg.setVersion(tmpNode.getTextContent());
            }
            List<Node> typeNodes = XMLUtil.getNamedChildNodes(segNode, "TYPE");
            for (Node typeNode : typeNodes) {
                DasTypes.Type type = new DasTypes.Type();
                //<TYPE id="HInvGeneMrna" category="transcription" method="BLAT"/>
                tmpNode = typeNode.getAttributes().getNamedItem("id");
                if (tmpNode != null) {
                    type.setId(tmpNode.getTextContent());
                }
                tmpNode = typeNode.getAttributes().getNamedItem("category");
                if (tmpNode != null) {
                    type.setCategory(tmpNode.getTextContent());
                }
                tmpNode = typeNode.getAttributes().getNamedItem("method");
                if (tmpNode != null) {
                    type.setMethod(tmpNode.getTextContent());
                }
                seg.getTypes().add(type);
            }

            gff.getSegments().add(seg);
        }

        return gff;
    }

    public DasTypes parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }
}
