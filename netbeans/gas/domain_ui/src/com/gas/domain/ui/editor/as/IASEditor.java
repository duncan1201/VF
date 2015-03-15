/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.editor.IMolPane;

/**
 *
 * @author dq
 */
public interface IASEditor {

    void setAnnotatedSeq(AnnotatedSeq as);

    AnnotatedSeq getAnnotatedSeq();

    void setSelection(Loc loc);

    void setMinimapShown(Boolean shown);

    void setRulerFontSize(Float size);

    void setBaseFontSize(Float size);

    LocList getSelections();

    IMolPane getMolPane();
    
    void displayRENAnalysisPanel();

    void setCanSave();
    
    void refresh();
}
