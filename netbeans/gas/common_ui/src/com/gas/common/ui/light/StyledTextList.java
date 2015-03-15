/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class StyledTextList extends ArrayList<StyledText> {

    private Double maxBits = null;
    private Double totalBits = 0d;

    @Override
    public boolean add(StyledText s) {
        boolean ret = super.add(s);
        updateMetaData(s);
        return ret;
    }

    public String getTexts() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            StyledText styledText = get(i);
            ret.append(styledText.getText());
        }
        return ret.toString();
    }

    public Double getTotalBits() {
        return totalBits;
    }

    private void updateMetaData(StyledText t) {
        if (t.getBits() != null) {
            if (maxBits == null || maxBits < t.getBits()) {
                maxBits = t.getBits();
            }
            totalBits += t.getBits();
        }
    }
}
