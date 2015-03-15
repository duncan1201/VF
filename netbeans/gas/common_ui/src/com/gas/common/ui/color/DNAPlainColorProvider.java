/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.combo.ImgComboRenderer.IComboItem;
import java.awt.Color;
import javax.swing.ImageIcon;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class DNAPlainColorProvider implements IColorProvider, IComboItem {

    @Override
    public Color getColor(String s) {
        return Color.WHITE;
    }

    @Override
    public Color getColor(Character c) {
        return Color.WHITE;
    }

    @Override
    public String getName() {
        return "Plain";
    }

    @Override
    public String getDisplayName() {
        return "Plain";
    }

    @Override
    public boolean isForProtein() {
        return false;
    }

    @Override
    public boolean isForNucleotide() {
        return true;
    }

    @Override
    public boolean isBackgroundColor() {
        return true;
    }
    private static ImageIcon imageIcon;

    @Override
    public ImageIcon getImageIcon() {
        if (imageIcon == null) {
            imageIcon = ImageIconCreator.createImageIcon(this);
        }
        return imageIcon;
    }
}
