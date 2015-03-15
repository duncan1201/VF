/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutPanel;
import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.ui.primer3.api.IPrimer3Panel;
import com.gas.main.ui.molpane.MolPane;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class Primer3Panel extends JPanel implements IPrimer3Panel, IReclaimable {

    enum PAGE { INPUT, OUTPUT };
    PAGE page;
    private P3Output p3output;
    private Primer3View primer3View;
    JButton rightBtn;
    JButton leftBtn;
    TitledPanel titledPanel;
    OutPanel outPanel;

    public Primer3Panel() {
        super();
        initComponents();

        hookupListeners();
    }

    private void hookupListeners() {
        leftBtn.addActionListener(new Primer3PanelListeners.LeftBtnListener());
        rightBtn.addActionListener(new Primer3PanelListeners.PickPrimerBtnListener());
        addPropertyChangeListener(new Primer3PanelListeners.PtyListener());
    }

    @Override
    public void cleanup() {
    }

    void populateUI() {
        if(p3output != null){
            primer3View.populateUI(p3output.getUserInput());
            outPanel.setP3output(p3output);        
        }
    }
    
    public void refresh(){
        
        outPanel.refresh();
        if(outPanel.p3output.getOligos().isEmpty()){
            setPage(PAGE.INPUT);
        }else{
            setPage(PAGE.OUTPUT);    
        }
    }

    List<String> validateInput() {
        MolPane molPane = UIUtil.getParent(this, MolPane.class);
        AnnotatedSeq as = molPane.getAs();
        List<String> ret = new ArrayList<String>();
        ret.addAll(primer3View.validateInput(as));
        return ret;
    }

    void updateUserInputFromUI() {
        primer3View.updateUserInputFromUI();
    }

    private void initComponents() {
        setToolTipText("Primer3");
        GridBagConstraints c;

        primer3View = new Primer3View();

        titledPanel = new TitledPanel("Primer3");

        setLayout(new java.awt.BorderLayout());

        leftBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.GEAR_16));
        leftBtn.setActionCommand(Primer3PanelListeners.SettingsListener.class.getName());
        titledPanel.setLeftDecoration(leftBtn);        

        rightBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.PLAY_16));

        titledPanel.setRightDecoration(rightBtn);
        titledPanel.getContentPane().setLayout(new CardLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        titledPanel.getContentPane().add(primer3View, PAGE.INPUT.toString());

        outPanel = new OutPanel();
        titledPanel.getContentPane().add(outPanel, PAGE.OUTPUT.toString());


        add(titledPanel, BorderLayout.CENTER);
        titledPanel.setBorder(BorderFactory.createEmptyBorder());
    }

    void setPage(PAGE page) {
        PAGE old = this.page;
        this.page = page;
        firePropertyChange("page", old, this.page);
    }

    public void setP3output(P3Output p3output) {
        P3Output old = this.p3output;
        this.p3output = p3output;
        firePropertyChange("p3output", old, this.p3output);
    }
    
    public P3Output getP3output(){
        return p3output;
    }   

    public String getChildName() {
        return "Primer3";
    }

    @Override
    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.PRIMER_16);
    }

    protected Primer3View getPrimer3View() {
        return primer3View;
    }
}
