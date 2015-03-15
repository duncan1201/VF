/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

/**
 *
 * @author dq
 */
class PreviewPageListeners {

    static class PrintParamListener implements PropertyChangeListener {

        private WeakReference<PreviewPage> previewPageRef;

        public PrintParamListener(PreviewPage previewPage) {
            previewPageRef = new WeakReference<PreviewPage>(previewPage);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PrintParam src = (PrintParam) evt.getSource();
            final String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            IPrintEditor printEditor = previewPageRef.get().getPrintEditor();
            if (name.equals("pageNo") 
                    || name.equals("pageFormat") 
                    || name.equals("zoomScale")
                    || name.equals("printPageNo")
                    || name.equals("printName")
                    || name.equals("printDate")) {
                BufferedImage image = printEditor.createImageForPrinting(previewPageRef.get().getPrintParam());
                previewPageRef.get().setImage(image);
            }
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PreviewPage src = (PreviewPage) evt.getSource();
            String name = evt.getPropertyName();
            Object oV = evt.getOldValue();
            Object v = evt.getNewValue();
            if (name.equals("printParam")) {
                if (oV == null) {
                    src.getPrintParam().addPropertyChangeListener(new PrintParamListener(src));
                }
                IPrintEditor printEditor = src.getPrintEditor();
                if (printEditor == null) {
                    return;
                }
                BufferedImage image = printEditor.createImageForPrinting(src.getPrintParam());
                src.setImage(image);
                
            } else if (name.equals("image")) {
                src.revalidate();
                src.repaint();
            }
        }
    }
}
