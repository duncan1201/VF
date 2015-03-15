/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

/**
 *
 * @author dq
 */
public class LocUtil {

    /**
     * @return the reverse complement position
     */
    public static int flip(int pos, int totalPos) {
        if (pos > totalPos) {
            throw new IllegalArgumentException(String.format("pos > totalPos:%d > %d ", pos, totalPos));
        }
        return totalPos - pos + 1;
    }

    /**
     * @return the reverse complement position
     */
    public static int reverseComplement(int pos, int totalPos) {
        return flip(pos, totalPos);
    }

    public static boolean contains(double startPos, double endPos, double pos) {
        return contains(startPos, endPos, pos, false);
    }

    public static boolean contains(double startPos, double endPos, double pos, boolean proper) {
        if (startPos <= endPos) {
            if (proper) {
                return pos > startPos && pos < endPos;
            } else {
                return pos >= startPos && pos <= endPos;
            }
        } else {
            if (proper) {
                return pos < endPos || pos > startPos;
            } else {
                return pos <= endPos || pos >= startPos;
            }
        }
    }
    
    /**
     * return the angle between two angles(in degree)
     */
    public static Double angleWidth(double startDegree, double endDegree){
        if(startDegree <= endDegree){
            return endDegree - startDegree;
        }else{
            return 360 - startDegree + endDegree;
        }
    }

    public static Double width(double start, double end, double totalPos) {
        if (start <= end) {
            return end - start + 1;
        } else {
            return totalPos - start + 1 + end;
        }
    }

    public static int getCenter(int start, int end, int totalPos) {
        if (start <= end) {
            return (start + end) / 2;
        } else {
            int width = LocUtil.width(start, end, totalPos).intValue();
            int ret = start + width / 2;
            if (ret > totalPos) {
                ret = ret % totalPos;
            }
            return 1;
        }
    }

    public static int getEnd(int start, int length, int totalPos) {
        int end;
        if (start + length - 1 <= totalPos) {
            end = start + length - 1;
        } else {
            end = start + length - 1 - totalPos;
        }
        return end;
    }

    public static boolean isFullLength(int start, int end, Integer totalPos) {
        int width = width(start, end, totalPos).intValue();
        return width == totalPos.intValue();
    }

    /**
     * <ol>
     * <li>If both linear, the max count is 1,
     * <ul>
     * <li>case a) count == 1 if any loc contains anther point from another loc
     * </ul>
     * </li>
     * <li>
     * If loc1 linear, loc2 crossing origin, then
     * <ul>
     * <li>case b) count == 2 if loc1 contains both the start and end point of
     * loc2
     * <li>case b1) count == 1 if lco2 connains both the start and end point of
     * lo1, but loc1 contains neither the start nor the end of loc2
     * <li>case c) count == 1 if loc2/1 contains either the start or the end
     * point of loc1/2
     * <li>case d) count == 0 if loc2/1 contains neither the start nor the end
     * point of loc1/2
     * </ul>
     * </li>
     * <li>If loc1 crossing origin, loc2 linear,
     * <ul>
     * <li>case e) switch loc1 and loc2, and apply logics in 2)
     * </ul>
     * </li>
     * <li>If both crossing origin, the max count is 1
     * <ul>
     * <li>case f) count == 1 if any loc contains anther point from another loc
     * <ul>
     * </li>
     * </ol>
     */
    public static int getOverlapCount(final int start, final int end, final int start2, final int end2) {
        int ret = 0;
        boolean overlap = isOverlapped(start, end, start2, end2);
        if (!overlap) {
            return 0;
        }
        if (start <= end) {
            //case a) count == 1 if any loc contains anther point from another loc
            if (start2 <= end2) {
                ret = LocUtil.contains(start, end, start2)
                        || LocUtil.contains(start, end, end2)
                        || LocUtil.contains(start2, end2, start)
                        || LocUtil.contains(start2, end2, end) ? 1 : 0;
            } else {
                //case b) count == 2 if loc1 contains both the start and end point of loc2
                if (LocUtil.contains(start, end, start2) && LocUtil.contains(start, end, end2)) {
                    ret = 2;
                } else {
                    ret = 1;
                }
            }
        } else {
            if (start2 <= end2) {
                //case e) switch loc1 and loc2, and apply logics in 2)
                ret = getOverlapCount(start2, end2, start, end);
            } else {
                //case f) count == 1 if any loc contains anther point from another loc
                ret = LocUtil.contains(start, end, start2) || LocUtil.contains(start, end, end2) ? 1 : 0;
            }
        }
        return ret;
    }

    public static boolean isOverlapped(int start, int end, int start2, int end2) {
        boolean ret = LocUtil.contains(start, end, start2)
                || LocUtil.contains(start, end, end2)
                || LocUtil.contains(start2, end2, start)
                || LocUtil.contains(start2, end2, end) ? true : false;
        return ret;
    }

    /**
     * Checks whether point indicated by start, end are subset of point
     * indicated by start2, end2
     *
     * @param totalLength
     */
    public static boolean isSubsetOf(int start, int end, int start2, int end2) {
        return isSubsetOf(start, end, start2, end2, false);
    }

    public static boolean isSubsetOf(int start, int end, int start2, int end2, boolean proper) {
        return isSupersetOf(start2, end2, start, end, proper);
    }

    public static boolean isSupersetOf(int start, int end, int start2, int end2) {
        return isSupersetOf(start, end, start2, end2, false);
    }

    /**
     * if <b>BOTH LINEAR</b>, the following must be true
     * <ul>
     * <li><b>case a) [start][start2][end2][end]</b>, i.e.:loc1 must contain
     * start and end points of loc2
     * </ul>
     * if loc 1 <b>LINEAR</b>, loc 2 <b>CROSSING ORIGIN</b>, no overlaps<p/>
     * if loc1 <b>CROSSING ORIGIN</b>, loc2 <b>LINEAR</b>, one of the following
     * must be true
     * <ul>
     * <li><b>case d1) [start]|[start2][end2][end]</b>,i.e.: loc1 must contain
     * start and end points of loc2
     * <li><b>case d2) [start][start2][end2]|[end]</b>,i.e.: loc1 must contain
     * start and end points of loc2
     * </ul>
     *
     if both p1 and p2 <b>CROSSING ORIGINS</b>, one of the following must be
     * true
     * <ul>
     * <li><b>case e) [start][start2]|[end2][end]</b>, i.e.: loc1 must contain
     * start and end points of loc2
     * </ul>
     */
    public static boolean isSupersetOf(int start, int end, int start2, int end2, boolean proper) {
        if (start <= end) {
            if (start2 <= end2) {
                //case a) [start][start2][end2][end] 
                return LocUtil.contains(start, end, start2, proper) && LocUtil.contains(start, end, end2, proper);
            }//case c) loc1 full length, loc2 crossing origin, no overlaps 
            else {// start2 > end2
                return false;
            }
        } else {  // pt 1 crossing origin, pt 2 linear
            if (start2 <= end2) {
                //case d1) [start]|[start2][end2][end]
                //case d2) [start][start2][end2]|[end]
                return LocUtil.contains(start, end, start2, proper) && LocUtil.contains(start, end, end2, proper);
            } else {// both crossing the origins              
                //case e) [start][start2]|[end2][end]                 
                return LocUtil.contains(start, end, start2, proper) && LocUtil.contains(start, end, end2, proper);
            }
        }
    }
}
