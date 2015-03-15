/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.brickComp;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.light.Base;
import com.gas.common.ui.light.BaseList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
class BrickCompListeners {

    static class PtyChangeListener implements PropertyChangeListener {

        public PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            BrickComp src = (BrickComp) evt.getSource();
            String name = evt.getPropertyName();
            Object nV = evt.getNewValue();
            if (name.equals("scale")) {
                src.resetDesiredWidth();
                UIUtil.setPreferredWidth(src, src.getDesiredWidth());
                src.scrollableUnitIncrement = null;
            } else if (name.equals("variants")) {
                Iterator<VariantMapMdl.Variant> itr = src.variants.getVariants().iterator();
                while (itr.hasNext()) {
                    VariantMapMdl.Variant v = itr.next();
                    Integer pos = v.getPos();
                    
                }
            } else if (name.equals("doubleLine")) {
                src.resetDesiredHeight();
                src.repaint();
            } else if (name.equals("colorProvider")) {
                src.getForwardTextList().setColorProvider((IColorProvider) nV);
                src.getReverseTextList().setColorProvider((IColorProvider) nV);
                src.repaint();
            } else if (name.equals("data")) {
                src.getForwardTextList().clear();
                src.getReverseTextList().clear();
                List<Character> data = (List<Character>) nV;
                for (int i = 0; i < data.size(); i++) {
                    Character base = data.get(i);
                    Base text = new Base();
                    text.setText(Character.toUpperCase(base));
                    src.getForwardTextList().add(text);

                    text = new Base();
                    base = BioUtil.complement(base);
                    text.setText(Character.toUpperCase(base));
                    src.getReverseTextList().add(text);
                }
            } else if (name.equals("selection")) {
                Loc selection = (Loc) nV;
                src.getForwardTextList().setSelection(selection);
                src.getReverseTextList().setSelection(selection);
                src.repaint();
            } else if (name.equals("bounds")) {
                final Insets insets = src.getInsets();
                final Dimension size = src.getSize();
                BaseList fTextList = src.getForwardTextList();
                BaseList rTextList = src.getReverseTextList();
                if (src.isDoubleLine() != null && src.isDoubleLine()) {
                    int height = Math.round(size.height * 0.5f);
                    fTextList.setRect(new Rectangle2D.Double(insets.left, 0, size.width - insets.left - insets.right, height));
                    rTextList.setRect(new Rectangle2D.Double(insets.left, height, size.width - insets.left - insets.right, height));
                } else {
                    fTextList.setRect(new Rectangle2D.Double(insets.left, 0, size.width - insets.left - insets.right, size.height));
                }
            }
        }
    }
}
