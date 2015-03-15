/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.misc.Loc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author dunqiang
 */
public class ArrowComparators {

    public static List<IShape> adjacentConservedCompact(List<IShape> arrowList) {

        //Collections.sort(arrowList, new StartPosComparator());
        arrowList = adjacentConservedComparator(arrowList);

        List<IShape> ret = new ArrayList<IShape>();

        if (arrowList.isEmpty()) {
            return ret;
        }

        while (!arrowList.isEmpty()) {
            IShape removed = arrowList.remove(0);
            ret.add(removed);
            Loc loc = removed.getLoc();

            for (int i = 0; i < arrowList.size(); i++) {
                IShape tmp = arrowList.get(i);
                if (!loc.isOverlapped(tmp.getLoc())) {
                    arrowList.remove(i);

                    ret.add(tmp);
                    loc = tmp.getLoc();

                    i--;
                }
            }
        }

        arrowList.clear();
        arrowList.addAll(ret);

        return ret;
    }

    /*
     */
    private static List<IShape> adjacentConservedComparator(List<IShape> arrowList) {
        List<IShape> ret = new ArrayList<IShape>();
        Map<Integer, List<IShape>> startMap = new TreeMap<Integer, List<IShape>>();
        Map<Integer, List<IShape>> endMap = new TreeMap<Integer, List<IShape>>();

        Collections.sort(arrowList, new ArrowComparators.StartPosComparator());
        for (int i = 0; i < arrowList.size(); i++) {
            IShape arrow = arrowList.get(i);
            List<IShape> list = startMap.get(arrow.getLuc().getStart());
            if (list == null) {
                list = new ArrayList<IShape>();
                startMap.put(arrow.getLuc().getStart(), list);
            }
            list.add(arrow);

            list = endMap.get(arrow.getLuc().getEnd());
            if (list == null) {
                list = new ArrayList<IShape>();
                endMap.put(arrow.getLuc().getEnd(), list);
            }
            list.add(arrow);

        }

        List<IShape> startList = new ArrayList<IShape>(arrowList);
        Collections.sort(startList, new StartPosComparator());

        for (int i = 0; i < startList.size(); i++) {
            IShape arrow = startList.get(i);

            ret.add(arrow);
            Integer max = arrow.getLuc().getEnd();

            while (startMap.containsKey(max + 1) && !startMap.get(max + 1).isEmpty()) {
                List<IShape> adjacentList = startMap.get(max + 1);
                int index = startList.indexOf(adjacentList.get(0));

                ret.add(adjacentList.get(0));

                adjacentList.remove(0);
                startList.remove(index);
            }
        }

        return ret;
    }

    public static class StartPosComparator implements Comparator<IShape> {

        private boolean posAscending = true;

        public StartPosComparator() {
            this(true);
        }

        public StartPosComparator(boolean posAscending) {
            this.posAscending = posAscending;
        }

        public boolean isPosAscending() {
            return posAscending;
        }

        public void setPosAscending(boolean posAscending) {
            this.posAscending = posAscending;
        }

        @Override
        public int compare(IShape o1, IShape o2) {
            int ret = 0;

            Integer start1 = o1.getLuc().getStart();
            Integer start2 = o2.getLuc().getStart();
            ret = start1.compareTo(start2);

            if (!posAscending) {
                ret *= -1;
            }

            return ret;
        }
    }
    
}
