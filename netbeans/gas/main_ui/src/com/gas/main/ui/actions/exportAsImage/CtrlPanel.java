/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dq
 */
class CtrlPanel extends JPanel {

    private WeakReference<JComboBox> formatComboRef;
    private WeakReference<JSpinner> zoomSpinnerRef;
    private WeakReference<IPrintEditor> editorRef;
    private WeakReference<JCheckBox> visibleOnlyRef;

    CtrlPanel(IPrintEditor printEditor) {
        editorRef = new WeakReference<IPrintEditor>(printEditor);
        initComponents();

        hookupListeners();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        add(new JLabel("Format:"), c);

        c = new GridBagConstraints();
        StringList formatNames = getFormatNames();
        String sample = formatNames.longest() + "AB";
        Dimension sizeCombo = UIUtil.getSize(sample, JComboBox.class);
        JComboBox formatCombo = new JComboBox(formatNames.toArray(new String[formatNames.size()]));
        UIUtil.setPreferredWidth(formatCombo, sizeCombo.width);
        formatComboRef = new WeakReference<JComboBox>(formatCombo);
        add(formatCombo, c);

        if (editorRef.get().supportVisibleAreaOnly()) {
            c = new GridBagConstraints();
            JCheckBox visibleOnly = new JCheckBox("Visible Area Only");
            visibleOnlyRef = new WeakReference<JCheckBox>(visibleOnly);
            add(visibleOnly, c);
        }

        c = new GridBagConstraints();
        JSpinner zoomSpinner = new JSpinner();
        zoomSpinnerRef = new WeakReference<JSpinner>(zoomSpinner);
        zoomSpinner.setModel(new SpinnerNumberModel(0.5, 0.1, 1, 0.1));
        zoomSpinner.setEditor(new JSpinner.NumberEditor(zoomSpinner, "#%"));
        add(zoomSpinner, c);
    }

    WeakReference<JCheckBox> getVisibleOnlyRef() {
        return visibleOnlyRef;
    }
    
    String getSelectedFormat() {
        return (String) formatComboRef.get().getSelectedItem();
    }

    Double getScale() {
        return (Double) zoomSpinnerRef.get().getValue();
    }

    private StringList getFormatNames() {
        String[] formatNames = ImageIO.getWriterFormatNames();
        StringList ret = new StringList(formatNames);

        ret.toUpperCase();
        ret.removeDuplicates();

        if (ret.contains("JPG") && ret.contains("JPEG")) {
            ret.remove("JPEG");
        }
        ret.remove("WBMP");

        return ret;
    }
    
    

    private void hookupListeners() {
        formatComboRef.get().addActionListener(new CtrlPanelListeners.FormatListener());
        zoomSpinnerRef.get().addChangeListener(new CtrlPanelListeners.ZoomListener());
        if(visibleOnlyRef != null && visibleOnlyRef.get() != null){
            visibleOnlyRef.get().addItemListener(new CtrlPanelListeners.VisibleOnlyListener());
        }
    }
}
