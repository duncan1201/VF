/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.button.PrevBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.predict.api.IPredictPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.lang.ref.WeakReference;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class PredictPanel extends JPanel implements IPredictPanel {

    WeakReference<ORFPanel> orfPanelRef;
    WeakReference<PrevBtn> prevBtnRef;
    FlatBtn rightBtn;
    TitledPanel titledPanel;
    WeakReference<ORFResultPanel> orfResultPanelRef;

    public static enum PAGE {

        SEARCH, RESULT
    };
    private PAGE page;

    public PredictPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setToolTipText("Open reading frames");
        titledPanel = new TitledPanel("Open Reading Frames");
        titledPanel.getContentPane().setLayout(new CardLayout());

        PrevBtn prevBtn = new PrevBtn();
        prevBtn.goPrivate();
        prevBtnRef = new WeakReference<PrevBtn>(prevBtn);
        titledPanel.setLeftDecoration(prevBtn);
        titledPanel.enableLeftDecoration(false);

        rightBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.PLAY_16));
        titledPanel.setRightDecoration(rightBtn);

        titledPanel.getContentPane().add(createSearchPanel(), PAGE.SEARCH.toString());
        titledPanel.getContentPane().add(createResultPanel(), PAGE.RESULT.toString());

        add(titledPanel, BorderLayout.CENTER);

        UIUtil.setBackground(titledPanel.getContentPane(), CNST.BG, JPanel.class);
    }

    @Override
    public void refresh() {
        IMolPane molPane = getMolPane();
        if(molPane == null || molPane.getAs() == null || getMolPane().getAs().getOrfResult() == null){
            return ;
        }        
        ORFResult or = getMolPane().getAs().getOrfResult();
        orfPanelRef.get().setOrfResult(or);
        orfResultPanelRef.get().setORF(or.getOrfSet().getOrfs());
        if (!or.getOrfSet().isEmpty()) {
            setPage(PredictPanel.PAGE.RESULT);
        } else {
            setPage(PredictPanel.PAGE.SEARCH);
        }
    }
    
    private IMolPane getMolPane(){
        IMolPane ret = UIUtil.getParent(this, IMolPane.class);
        return ret;
    }

    protected PrevBtn getPrevBtn() {
        return prevBtnRef.get();
    }

    private JPanel createSearchPanel() {
        ORFPanel orfPanel = new ORFPanel();
        orfPanelRef = new WeakReference<ORFPanel>(orfPanel);
        return orfPanel;
    }

    private ORFResultPanel createResultPanel() {
        ORFResultPanel ret = new ORFResultPanel();
        orfResultPanelRef = new WeakReference<ORFResultPanel>(ret);
        return ret;
    }

    public ORFResultPanel getORFResultPanel() {
        return orfResultPanelRef.get();
    }

    void setPage(PAGE page) {
        PAGE old = this.page;
        this.page = page;
        firePropertyChange("page", old, this.page);
    }

    public TitledPanel getTitledPanel() {
        return titledPanel;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new PredictPanelListeners.PtyListener());
        getPrevBtn().addActionListener(new PredictPanelListeners.PrevBtnListener(new WeakReference<PredictPanel>(this)));
        rightBtn.addActionListener(new PredictPanelListeners.GoBtnListener(orfPanelRef.get()));
    }

    ORFPanel getORFPanel() {
        return orfPanelRef.get();
    }

    @Override
    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.ORF_16);
    }
}
