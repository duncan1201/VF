/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.zoom;

import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.editor.IZoomable;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class ZoomPanelListeners {

    static class PrefPtyListener implements PropertyChangeListener {

        ZoomPanel zoomPanel;

        PrefPtyListener(ZoomPanel zoomPanel) {
            this.zoomPanel = zoomPanel;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final String name = evt.getPropertyName();
            if (name.equals("editable")) {
                zoomPanel.editingBtn.setSelected(Pref.CommonPtyPrefs.getInstance().getEditable());
                zoomPanel.editingBtn.setForeground(zoomPanel.editingBtn.isSelected() ? Color.BLACK : Color.GRAY);

            }
        }
    }

    static class SliderChangeListener implements ChangeListener {

        private ZoomPanel zoomPanel;
        private IZoomable zoomable;

        SliderChangeListener(ZoomPanel zoomPanel) {
            this.zoomPanel = zoomPanel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {

            JSlider source = (JSlider) e.getSource();
            if (zoomable == null) {
                zoomable = UIUtil.getParent(source, IZoomable.class);
            }
            
            if (!source.getValueIsAdjusting()) {
                int fps = (int) source.getValue();
                if (zoomPanel.getZoomValue() == null || fps > zoomPanel.getZoomValue()) {
                    zoomPanel.getLabel().setText(String.format("%d %%", fps));
                    zoomPanel.setZoomValue(fps);
                } else {
                    if (zoomable.canZoomOut(fps)) {
                        zoomPanel.getLabel().setText(String.format("%d %%", fps));
                        zoomPanel.setZoomValue(fps);
                    } else {
                        source.setValue(zoomPanel.getZoomValue());
                    }
                }
            }
        }
    }

    static class EditableListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton src = (JToggleButton) e.getSource();

            Pref.CommonPtyPrefs.getInstance().setEditable(src.isSelected());
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ZoomPanel src = (ZoomPanel) evt.getSource();

            Integer fps = src.getZoomValue();
            IZoomable zoomable = UIUtil.getParent(src, IZoomable.class);
            if (fps != null && zoomable != null) {
                zoomable.setZoom(fps);
            }
        }
    }

    static class ZoomBtnListener implements ActionListener {

        private ZoomPanel panel;
        private IZoomable zoomable;

        ZoomBtnListener(ZoomPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (zoomable == null) {
                zoomable = UIUtil.getParent(btn, IZoomable.class);
            }
            int v = panel.getSlider().getValue();
            int newV;

            String cmd = btn.getActionCommand();
            if (cmd.equalsIgnoreCase("zoomin")) {                
                newV = Math.round(v * 1.2f);
                if (newV == v) {
                    newV = v + 1;
                }
                newV = Math.min(newV, ZoomPanel.ZOOM_MAX);
            } else if (cmd.equalsIgnoreCase("zoomout")) {
                newV = Math.round(v * 0.9f);
                if (newV == v) {
                    newV = v - 1;
                }
                newV = Math.max(newV, ZoomPanel.ZOOM_MIN);
                if (!zoomable.canZoomOut(newV)) {
                    return ;
                }                
            } else {
                throw new UnsupportedOperationException();
            }

            panel.setZoom(newV);
        }
    }
}
