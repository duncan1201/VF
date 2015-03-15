/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 *
 * @author adm_LiaoDu
 */
public class HighlightCaret extends DefaultCaret {
    private static Color color = new Color(200, 200, 230);
    private static final Highlighter.HighlightPainter unfocusedPainter = new DefaultHighlighter.DefaultHighlightPainter(color);
    private static final Highlighter.HighlightPainter focusedPainter = new DefaultHighlighter.DefaultHighlightPainter(color);
    private static final long serialVersionUID = 1L;
    private boolean isFocused;

    @Override
    protected Highlighter.HighlightPainter getSelectionPainter() {
        setBlinkRate(500); // otherwise is disabled, stopped
        return isFocused ? focusedPainter/*super.getSelectionPainter()*/ : unfocusedPainter;
    }

    @Override
    public void setSelectionVisible(boolean hasFocus) {
        if (hasFocus != isFocused) {
            isFocused = hasFocus;
            super.setSelectionVisible(false);
            super.setSelectionVisible(true);
        }
    }    
}
