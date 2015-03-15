/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class JRNL implements Cloneable {

    public final static String RECORD_NAME = "JRNL ";
    private final static String SUB_R_AUTH = "AUTH";
    private final static String SUB_R_TITL = "TITL";
    private final static String SUB_R_DOI = "DOI";
    private final static String SUB_R_PMID = "PMID";
    private transient String title;
    private transient String doi;
    private Integer hibernateId;
    private Set<Element> elements = new HashSet<Element>();

    @Override
    public JRNL clone() {
        JRNL ret = new JRNL();
        ret.setElements(CommonUtil.copyOf(elements));
        return ret;
    }

    public Set<Element> getElements() {
        return elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }
    /*
     COLUMNS DATA TYPE FIELD DEFINITION
     --------------------------------------------------------------------------------
     1 - 6 Record name "JRNL "
     13 - 16 LString(4) "DOI "
     PDB File Format v. 3.3
     Page 46
     20 – 79 LString continuation Unique DOI assigned to the publication
     describing the experiment.
     Allows for a long DOI string.     
     */

    public String getDoi() {
        if (doi == null) {
            StringBuilder builder = new StringBuilder();
            List<Element> els = new ArrayList<Element>(elements);
            Collections.sort(els, new Sorter());
            Iterator<Element> itr = els.iterator();
            while (itr.hasNext()) {
                Element e = itr.next();
                String text = e.getText().trim();
                if (text.startsWith(SUB_R_DOI)) {

                    //20-79->8-67
                    String str = StrUtil.substring(text, 8, null, String.class, false, false);
                    builder.append(str);
                }
            }
            doi = builder.toString();
        }
        return doi;
    }

    /*
     COLUMNS DATA TYPE FIELD DEFINITION
     -----------------------------------------------------------------------------------
     1 - 6 Record name "REMARK"
     10 LString(1) "1"
     13 - 16 LString(4) "TITL" Appears on all continuation records.
     17 - 18 Continuation continuation Permits long titles.
     20 - 79 LString title Title of the article.    
     * 
     */
    public String getTitle() {
        if (title == null || title.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            List<Element> els = new ArrayList<Element>(elements);
            Collections.sort(els, new Sorter());
            Iterator<Element> itr = els.iterator();
            while (itr.hasNext()) {
                Element e = itr.next();
                String text = e.getText().trim();
                if (text.startsWith(SUB_R_TITL)) {

                    //20-79->8-67
                    String str = StrUtil.substring(text, 8, null, String.class, false, false);
                    builder.append(str);
                }
            }
            title = builder.toString();
        }
        return title;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        List<Element> eles = new ArrayList<Element>(elements);
        Collections.sort(eles, new Sorter());
        Iterator<Element> itr = eles.iterator();
        while (itr.hasNext()) {
            Element e = itr.next();
            ret.append(e.toString());
        }
        return ret.toString();
    }

    /*
     Record Format
     COLUMNS DATA TYPE FIELD DEFINITION
     -----------------------------------------------------------------------
     1 - 6 Record name "JRNL "
     13 - 79 LString text See Details below.     
     */
    public static class Element implements Cloneable {

        private Integer hibernateId;
        private String text;

        @Override
        public Element clone() {
            Element ret = new Element();
            ret.setText(text);
            return ret;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void parse(String line) {
            text = StrUtil.substring(line, 13, 79, String.class, false, false);
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
            StrUtil.replace(ret, text, 13, 79, true);
            StrUtil.replace(ret, '\n', 81, 81, true);
            return ret.toString();
        }
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            String[] splits1 = o1.getText().split(" ");
            String[] splits2 = o2.getText().split(" ");
            ret = splits1[0].compareTo(splits2[0]);
            if (ret == 0) {
                if (splits1.length < 3) {
                    ret = -1;
                } else if (splits2.length < 3) {
                    ret = 1;
                } else {
                    Integer c1 = CommonUtil.parseInt(splits1[1]);
                    Integer c2 = CommonUtil.parseInt(splits2[1]);
                    if (c1 == null) {
                        ret = -1;
                    } else if (c2 == null) {
                        ret = 1;
                    } else {
                        ret = c1.compareTo(c2);
                    }
                }
            }

            return ret;
        }
    }
}
