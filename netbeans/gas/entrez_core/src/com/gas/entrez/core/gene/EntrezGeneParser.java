package com.gas.entrez.core.gene;

import com.gas.common.ui.util.XMLUtil;
import java.io.InputStream;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EntrezGeneParser {

    public static EntrezGeneList parse(Class clazz, String resource) {
        return parse(clazz.getResourceAsStream(resource));
    }

    public static EntrezGeneList parse(InputStream inputStream) {
        EntrezGeneList ret = new EntrezGeneList();
        long t1 = System.currentTimeMillis();
        Document doc = XMLUtil.getDocument(inputStream);
        long t2 = System.currentTimeMillis();
        NodeList list = XMLUtil.xPathNodeList(doc, "./Entrezgene-Set/Entrezgene");
        long t3 = System.currentTimeMillis();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            EntrezGene entrezGene = parseEntrezGene(node);
            ret.getList().add(entrezGene);
        }
        long t4 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
        System.out.println(t4 - t3);
        return ret;
    }

    private static EntrezGene parseEntrezGene(Node node) {
        if (!node.getNodeName().equals("Entrezgene")) {
            throw new IllegalArgumentException("Node name must be Entrezgene");
        }
        EntrezGene ret = new EntrezGene();


        Node Entrezgene_track_info_node = XMLUtil.xPathNode(node, "./Entrezgene_track-info");
        String summary = XMLUtil.xPathString(node, "./Entrezgene_summary/text()");
        ret.setSummary(summary);
        Node Entrezgene_source_node = XMLUtil.xPathNode(node, "./Entrezgene_source");
        String geneType = XMLUtil.xPathString(node, "./Entrezgene_type/@value");
        ret.setGeneType(geneType);
        Node Entrezgene_gene_node = XMLUtil.xPathNode(node, "./Entrezgene_gene");
        Node Entrezgene_locus_node = XMLUtil.xPathNode(node, "./Entrezgene_locus");
        Node Entrezgene_properties_node = XMLUtil.xPathNode(node, "./Entrezgene_properties");
        Node Entrezgene_comments_node = XMLUtil.xPathNode(node, "./Entrezgene_comments");


        ret = parseEntrezgene_track_info_node(Entrezgene_track_info_node, ret);
        ret = parseEntrezgene_source_node(Entrezgene_source_node, ret);
        ret = parseEntrezgene_gene_node(Entrezgene_gene_node, ret);
        ret = parseEntrezgene_locus_node(Entrezgene_locus_node, ret);
        ret = parseEntrezgene_comments_node(Entrezgene_comments_node, ret);


        return ret;
    }

    private static EntrezGene parseEntrezgene_locus_node(Node node, EntrezGene gene) {
        if (!node.getNodeName().equals("Entrezgene_locus")) {
            throw new IllegalArgumentException("Node name must be Entrezgene_locus");
        }
        NodeList childList = XMLUtil.xPathNodeList(node, "./Gene-commentary");
        for (int i = 0; i < childList.getLength(); i++) {
            Node child = childList.item(i);
            EntrezGene.GeneCommentary geneCommentary = parseGeneCommentary(child);
            gene.getLocus().add(geneCommentary);
        }


        return gene;
    }

    private static EntrezGene parseEntrezgene_source_node(Node node, EntrezGene gene) {
        if (!node.getNodeName().equals("Entrezgene_source")) {
            throw new IllegalArgumentException("Node name must be Entrezgene_source");
        }
        String taxonName = XMLUtil.xPathString(node, "BioSource/BioSource_org/Org-ref/Org-ref_taxname/text()");
        gene.setTaxonName(taxonName);

        String taxonId = XMLUtil.xPathString(node, "BioSource/BioSource_org/Org-ref/Org-ref_db/Dbtag/Dbtag_tag/Object-id/Object-id_id/text()");
        gene.setTaxonId(taxonId);

        String lineage = XMLUtil.xPathString(node, "BioSource/BioSource_org/Org-ref/Org-ref_orgname/OrgName/OrgName_lineage/text()");
        gene.setLineage(lineage);
        return gene;
    }

    private static EntrezGene parseEntrezgene_track_info_node(Node node, EntrezGene gene) {
        if (!node.getNodeName().equals("Entrezgene_track-info")) {
            throw new IllegalArgumentException("Node name must be Entrezgene_track-info");
        }
        String geneId = XMLUtil.xPathString(node, "./Gene-track/Gene-track_geneid/text()");
        gene.setGeneId(geneId);
        return gene;
    }

    private static EntrezGene parseEntrezgene_comments_node(Node node, EntrezGene gene) {
        if (!node.getNodeName().equals("Entrezgene_comments")) {
            throw new IllegalArgumentException("Node must be Entrezgene_comments");
        }

        Node tmp = XMLUtil.xPathNode(node, "./Gene-commentary[Gene-commentary_heading = \"RefSeq Status\"]");
        String refSeqStatus = XMLUtil.xPathString(tmp, "./Gene-commentary_label/text()");
        gene.setRefSeqStatus(refSeqStatus);


        NodeList childList = XMLUtil.xPathNodeList(node, "./Gene-commentary[Gene-commentary_type/@value = \"generif\"]");
        for (int i = 0; i < Math.min(10, childList.getLength()); i++) {
            Node child = childList.item(i);
            EntrezGene.GeneCommentary geneCommentary = parseGeneCommentary(child);
            gene.getGenerifs().add(geneCommentary);
        }
        gene.setTotalNumOfRif(childList.getLength());

        childList = XMLUtil.xPathNodeList(node, "./Gene-commentary[Gene-commentary_heading/text() = \"Phenotypes\"]");
        for (int i = 0; i < childList.getLength(); i++) {
            Node child = childList.item(i);
            EntrezGene.GeneCommentary geneCommentary = parseGeneCommentary(child);
            gene.getPhenotypes().add(geneCommentary);
        }

        return gene;
    }

    private static EntrezGene parseEntrezgene_gene_node(Node node, EntrezGene gene) {
        if (!node.getNodeName().equals("Entrezgene_gene")) {
            throw new IllegalArgumentException("Node must be Entrezgene_gene");
        }
        String geneSymbol = XMLUtil.xPathString(node, "./Gene-ref/Gene-ref_locus/text()");
        gene.setGeneSymbol(geneSymbol);
        String fullName = XMLUtil.xPathString(node, "./Gene-ref/Gene-ref_desc/text()");
        gene.setGeneFullName(fullName);

        String locusTag = XMLUtil.xPathString(node, "./Gene-ref/Gene-ref_locus-tag/text()");
        gene.setLocusTag(locusTag);
        return gene;
    }

    private static EntrezGene.GeneCommentary parseGeneCommentary(Node node) {
        if (!node.getNodeName().equals("Gene-commentary")) {
            throw new IllegalArgumentException("Node name must be Gene-commentary");
        }
        EntrezGene.GeneCommentary ret = new EntrezGene.GeneCommentary();
        String text = XMLUtil.xPathString(node, "./Gene-commentary_text/text()");
        String type = XMLUtil.xPathString(node, "./Gene-commentary_type/@value");
        String heading = XMLUtil.xPathString(node, "./Gene-commentary_heading/text()");
        String label = XMLUtil.xPathString(node, "./Gene-commentary_label/text()");
        String accession = XMLUtil.xPathString(node, "./Gene-commentary_accession/text()");
        String version = XMLUtil.xPathString(node, "./Gene-commentary_version/text()");
        String pubmedId = XMLUtil.xPathString(node, "./Gene-commentary_refs/Pub/Pub_pmid/PubMedId/text()");


        if (text != null) {
            ret.setText(text);
        }
        if (type != null) {
            ret.setType(type);
        }
        if (heading != null) {
            ret.setHeading(heading);
        }
        if (label != null) {
            ret.setLabel(label);
        }
        if (accession != null) {
            ret.setAccession(accession);
        }
        if (version != null) {
            ret.setVersion(version);
        }
        if (pubmedId != null) {
            ret.setPubmedId(pubmedId);
        }

        return ret;
    }
}
