/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.primer3.OverlapPrimer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class PrimerDetailsPanel extends JPanel {

    private JButton backBtn;
    PrimerDetails primerDetails;
    WarningsPanel warningsPanel;

    PrimerDetailsPanel() {
        initComponents();

        hookupListeners();
    }

    void setOverlapPrimer(OverlapPrimer overlapPrimer) {
        primerDetails.setOverlapPrimer(overlapPrimer);
        if (overlapPrimer.getProblemCount() > 0) {
            warningsPanel.setProblems(overlapPrimer.getProblemList());
        } else {
            warningsPanel.setProblems(Collections.EMPTY_LIST);
        }
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        RichSeparator separator = new RichSeparator("Primer Details");
        backBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.BR_PREV_16));
        backBtn.setActionCommand("back");
        separator.setRightDecoratioin(backBtn);
        add(separator, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        primerDetails = new PrimerDetails();
        add(primerDetails, c);

        warningsPanel = new WarningsPanel();
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(warningsPanel, c);
    }

    private void hookupListeners() {
        backBtn.addActionListener(new PrimerDetailsPanelListeners.BtnListeners());
    }

    static class WarningsPanel extends JPanel {

        JPanel contentPanel = new JPanel();

        WarningsPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            JPanel titledPanel = createTitledPanel();
            add(titledPanel, c);
        }

        JPanel createTitledPanel() {
            JPanel ret = new JPanel(new GridBagLayout());
            GridBagConstraints c;

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            JLabel label = new JLabel("Warnings");
            label.setFont(label.getFont());
            ret.add(label, c);

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            JLabel label2 = new JLabel();
            final float fontSize = FontUtil.getSmallFontSize();
            label2.setFont(label2.getFont().deriveFont(Font.ITALIC).deriveFont(fontSize));
            label2.setText("(please note: Warnings are for guidance only)");
            ret.add(label2, c);
            return ret;
        }

        void setProblems(List<String> problems) {
            if (contentPanel != null) {
                remove(contentPanel);
            }

            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 1;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;
            final FontMetrics fm = FontUtil.getDefaultFontMetrics();
            c.insets = new Insets(0,fm.charWidth('A'),0,0);
            contentPanel = createContentPanel(problems);            
            add(contentPanel, c);
            revalidate();
        }

        private JPanel createContentPanel(List<String> problems) {
            JPanel ret = new JPanel(new GridBagLayout());
            int gridy = 0;
            GridBagConstraints c;
            if (!problems.isEmpty()) {
                for (int i = 0; i < problems.size(); i++) {
                    gridy = i / 2 + 1;
                    String p = problems.get(i);
                    c = new GridBagConstraints();
                    c.gridy = gridy;
                    c.anchor = GridBagConstraints.NORTHWEST;
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.weightx = 1;
                    JLabel label = new JLabel();
                    float fontSize = label.getFont().getSize2D() - 1;
                    label.setFont(label.getFont().deriveFont(fontSize));
                    label.setIcon(ImageHelper.createImageIcon(ImageNames.BLACK_SMALL_DOT_16));
                    label.setText(p);
                    ret.add(label, c);
                }
                // add fillers
                c = new GridBagConstraints();
                c.gridy = gridy;
                c.fill = GridBagConstraints.VERTICAL;
                c.weighty = 1;
                Component filler = Box.createRigidArea(new Dimension(1, 1));
                ret.add(filler, c);
            } else {
                c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                c.anchor = GridBagConstraints.NORTHWEST;
                JLabel label = new JLabel();
                float fontSize = label.getFont().getSize2D() - 1;
                label.setFont(label.getFont().deriveFont(fontSize).deriveFont(Font.ITALIC));
                label.setText("There are no warnings for the selected primer");
                ret.add(label, c);
                
                // add filler
                c = new GridBagConstraints();
                c.fill = GridBagConstraints.VERTICAL;
                c.weighty = 1;
                Component comp = Box.createRigidArea(new Dimension(1,1));
                ret.add(comp, c);
            }


            return ret;
        }
    }
}
