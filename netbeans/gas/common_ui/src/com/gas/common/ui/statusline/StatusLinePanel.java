/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.statusline;

import java.awt.*;
import java.lang.ref.WeakReference;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class StatusLinePanel extends JPanel {

    private WeakReference<JLabel> labelRef = null;
    Integer caretPos;
    String caretSeqName;
    Integer start;
    String startSeqName;
    Integer end;
    String endSeqName;
    Integer selecLength;

    public StatusLinePanel() {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel();
        this.labelRef = new WeakReference<JLabel>(label);
        add(this.labelRef.get(), c);

        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new StatusLinePanelListeners.PtyListener());
    }

    public JLabel getLeftMostLabel() {
        return labelRef.get();
    }

    public void setStartSeqName(String startSeqName) {
        String old = this.startSeqName;
        this.startSeqName = startSeqName;
        firePropertyChange("startSeqName", old, this.startSeqName);
    }

    public void setEndSeqName(String endSeqName) {
        String old = this.endSeqName;
        this.endSeqName = endSeqName;
        firePropertyChange("endSeqName", old, this.endSeqName);
    }

    public void setText(String txt) {
        labelRef.get().setText(txt);
    }
    
    public String getText(){
        return labelRef.get().getText();
    }

    public void setCaretPos(Integer caretPos) {
        Integer old = this.caretPos;
        this.caretPos = caretPos;
        firePropertyChange("caretPos", old, this.caretPos);
    }

    public void setCaretSeqName(String caretSeqName) {
        String old = this.caretSeqName;
        this.caretSeqName = caretSeqName;
        firePropertyChange("caretSeqName", old, this.caretSeqName);
    }

    public void setSelectLength(Integer selectLength) {
        Integer old = this.selecLength;
        this.selecLength = selectLength;
        firePropertyChange("selectLength", old, this.selecLength);
    }

    public void setStart(Integer start) {
        Integer old = this.start;
        this.start = start;
        firePropertyChange("start", old, this.start);
    }

    public void clearSelection() {
        setStart(null);
        setEnd(null);
        setSelectLength(null);
    }

    public void clear() {
        setCaretSeqName(null);
        setCaretPos(null);
        clearSelection();
    }

    public void setEnd(Integer end) {
        Integer old = this.end;
        this.end = end;
        firePropertyChange("end", old, this.end);
    }
}
