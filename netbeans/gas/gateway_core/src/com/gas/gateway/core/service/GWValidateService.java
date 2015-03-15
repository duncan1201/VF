/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.gateway.core.service.api.*;
import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IGWValidateService.class)
public class GWValidateService implements IGWValidateService {

    static final Map<String, String> insert2donors = new HashMap<String, String>();

    static {
        insert2donors.put(PRO1_INSERT[0], PRO1_DONOR[0]);

        insert2donors.put(PRO2_INSERT[0], PRO2_DONOR[0]);
        insert2donors.put(PRO2_INSERT[1], PRO2_DONOR[1]);

        insert2donors.put(PRO3_INSERT[0], PRO3_DONOR[0]);
        insert2donors.put(PRO3_INSERT[1], PRO3_DONOR[1]);
        insert2donors.put(PRO3_INSERT[2], PRO3_DONOR[2]);

        insert2donors.put(PRO4_INSERT[0], PRO4_DONOR[0]);
        insert2donors.put(PRO4_INSERT[1], PRO4_DONOR[1]);
        insert2donors.put(PRO4_INSERT[2], PRO4_DONOR[2]);
        insert2donors.put(PRO4_INSERT[3], PRO4_DONOR[3]);

        insert2donors.put(FRAG3_INSERT[0], FRAG3_DONOR[0]);
        insert2donors.put(FRAG3_INSERT[1], FRAG3_DONOR[1]);
        insert2donors.put(FRAG3_INSERT[2], FRAG3_DONOR[2]);
    }
    IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
    String[] PRO2 = {"L1-R5", "L5-L2"};
    String[] PRO3 = {"L1-L4", "R4-R3", "L3-L2"};
    String[] PRO4 = {"L1-R5", "L5-L4", "R4-R3", "L3-L2"};
    String[] FRAG3 = {"L4-R1", "L1-L2", "R2-L3"};

    public GWValidateService() {
        Arrays.sort(PRO2);
        Arrays.sort(PRO3);
        Arrays.sort(PRO4);
        Arrays.sort(FRAG3);
    }

    @Override
    public GW_STATE validateAddAttbSites(AnnotatedSeq seq) {
        GW_STATE ret = GW_STATE.VALID;
        if (seq == null) {
            ret = GW_STATE.NO_SELECTION;
        } else if (seq.isCircular()) {
            ret = GW_STATE.NOT_LINEAR;
        } else if (seq.getOverhangSize() > 0) {
            ret = GW_STATE.NOT_BLUNT_ENDED;
        }
        return ret;
    }

    @Override
    public GW_STATE validateLR(List<AnnotatedSeq> seqs) {
        GW_STATE ret = GW_STATE.VALID;
        int destVectorCount = getDestVectorCount(seqs);
        if (destVectorCount == 0) {
            ret = GW_STATE.DEST_NOT_FOUND;
            return ret;
        } else if (destVectorCount > 1) {
            ret = GW_STATE.DEST_TOO_MANY;
            return ret;
        }
        Integer destIndex = indexOfDestVector(seqs);

        AnnotatedSeq destVector = seqs.get(destIndex.intValue());
        List<AnnotatedSeq> entryClones = CommonUtil.remove(seqs, destVector);
        if (entryClones.isEmpty()) {
            ret = GW_STATE.ENTRY_NOT_FOUND;
            return ret;
        }

        Collections.sort(entryClones, new Sorters.EntryCloneSorter());

        AttSiteList rSiteList = attSiteService.getAttRSites(destVector);

        for (int i = 0; i < entryClones.size(); i++) {
            AnnotatedSeq entryClone = entryClones.get(i);

            AttSiteList lrSitesList = attSiteService.getAttLRSites(entryClone);
            String lrSiteNames = lrSitesList.getShortNames().toUpperCase(Locale.ENGLISH);
            String lrSiteNamesReversed = lrSitesList.getShortNames(true).toUpperCase(Locale.ENGLISH);

            String entryShortNamesExpected = null;

            if (rSiteList.contains("attR1") && rSiteList.contains("attR2")) {
                if (entryClones.size() == 1) {
                    entryShortNamesExpected = PRO1_ENTRY_CLONES[i];
                } else if (entryClones.size() == 2) {
                    entryShortNamesExpected = PRO2_ENTRY_CLONES[i];
                } else if (entryClones.size() == 3) {
                    entryShortNamesExpected = PRO3_ENTRY_CLONES[i];
                } else if (entryClones.size() == 4) {
                    entryShortNamesExpected = PRO4_ENTRY_CLONES[i];
                } else {
                    ret = GW_STATE.ENTRY_TOO_MANY;
                    return ret;
                }

            } else if (rSiteList.contains("attR3") && rSiteList.contains("attR4")) {
                if (entryClones.size() == 3) {
                    entryShortNamesExpected = FRAG3_ENTRY_CLONES[i];
                } else if (entryClones.size() > 3) {
                    ret = GW_STATE.ENTRY_TOO_MANY;
                    return ret;
                } else if (entryClones.size() < 3) {
                    ret = GW_STATE.ENTRY_TOO_LITTLE;
                    return ret;
                }
            } else {
                ret = GW_STATE.DEST_VECTOR_INVALID;
                ret.setData(destVector.getName());
                return ret;
            }
            if (!entryShortNamesExpected.equalsIgnoreCase(lrSiteNames) && !entryShortNamesExpected.equalsIgnoreCase(lrSiteNamesReversed)) {
                ret = GW_STATE.ENTRY_WRONG_SITE;
                ret.setData(entryClone.getName());
                return ret;
            }
        }

        return ret;
    }

    /**
     * The R1R2 destination vector must contain one attR1 site, one attR2 site and one "CCDB" gene
     */
    private boolean isR1R2DestVector(AnnotatedSeq destVector) {
        AttSiteList rSites = attSiteService.getAttRSites(destVector);
        boolean containsCCDB = destVector.getFetureSet().getByFetureKeys("cds").getByLabels("ccdb").size() == 1;
        return rSites.size() == 2 && rSites.contains("attR1") && rSites.contains("attR2") && containsCCDB;
    }
    
    /**
     * The R4R3 destination vector must contain one attR4 site, one attR3 site and one "CCDB" gene
     */
    private boolean isR4R3DestVectorByAnnotation(AnnotatedSeq as){
        boolean containsAttR4 = as.getFetureSet().getByFetureKeys(FetureKeyCnst.attR4).size() == 1;
        boolean containsAttR3 = as.getFetureSet().getByFetureKeys(FetureKeyCnst.attR3).size() == 1;
        boolean containsCCDB = as.getFetureSet().getByFetureKeys("cds").getByLabels("ccdb").size() == 1;
        return containsAttR3 && containsAttR4 && containsCCDB;
    }

    private boolean isR4R3DestVector(AnnotatedSeq destVector) {
        AttSiteList rSites = attSiteService.getAttRSites(destVector);
        boolean containsCCDB = destVector.getFetureSet().getByFetureKeys("ccdb").size() == 1;
        return rSites.size() == 2 && rSites.contains("attR3") && rSites.contains("attR4") && containsCCDB;
    }

    @Override
    public Integer indexOfDonorVector(List<AnnotatedSeq> seqs) {
        Integer ret = null;
        for (int i = 0; i < seqs.size(); i++) {
            AnnotatedSeq as = seqs.get(i);
            AttSiteList pSiteList = attSiteService.getAttPSites(as);
            if (pSiteList.size() == 2) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    @Override
    public boolean isDestVector(AnnotatedSeq dest) {
        boolean ret = false;
        AttSiteList siteList = attSiteService.getAttRSites(dest);
        if (siteList.size() == 2) {
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean isDonorVector(AnnotatedSeq donor) {
        boolean ret = false;
        if (!donor.isCircular()) {
            ret = false;
            return ret;
        }
        AttSiteList siteList = attSiteService.getAttPSites(donor);
        if (siteList.size() == 2) {
            ret = true;
        }
        return ret;
    }

    /**
     * 1. Get the number of donors and non-donors 2. Check to make sure the
     * number of donors is the same as the number of non-donors 3. Check whether
     * it's pro1, pro2, pro3, pro4 or frag3 4. Check the attP sites and attB
     * sites.
     */
    @Override
    public GW_STATE validateBP(List<AnnotatedSeq> seqs) {
        GW_STATE ret = GW_STATE.VALID;
        Integer donorIndex = indexOfDonorVector(seqs);
        if (donorIndex == null) {
            ret = GW_STATE.DONOR_NOT_FOUND;
            return ret;
        }

        List<AnnotatedSeq> donors = getDonors(seqs);
        List<AnnotatedSeq> inserts = getInserts(seqs);
        Collections.sort(donors, new Sorters.DonorSorter());
        Collections.sort(inserts, new Sorters.InsertSorter());
        if (donors.isEmpty()) {
            ret = GW_STATE.DONOR_NOT_FOUND;
        } else if (inserts.isEmpty()) {
            ret = GW_STATE.INSERT_NOT_FOUND;
        } else if (donors.size() != inserts.size()) {
            ret = GW_STATE.DONOR_INSERT_COUNT_NO_MATCH;
        } else if (donors.size() == inserts.size()) {
            for (int i = 0; i < donors.size(); i++) {
                AnnotatedSeq insert = inserts.get(i);
                AnnotatedSeq donor = donors.get(i);

                AttSiteList bSiteList = attSiteService.getAttBSites(insert, true);
                String bShortNames = bSiteList.getShortNames().toUpperCase(Locale.ENGLISH);
                String bShortNamesReversed = bSiteList.getShortNames(true).toUpperCase(Locale.ENGLISH);

                AttSiteList pSiteList = attSiteService.getAttPSites(donor);
                String pSiteNames = pSiteList.getShortNames().toUpperCase(Locale.ENGLISH);
                String pSiteNamesReversed = pSiteList.getShortNames(true).toUpperCase(Locale.ENGLISH);
                String donorStr = null;
                if (insert2donors.containsKey(bShortNames)) {
                    donorStr = insert2donors.get(bShortNames);
                } else if (insert2donors.containsKey(bShortNamesReversed)) {
                    donorStr = insert2donors.get(bShortNamesReversed);
                } else {
                    ret = GW_STATE.INSERT_NOT_FOUND;
                    return ret;
                }
                if (!donorStr.equalsIgnoreCase(pSiteNames) && !donorStr.equalsIgnoreCase(pSiteNamesReversed)) {
                    ret = GW_STATE.DONOR_INSERT_NOT_MATCHING;
                }
                attSiteService.getAttPSites(donor);
            }
        }
        return ret;
    }

    @Override
    public List<AnnotatedSeq> getInserts(List<AnnotatedSeq> seqs) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq seq : seqs) {
            boolean isInsert = !attSiteService.getAttBSites(seq, true).isEmpty();
            if (isInsert) {
                ret.add(seq);
            }
        }
        return ret;
    }

    @Override
    public List<AnnotatedSeq> getDonors(List<AnnotatedSeq> seqs) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq seq : seqs) {
            boolean isDonor = isDonorVector(seq);
            if (isDonor) {
                ret.add(seq);
            }
        }
        return ret;
    }

    @Override
    public GW_STATE validateOneClick(final List<AnnotatedSeq> seqs) {
        GW_STATE ret = GW_STATE.VALID;
        Integer destIndex = indexOfDestVector(seqs);
        if (destIndex == null || destIndex == -1) {
            ret = GW_STATE.DEST_NOT_FOUND;
            return ret;
        }
        AnnotatedSeq dest = seqs.get(destIndex.intValue());
        List<AnnotatedSeq> rest = CommonUtil.remove(seqs, dest);
        if (rest.size() == 2 || rest.size() == 4 || rest.size() == 8) { // pro1, pro2, pro4
            Integer r1r2Vector = indexOfDestR1R2Vector(seqs);
            if (r1r2Vector == null || r1r2Vector == -1) {
                ret = GW_STATE.DEST_VECTOR_INVALID;
                ret.setData(dest.getName());
                return ret;
            }
        }
        ret = validateBP(rest);
        if (ret != GW_STATE.VALID) {
            return ret;
        }
        return ret;
    }

    @Override
    public Integer indexOfDestR1R2Vector(List<AnnotatedSeq> seqs) {
        Integer ret = null;
        for (int i = 0; i < seqs.size(); i++) {
            boolean isR1R2 = isR1R2DestVector(seqs.get(i));
            if (isR1R2) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public int getDestVectorCount(List<AnnotatedSeq> seqs) {
        int ret = 0;
        for (AnnotatedSeq seq : seqs) {
            if (isR1R2DestVector(seq) || isR4R3DestVectorByAnnotation(seq)) {
                ret++;
            }
        }
        return ret;
    }

    /**
     * Will look for vectors with R1 R2 site first; if not found, then R3 R4
     * sites.
     */
    @Override
    public Integer indexOfDestVector(List<AnnotatedSeq> seqs) {
        Integer ret = -1;
        for (int i = 0; i < seqs.size(); i++) {
            boolean isR1R2 = isR1R2DestVector(seqs.get(i));
            if (isR1R2) {
                ret = i;
                break;
            }
        }

        if (ret == null || ret.intValue() == -1) {
            for (int i = 0; i < seqs.size(); i++) {
                boolean isR3R4 = isR4R3DestVectorByAnnotation(seqs.get(i));
                if (isR3R4) {
                    ret = i;
                    break;
                }
            }
        }
        return ret;
    }
}
