/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJPanel.java
 *
 * Created on Nov 24, 2011, 4:06:57 PM
 */
package com.gas.enzyme.core.ui;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.button.PrevBtn;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.ren.api.IRENAnalysisPanel;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.IRENListService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.ren.RENSet;
import java.awt.BorderLayout;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dunqiang
 */
@ServiceProvider(service = IRENAnalysisPanel.class)
public class RENAnalPanel extends JPanel implements IRENAnalysisPanel {

    private IRENListService renListService = null;
    RENAnalView renAnalysisView;
    RMap rmap;
    private WeakReference<TitledPanel> titledPanelRef;
    WeakReference<PrevBtn> prevBtnRef;
    FlatBtn rightDecBtn;
    private Integer totalLength;

    @Override
    public RENSet getSelectedRENs() {
        return renAnalysisView.getEnzymesUsedPanel().getSelectedRENs();
    }

    @Override
    public RENList getSelectedRENList() {
        return renAnalysisView.getEnzymesUsedPanel().getSelectedRENList();
    }

    public enum PAGE {

        SEARCH, RESULT
    };
    private PAGE page;

    /**
     * Creates new form NewJPanel
     */
    public RENAnalPanel() {
        initService();
        initComponents();
        hookupListeners();
    }

    public void setTotalLength(Integer totalLength) {
        Integer old = this.totalLength;
        this.totalLength = totalLength;
        firePropertyChange("totalLength", old, this.totalLength);
    }
    
    public void performAnalysis(){
        rightDecBtn.doClick();
    }

    public RENAnalView getRenAnalysisView() {
        return renAnalysisView;
    }

    protected void setPage(RENAnalPanel.PAGE page) {
        PAGE old = this.page;
        this.page = page;
        firePropertyChange("page", old, this.page);
    }

    public boolean isShowingResultPage() {
        return page == PAGE.RESULT;
    }
    
    @Override
    public boolean isAnywhere() {
        return renAnalysisView.getCutSitesPanel().isAnywhereEnabled();
    }

    @Override
    public boolean isMustCut() {
        return renAnalysisView.getCutSitesPanel().isMustCutEnabled();
    }

    @Override
    public boolean isMustNotCut() {
        return renAnalysisView.getCutSitesPanel().isMustNotCutEnabled();
    }

    private void initService() {
        IHibernateConfigService configService = Lookup.getDefault().lookup(IHibernateConfigService.class);
        renListService = Lookup.getDefault().lookup(IRENListService.class);
        renListService.setConfig(configService.getDefaultConfiguration());
    }

    private void initComponents() {
        setToolTipText("Restriction Analysis");
        
        TitledPanel titledPanel = new TitledPanel("Restriction Analysis");
        titledPanelRef = new WeakReference<TitledPanel>(titledPanel);
        setLayout(new java.awt.BorderLayout());

        titledPanel.getContentPane().setLayout(new BorderLayout());

        PrevBtn prevBtn = new PrevBtn();
        prevBtnRef = new WeakReference<PrevBtn>(prevBtn);
        titledPanel.setLeftDecoration(prevBtn);
        prevBtn.goPrivate();

        rightDecBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.PLAY_16));
        rightDecBtn.setToolTipText("Start restriction analysis");
        titledPanel.setRightDecoration(rightDecBtn);

        renAnalysisView = new RENAnalView();
        titledPanel.getContentPane().add(renAnalysisView, BorderLayout.CENTER);

        add(titledPanel, java.awt.BorderLayout.CENTER);
        titledPanel.setBorder(BorderFactory.createEmptyBorder());

        UIUtil.setBackground(titledPanel.getContentPane(), CNST.BG, JPanel.class);
    }

    @Override
    public void enableLeftDecoration(boolean enable) {
        titledPanelRef.get().enableLeftDecoration(enable);
    }

    private void hookupListeners() {
        rightDecBtn.addActionListener(new RENAnalPanelListeners.FindBtnListener(this));
        prevBtnRef.get().addActionListener(new RENAnalPanelListeners.PrevBtnListener());
        addPropertyChangeListener(new RENAnalPanelListeners.PtyListener());
    }

    public String getChildName() {
        return "Enzyme";
    }

    @Override
    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.CLONING_16);
    }

    @Override
    public Integer getMinOccurrence() {
        return renAnalysisView.getCutSitesPanel().getMatchMin();
    }

    @Override
    public Integer getMaxOccurrence() {
        return renAnalysisView.getCutSitesPanel().getMatchMax();
    }

    public void refresh() {
        if (rmap == null) {
            return;
        }
        renAnalysisView.getCutSitesPanel().setInputParams(rmap.getInputParams());
        renAnalysisView.getEnzymesUsedPanel().setInputParams(rmap.getInputParams());
        renAnalysisView.getSitesFoundPanel().setRMap(rmap);
        renAnalysisView.setSiteCount(rmap.getSize());
        if(rmap.getEntries().isEmpty()){
            setPage(RENAnalPanel.PAGE.SEARCH);
        }else{
            setPage(RENAnalPanel.PAGE.RESULT);
        }
    }

    @Override
    public void setRmap(RMap rmap) {
        RMap old = this.rmap;
        this.rmap = rmap;
        firePropertyChange("rmap", old, this.rmap);
    }

    @Override
    public Integer getMatchMin() {
        Integer ret = renAnalysisView.getCutSitesPanel().getMatchMin();
        return ret;
    }

    @Override
    public Integer getMatchMax() {
        Integer ret = renAnalysisView.getCutSitesPanel().getMatchMax();
        return ret;
    }

    @Override
    public Integer getMustCutFrom() {
        Integer ret = renAnalysisView.getCutSitesPanel().getMustCutFrom();
        return ret;
    }

    @Override
    public Integer getMustCutTo() {
        Integer ret = renAnalysisView.getCutSitesPanel().getMustCutTo();
        return ret;
    }

    @Override
    public Integer getMustNotCutFrom() {
        Integer ret = renAnalysisView.getCutSitesPanel().getMustNotCutFrom();
        return ret;
    }

    @Override
    public Integer getMustNotCutTo() {
        Integer ret = renAnalysisView.getCutSitesPanel().getMustNotCutTo();
        return ret;
    }
}