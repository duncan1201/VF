/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.BooleanList;
import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Pozition;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.domain.ui.shape.ToolTipCreatorHelper;
import java.awt.Color;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class RingFactory {

    public static Ring createRing(Feture feture, int totalPos) {
        ColorPref pref = ColorPref.getInstance();

        Ring ret = new Ring();
        Lucation lucation = feture.getLucation();
        
        FloatList startAngles = new FloatList();
        FloatList extents = new FloatList();
        BooleanList forwards = new BooleanList();
        BooleanList fuzzyStarts = new BooleanList();
        BooleanList fuzzyEnds = new BooleanList();
        Iterator<Pozition> itr = lucation.getPozitionsItr();
        while(itr.hasNext()){
            Pozition poz = itr.next();
            int pozStart = poz.getStart();
            boolean fuzzyStart = poz.isFuzzyStart();
            boolean fuzzyEnd = poz.isFuzzyEnd();
            int pozWidth = LocUtil.width(poz.getStart(), poz.getEnd(), totalPos).intValue();            
            Boolean strand = poz.getStrand();
            
            Float pozStartAngle = UIUtil.toDegree(pozStart, totalPos, SwingConstants.LEFT, Float.class);  
            pozStartAngle = MathUtil.normalizeDegree(pozStartAngle).floatValue();
            Float pozExtent = UIUtil.toDegree(pozWidth, totalPos, SwingConstants.RIGHT, Float.class);
            
            startAngles.add(pozStartAngle);
            fuzzyStarts.add(fuzzyStart);
            fuzzyEnds.add(fuzzyEnd);
            extents.add(pozExtent);
            forwards.add(strand);
        }
        
        ret.setStartAngles(startAngles);
        ret.setFuzzyStarts(fuzzyStarts);
        ret.setFuzzyEnds(fuzzyEnds);
        ret.setExtents(extents);
        ret.setForwards(forwards);
        ret.setData(feture);                
        ret.setRichToolTip(ToolTipCreatorHelper.create(ret, totalPos), false, true);

        String key = feture.getKey().toUpperCase(Locale.ENGLISH);
        
        Color color = pref.getColor(key);
        ret.setSeedColor(color);
        return ret;
    }
}
