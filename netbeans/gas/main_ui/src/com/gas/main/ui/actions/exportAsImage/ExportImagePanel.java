/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class ExportImagePanel extends JPanel {

    private WeakReference<CtrlPanel> ctrlPanelRef;
    private WeakReference<IPrintEditor> editorRef;
    private WeakReference<PreviewPanel> previewPanelRef;

    ExportImagePanel(IPrintEditor editor) {
        editorRef = new WeakReference<IPrintEditor>(editor);
        initComponents();
    }

    private void initComponents() {
        Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        setLayout(new BorderLayout());

        CtrlPanel ctrlPanel = new CtrlPanel(editorRef.get());
        ctrlPanelRef = new WeakReference<CtrlPanel>(ctrlPanel);
        add(ctrlPanel, BorderLayout.NORTH);

        PreviewPanel previewPanel = new PreviewPanel();
        previewPanelRef = new WeakReference<PreviewPanel>(previewPanel);
        add(previewPanel, BorderLayout.CENTER);
        BufferedImage image = editorRef.get().createImageForExporting(isVisibleOnly(), Transparency.TRANSLUCENT);

        previewPanel.getPreviewPage().setImage(image);
        previewPanel.getPreviewPage().setScale(ctrlPanel.getScale().floatValue());
    }

    Boolean isVisibleOnly() {
        if (ctrlPanelRef.get().getVisibleOnlyRef() != null) {
            return ctrlPanelRef.get().getVisibleOnlyRef().get().isSelected();
        } else {
            return null;
        }
    }

    protected String getSelectedFormat() {
        return ctrlPanelRef.get().getSelectedFormat();
    }

    protected PreviewPanel getPreviewPanel() {
        return previewPanelRef.get();
    }

    protected IPrintEditor getPrintEditor() {
        return editorRef.get();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
        int height = Math.round(sizeScreen.height * 0.77f);
        int width = Math.round(sizeScreen.width * 0.77f);
        ret.setSize(width, height);
        return ret;
    }
}
