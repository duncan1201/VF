package com.gas.common.ui.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

public class MathUtil {

    /**
     * 41.3456 
     *    multiply     
     * 41345.6    
     *     round 
     * 413457    
     *     divide 
     * 41.3457
     */
    public static double round(double d, int decimalPlaces) {
        if(decimalPlaces < 1){
            throw new IllegalArgumentException("must be >= 1");
        }
        double ret = d;
        // multiply
        for(int i = 0; i < decimalPlaces - 1; i++){
            ret = ret * 10;
        }
        // round
        ret = Math.round(ret);
        // divide
        int divisor = 1;
        for(int i = 0; i < decimalPlaces - 1; i++){
            divisor *= 10;
        }
        ret = ret / divisor;
        return ret;
    }

    /**
     * @param n: natural number >=1
     * @return : the sum of first n natural numbers
     */
    public static int sumOfNaturalNumbers(int n) {
        return n * (n + 1) / 2;
    }

    public static Integer round(float f) {
        return round((double) f, Integer.class);
    }

    /**
     * @param angle1 in degrees
     * @param angle2 in degrees
     * @param clockwise how angle1 should rotate towards angle2
     * @return the normalized distance inclusive. always positive
     */
    public static Double getDistance(double angle1, double angle2, boolean clockwise) {
        angle1 = MathUtil.normalizeDegree(angle1);
        angle2 = MathUtil.normalizeDegree(angle2);
        double ret;
        if (clockwise) {
            ret = LocUtil.angleWidth(angle1, angle2);
            ret = MathUtil.normalizeDegree(ret);
        } else {
            ret = getDistance(angle2, angle1, !clockwise);
        }
        return ret;
    }

    public static String toOrdinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static <T> T roundUp(double d, Class<T> clazz) {
        T ret = null;
        Double retDouble = Math.ceil(d);
        if (clazz.isAssignableFrom(Integer.class)) {
            Integer i = retDouble.intValue();
            ret = (T) i;
        } else if (clazz.isAssignableFrom(Float.class)) {
            Float i = retDouble.floatValue();
            ret = (T) i;
        } else if (clazz.isAssignableFrom(Double.class)) {
            ret = (T) retDouble;
        }
        return ret;
    }

    /**
     * @param angleDeg: Angle in degrees. It started from 3 o'clock, counter
     * clockwise.
     * @return the coordinates
     */
    public static Point2D.Double getCoordsDeg(Point2D center, double radius, double angleDeg) {
        return getCoordsRad(center, radius, Math.toRadians(angleDeg));
    }

    /**
     * @param angRad: Angle in radian. It started from 3 o'clock, counter
     * clockwise.
     * @return the coordinates
     */
    public static Point2D.Double getCoordsRad(Point2D center, double radius, double angRad) {
        Point2D.Double ret = new Point.Double();
        ret.x = center.getX() + radius * Math.cos(angRad);
        ret.y = center.getY() - radius * Math.sin(angRad);
        return ret;
    }

    public static float mod(float dividend, float divisor) {
        float ret = 0f;
        final float CNST = 100.0f;
        int retInt = Math.round(dividend * CNST) % Math.round(divisor * CNST);
        ret = retInt / CNST;
        return ret;
    }

    /*
     * @ret normalize a degree to range 0-360 inclusively
     */
    public static Double normalizeDegree(double degree) {
        if (degree < 0) {
            while (degree < 0) {
                degree += 360;
            }
        } else if (degree > 360) {
            degree = degree % 360;
        }
        return degree;
    }

    public static <T> T avg(Object[] d, Class<T> clazz) {
        T ret = null;
        if (clazz.isAssignableFrom(Float.class)) {
            double sum = 0;
            for (int i = 0; i < d.length; i++) {
                sum += (Float) d[i];
            }
            ret = MathUtil.round(sum / d.length, clazz);
        } else if (clazz.isAssignableFrom(Integer.class)) {
            double sum = 0;
            for (int i = 0; i < d.length; i++) {
                sum += (Integer) d[i];
            }
            ret = MathUtil.round(sum / d.length, clazz);
        } else if (clazz.isAssignableFrom(Integer.class)) {
        } else {
            throw new IllegalArgumentException(String.format("class '%s' not supported", clazz.getName()));
        }
        return ret;
    }

    /*
     * @param s: the number of significant digits
     */
    public static String toString(double x, int s) {
        DecimalFormat format;
        StringBuilder patternBuilder = new StringBuilder();
        if (x < 0) {
            patternBuilder.append("0.");
            while (s > 0) {
                patternBuilder.append('#');
                s--;
            }
        } else if (x > 0) {
            patternBuilder.append("#.");
            while (s > 0) {
                patternBuilder.append('#');
                s--;
            }
        }
        String pattern = patternBuilder.toString();
        format = new DecimalFormat(pattern);
        return format.format(x);
    }

    /*
     * @param d: the number of decimal places
     */
    public static double round(Double x, int d) {
        double ret = 0;
        int m = 1;
        for (int i = 0; i < d; i++) {
            m = m * 10;
        }
        x = x * m;
        long xLong = Math.round(x);
        ret = xLong * 1.0 / m;
        return ret;
    }

    public static int ceil(double d) {
        return (int) Math.ceil(d);
    }

    public static Integer round(double f) {
        return round(f, Integer.class);
    }

    public static <T> T round(float f, Class<T> clazz) {
        return round((double) f, clazz);
    }

    public static <T> T round(double f, Class<T> clazz) {
        T ret = null;
        double d = Math.floor(f + 0.5);
        if (clazz.isAssignableFrom(Float.class)) {
            Float tmp = (float) d;
            ret = (T) tmp;
        } else if (clazz.isAssignableFrom(Integer.class)) {
            Integer tmp = (int) d;
            ret = (T) tmp;
        } else if (clazz.isAssignableFrom(Double.class)) {
            Double tmp = d;
            ret = (T) tmp;
        } else {
            throw new IllegalArgumentException(String.format("class '%s' not supported", clazz.getName()));
        }
        return ret;
    }

    public static double getArcLength(double radius, double angle, boolean radian) {
        double ret;
        if (radian) {
            ret = radius * angle;
        } else {
            ret = 2 * Math.PI * radius / 360 * angle;
        }
        return ret;
    }

    private static double getSlope(double x1, double y1, double x2, double y2) {
        return (y1 - y2) / (x1 - x2);
    }

    private static double getYIntercept(double x1, double y1, double x2, double y2) {
        return (x2 * y1 - x1 * y2) / (x2 - x1);
    }

    public static double[] getIntercept(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] ret = new double[2];
        double slope1 = getSlope(x1, y1, x2, y2);
        double intercept1 = getYIntercept(x1, y1, x2, y2);
        double slope2 = getSlope(x3, y3, x4, y4);
        double intercept2 = getYIntercept(x3, y3, x4, y4);

        ret[0] = (intercept2 - intercept1) / (slope1 - slope2);
        ret[1] = (slope2 * intercept1 - slope1 * intercept2) / (slope2 - slope1);
        return ret;
    }

    public static double distance(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
    }

    public static Double distance(Point p, Point p2) {
        if (p != null && p2 != null) {
            return distance(p.x, p.y, p2.x, p2.y);
        } else {
            return null;
        }
    }

    public static double getRadians(int arcLength, double radius) {
        return arcLength / radius;
    }

    public static Double getAngle(double arcLength, double radius, boolean radian) {
        Double ret;
        if (radian) {
            ret = arcLength / radius;
        } else {
            ret = 180 * arcLength / (Math.PI * radius);
        }
        return ret;
    }

    public static Integer max(List<Integer> data) {
        Integer ret = null;
        if (!data.isEmpty()) {
            ret = data.get(0);
        }
        for (Integer i : data) {
            if (i != null && ret < i) {
                ret = i;
            }
        }
        return ret;
    }

    public static Integer max(int[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Integer max = Integer.MIN_VALUE;
        for (int d : data) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }

    public static int nextEven(double d) {
        long i = Math.round(d);
        if (i % 2 == 0) {
            return (int) i;
        } else {
            return (int) (i + 1);
        }
    }

    public static int previousEven(double d) {
        long i = Math.round(d);
        if (i % 2 == 0) {
            return (int) i;
        } else {
            return (int) (i - 1);
        }
    }

    public static int previousOdd(double d) {
        long i = Math.round(d);
        if (i % 2 == 1) {
            return (int) i;
        } else {
            return (int) (i - 1);
        }
    }

    public static int nextOdd(double d) {
        long i = Math.round(d);
        if (i % 2 == 1) {
            return (int) i;
        } else {
            return (int) (i + 1);
        }
    }

    public static int max(Map<String, int[]> data) {
        int max = Integer.MIN_VALUE;
        Iterator<String> keys = data.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            int[] datum = data.get(key);
            int tmp = max(datum);
            if (tmp > max) {
                max = tmp;
            }
        }
        return max;
    }

    /*
     * @ret the returned angle is in the range 0.0 through 180 degree
     */
    public static double getAngleInDegrees(double adjacent1, double adjacent2, double opposite) {
        return Math.toDegrees(getAngleInRadians(adjacent1, adjacent2, opposite));
    }

    /*
     * @ret the returned angle is in the range 0.0 through pi
     */
    public static Double getAngleInRadians(Double adjacent1, Double adjacent2, Double opposite) {
        if (adjacent1 != null && adjacent2 != null && opposite != null) {
            return Math.acos((adjacent1 * adjacent1 + adjacent2 * adjacent2 - opposite * opposite) / (2 * adjacent1 * adjacent2));
        } else {
            return null;
        }
    }

    public static Double getAngleInRadians(Point2D pt, Point2D pt2, Point2D pt3) {
        Double opposite = pt2.distance(pt3);
        Double adjacent1 = pt.distance(pt2);
        Double adjacent2 = pt.distance(pt3);
        Double ret = getAngleInRadians(adjacent1, adjacent2, opposite);
        return ret;
    }

    /**
     * upper-right: 0-PI/2(0-90) upper-left: PI/2-PI(90-180) lower-left:
     * PI-3PI/2(180-270) lower-right: 3PI/2-2PI(270-360)
     *
     * @return the angle in degrees in the range 0-360 exclusively
     */
    public static Double getAngleInDegrees(Point2D p, Point2D origin) {
        return Math.toDegrees(getAngleInRadians(p, origin));
    }

    /*
     * upper-right: 0-PI/2(0-90) upper-left: PI/2-PI(90-180) lower-left:
     * PI-3PI/2(180-270) lower-right: 3PI/2-2PI(270-360)
     */
    public static Double getAngleInRadians(Point2D p, Point2D origin) {
        Double ret = null;
        Double distance = p.distance(origin);
        Point2D opp = (Point2D) origin.clone();
        opp.setLocation(opp.getX() + distance, opp.getY());
        ret = getAngleInRadians(origin, p, opp);
        if (p.getY() < origin.getY()) { // upper right or upper left                   
            // nothing to do
        } else if (p.getY() > origin.getY()) { // lower left
            ret = Math.PI * 2 - ret;
        } else {
            if (p.getX() >= origin.getX()) {
                ret = 0.0;
            } else if (p.getX() < origin.getX()) {
                ret = Math.PI;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        double a = getAngleInDegrees(60, 60, 60);
        double b = Math.toDegrees(a);
        System.out.println(getAngleInDegrees(60, 60, 60));
        System.out.println(b);
        System.out.println(distance(0, 0, 1, 1));
    }
}
