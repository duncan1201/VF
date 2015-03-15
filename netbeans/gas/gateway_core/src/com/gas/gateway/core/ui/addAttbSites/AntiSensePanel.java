/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.misc.UppercaseDocumentFilter;
import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.PrimerAdapter;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.lang.ref.WeakReference;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

/**
 *
 * @author dq
 */
class AntiSensePanel extends JPanel {

    protected Boolean stopCodonIncluded;
    protected JCheckBox stopCodonBtn;
    JTextField spacerField;
    private JCheckBox fuseBox;

    public AntiSensePanel() {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(new LineBorder(Color.gray), "Other additions to antisense primer", TitledBorder.LEFT, TitledBorder.TOP);
        setBorder(titledBorder);

        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        // other conditions:        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(createAdditionsPanel(), c);

        // filler
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        c.gridwidth = 2;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        add(comp, c);

        hookupListeners();

        setStopCodonIncluded(false);
    }

    private JPanel createSpacerPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        fuseBox = new JCheckBox("Fuse the PCR product in frame with an N- or C-terminal tag", true);
        ret.add(fuseBox, c);

        c = new GridBagConstraints();
        spacerField = new JTextField();
        ((AbstractDocument)spacerField.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
        UIUtil.setPreferredWidthByPrototype(spacerField, "ABC");
        ret.add(spacerField, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);
        return ret;
    }
    
    boolean isFuse(){
        return this.fuseBox.isSelected();
    }

    private void hookupListeners() {
        stopCodonBtn.addItemListener(new AntiSensePanelListeners.StopCodonListener(this));
        addPropertyChangeListener(new AntiSensePanelListeners.PtyChangeLisener(this));
        spacerField.getDocument().addDocumentListener(new AntiSensePanelListeners.SpacerListener(spacerField));
        fuseBox.addItemListener(new AntiSensePanelListeners.FuseListener());
    }

    public Boolean isStopCodonIncluded() {
        return stopCodonIncluded;
    }

    public void setStopCodonIncluded(Boolean stopCodonIncluded) {
        Boolean old = this.stopCodonIncluded;
        this.stopCodonIncluded = stopCodonIncluded;
        firePropertyChange("stopCodonIncluded", old, this.stopCodonIncluded);
    }

    void updateUI(PrimerAdapter primerAdapter) {
        spacerField.setText(primerAdapter.getPrefix());
    }

    private JPanel createAdditionsPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPanel stopPanel = createStopPanel();
        ret.add(stopPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel spacerPanel = createSpacerPanel();
        ret.add(spacerPanel, c);
        return ret;
    }
    
    private JPanel createStopPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        stopCodonBtn = new JCheckBox("Stop codon ");
        ret.add(stopCodonBtn, c);
        
        c = new GridBagConstraints();
        JLabel t = new JLabel("(the underlined)");
        Font font = t.getFont();
        t.setForeground(Color.DARK_GRAY);
        font = font.deriveFont(font.getSize2D() - 1);
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        font = font.deriveFont(attributes);
        t.setFont(font);
        ret.add(t, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component comp = Box.createRigidArea(new Dimension(1,1));
        ret.add(comp, c);
        
        return ret;
    }
}
