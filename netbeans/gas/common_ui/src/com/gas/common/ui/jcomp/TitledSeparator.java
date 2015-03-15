/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author dq
 */
public class TitledSeparator extends JPanel {

    private JLabel label;
    private JSeparator separator;

    public TitledSeparator(String title){
        this(title, true);
    }
    
    public TitledSeparator(String title, boolean bold) {
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        label = new JLabel();
        Font oldFont = label.getFont();
        Font font = oldFont.deriveFont(FontUtil.getDefaultMenuFontSize());
        if(bold){
            font = font.deriveFont(Font.BOLD);
        }
        label.setFont(font);
        label.setText(title);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;        
        add(label, c);

        separator = new JSeparator();
        c = new GridBagConstraints();
        Insets defaultInsets = UIUtil.getDefaultInsets();
        c.insets = new Insets(0, defaultInsets.left,0 , 0);
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(separator, c);
    }
    
    @Override
    public void setOpaque(boolean opaque){
        super.setOpaque(opaque);
        if(label != null){
            label.setOpaque(opaque);
        }
        if(separator != null){
            separator.setOpaque(opaque);
        }
    }

    public void setTitle(String title) {
        label.setText(title);
    }
}
