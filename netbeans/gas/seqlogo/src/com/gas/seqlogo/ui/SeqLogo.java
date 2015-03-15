/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.ui;

import com.gas.seqlogo.service.api.Heights;
import com.gas.seqlogo.service.api.HeightsList;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class SeqLogo extends JComponent {

    private StyledTextList textList = new StyledTextList();
    private HeightsList heightsList;
    private final int MIN_COLUMN_WIDTH = 4;
    private final int MIN_HEIGHT = 5;

    @Override
    public void paint(Graphics g) {
        Dimension size = getSize();
        if (size.width <= 0 || size.height <= 0) {
            return;
        }
        if (heightsList.isEmpty()) {
            return;
        }

        textList = createStyledTextList(heightsList);

        int textLength = textList.calculateTextLength();
        if (size.height <= MIN_HEIGHT || size.width * MIN_COLUMN_WIDTH < textLength) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        textList.paint(g2d);
    }

    public HeightsList getHeightsList() {
        return heightsList;
    }

    public void setHeightsList(HeightsList heightsList) {
        this.heightsList = heightsList;
    }

    private StyledTextList createStyledTextList(HeightsList heightslist) {
        StyledTextList ret = new StyledTextList();
        final int seqLength = heightslist.size();
        final Dimension size = getSize();
        final double width = 1.0 * size.width / seqLength;
        double max;
        if (heightslist.isProtein()) {
            max = 4;
        } else {
            max = 2;
        }
        for (int i = 0; i < heightslist.size(); i++) {
            float totalHeight = 0;
            Heights heights = heightslist.get(i);
            List<Heights.Entry> entries = heights.getSortedEntries(new Heights.EntrySorter());
            Iterator<Heights.Entry> itr = entries.iterator();
            while (itr.hasNext()) {
                Heights.Entry entry = itr.next();
                Double bits = entry.getBits();

                StyledText s = new StyledText();
                s.setText(entry.getName().toUpperCase());
                float x = (float) (i * width);

                float height = (float) (size.height * bits / max);
                float y = size.height - height - totalHeight;

                s.setX(x);
                s.setY(y);
                s.setWidth((float) width);
                s.setHeight(height);
                totalHeight += height;
                ret.add(s);
            }
        }
        return ret;
    }
}
