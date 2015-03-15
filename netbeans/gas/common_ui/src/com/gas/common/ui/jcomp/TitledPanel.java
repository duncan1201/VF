/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.jdesktop.swingx.JXPanel;

/**
 *
 * @author dunqiang
 */
public class TitledPanel extends JPanel {

    private String title;
    private JLabel titleComp;
    private JXPanel topPanel;
    private WeakReference<JComponent> leftDecoRef;
    private WeakReference<JComponent> rightDecoRef;
    private JPanel contentPane;

    public TitledPanel(String title) {
        super(new BorderLayout());
        this.title = title;
        titleComp = new JLabel();
        Font oldFont = titleComp.getFont();
        Font font = oldFont.deriveFont(Font.BOLD, FontUtil.getDefaultMenuFontSize());
        titleComp.setFont(font);
        titleComp.setHorizontalAlignment(SwingConstants.CENTER);
        titleComp.setText(title);
        LinearGradientPainter p = new LinearGradientPainter();
        p.setFractions(0f, 0.5f, 1.0f);
        p.setColors(new Color(204, 225, 240), new Color(186, 206, 224), new Color(204, 225, 240));
        int height = FontUtil.getFontMetrics(font).getHeight();
        UIUtil.setPreferredHeight(titleComp, Math.round(height * 1.5f));
        contentPane = new JPanel();

        topPanel = new JXPanel(new BorderLayout());
        topPanel.add(titleComp, BorderLayout.CENTER);
        if(title != null && !title.isEmpty()){
            topPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            topPanel.setBackgroundPainter(p);
        }

        add(topPanel, BorderLayout.NORTH);
        add(contentPane, BorderLayout.CENTER);
    }

    public void setLeftDecoration(JComponent comp) {
        leftDecoRef = new WeakReference<JComponent>(comp);
        topPanel.add(comp, BorderLayout.WEST);
    }

    public void enableLeftDecoration(boolean enable) {
        if (leftDecoRef != null && leftDecoRef.get() != null) {
            leftDecoRef.get().setEnabled(enable);
        }
    }

    public void enableRightDecoration(boolean enable) {
        if (rightDecoRef != null && rightDecoRef.get() != null) {
            rightDecoRef.get().setEnabled(enable);
        }
    }

    public void setRightDecoration(JComponent comp) {
        rightDecoRef = new WeakReference<JComponent>(comp);
        topPanel.add(comp, BorderLayout.EAST);
    }

    public JComponent getLeftDecoration() {
        JComponent ret = null;
        if (leftDecoRef != null) {
            ret = leftDecoRef.get();
        }
        return ret;
    }

    public JComponent getRightDecoration() {
        JComponent ret = null;
        if (rightDecoRef != null) {
            ret = rightDecoRef.get();
        }
        return ret;
    }

    public JComponent getTitleComp() {
        return titleComp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleComp.setText(title);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void setContentPane(JPanel contentPane) {
        this.contentPane = contentPane;
    }
}
