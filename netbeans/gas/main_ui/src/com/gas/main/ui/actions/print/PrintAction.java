/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.util.Date;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.JobHoldUntil;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.openide.windows.TopComponent;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.print.PrintAction")
@ActionRegistration(displayName = "#CTL_PrintAction",
        iconBase = "com/gas/main/ui/actions/print/printer_16.png")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 2440, separatorBefore = 2439)
})
@NbBundle.Messages("CTL_PrintAction=Print...")
public class PrintAction extends AbstractAction{

    IPrintEditor editor;

    public PrintAction() {
        putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.PRINTER_24));
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();

        if (tc instanceof IPrintEditor) {
            editor = (IPrintEditor) tc;
        } else {
            return;
        }

        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        PrintRequestAttributeSet printReqAttSet = UIUtil.getDefaultPrintRequestAttributeSet(printService);
        if (editor.isPrintableJComponent()) {
            printReqAttSet.add(OrientationRequested.PORTRAIT);
        } else {
            printReqAttSet.add(OrientationRequested.LANDSCAPE);
        }
        updatePrintRequestAttributeSet(printReqAttSet);
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        // 
        PrintParam printParam = new PrintParam();
        PageFormat pageFormat = printerJob.getPageFormat(printReqAttSet);
        printParam.setPrintReqAttSet(printReqAttSet);
        printParam.setJobName(editor.getJobName());
        printParam.mergePageFormat(pageFormat);

        if (editor.isPrintableJComponent()) {
            JComponent printableComp = editor.getPrintableJComponent();
            if (printableComp instanceof JTextComponent) {
                JTextComponent jtc = (JTextComponent) printableComp;
                CommonUtil.printQuietly(jtc, new MessageFormat(editor.getJobName()), new MessageFormat("Page {0}"), true, printService, printReqAttSet, true);
            } else if (printableComp instanceof JTable) {
                throw new UnsupportedOperationException("JTable not supported yet");
            }
            return;
        }

        // display preview here
        PrintPanel printPanel = new PrintPanel(editor, printParam);
        printPanel.getCtrlPanel().setPrinterJob(printerJob);
        DialogDescriptor dd = new DialogDescriptor(printPanel, String.format("Print %s", editor.getJobName()));

        Object ret = DialogDisplayer.getDefault().notify(dd);
        if (ret.equals(DialogDescriptor.OK_OPTION)) {

            if (editor.isPrintableJComponent()) {
                printerJob.setPrintable(editor.createFromPrintableJComponent(printParam));
            } else {
                printerJob.setPrintable(printPanel.getPreviewPage());
            }

            try {
                printerJob.print(printParam.getPrintReqAttSet());
            } catch (PrinterException pe) {
            }

        }
    }

    private PrintRequestAttributeSet updatePrintRequestAttributeSet(PrintRequestAttributeSet ret) {
        ret.remove(Destination.class);

        JobHoldUntil immediately = new JobHoldUntil(new Date(0L));
        ret.add(immediately);

        return ret;
    }
}
