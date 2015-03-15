package com.gas.domain.core.pubmed;

import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PubmedArticle implements Cloneable, IFolderElement {

    private String hibernateId;
    private Folder folder;
    private Date dateCreated;
    private Date lastModifiedDate;
    private String pubYear;
    private String volume;
    private String journalTitle;
    private String title;
    private String abstractTxt;
    private String pmid;
    private String pagination;
    private Map<String, String> articleIdList = new HashMap<String, String>();
    private List<Author> authors = new ArrayList<Author>();
    private String ISOAbbreviation;
    private String prevFolderPath;
    private boolean read;

    public PubmedArticle() {
    }

    @Override
    public boolean isRead() {
        return read;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void setName(String name) {
        setTitle(name);
    }

    @Override
    public PubmedArticle clone() {
        PubmedArticle ret = new PubmedArticle();
        ret.setAbstractTxt(getAbstractTxt());
        Iterator<String> itr = getArticleIdList().keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String value = getArticleIdList().get(key);
            ret.getArticleIdList().put(key, value);
        }
        Iterator<Author> aItr = getAuthors().iterator();
        while (aItr.hasNext()) {
            Author author = aItr.next();
            ret.getAuthors().add(author.clone());
        }

        ret.setDateCreated(getDateCreated());
        ret.setFolder(getFolder());
        ret.setISOAbbreviation(getISOAbbreviation());
        ret.setJournalTitle(getJournalTitle());
        ret.setPagination(getPagination());
        ret.setPmid(getPmid());
        ret.setPubYear(getPubYear());
        ret.setTitle(getTitle());
        ret.setVolume(getVolume());

        return ret;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractTxt() {
        return abstractTxt;
    }

    public void setAbstractTxt(String abstractTxt) {
        this.abstractTxt = abstractTxt;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getISOAbbreviation() {
        return ISOAbbreviation;
    }

    public void setISOAbbreviation(String iSOAbbreviation) {
        ISOAbbreviation = iSOAbbreviation;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o == this) {
                return true;
            } else if (o instanceof PubmedArticle) {
                PubmedArticle another = (PubmedArticle) o;
                return another.getPmid().equals(getPmid());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getPmid().hashCode();
    }

    @Override
    public String getPrevFolderPath() {
        return prevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String p) {
        this.prevFolderPath = p;
    }

    @Override
    public void setDesc(String desc) {
    }

    @Override
    public String getDesc() {
        return null;
    }

    public static class Author implements Cloneable {

        private String lastName;
        private String foreName;
        private String initials;

        @Override
        public Author clone() {
            Author ret = new Author();
            ret.setForeName(foreName);
            ret.setInitials(initials);
            ret.setLastName(lastName);
            return ret;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getForeName() {
            return foreName;
        }

        public void setForeName(String foreName) {
            this.foreName = foreName;
        }

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }
    }

    public Map<String, String> getArticleIdList() {
        return articleIdList;
    }

    public String getDoi() {
        return articleIdList.get("doi");
    }

    public void setArticleIdList(Map<String, String> articleIdList) {
        this.articleIdList = articleIdList;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
