/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.ren.RMap;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IMolPane extends ISequenceUI {

    AnnotatedSeq getAs();

    void setSelection(Loc loc);

    void setRmap(RMap rmap);

    void refresh();

    List<Object> getShapeData(String trackName, Integer start, Integer end);
    
    /**
     * Select & center the feature
     */
    void setSelectedFeture(Feture feture);
    void clearSelectedSites();
    void setSelectedSite(TopBracket tp);
    void setSelectedSite(RMap.EntryList entries);
    void performRENAnalysis();
    boolean isShowingRENResultPage();
}
