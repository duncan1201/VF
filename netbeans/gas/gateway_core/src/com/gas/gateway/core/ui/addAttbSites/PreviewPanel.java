/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
class PreviewPanel extends JPanel {

    private PreviewComp previewComp;

    PreviewPanel() {
        super(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(new LineBorder(Color.gray), "Preview", TitledBorder.LEFT, TitledBorder.TOP);
        setBorder(border);

        previewComp = new PreviewComp();
        add(previewComp, BorderLayout.CENTER);
    }

    protected void updatePreview() {
        previewComp.repaint();
    }
}

