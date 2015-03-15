/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.painter;

import com.gas.common.ui.util.ColorUtil;
import java.awt.Color;

/**
 *
 * @author dunqiang
 */
public class LinearGradientPainterFactory {

    public static LinearGradientPainter create(Color seed) {
        return create(seed, 0.7f);
    }

    public static LinearGradientPainter create(Color seed, float brightness) {

        LinearGradientPainter ret = null;

        ColorUtil.getBrightness(seed);
        Color c2 = ColorUtil.changeBrightness(seed, brightness);

        ret = new LinearGradientPainter(new float[]{0, 0.5f, 1.0f}, new Color[]{c2, seed, c2});

        return ret;
    }
}
