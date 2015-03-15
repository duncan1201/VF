/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.core.StringSet;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Qualifier;
import java.util.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author dunqiang
 */
public class RMap implements Cloneable {

    public static String HEADER_PREFIX = "RestrictionMap";
    static String DELIMITER = "#";
    private String id;
    private Integer hibernateId;
    private Set<Entry> entries = new HashSet<Entry>();
    private InputParams inputParams = new InputParams();
    private AnalysisDetails analysisDetails = new AnalysisDetails();

    public RMap() {
    }

    protected Integer getHibernateId() {
        return hibernateId;
    }

    public AnalysisDetails getAnalysisDetails() {
        return analysisDetails;
    }
    
    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public RMap clone() {
        RMap ret = new RMap();
        if (inputParams != null) {
            ret.setInputParams(inputParams.clone());
        }
        ret.setEntries(CommonUtil.copyOf(entries));
        return ret;
    }

    public void clearResults() {
        entries.clear();
    }

    public InputParams getInputParams() {
        return inputParams;
    }

    public void setInputParams(InputParams inputParams) {
        this.inputParams = inputParams;
    }

    public void filter(int minOccurrence, int maxOccurence) {
        Map<String, Integer> siteCounter = new HashMap<String, Integer>();

        Iterator<Entry> entryItr = entries.iterator();
        while (entryItr.hasNext()) {
            Entry entry = entryItr.next();
            Integer count = siteCounter.get(entry.getName());
            if (count == null) {
                count = 0;
            }
            count++;
            siteCounter.put(entry.getName(), count);
        }

        entryItr = entries.iterator();
        while (entryItr.hasNext()) {
            Entry entry = entryItr.next();
            int count = siteCounter.get(entry.getName());
            if (count < minOccurrence || count > maxOccurence) {
                entryItr.remove();
                if(count < minOccurrence){
                    getAnalysisDetails().incrementTooFewTimesCount();
                }else{
                    getAnalysisDetails().incrementTooManyTimesCount();
                }
            }
        }
    }

    public Map<String, Integer> getCount() {
        Map<String, Integer> ret = new HashMap<String, Integer>();
        Iterator<Entry> entryItr = getEntriesIterator();
        while (entryItr.hasNext()) {
            Entry entry = entryItr.next();
            Integer count = ret.get(entry.getName());
            if (count == null) {
                count = 0;
            }
            count++;
            ret.put(entry.getName(), count);
        }
        return ret;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public Set<Entry> getReadOnlyEntries() {
        Set<Entry> ret = Collections.unmodifiableSet(new HashSet<Entry>(entries));
        return ret;
    }

    protected void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void addEntries(Collection<Entry> _entries) {
        this.entries.addAll(_entries);
    }

    public Iterator<Entry> getEntriesIterator() {
        Set<Entry> ret = new TreeSet<Entry>(new NameStartPosComparator());
        ret.addAll(entries);
        return ret.iterator();
    }

    public Iterator<Entry> getEntriesIterator(Comparator<Entry> comparator) {
        Set<Entry> ret = new TreeSet<Entry>(comparator);
        ret.addAll(entries);
        return ret.iterator();
    }

    public Set<RMap.Entry> getSortedEntries(StringList names) {
        Set<RMap.Entry> ret = new TreeSet<RMap.Entry>(new StartPosComparator());
        Iterator<RMap.Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            RMap.Entry entry = itr.next();
            if (names.containsIgnoreCase(entry.getName())) {
                ret.add(entry);
            }
        }
        return ret;
    }

    public StringList getEntryNames() {
        StringList ret = new StringList();
        Iterator<Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            ret.add(entry.getName());
        }
        return ret;
    }

    public int getSize() {
        return entries.size();
    }

    /*
     * @param str follows the pattern "entry#entry#entry"
     */
    public static RMap parse(String str) {
        RMap ret = new RMap();

        String[] splits = str.split("=");
        String header = splits[0];
        String _id = header.substring(HEADER_PREFIX.length() - 1);
        ret.setId(_id);
        if (splits.length > 1) {
            String entriesStr = splits[1];
            splits = entriesStr.split(RMap.DELIMITER);
            for (String entryStr : splits) {
                Entry entry = Entry.parse(entryStr);
                ret.addEntry(entry);
            }
        }
        return ret;
    }

    /**
     *
     */
    public void removeEntries(int pos) {
        Iterator<Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            Integer start = entry.getStart();
            Integer end = entry.getEnd();
            boolean contains = LocUtil.contains(start, end, pos);
            if (contains) {
                itr.remove();
            }
        }
    }

    /**
     * Removes the entries fall outside the range
     *
     * @param startPos: inclusive
     * @param endPos: inclusive
     */
    public void sub(int startPos, int endPos) {
        Iterator<Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            Integer start = entry.getStart();
            Integer end = entry.getEnd();
            boolean supserset = LocUtil.isSupersetOf(startPos, endPos, start, end);
            if (!supserset) {
                itr.remove();
            }
        }
    }

    public void flip(int totalPos) {
        Iterator<RMap.Entry> rItr = entries.iterator();
        while (rItr.hasNext()) {
            RMap.Entry entry = rItr.next();
            entry.flip(totalPos);
        }
    }

    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
     * entry#entry#entry
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        ret.append(HEADER_PREFIX);
        ret.append(getId());
        ret.append("=");

        Iterator<Entry> entryItr = entries.iterator();
        while (entryItr.hasNext()) {
            Entry entry = entryItr.next();
            ret.append(entry.toString());
            if (entryItr.hasNext()) {
                ret.append(DELIMITER);
            }
        }
        return ret.toString();
    }

    public List<Feture> toFetures() {
        List<Feture> ret = new ArrayList<Feture>();
        Iterator<Entry> itr = getEntriesIterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            Feture feture = entry.toFeture();
            ret.add(feture);
        }
        return ret;
    }

    public static class Entry implements Cloneable {

        static final String INNER_DELIMITER = "@";
        private Integer hibernateId;
        private String name;
        private String seq;
        private String overhang;
        private transient String visual;
        private Integer start;
        private Integer end;
        private int[] upstreamCutPos;
        private int[] downstreamCutPos;
        private Integer downstreamCutType;

        @Override
        public Entry clone() {
            Entry ret = CommonUtil.cloneSimple(this);
            ret.setVisual(visual);
            if (downstreamCutPos != null) {
                ret.setDownstreamCutPos(Arrays.copyOf(downstreamCutPos, downstreamCutPos.length));
            }
            if (upstreamCutPos != null) {
                ret.setUpstreamCutPos(Arrays.copyOf(upstreamCutPos, upstreamCutPos.length));
            }
            return ret;
        }
        
        @Override
        public boolean equals(Object other){
            boolean result;
            if(other == this){
                return true;
            }
            else if((other == null) || (getClass() != other.getClass())){
                result = false;
            } // end if
            else{
                RMap.Entry otherEntry = (RMap.Entry)other;                
                result = ObjectUtils.equals(otherEntry.getEnd(), this.getEnd())
                        && ObjectUtils.equals(otherEntry.getName(), this.getName())
                        && ObjectUtils.equals(otherEntry.getSeq(), this.getSeq())
                        && ObjectUtils.equals(otherEntry.getStart(), this.getStart());                
            }
            return result;
        }
        
        @Override
        public int hashCode(){            
            return new HashCodeBuilder().append(getEnd()).append(getName()).append(getSeq()).append(getStart()).toHashCode();
        }

        public Feture toFeture() {
            Feture ret = new Feture();
            ret.setKey(FetureKeyCnst.RECOGNITION_SITE);
            ret.setLucation(new Lucation(start, end, null));
            ret.getQualifierSet().add(new Qualifier(String.format("label=%s", name)));

            ret.getQualifierSet().add(new Qualifier(String.format("recognition_site=%s", getVisual())));
            if (overhang != null && !overhang.isEmpty()) {
                ret.getQualifierSet().add(new Qualifier(String.format("Overhang=%s", overhang)));
            }
            ret.getQualifierSet().add(new Qualifier(String.format("vector friend type=%s", "restriction site")));
            return ret;
        }

        public Integer getOverhangLength() {
            Integer ret = null;
            if (downstreamCutPos != null && downstreamCutPos.length == 2) {
                int max = Math.max(downstreamCutPos[0], downstreamCutPos[1]);
                int min = Math.min(downstreamCutPos[0], downstreamCutPos[1]);
                ret = max - min;
            }
            return ret;
        }

        public void setOverhang(String overhang) {
            this.overhang = overhang;
        }

        public String getOverhang() {
            return this.overhang;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        /**
         * @see {@link REN}
         */
        public Integer getDownstreamCutType() {
            return downstreamCutType;
        }

        public void setDownstreamCutType(Integer downstreamCutType) {
            this.downstreamCutType = downstreamCutType;
        }

        public String getVisual() {
            return visual;
        }

        public void setVisual(String visual) {
            this.visual = visual;
        }

        public int[] getDownstreamCutPos() {
            return downstreamCutPos;
        }

        public void setDownstreamCutPos(int[] downstreamCutPos) {
            this.downstreamCutPos = downstreamCutPos;
        }

        public int[] getUpstreamCutPos() {
            return upstreamCutPos;
        }

        public void setUpstreamCutPos(int[] upstreamCutPos) {
            this.upstreamCutPos = upstreamCutPos;
        }

        public Integer getStart() {
            return start;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public void flip(int totalPos) {
            int newStart = LocUtil.flip(end, totalPos);
            int newEnd = LocUtil.flip(start, totalPos);
            setStart(newStart);
            setEnd(newEnd);
        }
        
        public Loc toLoc(){
            Loc ret = new Loc(start, end);
            return ret;
        }


        /*
         * @param from 1-based inclusive
         */
        public void setStart(Integer start) {
            this.start = start;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getEnd() {
            return end;
        }

        /*
         * @param to 1-based inclusive
         */
        public void setEnd(Integer end) {
            this.end = end;
        }

        /*
         * name@from@to
         */
        public static Entry parse(String d) {
            Entry ret = new Entry();
            String[] splits = d.split(INNER_DELIMITER);
            int i = 0;

            ret.setName(splits[i++]);
            ret.setStart(CommonUtil.parseInt(splits[i++]));
            ret.setEnd(CommonUtil.parseInt(splits[i++]));

            return ret;
        }

        /*
         * name@from@to
         */
        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append(getName());
            ret.append(INNER_DELIMITER);
            ret.append(getStart());
            ret.append(INNER_DELIMITER);
            ret.append(getEnd());
            ret.append(INNER_DELIMITER);
            return ret.toString();
        }
    }

    public static class EntryList extends ArrayList<Entry> {

        public EntryList(){}
        
        public EntryList(Entry entry){
            add(entry);
        }
        
        public Entry getEntry(String name, Integer startPos, Integer endPos){
            Entry ret = null;
            Iterator<Entry> itr = iterator();
            while(itr.hasNext()){
                Entry entry = itr.next();
                if(entry.getName().equalsIgnoreCase(name) && startPos.equals(entry.getStart()) && endPos.equals(entry.getEnd())){
                    ret = entry;
                    break;
                }
            }
            return ret;
        }
        
        public StringList getAllNames() {
            StringList ret = new StringList();
            for (int i = 0; i < size(); i++) {
                Entry entry = get(i);
                ret.add(entry.getName());
            }
            return ret;
        }

        public StringSet getAllUniqueNames() {
            StringSet ret = new StringSet();
            for (int i = 0; i < size(); i++) {
                Entry entry = get(i);
                ret.add(entry.getName());
            }
            return ret;
        }

        public String getDisplayNames(boolean unique) {
            StringBuilder ret = new StringBuilder();
            Iterator<String> itr;
            if (unique) {
                itr = getAllUniqueNames().iterator();
            } else {
                itr = getAllNames().iterator();
            }
            while (itr.hasNext()) {
                String name = itr.next();
                ret.append(name);
                if (itr.hasNext()) {
                    ret.append(',');
                    ret.append(' ');
                }
            }
            return ret.toString();
        }
        
        public String toString(boolean includePositions){
            StringBuilder ret = new StringBuilder();           
            Iterator<RMap.Entry> itr = iterator();
            while(itr.hasNext()){
                RMap.Entry entry = itr.next();
                ret.append(entry.getName());
                if(includePositions){
                    ret.append(String.format("(%d-%d)", entry.getStart(), entry.getEnd()));
                }
                if(itr.hasNext()){
                    ret.append(",");
                }
            }
            return ret.toString();
        }
    }

    public static class StartPosComparator implements Comparator<Entry> {

        @Override
        public int compare(Entry o1, Entry o2) {
            int ret = o1.getStart().compareTo(o2.getStart());

            return ret;
        }
    }

    public static class NameStartPosComparator implements Comparator<Entry> {

        @Override
        public int compare(Entry o1, Entry o2) {
            int ret = 0;
            ret = o1.getName().compareToIgnoreCase(o2.getName());
            if (ret == 0) {
                ret = o1.getStart().compareTo(o2.getStart());
            }
            return ret;
        }
    }
    
    public static class AnalysisDetails {
    
        private int totalCandidates = 0;
        private int outOfRangeCount = 0;
        private int tooManyTimesCount = 0;
        private int tooFewTimesCount = 0;

        public void setTotalCandidates(int totalCandidates) {
            this.totalCandidates = totalCandidates;
        }
        
        public void incrementOutOfRangeCount(){
            outOfRangeCount++;
        }
        
        public void incrementTooManyTimesCount(){
            tooManyTimesCount++;
        }
        
        public void incrementTooFewTimesCount(){
            tooFewTimesCount++;
        }

        public int getTotalCandidates() {
            return totalCandidates;
        }

        public int getOutOfRangeCount() {
            return outOfRangeCount;
        }

        public int getTooManyTimesCount() {
            return tooManyTimesCount;
        }

        public int getTooFewTimesCount() {
            return tooFewTimesCount;
        }                
    }

    public static class InputParams implements Cloneable {

        private Integer hibernateId;
        private Boolean allow;
        private Integer startPos;
        private Integer endPos;
        private Integer minOccurence;
        private Integer maxOccurence;
        private String renListName;
        private Set<String> renNames;

        @Override
        public InputParams clone() {
            InputParams ret = new InputParams();

            ret.setAllow(allow);
            ret.setEndPos(endPos);
            ret.setMinOccurence(minOccurence);
            ret.setMaxOccurence(maxOccurence);
            ret.setRenListName(renListName);
            ret.setRenNames(CommonUtil.copyOf(renNames));
            ret.setStartPos(startPos);
            return ret;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Set<String> getRenNames() {
            return renNames;
        }

        public void setRenNames(Set<String> renNames) {
            this.renNames = renNames;
        }

        public String getRenListName() {
            return renListName;
        }

        public void setRenListName(String renListName) {
            this.renListName = renListName;
        }

        public Integer getMinOccurence() {
            return minOccurence;
        }

        public void setMinOccurence(Integer minOccurence) {
            this.minOccurence = minOccurence;
        }

        public Integer getMaxOccurence() {
            return maxOccurence;
        }

        public void setMaxOccurence(Integer maxOccurence) {
            this.maxOccurence = maxOccurence;
        }

        public Boolean getAllow() {
            return allow;
        }

        public void setAllow(Boolean allow) {
            this.allow = allow;
        }

        public Integer getStartPos() {
            return startPos;
        }

        public void setStartPos(Integer startPos) {
            this.startPos = startPos;
        }

        public Integer getEndPos() {
            return endPos;
        }

        public void setEndPos(Integer endPos) {
            this.endPos = endPos;
        }
    }
}
