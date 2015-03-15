/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane.ftrtrack;

import com.gas.domain.ui.shape.IShape;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.IShapeList;
import com.gas.main.ui.molpane.graphpane.ftrtrack.layout.AdjacentConservedLayout;
import com.gas.main.ui.molpane.graphpane.ftrtrack.layout.FtrTrackLayoutFactory;
import com.gas.main.ui.molpane.graphpane.ftrtrack.layout.LabelSeenTrackLayout;
import com.gas.main.ui.molpane.graphpane.ftrtrack.layout.PrimerTrackLayout;
import java.awt.LayoutManager2;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author dq
 */
public class FtrTrackMap extends TreeMap<String, FtrTrack> {

    public FtrTrackMap() {
        super(new Feture.FtrNameComparator());
    }

    public void create(Map<String, IShapeList> arrowMap) {
        if (arrowMap == null) {
            return;
        }
        clear();
        Iterator<String> keyItr = arrowMap.keySet().iterator();

        while (keyItr.hasNext()) {
            String key = keyItr.next();
            List<IShape> arrows = arrowMap.get(key);
            FtrTrack track = get(key);
            if (track == null) {
                track = new FtrTrack();
                track.setTrackName(key);

                LayoutManager2 layoutMgr2 = FtrTrackLayoutFactory.getByFetureType(key);
                
                track.setLayout(layoutMgr2);
            }
            track.addArrows(arrows);
            put(key.toUpperCase(Locale.ENGLISH), track);
        }
    }

    public void setSelectedArrow(Arrow selectedArrow) {
        Iterator<FtrTrack> tracksItr = values().iterator();
        while (tracksItr.hasNext()) {
            FtrTrack track = tracksItr.next();
            List<IShape> arrows = track.getArrows();
            for (IShape a : arrows) {
                if (a != selectedArrow) {
                    a.setSelected(false);
                } else {
                    a.setSelected(true);
                }
            }
        }
    }

    public void setSelectedFeture(Feture selectedFeture) {
        Iterator<FtrTrack> tracksItr = values().iterator();
        while (tracksItr.hasNext()) {
            FtrTrack track = tracksItr.next();
            List<IShape> arrows = track.getArrows();
            for (IShape a : arrows) {
                if (a.getData() != selectedFeture) {
                    a.setSelected(false);
                } else {
                    a.setSelected(true);
                }
            }
        }
    }
}
