/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.task;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.SpinnerValidators;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.main.ui.molpane.MolPane;
import com.gas.domain.core.primer3.UserInput;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dq
 */
public class PickPanel extends JPanel {

    enum TYPE {

        LEFT, RIGHT, INTERNAL
    };
    TYPE type;
    JCheckBox pickCheckbox;
    JCheckBox useExistingBox;
    WeakReference<JSpinner> fromSpinnerRef;
    WeakReference<JSpinner> toSpinnerRef;
    WeakReference<FlatBtn> copyLocBtnRef;

    PickPanel(TYPE type) {
        this.type = type;

        initComponents();
        hookupListeners();
    }

    public boolean isPicking() {
        return pickCheckbox.isSelected() && !useExistingBox.isSelected();
    }
    
    boolean isUseExisting(){
        return useExistingBox.isSelected();
    }

    private void initComponents() {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        pickCheckbox = new JCheckBox();
        pickCheckbox.setActionCommand("pick");
        if (type == TYPE.LEFT) {
            pickCheckbox.setText("<html>Forward Primer<font color='GRAY'>, or use existing</font></html>");
        } else if (type == TYPE.INTERNAL) {
            pickCheckbox.setText("<html>DNA Probe<font color='GRAY'>, or use existing</font></html>");
        } else if (type == TYPE.RIGHT) {
            pickCheckbox.setText("<html>Reverse Primer<font color='GRAY'>, or use existing</font></html>");
        }
        add(pickCheckbox, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel tmp = new JPanel();
        add(tmp, c);

        useExistingBox = new JCheckBox();
        useExistingBox.setActionCommand("useExisting");
        tmp.add(useExistingBox);

        JSpinner fromSpinner = new JSpinner();
        fromSpinnerRef = new WeakReference<JSpinner>(fromSpinner);
        fromSpinner.setModel(new SpinnerNumberModel(1, 1, null, 1));
        UIUtil.setPreferredWidthByPrototype(fromSpinner, 10000);
        tmp.add(fromSpinner);

        tmp.add(new JLabel("-"));

        JSpinner toSpinner = new JSpinner();
        toSpinnerRef = new WeakReference<JSpinner>(toSpinner);
        toSpinner.setModel(new SpinnerNumberModel(2, 1, null, 1));
        UIUtil.setPreferredWidthByPrototype(toSpinner, 10000);
        tmp.add(toSpinner);

        FlatBtn copyLocBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.SELECT_16));
        copyLocBtnRef = new WeakReference<FlatBtn>(copyLocBtn);
        tmp.add(copyLocBtn);
    }

    private void hookupListeners() {
        pickCheckbox.addItemListener(new PickPanelListeners.BoxListener());
        useExistingBox.addItemListener(new PickPanelListeners.BoxListener());
        copyLocBtnRef.get().addActionListener(new PickPanelListeners.CopyLocListener(this));
        fromSpinnerRef.get().addChangeListener(null);

        new SpinnerValidators.Linker(fromSpinnerRef.get(), toSpinnerRef.get());
    }

    List<String> validateInput(AnnotatedSeq as) {
        List<String> ret = new ArrayList<String>();
        int totalPos = as.getSiquence().getData().length();

        if (useExistingBox.isEnabled() && useExistingBox.isSelected()) {
            Integer from = (Integer) fromSpinnerRef.get().getValue();
            Integer to = (Integer) toSpinnerRef.get().getValue();
            int width = LocUtil.width(from, to, totalPos).intValue();
            String errorMsg = null;
            if (width > IPrimer3Service.PRIMER_LENGTH_MAX) {
                if (type == TYPE.LEFT) {
                    errorMsg = String.format("Forward primer size exceeds the maximum of %d", IPrimer3Service.PRIMER_LENGTH_MAX);
                } else if (type == TYPE.RIGHT) {
                    errorMsg = String.format("Reverse primer size exceeds the maximum of %d", IPrimer3Service.PRIMER_LENGTH_MAX);
                } else if (type == TYPE.INTERNAL) {
                    errorMsg = String.format("DNA probe size exceeds the maximum of %d", IPrimer3Service.PRIMER_LENGTH_MAX);
                } else {
                    throw new UnsupportedOperationException();
                }

            } else if (width == 0) {
                if (type == TYPE.LEFT) {
                    errorMsg = String.format("Forward primer size cannot be less than %d", IPrimer3Service.PRIMER_LENGTH_MIN);
                } else if (type == TYPE.RIGHT) {
                    errorMsg = String.format("Reverse primer size cannot be less than %d", IPrimer3Service.PRIMER_LENGTH_MIN);
                } else if (type == TYPE.INTERNAL) {
                    errorMsg = String.format("DNA probe size cannot be less than %d", IPrimer3Service.PRIMER_LENGTH_MIN);
                } else {
                    throw new UnsupportedOperationException();
                }
            }
            if (errorMsg != null) {
                ret.add(errorMsg);
            }
        }

        return ret;
    }

    void populateUI(UserInput userInput) {
        String pick = null;
        String seq = null;
        if (type == TYPE.LEFT) {
            pick = userInput.get("PRIMER_PICK_LEFT_PRIMER");
            seq = userInput.get("SEQUENCE_PRIMER");
        } else if (type == TYPE.RIGHT) {
            pick = userInput.get("PRIMER_PICK_RIGHT_PRIMER");
            seq = userInput.get("SEQUENCE_PRIMER_REVCOMP");
        } else if (type == TYPE.INTERNAL) {
            pick = userInput.get("PRIMER_PICK_INTERNAL_OLIGO");
            seq = userInput.get("SEQUENCE_INTERNAL_OLIGO");
        } else {
            throw new UnsupportedOperationException();
        }
        pickCheckbox.setSelected("1".equals(pick));

        fromSpinnerRef.get().setEnabled(!seq.isEmpty());
        toSpinnerRef.get().setEnabled(!seq.isEmpty());
        copyLocBtnRef.get().setEnabled(!seq.isEmpty());
        useExistingBox.setSelected(!seq.isEmpty());
        if (!seq.isEmpty()) {
            MolPane molPane = getMolPane();
            String seqAs = molPane.getAs().getSiquence().getData();
            Loc loc = BioUtil.find(seqAs, seq);

            if ((type == TYPE.LEFT || type == TYPE.INTERNAL) && loc.isStrand()) {
                fromSpinnerRef.get().setValue(loc.getStart());
                toSpinnerRef.get().setValue(loc.getEnd());
            } else if (type == TYPE.RIGHT && !loc.isStrand()) {
                fromSpinnerRef.get().setValue(loc.getStart());
                toSpinnerRef.get().setValue(loc.getEnd());
            }
        }
    }

    MolPane getMolPane() {
        MolPane ret = UIUtil.getParent(this, MolPane.class);
        return ret;
    }

    void updateUserInputFromUI(UserInput userInput, AnnotatedSeq as) {
        final String seq = as.getSiquence().getData();        
        final Integer totalLength = as.getLength();
        if (type == TYPE.LEFT) {
            userInput.set("PRIMER_PICK_LEFT_PRIMER", pickCheckbox.isSelected() ? "1" : "0");
            if (useExistingBox.isEnabled() && useExistingBox.isSelected()) {
                Integer from = (Integer) fromSpinnerRef.get().getValue();
                Integer to = (Integer) toSpinnerRef.get().getValue();
                String primer = StrUtil.sub(seq, from, to);
                userInput.set("SEQUENCE_PRIMER", primer);
            }else{
                userInput.removeEntries("SEQUENCE_PRIMER");
            }
        } else if (type == TYPE.INTERNAL) {
            userInput.set("PRIMER_PICK_INTERNAL_OLIGO", pickCheckbox.isSelected() ? "1" : "0");
            if (useExistingBox.isEnabled() && useExistingBox.isSelected()) {
                Integer from = (Integer) fromSpinnerRef.get().getValue();
                Integer to = (Integer) toSpinnerRef.get().getValue();
                String primer = StrUtil.sub(seq, from, to);
                userInput.set("SEQUENCE_INTERNAL_OLIGO", primer);
            }else{
                userInput.removeEntries("SEQUENCE_INTERNAL_OLIGO");
            }       
        } else if (type == TYPE.RIGHT) {
            userInput.set("PRIMER_PICK_RIGHT_PRIMER", pickCheckbox.isSelected() ? "1" : "0");
            if (useExistingBox.isEnabled() && useExistingBox.isSelected()) {
                Integer from = (Integer) fromSpinnerRef.get().getValue();
                Integer to = (Integer) toSpinnerRef.get().getValue();
                
                String primer = StrUtil.sub(seq, from, to);
                primer = BioUtil.reverseComplement(primer);
                userInput.set("SEQUENCE_PRIMER_REVCOMP", primer);
            }else{
                userInput.removeEntries("SEQUENCE_PRIMER_REVCOMP");
            }              
        }
    }
}
