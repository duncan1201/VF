/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.zoom;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

/**
 *
 * @author dunqiang
 */
public class ZoomPanel extends JPanel {

    protected static final int ZOOM_MIN = 2;
    protected static final int ZOOM_MAX = 160;
    private boolean valueAdjusting;
    private JSlider zoomSlider;
    private JLabel label;
    JButton zoomInBtn;
    JButton zoomOutBtn;
    JToggleButton editingBtn;
    private Integer zoomValue;

    public ZoomPanel(Integer initValue) {
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        editingBtn = new JToggleButton(ImageHelper.createImageIcon(ImageNames.EDIT_NO_16));
        editingBtn.setSelectedIcon(ImageHelper.createImageIcon(ImageNames.PENCIL_16));
        boolean editable = Pref.CommonPtyPrefs.getInstance().getEditable();
        editingBtn.setSelected(editable);
        editingBtn.setForeground(editable? Color.BLACK: Color.GRAY);
        editingBtn.setText("Editable");
        editingBtn.setFocusable(false);
        add(editingBtn);

        label = new JLabel();
        UIUtil.setPreferredWidthByPrototype(label, "100 %");
        add(label);

        ImageIcon icon = ImageHelper.createImageIcon(ImageNames.ZOOM_OUT_16);
        zoomOutBtn = new FlatBtn(icon, false);
        zoomOutBtn.setActionCommand("zoomout");
        zoomOutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        zoomOutBtn.setFocusable(false);
        add(zoomOutBtn);
        
        if(initValue != null){
            zoomValue = initValue;
        }

        zoomSlider = new JSlider(JSlider.HORIZONTAL,
                ZOOM_MIN, ZOOM_MAX, initValue == null ? ZOOM_MIN: initValue);
        zoomSlider.setPaintTicks(false);
        zoomSlider.setFocusable(false);
        add(zoomSlider);
        label.setText(String.format("%d %%", zoomSlider.getValue()));        

        icon = ImageHelper.createImageIcon(ImageNames.ZOOM_IN_16);
        zoomInBtn = new FlatBtn(icon, false);
        zoomInBtn.setActionCommand("zoomin");
        zoomInBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        zoomInBtn.setFocusable(false);
        add(zoomInBtn);

        hookupListeners();
    }

    private void hookupListeners() {
        zoomInBtn.addActionListener(new ZoomPanelListeners.ZoomBtnListener(this));
        zoomOutBtn.addActionListener(new ZoomPanelListeners.ZoomBtnListener(this));
        editingBtn.addActionListener(new ZoomPanelListeners.EditableListener());
        Pref.CommonPtyPrefs.getInstance().addPropertyChangeListener(new ZoomPanelListeners.PrefPtyListener(this));
        zoomSlider.addChangeListener(new ZoomPanelListeners.SliderChangeListener(this));
        addPropertyChangeListener(new ZoomPanelListeners.PtyListener());
    }

    public void setZoomValue(Integer v) {
        Integer old = this.zoomValue;
        this.zoomValue = v;
        firePropertyChange("zoomValue", old, this.zoomValue);
    }

    public Integer getZoomValue() {
        return this.zoomValue;
    }

    public void setZoom(int zoom) {
        zoomSlider.setValue(zoom);
    }

    protected boolean isValueAdjusting() {
        return valueAdjusting;
    }

    protected void setValueAdjusting(boolean valueAdjusting) {
        this.valueAdjusting = valueAdjusting;
    }

    public JLabel getLabel() {
        return label;
    }

    public JSlider getSlider() {
        return zoomSlider;
    }
}
