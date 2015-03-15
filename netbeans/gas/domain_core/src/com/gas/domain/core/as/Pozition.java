package com.gas.domain.core.as;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.LocUtil;
import java.util.Comparator;
import org.biojavax.bio.seq.Position;

public class Pozition implements Cloneable {

    private int hibernateId;
    private boolean fuzzyStart;
    private boolean fuzzyEnd;
    private Boolean strand = true;
    private int rank;
    private Integer start;
    private Integer end;
    private String type;

    public Pozition() {
        this(null, null);
    }

    public Pozition(Integer start, Integer end) {
        this.start = start;
        this.end = end;
        this.type = "..";
    }

    public Boolean isCrossingOrigin() {
        Boolean ret = null;
        if (start != null && end != null) {
            ret = start > end;
        }
        return ret;
    }
    
    public void toTriplet(){
        int startMod = start % 3;
        if(startMod != 1){
            fuzzyStart = true;
            start = (int)Math.ceil(start / 3.0) + 1;
        }else{
            start = (int)Math.ceil(start / 3.0);
        }
                
        int endMod = end % 3;
        if(endMod != 0){
            fuzzyEnd = true;
        }
        end = (int)Math.ceil(end / 3);
    }
    
    public void circularize(int totalLength){
        if(end > totalLength){
            end = end % totalLength;
        }
    }

    @Override
    public Pozition clone() {
        Pozition ret = new Pozition();
        ret.setEnd(end);
        ret.setFuzzyEnd(fuzzyEnd);
        ret.setFuzzyStart(fuzzyStart);
        ret.setRank(rank);
        ret.setStart(start);
        ret.setStrand(strand);
        ret.setType(type);
        return ret;
    }

    public int getRank() {
        return rank;
    }

    /**
     * @param rank the order of the position in 5' to 3' direction
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    public Pozition(Pozition poz) {
        this.fuzzyStart = poz.isFuzzyStart();
        this.fuzzyEnd = poz.isFuzzyEnd();
        this.strand = poz.getStrand();
        this.start = poz.getStart();
        this.end = poz.getEnd();
        this.type = poz.getType();
    }

    public boolean isOverlapped(int _start, int _end) {
        return LocUtil.isOverlapped(start, end, _start, _end);
    }
    
    /**
     * Assuming the intersection cannot be 2
     *
     * @param start2 something
     * @param end2 something
     * @param totalLength something
     */
    public void sub(final int start2, final int end2, boolean circular, Integer totalLength) {
        LocList locList = LocUtil.intersect(start, end, start2, end2);
        if (!locList.isEmpty()) {
            int oldStart = start;
            int oldEnd = end;
            int newStart = locList.get(0).getStart();
            int newEnd = locList.get(0).getEnd();
            setStart(newStart);
            setEnd(newEnd);
            if (LocUtil.contains(oldStart, oldEnd, start2, true)) {
                fuzzyStart = true;
            }
            if (LocUtil.contains(oldStart, oldEnd, end2, true)) {
                fuzzyEnd = true;
            }

            translate(-(start2 - 1), circular, totalLength);
        }
    }
    
    /**
     * If the pozition contains the poz, it will return the splitted
     *
     * @param pos The position to split the pozition
     * @param totalPos The total position
     * @return An array of 2 pozitions by splitting at position indicated by
     * parameter "pos". {@code Null} if it does not contain the specified
     * position.
     */
    public Pozition[] split(int pos, int totalPos) {
        Pozition[] ret = null;
        Boolean contains = contains(pos, true);
        if (contains != null && contains) {
            Pozition c0 = clone();
            c0.setEnd(pos == 1 ? totalPos : pos - 1);

            Pozition c1 = clone();
            c1.setStart(pos);
            ret[0] = c0;
            ret[1] = c1;
        }
        return ret;
    }

    public Boolean contains(int pos) {
        return contains(pos, false);
    }

    public Boolean contains(int pos, boolean proper) {
        Boolean ret = null;
        if (start != null && end != null) {
            ret = LocUtil.contains(start, end, pos, proper);
        }
        return ret;
    }

    /**
     *
     * Assumption: poz is <b>NOT</b> a <b>SUBSET</b> of loc2 because it cannot
     * remove itself.<p>
     * If poz is <b>SUPERSET</b> of loc2, then there are following cases
     * <ul>
     * <li>if both linear, i.e.: <b>case a)|[start][start2][end2][end]</b>, then
     * {@code end -= width2;}
     * <li>loc1 linear & full length, loc2 crossing the origin, i.e.: <b>case b)
     * [start2][end]|[start][end2]</b>, then
     * {@code end -= width2, fuzzyStart = fuzzyEnd = true}
     * <li>Poz crossing the origin, loc2 is linear, i.e: <b>case
     * c1)[start]|[start2][end2][end]</b>, then
     * {@code start -= width2; end-= width2}
     * <li>Poz crossing the origin, loc2 is linear, i.e: <b>case
     * c2)[start][start2][end2]|[end]</b>, then nothing to do
     * <li>both crossing the origin, i.e.: <b>case c3)
     * [start][start2]|[end2][end]</b>, then {@code start -= end2 ; end -= end2}
     * </ul>
     * If poz <b>OVERLAPS</b> with lco2, but <b>NOT</b> a <b>SUPERSET or
     * SUBSET</b> of loc2,
     * <ul>
     * <li>if both linear, then <b>case d)[start][start2][end][end2]</b>, then
     * {@code end = start2 - 1; fuzzyEnd = true}
     * <li>if both linear, then <b>case e)[start2][start][end2][end]</b>, then
     * {@code start = start2; end -= width2; fuzzyStart = true}
     * <li>poz linear, loc2 crossing origin, <b>case
     * f)[start2][end]|[start][end2]</b>, then
     * {@code start = 1;end = start2 - end2 - 1; fuzzyStart = fuzzyEnd = true}
     * <li>poz linear, loc2 crossing origin, <b>case
     * g)[start2]|[start][end2][end]</b>, then
     * {@code start = 1;end -= end2;fuzzyStart = true}
     * <li>poz linear, loc2 crossing origin, <b>case
     * h)[start][start2][end]|[end2]</b>, then
     * {@code start -= end2; end = start2 - end2 - 1;fuzzyEnd = true}
     * <li>poz crossing origin, loc2 linear, <b> case
     * i)[start][end2]|[start2][end]</b>, then
     * {@code end = start2 - 1; start = (end2 + 1)- width2; fuzzyStart = fuzzyEnd = true}
     * <li>poz crossing origin, loc2 linear, <b> case
     * j)[start]|[start2][end][end2]</b>, then
     * {@code end = start2 - 1, start= start - width2; fuzzyEnd = true}
     * <li>poz crossing origin, loc2 linear, <b> case
     * k)[start2][start][end2]|[end]</b>, then
     * {@code start = end2 + 1 - width2; fuzzyStart = true}
     * </ul>
     * If <b>no OVERLAPS</b>,
     * <ul>
     * <li>both linear, <b>case l)[start2][end2][start][end]</b>, then
     * {@code start-=width2;end-=width2;}
     * <li>poz linear, loc2 crossing origin <b>case
     * m)[start2]|[end2][start][end]</b>, then {@code start-=end2; end-=end2;}
     * <li>poz crossing origin, loc2 linear <b>case
     * n)[start]|[end][start2][end2]</b>, then {@code start-=width2;}
     * </ul>
     *
     */
    protected void deleteSeq(final int start2, final int end2, final Integer totalPos) {
        final int startOld = start;
        final int endOld = end;
        final int width2 = LocUtil.width(start2, end2, totalPos).intValue();
        Integer newTotalPos = null;
        if (isSupersetOf(start2, end2)) {
            if (start <= end) {
                //case a)|[start][start2][end2][end]
                if (start2 <= end2) {
                    end -= width2;
                } //case b) [start2][end]|[start][end2] poz full length; loc2 cross origin
                else if (start2 > end2) { // special case: 
                    end -= width2;
                    fuzzyStart = fuzzyEnd = true;
                }
            } else {
                if (start2 <= end2) {
                    // case c1) [start]|[start2][end2][end]
                    if (end2 <= end) {
                        start -= width2;
                        end -= width2;
                    } // case c2)[start][start2][end2]|[end]
                    else {
                        // nothing to do
                    }
                } else {
                    //case c3) [start][start2]|[end2][end]
                    start = start - end2;
                    end = end - end2;
                }
            }
            newTotalPos = totalPos - width2;
        } else if (isOverlapped(start2, end2)) {
            if (start <= end) {
                if (start2 <= end2) {
                    //case d)[start][start2][end][end2]
                    if (LocUtil.contains(start, end, start2)) {
                        end = start2 - 1;
                        fuzzyEnd = true;
                    } //case e)[start2][start][end2][end]
                    else if (LocUtil.contains(start, end, end2)) {
                        start = start2;
                        end -= width2;
                        fuzzyStart = true;
                    }
                } else if (start2 > end2) {
                    // case f)[start2][end]|[start][end2]
                    if (LocUtil.contains(start, end, start2) && LocUtil.contains(start, end, end2)) {
                        start = 1;
                        end = start2 - end2 - 1;
                        fuzzyStart = fuzzyEnd = true;
                    }//case g)[start2]|[start][end2][end]
                    else if (LocUtil.contains(start, end, end2)) {
                        start = 1;
                        end -= end2;
                        fuzzyStart = true;
                    } //case h)[start][start2][end]|[end2]
                    else if (LocUtil.contains(start, end, start2)) {
                        start -= end2;
                        end = start2 - end2 - 1;
                        fuzzyEnd = true;
                    }
                }
            } else if (start > end) {
                if (start2 <= end2) {
                    //case i)[start][end2]|[start2][end]
                    if (LocUtil.contains(start, end, start2) && LocUtil.contains(start, end, end2)) {
                        end = start2 - 1;
                        start = (end2 + 1) - width2;
                        fuzzyStart = fuzzyEnd = true;
                    }//case j)[start]|[start2][end][end2] 
                    else if (LocUtil.contains(start, end, start2)) {
                        end = start2 - 1;
                        start = start - width2;
                        fuzzyEnd = true;
                    }//case k)[start2][start][end2]|[end]
                    else if (LocUtil.contains(start, end, end2)) {
                        start = end2 + 1 - width2;
                        fuzzyStart = true;
                    }
                }
            }
            int length = LocUtil.intersectLength(start2, end2, start2, end2, totalPos);
            newTotalPos = totalPos - length;
        } // no overlaps here 
        else if (start <= end) {
            //case l)[start2][end2][start][end]
            if (start2 <= end2) {
                if (end2 < start) {
                    start -= width2;
                    end -= width2;
                }
            }//case m)[start2]|[end2][start][end]
            else {
                start -= end2;
                end -= end2;
            }
            newTotalPos = totalPos - width2;
        } else if (start > end) { // since no overlap, loc2 must be linear            
            start -= width2;
            newTotalPos = totalPos - width2;
        }
        start = BioUtil.normalizeCircularPos(start, newTotalPos);
        end = BioUtil.normalizeCircularPos(end, newTotalPos);
        if (LocUtil.contains(start2, end2, startOld)) {
            fuzzyStart = true;
        }
        if (LocUtil.contains(start2, end2, endOld)) {
            fuzzyEnd = true;
        }

    }

    public boolean isSupersetOf(int _start, int _end) {
        return LocUtil.isSupersetOf(start, end, _start, _end);
    }

    public boolean isSubsetOf(int _start, int _end) {
        return LocUtil.isSubsetOf(start, end, _start, _end);
    }

    /**
     * <ul>If linear or circular but not crossing origin     
     * <li> case 1) [insert][NNN], then translate
     * <li> case 2) [NNN[insert]NN], then @code{end += insertLength}
     * <li> case 3) [NNN][insert], then nothing to do
     *
     * </ul>
     * <ul>If circular and crossing the origin
     * <li>case a)----[NNNN|NNNN]----[insert]
     * <li>case b)----[NNNN|NNN[insert]N]----
     * <li>case c)----[NN[insert]NN|NNNN]----
     * </ul>
     */
    protected void insert(int pos, int insertLength, boolean circular, int originalLength) {
        if (!circular || !isCrossingOrigin()) {
            // case 1)
            if (pos <= start) {
                translate(insertLength, circular, originalLength + insertLength);
            } // case 2)
            else if (pos <= end) {
                end += insertLength;
            } // case 3)
            else {
                // nothing to do
            }
        } else {
            //case a)----[NNNN|NNNN]----[insert]
            if (pos > end) {
                start += insertLength;
            } //case b)----[NNNN|NNN[insert]N]----
            else if (pos <= end) {
                end += insertLength;
                start += insertLength;
            } //case c)----[NN[insert]NN|NNNN]----
            else if (pos > start) {
                // nothing to do here
            }
        }
    }

    /**
     * Translates the pozitions by amount indicated by the parameter
     */
    public void translate(int amount, Boolean circular, Integer totalLength) {
        if (start == null || end == null) {
            return;
        }

        if (circular != null && circular) {
            start += amount;
            end += amount;

            start = BioUtil.normalizeCircularPos(start, totalLength);
            end = BioUtil.normalizeCircularPos(end, totalLength);
        } else if (circular == null || !circular) {
            start += amount;
            end += amount;

            if(start < 1 || start > totalLength){
                fuzzyStart = true;
            }     
            if(end < 1 || end > totalLength){
                fuzzyEnd = true;
            }             
            
            if (totalLength != null) {                
                start = Math.min(start, totalLength);
            }

            start = Math.max(start, 1);
            if (totalLength != null) {
                end = Math.min(end, totalLength);
            }
            end = Math.max(end, 1);
        }
    }

    protected int getHibernateId() {
        return hibernateId;
    }
    /*
     * For hibernate use only
     *
     */

    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Boolean getStrand() {
        return strand;
    }

    public void setStrand(Boolean strand) {
        this.strand = strand;
    }

    public boolean isFuzzyStart() {
        return fuzzyStart;
    }

    public void setFuzzyStart(boolean fuzzyStart) {
        this.fuzzyStart = fuzzyStart;
    }

    public boolean isFuzzyEnd() {
        return fuzzyEnd;
    }

    public void setFuzzyEnd(boolean fuzzyEnd) {
        this.fuzzyEnd = fuzzyEnd;
    }

    /**
     * Don't need to be persisted
     *
     */
    public Boolean isBetweenBases() {
        Boolean ret = null;
        if (type != null) {
            if (type.equals(Position.BETWEEN_BASES)) {
                ret = true;
            } else {
                ret = false;
            }
        }
        return ret;
    }

    /**
     * Don't need to be persisted
     *
     */
    public Boolean isInRange() {
        Boolean ret = null;
        if (type != null) {
            if (type.equals(Position.IN_RANGE)) {
                ret = true;
            } else {
                ret = false;
            }
        }
        return ret;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    static class PozComparator implements Comparator<Pozition> {

        Boolean strand = true;
        
        PozComparator(){
            this(true);
        }
        
        PozComparator(Boolean strand){
            this.strand = strand;
        }
        
        /**
         * The expected order for positive strand is the one with the smallest
         * start position at 1st order, 2nd at 2 order.
         */
        @Override
        public int compare(Pozition o1, Pozition o2) {
            int ret = o1.getRank() - o2.getRank();
            if (ret == 0) {
                ret = o1.getStart().compareTo(o2.getStart());
            }
            if(strand != null && !strand){
                ret = ret * -1;
            }
            return ret;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (strand != null && !strand) {
            ret.append("complement(");
        }
        if (isFuzzyStart()) {
            ret.append("<");
        }
        ret.append(start);
        if (start != end) {
            ret.append(type == null ? ".." : type);
            if (isFuzzyEnd()) {
                ret.append(">");
            }
            ret.append(end);
        }
        if (strand != null && !strand) {
            ret.append(")");
        }
        return ret.toString();
    }
}
