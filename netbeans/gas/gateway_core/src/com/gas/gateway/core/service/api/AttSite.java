/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.ReflectHelper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class AttSite implements Cloneable {

    /*
     * 5' ACAAGTTT GTACAAAAAAGCAGGCT 3' 12345678 90123456789012345
     */
    public static final AttSite attB1 = new AttSite("attB1", "ACAAGTTTGTACAAAAAAGCAGGCT");
    public static final AttSite attB1r = new AttSite("attB1r", "CAAGTTTGTACAAAAAAGCAGT");
    /*
     * 5' ACCACTTT GTACAAGAAAGCTGGGT 3' 12345678 9
     *
     */
    public static final AttSite attB2 = new AttSite("attB2", "ACCACTTTGTACAAGAAAGCTGGGT");
    public static final AttSite attB2r = new AttSite("attB2r", "CCACTTTGTACAAGAAAGCTGT");
    /*
     * 5' ACAACTTT GTATAATAAAGTT G 3' 12345678 9
     */
    public static final AttSite attB3 = new AttSite("attB3", "ACAACTTTGTATAATAAAGTTG");
    /*
     * 5' CAACTTTGTATAAT AAAGTTGT 3' 12345678901234 567890
     */
    public static final AttSite attB3r = new AttSite("attB3r", "CAACTTTGTATAATAAAGTTGT");
    /*
     * 5' CAACTTTGTATAG AAAAGTTGT 3' 1234567890123 4567890
     */
    public static final AttSite attB4r = new AttSite("attB4r", "CAACTTTGTATAGAAAAGTTGT");
    /*
     * 5' ACAACTTT GTATAGAAAAGTTGGGT 3' 12345678 901234
     */
    public static final AttSite attB4 = new AttSite("attB4", "ACAACTTTGTATAGAAAAGTTGGGT");
    public static final AttSite attB4_3FRAG = new AttSite("attB4", "ACAACTTTGTATAGAAAAGTTG");
    public static final AttSite attB5 = new AttSite("attB5", "ACAACTTTGTATACAAAAGTTG");
    public static final AttSite attB5r = new AttSite("attB5r", "CAACTTTGTATACAAAAGTTGT");
    public static final AttSite attP1_pDNOR221 = new AttSite("attP1",
            "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACACATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP1_pDNOR201_pDNOR207 = new AttSite("attP1",
            "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAAGTTTGTACAAAAAAGCAGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP1_pro = new AttSite("attP1",
            "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP1r = new AttSite("attP1r", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATGCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP2_pDNOR201_pDNOR207 = new AttSite("attP2", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCCACTTTGTACAAGAAAGCTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP2_pDONR221 = new AttSite("attP2", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCAACTTTGTACAAGAAAGCTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP2_3FRAG = new AttSite("attP2", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTACAAGAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATGCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP3 = new AttSite("attP3", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTATAATAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP3_3FRAG = new AttSite("attP3", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTATAATAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATGCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP4 = new AttSite("attP4", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCAACTTTGTATAGAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP4_3FRAG = new AttSite("attP4",
            "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTATAGAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATGCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attP5 = new AttSite("attP5", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTATACAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    // attL site                                                                                        
    public static final AttSite attL1_221 = new AttSite("attL1", "CAAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACACATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAGGCT");
    public static final AttSite attL1_pro = new AttSite("attL1", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAG");
    public static final AttSite attL1_DUAL_SELECTION = new AttSite("attL1",
            "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTACAAAAAAGCAG");
    public static final AttSite attL2 = new AttSite("attL2", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCAACTTTGTACAAGAAAGCTG");
    //public static final AttSite attL2_3FRAG = new AttSite("attL2", "CAAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCAACTTTGTACAAGAAAGCTGGGT");
    public static final AttSite attL2_pCR8_GW_TOPO = new AttSite("attL2", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATTATTTTTTATAATGCCAACTTTGTACAAGAAAGCTG");
    public static final AttSite attL3 = new AttSite("attL3", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTATAATAAAGTTG");
    public static final AttSite attL3_3FRAG = new AttSite("attL3", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTATAATAAAGTTG");
    public static final AttSite attL4 = new AttSite("attL4", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTCTTATAATGCCAACTTTGTATAGAAAAGTTG");
    public static final AttSite attL4_3FRAG = new AttSite("attL4", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATAAGCAATGCTTTTTTATAATGCCAACTTTGTATAGAAAAGTTG");
    public static final AttSite attL5 = new AttSite("attL5", "AAATAATGATTTTATTTTGACTGATAGTGACCTGTTCGTTGCAACAAATTGATGAGCAATGCTTTTTTATAATGCCAACTTTGTATACAAAAGTTG");
    // attR site    
    
    public static final AttSite attR1 = new AttSite("attR1", "ACAAGTTTGTACAAAAAAGCTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATG");
    public static final AttSite attR1_3FRAG = new AttSite("attR1", "CAAGTTTGTACAAAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATGCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attR2 = new AttSite("attR2", "ACCACTTTGTACAAGAAAGCTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATG");
    public static final AttSite attR2_3FRAG = new AttSite("attR2", "CCACTTTGTACAAGAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATGCAGTCACTATGAATCAACTACTTAGATGGTATTAGTGACCTGTA");
    public static final AttSite attR3_pro = new AttSite("attR3", "CAACTTTGTATAATAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATG");
    public static final AttSite attR3_3FRAG = new AttSite("attR3", "CAACTATGTATAATAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATGG");
    public static final AttSite attR4_pro = new AttSite("attR4", "CAACTTTGTATAGAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATG");
    public static final AttSite attR5 = new AttSite("attR5", "CAACTTTGTATACAAAAGTTGAACGAGAAACGTAAAATGATATAAATATCAATATATTAAATTAGATTTTGCATAAAAAACAGACTACATAATACTGTAAAACACAACATATCCAGTCACTATG");

    static {
        List<Field> fields = ReflectHelper.getDeclaredFields(AttSite.class, AttSite.class, Boolean.TRUE);
        for (Field fd : fields) {
            String name = fd.getName();
            int index = name.indexOf("_");
            AttSite site = ReflectHelper.getStaticQuietly(fd, AttSite.class);
            if (index > -1) {
                site.setVariation(name.substring(index));
            }
        }
    }
    private String baseName;
    private String variation = "";
    private String seq;
    private AttSite partner;
    private Loc loc;
    private boolean toInsert = true;
    private boolean reverseComp = false;

    public AttSite(AttSite site, Loc loc) {
        this(site.getBaseName(), site.getSeq());
        this.setVariation(site.getVariation());
        this.loc = loc;
    }

    private AttSite(String name, String seq) {
        this.baseName = name;
        this.seq = seq;
    }

    public AttSite() {
    }

    public boolean isReverseComp() {
        return reverseComp;
    }

    public void setReverseComp(boolean reverseComp) {
        this.reverseComp = reverseComp;
    }

    @Override
    public AttSite clone() {
        AttSite ret = CommonUtil.cloneSimple(this);        
        if (loc != null) {
            ret.setLoc(loc.clone());
        }       
        return ret;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public AttSite flip() {
        String newSeqs = null;

        String rc = BioUtil.reverseComplement(seq);
        newSeqs = rc;

        AttSite ret = new AttSite(getBaseName(), newSeqs);
        ret.setReverseComp(!ret.isReverseComp());
        return ret;
    }

    public boolean isToInsert() {
        return toInsert;
    }

    public void setToInsert(boolean toInsert) {
        this.toInsert = toInsert;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public int length() {
        return seq.length();
    }

    public Loc getLoc() {
        return loc;
    }

    public boolean recombinesWith(AttSite attSite) {
        boolean ret = true;
        String clazz = getPartnerClazz().toString();
        String clazz2 = attSite.getClazz().toString();
        if (!clazz.equalsIgnoreCase(clazz2)) {
            ret = false;
            return ret;
        }

        Character subclazz = getSubclazz();
        Character subclazz2 = attSite.getSubclazz();
        if (subclazz.equals(subclazz2)) {
            ret = false;
            return ret;
        }
        return ret;
    }

    public String getPartnerName() {
        StringBuilder ret = new StringBuilder();
        ret.append("att");
        Character clazz = getPartnerClazz();
        ret.append(clazz);
        Character subclazz = getSubclazz();
        ret.append(subclazz);
        if (!isToInsert()) {
            ret.append("r");
        }
        return ret.toString();
    }

    private AttSite getPartner() {
        if (partner == null) {
            String subClazz = getSubclazz().toString();
            String clazz = getPartnerClazz().toString();
            partner = getAttSite(String.format("att%s%s", clazz, subClazz));
        }
        return partner;
    }

    /**
     * @return 'B', 'P', 'L', 'R'
     */
    public Character getClazz() {
        Character ret = baseName.charAt(3);
        return ret;
    }

    /*
     * @ret 1, 2, 3, 4, 5(attB1)
     */
    public Character getSubclazz() {
        return baseName.charAt(4);
    }

    private Character getPartnerClazz() {
        final String clazz = getClazz().toString();
        Character ret = null;
        if (clazz.equalsIgnoreCase("B")) {
            ret = 'P';
        } else if (clazz.equalsIgnoreCase("P")) {
            ret = 'B';
        } else if (clazz.equalsIgnoreCase("L")) {
            ret = 'R';
        } else if (clazz.equalsIgnoreCase("R")) {
            ret = 'L';
        }
        return ret;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getName() {
        StringBuilder ret = new StringBuilder();
        ret.append(baseName);
        if (!toInsert) {
            if (!baseName.endsWith("r") && !baseName.endsWith("R")) {
                ret.append('r');
            }
        }
        return ret.toString();
    }
    
    /**
     * @return 'B1', 'B1r', 'L1', "R1'
     */
    public String getShortName(){
        StringBuilder ret = new StringBuilder();
        String clazz = getClazz().toString().toUpperCase(Locale.ENGLISH);
        Character subClazz = getSubclazz();
        ret.append(clazz);
        ret.append(subClazz);
        if(clazz.equalsIgnoreCase("P") || clazz.equalsIgnoreCase("B")){
            if(!toInsert){
                ret.append('r');
            }
        }
        return ret.toString();
    }

    public String getSeq() {
        return seq;
    }

    public static List<AttSite> getAllSites() {
        List<AttSite> ret = new ArrayList<AttSite>();
        ret.addAll(getAllAttBSites());
        ret.addAll(getAllAttPSites());
        ret.addAll(getAllAttLSites());
        ret.addAll(getAllAttRSites());
        return ret;
    }

    public static AttSite getAttSite(String name) {
        AttSite ret = null;
        List<AttSite> sites = getAllSites();
        for (AttSite site : sites) {
            if (site.getName().equalsIgnoreCase(name)) {
                ret = site;
                break;
            }
        }
        return ret;
    }

    private static List<AttSite> getAllAttLSites() {
        List<AttSite> ret = getAttSites("attL");
        return ret;
    }

    private static List<AttSite> getAllAttRSites() {
        List<AttSite> ret = getAttSites("attR");
        return ret;
    }

    private static List<AttSite> getAttSites(String str) {
        List<AttSite> ret = new ArrayList<AttSite>();
        List<Field> fields = ReflectHelper.getDeclaredFields(AttSite.class, AttSite.class, Boolean.TRUE);
        for (Field field : fields) {
            String name = field.getName();
            Class clazz = field.getDeclaringClass();
            if (name.startsWith(str)) {
                AttSite site = ReflectHelper.getStaticQuietly(field, AttSite.class);
                ret.add(site);
            }
        }
        return ret;
    }

    private static List<AttSite> getAllAttBSites() {
        List<AttSite> ret = getAttSites("attB");
        return ret;
    }

    private static List<AttSite> getAllAttPSites() {
        List<AttSite> ret = getAttSites("attP");
        return ret;
    }

    public String getResultantName(AttSite site, AttSite site2) {
        List<AttSite> aaa = new ArrayList<AttSite>();
        StringBuilder ret = new StringBuilder();
        Character clazz = getClazz();
        if (clazz.equals('B') || clazz.equals('P')) {
            if (toInsert) {
                ret.append('L');
            } else {
            }
        } else if (clazz.equals('L') || clazz.equals('R')) {
        }
        return ret.toString();
    }

    public static class LocSorter implements Comparator<AttSite> {

        private boolean reversed = false;
                
        public LocSorter(){
            this(false);
        }
        
        public LocSorter(boolean reversed){
            this.reversed = reversed;
        }
        
        @Override
        public int compare(AttSite o1, AttSite o2) {
            int ret = 0;
            Loc loc1 = o1.getLoc();
            Loc loc2 = o2.getLoc();
            if (loc1 != null && loc2 != null) {
                ret = loc1.getStart().compareTo(loc2.getStart());
            }
            
            if(reversed){
                ret = ret * -1;
            }
            
            return ret;
        }
    }
}
