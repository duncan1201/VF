/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.common.ui.util.Pref;
import com.gas.domain.core.as.FetureKeyCnst;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author dq
 */
public class ArrowCreator {

    public static Arrow create(Feture feture) {        
        String key = feture.getKey();
        if(key.equalsIgnoreCase(FetureKeyCnst.PRIMER_BINDING_SITE)){
            return createPrimerArrow(feture);
        }else{
            return _create(feture);
        }
    }
    
    private static Arrow _create(Feture feture){
        Arrow arrow = new Arrow();
        String key = feture.getKey();
        if(key.equalsIgnoreCase(FetureKeyCnst.PRIMER_BINDING_SITE)){
            
        }
        Color color = ColorPref.getInstance().getColor(key);
        arrow.setSeedColor(color);
        arrow.setDisplayText(feture.getDisplayName());
        arrow.setData(feture);

        Font font = FontUtil.getDefaultMSFont();
        font = font.deriveFont(Pref.CommonPtyPrefs.getInstance().getAnnotationLabelSize());
        arrow.setTextFont(font);
        Lucation lucation = feture.getLucation();

        arrow.setLuc(lucation);
        arrow.setForward(lucation.getStrand());
        return arrow;
    }
    
    private static Arrow createPrimerArrow(Feture feture) {
        Arrow arrow = new Arrow();
        String key = feture.getKey();
        if(key.equalsIgnoreCase(FetureKeyCnst.PRIMER_BINDING_SITE)){
            
        }
        Color color = ColorPref.getInstance().getColor(key);
        arrow.setSeedColor(color);
        arrow.setDisplayText(feture.getDisplayName());
        arrow.setData(feture);

        Font font = FontUtil.getDefaultMSFont();
        font = font.deriveFont(Pref.CommonPtyPrefs.getInstance().getAnnotationLabelSize());
        arrow.setTextFont(font);
        Lucation lucation = feture.getLucation();

        arrow.setLuc(lucation);
        arrow.setForward(lucation.getStrand());
        return arrow;
    }
}
