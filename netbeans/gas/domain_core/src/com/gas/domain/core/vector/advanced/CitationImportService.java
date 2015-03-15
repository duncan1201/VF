/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.vector.advanced.api.ICitationImportService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class CitationImportService implements ICitationImportService {

    private static final String DATA_START = "obj|";
    private static final String PMID_REGEX = "<PMID>(.+)</PMID>";
    private static final String ARTICLE_TITLE_REGEX = "<ArticleTitle>(.+)</ArticleTitle>";
    private static final String ABSTRACT_TXT_REGEX = "<AbstractText>(.+)</AbstractText>";
    private static final String JOURNAL_TITLE_REGEX = "<MedlineTA>(.+)</MedlineTA>";
    private static final String JOURNAL_YEAR_REGEX = "<JournalIssue>.+<PubDate>.+<Year>(.+)</Year>.+</PubDate>.+</JournalIssue>";
    private static final String JOURNAL_VOLUME_REGEX = "<JournalIssue>.+<Volume>(.+)</Volume>.+<JournalIssue>";
    private static final String PAGINATION_REGEX = "<Pagination>.+<MedlinePgn>(.+)</MedlinePgn>.+</Pagination>";

    public PubmedArticle receive(File file) {
        PubmedArticle ret = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            ret = receive(inputStream);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public PubmedArticle receive(InputStream inputStream) {
        PubmedArticle ret = new PubmedArticle();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(DATA_START)) {
                    String matched = null;

                    // extract PMID
                    matched = StrUtil.extract(PMID_REGEX, line);
                    if (matched != null) {
                        ret.setPmid(matched);
                    }

                    // extract abstract title
                    matched = StrUtil.extract(ARTICLE_TITLE_REGEX, line);
                    if (matched != null) {
                        ret.setTitle(matched);
                    }

                    // extract abstract text
                    matched = StrUtil.extract(ABSTRACT_TXT_REGEX, line);
                    if (matched != null) {
                        ret.setAbstractTxt(matched);
                    }

                    // extract journal title
                    matched = StrUtil.extract(JOURNAL_TITLE_REGEX, line);
                    if (matched != null) {
                        ret.setJournalTitle(matched);
                    }

                    // extract JOURNAL_YEAR_REGEX
                    matched = StrUtil.extract(JOURNAL_YEAR_REGEX, line);
                    if (matched != null) {
                        ret.setPubYear(matched);
                    }

                    // extract JOURNAL_VOLUME_REGEX
                    matched = StrUtil.extract(JOURNAL_VOLUME_REGEX, line);
                    if (matched != null) {
                        ret.setVolume(matched);
                    }

                    // extract PAGINATION_REGEX
                    matched = StrUtil.extract(PAGINATION_REGEX, line);
                    if (matched != null) {
                        ret.setPagination(matched);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        } finally {
            return ret;
        }
    }
}
