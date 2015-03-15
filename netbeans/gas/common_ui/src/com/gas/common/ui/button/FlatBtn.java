/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.button;

import com.gas.common.ui.util.FontUtil;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author dq
 */
public class FlatBtn extends JButton {

    public FlatBtn() {
        this("");
    }

    public FlatBtn(boolean hover) {
        this(null, "", hover);
    }

    public FlatBtn(Icon icon) {
        this(icon, "", true);
    }

    public FlatBtn(String text) {
        this(null, text, true);
    }

    public FlatBtn(String text, boolean hover) {
        this(null, text, hover);
    }

    public FlatBtn(Icon icon, boolean hover) {
        this(icon, "", hover);
    }

    public static void decorate(JButton btn, boolean hover) {
        btn.setIconTextGap(0);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        if (hover) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    Object source = e.getSource();
                    if (source instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) source;
                        if (button.isEnabled()) {
                            button.setContentAreaFilled(true);
                            button.setBorderPainted(true);
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Object source = e.getSource();
                    if (source instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) source;
                        button.setContentAreaFilled(false);
                        button.setBorderPainted(false);
                    }
                }
            });
        }
    }

    public FlatBtn(Icon icon, String text) {
        this(icon, text, true);
    }

    public FlatBtn(Icon icon, String text, boolean hover) {
        this(icon, text, hover, 8);
    }

    public FlatBtn(Icon icon, String text, boolean hover, int gap) {
        super();
        int preferredHeight = 0;
        int preferredWidth = 0;
        final int iconHeight = icon == null? 0: icon.getIconHeight();
        final int iconWidth = icon == null? 0: icon.getIconWidth();
        final FontMetrics fm = FontUtil.getFontMetrics(this);
        final int fHeight = text == null ? 0 : fm.getHeight();
        final int fWidth = text == null ? 0 : fm.stringWidth(text);
        final Insets insets = getInsets();

        if (text != null && !text.isEmpty()) {
            setText(text);
        }
        setIcon(icon);
        preferredHeight = Math.max(iconHeight, fHeight) ;
        preferredWidth = iconWidth + fWidth;
        
        if(text != null && text.isEmpty()){
            preferredHeight =  insets.top + insets.bottom;
            preferredWidth = insets.left + insets.right;
        }

        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        decorate(this, hover);
    }
}
