/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.triangle;

import com.gas.common.ui.core.BooleanList;
import com.gas.common.ui.core.IntList;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class TriangleMap extends HashMap<String, Triangle> {

    private final String FORMAT = "%d-%s";

    /**
     * @param pos: the triangle's position
     * @see Triangle#getPos()
     */
    private void put(Integer pos, Boolean down, Triangle triangle) {
        put(String.format(FORMAT, pos, down.toString()), triangle);
    }

    public void clearSelection() {
        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            Triangle triangle = get(key);
            triangle.setSelected(false);
        }
    }

    public void select(RMap.EntryList entries) {
        IntList positions = new IntList();
        BooleanList downs = new BooleanList();
        for (RMap.Entry entry : entries) {
            int[] cutPos = entry.getDownstreamCutPos();
            Arrays.sort(cutPos);
            for(int i = 0; i < cutPos.length; i++){
                positions.add(cutPos[i] + entry.getStart() - 1);
            }
            Integer cutType = entry.getDownstreamCutType();
                                    
            if (cutType.equals(REN.OVERHANG_5PRIME) || cutType.equals(REN.BLUNT)) {
                downs.add(true);
                downs.add(false);
            } else if (cutType.equals(REN.OVERHANG_3PRIME)) {
                downs.add(false);
                downs.add(true);
            }
        }
        select(positions, downs);
    }

    /**
     * @param positions the cut positions
     */
    public void select(IntList positions, Integer downstreamCutType) {
        BooleanList downs = new BooleanList();
        for (int i = 0; i < positions.size(); i += 2) {
            if (downstreamCutType.equals(REN.OVERHANG_5PRIME) || downstreamCutType.equals(REN.BLUNT)) {
                downs.add(true);
                downs.add(false);
            } else if (downstreamCutType.equals(REN.OVERHANG_3PRIME)) {
                downs.add(false);
                downs.add(true);
            }
        }
        select(positions, downs);
    }

    /**
     * @param positions: the positions of triangles
     * @param downs the direction of the arrow
     * @see Triangle#getPos()
     */
    private void select(IntList positions, BooleanList downs) {
        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            Triangle triangle = get(key);
            Integer pos = getPos(key);
            boolean down = isDown(key);
            int index = positions.indexOf(pos);
            if (index > -1 && downs.get(index).booleanValue() == down) {
                triangle.setSelected(true);
            } else {
                triangle.setSelected(false);
            }
        }
    }

    private boolean isDown(String key) {
        return key.split("-")[1].equals("true");
    }

    private Integer getPos(String key) {
        return Integer.parseInt(key.split("-")[0]);
    }

    public void create(RMap rmap) {
        if (rmap == null) {
            return;
        }
        clear();

        Iterator<RMap.Entry> itr = rmap.getEntriesIterator();
        while (itr.hasNext()) {
            RMap.Entry entry = itr.next();
            int[] usCutPos = entry.getUpstreamCutPos();
            if (usCutPos != null && usCutPos.length == 2) {

                Triangle triangle = new Triangle();
                triangle.setPos(entry.getStart() + usCutPos[0] - 1);
                triangle.setDown(true);
                put(triangle.getPos(), triangle.isDown(), triangle);

                triangle = new Triangle();
                triangle.setPos(entry.getStart() + usCutPos[1] - 1);
                put(triangle.getPos(), triangle.isDown(), triangle);
            }

            int[] dsCutPos = entry.getDownstreamCutPos();
            if (dsCutPos != null) {
                if (dsCutPos.length == 2) {
                    Triangle triangle = new Triangle();
                    triangle.setDown(true);
                    triangle.setPos(entry.getStart() + dsCutPos[0] - 1);
                    put(triangle.getPos(), triangle.isDown(), triangle);

                    triangle = new Triangle();
                    triangle.setPos(entry.getStart() + dsCutPos[1] - 1);
                    put(triangle.getPos(), triangle.isDown(), triangle);
                }
            }
        }
    }
}
