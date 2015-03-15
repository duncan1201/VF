/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.UppercaseDocumentFilter;
import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.gateway.core.service.api.RecomType;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
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
class SensePanel extends JPanel {

    protected ButtonGroup btnGroup;
    protected JCheckBox sdBtn;
    protected JRadioButton kozakBtn;
    protected JRadioButton startBtn;
    protected JRadioButton noStartKozak;
    protected JTextField spacerField;
    JLabel forbiddenText;
    JCheckBox fuseBox;
    protected Boolean kozakIncluded;
    protected Boolean startCodonIncluded;
    protected Boolean sdIncluded;

    SensePanel() {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(new LineBorder(Color.gray), "Other additions to sense primer", TitledBorder.LEFT, TitledBorder.TOP);
        setBorder(titledBorder);

        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(createOtherAdditionsPanel(), c);

        hookupListeners();

        setStartCodonIncluded(false);
        setKozakIncluded(false);
        setSdIncluded(false);
    }

    void updateUI(PrimerAdapter primerAdapter) {
        spacerField.setText(primerAdapter.getPostfix());
        StringList forbidden = new StringList(primerAdapter.getForbidden());
        if (forbidden.isEmpty()) {
            forbiddenText.setText("");
        } else {
            forbiddenText.setText("Disallowed: " + forbidden.toString(','));
        }
    }

    private void hookupListeners() {
        addPropertyChangeListener(new SensePanelListeners.PtyChangeListener(new WeakReference<SensePanel>(this)));
        sdBtn.addItemListener(new SensePanelListeners.SDItemListener(new WeakReference<SensePanel>(this)));
        startBtn.addItemListener(new SensePanelListeners.StartCodonItemListener(new WeakReference<SensePanel>(this)));
        kozakBtn.addItemListener(new SensePanelListeners.KozakBtnItemListener(new WeakReference<SensePanel>(this)));

        spacerField.getDocument().addDocumentListener(new SensePanelListeners.SpacerListener(spacerField));
        fuseBox.addItemListener(new SensePanelListeners.FuseListener());
    }

    public boolean isFuse() {
        return fuseBox.isSelected();
    }

    public Boolean isKozakIncluded() {
        return kozakIncluded;
    }

    public Boolean isSdIncluded() {
        return sdIncluded;
    }

    public Boolean isStartCodonIncluded() {
        return startCodonIncluded;
    }

    public void setKozakIncluded(Boolean kozakIncluded) {
        Boolean old = this.kozakIncluded;
        this.kozakIncluded = kozakIncluded;
        firePropertyChange("kozakIncluded", old, this.kozakIncluded);
    }

    public void setSdIncluded(Boolean sdIncluded) {
        Boolean old = this.sdIncluded;
        this.sdIncluded = sdIncluded;
        firePropertyChange("sdIncluded", old, this.sdIncluded);
    }

    public void setStartCodonIncluded(Boolean startCodonIncluded) {
        Boolean old = this.startCodonIncluded;
        this.startCodonIncluded = startCodonIncluded;
        firePropertyChange("startCodonIncluded", old, this.startCodonIncluded);
    }

    private JPanel createOtherAdditionsPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPanel sdPanel = createSDPanel();
        ret.add(sdPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPanel kozakPanel = createKozakPanel();
        ret.add(kozakPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPanel startPanel = createStartCodonPanel();
        ret.add(startPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        noStartKozak = new JRadioButton("No start codon or Kozak consensus sequence");
        noStartKozak.setSelected(true);
        ret.add(noStartKozak, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel spacerPanel = createSpacerPanel();
        ret.add(spacerPanel, c);

        btnGroup = new ButtonGroup();
        btnGroup.add(kozakBtn);
        btnGroup.add(startBtn);
        btnGroup.add(noStartKozak);

        return ret;
    }

    private JPanel createStartCodonPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        startBtn = new JRadioButton("Start codon only ");
        ret.add(startBtn, c);

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
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);

        return ret;
    }

    private JPanel createSDPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        sdBtn = new JCheckBox("Shine-Dalgarno sequence(SD)");
        ret.add(sdBtn, c);

        c = new GridBagConstraints();
        JButton info = new FlatBtn(ImageHelper.createImageIcon(ImageNames.INFO_16), false);
        info.setToolTipText("In prokaryotes, Shine-Dalgarno sequence promotes efficient and accurate translation of mRNA");
        ret.add(info, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);

        return ret;
    }

    private JPanel createKozakPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        kozakBtn = new JRadioButton("Kozak consensus sequence, including start codon");
        ret.add(kozakBtn, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        JButton info = new FlatBtn(ImageHelper.createImageIcon(ImageNames.INFO_16), false);
        info.setToolTipText("In eukaryotes, the Kozak consensus sequence plays a major role in the initiation of the translation process");
        ret.add(info, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);
        return ret;
    }

    private JPanel createSpacerPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        fuseBox = new JCheckBox("Fuse the PCR product in frame with an N- or C-terminal tag", true);
        ret.add(fuseBox, c);

        c = new GridBagConstraints();
        spacerField = new JTextField();
        ((AbstractDocument) spacerField.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
        UIUtil.setPreferredWidthByPrototype(spacerField, "ABC");
        ret.add(spacerField, c);


        c = new GridBagConstraints();
        forbiddenText = new JLabel();
        Font font = forbiddenText.getFont();
        forbiddenText.setFont(font.deriveFont(font.getSize2D() - 1));
        forbiddenText.setForeground(Color.DARK_GRAY);
        ret.add(forbiddenText, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);

        return ret;
    }
}
