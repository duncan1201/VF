/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

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
    
    private WeakReference<PreviewPage> previewPageRef;
    
    public PreviewPanel(){
        setLayout(new BorderLayout());
        
        add(new JXTitledSeparator("Preview"));
                
        PreviewPage previewPage = new PreviewPage();
        previewPageRef = new WeakReference<PreviewPage>(previewPage);
        JScrollPane scrollPane = new JScrollPane(previewPage);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    protected PreviewPage getPreviewPage(){
        return previewPageRef.get();
    }
        
}
