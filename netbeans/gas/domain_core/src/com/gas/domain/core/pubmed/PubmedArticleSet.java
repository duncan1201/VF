package com.gas.domain.core.pubmed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PubmedArticleSet {

    private Set<PubmedArticle> articles = new HashSet<PubmedArticle>();

    public Set<PubmedArticle> getArticles() {
        return articles;
    }

    public PubmedArticle getArticle(String accession) {
        PubmedArticle ret = null;
        Iterator<PubmedArticle> itr = articles.iterator();
        while (itr.hasNext()) {
            PubmedArticle article = itr.next();
            if (article.getPmid().equalsIgnoreCase(accession)) {
                ret = article;
                break;
            }
        }
        return ret;
    }

    public List<PubmedArticle> getArticleList() {
        List<PubmedArticle> ret = new ArrayList<PubmedArticle>();
        ret.addAll(articles);
        return ret;
    }

    public void setArticles(Set<PubmedArticle> articles) {
        this.articles = articles;
    }
}
