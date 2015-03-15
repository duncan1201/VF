/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureSorters;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.AsPref;
import java.util.*;

/**
 *
 * @author dq
 */
public class RingListMap extends HashMap<String, RingList> {

    public void createRings(final AnnotatedSeq as) {
        clear();
        AsPref trackVisiblePref = as.getAsPref();
        int totalPos = as.getLength();
        Map<String, List<Feture>> fetureMap = AsHelper.getFetureMap(as, true);
        Iterator<String> keyItr = fetureMap.keySet().iterator();
        while (keyItr.hasNext()) {
            final String key = keyItr.next();
            boolean visible = trackVisiblePref.isTrackVisible(key);
            if(!visible){
                continue;
            }
            List<Feture> fetures = fetureMap.get(key);
            fetures = FetureSorters.adjacentConservedCompact(fetures, as.getLength());
            Iterator<Feture> fItr = fetures.iterator();
            while (fItr.hasNext()) {
                Feture feture = fItr.next();
                Ring ring = RingFactory.createRing(feture, totalPos);

                if (!containsKey(key)) {
                    put(key, new RingList());
                }
                get(key).add(ring);
            }
        }
    }

    public SortedRingListMap sort() {
        SortedRingListMap ret = new SortedRingListMap();
        ret.create(this);
        return ret;
    }
}
