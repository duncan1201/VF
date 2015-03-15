package com.gas.entrez.core.elink;

import com.gas.entrez.core.elink.api.ELinkResult;
import java.io.InputStream;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.entrez.core.elink.api.ELinkResult.LinkSetDb;
import com.gas.common.ui.util.XMLUtil;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

public class ElinkResultParser {

    public static ELinkResult parse(String xml) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        return parse(inputStream);
    }

    public static ELinkResult parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    public static ELinkResult parse(InputStream inputStream) {
        ELinkResult ret = new ELinkResult();
        Document doc = XMLUtil.getDocument(inputStream);
        String dbFrom = XMLUtil.xPathString(doc, "./eLinkResult/LinkSet/DbFrom/text()");
        ret.setDbFrom(dbFrom);

        String idList = XMLUtil.xPathString(doc, "./eLinkResult/LinkSet/IdList/Id/text()");
        ret.setIdList(idList);

        NodeList dbs = XMLUtil.xPathNodeList(doc, "./eLinkResult/LinkSet/LinkSetDb");

        for (int i = 0; i < dbs.getLength(); i++) {
            LinkSetDb db = parseLinkSetDb(dbs.item(i));
            ret.getDbs().add(db);
        }

        return ret;
    }

    private static LinkSetDb parseLinkSetDb(Node node) {
        if (!node.getNodeName().equals("LinkSetDb")) {
            throw new IllegalArgumentException("Node name must be LinkSetDb");
        }
        LinkSetDb ret = new LinkSetDb();

        String dbTo = XMLUtil.xPathString(node, "./DbTo/text()");
        String linkName = XMLUtil.xPathString(node, "./LinkName/text()");

        ret.setDbTo(dbTo);
        ret.setLinkName(linkName);

        NodeList idNodes = XMLUtil.xPathNodeList(node, "./Link/Id");
        for (int i = 0; i < idNodes.getLength(); i++) {
            Node idNode = idNodes.item(i);
            String id = idNode.getTextContent();
            ret.getLinkIds().add(id);
        }

        return ret;
    }
}
