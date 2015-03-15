/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.TopBracket;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class MockMolPane extends JPanel implements IMolPane {

    private AnnotatedSeq as;

    public MockMolPane() {
        setLayout(new BorderLayout());
    }
    
    @Override
    public void performRENAnalysis(){
    }

    public void setAs(AnnotatedSeq as) {
        this.as = as;
    }

    @Override
    public AnnotatedSeq getAs() {
        return as;
    }

    @Override
    public void setSelection(Loc loc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object> getShapeData(String trackName, Integer start, Integer end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.        
    }

    @Override
    public void setSelectedFeture(Feture feture) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.        
    }

    @Override
    public void setRmap(RMap rmap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh() {
    }

    @Override
    public Map<Loc, String> getSelectedSeqs() {
        Map<Loc, String> ret = new HashMap<Loc, String>();
        return ret;
    }

    @Override
    public LocList getSelections() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCircular() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSequence() {
        return as.getSiquence().getData();
    }

    @Override
    public void setTranslationResults(Set<TranslationResult> t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTranslationColorProvider(IColorProvider colorProvider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPrimarySeqColorProvider(IColorProvider colorProvider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void center(Loc loc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectedSite(RMap.EntryList entries) {}

    @Override
    public void setSelectedSite(TopBracket tp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearSelectedSites() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isShowingRENResultPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
