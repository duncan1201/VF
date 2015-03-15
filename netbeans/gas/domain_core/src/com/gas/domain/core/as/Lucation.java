package com.gas.domain.core.as;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.CommonUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.biojavax.bio.seq.CompoundRichLocation;

public class Lucation implements Cloneable {

    private int hibernateId;
    private String term;
    private Boolean strand;
    private List<Pozition> pozitions = new ArrayList<Pozition>();

    public Lucation() {
    }

    /**
     * must invoke setStrand(...) last, other pozitions' strand will be null
     */
    public Lucation(int min, int max, Boolean strand) {
        setContiguousMin(min);
        setContiguousMax(max);
        setStrand(strand);
    }

    public void clearPozitions() {
        pozitions.clear();
    }

    @Override
    public Lucation clone() {
        Lucation ret = new Lucation();
        ret.setPozitions(CommonUtil.copyOf(pozitions));
        ret.setStrand(strand);
        ret.setTerm(term);
        return ret;
    }

    public void translate(int f, boolean circular, Integer totalLength) {
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            poz.translate(f, circular, totalLength);
        }
    }
    
    public void circularize(int totalLength){
        Iterator<Pozition> itr = pozitions.iterator();
        while(itr.hasNext()){
            Pozition poz = itr.next();
            poz.circularize(totalLength);
        }
    }

    public Boolean contains(int pos) {
        return contains(pos, false);
    }

    public Boolean contains(int pos, boolean proper) {
        Boolean ret = pozitions.isEmpty() ? null : false;
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            if (poz.contains(pos, proper)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /*
     * For hibernate use only
     *
     */
    protected int getHibernateId() {
        return hibernateId;
    }
    
    public Integer width() {
        return width(null);
    }

    public Integer width(Integer totalPos) {
        Integer ret = null;
        Integer end = getEnd();
        Integer start = getStart();
        if (end != null && start != null) {            
            if(totalPos != null){                
                ret = LocUtil.width(start, end, totalPos).intValue();                                
            }else{
                ret = end - start + 1;
            }
        }
        return ret;
    }

    /*
     * For hibernate use only
     *
     */
    protected void setHibernateId(int hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Not be to persisted
     *
     */
    public Integer getStart() {
        Integer ret = null;
        if (pozitions.size() == 1) {
            ret = pozitions.iterator().next().getStart();
        } else if (pozitions.size() > 1) {
            Pozition[] pozArray = pozitions.toArray(new Pozition[pozitions.size()]);
            Arrays.sort(pozArray, new Pozition.PozComparator(strand));
            if (strand != null && strand) {
                ret = pozArray[0].getStart();
            } else if (strand != null && !strand) {
                ret = pozArray[pozArray.length - 1].getStart();
            } else {
                ret = pozArray[0].getStart();
            }
        }
        return ret;
    }

    public Boolean isCrossingOrigin() {
        Boolean ret;
        Iterator<Pozition> itr = pozitions.iterator();
        ret = itr.hasNext() ? false : null;
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            Boolean crossing = poz.isCrossingOrigin();
            if (crossing != null && crossing) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public boolean isEmpty() {
        return pozitions.isEmpty();
    }

    public boolean isOverlapped(int start2, int end2) {
        Integer start = getStart();
        Integer end = getEnd();
        return LocUtil.isOverlapped(start2, end2, start, end);
    }
    
    public void toTriplet(){
        Iterator<Pozition> itr = pozitions.iterator();
        while(itr.hasNext()){
            Pozition poz = itr.next();
            poz.toTriplet();
        }
    }
    
    public boolean isSupersetOf(int start2, int end2){
        Integer start = getStart();
        Integer end = getEnd();
        return LocUtil.isSupersetOf(start, end, start2, end2);
    }

    /**
     * Assuming the overlap count cannot be 2
     */
    public void sub(final int start, int end, boolean circular, Integer totalLength) {
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            poz.sub(start, end, circular, totalLength);
        }
    }
    
    /**
     * @return The two locations split by the location specified by the
     * parameter, null if the location does not properly contain the pos
     */
    public Lucation[] split(int pos, int totalPos) {
        Lucation[] ret = null;
        Boolean contains = contains(pos, true);
        if (contains != null && contains) {
            Collections.sort(pozitions, new Pozition.PozComparator());
            Iterator<Pozition> itr = pozitions.iterator();
            Lucation c0 = clone();
            c0.clearPozitions();
            Lucation c1 = clone();
            c1.clearPozitions();
            ret[0] = c0;
            ret[1] = c1;
            boolean found = false;
            while (itr.hasNext()) {
                Pozition poz = itr.next();
                if (!found) {
                    found = poz.contains(pos, true);
                    if (found) {
                        Pozition[] pozs = poz.split(pos, totalPos);
                        c0.addPozition(pozs[0]);
                        c1.addPozition(pozs[1]);
                        continue;
                    }
                }
                if (!found) {
                    c0.addPozition(poz);
                } else {
                    c1.addPozition(poz);
                }
            }
        }
        return ret;
    }    
    
    /**
     * The method will change its position as if deleting sequence whose
     * location indicated by parameters
     * <code>start2</code> to
     * <code>end2</code><p/>
     * <ul> <li>case a)if is a subset of loc2, remove it </ul>
     *
     * @param totalPos the total position before deletion
     * @see Pozition#deleteSeq(int start2, int end2, Integer totalLength)
     */
    public void deleteSeq(int start2, int end2, Integer totalPos) {
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            //case a)if is a subset of loc2, remove it
            if (poz.isSubsetOf(start2, end2)) {
                itr.remove();
            } else {
                poz.deleteSeq(start2, end2, totalPos);
            }
        }

    }

    /**
     <pre>
     If circular, 
     a). if crossing origin,
     b). if NOT crossing orgin, proceed as usually
     </pre>
     */
    public void insert(int pos, int insertLength, boolean circular, int originalLength) {
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            poz.insert(pos, insertLength, circular, originalLength);
        }
    }

    /**
     * Not be to persisted
     *
     */
    public Integer getEnd() {
        Integer ret = null;
        if (pozitions.isEmpty()) {
            return null;
        } else if (pozitions.size() == 1) {
            ret = pozitions.iterator().next().getEnd();
        } else {
            Pozition[] pozArray = pozitions.toArray(new Pozition[pozitions.size()]);
            Arrays.sort(pozArray, new Pozition.PozComparator(strand));
            if (strand != null && strand) {
                ret = pozArray[pozArray.length - 1].getEnd();
            } else if (strand != null && !strand) {
                ret = pozArray[0].getEnd();
            } else {
                ret = pozArray[pozArray.length - 1].getEnd();
            }
        }
        return ret;
    }

    public Boolean getStrand() {
        return strand;
    }

    public void setStrand(Boolean strand) {
        this.strand = strand;
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            poz.setStrand(strand);
        }
    }

    public Boolean isFuzzyStart() {
        Boolean ret = null;
        if (isContiguous() != null && isContiguous()) {
            Iterator<Pozition> itr = pozitions.iterator();
            Pozition poz = itr.next();
            ret = poz.isFuzzyStart();
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    public String getType() {
        String ret = null;
        if (isContiguous() != null && isContiguous()) {
            Iterator<Pozition> itr = pozitions.iterator();
            Pozition poz = itr.next();
            ret = poz.getType();
        }
        return ret;
    }

    public Boolean isFuzzyEnd() {
        Boolean ret = null;
        if (isContiguous() != null && isContiguous()) {
            Iterator<Pozition> itr = pozitions.iterator();
            Pozition poz = itr.next();
            ret = poz.isFuzzyEnd();
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    public Boolean isContiguous() {
        Boolean ret = null;
        if (!pozitions.isEmpty()) {
            ret = pozitions.size() == 1;
        }
        return ret;
    }

    /*
     * for hibernate only
     */
    protected List<Pozition> getPozitions() {
        return pozitions;
    }

    public Iterator<Pozition> getPozitionsItr() {
        Collections.sort(pozitions, new Pozition.PozComparator());
        return pozitions.iterator();
    }
    
    public Iterator<Pozition> getPozitionsItr2(){
        Collections.sort(pozitions, new Pozition.PozComparator(getStrand()));
        return pozitions.iterator();
    }

    public void addPozition(Pozition poz) {
        pozitions.add(poz);
        if (strand == null) {
            strand = poz.getStrand();
        }
    }

    public void setContiguousFuzzyStart(boolean fuzzy) {
        if (pozitions.isEmpty()) {
            Pozition poz = new Pozition();
            poz.setFuzzyStart(fuzzy);
            addPozition(poz);
        } else {
            Pozition poz = pozitions.iterator().next();
            poz.setFuzzyStart(fuzzy);
        }
    }

    public Boolean isContiguousFuzzyStart() {
        Boolean ret = null;
        if (!pozitions.isEmpty()) {
            Pozition poz = pozitions.iterator().next();
            ret = poz.isFuzzyStart();
        }
        return ret;
    }

    public Boolean isContiguousFuzzyEnd() {
        Boolean ret = null;
        if (!pozitions.isEmpty()) {
            Pozition poz = pozitions.iterator().next();
            ret = poz.isFuzzyEnd();
        }
        return ret;
    }

    public void setContiguousFuzzyEnd(boolean fuzzy) {
        if (pozitions.isEmpty()) {
            Pozition poz = new Pozition();
            poz.setFuzzyEnd(fuzzy);
            addPozition(poz);
        } else {
            Pozition poz = pozitions.iterator().next();
            poz.setFuzzyEnd(fuzzy);
        }
    }

    public void setContiguousMax(int max) {
        if (pozitions.isEmpty()) {
            Pozition poz = new Pozition();
            poz.setEnd(max);
            addPozition(poz);
        } else {
            Pozition poz = pozitions.iterator().next();
            poz.setEnd(max);
        }
    }

    private Pozition getMinPoz() {
        Pozition ret = null;
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            if (ret == null || ret.getStart() > poz.getStart()) {
                ret = poz;
            }
        }
        return ret;
    }

    private Pozition getMaxPoz() {
        Pozition ret = null;
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            if (ret == null || ret.getEnd() < poz.getEnd()) {
                ret = poz;
            }
        }
        return ret;
    }

    public void setContiguousMin(int min) {
        if (!pozitions.isEmpty()) {
            Pozition poz = pozitions.iterator().next();
            poz.setStart(min);
        } else {
            Pozition poz = new Pozition();
            poz.setStart(min);
            addPozition(poz);
        }
    }

    public void setPozitions(List<Pozition> pozitions) {
        this.pozitions = pozitions;
    }
    
    public Loc toLoc() {
        Loc ret = new Loc(getStart(), getEnd());
        ret.setStrand(strand);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        int min = getStart();
        int max = getEnd();

        if (isContiguous()) {
            if (getStrand() == null) {
                ret.append(min);
                ret.append(max);
            } else if (getStrand()) {
                Iterator<Pozition> pozItr = pozitions.iterator();
                Pozition poz = pozItr.next();
                ret.append(poz);
            } else {
                ret.append("complement");
                ret.append("(");
                ret.append(min);
                ret.append("..");
                ret.append(max);
                ret.append(")");
            }
        } else {
            if (CompoundRichLocation.getJoinTerm().toString().equalsIgnoreCase(term)) {
                ret.append("Join");
            } else if (CompoundRichLocation.getOrderTerm().toString().equalsIgnoreCase(term)) {
                ret.append("Order");
            }

            ret.append("(");
            Iterator<Pozition> pozItr = pozitions.iterator();
            while (pozItr.hasNext()) {
                Pozition poz = pozItr.next();
                ret.append(poz.toString());
                if (pozItr.hasNext()) {
                    ret.append(",");
                }
            }
            ret.append(")");
        }
        return ret.toString();
    }

    public void flip(int totalLength) {
        setStrand(!strand);
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition poz = itr.next();
            int newStart = LocUtil.flip(poz.getEnd(), totalLength);
            int newEnd = LocUtil.flip(poz.getStart(), totalLength);
            poz.setStart(newStart);
            poz.setEnd(newEnd);
            poz.setStrand(!poz.getStrand());
            boolean fuzzyStartOld = poz.isFuzzyStart();
            boolean fuzzyEndOld = poz.isFuzzyEnd();
            
            poz.setFuzzyStart(fuzzyEndOld);
            poz.setFuzzyEnd(fuzzyStartOld);
        }
    }
}
