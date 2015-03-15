/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pubmed.util;

import com.gas.common.ui.util.XMLUtil;
import com.gas.domain.core.pubmed.PubmedArticleSet;
import java.io.File;
import org.w3c.dom.Document;

/**
 *
 * @author dq
 */
public class PubmedArticleWriterService {

    public void write(PubmedArticleSet articleSet, File file) {
        Document doc = XMLUtil.newDoc();

        XMLUtil.transform(doc, file);
    }
}
