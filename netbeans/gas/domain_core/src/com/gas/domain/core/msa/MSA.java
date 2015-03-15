/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.IExportable;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.aln.Aln;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaWriter;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.clustalw.ClustalTreeParam;
import com.gas.domain.core.msa.muscle.MuscleParam;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import com.gas.domain.core.nexus.api.Nexus;
import java.awt.Point;
import java.util.*;

/**
 *
 * @author dq
 */
public class MSA implements IFolderElement, Cloneable, IExportable {

    private String hibernateId;
    private String name;
    private boolean read;
    private String type;// DNA or protein
    private String desc;
    private Date lastModifiedDate;;
    private Folder folder;
    private String prevFolderPath;
    private Set<Entry> entries = new HashSet<Entry>();
    private ConsensusParam consensusParam;
    private SeqLogoParam seqLogoParam;
    private Set<ClustalTreeParam> clustalTreeParams = new HashSet<ClustalTreeParam>();
    private Set<ClustalwParam> clustalwParams = new HashSet<ClustalwParam>();
    private Set<MuscleParam> muscleParams = new HashSet<MuscleParam>();
    private Set<VfMsaParam> vfMsaParams = new HashSet<VfMsaParam>();
    private MSASetting msaSetting = new MSASetting();
    private RichConsensus richConsensus;
    private Integer length;
    private String subMatrix;
    private transient Newick newick;
    private transient boolean newickDirty = false;
    private String newickStr; // for hibernate use only    

    public MSA() {
        consensusParam = new ConsensusParam();
    }

    @Override
    public MSA clone() {
        MSA ret = CommonUtil.cloneSimple(this);
        ret.setClustalwParams(CommonUtil.copyOf(getClustalwParams()));
        ret.setConsensusParam(consensusParam.clone());
        ret.setEntries(CommonUtil.copyOf(entries));
        ret.setFolder(folder);
        ret.setMsaSetting(msaSetting.clone());
        ret.setMuscleParams(CommonUtil.copyOf(getMuscleParams()));
        ret.setNewickStr(newickStr);
        ret.setRichConsensus(richConsensus.clone());
        if (seqLogoParam != null) {
            ret.setSeqLogoParam(seqLogoParam.clone());
        }
        return ret;
    }

    public MSASetting getMsaSetting() {
        return msaSetting;
    }

    public void setMsaSetting(MSASetting msaSettings) {
        this.msaSetting = msaSettings;
    }
    
    public void setClustalTreeParam(ClustalTreeParam param) {
        Set<ClustalTreeParam> tmp = new HashSet<ClustalTreeParam>();
        tmp.add(param);
        setClustalTreeParams(tmp);
    }

    public ClustalTreeParam getClustalTreeParam() {
        if (clustalTreeParams.isEmpty()) {
            setClustalTreeParam(new ClustalTreeParam());
        }
        return clustalTreeParams.iterator().next();
    }

    /**
     * for hibernate use only
     */
    protected Set<ClustalTreeParam> getClustalTreeParams() {
        return clustalTreeParams;
    }

    /**
     * for hibernate use only
     */
    protected void setClustalTreeParams(Set<ClustalTreeParam> clustalTreeParams) {
        this.clustalTreeParams = clustalTreeParams;
    }

    public String getSubMatrix() {
        return subMatrix;
    }

    public void setSubMatrix(String subMatrix) {
        this.subMatrix = subMatrix;
    }

    /**
     * for hibernate use only
     */
    protected RichConsensus getRichConsensus() {
        return richConsensus;
    }

    /**
     * for hibernate use only
     */
    protected void setRichConsensus(RichConsensus richConsensus) {
        this.richConsensus = richConsensus;
    }

    public void renameEntry(String nameOld, String nameNew) {
        Iterator<Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            if (entry.getName().equals(nameOld)) {
                entry.setName(nameNew);
                break;
            }
        }
    }

    /**
     * hibernate use only
     */
    protected Set<VfMsaParam> getVfMsaParams() {
        return vfMsaParams;
    }

    /**
     * hibernate use only
     */
    protected void setVfMsaParams(Set<VfMsaParam> vfMsaParams) {
        this.vfMsaParams = vfMsaParams;
    }
    
    public void setVfMsaParam(VfMsaParam param){
        this.vfMsaParams.clear();
        this.vfMsaParams.add(param);
    }
    
    public VfMsaParam getVfMsaParam(){
        if(vfMsaParams.isEmpty()){
            return null;
        }else{
            return vfMsaParams.iterator().next();
        }
    }
    
    public void setClustalwParam(ClustalwParam param) {
        this.clustalwParams.clear();
        this.clustalwParams.add(param);
    }

    public ClustalwParam getClustalwParam() {
        if (clustalwParams.isEmpty()) {
            return null;
        } else {
            return clustalwParams.iterator().next();
        }
    }

    public void setMuscleParam(MuscleParam param) {
        this.muscleParams.clear();
        this.muscleParams.add(param);
    }

    public MuscleParam getMuscleParam() {
        if (this.muscleParams.isEmpty()) {
            return null;
        } else {
            return muscleParams.iterator().next();
        }
    }

    /**
     * for hibernate use only
     */
    protected Set<ClustalwParam> getClustalwParams() {
        return clustalwParams;
    }

    /**
     * for hibernate use only
     */
    protected Set<MuscleParam> getMuscleParams() {
        return muscleParams;
    }

    /**
     * for hibernate use only
     */
    protected void setMuscleParams(Set<MuscleParam> muscleParams) {
        this.muscleParams = muscleParams;
    }

    /**
     * for hibernate use only
     */
    protected void setClustalwParams(Set<ClustalwParam> msaParams) {
        this.clustalwParams = msaParams;
    }

    @Override
    public String getPrevFolderPath() {
        return prevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String prevFolderPath) {
        this.prevFolderPath = prevFolderPath;
    }

    public SeqLogoParam getSeqLogoParam() {
        return seqLogoParam;
    }

    public void setSeqLogoParam(SeqLogoParam seqLogoParam) {
        this.seqLogoParam = seqLogoParam;
    }

    public ConsensusParam getConsensusParam() {
        return consensusParam;
    }

    public void setConsensusParam(ConsensusParam consensusParam) {
        this.consensusParam = consensusParam;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    public Boolean isDnaByGuess() {
        return guessType();
    }

    public Boolean isDNA() {
        return "DNA".equalsIgnoreCase(type);
    }

    private boolean guessType() {
        String seq = getNogap(20);
        boolean ret = BioUtil.isDNAByGuess(seq);
        return ret;
    }

    /**
     * @param name: possible values: "consensus" or entry name
     */
    public String getSeq(String name, Loc loc) {
        String ret = null;
        if (name.equalsIgnoreCase("consensus")) {
            ret = StrUtil.sub(getConsensus(), loc.getStart(), loc.getEnd());
        } else {
            Iterator<Entry> itr = entries.iterator();
            while (itr.hasNext()) {
                Entry entry = itr.next();
                if (entry.getName().equalsIgnoreCase(name)) {
                    ret = StrUtil.sub(entry.getData(), loc.getStart(), loc.getEnd());
                    break;
                }
            }
        }
        return ret;
    }

    private String getNogap(int max) {
        StringBuilder ret = new StringBuilder();
        Iterator<Entry> itr = getEntries().iterator();
        while (itr.hasNext() && ret.length() < max) {
            Entry entry = itr.next();
            String data = entry.getData();
            for (int i = 0; i < data.length() && ret.length() < max; i++) {
                if (Character.isLetter(data.charAt(i))) {
                    ret.append(data.charAt(i));
                }
            }
        }
        return ret.toString();
    }

    /*
     * 1-based. If loc includes consensus, the corresponding columns will also be deleted
     */
    public void delete(Loc2D loc) {
        EntryList entryList = new EntryList();
        entryList.addAll(getEntries());
        Collections.sort(entryList);
        if (loc.getMinY() == 1) {
            setConsensus(StrUtil.delete(getConsensus(), loc.getMinX(), loc.getMaxX()));
            entryList.delete(1, loc.getMinX(), entryList.size(), loc.getMaxX());
        } else if (loc.getMinY() > 1) {
            entryList.delete(loc.getMinY() - 1, loc.getMinX(), loc.getMaxY() - 1, loc.getMaxX());
            if (loc.height() == entryList.size()) {
                setConsensus(StrUtil.delete(getConsensus(), loc.getMinX(), loc.getMaxX()));
            }
        }
    }

    public void replace(Loc2D loc, String str) {
        EntryList entryList = new EntryList();
        entryList.addAll(getEntries());
        Collections.sort(entryList);
        if (loc.getMinY() == 1) {
            setConsensus(StrUtil.replace(getConsensus(), loc.getMinX(), loc.getMaxX(), str));
            entryList.replace(1, loc.getMinX(), entryList.size(), loc.getMaxX(), str);
        } else if (loc.getMinY() > 1) {
            entryList.replace(loc.getMinY() - 1, loc.getMinX(), loc.getMaxY() - 1, loc.getMaxX(), str);
            int lengthDelta = str.length() - loc.width();
            if (lengthDelta > 0) {
                String GAPs = StrUtil.createString("-", lengthDelta);
                setConsensus(getConsensus() + GAPs);
            }
        }
    }

    /**
     * @param loc: 1-based. The first row is the consensus
     */
    public void insert(Point loc, String str) {
        EntryList entryList = new EntryList();
        entryList.addAll(getEntries());
        Collections.sort(entryList);
        if (loc.y == 1) {
            setConsensus(StrUtil.insert(getConsensus(), loc.x, str));
            entryList.insert(1, entryList.size(), loc.x, str);
        } else if (loc.y > 1) {
            entryList.insert(loc.y - 1, loc.y - 1, loc.x, str);
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public void setQualityScores(int[] qualityScores) {
        if (richConsensus == null) {
            setRichConsensus(new RichConsensus());
        }
        getRichConsensus().setQualities(qualityScores);
    }

    public int[] getQualityScores() {
        if (richConsensus == null) {
            setRichConsensus(new RichConsensus());
        }
        return richConsensus.getQualities();
    }

    public String getConsensus() {
        if (richConsensus == null) {
            setRichConsensus(new RichConsensus());
        }
        return richConsensus.getBases();
    }

    public void setConsensus(String consensus) {
        if (richConsensus == null) {
            setRichConsensus(new RichConsensus());
        }
        getRichConsensus().setBases(consensus);
        setLength(consensus.length());
    }

    public Map<String, String> getEntriesMapCopy() {
        Map<String, String> ret = new HashMap<String, String>();
        Iterator<Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            Entry e = itr.next();
            ret.put(e.getName(), e.getData());
        }
        return ret;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public List<Entry> getSortedEntries() {
        List<Entry> ret = new ArrayList<Entry>();
        ret.addAll(entries);
        Collections.sort(ret);
        return ret;
    }

    public int getEntriesCount() {
        return entries.size();
    }

    public StringList getEntriesNames() {
        StringList ret = new StringList();
        Iterator<Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            ret.add(entry.getName());
        }
        return ret;
    }
    
    public void setEntries(Map<String, String> entryMap){
        Iterator<String> itr = entryMap.keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            String data = entryMap.get(key);
            Entry entry = new Entry(key, data);
            this.entries.add(entry);
        }
    }

    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Newick getNewick() {
        if (newickStr != null && !newickStr.isEmpty()) {
            if(newick == null){
                newick = new Newick(newickStr);
            }
        }else{
            newick = null;
        }
        return newick;
    }

    public void setNewick(Newick newick) {
        this.newick = newick;
        updateNewickStr();
    }
    
    public void updateNewickStr(){
        if(newickStr != null && !newickStr.isEmpty()){
            newickDirty = true;
        }
        newickStr = newick.toString();
    }

    /**
     * for hibernate use only
     */
    public String getNewickStr() {
        return newickStr;
    }

    public boolean isNewickDirty() {
        return newickDirty;
    }
    
    /**
     * for hibernate use only
     */
    protected void setNewickStr(String newickStr) {
        this.newickStr = newickStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }        

    public Fasta toFasta() {
        Fasta ret = new Fasta();
        ret.setRecords(getEntriesMapCopy());
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(" ");
        ret.append('\t');
        ret.append(getConsensus());
        ret.append('\n');
        EntryList entryList = new EntryList();
        entryList.addAll(getEntries());
        Collections.sort(entryList);
        Iterator<Entry> itr = entryList.iterator();
        while (itr.hasNext()) {
            Entry entry = itr.next();
            ret.append(entry.getName());
            ret.append('\t');
            ret.append(entry.getData());
            if (itr.hasNext()) {
                ret.append('\n');
            }
        }
        return ret.toString();
    }

    public void setEntries(Aln aln) {
        Map<String, String> e = aln.getEntries();
        Iterator<String> itr = e.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String value = e.get(key);
            MSA.Entry entry = new MSA.Entry();
            entry.setName(key);
            entry.setData(value);
            entries.add(entry);
        }
        if (aln.getConservation() != null && !aln.getConservation().isEmpty()) {
            setConservation(aln.getConservation());
        }
    }

    public void setConservation(String str) {
        if (richConsensus == null) {
            setRichConsensus(new RichConsensus());
        }
        richConsensus.setConservation(str);
    }

    public String getConservation() {
        if (richConsensus == null) {
            setRichConsensus(new RichConsensus());
        }
        return richConsensus.getConservation();
    }

    public void setEntries(Fasta fasta) {
        Iterator<Fasta.Record> itr = fasta.getRecords().iterator();
        while (itr.hasNext()) {
            Fasta.Record r = itr.next();
            MSA.Entry e = new MSA.Entry();
            e.setName(r.getDefinitionLine().getName());
            e.setData(r.getSequence());
            entries.add(e);
        }
    }
    
    public void addEntry(String name, String data){
        Entry entry = new Entry();
        entry.setName(name);
        entry.setData(data);
        entries.add(entry);
    }

    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    public static class EntryList extends ArrayList<Entry> {

        /*
         * 1-based,
         */
        public void delete(int rowStart, int columnStart, int rowEnd, int columnEnd) {
            boolean maintainLength = rowEnd - rowStart + 1 != size();

            for (int row = rowStart; row <= rowEnd; row++) {
                Entry entry = get(row - 1);
                entry.delete(columnStart, columnEnd, maintainLength);
            }
        }

        public void replace(int rowStart, int columnStart, int rowEnd, int columnEnd, String replacement) {
            if (rowEnd - rowStart + 1 == size()) {
                Iterator<Entry> itr = iterator();
                while (itr.hasNext()) {
                    Entry entry = itr.next();
                    entry.replace(columnStart, columnEnd - columnStart + 1, replacement);
                }
            } else {
                int lengthDelta = replacement.length() - (columnEnd - columnStart + 1);
                final String GAPs = StrUtil.createString("-", Math.abs(lengthDelta));
                for (int row = 1; row <= size(); row++) {
                    Entry entry = get(row - 1);
                    if (row >= rowStart && row <= rowEnd) {
                        entry.replace(columnStart, columnEnd - columnStart + 1, replacement);
                        if (lengthDelta < 0) {
                            entry.setData(entry.getData() + GAPs);
                        }
                    } else {
                        if (lengthDelta > 0) {
                            entry.setData(entry.getData() + GAPs);
                        }
                    }
                }
            }
        }

        /*
         * 1-based. The first row is the first entry
         */
        public void insert(int rowStart, int rowEnd, int column, String str) {
            String gaps = StrUtil.createString("-", str.length());
            for (int row = 1; row <= size(); row++) {
                Entry entry = get(row - 1);
                if (row >= rowStart && row <= rowEnd) {
                    entry.insert(column, str);
                } else {
                    entry.insert(column, gaps);
                }
            }
        }
    }

    public static class Entry implements Comparable<Entry>, Cloneable {

        private Integer hibernateId;
        private String name;
        private String data;
        private Set<Feture> fetures = new HashSet<Feture>();

        public Entry(){}
        
        public Entry(String name, String data){
            this.name = name;
            this.data = data;
        }
        
        @Override
        public Entry clone() {
            Entry ret = CommonUtil.cloneSimple(this);
            return ret;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        protected void delete(int startPos, int endPos) {
            delete(startPos, endPos, false);
        }

        protected void delete(int startPos, int endPos, boolean maintainLength) {
            StringBuilder newData = new StringBuilder();
            newData.append(StrUtil.delete(data, startPos, endPos));
            if (maintainLength) {
                for (int i = startPos; i <= endPos; i++) {
                    newData.append('-');
                }
            }
            this.data = newData.toString();
        }

        protected void insert(int pos, String str) {
            this.data = StrUtil.insert(data, pos, str);
        }

        protected void replace(int pos, int length, String str) {
            this.data = StrUtil.replace(data, pos, pos + length - 1, str);
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Set<Feture> getFetures() {
            return fetures;
        }

        public void setFetures(Set<Feture> fetures) {
            this.fetures = fetures;
        }

        @Override
        public int compareTo(Entry o) {
            int ret = getName().compareToIgnoreCase(o.getName());
            return ret;
        }
    }

    public void setLength(Integer l) {
        this.length = l;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public List<FileFormat> getSupportedExportFormats() {
        List<FileFormat> ret = new ArrayList<FileFormat>();
        ret.add(FileFormat.FASTA);
        ret.add(FileFormat.CLUSTAL);
        if(newickStr != null && !newickStr.isEmpty()){
            ret.add(FileFormat.NEWICK);
        }
        ret.add(FileFormat.NEXUS);
        return ret;
    }

    @Override
    public String export(FileFormat format) {
        StringBuilder ret = new StringBuilder();
        if (format == FileFormat.CLUSTAL) {
            Aln aln = new Aln(getEntriesMapCopy());
            aln.setHeader("CLUSTAL");
            aln.setConservation(getConservation());
            ret.append(aln.toString());
        } else if (format == FileFormat.FASTA) {
            Fasta fasta = new Fasta(getEntriesMapCopy());
            ret.append(FastaWriter.to(fasta, String.class));
        } else if(format == FileFormat.NEXUS){
            Nexus nexus = MSAHelper.toNexus(this);
            ret.append(nexus.toString());
        } else if(format == FileFormat.NEWICK){            
            ret.append(newickStr);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret.toString();
    }
    
    
}
