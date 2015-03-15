/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tigr.Kromatogram;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
public class ChromatogramPanel extends JPanel {
    
    Kromatogram kromatogram;
    ChromatogramComp chromatogramComp;
    RowHeaderUI rowHeaderUI;
    JScrollPane scrollPane;
    
    public ChromatogramPanel(){
        initComp();
        hookupListeners();
    }
    
    void setKromatogram(Kromatogram kromatogram){
        Kromatogram old = this.kromatogram;
        this.kromatogram = kromatogram;
        firePropertyChange("kromatogram", old, this.kromatogram);
    }
    
    final void initComp(){
        setLayout(new BorderLayout());
        rowHeaderUI = new RowHeaderUI();
        UIUtil.setPreferredWidth(rowHeaderUI, 20);
        
        chromatogramComp = new ChromatogramComp();
        scrollPane = new JScrollPane(chromatogramComp);
        scrollPane.setRowHeaderView(rowHeaderUI);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    final void hookupListeners(){
        addPropertyChangeListener(new ChromatogramPanelListeners.PtyListener());
    }

    public ChromatogramComp getChromatogramComp() {
        return chromatogramComp;
    }

    public RowHeaderUI getRowHeaderUI() {
        return rowHeaderUI;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }    
        
}
