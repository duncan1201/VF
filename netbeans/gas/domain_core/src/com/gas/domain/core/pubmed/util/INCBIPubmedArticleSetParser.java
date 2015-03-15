/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pubmed.util;

import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.PubmedArticleSet;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dunqiang
 */
public interface INCBIPubmedArticleSetParser {

    PubmedArticleSet parse(File file);

    PubmedArticleSet parse(String xmlContents, boolean includeAbstractText);

    PubmedArticleSet parse(String xmlContents);

    <T> List<T> parse(String xmlStr, Class<T> retType);

    <T> List<T> parse(String xmlStr, boolean includeAbstractText, Class<T> retType);

    PubmedArticleSet parse(Class clazz, String resource);

    PubmedArticleSet parse(InputStream inputStream);

    PubmedArticle singleParse(Class clazz, String resource);

    PubmedArticle singleParse(String xmlString);

    PubmedArticle singleParse(InputStream inputStream);

    PubmedArticle singleParse(File file);
}
