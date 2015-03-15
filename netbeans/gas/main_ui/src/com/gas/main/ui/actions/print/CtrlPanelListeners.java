/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class CtrlPanelListeners {

    static class PrintDialogBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton)e.getSource();
            CtrlPanel ctrlPanel = UIUtil.getParent(src, CtrlPanel.class);
            if(ctrlPanel == null){
                return;
            }
            PrintParam printParam = ctrlPanel.getPrintParam();
            PrintRequestAttributeSet printReqAttSet = printParam.getPrintReqAttSet();
            PrinterJob printerJob = ctrlPanel.getPrinterJob();
            printerJob.printDialog(printReqAttSet);
            PageFormat pageFormat = printerJob.getPageFormat(printReqAttSet);
            
            printParam.mergePageFormat(pageFormat);
            
            updateOrientationBtns(ctrlPanel);
        }
    }
    
    static class CheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox src = (JCheckBox) e.getSource();
            CtrlPanel ctrlPanel = UIUtil.getParent(src, CtrlPanel.class);
            if (ctrlPanel == null) {
                return;
            }

            if (src.getActionCommand().equals("date")) {
                ctrlPanel.getPrintParam().setPrintDate(e.getStateChange() == ItemEvent.SELECTED);
            } else if (src.getActionCommand().equals("pageNo")) {
                ctrlPanel.getPrintParam().setPrintPageNo(e.getStateChange() == ItemEvent.SELECTED);
            } else if (src.getActionCommand().equals("name")) {
                ctrlPanel.getPrintParam().setPrintName(e.getStateChange() == ItemEvent.SELECTED);
            } else if (src.getActionCommand().equals("visibleArea")) {
                ctrlPanel.getPrintParam().setVisibleArea(e.getStateChange() == ItemEvent.SELECTED);
            }
        }
    }

    static class ZoomListener implements ActionListener, ChangeListener {

        @Override
        public void actionPerformed(ActionEvent e) {
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner src = (JSpinner) e.getSource();
            CtrlPanel ctrlPanel = UIUtil.getParent(src, CtrlPanel.class);
            PrintPanel printPanel = UIUtil.getParent(src, PrintPanel.class);
            if (ctrlPanel == null || printPanel == null) {
                return;
            }
            SpinnerNumberModel model = (SpinnerNumberModel) src.getModel();
            Double value = (Double) src.getValue();

            ctrlPanel.getPrintParam().setZoomScale(value);            
        }
    }

    static class OrientationBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton) e.getSource();
            CtrlPanel ctrlPanel = UIUtil.getParent(src, CtrlPanel.class);
            PrintParam printParam = ctrlPanel.getPrintParam();
            PrintRequestAttributeSet pras = printParam.getPrintReqAttSet();
            if (src.getActionCommand().equals("landscape")) {
                pras.add(OrientationRequested.LANDSCAPE);               
            } else if (src.getActionCommand().equals("portrait")) {
                pras.add(OrientationRequested.PORTRAIT);                
            }

            // update the orientation      
            PageFormat pf = ctrlPanel.getPrinterJob().getPageFormat(pras);
            printParam.mergePageFormat(pf);
                        
            // update the pageCount
            int pageCount = ctrlPanel.getPrintEditor().getTotalPages(printParam);
            printParam.setPageCount(pageCount);

            // update pageNo if necessary
            int pageNo = printParam.getPageNo();
            if (pageNo > pageCount) {
                printParam.setPageNo(1);
            }

            // update the orientation      
            //PageFormat pf = ctrlPanel.getPrinterJob().getPageFormat(pras);
            //printParam.mergePageFormat(pf);
        }
    }

    static class NextPrevListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            CtrlPanel ctrlPanel = UIUtil.getParent(src, CtrlPanel.class);
            PrintPanel printPanel = UIUtil.getParent(src, PrintPanel.class);

            PrintParam printParam = printPanel.getPrintParam();
            if (src.getActionCommand().equals("next")) {
                printParam.setPageNo(printParam.getPageNo() + 1);
            } else if (src.getActionCommand().equals("prev")) {
                printParam.setPageNo(printParam.getPageNo() - 1);
            }

            updateNaviBtns(ctrlPanel);
            updatePageLabel(ctrlPanel);
        }
    }

    static class PrintParamListener implements PropertyChangeListener {

        private WeakReference<CtrlPanel> ctrlPanelRef;

        public PrintParamListener(CtrlPanel ctrlPanel) {
            ctrlPanelRef = new WeakReference<CtrlPanel>(ctrlPanel);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PrintParam src = (PrintParam) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("pageCount") || name.equals("pageNo")) {
                updatePageLabel(ctrlPanelRef.get());
                updateNaviBtns(ctrlPanelRef.get());
            }else if(name.equals("pageFormat")){                
            }else if(name.equals("visibleArea")){
                PrintPanel printPanel = ctrlPanelRef.get().getPrintPanel();
                IPrintEditor editor = printPanel.getPrintEditor();
                final int pageCount = editor.getTotalPages(printPanel.getPrintParam());
                
                printPanel.getPrintParam().setPageCount(pageCount);                
                printPanel.getPrintParam().setPageNo(1);                
            }
        }               
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CtrlPanel src = (CtrlPanel) evt.getSource();
            final String name = evt.getPropertyName();
            Object oV = evt.getOldValue();
            Object v = evt.getNewValue();
            if (name.equals("printParam")) {
                PrintParam printParam = (PrintParam) v;

                if (oV == null) {
                    printParam.addPropertyChangeListener(new PrintParamListener(src));
                }

                src.getPrintDateBox().setSelected(printParam.getPrintDate());
                src.getPrintPageNoBox().setSelected(printParam.getPrintPageNo());

                final int orientation = printParam.getPageFormat().getOrientation();
                src.getPortraitBtn().setSelected(orientation == PageFormat.PORTRAIT);
                src.getLandscapeBtn().setSelected(orientation == PageFormat.LANDSCAPE);

                src.getZoomSpinner().setValue(printParam.getZoomScale());

                if(src.getVisibleAreaBox() != null){
                    src.getVisibleAreaBox().setSelected(printParam.getVisibleArea());
                }
                
                src.getPrintDateBox().setSelected(printParam.getPrintDate());
                
                src.getPrintNameBox().setSelected(printParam.getPrintName());
                
                src.getPrintPageNoBox().setSelected(printParam.getPrintPageNo());
                
                updateNaviBtns(src);
                updatePageLabel(src);
            }
        }
    }

    static void updateNaviBtns(CtrlPanel src) {
        if (src.getPrintParam().getPageCount() == null || src.getPrintParam().getPageNo() == null) {
            return;
        }
        final PrintParam printParam = src.getPrintParam();
        
        src.getPrevBtn().setEnabled(!printParam.getPageNo().equals(1));
        src.getNextBtn().setEnabled(!printParam.getPageCount().equals(printParam.getPageNo()));
    }
    
    static void updateOrientationBtns(CtrlPanel src){
        int orientation = src.getPrintParam().getPageFormat().getOrientation();
        if(orientation == PageFormat.PORTRAIT){
            src.getPortraitBtn().setSelected(true);
        }else{
            src.getLandscapeBtn().setSelected(true);
        }      
    }

    static void updatePageLabel(CtrlPanel src) {
        if (src.getPrintParam().getPageCount() == null || src.getPrintParam().getPageNo() == null) {
            return;
        }
        src.getPageLabel().setText(String.format("Page %d of %d", src.getPrintParam().getPageNo(), src.getPrintParam().getPageCount()));
    }
}
