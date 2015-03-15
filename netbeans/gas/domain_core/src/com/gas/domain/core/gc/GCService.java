/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.gc;

import com.gas.domain.core.gc.api.IGCService;
import com.gas.common.ui.core.BooleanList;
import com.gas.common.ui.core.FloatList;
import com.gas.domain.core.gc.api.GCResult;
import java.util.Iterator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IGCService.class)
public class GCService implements IGCService {

    /**
     * @param windowSize the window size must be an odd number and >=3, so that
     * can take equal number of data from either side
     * @return moving average with the window size indicated by the parameter
     */
    @Override
    public FloatList calculateGC(String nucleotides, int windowSize, boolean circular) {
        if (windowSize % 2 == 0 || windowSize < 3) {
            throw new IllegalArgumentException(String.format("Window size must be odd and >=3: %d", windowSize));
        }
        if (circular) {
            return _calculateCircularGC(nucleotides, windowSize);
        } else {
            return _calculateLinearGC(nucleotides, windowSize);
        }
    }

    private FloatList _calculateLinearGC(String nucleotides, int windowSize) {
        FloatList ret = new FloatList();
        BooleanList a = new BooleanList();
        for (int i = 0; i < nucleotides.length(); i++) {
            char base = nucleotides.charAt(i);
            if (isGC(base)) {
                a.add(Boolean.TRUE);
            } else {
                a.add(Boolean.FALSE);
            }
            if (a.size() == windowSize) {
                ret.add(a.getTrueCount() * 1.0f / a.size());
                a.removeFirst();
            }
        }
        return ret;
    }

    private FloatList _calculateCircularGC(String nucleotides, int windowSize) {
        String start = nucleotides.substring(0, windowSize / 2);
        String end = nucleotides.substring(nucleotides.length() - windowSize / 2, nucleotides.length());

        nucleotides = end + nucleotides + start;

        FloatList ret = new FloatList();
        BooleanList a = new BooleanList();
        for (int i = 0; i < nucleotides.length(); i++) {
            char base = nucleotides.charAt(i);
            if (isGC(base)) {
                a.add(Boolean.TRUE);
            } else {
                a.add(Boolean.FALSE);
            }
            if (a.size() == windowSize) {
                ret.add(a.getTrueCount() * 1.0f / a.size());
                a.removeFirst();
            }
        }
        return ret;
    }

    /**
     * @see #calculateGC(java.lang.String, int)
     */
    public FloatList calculateAT(String nucleotides, int windowSize, boolean circular) {
        FloatList gcList = calculateGC(nucleotides, windowSize, circular);
        FloatList ret = new FloatList();
        Iterator<Float> itr = gcList.iterator();
        while (itr.hasNext()) {
            Float gc = itr.next();
            Float at = 1 - gc;
            ret.add(at);
        }
        return ret;
    }

    public GCResult calculate(String nucleotides, int windowSize, boolean circular) {
        GCResult ret = new GCResult();
        FloatList floatList = calculateGC(nucleotides, windowSize, circular);
        ret.setContents(floatList.toPrimitiveArray());
        ret.setWindowSize(windowSize);
        return ret;
    }

    private boolean isGC(char c) {
        return c == 'g' || c == 'c' || c == 'G' || c == 'C';
    }
}
