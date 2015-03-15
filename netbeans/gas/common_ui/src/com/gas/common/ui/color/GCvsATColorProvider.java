/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.combo.ImgComboRenderer.IComboItem;
import java.awt.Color;
import javax.swing.ImageIcon;

/**
 *
 * @author dq
 */
public class GCvsATColorProvider implements IColorProvider, IComboItem {

    @Override
    public Color getColor(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getColor(Character c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isForProtein() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isForNucleotide() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBackgroundColor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    private static ImageIcon imageIcon;

    @Override
    public ImageIcon getImageIcon() {
        if (imageIcon == null) {
            imageIcon = ImageIconCreator.createImageIcon(this);
        }
        return imageIcon;
    }

    @Override
    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
