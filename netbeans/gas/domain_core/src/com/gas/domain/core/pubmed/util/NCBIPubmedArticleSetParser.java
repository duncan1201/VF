package com.gas.domain.core.pubmed.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.gas.common.ui.util.DateUtil;
import com.gas.common.ui.util.XMLUtil;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.PubmedArticleSet;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = INCBIPubmedArticleSetParser.class)
public class NCBIPubmedArticleSetParser implements INCBIPubmedArticleSetParser {

    @Override
    public PubmedArticleSet parse(File file) {
        PubmedArticleSet ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NCBIPubmedArticleSetParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public PubmedArticleSet parse(String xmlContents) {
        return parse(xmlContents, true);
    }

    @Override
    public PubmedArticleSet parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    @Override
    public PubmedArticle singleParse(String xmlString) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes(Charset.forName("UTF-8")));
        return singleParse(inputStream);
    }

    @Override
    public PubmedArticle singleParse(Class clazz, String resource) {
        return singleParse(clazz.getResourceAsStream(resource));
    }

    @Override
    public PubmedArticle singleParse(InputStream inputStream) {
        PubmedArticleSet _set = parse(inputStream);
        return _set.getArticleList().get(0);
    }

    @Override
    public PubmedArticleSet parse(InputStream inputStream) {
        return _parse(inputStream, true);
    }

    private PubmedArticleSet _parse(InputStream inputStream, boolean includeAbstractText) {
        PubmedArticleSet ret = new PubmedArticleSet();

        Document doc = XMLUtil.getDocument(inputStream);

        NodeList nodeList = XMLUtil.xPathNodeList(doc, "./PubmedArticleSet/PubmedArticle");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            PubmedArticle pubmedArticle = parsePubmedArticle(node, includeAbstractText);
            ret.getArticles().add(pubmedArticle);
        }

        return ret;
    }

    private static PubmedArticle parsePubmedArticle(Node node, boolean includeAbstractText) {
        if (!node.getNodeName().equals("PubmedArticle")) {
            throw new IllegalArgumentException("Node name must be PubmedArticle");
        }
        PubmedArticle ret = new PubmedArticle();

        String pmid = XMLUtil.xPathString(node, "./MedlineCitation/PMID/text()");

        String created_year = XMLUtil.xPathString(node, "./MedlineCitation/DateCreated/Year/text()");
        String created_month = XMLUtil.xPathString(node, "./MedlineCitation/DateCreated/Month/text()");
        String created_day = XMLUtil.xPathString(node, "./MedlineCitation/DateCreated/Day/text()");
        String journalTitle = XMLUtil.xPathString(node, "./MedlineCitation/Article/Journal/Title/text()");

        String ISOAbbreviation = XMLUtil.xPathString(node, "./MedlineCitation/Article/Journal/ISOAbbreviation/text()");

        Node journalIssueNode = XMLUtil.xPathNode(node, "./MedlineCitation/Article/Journal/JournalIssue");
        String pubYear = XMLUtil.xPathString(journalIssueNode, "./PubDate/Year/text()");
        String volume = XMLUtil.xPathString(journalIssueNode, "./Volume/text()");
        String pagination = XMLUtil.xPathString(node, "./MedlineCitation/Article/Pagination/MedlinePgn/text()");

        String title = XMLUtil.xPathString(node, "./MedlineCitation/Article/ArticleTitle/text()");
        String abstractTxt = null;
        if (includeAbstractText) {
            abstractTxt = XMLUtil.xPathString(node, "./MedlineCitation/Article/Abstract/AbstractText/text()");
        }
        Node authorList = XMLUtil.xPathNode(node, "./MedlineCitation/Article/AuthorList");


        if (pmid != null) {
            ret.setPmid(pmid);
        }
        Date creationDate = DateUtil.create(created_year, created_month, created_day);
        if (creationDate != null) {
            ret.setDateCreated(creationDate);
        }
        if (journalTitle != null) {
            ret.setJournalTitle(journalTitle);
        }
        if (ISOAbbreviation != null) {
            ret.setISOAbbreviation(ISOAbbreviation);
        }
        if (pubYear != null) {
            ret.setPubYear(pubYear);
        }
        if (volume != null) {
            ret.setVolume(volume);
        }
        if (pagination != null) {
            ret.setPagination(pagination);
        }
        if (title != null) {
            ret.setTitle(title);
        }
        if (abstractTxt != null) {
            ret.setAbstractTxt(abstractTxt);
        }
        List<PubmedArticle.Author> authors = parseAuthorList(authorList);
        if (authorList != null) {
            ret.setAuthors(authors);
        }

        NodeList nodeList = XMLUtil.xPathNodeList(node, "./PubmedData/ArticleIdList/ArticleId");
        Map<String, String> articalIdList = parseArticleIdList(nodeList);
        if (articalIdList != null) {
            ret.setArticleIdList(articalIdList);
        }

        return ret;
    }

    @Override
    public PubmedArticle singleParse(File file) {
        PubmedArticle ret = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = singleParse(inputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NCBIPubmedArticleSetParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private static List<PubmedArticle.Author> parseAuthorList(Node node) {
        if (node != null && node.getNodeName() != null && !node.getNodeName().equals("AuthorList")) {
            throw new IllegalArgumentException("Node name must be AuthorList");
        }

        List<PubmedArticle.Author> ret = new ArrayList<PubmedArticle.Author>();

        return ret;
    }

    private static Map<String, String> parseArticleIdList(NodeList nodeList) {
        Map<String, String> ret = new HashMap<String, String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String type = XMLUtil.xPathString(node, "./@IdType");
            String value = node.getTextContent();
            ret.put(type, value);
        }
        return ret;
    }

    @Override
    public <T> List<T> parse(String xmlStr, boolean includeAbstractText, Class<T> retType) {
        if (!retType.isAssignableFrom(PubmedArticle.class)) {
            throw new IllegalArgumentException(String.format("class '%s' not supported", retType.toString()));
        }
        List<T> ret = new ArrayList<T>();

        PubmedArticleSet retSet = parse(xmlStr, includeAbstractText);
        ret = new ArrayList<T>();
        Iterator<PubmedArticle> itr = retSet.getArticleList().iterator();
        while (itr.hasNext()) {
            PubmedArticle article = itr.next();
            ret.add((T) article);
        }
        return ret;
    }

    @Override
    public <T> List<T> parse(String xmlStr, Class<T> retType) {
        return parse(xmlStr, true, retType);
    }

    @Override
    public PubmedArticleSet parse(String xmlContents, boolean includeAbstractText) {
        PubmedArticleSet ret = new PubmedArticleSet();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContents.getBytes(Charset.forName("UTF-8")));
        ret = _parse(inputStream, includeAbstractText);

        return ret;
    }
}
