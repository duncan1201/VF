/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public interface IPrintEditor {

    /**
     * creates image for printing
     */
    BufferedImage createImageForPrinting(PrintParam printParam);

    /**
     * creates image for exporting
     * @param visibleOnly null if it does not support Print-visible-Only feature
     */
    BufferedImage createImageForExporting(Boolean visibleOnly, int transparency);

    /**
     * @return Printable from a printable JComponent(JEditorPane, JTable).
     */
    Printable createFromPrintableJComponent(PrintParam printParam);

    /**
     * @return whether the selected component supports print functionality
     * natively
     */
    boolean isPrintableJComponent();

    boolean supportVisibleAreaOnly();

    int getTotalPages(PrintParam pf);

    /**
     * @return the JComponent that supports print functionality natively
     */
    JComponent getPrintableJComponent();

    String getJobName();
}
