/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.color.IColorProvider;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.ren.RMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dq
 */
public interface ISequenceUI {

    Map<Loc, String> getSelectedSeqs();

    LocList getSelections();

    boolean isCircular();

    String getSequence();

    void setTranslationResults(Set<TranslationResult> t);

    void setTranslationColorProvider(IColorProvider colorProvider);

    void setPrimarySeqColorProvider(IColorProvider colorProvider);

    void center(Loc loc);

}
