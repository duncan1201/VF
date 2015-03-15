package com.gas.uniprot;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gas.uniprot.UniProt.DbReference;
import com.gas.common.ui.util.XMLUtil;

public class UniProtSetParser {

    public static UniProtSet parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    public static UniProtSet parse(InputStream inputStream) {
        Document doc = XMLUtil.getDocument(inputStream);

        UniProtSet ret = new UniProtSet();

        NodeList list = XMLUtil.xPathNodeList(doc, "./uniprot/entry");

        for (int i = 0; i < list.getLength(); i++) {
            UniProt uniProt = parse(list.item(i));
            ret.getSets().add(uniProt);
        }
        return ret;

    }

    private static UniProt parse(Node node) {
        XMLUtil.validate(node, "entry");
        UniProt ret = new UniProt();

        NodeList accessionNodes = XMLUtil.xPathNodeList(node, "./accession");

        parseAccessionNodeList(accessionNodes, ret);

        String name = XMLUtil.xPathString(node, "./name/text()");
        if (name != null) {
            ret.setName(name);
        }

        Node proteinNode = XMLUtil.xPathNode(node, "./protein");
        ret = parseProteinNode(proteinNode, ret);

        Node geneNode = XMLUtil.xPathNode(node, "./gene");
        ret = parseGeneNode(geneNode, ret);

        Node organismNode = XMLUtil.xPathNode(node, "./organism");
        ret = parseOrganismNode(organismNode, ret);

        NodeList refNodeList = XMLUtil.xPathNodeList(node, "./reference");
        if (refNodeList != null) {
            for (int i = 0; i < refNodeList.getLength(); i++) {
                UniProt.Reference ref = parseReferenceNode(refNodeList.item(i));
                ret.getRefs().add(ref);
            }
        }

        NodeList commentNodeList = XMLUtil.xPathNodeList(node, "./comment");
        if (commentNodeList != null) {
            for (int i = 0; i < commentNodeList.getLength(); i++) {
                UniProt.Comment comment = parseCommentNode(commentNodeList.item(i));
                ret.getComments().add(comment);
            }
        }

        NodeList dbRefNodeList = XMLUtil.xPathNodeList(node, "./dbReference");
        if (dbRefNodeList != null) {
            for (int i = 0; i < dbRefNodeList.getLength(); i++) {
                DbReference dbRef = parseDbReference(dbRefNodeList.item(i));
                ret.getDbrefs().add(dbRef);
            }
        }

        NodeList keywordNodeList = XMLUtil.xPathNodeList(node, "./keyword");
        if (keywordNodeList != null) {
            for (int i = 0; i < keywordNodeList.getLength(); i++) {
                UniProt.Keyword kw = parseKeywordNode(keywordNodeList.item(i));
                ret.getKeywords().add(kw);
            }
        }

        NodeList featureNodeList = XMLUtil.xPathNodeList(node, "./feature");
        if (featureNodeList != null) {
            for (int i = 0; i < featureNodeList.getLength(); i++) {
                Node featureNode = featureNodeList.item(i);
                UniProt.Feature feature = parseFeatureNode(featureNode);
                ret.getFeatures().add(feature);
            }
        }

        NodeList evidenceNodeList = XMLUtil.xPathNodeList(node, "./evidence");
        if (evidenceNodeList != null) {
            for (int i = 0; i < evidenceNodeList.getLength(); i++) {
                Node evidenceNode = evidenceNodeList.item(i);
                UniProt.Evidence evidence = parseEvidence(evidenceNode);
                ret.getEvidences().add(evidence);
            }
        }

        Node sequenceNode = XMLUtil.xPathNode(node, "./sequence");
        if (sequenceNode != null) {
            UniProt.Sequence seq = new UniProt.Sequence();

            String length = XMLUtil.xPathString(sequenceNode, "./@length");
            if (length != null) {
                seq.setLength(Integer.parseInt(length));
            }
            String mass = XMLUtil.xPathString(sequenceNode, "./@mass");
            if (mass != null) {
                seq.setMass(mass);
            }
            String modified = XMLUtil.xPathString(sequenceNode, "./@modified");
            if (modified != null) {
                seq.setModified(modified);
            }
            String version = XMLUtil.xPathString(sequenceNode, "./@version");
            if (version != null) {
                seq.setVersion(version);
            }
        }

        return ret;
    }

    private static UniProt.Evidence parseEvidence(Node node) {
        XMLUtil.validate(node, "evidence");

        UniProt.Evidence ret = new UniProt.Evidence();
        String key = XMLUtil.xPathString(node, "./@key");
        if (key != null) {
            ret.setKey(key);
        }
        String type = XMLUtil.xPathString(node, "./@type");
        if (type != null) {
            ret.setType(type);
        }

        Node dbRefNode = XMLUtil.xPathNode(node, "./source/dbReference");
        if (dbRefNode != null) {
            UniProt.Source source = new UniProt.Source();
            DbReference dbRef = parseDbReference(dbRefNode);
            source.setDbRef(dbRef);
            ret.setSource(source);
        }

        return ret;
    }

    private static UniProt.Feature parseFeatureNode(Node node) {
        XMLUtil.validate(node, "feature");
        UniProt.Feature ret = new UniProt.Feature();

        String type = XMLUtil.xPathString(node, "./@type");
        if (type != null) {
            ret.setType(type);
        }

        String id = XMLUtil.xPathString(node, "./@id");
        if (id != null) {
            ret.setId(id);
        }

        String desc = XMLUtil.xPathString(node, "./@description");
        if (desc != null) {
            ret.setDesc(desc);
        }

        String original = XMLUtil.xPathString(node, "./original/text()");
        if (original != null) {
            ret.setOriginal(original);
        }

        String variation = XMLUtil.xPathString(node, "./variation/text()");
        if (variation != null) {
            ret.setVariation(variation);
        }

        Node locationNode = XMLUtil.xPathNode(node, "./location");
        if (locationNode != null) {
            UniProt.Location location = new UniProt.Location();

            String beginPosition = XMLUtil.xPathString(node, "./location/begin/@position");
            if (beginPosition != null) {
                UniProt.Begin begin = new UniProt.Begin();
                begin.setPosition(Integer.parseInt(beginPosition));
                location.setBegin(begin);
            }
            String endPosition = XMLUtil.xPathString(node, "./location/end/@position");


            if (endPosition != null) {
                UniProt.End end = new UniProt.End();
                end.setPosition(Integer.parseInt(endPosition));
                location.setEnd(end);
            }

            ret.setLocation(location);
        }
        return ret;
    }

    private static UniProt.Keyword parseKeywordNode(Node node) {
        XMLUtil.validate(node, "keyword");

        UniProt.Keyword kw = new UniProt.Keyword();

        String id = XMLUtil.xPathString(node, "./@id");
        String value = XMLUtil.xPathString(node, "./text()");
        kw.setId(id);
        kw.setValue(value);

        return kw;
    }

    private static UniProt.Comment parseCommentNode(Node node) {
        XMLUtil.validate(node, "comment");
        UniProt.Comment ret = new UniProt.Comment();

        String type = XMLUtil.xPathString(node, "@type");
        if (type != null) {
            ret.setType(type);
        }

        String phDependence = XMLUtil.xPathString(node, "./phDependence/text()");
        if (phDependence != null) {
            ret.setPhDependence(phDependence);
        }

        String value = XMLUtil.xPathString(node, "./text/text()");
        String evidence = XMLUtil.xPathString(node, "./text/@evidence");


        if (value != null || evidence != null) {
            UniProt.Text text = new UniProt.Text();
            text.setValue(value);
            text.setEvidence(evidence);
        }
        //if(text != null) ret.setText(text);

        return ret;
    }

    private static UniProt.Reference parseReferenceNode(Node node) {
        XMLUtil.validate(node, "reference");
        UniProt.Reference ret = new UniProt.Reference();
        NodeList scopeNodeList = XMLUtil.xPathNodeList(node, "./scope");
        if (scopeNodeList != null) {
            for (int i = 0; i < scopeNodeList.getLength(); i++) {
                Node scopeNode = scopeNodeList.item(i);
                String textContent = scopeNode.getTextContent();
                if (textContent != null) {
                    ret.getScopes().add(textContent);
                }
            }
        }

        Node citationNode = XMLUtil.xPathNode(node, "./citation");
        if (citationNode != null) {
            UniProt.Citation citation = parseCitationNode(citationNode);
            ret.setCitation(citation);
        }
        return ret;
    }

    private static UniProt.Citation parseCitationNode(Node node) {
        XMLUtil.validate(node, "citation");
        UniProt.Citation ret = new UniProt.Citation();
        String type = XMLUtil.xPathString(node, "./@type");
        if (type != null) {
            ret.setType(type);
        }
        String date = XMLUtil.xPathString(node, "./@date");
        if (date != null) {
            ret.setDate(date);
        }
        String name = XMLUtil.xPathString(node, "./@name");
        if (name != null) {
            ret.setName(name);
        }
        String volume = XMLUtil.xPathString(node, "./@volume");
        if (volume != null) {
            ret.setVolume(volume);
        }
        String first = XMLUtil.xPathString(node, "./@first");
        if (first != null) {
            ret.setFirst(first);
        }
        String last = XMLUtil.xPathString(node, "./@last");
        if (last != null) {
            ret.setLast(last);
        }
        String db = XMLUtil.xPathString(node, "./@db");
        if (db != null) {
            ret.setDb(db);
        }

        String title = XMLUtil.xPathString(node, "./title/text()");
        if (title != null) {
            ret.setTitle(title);
        }

        NodeList personNodeList = XMLUtil.xPathNodeList(node, "./authorList/person");
        if (personNodeList != null) {
            for (int i = 0; i < personNodeList.getLength(); i++) {
                Node personNode = personNodeList.item(i);
                String author = personNode.getTextContent();
                ret.getAuthors().add(author);
            }
        }

        NodeList dbRefNodeList = XMLUtil.xPathNodeList(node, "./dbReference");
        for (int i = 0; i < dbRefNodeList.getLength(); i++) {
            DbReference dbRef = parseDbReference(dbRefNodeList.item(i));
            ret.getRefs().add(dbRef);
        }

        return ret;
    }

    private static UniProt parseOrganismNode(Node node, UniProt uniProt) {
        XMLUtil.validate(node, "organism");
        UniProt.Organism organism = new UniProt.Organism();

        NodeList nodeList = XMLUtil.xPathNodeList(node, "./name");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                UniProt.Name name = parseNameNode(nodeList.item(i));
                organism.getNames().add(name);
            }
        }
        Node dbRefNode = XMLUtil.xPathNode(node, "./dbReference");
        if (dbRefNode != null) {
            DbReference dbRef = parseDbReference(dbRefNode);
            organism.setDbRef(dbRef);
        }
        NodeList taxonNodeList = XMLUtil.xPathNodeList(node, "lineage/taxon");
        if (taxonNodeList != null) {
            UniProt.Lineage lineage = new UniProt.Lineage();
            for (int i = 0; i < taxonNodeList.getLength(); i++) {
                String taxon = taxonNodeList.item(i).getTextContent();
                if (taxon != null) {
                    lineage.getNames().add(taxon);
                }
            }
            organism.setLineage(lineage);
        }
        uniProt.setOrganism(organism);
        return uniProt;
    }

    private static DbReference parseDbReference(Node node) {
        XMLUtil.validate(node, "dbReference");
        DbReference ret = new DbReference();

        String type = XMLUtil.xPathString(node, "./@type");
        String id = XMLUtil.xPathString(node, "./@id");
        String key = XMLUtil.xPathString(node, "./@key");

        if (type != null) {
            ret.setType(type);
        }
        if (id != null) {
            ret.setId(id);
        }
        if (key != null) {
            ret.setKey(key);
        }

        return ret;
    }

    private static UniProt parseGeneNode(Node node, UniProt uniProt) {
        XMLUtil.validate(node, "gene");
        UniProt.Gene gene = new UniProt.Gene();
        NodeList nodeList = XMLUtil.xPathNodeList(node, "./name");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                UniProt.Name name = parseNameNode(nodeList.item(i));
                gene.getNames().add(name);
            }
        }
        uniProt.setGene(gene);
        return uniProt;
    }

    private static UniProt.Name parseNameNode(Node node) {
        XMLUtil.validate(node, "name");
        UniProt.Name name = new UniProt.Name();

        String value = XMLUtil.xPathString(node, "./text()");
        String type = XMLUtil.xPathString(node, "./@type");

        if (value != null) {
            name.setValue(value);
        }
        if (type != null) {
            name.setType(type);
        }

        return name;
    }

    private static UniProt parseProteinNode(Node node, UniProt uniProt) {
        XMLUtil.validate(node, "protein");
        String recommendedName = XMLUtil.xPathString(node, "./recommendedName/fullName/text()");
        UniProt.Protein protein = new UniProt.Protein();
        if (recommendedName != null) {
            protein.setRecommendedName(recommendedName);
        }

        NodeList nodeList = XMLUtil.xPathNodeList(node, "./alternativeName/fullName");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String alternativeName = nodeList.item(i).getTextContent();
                if (alternativeName != null) {
                    protein.getAlternativeNames().add(alternativeName);
                }
            }
        }
        uniProt.setProtein(protein);
        return uniProt;
    }

    private static UniProt parseAccessionNodeList(NodeList nodeList, UniProt uniProt) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            String acc = XMLUtil.xPathString(nodeList.item(i), "./text()");
            if (acc != null) {
                uniProt.getAccessions().add(acc);
            }
        }
        return uniProt;
    }
}
