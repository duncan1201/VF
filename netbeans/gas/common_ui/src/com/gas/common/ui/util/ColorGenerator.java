/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dunqiang
 */
public class ColorGenerator {

    /**
     * @param numOfColors number of colors
     */
    public static List<Color> generate(int numOfColors) {
        float[] s = {1.0f};
        float[] v = {0.8f};
        List<Color> retList = new ArrayList<Color>();
        int round = (int) Math.ceil(numOfColors * 1.0f / s.length / v.length);
        for (int i = 0; i < round; i++) {
            float hue = 1.0f / round * (i + 1);
            hue = Math.min(hue, 1);
            Color c = ColorUtil.getColor(hue, s[i % s.length], v[i % v.length]);
            retList.add(c);
        }
        retList = shuffle(retList);
        return retList;
    }

    private static List<Color> shuffle(List<Color> list) {
        final int X = 5;
        Map<Integer, List<Color>> colorMaps = new LinkedHashMap<Integer, List<Color>>();
        for (int i = 0; i < list.size(); i++) {
            if (!colorMaps.containsKey(i % X)) {
                colorMaps.put(i % X, new ArrayList<Color>());
            }
            colorMaps.get(i % X).add(list.get(i));
        }
        List<Color> ret = new ArrayList<Color>();
        int i = 0;
        while (ret.size() < list.size()) {
            if (!colorMaps.get(i).isEmpty()) {
                List<Color> colorList = colorMaps.get(i);
                Color c = colorList.remove(colorList.size() - 1);
                ret.add(c);
            }
            i++;
            if (i == colorMaps.size()) {
                i = 0;
            }
        }
        return ret;
    }
}
