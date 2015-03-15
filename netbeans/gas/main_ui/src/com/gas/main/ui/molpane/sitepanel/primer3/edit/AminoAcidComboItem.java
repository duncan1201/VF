/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.biojava.BioJavaHelper;

/**
 *
 * @author dq
 */
public class AminoAcidComboItem {

    public static AminoAcidComboItem[] create(Collection<Character> aminoAcids, AminoAcidComboItem.DISPLAY display) {
        List<AminoAcidComboItem> ret = new ArrayList<AminoAcidComboItem>();
        for (Character aa : aminoAcids) {
            ret.add(new AminoAcidComboItem(aa, display));
        }
        AminoAcidComboItem[] _ret = ret.toArray(new AminoAcidComboItem[ret.size()]);
        Arrays.sort(_ret, new AminoAcidComboItem.ThreeLetterCodeComparator());
        return _ret;
    }

    public enum DISPLAY {

        THREE_LETTER_ONLY
    };
    private DISPLAY display;
    private Character aminoAcid;
    private String threeLetterCode;

    public AminoAcidComboItem(Character aa, DISPLAY display) {
        this.aminoAcid = aa;
        this.threeLetterCode = StrUtil.capitalize(BioJavaHelper.toAA3LetterCode(aa));
        this.display = display;
    }

    public Character getAminoAcid() {
        return aminoAcid;
    }

    public void setAminoAcid(Character aminoAcid) {
        this.aminoAcid = aminoAcid;
    }

    public String getThreeLetterCode() {
        return threeLetterCode;
    }

    public void setThreeLetterCode(String threeLetterCode) {
        this.threeLetterCode = threeLetterCode;
    }

    public DISPLAY getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (display == DISPLAY.THREE_LETTER_ONLY) {
            ret.append(threeLetterCode);
        }
        return ret.toString();
    }

    static class ThreeLetterCodeComparator implements Comparator<AminoAcidComboItem> {

        @Override
        public int compare(AminoAcidComboItem o1, AminoAcidComboItem o2) {
            return o1.getThreeLetterCode().compareTo(o2.getThreeLetterCode());
        }
    }
}
