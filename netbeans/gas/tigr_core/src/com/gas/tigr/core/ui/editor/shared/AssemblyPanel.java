/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tasm.Condig;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class AssemblyPanel extends JPanel {
    private Condig condig;
    AssemblyScroll assemblySPane;
    Font baseFont;
    
    public AssemblyPanel(){
        LayoutManager layout = null;
        layout = new BorderLayout();
        setLayout(layout);
        
        assemblySPane = new AssemblyScroll();
        add(assemblySPane, BorderLayout.CENTER);
        
        addPropertyChangeListener(new AssemblyPanelListeners.PtyListener());        
    }
    
    public void setBaseFont(Font baseFont){
        Font old = this.baseFont;
        this.baseFont = baseFont;
        firePropertyChange("baseFont", old, this.baseFont);
    }
    
    @Deprecated
    public void zoom(){
        Dimension mSize = assemblySPane.getMainComp().getSize();
       
        Dimension pSize = new Dimension(mSize);
        pSize.width = pSize.width + pSize.width * 3;
        UIUtil.setPreferredWidth(assemblySPane.getMainComp(), mSize.width * 2);
        
        ColumnHeaderView columnHeaderView = assemblySPane.getColumnHeaderView();
        UIUtil.setPreferredWidth(columnHeaderView, mSize.width * 2);
        
        columnHeaderView.revalidate();
        columnHeaderView.repaint();
        
        //assemblySPane.getMainPanel().setPreferredSize(pSize);
        assemblySPane.getMainComp().revalidate();
        assemblySPane.getMainComp().repaint();
      
    }
    
    public void setCondig(Condig condig){
        Condig old = this.condig;
        this.condig = condig;
        firePropertyChange("condig", old, this.condig);
    }

    public Condig getCondig() {
        return condig;
    }

    public AssemblyScroll getAssemblySPane() {
        return assemblySPane;
    }
    
    
}
