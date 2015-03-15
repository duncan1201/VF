/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
public class SeqSelectPanel extends JPanel {

    WeakReference<JRadioButton> wholeRef;
    WeakReference<JRadioButton> selectRef;
    WeakReference<JRadioButton> directRef;
    WeakReference<JRadioButton> complementaryRef;

    public SeqSelectPanel() {
        this(true, true, true, true);
    }

    public SeqSelectPanel(Boolean wholeSeq, Boolean directStrand, boolean range, boolean strand) {
        setBorder(UIUtil.createDefaultBorder());
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        if (range) {
            c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            JPanel rangePanel = createRangePanel(wholeSeq);
            add(rangePanel, c);
        }

        if (strand) {
            c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            JPanel strandPanel = createStrandPanel(directStrand);
            add(strandPanel, c);
        }
        if (directStrand == null) {
            if (directRef != null && directRef.get() != null) {
                directRef.get().setSelected(true);
            }
            if (wholeRef != null && wholeRef.get() != null) {
                wholeRef.get().setSelected(true);
            }
        }
    }

    public boolean isDirectStrand() {
        return directRef.get().isSelected();
    }

    public boolean isComplementaryStrand() {
        return complementaryRef.get().isSelected();
    }

    public boolean isSelectionOnly() {
        return selectRef.get().isSelected();
    }

    public boolean isWholeSequence() {
        return wholeRef.get().isSelected();
    }

    private JPanel createStrandPanel(Boolean directStrand) {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Strand Selection", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JRadioButton direct = new JRadioButton("Direct");
        direct.setSelected(directStrand != null && directStrand);
        directRef = new WeakReference<JRadioButton>(direct);
        ret.add(direct, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JRadioButton complementary = new JRadioButton("Complementary");
        complementary.setSelected(directStrand != null && !directStrand);
        complementaryRef = new WeakReference<JRadioButton>(complementary);
        ret.add(complementary, c);

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(direct);
        bGroup.add(complementary);

        return ret;
    }

    private JPanel createRangePanel(Boolean wholeSeq) {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Range Selection", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JRadioButton whole = new JRadioButton("Whole Sequence");
        whole.setSelected(wholeSeq != null && wholeSeq);
        wholeRef = new WeakReference<JRadioButton>(whole);
        ret.add(whole, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JRadioButton select = new JRadioButton("Selection Only");
        select.setSelected(wholeSeq != null && !wholeSeq);
        selectRef = new WeakReference<JRadioButton>(select);
        ret.add(select, c);

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(whole);
        bGroup.add(select);

        return ret;
    }
}
