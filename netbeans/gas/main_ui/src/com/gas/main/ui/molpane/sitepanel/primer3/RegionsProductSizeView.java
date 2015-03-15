package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.accordian2.IOutlookPanel;
import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.core.IntList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.misc.SpinnerValidators;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.main.ui.molpane.MolPane;
import com.gas.domain.core.primer3.UserInput;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class RegionsProductSizeView extends JPanel implements IOutlookPanel {

    private JPanel targetRegionPanel;

    /**
     * Creates new form RegionsProductSizeView
     */
    RegionsProductSizeView() {
        initComponents();
        hookupListeners();
    }

    List<String> validateInput() {
        List<String> ret = new ArrayList<String>();
        if (sequenceIncludedRegionCheckBox.isSelected()) {
            Integer from = (Integer) includedRegionFrom.getValue();
            Integer to = (Integer) includedRegionTo.getValue();

        }
        return ret;
    }

    void updateUserInputFromUI(UserInput userInput) {

        String sequenceTarget = getSequenceTarget();
        userInput.set("SEQUENCE_TARGET", sequenceTarget);

        String sequenceIncludedRegion = getSequenceIncludedRegion();
        userInput.set("SEQUENCE_INCLUDED_REGION", sequenceIncludedRegion);


        String primerProductSizeRange = getPrimerProductSizeRange();
        userInput.set("PRIMER_PRODUCT_SIZE_RANGE", primerProductSizeRange);
        if (!primerProductSizeRange.isEmpty()) {
            //userInput.set("PRIMER_PRODUCT_SIZE_RANGE", primerProductSizeRange);
        }

        String primerProductOptSize = getPrimerProductOptSize();
        userInput.set("PRIMER_PRODUCT_OPT_SIZE", primerProductOptSize);

        if (!productOptSizeCheckBoxRef.get().isSelected()) {
            userInput.set("PRIMER_PRODUCT_OPT_SIZE", "0");
        } else {
            userInput.set("PRIMER_PRODUCT_OPT_SIZE", PRIMER_PRODUCT_OPT_SIZE.getValue().toString());
        }

        //if (this.productSizeCheckBox.isSelected()) {
        String primerProductSizeRangeMin = PRIMER_PRODUCT_SIZE_RANGE_MIN.getValue().toString();
        String primerProductSizeRangeMax = PRIMER_PRODUCT_SIZE_RANGE_MAX.getValue().toString();

        userInput.set("PRIMER_PRODUCT_SIZE_RANGE", String.format("%s-%s", primerProductSizeRangeMin, primerProductSizeRangeMax));
        //} else {
        //userInput.set("PRIMER_PRODUCT_SIZE_RANGE", "");
        //}
    }

    private void hookupListeners() {
        //productSizeCheckBox.addItemListener(new RegionsProductSizeViewListeners.CheckBoxListener(this));
        sequenceIncludedRegionCheckBox.addItemListener(new RegionsProductSizeViewListeners.CheckBoxListener(this));
        sequenceTargetRegionCheckBox.addItemListener(new RegionsProductSizeViewListeners.CheckBoxListener(this));

        productOptSizeCheckBoxRef.get().addItemListener(new RegionsProductSizeViewListeners.CheckBoxListener(this));
        new SpinnerValidators.Linker(this.sequenceTargetRegionFrom, this.sequenceTargetRegionTo);
        new SpinnerValidators.Linker(this.includedRegionFrom, this.includedRegionTo);
        new SpinnerValidators.Linker(this.PRIMER_PRODUCT_SIZE_RANGE_MIN, this.PRIMER_PRODUCT_SIZE_RANGE_MAX);

        copyIncludedBtn.addActionListener(new RegionsProductSizeViewListeners.CopyLocListener(this));
        copyTargetBtn.addActionListener(new RegionsProductSizeViewListeners.CopyLocListener(this));
    }

    void populateUI(UserInput userInput, AnnotatedSeq as) {
        if (userInput == null) {
            return;
        }
        final String sequenceTarget = userInput.get("SEQUENCE_TARGET");
        sequenceTargetRegionCheckBox.setSelected(!sequenceTarget.isEmpty());
        sequenceTargetRegionFrom.setEnabled(!sequenceTarget.isEmpty());
        sequenceTargetRegionTo.setEnabled(!sequenceTarget.isEmpty());
        copyTargetBtn.setEnabled(!sequenceTarget.isEmpty());
        if (!sequenceTarget.isEmpty()) {
            String[] splits = sequenceTarget.split(",");
            int from = Integer.parseInt(splits[0]);
            int length = Integer.parseInt(splits[1]);
            int to = from + length - 1;

            sequenceTargetRegionFrom.setValue(from);
            sequenceTargetRegionTo.setValue(to);
        }

        String sequenceIncludedRegion = userInput.get("SEQUENCE_INCLUDED_REGION");
        sequenceIncludedRegionCheckBox.setSelected(!sequenceIncludedRegion.isEmpty());
        includedRegionFrom.setEnabled(!sequenceIncludedRegion.isEmpty());
        includedRegionTo.setEnabled(!sequenceIncludedRegion.isEmpty());
        copyIncludedBtn.setEnabled(!sequenceIncludedRegion.isEmpty());
        if (!sequenceIncludedRegion.isEmpty()) {
            String[] splits = sequenceIncludedRegion.split(",");
            int start = Integer.parseInt(splits[0]);
            int length = Integer.parseInt(splits[1]);
            int end = start + length - 1;
            includedRegionFrom.setValue(start);
            includedRegionTo.setValue(end);
        }

        String optSize = userInput.get("PRIMER_PRODUCT_OPT_SIZE");
        productOptSizeCheckBoxRef.get().setSelected(!optSize.equals("0") && !optSize.isEmpty());
        if (productOptSizeCheckBoxRef.get().isSelected()) {
            PRIMER_PRODUCT_OPT_SIZE.setValue(Integer.parseInt(optSize));
        } else {
            PRIMER_PRODUCT_OPT_SIZE.setValue(0);
        }

        String primerProductSizeRange = userInput.get("PRIMER_PRODUCT_SIZE_RANGE");
        //this.productSizeCheckBox.setSelected(!primerProductSizeRange.isEmpty());
        String[] sizes = primerProductSizeRange.replaceAll("-", " ").split(" ");
        if (sizes.length > 0) {
            IntList intList = new IntList(sizes);

            PRIMER_PRODUCT_SIZE_RANGE_MIN.setValue(intList.getMin());
            PRIMER_PRODUCT_SIZE_RANGE_MAX.setValue(intList.getMax());
        }
    }

    /**
     * @return <start>,<length> SEQUENCE_TARGET. default Empty
     */
    private String getSequenceTarget() {
        StringBuilder ret = new StringBuilder();
        if (sequenceTargetRegionFrom.isEnabled() && sequenceTargetRegionTo.isEnabled()) {
            int from = (Integer) sequenceTargetRegionFrom.getValue();
            int to = (Integer) sequenceTargetRegionTo.getValue();
            ret.append(from);
            ret.append(',');
            int width = to - from + 1;
            ret.append(width);
        }
        return ret.toString();
    }

    private MolPane getMolPane() {
        MolPane ret = UIUtil.getParent(this, MolPane.class);
        return ret;
    }

    /*
     * PRIMER_PRODUCT_SIZE_RANGE
     */
    String getPrimerProductSizeRange() {
        StringBuilder ret = new StringBuilder();
        if (PRIMER_PRODUCT_SIZE_RANGE_MIN.isEnabled() && PRIMER_PRODUCT_SIZE_RANGE_MAX.isEnabled()) {
            Integer minInt = (Integer) PRIMER_PRODUCT_SIZE_RANGE_MIN.getValue();
            Integer maxInt = (Integer) PRIMER_PRODUCT_SIZE_RANGE_MAX.getValue();
            String min = minInt.toString();
            String max = maxInt.toString();
            ret.append(min);
            ret.append('-');
            ret.append(max);
        }
        return ret.toString();
    }

    /*
     * PRIMER_PRODUCT_OPT_SIZE
     */
    String getPrimerProductOptSize() {
        String ret = null;
        Integer retInt = (Integer) PRIMER_PRODUCT_OPT_SIZE.getValue();
        ret = retInt.toString();
        return ret;
    }

    /**
     * @return <start>,<length>. default is empty. SEQUENCE_INCLUDED_REGION
     */
    String getSequenceIncludedRegion() {
        StringBuilder ret = new StringBuilder();
        if (includedRegionFrom.isEnabled() && includedRegionTo.isEnabled()) {
            Integer from = (Integer) includedRegionFrom.getValue();
            Integer to = (Integer) includedRegionTo.getValue();
            ret.append(from.toString());
            ret.append(',');
            int width = to - from + 1;
            ret.append(width);
        }
        return ret.toString();
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel panel = createPanel();
        add(panel, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);
    }

    private JPanel createTargetRegionPanel(JPanel targetRegionPanel) {
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        sequenceTargetRegionCheckBox = new JCheckBox();
        sequenceTargetRegionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
        sequenceTargetRegionCheckBox.setText("Target Region");
        sequenceTargetRegionCheckBox.setActionCommand("targetRegion");
        RichSeparator rs = new RichSeparator(sequenceTargetRegionCheckBox);
        targetRegionPanel.add(rs, c);

        addTargetRegionRow();
        return targetRegionPanel;
    }

    private void addTargetRegionRow() {
        JPanel tmp = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        //
        int gridy = 0;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        sequenceTargetRegionFrom = new JSpinner();
        sequenceTargetRegionFrom.setName("sequenceTargetRegionFrome");
        sequenceTargetRegionFrom.setModel(new SpinnerNumberModel(1, 1, null, 1));
        UIUtil.setPreferredWidthByPrototype(sequenceTargetRegionFrom, 1000);
        tmp.add(sequenceTargetRegionFrom, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        tmp.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        sequenceTargetRegionTo = new JSpinner();
        sequenceTargetRegionTo.setName("sequenceTargetRegionTo");
        sequenceTargetRegionTo.setModel(new SpinnerNumberModel(1, 1, null, 1));
        UIUtil.setPreferredWidthByPrototype(sequenceTargetRegionTo, 10000);
        tmp.add(sequenceTargetRegionTo, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        copyTargetBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.SELECT_16));
        copyTargetBtn.setActionCommand("target");
        tmp.add(copyTargetBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        targetRegionPanel.add(tmp, c);
    }

    private JPanel createIncludedRegionPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        sequenceIncludedRegionCheckBox = new JCheckBox();
        sequenceIncludedRegionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
        sequenceIncludedRegionCheckBox.setText("Included Region");
        sequenceIncludedRegionCheckBox.setActionCommand("includedRegion");
        RichSeparator rs = new RichSeparator(sequenceIncludedRegionCheckBox);
        ret.add(rs, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        JPanel tmp = new JPanel(new GridBagLayout());
        ret.add(tmp, c);

        // add contents to tmp
        gridy = 0;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        includedRegionFrom = new JSpinner();
        includedRegionFrom.setModel(new SpinnerNumberModel(1, 1, null, 1));
        includedRegionFrom.setEnabled(false);
        UIUtil.setPreferredWidthByPrototype(includedRegionFrom, 1000);
        tmp.add(includedRegionFrom, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        tmp.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        includedRegionTo = new JSpinner();
        includedRegionTo.setModel(new SpinnerNumberModel(1, 1, null, 1));
        includedRegionTo.setEnabled(false);
        UIUtil.setPreferredWidthByPrototype(includedRegionTo, 10000);
        tmp.add(includedRegionTo, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        copyIncludedBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.SELECT_16));
        copyIncludedBtn.setActionCommand("included");
        tmp.add(copyIncludedBtn, c);
        return ret;
    }

    private JPanel createProductSizePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        productSizeCheckBox = new JLabel();

        productSizeCheckBox.setText("Product Size");
        c.gridx = 0;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator(productSizeCheckBox);
        ret.add(rs, c);

        JPanel tmp = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        ret.add(tmp, c);

        gridy = 0;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        PRIMER_PRODUCT_SIZE_RANGE_MIN = new JSpinner();
        PRIMER_PRODUCT_SIZE_RANGE_MIN.setModel(new SpinnerNumberModel(1, 1, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PRODUCT_SIZE_RANGE_MIN, 1000);
        tmp.add(PRIMER_PRODUCT_SIZE_RANGE_MIN, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        tmp.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        PRIMER_PRODUCT_SIZE_RANGE_MAX = new JSpinner();
        PRIMER_PRODUCT_SIZE_RANGE_MAX.setModel(new SpinnerNumberModel(1, 1, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PRODUCT_SIZE_RANGE_MAX, 10000);
        tmp.add(PRIMER_PRODUCT_SIZE_RANGE_MAX, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        FlatBtn emptyBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.EMPTY_16), false);
        tmp.add(emptyBtn, c);
        return ret;
    }

    private JPanel createPanel() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints c = null;
        int gridy = 0;

        // target region
        c = new GridBagConstraints();
        targetRegionPanel = new JPanel(new GridBagLayout());
        createTargetRegionPanel(targetRegionPanel);
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(targetRegionPanel, c);

        // included region
        JPanel includedRegionPanel = createIncludedRegionPanel();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(includedRegionPanel, c);

        // product size        
        JPanel productSizePanel = createProductSizePanel();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(productSizePanel, c);

        // product opt size
        JPanel productOptSizePanel = createProductOptSizePanel();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(productOptSizePanel, c);

        return ret;
    }

    private JPanel createProductOptSizePanel() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints c = null;
        int gridy = 0;
        // product size        
        c = new GridBagConstraints();
        JCheckBox productOptSizeCheckBox = new JCheckBox();
        productOptSizeCheckBoxRef = new WeakReference<JCheckBox>(productOptSizeCheckBox);
        productOptSizeCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
        productOptSizeCheckBox.setSelected(true);
        productOptSizeCheckBox.setActionCommand("productOptSize");
        productOptSizeCheckBox.setText("Product Opt Size");
        c.gridx = 0;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator(productOptSizeCheckBox);
        ret.add(rs, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.CENTER;
        PRIMER_PRODUCT_OPT_SIZE = new JSpinner();
        PRIMER_PRODUCT_OPT_SIZE.setModel(new SpinnerNumberModel(1, 0, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PRODUCT_OPT_SIZE, 100000);
        ret.add(PRIMER_PRODUCT_OPT_SIZE, c);

        return ret;
    }

    JSpinner getIncludedRegionFrom() {
        return includedRegionFrom;
    }

    JSpinner getIncludedRegionTo() {
        return includedRegionTo;
    }

    JSpinner getPRIMER_PRODUCT_SIZE_RANGE_MAX() {
        return PRIMER_PRODUCT_SIZE_RANGE_MAX;
    }

    JSpinner getPRIMER_PRODUCT_SIZE_RANGE_MIN() {
        return PRIMER_PRODUCT_SIZE_RANGE_MIN;
    }

    JSpinner getPRIMER_PRODUCT_OPT_SIZE() {
        return PRIMER_PRODUCT_OPT_SIZE;
    }

    JSpinner getSequenceTargetRegionFrom() {
        return sequenceTargetRegionFrom;
    }

    JSpinner getSequenceTargetRegionTo() {
        return sequenceTargetRegionTo;
    }
    private JSpinner PRIMER_PRODUCT_SIZE_RANGE_MAX;
    JSpinner PRIMER_PRODUCT_SIZE_RANGE_MIN;
    private JSpinner PRIMER_PRODUCT_OPT_SIZE;
    private JCheckBox sequenceIncludedRegionCheckBox;
    JSpinner includedRegionFrom;
    JSpinner includedRegionTo;
    JLabel productSizeCheckBox;
    private JCheckBox sequenceTargetRegionCheckBox;
    JSpinner sequenceTargetRegionFrom;
    JSpinner sequenceTargetRegionTo;
    FlatBtn copyIncludedBtn;
    JButton copyTargetBtn;
    private WeakReference<JCheckBox> productOptSizeCheckBoxRef;

    @Override
    public void expanded() {
        MolPane molPane = getMolPane();
        if (molPane == null) {
            return;
        }
        AnnotatedSeq as = molPane.getAs();
        SpinnerNumberModel modelIncludedTo = (SpinnerNumberModel) includedRegionTo.getModel();
        modelIncludedTo.setMaximum(as.getLength());

        SpinnerNumberModel modelTargetTo = (SpinnerNumberModel) sequenceTargetRegionTo.getModel();
        modelTargetTo.setMaximum(as.getLength());
    }
}
