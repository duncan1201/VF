/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.misc.Loc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author dq
 */
public class FetureSorters {

    public static List<Feture> adjacentConservedCompact(List<Feture> fetureList, int totalLength) {

        //Collections.sort(arrowList, new StartPosComparator());
        fetureList = adjacentConservedComparator(fetureList);

        List<Feture> ret = new ArrayList<Feture>();

        if (fetureList.isEmpty()) {
            return ret;
        }

        while (!fetureList.isEmpty()) {
            Feture removed = fetureList.remove(0);
            ret.add(removed);
            Loc loc = removed.getLucation().toLoc();
            loc.setTotalPos(totalLength);

            for (int i = 0; i < fetureList.size(); i++) {
                Feture tmp = fetureList.get(i);
                if (!loc.isOverlapped(tmp.getLucation().toLoc())) {
                    fetureList.remove(i);

                    ret.add(tmp);
                    loc = tmp.getLucation().toLoc();

                    i--;
                }
            }
        }

        fetureList.clear();
        fetureList.addAll(ret);

        return ret;
    }

    private static List<Feture> adjacentConservedComparator(List<Feture> fetureList) {
        List<Feture> ret = new ArrayList<Feture>();
        Map<Integer, FetureList> startMap = new TreeMap<Integer, FetureList>();

        Collections.sort(fetureList, new Feture.FComparator());
        for (int i = 0; i < fetureList.size(); i++) {
            Feture feture = fetureList.get(i);
            FetureList startMapList = startMap.get(feture.getLucation().getStart());
            if (startMapList == null) {
                startMapList = new FetureList();
                startMap.put(feture.getLucation().getStart(), startMapList);
            }
            startMapList.add(feture);
        }

        List<Feture> startList = new ArrayList<Feture>(fetureList);
        Collections.sort(startList, new Feture.FComparator());

        for (int i = 0; i < startList.size(); i++) {
            Feture feture = startList.get(i);

            ret.add(feture);
            FetureList startMapList = startMap.get(feture.getLucation().getStart());
            if (startMapList != null) {
                startMapList.remove(feture);
            }

            Integer max = feture.getLucation().getEnd();

            while (startMap.containsKey(max + 1) && !startMap.get(max + 1).isEmpty()) {
                FetureList adjacentList = startMap.get(max + 1);
                int index = startList.indexOf(adjacentList.get(0));

                if (!ret.contains(adjacentList.get(0))) {
                    ret.add(adjacentList.get(0));
                }

                adjacentList.remove(0);
                startList.remove(index);
            }
        }

        return ret;
    }
}
