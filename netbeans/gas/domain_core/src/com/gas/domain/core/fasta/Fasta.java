/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.fasta;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 *
 * @author dq
 */
public class Fasta {

    private Set<Record> records = new HashSet<Record>();

    public Fasta() {
    }

    public Fasta(Map<String, String> records) {
        setRecords(records);
    }

    public Boolean isDNAByGuess() {
        Integer length = getLength();
        if (length == null || length.intValue() == 0) {
            return null;
        }
        String seq = getNongapSeq(20);
        return BioUtil.isDNAByGuess(seq);
    }

    private String getNongapSeq(int max) {
        StringBuilder ret = new StringBuilder();
        Iterator<Record> itr = records.iterator();
        while (itr.hasNext() && ret.length() < max) {
            Record r = itr.next();
            String seq = r.getSequence();
            for (int i = 0; i < seq.length(); i++) {
                char c = seq.charAt(i);
                boolean isLetter = Character.isLetter(c);
                if (isLetter) {
                    ret.append(c);
                }
            }
        }
        return ret.toString();
    }

    public Integer getLength() {
        Integer ret = null;
        Iterator<Record> itr = records.iterator();
        while (itr.hasNext()) {
            Record r = itr.next();
            ret = r.getSequence().length();
            break;
        }
        return ret;
    }

    public int getSeqCount() {
        return records.size();
    }

    public Set<Record> getRecords() {
        return records;
    }

    public Fasta removeGaps() {
        Fasta ret = new Fasta();
        Iterator<Record> itr = records.iterator();
        while (itr.hasNext()) {
            Record r = itr.next();
            ret.add(r.getDefinitionLine().getData(), r.getSequence().replaceAll("-", ""));
        }
        return ret;
    }

    public void add(String definitionLine, String seq) {
        Iterator<Record> itr = records.iterator();
        boolean found = false;
        Record r = null;
        while (itr.hasNext()) {
            r = itr.next();
            if (r.getDefinitionLine().getData().equalsIgnoreCase(definitionLine)) {
                found = true;
                break;
            }
        }

        if (found) {
            String old = r.getSequence();
            String newS = old + seq;
            r.setSequence(newS);
        } else {
            Record record = new Record();
            record.setDefinitionLine(definitionLine);
            record.setSequence(seq);
            records.add(record);
        }
    }

    public final void setRecords(Map<String, String> recordsMap) {
        Set<Record> _records = new HashSet<Record>();

        Iterator<String> itr = recordsMap.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String data = recordsMap.get(key);
            Record r = new Record();
            r.setDefinitionLine(key);
            r.setSequence(data);

            _records.add(r);
        }
        setRecords(_records);
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }

    public String toString(int perLine) {
        return FastaWriter.to(this, perLine, String.class);
    }

    @Override
    public String toString() {
        return toString(60);
    }

    public ByteArrayOutputStream toOutputStram(int perLine) {
        return FastaWriter.to(this, perLine, ByteArrayOutputStream.class);
    }

    public Map<String, String> toMap() {
        Map<String, String> ret = new HashMap<String, String>();
        Iterator<Record> itr = records.iterator();
        while (itr.hasNext()) {
            Record r = itr.next();
            ret.put(r.getDefinitionLine().getData(), r.getSequence());
        }
        return ret;
    }

    public byte[] toByteArray(int perLine) {
        return FastaWriter.to(this, perLine, byte[].class);
    }
    
    public static class DefinitionLine {
        private static final String GB_TAG = "gb";
        private static final String EMBL_TAG = "embl";
        private static final String DDBJ_TAG = "DDBJ";
        private static final String NBRF_PIR_TAG = "pir";
        private static final String PRF_TAG = "prf"; // protein research foundation
        private static final String SWISS_PROT_TAG = "sp";
        private static final String UniProtKB_TrEMBL_TAG = "tr";
        private static final String PDB_TAG = "pdb";
        private static final String PATENTS_TAG = "pat";
        private static final String BBS_TAG = "bbs"; //GenInfo Backbone Id
        private static final String GNI_TAG = "gnl"; // gnl
        private static final String REF_TAG = "ref"; // NCBI Reference Sequence
        private static final String LCL_TAG = "lcl"; // Local Sequence identifier
        
        private static final String[] ALL_TAGs = {
            GB_TAG, EMBL_TAG, DDBJ_TAG, NBRF_PIR_TAG, PRF_TAG, SWISS_PROT_TAG, UniProtKB_TrEMBL_TAG, PDB_TAG, PATENTS_TAG, BBS_TAG,
            GNI_TAG, REF_TAG, LCL_TAG
        };
        
        static {
            for(int i = 0; i < ALL_TAGs.length; i++){
                ALL_TAGs[i] = ALL_TAGs[i].toLowerCase(Locale.ENGLISH);
            }
            Arrays.sort(ALL_TAGs);
        }
        
        private String data;

        public DefinitionLine(String data){
            this.data = data;
        }
        
        public String getName(){
            String ret = this.data;            
            List<String> splits = StrUtil.tokenize(this.data, "|");
            if(splits.size() > 1){
                String tag = splits.get(0);
                if(Arrays.binarySearch(ALL_TAGs, tag.toLowerCase(Locale.ENGLISH)) > -1){
                    ret = tag + " "+ splits.get(1);
                }
            }
            return ret;
        }
        
        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
        
    }

    public static class Record {
        private DefinitionLine definitionLine;
        private String sequence;

        
        
        public DefinitionLine getDefinitionLine() {
            return definitionLine;
        }
        
        public String getName(){
            
            return null;
        }
        
        public void setDefinitionLine(String data){
            if(definitionLine == null){
                this.definitionLine = new DefinitionLine(data);
            }else{
                this.definitionLine.setData(data);
            }
        }

        public void setDefinitionLine(DefinitionLine definitionLine) {
            this.definitionLine = definitionLine;
        }
        
        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }
}
