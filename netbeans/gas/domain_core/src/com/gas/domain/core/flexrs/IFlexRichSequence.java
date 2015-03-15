package com.gas.domain.core.flexrs;

import java.util.Map;
import java.util.Set;

import org.biojavax.bio.seq.RichSequence;

public interface IFlexRichSequence extends RichSequence {

    Set<String> getKeywords();

    void setKeywords(Set<String> keywords);

    Map<String, String> getDbsources();

    void setDbsources(Map<String, String> dbsources);

    void setLocus(String locus);

    String getLocus();
}
