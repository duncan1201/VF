/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.ren.api;

import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import javax.swing.ImageIcon;

/**
 *
 * @author dq
 */
public interface IRENAnalysisPanel {

    boolean isAnywhere();

    boolean isMustCut();

    boolean isMustNotCut();

    Integer getMinOccurrence();

    Integer getMaxOccurrence();

    void setRmap(RMap rmap);

    Integer getMatchMin();

    Integer getMatchMax();

    Integer getMustCutFrom();

    Integer getMustCutTo();

    Integer getMustNotCutFrom();

    Integer getMustNotCutTo();

    ImageIcon getImageIcon();

    void enableLeftDecoration(boolean enable);
    
    RENSet getSelectedRENs();
    
    RENList getSelectedRENList();
}
