/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.brickComp;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.as.AsHelper;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.swing.BorderFactory;

/**
 *
 * @author dq
 */
public class BrickCompMap extends TreeMap<String, BrickComp> {

    public BrickCompMap() {
        super(new BrickComp.SeqFirstComparator());
    }

    public StringList getBrickCompNames() {
        StringList ret = new StringList();
        ret.addAll(keySet());
        Collections.sort(ret, new BrickComp.SeqFirstComparator());
        return ret;
    }

    public BrickComp getPrimaryBrickComp() {
        return get(BrickComp.SEQ);
    }

    public List<BrickComp> getTranslationBrickComps() {
        List<BrickComp> ret = new ArrayList<BrickComp>();
        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            if (!key.equals(BrickComp.SEQ)) {
                ret.add(get(key));
            }
        }
        return ret;
    }

    public void create(AnnotatedSeq as, Insets insets, IColorProvider colorProvider, IColorProvider translationColorProvider) {
        if (as == null) {
            return;
        }
        clear();

        String seq = as.getSiquence().getData();

        // primary sequence

        BrickComp brickComp = new BrickComp();
        brickComp.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        brickComp.setFont(FontUtil.getDefaultMSFont());
        brickComp.setData(seq);
        brickComp.setDoubleLine(as.getAsPref().isDoubleStranded());
        if (!as.getReadOnlyOverhangs().isEmpty()) {
            Overhang startOverhang = AsHelper.getStartOverhang(as);
            if (startOverhang != null) {
                int length = startOverhang.getLength();
                boolean fivePrime = startOverhang.isFivePrime();
                if (fivePrime) {
                    brickComp.strikeThroughLine2(new Loc(1, length), true);
                } else {
                    brickComp.strikeThroughLine1(new Loc(1, length), true);
                }
            }
            Overhang endOverhang = AsHelper.getEndOverhang(as);
            if (endOverhang != null) {
                int oLength = endOverhang.getLength();
                boolean fivePrime = endOverhang.isFivePrime();
                if (fivePrime) {
                    brickComp.strikeThroughLine1(new Loc(as.getLength() - oLength + 1, as.getLength()), true);
                } else {
                    brickComp.strikeThroughLine2(new Loc(as.getLength() - oLength + 1, as.getLength()), true);
                }
            }
        }
        if (colorProvider != null) {
            brickComp.setColorProvider(colorProvider);
        }
        put(BrickComp.SEQ, brickComp);

        // translated sequence

        Iterator<TranslationResult> results = as.getTranslationResults().iterator();
        while (results.hasNext()) {
            TranslationResult rlt = results.next();
            brickComp = new BrickComp();
            brickComp.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
            brickComp.setFont(FontUtil.getDefaultMSFont());
            brickComp.setData(rlt.getData());
            brickComp.setFrame(rlt.getFrame());
            if (translationColorProvider != null) {
                brickComp.setColorProvider(translationColorProvider);
            }

            put(String.format(BrickComp.FRAME, rlt.getFrame()), brickComp);
        }

    }
}
