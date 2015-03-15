/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.dialog;

import com.gas.common.ui.util.UIUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class ButtonPanel extends JPanel {

    private int optionType;
    private JButton okButton;
    private JButton cancelButton;

    public ButtonPanel(int optionType, int alignment) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(18, 8, 8, 8));
        this.optionType = optionType;
        GridBagConstraints c;
        int gridx = 0;
        if (alignment == SwingConstants.RIGHT) {
            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            Component r = Box.createRigidArea(new Dimension(1, 1));
            add(r, c);
        }
        if (optionType == JOptionPane.OK_CANCEL_OPTION) {

            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            add(getOkButton(), c);

            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            Component filler = Box.createRigidArea(new Dimension(5, 1));
            add(filler, c);

            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            add(getCancelButton(), c);

        } else if (optionType == JOptionPane.OK_OPTION) {
            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            add(getOkButton(), c);

        } else {
            throw new IllegalArgumentException(String.format("Option type '%d'is not supported", optionType));
        }

        if (alignment == SwingConstants.LEFT) {
            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            Component r = Box.createRigidArea(new Dimension(1, 1));
            add(r, c);
        }
    }

    public JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = UIUtil.getParent(cancelButton, JDialog.class);

                    IVDialog vDialog = UIUtil.getChild(dialog, IVDialog.class);
                    vDialog.setClosedButton(JOptionPane.CANCEL_OPTION);
                    Window w = SwingUtilities.getWindowAncestor(cancelButton);
                    w.setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    public JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = UIUtil.getParent(okButton, JDialog.class);

                    IVDialog vDialog = UIUtil.getChild(dialog, IVDialog.class);
                    boolean valid = vDialog.validateInput();
                    if (valid) {
                        vDialog.setClosedButton(JOptionPane.OK_OPTION);
                        dialog.setVisible(false);
                    } else {
                        DialogUtil.error(vDialog.getErrorMessage());
                    }
                }
            });
            JButton cBtn = getCancelButton();
            okButton.setPreferredSize(cBtn.getPreferredSize());
        }
        return okButton;
    }

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }
}
