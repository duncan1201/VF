/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.general;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.combo.ImgComboRenderer;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class PropertiesPanel extends JPanel implements IReclaimable {

    JCheckBox doubleStrandedCheckBox;
    private JCheckBox minimapCheckBox;
    private JCheckBox baseNumberCheckBox;
    private WeakReference<JComboBox> colorComboRef;
    private Boolean forProtein;

    PropertiesPanel() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        Pref.CommonPtyPrefs pref = Pref.CommonPtyPrefs.getInstance();
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        // colors:
        JPanel colorPanel = new JPanel(new GridBagLayout());

        JLabel colorsLabel = new JLabel("Colors:");
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        colorPanel.add(colorsLabel, c);

        JComboBox colorCombo = new JComboBox();
        colorCombo.setRenderer(new ImgComboRenderer());
        c = new GridBagConstraints();
        colorPanel.add(colorCombo, c);
        colorComboRef = new WeakReference<JComboBox>(colorCombo);

        c = new GridBagConstraints();
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        colorPanel.add(Box.createGlue(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(colorPanel, c);

        // double stranded
        doubleStrandedCheckBox = new JCheckBox("Double stranded");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(doubleStrandedCheckBox, c);

        // minimap
        minimapCheckBox = new JCheckBox("Minimap");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(minimapCheckBox, c);

        // base number
        baseNumberCheckBox = new JCheckBox("Base number", true);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(baseNumberCheckBox, c);

        hookupListeners();
        
    }

    @Override
    public void cleanup() {
        doubleStrandedCheckBox = null;
        minimapCheckBox = null;
        baseNumberCheckBox = null;
    }

    protected void reinitColorCombo() {
        Pref.ColorProviderPrefs.KEY key = forProtein ? Pref.ColorProviderPrefs.KEY.PROTEIN : Pref.ColorProviderPrefs.KEY.DNA;
        String selectedName = Pref.ColorProviderPrefs.getInstance().getColorProviderName(key);
        IColorProvider selectedColorProvider = ColorProviderFetcher.getColorProvider(selectedName);
        ColorProviderFetcher.TYPE type = forProtein ? ColorProviderFetcher.TYPE.PROTEIN : ColorProviderFetcher.TYPE.DNA;
        List<IColorProvider> colorProviders = ColorProviderFetcher.getColorProviders(type, List.class);

        colorComboRef.get().setModel(new DefaultComboBoxModel(colorProviders.toArray(new IColorProvider[colorProviders.size()])));
        colorComboRef.get().setSelectedItem(selectedColorProvider);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new PropertiesPanelListeners.PtyChangeListener());
        colorComboRef.get().addActionListener(new PropertiesPanelListeners.ColorComboListener(this));
        doubleStrandedCheckBox.addItemListener(new PropertiesPanelListeners.DoubleStrandedItemListener(this));

        minimapCheckBox.addItemListener(new PropertiesPanelListeners.MinimapItemListener(this));
        baseNumberCheckBox.addItemListener(new PropertiesPanelListeners.BaseNumberItemListener(this));
    }

    public Boolean getForProtein() {
        return forProtein;
    }

    public void setForProtein(Boolean forProtein) {
        Boolean old = this.getForProtein();
        this.forProtein = forProtein;
        firePropertyChange("forProtein", old, this.forProtein);
    }

    public JCheckBox getBaseNumberCheckBox() {
        return baseNumberCheckBox;
    }

    public JComboBox getColorCombo() {
        return colorComboRef.get();
    }

    public boolean isDoubleStranded() {
        return doubleStrandedCheckBox.isSelected();
    }

    public void setDoubleStranded(boolean doubleStranded) {
        doubleStrandedCheckBox.setSelected(doubleStranded);
    }

    public boolean isBaseNumber() {
        return baseNumberCheckBox.isSelected();
    }

    public void setBaseNumber(boolean baseNumber) {
        this.baseNumberCheckBox.setSelected(baseNumber);
    }

    public boolean isMinimap() {
        return minimapCheckBox.isSelected();
    }

    public void setMinimap(boolean minimap) {
        this.minimapCheckBox.setSelected(minimap);
    }
}
