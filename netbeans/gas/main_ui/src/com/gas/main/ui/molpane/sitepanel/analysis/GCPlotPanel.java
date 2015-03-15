/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.analysis;

import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Pref;
import com.gas.domain.core.gc.api.GCResult;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**

 @author dq
 */
public class GCPlotPanel extends JPanel {

    private JCheckBox visibleBox;
    private WeakReference<JSpinner> windowSpinnerRef;
    private JSpinner heightSpinner;
    private GCResult gcResult;
    boolean populatingUI;
    
    public GCPlotPanel() {
        initComponents();
        hookupListeners();
    }
    
    private void hookupListeners(){
        windowSpinnerRef.get().addChangeListener(new GCPlotPanelListeners.WindowSizeListener());
        visibleBox.addItemListener(new GCPlotPanelListeners.VisibleListener(this));
        heightSpinner.addChangeListener(new GCPlotPanelListeners.HeightListener());
        addPropertyChangeListener(new GCPlotPanelListeners.PtyListener());
    }
    
    private void initComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints c;
                       
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel gcPlots = createGCPlots();
        
        CollapsibleTitlePanel collapsibleTitlePanel = new CollapsibleTitlePanel("GC Plot");
        visibleBox = new JCheckBox();
        collapsibleTitlePanel.setRightDecoratioin(visibleBox);         
        collapsibleTitlePanel.getContentPane().add(gcPlots, BorderLayout.CENTER);
        
        add(collapsibleTitlePanel, c);    
    }

    public void setGcResult(GCResult gcResult) {
        GCResult old = this.gcResult;
        this.gcResult = gcResult;
        firePropertyChange("gcResult", old, this.gcResult);
    }    
    
    void populateUI(){
        populatingUI = true;
        if(gcResult == null){
            return;
        }
        visibleBox.setSelected(!gcResult.isEmpty());
        windowSpinnerRef.get().setValue(gcResult.getWindowSize());
        
        populatingUI = false;
    }
    
    private JPanel createGCPlots(){
        JPanel ret = new JPanel();
        ret.setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;     

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel b = createGcWindowSizePanel();
        ret.add(b, c);
        
        return ret;
    }
    
    public JSpinner getHeightSpinner(){
        return heightSpinner;
    }
    
    JSpinner getWindowSizeSpinner(){
        return windowSpinnerRef.get();
    }
    
    JCheckBox getVisibleBox(){
        return visibleBox;
    }
    
    private JPanel createGcWindowSizePanel(){
        JPanel ret = new JPanel();
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;                
        ret.add(new JLabel("Window Size:"), c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;         
        JSpinner windowSpinner = new JSpinner();
        windowSpinnerRef = new WeakReference<JSpinner>(windowSpinner);
        Dimension size = UIUtil.getSize(1000, JSpinner.class);
        UIUtil.setPreferredWidth(windowSpinner, size.width);
        windowSpinner.setModel(new SpinnerNumberModel(5, 3, Integer.MAX_VALUE, 2));
        ret.add(windowSpinner, c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Height:"), c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        heightSpinner = new JSpinner();
        size = UIUtil.getSize(1000, JSpinner.class);
        UIUtil.setPreferredWidth(heightSpinner, size.width);
        int gcHeight = Pref.CommonPtyPrefs.getInstance().getGCHeight();
        heightSpinner.setModel(new SpinnerNumberModel(gcHeight, 10, 400, 2));
        ret.add(heightSpinner, c); 
        
        return ret;
    }
    
    
}
