/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import java.awt.BorderLayout;
import java.lang.ref.WeakReference;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXTitledSeparator;

/**
 *
 * @author dq
 */
public class PreviewPanel extends JPanel {

    private PreviewPage previewPage;

    public PreviewPanel() {
        setLayout(new BorderLayout());

        add(new JXTitledSeparator("Preview"), BorderLayout.NORTH);

        previewPage = new PreviewPage();
        JScrollPane scrollPane = new JScrollPane(previewPage);        
        add(scrollPane, BorderLayout.CENTER);
    }

    PreviewPage getPreviewPage() {
        return previewPage;
    }
}