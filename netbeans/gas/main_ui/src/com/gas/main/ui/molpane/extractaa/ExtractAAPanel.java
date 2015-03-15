/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.extractaa;

import com.gas.common.ui.button.WideComboBox;
import com.gas.common.ui.core.IntList;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.GeneticCodeTableList;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class ExtractAAPanel extends JPanel {

    JCheckBox plus1, plus2, plus3, minus1, minus2, minus3;
    LocList selections;
    int totalLength;
    WideComboBox table;
    JCheckBox relativeStart;
    JRadioButton entireBtn;
    JRadioButton selectedRegionBtn;
    DialogDescriptor dialogDescriptor;
    NotificationLineSupport notificationLineSupport;
    private IGeneticCodeTableService tableService = Lookup.getDefault().lookup(IGeneticCodeTableService.class);

    ExtractAAPanel(LocList selections, int totalLength) {
        this.selections = selections;
        this.totalLength = totalLength;
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(createTargetPanel(selections), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(createTranslateParameters(!selections.isEmpty()), c);

        hookupListeners();
    }

    private void hookupListeners() {
        plus1.addItemListener(new ExtractAAPanelListeners.FramesListeners());
        plus2.addItemListener(new ExtractAAPanelListeners.FramesListeners());
        plus3.addItemListener(new ExtractAAPanelListeners.FramesListeners());
        minus1.addItemListener(new ExtractAAPanelListeners.FramesListeners());
        minus2.addItemListener(new ExtractAAPanelListeners.FramesListeners());
        minus3.addItemListener(new ExtractAAPanelListeners.FramesListeners());

        entireBtn.addActionListener(new ExtractAAPanelListeners.TargetsListener());
        selectedRegionBtn.addActionListener(new ExtractAAPanelListeners.TargetsListener());
    }

    GeneticCodeTable getSelectedTable() {
        GeneticCodeTable ret = (GeneticCodeTable) table.getSelectedItem();
        return ret;
    }

    private int[] getSelectedFrames() {
        IntList ret = new IntList();
        if (plus1.isSelected()) {
            ret.add(1);
        }
        if (plus2.isSelected()) {
            ret.add(2);
        }
        if (plus3.isSelected()) {
            ret.add(3);
        }
        if (minus1.isSelected()) {
            ret.add(-1);
        }
        if (minus2.isSelected()) {
            ret.add(-2);
        }
        if (minus3.isSelected()) {
            ret.add(-3);
        }
        return ret.toPrimitiveArray();
    }

    private JPanel createTranslateParameters(boolean hasSelection) {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Genetic Codes:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        GeneticCodeTableList tableList = tableService.getTables();
        table = new WideComboBox(tableList);
        JPanel tmp = new JPanel(new BorderLayout());
        tmp.add(table, BorderLayout.CENTER);
        final String prototype = "12345678901234567890123";
        FontMetrics fm = FontUtil.getFontMetrics(this);
        final int width = fm.stringWidth(prototype);
        UIUtil.setPreferredWidth(tmp, width);
        ret.add(tmp, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Frames:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(createFramesPanel(), c);

        if (hasSelection) {
            c = new GridBagConstraints();
            c.gridy = gridy;
            ret.add(new JLabel(""), c);

            c = new GridBagConstraints();
            c.gridy = gridy;
            c.fill = GridBagConstraints.HORIZONTAL;
            relativeStart = new JCheckBox("Start from the selected region");
            ret.add(relativeStart, c);
        }
        return ret;
    }

    int[] getSelectedRelativeFrames() {
        IntList ret = new IntList();
        final int[] frames = getSelectedFrames();
        if (isEntireSeq()) {
            return frames;
        } else {
            Loc loc = selections.iterator().next();
            if (isRelativeStart()) {
                return frames;
            } else {
                for (int frame : frames) {
                    int mod;
                    if (frame > 0) {
                        mod = loc.getStart() % 3;
                    } else {
                        mod = LocUtil.reverseComplement(loc.getEnd(), totalLength) % 3;
                    }
                    final int direction = frame > 0 ? 1 : -1;
                    if (mod == 1) {
                        ret.add(frame);
                    } else if (mod == 2) {
                        int newFrame = Math.abs(frame) + 2;
                        if (newFrame > 3) {
                            newFrame = newFrame % 3;
                        }
                        ret.add(newFrame * direction);
                    } else if (mod == 0) {
                        int newFrame = frame + 1;
                        if (newFrame > 3) {
                            newFrame = newFrame % 3;
                        }
                        ret.add(newFrame * direction);
                    }
                }
            }
            return ret.toPrimitiveArray();
        }
    }

    private boolean isRelativeStart() {
        return relativeStart.isSelected();
    }

    void validateInput() {
        if (notificationLineSupport == null) {
            return;
        }
        if (!plus1.isSelected() && !plus2.isSelected() && !plus3.isSelected()
                && !minus1.isSelected() && !minus2.isSelected() && !minus3.isSelected()) {
            this.notificationLineSupport.setInformationMessage("Please select at least one frame");
            this.dialogDescriptor.setValid(false);
        } else {
            this.notificationLineSupport.clearMessages();
            this.dialogDescriptor.setValid(true);
        }
    }

    void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }

    private JPanel createFramesPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        plus1 = new JCheckBox("+1");
        plus1.setActionCommand("1");
        ret.add(plus1, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        plus2 = new JCheckBox("+2");
        plus2.setActionCommand("2");
        ret.add(plus2, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        plus3 = new JCheckBox("+3");
        plus3.setActionCommand("3");
        ret.add(plus3, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        minus1 = new JCheckBox("-1");
        minus1.setActionCommand("-1");
        ret.add(minus1, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        minus2 = new JCheckBox("-2");
        minus2.setActionCommand("-2");
        ret.add(minus2, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        minus3 = new JCheckBox("-3");
        minus3.setActionCommand("-3");
        ret.add(minus3, c);

        return ret;
    }

    private JPanel createTargetPanel(LocList selections) {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = gridy;
        entireBtn = new JRadioButton("Entire Sequence", selections.isEmpty());
        entireBtn.setActionCommand("entire");
        ret.add(entireBtn, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        selectedRegionBtn = new JRadioButton();
        if (selections.isEmpty()) {
            selectedRegionBtn.setText("Selected Region");
        } else {
            Loc loc = selections.iterator().next();
            selectedRegionBtn.setText(String.format("Selected Region(%s)", loc.toString(false, false)));
        }
        selectedRegionBtn.setSelected(!selections.isEmpty());
        selectedRegionBtn.setActionCommand("selected");
        selectedRegionBtn.setEnabled(!selections.isEmpty());
        ret.add(selectedRegionBtn, c);

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(entireBtn);
        bGroup.add(selectedRegionBtn);

        return ret;
    }

    boolean isEntireSeq() {
        return this.entireBtn.isSelected();
    }

    boolean isSelectedRegion() {
        return this.selectedRegionBtn.isSelected();
    }
}
