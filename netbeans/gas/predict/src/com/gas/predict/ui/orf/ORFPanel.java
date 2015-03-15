/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.common.ui.button.WideComboBox;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.GeneticCodeTableList;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.domain.core.orf.api.ORFParam;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.ui.editor.IMolPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class ORFPanel extends JPanel {

    ORFResult orfResult;
    private IGeneticCodeTableService tableService = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
    private WeakReference<WideComboBox> geneticCodeBoxRef;
    private WeakReference<JTextField> stopCodonsFieldRef;
    private WeakReference<JTextField> startCodonsFieldRef;
    private WeakReference<JSpinner> minLengthSpinnerRef;
    private WeakReference<JCheckBox> plus1Ref;
    private WeakReference<JCheckBox> plus2Ref;
    private WeakReference<JCheckBox> plus3Ref;
    private WeakReference<JCheckBox> minus1Ref;
    private WeakReference<JCheckBox> minus2Ref;
    private WeakReference<JCheckBox> minus3Ref;
    private WeakReference<JCheckBox> includeStopBoxRef;
    private boolean updatingUI;

    ORFPanel() {
        initComponents();
        hookupListeners();
        initUIValues();
    }

    private void initUIValues() {
        updatingUI = true;
        GeneticCodeTable codeTable = (GeneticCodeTable) geneticCodeBoxRef.get().getSelectedItem();
        startCodonsFieldRef.get().setText(codeTable.getStartCodons().getBases().toString(' '));
        stopCodonsFieldRef.get().setText(codeTable.getStopCodons().getBases().toString(' '));
        updatingUI = false;
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Genetic Code:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel geneticCodePanel = createGeneticCodeComboPanel();
        final String prototype = "12345678901234567890123";
        FontMetrics fm = FontUtil.getFontMetrics(this);
        final int width = fm.stringWidth(prototype);
        UIUtil.setPreferredWidth(geneticCodePanel, width);
        add(geneticCodePanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Min Length:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        SpinnerNumberModel model = new SpinnerNumberModel(100, 3, 300, 1);
        JSpinner minLengthSpinner = new JSpinner(model);
        minLengthSpinnerRef = new WeakReference<JSpinner>(minLengthSpinner);
        add(minLengthSpinner, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Start Codons:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JTextField startCodonsField = new JTextField();
        startCodonsFieldRef = new WeakReference<JTextField>(startCodonsField);
        add(startCodonsField, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Stop Codons:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JTextField stopCodonsField = new JTextField();
        stopCodonsFieldRef = new WeakReference<JTextField>(stopCodonsField);
        add(stopCodonsField, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Frames:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        add(createFramesPanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JCheckBox includeStopBox = new JCheckBox();
        includeStopBoxRef = new WeakReference<JCheckBox>(includeStopBox);
        includeStopBox.setText("Include Stop Codons");
        add(includeStopBox, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        add(comp, c);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ORFPanelListeners.PtyListener());
        getGeneticCodeBox().addActionListener(new ORFPanelListeners.GeneticCodeComboListener(this));
    }

    List<String> validateInput() {
        populateFromUI();
        return orfResult.getOrfParams().validate();
    }

    public void setOrfResult(ORFResult orfResult) {
        ORFResult old = this.orfResult;
        this.orfResult = orfResult;
        firePropertyChange("orfResult", old, this.orfResult);
    }

    private JPanel createFramesPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox plus1 = new JCheckBox("+1");
        plus1.setActionCommand("1");
        plus1Ref = new WeakReference<JCheckBox>(plus1);
        ret.add(plus1, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox plus2 = new JCheckBox("+2");
        plus2.setActionCommand("2");
        plus2Ref = new WeakReference<JCheckBox>(plus2);
        ret.add(plus2, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox plus3 = new JCheckBox("+3");
        plus3.setActionCommand("3");
        plus3Ref = new WeakReference<JCheckBox>(plus3);
        ret.add(plus3, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox minus1 = new JCheckBox("-1");
        minus1.setActionCommand("-1");
        minus1Ref = new WeakReference<JCheckBox>(minus1);
        ret.add(minus1, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox minus2 = new JCheckBox("-2");
        minus2.setActionCommand("-2");
        minus2Ref = new WeakReference<JCheckBox>(minus2);
        ret.add(minus2, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JCheckBox minus3 = new JCheckBox("-3");
        minus3.setActionCommand("-3");
        minus3Ref = new WeakReference<JCheckBox>(minus3);
        ret.add(minus3, c);
        return ret;
    }

    protected void turnOnAllFrames() {
        plus1Ref.get().setSelected(true);
        plus2Ref.get().setSelected(true);
        plus3Ref.get().setSelected(true);
        minus1Ref.get().setSelected(true);
        minus2Ref.get().setSelected(true);
        minus3Ref.get().setSelected(true);
    }

    protected void populateUI(ORFParam param) {
        updatingUI = true;
        getMinLengthSpinner().setValue(param.getMinLength());
        getIncludeStopBox().setSelected(param.isStopCodonIncluded());
        Integer geneticCodeTableId = param.getGeneticCodeTableId();
        selectGeneticCodeTableCombo(geneticCodeTableId);

        if (param.getStartCodons().isEmpty() || param.getStopCodons().isEmpty()) {
            GeneticCodeTable codeTable = (GeneticCodeTable)getGeneticCodeBox().getSelectedItem();
            if (param.getStartCodons().isEmpty()) {
                Set<String> startCodons = codeTable.getStartCodons().getBasesAsSet();
                param.setStartCodons(startCodons);
            }
            if (param.getStopCodons().isEmpty()) {
                Set<String> startCodons = codeTable.getStopCodons().getBasesAsSet();
                param.setStopCodons(startCodons);
            }
        }

        getStartCodonsField().setText(param.getStartCodonsStr());

        getStopCodonsField().setText(param.getStopCodonsStr());
        Set<Integer> frames = param.getFrames();
        plus1Ref.get().setSelected(frames.contains(1));
        plus2Ref.get().setSelected(frames.contains(2));
        plus3Ref.get().setSelected(frames.contains(3));
        minus1Ref.get().setSelected(frames.contains(-1));
        minus2Ref.get().setSelected(frames.contains(-2));
        minus3Ref.get().setSelected(frames.contains(-3));
        updatingUI = false;
    }

    private void selectGeneticCodeTableCombo(Integer geneticCodeTableId) {
        if(geneticCodeTableId == null){
            return;
        }
        WideComboBox box = getGeneticCodeBox();
        for (int i = 0; i < box.getItemCount(); i++) {
            GeneticCodeTable table = (GeneticCodeTable) box.getItemAt(i);
            Integer tId = table.getId();
            if (tId.equals(geneticCodeTableId)) {
                box.setSelectedIndex(i);
                break;
            }
        }
    }

    void populateFromUI() {
        String startCodons = getStartCodonsField().getText().trim();
        orfResult.getOrfParams().setStartCodons(startCodons);

        String stopCodons = getStopCodonsField().getText().trim();
        orfResult.getOrfParams().setStopCodons(stopCodons);

        orfResult.getOrfParams().setMinLength((Integer) minLengthSpinnerRef.get().getValue());
        GeneticCodeTable table = (GeneticCodeTable) getGeneticCodeBox().getSelectedItem();
        orfResult.getOrfParams().setGeneticCodeTableId(table.getId());

        orfResult.getOrfParams().setStopCodonIncluded(getIncludeStopBox().isSelected());

        orfResult.getOrfParams().getFrames().clear();

        if (plus1Ref.get().isSelected()) {
            orfResult.getOrfParams().getFrames().add(1);
        }
        if (plus2Ref.get().isSelected()) {
            orfResult.getOrfParams().getFrames().add(2);
        }
        if (plus3Ref.get().isSelected()) {
            orfResult.getOrfParams().getFrames().add(3);
        }
        if (minus1Ref.get().isSelected()) {
            orfResult.getOrfParams().getFrames().add(-1);
        }
        if (minus2Ref.get().isSelected()) {
            orfResult.getOrfParams().getFrames().add(-2);
        }
        if (minus3Ref.get().isSelected()) {
            orfResult.getOrfParams().getFrames().add(-3);
        }
    }

    protected WideComboBox getGeneticCodeBox() {
        return geneticCodeBoxRef.get();
    }

    protected JCheckBox getIncludeStopBox() {
        return includeStopBoxRef.get();
    }

    protected JSpinner getMinLengthSpinner() {
        return minLengthSpinnerRef.get();
    }

    protected JTextField getStartCodonsField() {
        return startCodonsFieldRef.get();
    }

    protected JTextField getStopCodonsField() {
        return stopCodonsFieldRef.get();
    }

    private JPanel createGeneticCodeComboPanel() {
        JPanel ret = new JPanel(new BorderLayout());
        GeneticCodeTableList tableList = tableService.getTables();
        WideComboBox geneticCodeBox = new WideComboBox(tableList);
        geneticCodeBoxRef = new WeakReference<WideComboBox>(geneticCodeBox);
        ret.add(geneticCodeBox, BorderLayout.CENTER);
        return ret;
    }

    void save() {
        if (updatingUI) {
            return;
        }
        IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
        IMolPane molPane = UIUtil.getParent(this, IMolPane.class);
        if (molPane != null) {
            populateFromUI();
            AnnotatedSeq as = molPane.getAs();
            asService.merge(as);
        }
    }
}