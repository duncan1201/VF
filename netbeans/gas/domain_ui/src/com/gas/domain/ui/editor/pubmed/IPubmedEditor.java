/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.pubmed;

import com.gas.domain.core.pubmed.PubmedArticle;

/**
 *
 * @author dq
 */
public interface IPubmedEditor {

    void setPubmedArticle(final PubmedArticle article);

    void setName(String name);

    PubmedArticle getPubmedArticle();
}
