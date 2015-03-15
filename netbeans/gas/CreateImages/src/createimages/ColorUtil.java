/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Color;

/**
 *
 * @author dunqiang
 */
public class ColorUtil {

    public final static int LUMA_THRESHOLD = 130;

    public static Color getColor(float[] hsv, float alpha) {
        int rgb = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
        Color tmp = new Color(rgb);
        if (alpha == 1) {
            return tmp;
        } else {
            return new Color(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), MathUtil.round(alpha * 255, Integer.class));
        }
    }

    public static String toHex(Color color) {
        StringBuilder ret = new StringBuilder();

        ret.append(Integer.toHexString(color.getRed()));
        ret.append(Integer.toHexString(color.getGreen()));
        ret.append(Integer.toHexString(color.getBlue()));

        return ret.toString();
    }

    /**
     * Derives a color by adding the specified offsets to the base color's hue,
     * saturation, and brightness values. The resulting hue, saturation, and
     * brightness values will be constrained to be between 0 and 1.
     *
     * @param base the color to which the HSV offsets will be added
     * @param dH the offset for hue
     * @param dS the offset for saturation
     * @param dB the offset for brightness
     * @return Color with modified HSV values
     */
    public static Color deriveColorHSB(Color base, float dH, float dS, float dB) {
        float hsb[] = Color.RGBtoHSB(
                base.getRed(), base.getGreen(), base.getBlue(), null);

        hsb[0] += dH;
        hsb[1] += dS;
        hsb[2] += dB;
        return Color.getHSBColor(
                hsb[0] < 0 ? 0 : (hsb[0] > 1 ? 1 : hsb[0]),
                hsb[1] < 0 ? 0 : (hsb[1] > 1 ? 1 : hsb[1]),
                hsb[2] < 0 ? 0 : (hsb[2] > 1 ? 1 : hsb[2]));

    }

    public static Color getColor(float[] hsv) {
        return getColor(hsv, 1);
    }

    public static Color getColor(float h, float s, float v) {
        return getColor(new float[]{h, s, v}, 1);
    }

    public static Color changeSB(Color color, float sMul, float bMul) {
        Color t = changeBrightness(color, bMul);
        Color ret = changeSaturation(t, sMul);
        return ret;
    }

    public static Color changeBrightness(Color color, float multiplier) {
        if (multiplier <= 0) {
            throw new IllegalArgumentException("Multiplier must > 0");
        }
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        hsb[2] = hsb[2] * multiplier;
        hsb[2] = Math.min(1.0f, hsb[2]); // brightness is between 0 and 1.0
        return getColor(hsb);
    }

    public static Color changeSaturation(Color color, float multiplier) {
        if (multiplier <= 0) {
            throw new IllegalArgumentException("Multiplier must > 0");
        }
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        hsb[1] = hsb[1] * multiplier;
        hsb[1] = Math.min(1.0f, hsb[1]); // brightness is between 0 and 1.0
        return getColor(hsb);
    }

    public static float getBrightness(Color c) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return hsb[2];
    }

    public static float getSaturation(Color c) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return hsb[1];
    }

    public static float getHue(Color c) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return hsb[0];
    }

    public static <T> T getHSB(Color c, Class<T> clazz) {
        T ret = null;
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        if (clazz.isAssignableFrom(float[].class)) {
            ret = (T) hsb;
        } else if (clazz.isAssignableFrom(String.class)) {
            StringBuilder builder = new StringBuilder();

            builder.append("[");
            builder.append(hsb[0]);
            builder.append(",");
            builder.append(hsb[1]);
            builder.append(",");
            builder.append(hsb[2]);
            builder.append("]");

            String str = builder.toString();
            ret = (T) str;
        } else {
            throw new IllegalArgumentException(String.format("'%s' not supported", clazz.getName()));
        }
        return ret;
    }

    enum ALGO {

        REC_601, REC_709, W3C, X
    };

    public static <T> T getPerceivedBrightness(Color c, Class<T> clazz) {

        return getPerceivedBrightness(c, clazz, ColorUtil.ALGO.X);

    }

    public static <T> T getPerceivedBrightness(Color c, Class<T> clazz, ColorUtil.ALGO algo) {
        T ret = null;
        Color retColor = null;
        double pB;
        if (algo == ColorUtil.ALGO.REC_709) {
            pB = 0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue();
        } else if (algo == ColorUtil.ALGO.REC_601) {
            pB = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
        } else if (algo == ColorUtil.ALGO.X) {
            pB = Math.sqrt(c.getRed() * c.getRed() * .241
                    + c.getGreen() * c.getGreen() * .691
                    + c.getBlue() * c.getBlue() * .068);
        } else {
            throw new IllegalArgumentException(String.format("Digital format '%s' not supported", algo.toString()));
        }

        if (clazz.isAssignableFrom(Integer.class)) {
            Integer tmp = MathUtil.round(pB, Integer.class);
            ret = (T) tmp;
        } else if (clazz.isAssignableFrom(Double.class)) {
            Double tmp = pB;
            ret = (T) tmp;
        } else {
            throw new IllegalArgumentException(String.format("'%s' not supported", clazz.getName()));
        }

        return ret;
    }

    public static Color getForeground(Color c) {
        return getForeground(c, Color.class, LUMA_THRESHOLD);
    }

    /*
     * create a new color between color1 and color2
     * @param closeness: the closeness to color1
     */
    public static Color interpolate(Color color1, Color color2, Float closeness) {
        if (closeness > 1 || closeness < 0) {
            throw new IllegalArgumentException(String.format("closeness out of range: %s", closeness.toString()));
        }
        float[] comp1 = color1.getComponents(null);
        float[] comp2 = color2.getComponents(null);
        float[] comp = new float[comp1.length];
        for (int i = 0; i < comp1.length; i++) {
            comp[i] = comp1[i] + (comp2[i] - comp1[i]) * (1 - closeness);
        }
        Color ret = new Color(comp[0], comp[1], comp[2], comp[3]);
        return ret;
    }

    public static <T> T getForeground(Color c, Class<T> clazz, int threshold) {
        T ret = null;
        Color retColor;
        int b = getPerceivedBrightness(c, Integer.class);
        if (b > threshold) {
            retColor = Color.BLACK;
        } else {
            retColor = Color.WHITE;
        }

        if (clazz.isAssignableFrom(Color.class)) {
            ret = (T) retColor;
        } else if (clazz.isAssignableFrom(String.class)) {
            ret = (T) retColor.toString();
        } else {
            throw new IllegalArgumentException(String.format("'%s' not supported", clazz.getName()));
        }
        return ret;
    }
}
