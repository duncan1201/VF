/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.msa.ui.common.ITree;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import javax.swing.*;

/**
 *
 * @author dq
 */
public class GeneralPanel extends JPanel {

    private JToggleButton rectBtn;
    private JToggleButton radialBtn;
    private JToggleButton circularBtn;
    private JRadioButton phylogramBtn;
    private JRadioButton cladogramBtn;
    private JCheckBox sameLengthBtn;
    private WeakReference<JSpinner> fontSizeSpinnerRef;
    private JButton colorBtn;

    public GeneralPanel() {
        initComponents();

        hookupListeners();

    }
    
    private void initComponents(){
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(0, insets.left, 0, insets.right));
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = null;
        int gridx = 0;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy++;
        add(createTreeTypePanel(), c);

        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy++;
        add(createEdgeTransformPanel(), c);

        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy++;
        add(createFontSizePanel(), c);  
    }

    void initValues(MSA msa) {        
        String t = msa.getMsaSetting().getTreeEdgeTransform();
        if (t.equalsIgnoreCase(ITree.TRANSFORM.NONE.toString())) {
            getPhylogramBtn().setSelected(true);
            getSameLengthBtn().setSelected(false);
            getSameLengthBtn().setEnabled(false);
        } else if (t.equalsIgnoreCase(ITree.TRANSFORM.EQUAL.toString())) {
            getCladogramBtn().setSelected(true);
            getSameLengthBtn().setSelected(true);
            getSameLengthBtn().setEnabled(true);
        } else if (t.equalsIgnoreCase(ITree.TRANSFORM.CLADOGRAM.toString())) {
            getCladogramBtn().setSelected(true);
            getSameLengthBtn().setSelected(false);
            getSameLengthBtn().setEnabled(true);
        }

    }

    protected JToggleButton getRectButton() {
        return rectBtn;
    }

    protected JToggleButton getRadialButton() {
        return radialBtn;
    }

    protected JToggleButton getCircularButton() {
        return circularBtn;
    }

    protected JRadioButton getCladogramBtn() {
        return cladogramBtn;
    }

    protected JRadioButton getPhylogramBtn() {
        return phylogramBtn;
    }

    protected JCheckBox getSameLengthBtn() {
        return sameLengthBtn;
    }

    private JPanel createColorPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        colorBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.COLOR_CUBE_16), "", true, 8);
        ret.add(colorBtn, c);

        return ret;
    }

    protected JButton getColorButton() {
        return colorBtn;
    }

    private JPanel createTreeTypePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        rectBtn = new JToggleButton();
        rectBtn.setActionCommand(TreePane.FORM.RECTANGLE.toString());
        rectBtn.setIcon(ImageHelper.createImageIcon(ImageNames.RECT_TREE_16));
        rectBtn.setToolTipText("Rooted tree layout");
        ret.add(rectBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        radialBtn = new JToggleButton();
        radialBtn.setActionCommand(TreePane.FORM.RADIAL.toString());
        radialBtn.setIcon(ImageHelper.createImageIcon(ImageNames.RADIAL_TREE_16));
        radialBtn.setToolTipText("Unrooted tree layout");
        ret.add(radialBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        circularBtn = new JToggleButton();
        circularBtn.setActionCommand(TreePane.FORM.CIRCULAR.toString());
        circularBtn.setIcon(ImageHelper.createImageIcon(ImageNames.CIRCULAR_TREE_16));
        circularBtn.setToolTipText("Circular tree layout");
        ret.add(circularBtn, c);

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(rectBtn);
        bGroup.add(radialBtn);
        bGroup.add(circularBtn);

        return ret;
    }

    private JPanel createEdgeTransformPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        phylogramBtn = new JRadioButton("Phylogram");
        phylogramBtn.setActionCommand("phylogram");
        ret.add(phylogramBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        cladogramBtn = new JRadioButton("Cladogram");
        cladogramBtn.setActionCommand("cladogram");
        ret.add(cladogramBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        sameLengthBtn = new JCheckBox("Same Length");
        sameLengthBtn.setActionCommand("sameLength");
        ret.add(sameLengthBtn, c);

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(phylogramBtn);
        bGroup.add(cladogramBtn);

        return ret;
    }

    private JPanel createFontSizePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();        
        ret.add(new JLabel("Font Size:"), c);
        
        c = new GridBagConstraints();
        JSpinner fontSizeSpinner = new JSpinner();
        Float fontSize = MSAPref.getInstance().getFontSize();
        int defaultFontSize = Math.round(FontUtil.getDefaultFontSize());
        fontSizeSpinner.setModel(new SpinnerNumberModel(Math.round(fontSize), defaultFontSize - 3, defaultFontSize + 3, 1));
        fontSizeSpinnerRef = new WeakReference<JSpinner>(fontSizeSpinner);
        ret.add(fontSizeSpinner, c);
        
        c = new GridBagConstraints();
        ret.add(createColorPanel(), c);
        return ret;
    }

    protected JSpinner getFontSizeSpinner() {
        return fontSizeSpinnerRef.get();
    }

    private void hookupListeners() {
        getRectButton().addItemListener(new GeneralPanelListeners.TreeFormBtnListener());
        getCircularButton().addItemListener(new GeneralPanelListeners.TreeFormBtnListener());
        getRadialButton().addItemListener(new GeneralPanelListeners.TreeFormBtnListener());

        getPhylogramBtn().addActionListener(new GeneralPanelListeners.EdgeFormListener());
        getCladogramBtn().addActionListener(new GeneralPanelListeners.EdgeFormListener());
        getSameLengthBtn().addItemListener(new GeneralPanelListeners.SameLengthListener());

        getFontSizeSpinner().addChangeListener(new GeneralPanelListeners.FontSizeListener());
        getColorButton().addActionListener(new GeneralPanelListeners.ColorBtnListener());
    }
}
