/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.as.TranslationResult;
import com.gas.msa.ui.alignment.widget.MSAScroll;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.editor.ISequenceUI;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.common.ui.util.Pref;
import com.gas.msa.ui.alignment.ctrl.CtrlPanel;
import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class AlignPane extends JPanel implements ISequenceUI, IReclaimable {

    private CtrlPanel ctrlPanel;
    private MSAScroll msaScroll;
    private IColorProvider colorProvider;
    AlignPaneListeners.MSAPrefListener msaPrefListener;
    AlignPaneListeners.PrefListener prefListener;
    private MSA msa;
    private int zoom ;
    private static Insets padding;
    
    public AlignPane() {
        LayoutManager layout = new BorderLayout();
        setLayout(layout);

        msaScroll = new MSAScroll();
        add(msaScroll, BorderLayout.CENTER);

        ctrlPanel = new CtrlPanel();
        add(ctrlPanel, BorderLayout.EAST);

        hookupListeners();
    }
    
    public static Insets getPadding(){        
        if(padding == null){
            final FontMetrics fm = FontUtil.getDefaultFontMetrics();
            int charWidth = Math.round(fm.charWidth('W') * 1.3f);
            padding = new Insets(0,charWidth/2,0,charWidth);
        }
        return padding;
    }
    
    public void setZoom(int zoom){
        int old = this.zoom;
        this.zoom = zoom;
        firePropertyChange("zoom", old, this.zoom);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new AlignPaneListeners.PtyListener());
        prefListener = new AlignPaneListeners.PrefListener(this);
        Pref.CommonPtyPrefs.getInstance().addPropertyChangeListener(prefListener);
        msaPrefListener = new AlignPaneListeners.MSAPrefListener(this);
        MSAPref.getInstance().addPropertyChangeListener(msaPrefListener);
    }
    
    @Override
    public void cleanup(){
        Pref.CommonPtyPrefs.getInstance().removePropertyChangeListener(prefListener);
        MSAPref.getInstance().removePropertyChangeListener(msaPrefListener);
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    public MSA getMsa() {
        return msa;
    }

    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }

    public CtrlPanel getCtrlPane() {
        return ctrlPanel;
    }

    public MSAScroll getMsaScroll() {
        return msaScroll;
    }

    @Override
    public Map<Loc, String> getSelectedSeqs() {
        return null;
    }

    @Override
    public LocList getSelections() {
        return null;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public String getSequence() {
        return "";
    }


    @Override
    public void setTranslationResults(Set<TranslationResult> t) {
        
    }

    @Override
    public void setTranslationColorProvider(IColorProvider colorProvider) {
        
    }

    @Override
    public void setPrimarySeqColorProvider(IColorProvider colorProvider) {
        
    }

    @Override
    public void center(Loc loc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
