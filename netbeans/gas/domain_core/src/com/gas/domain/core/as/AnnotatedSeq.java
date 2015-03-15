package com.gas.domain.core.as;

import com.gas.common.ui.FileFormat;
import com.gas.common.ui.core.CharList;
import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.IExportable;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.core.primer3.GbOutput;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.ren.RMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class AnnotatedSeq implements Cloneable, IFolderElement, IExportable {

    public static final CharList forbiddenChars = new CharList('/', '\\');
    private Set<StructuredComment> structuredComments = new HashSet<StructuredComment>();
    
    public enum ELEMENT {
        ACCESSION, COMMENT, DB_REF, DESC, FEATURE, FOLDER, GC, TRANSLATION, ORF, OVERHANG, P3OUTPUT, REF, RMAP, SEQ,
    };
    public static ELEMENT[] NO_ANALYSIS = {ELEMENT.ACCESSION, ELEMENT.COMMENT, ELEMENT.DB_REF, ELEMENT.DESC, ELEMENT.FEATURE, ELEMENT.FOLDER, ELEMENT.OVERHANG, ELEMENT.REF, ELEMENT.SEQ};
    private String hibernateId; // for hibernate use    
    private String accession;
    private int taxonId;
    private String taxonName;
    private String desc;
    private String division;
    private Integer length;
    private String locus;
    private boolean original;
    private Boolean circular;
    private Date creationDate;
    private Date lastModifiedDate;
    private Siquence siquence = new Siquence();
    private FetureSet fetureSet = new FetureSet();
    private Folder folder;
    private String prevFolderPath;
    private Set<String> _keywords = new LinkedHashSet<String>();
    private Map<String, String> _sequenceProperties = new HashMap<String, String>();
    private AsPref asPref = new AsPref();
    private Set<Comment> comments = new HashSet<Comment>();
    private Set<Dbref> dbrefs = new HashSet<Dbref>();
    private Set<GCResult> gcResults = new HashSet<GCResult>();
    private Set<ORFResult> orfResults = new HashSet<ORFResult>();
    private Set<Overhang> overhangs = new HashSet<Overhang>();
    private Set<P3Output> p3outputs = new HashSet<P3Output>();
    private Set<GbOutput> gbOutputs = new HashSet<GbOutput>();
    private Set<Operation> operations = new HashSet<Operation>();
    private Set<Reference> references = new LinkedHashSet<Reference>();
    private Set<RMap> rmaps = new HashSet<RMap>();
    private Set<TranslationResult> translationResults = new HashSet<TranslationResult>();
    private ParentLocSet parentLocSet = new ParentLocSet();
    private boolean read;
    private boolean oligo;

    public AsPref getAsPref() {
        return asPref;
    }

    public void setAsPref(AsPref asPref) {
        this.asPref = asPref;
    }

    @Override
    public List<FileFormat> getSupportedExportFormats() {
        List<FileFormat> ret = new ArrayList<FileFormat>();
        if (this.isNucleotide()) {
            ret.add(FileFormat.GenBank);
        } else {
            ret.add(FileFormat.GenPept);
        }
        ret.add(FileFormat.FASTA);
        return ret;
    }

    protected Set<GbOutput> getGbOutputs() {
        return gbOutputs;
    }

    protected void setGbOutputs(Set<GbOutput> gbOutputs) {
        this.gbOutputs = gbOutputs;
    }
    
    public void setGbOutput(GbOutput gbOutput){
        gbOutputs.clear();
        gbOutputs.add(gbOutput);
    }
    
    public GbOutput getGbOutput(){
        if(gbOutputs.isEmpty()){
            return null;
        }else{
            GbOutput ret = gbOutputs.iterator().next();
            return ret;
        }
    }
    
    public void setOperation(Operation operation) {
        operations.clear();
        if (operation != null) {
            operations.add(operation);
        }
    }

    public Set<StructuredComment> getStructuredComments() {
        return structuredComments;
    }

    public void setStructuredComments(Set<StructuredComment> structuredComments) {
        this.structuredComments = structuredComments;
    }
    
    public Operation getOperation() {
        if (operations.isEmpty()) {
            return null;
        } else {
            return operations.iterator().next();
        }
    }

    /**
     * for hibernate use only
     */
    protected Set<Operation> getOperations() {
        return operations;
    }

    /**
     * for hibernate use only
     */
    protected void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }
    
    public ParentLocSet getParentLocSet() {
        return parentLocSet;
    }

    public void setParentLocSet(ParentLocSet parentLocSet) {
        this.parentLocSet = parentLocSet;
    }

    @Override
    public String export(FileFormat format) {
        String ret = null;
        if (format == FileFormat.GenBank || format == FileFormat.GenPept) {
            ret = AnnotatedSeqWriter.toString(this);
        } else if (format == FileFormat.FASTA) {
            Fasta fasta = new Fasta();
            fasta.add(this.getName(), this.getSiquence().getData());
            ret = FastaWriter.to(fasta, String.class);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }
    /*
     * @ret the sequence 5'->3' direction
     */

    public String getOverhangSeq(Overhang overhang) {
        return AsHelper.getOverhangSeq(this, overhang);
    }

    /**
     * For hibernate use only
     */
    protected Set<P3Output> getP3outputs() {
        return p3outputs;
    }

    /**
     * For hibernate use only
     */
    protected void setP3outputs(Set<P3Output> p3outputs) {
        this.p3outputs = p3outputs;
    }

    public P3Output getP3output() {
        P3Output ret = null;
        if (!p3outputs.isEmpty()) {
            ret = p3outputs.iterator().next();
        }
        return ret;
    }

    public void setP3output(P3Output p3output) {
        if (p3output != null) {
            p3outputs.clear();
            p3outputs.add(p3output);
        }
    }

    public boolean isOligo() {
        return oligo;
    }

    public void setOligo(boolean oligo) {
        this.oligo = oligo;
    }

    public boolean ligatable(AnnotatedSeq another) {
        return AsHelper.ligatable(this, another);
    }

    @Override
    public boolean isRead() {
        return read;
    }

    protected Set<GCResult> getGcResults() {
        return gcResults;
    }

    protected void setGcResults(Set<GCResult> gcResults) {
        this.gcResults = gcResults;
    }

    public GCResult getGcResult() {
        if (gcResults.isEmpty()) {
            return null;
        } else {
            return gcResults.iterator().next();
        }
    }

    /**
     * @param gcResult null allowed, that will empty the GcResults
     */
    public void setGcResult(GCResult gcResult) {
        gcResults.clear();
        if (gcResult != null) {
            gcResults.add(gcResult);
        }
    }

    /**
     * for hibernate use only
     */
    protected Set<ORFResult> getOrfResults() {
        return orfResults;
    }

    /**
     * for hibernate use only
     */
    protected void setOrfResults(Set<ORFResult> orfResults) {
        this.orfResults = orfResults;
    }

    public ORFResult getOrfResult() {
        if (orfResults.isEmpty()) {
            return null;
        } else {
            return orfResults.iterator().next();
        }
    }

    public void setOrfResult(ORFResult orfResult) {
        orfResults.clear();
        orfResults.add(orfResult);
    }

    public void delete(int start, int end) {
        AsHelper.removeSeq(this, start, end, true);
    }

    public void insertSeq(Integer pos, String bases) {
        AsHelper.insertAs(this, pos, bases);
    }

    public void removeSeq(int start, int end) {
        AsHelper.removeSeq(this, start, end, true);
    }

    public AnnotatedSeq subAs(int start, int end) {
        return AsHelper.subAs(this, start, end);
    }

    public boolean isNucleotide() {
        return AsHelper.isNucleotide(this);
    }

    public boolean isDNA() {
        return AsHelper.isDNA(this);
    }

    public boolean isRNA() {
        return AsHelper.isRNA(this);
    }

    public boolean isProtein() {
        return AsHelper.isAminoAcid(this);
    }

    public AnnotatedSeq flip() {
        return AsHelper.flip(this);
    }

    public Overhang getStartOverhang() {
        return AsHelper.getStartOverhang(this);
    }

    public void remove5Overhang() {
        AsHelper.remove5Overhang(this);
    }

    public void remove3Overhang() {
        AsHelper.remove3Overhang(this);
    }

    public Overhang removeStartOverhang() {
        return AsHelper.removeStartOverhang(this);
    }

    public Overhang removeEndOverhang() {
        Overhang ret = null;
        Iterator<Overhang> itr = overhangs.iterator();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isEndOverhang()) {
                ret = overhang;
                itr.remove();
            }
        }
        return ret;
    }

    public void replace(int startPos, int endPos, String replacement) {
        AsHelper.replace(this, startPos, endPos, replacement);
    }

    public Overhang getEndOverhang() {
        return AsHelper.getEndOverhang(this);
    }

    public List<Overhang> get5Overhang() {
        List<Overhang> ret = new ArrayList();
        Iterator<Overhang> itr = overhangs.iterator();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isFivePrime()) {
                ret.add(overhang);
            }
        }
        return ret;
    }

    public List<Overhang> get3Overhang() {
        List<Overhang> ret = new ArrayList();
        Iterator<Overhang> itr = overhangs.iterator();
        while (itr.hasNext()) {
            Overhang overhang = itr.next();
            if (overhang.isThreePrime()) {
                ret.add(overhang);
            }
        }
        return ret;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String getPrevFolderPath() {
        return prevFolderPath;
    }

    @Override
    public void setPrevFolderPath(String prevFolderPath) {
        this.prevFolderPath = prevFolderPath;
    }

    public Set<Overhang> getOverhangs() {
        return overhangs;
    }

    public int getOverhangSize() {
        return overhangs.size();
    }

    public Iterator<Overhang> getOverhangItr() {
        return overhangs.iterator();
    }

    public Set<Overhang> getReadOnlyOverhangs() {
        return Collections.unmodifiableSet(getOverhangs());
    }

    public void setStartOverhang(Overhang overhang) {
        removeStartOverhang();
        _addOverhang(overhang);
    }

    public void setEndOverhang(Overhang overhang) {
        removeEndOverhang();
        _addOverhang(overhang);
    }

    public void addOverhang(Overhang overhang) {
        _addOverhang(overhang);
    }

    private void _addOverhang(Overhang overhang) {
        if (getOverhangs().size() > 1) {
            throw new IllegalArgumentException("Cannot have more than 2 overhangs");
        }
        overhangs.add(overhang);
    }

    public void clearOverhangs() {
        overhangs.clear();
    }

    protected void setOverhangs(Set<Overhang> overhangs) {
        this.overhangs = overhangs;
    }

    public RMap getRmap() {
        RMap ret = null;
        if (!rmaps.isEmpty()) {
            ret = rmaps.iterator().next();
        }
        return ret;
    }

    public void setRmap(RMap rmap) {
        rmaps.clear();
        rmaps.add(rmap);
    }

    /**
     * For hibernate use only
     */
    protected Set<RMap> getRmaps() {
        return rmaps;
    }

    /**
     * For hibernate use only
     */
    protected void setRmaps(Set<RMap> rmaps) {
        this.rmaps = rmaps;
    }

    public Boolean isCircular() {
        return circular;
    }

    public void setCircular(Boolean circular) {
        this.circular = circular;
    }

    public Set<TranslationResult> getTranslationResults() {
        return translationResults;
    }

    public void setTranslationResults(Set<TranslationResult> translationResults) {
        this.translationResults = translationResults;
    }

    public AnnotatedSeq clone(ELEMENT... elements) {
        Arrays.sort(elements);
        AnnotatedSeq ret = clone();
        if (Arrays.binarySearch(elements, ELEMENT.ACCESSION) < 0) {
            ret.setAccession("");
        }
        if (Arrays.binarySearch(elements, ELEMENT.COMMENT) < 0) {
            ret.getComments().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.DB_REF) < 0) {
            ret.getDbrefs().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.DESC) < 0) {
            ret.setDesc("");
        }
        if (Arrays.binarySearch(elements, ELEMENT.FEATURE) < 0) {
            ret.getFetureSet().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.FOLDER) < 0) {
            ret.setFolder(null);
            ret.setPrevFolderPath("");
        }
        if (Arrays.binarySearch(elements, ELEMENT.GC) < 0) {
            ret.getGcResults().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.TRANSLATION) < 0){
            ret.getTranslationResults().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.ORF) < 0) {
            ret.getOrfResults().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.OVERHANG) < 0) {
            ret.getOverhangs().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.P3OUTPUT) < 0) {
            ret.getP3outputs().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.REF) < 0) {
            ret.getReferences().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.RMAP) < 0) {
            ret.getRmaps().clear();
        }
        if (Arrays.binarySearch(elements, ELEMENT.SEQ) < 0) {
            ret.setSequence("");
        }

        return ret;
    }

    @Override
    public AnnotatedSeq clone() {
        AnnotatedSeq ret = CommonUtil.cloneSimple(this);
        ret.setAsPref(asPref.clone());
        ret.setComments(CommonUtil.copyOf(comments));
        ret.setDbrefs(CommonUtil.copyOf(dbrefs));
        ret.setFetureSet(fetureSet.clone());
        ret.setFolder(folder);
        if (gcResults != null) {
            ret.setGcResults(CommonUtil.copyOf(gcResults));
        }
        if(!gbOutputs.isEmpty()){
            ret.setGbOutputs(CommonUtil.copyOf(gbOutputs));
        }
        ret.setKeywords(CommonUtil.copyOf(_keywords));
        ret.setOperations(CommonUtil.copyOf(operations));
        ret.setOrfResults(CommonUtil.copyOf(orfResults));
        ret.setOverhangs(CommonUtil.copyOf(overhangs));
        ret.setP3outputs(CommonUtil.copyOf(p3outputs));
        ret.setParentLocSet(parentLocSet.clone());
        if (getRmap() != null) {
            ret.setRmap(getRmap().clone());
        }

        ret.setReferences(CommonUtil.copyOf(references));
        ret.setSiquence(siquence.clone());
        ret.set_keywords(CommonUtil.copyOf(_keywords));
        ret.set_sequenceProperties(CommonUtil.copyOf(_sequenceProperties));
        ret.setStructuredComments(CommonUtil.copyOf(structuredComments));
        ret.setTranslationResults(CommonUtil.copyOf(translationResults));
        return ret;
    }

    public boolean getOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public int getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(int taxonId) {
        this.taxonId = taxonId;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
    }

    protected Set<Comment> getComments() {
        return comments;
    }

    protected void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
    
    public Comment getComment(){
        if(comments.isEmpty()){
            return null;
        }else{
            return comments.iterator().next();
        }
    }
    
    public void setComment(String str){
        com.gas.domain.core.as.Comment comment = new com.gas.domain.core.as.Comment();
        comment.setData(str);
        setComment(comment);
    }
    
    public void setComment(Comment comment){
        this.comments.clear();
        this.comments.add(comment);
    }

    public Siquence getSiquence() {
        return siquence;
    }

    protected void setSiquence(Siquence siquence) {
        this.siquence = siquence;
    }

    public void setSequence(String data) {
        this.siquence.setData(data);
        setLength(data.length());
    }

    public void appendData(String d) {
        if (siquence.getData() == null) {
            setSequence(d);
        } else {
            String newData = siquence.getData() + d;
            setSequence(newData);
        }
    }

    public void addFrontData(String d) {
        if (siquence.getData() == null) {
            setSequence(d);
        } else {
            String newData = d + siquence.getData();
            setSequence(newData);
        }
        getFetureSet().translate(d.length(), isCircular(), getLength());
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FetureSet getFetureSet() {
        return fetureSet;
    }

    public void setFetureSet(FetureSet fetureSet) {
        this.fetureSet = fetureSet;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Map<String, String> getSequenceProperties() {
        return _sequenceProperties;
    }

    public Map<String, String> get_sequenceProperties() {
        return _sequenceProperties;
    }

    protected void set_sequenceProperties(Map<String, String> sequenceProperties) {
        this._sequenceProperties = sequenceProperties;
    }

    public void setSequenceProperties(Map<String, String> sequenceProperties) {
        this._sequenceProperties = sequenceProperties;
    }

    @Override
    public String getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public String getName() {
        StructuredComment sc = getVfData();
        if(!sc.getData().containsKey("name")){
            if(locus != null){
                sc.getData().put("name", locus);
            }
        }        
        return sc.getData().get("name");
    }

    @Override
    public void setName(String name) {
        StructuredComment sc = getVfData();
        sc.getData().put("name", name);
    }
    
    private StructuredComment getVfData(){
        StructuredComment ret = null;
        for(StructuredComment sc: structuredComments){
            String _name = sc.getName();
            if(_name != null && _name.equalsIgnoreCase("VfData")){
                ret = sc;
                break;
            }
        }
        if(ret == null){
            ret = new StructuredComment();
            ret.setName("VfData");
            structuredComments.add(ret);
        }
        return ret;
    }

    public Set<Reference> getReferences() {
        return references;
    }

    /*
     * Convenience method
     */
    public Set<Reference> getSortedReference(Comparator<Reference> c) {
        Set<Reference> ret = new TreeSet<Reference>(c);
        ret.addAll(references);
        return ret;
    }

    public void setReferences(Set<Reference> references) {
        this.references = references;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Dbref> getDbrefs() {
        return dbrefs;
    }

    public void setDbrefs(Set<Dbref> dbrefs) {
        this.dbrefs = dbrefs;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Set<String> getKeywords() {
        return _keywords;
    }

    /*
     * For hibernate use only
     */
    public Set<String> get_keywords() {
        return _keywords;
    }
    /*
     * For hibernate use only
     */

    protected void set_keywords(Set<String> keywords) {
        this._keywords = keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this._keywords = keywords;
    }

    @Override
    public String toString() {
        return getName();
    }
}
