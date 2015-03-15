/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import com.gas.main.ui.editor.as.ASEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.lang.ref.WeakReference;
import javax.print.attribute.standard.MediaSize;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class PrintPanel extends JPanel {

    private WeakReference<IPrintEditor> printEditorRef;
    private WeakReference<PreviewPage> previewPageRef;
    private WeakReference<CtrlPanel> ctrlPanelRef;
    private WeakReference<PreviewPanel> previewPanelRef;
    private PrintParam printParam;

    PrintPanel(IPrintEditor editor, PrintParam printParam) {
        initComponents(editor, printParam);
        initValues();
        hookupListeners();
    }

    private void initValues() {
        previewPanelRef.get().getPreviewPage().setPrintParam(printParam);
    }

    private void hookupListeners() {
    }
    
    CtrlPanel getCtrlPanel(){
        return ctrlPanelRef.get();
    }

    private void initComponents(IPrintEditor editor, PrintParam printParam) {
        Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));

        int totalPages = editor.getTotalPages(printParam);
        printParam.setPageCount(totalPages);
        printParam.setPageNo(1);
        printEditorRef = new WeakReference<IPrintEditor>(editor);
        this.printParam = printParam;


        setLayout(new BorderLayout());

        CtrlPanel ctrlPanel = new CtrlPanel(editor);
        ctrlPanel.setPrintParam(printParam);
        ctrlPanelRef = new WeakReference<CtrlPanel>(ctrlPanel);
        add(ctrlPanel, BorderLayout.NORTH);

        PreviewPanel previewPanel = new PreviewPanel();
        previewPageRef = new WeakReference<PreviewPage>(previewPanel.getPreviewPage());
        previewPanelRef = new WeakReference<PreviewPanel>(previewPanel);
        add(previewPanel, BorderLayout.CENTER);
    }

    IPrintEditor getPrintEditor() {
        return printEditorRef.get();
    }

    PrintParam getPrintParam() {
        return printParam;
    }

    void setPrintParam(PrintParam printParam) {
        PrintParam old = this.printParam;
        this.printParam = printParam;
        firePropertyChange("printParam", old, this.printParam);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();       
        double heightPreferred = size.getHeight() * 0.73;
        float widthInch = MediaSize.ISO.A4.getSize(MediaSize.INCH)[0];
        float width_A4 = UIUtil.inch2Pixels(widthInch);
        double widthPreferred = Math.max(ctrlPanelRef.get().getPreferredSize().width + 40, width_A4 + 60);
        ret.setSize(widthPreferred, heightPreferred);
        return ret;
    }

    PreviewPage getPreviewPage() {
        return previewPanelRef.get().getPreviewPage();
    }
}
