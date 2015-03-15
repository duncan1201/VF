/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.common.ui.misc.Loc;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.AsHelper;
import static com.gas.domain.core.as.AsHelper.insertAs;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Operation;
import com.gas.gateway.core.service.api.*;
import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IBPService.class)
public class BPService implements IBPService {

    private String NAME = "pENTR 221-p53";
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
    private IAttPosService attPosSerivce = Lookup.getDefault().lookup(IAttPosService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);

    @Override
    public List<AnnotatedSeq> createEntryClones(List<AnnotatedSeq> all, boolean includeOperations) {
        List<AnnotatedSeq> inserts = getInserts(all);
        List<AnnotatedSeq> donors = validateService.getDonors(all);
        Collections.sort(inserts, new Sorters.InsertSorter());
        Collections.sort(donors, new Sorters.DonorSorter());
        return createEntryClones(inserts, donors, includeOperations);
    }

    protected List<AnnotatedSeq> getInserts(List<AnnotatedSeq> seqs) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (AnnotatedSeq seq : seqs) {
            boolean isDonor = validateService.isDonorVector(seq);
            if (!isDonor) {
                ret.add(seq);
            }
        }
        return ret;
    }

    List<AnnotatedSeq> createEntryClones(List<AnnotatedSeq> inserts, List<AnnotatedSeq> donors, boolean includeOperations) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        for (int i = 0; i < inserts.size(); i++) {
            AnnotatedSeq insert = inserts.get(i);
            AnnotatedSeq donor = donors.get(i);
            AnnotatedSeq entryClone = createEntryClone(insert, donor, true);
            ret.add(entryClone);
        }
        return ret;
    }

    /**
     *
     */
    AnnotatedSeq createEntryClone(AnnotatedSeq pcrProduct, AnnotatedSeq donor, boolean includeOperation) {
        Operation operation = null;
        if (includeOperation) {
            operation = new Operation();
            operation.setNameEnum(Operation.NAME.GW_BP);
            operation.setDate(new Date());
            final String folderPath = folderService.loadWithParents(pcrProduct.getFolder().getHibernateId()).getAbsolutePath();
            Operation.Participant part = new Operation.Participant(folderPath + "\\" + pcrProduct.getName(), true);
            operation.getParticipants().add(part);
            final String donorFolderPath = folderService.loadWithParents(donor.getFolder().getHibernateId()).getAbsolutePath();
            part = new Operation.Participant(donorFolderPath + "\\" + donor.getName(), true);
            operation.getParticipants().add(part);
        }

        pcrProduct = FlipPcrProductIfNeeded(pcrProduct);
        donor = FlipDonorIfNeeded(donor);

        // Get the attB sites, sort them, excise the insert.
        final List<AttSite> attbSites = attSiteService.getAttBSites(pcrProduct, true);
        Collections.sort(attbSites, new AttSite.LocSorter());

        AttSite bSite = attbSites.get(0);
        AttSite bSite2 = attbSites.get(1);

        // Get the attP sites, sort them, excise the donor
        final List<AttSite> attpSites = attSiteService.getAttPSites(donor);
        Collections.sort(attpSites, new AttSite.LocSorter());

        AttSite pSite = attpSites.get(0);
        AttSite pSite2 = attpSites.get(1);

        Integer attPos = attPosSerivce.getAttPos(bSite, pSite);
        int subStart = bSite.getLoc().getStart() + attPos - 1;

        attPos = attPosSerivce.getAttPos(bSite2, pSite2);
        int subEnd = bSite2.getLoc().getStart() + attPos - 2;

        AnnotatedSeq insert2 = AsHelper.subAs(pcrProduct, subStart, subEnd);

        // 
        int removedStart;
        attPos = attPosSerivce.getAttPos2(bSite, pSite);
        removedStart = pSite.getLoc().getStart() + attPos - 1;
        int removedEnd;
        attPos = attPosSerivce.getAttPos2(bSite2, pSite2);
        removedEnd = pSite2.getLoc().getStart() + attPos - 2;
        AnnotatedSeq donor2 = AsHelper.createByRemoving(donor, removedStart, removedEnd, false);

        // recombinate 
        AnnotatedSeq ret = AsHelper.createNewByInsertAs(donor2, insert2, removedStart, false, new AnnotatedSeq.ELEMENT[]{AnnotatedSeq.ELEMENT.FEATURE, AnnotatedSeq.ELEMENT.SEQ});
        ret.setFolder(donor2.getFolder());
        ret.setCircular(true);

        // annotate the two "Source" feature
        annotateSourceFeatures(ret, insert2, donor2, removedStart);

        // annotate first attL/R site        
        AttSiteList attLSites = attSiteService.getAttLSites(ret);
        AttSiteList attRSites = attSiteService.getAttRSites(ret);
        AttSiteList lrSites = new AttSiteList();
        lrSites.addAll(attLSites);
        lrSites.addAll(attRSites);
        Iterator<AttSite> lrSiteItr = lrSites.iterator();
        while (lrSiteItr.hasNext()) {
            AttSite lSite = lrSiteItr.next();
            Feture feture = createFeture(lSite);
            ret.getFetureSet().add(feture);
        }

        ret.getFetureSet().removeByFetureKeysStartingWith("attb", "attp");

        StringBuilder builder = new StringBuilder();
        builder.append("pENTR");
        builder.append('/');
        builder.append(pcrProduct.getName());
        builder.append('/');
        builder.append(donor.getName());
        ret.setName(builder.toString());
        ret.setDesc(String.format("an entry clone from %s and %s", pcrProduct.getName(), donor.getName()));
        ret.setCreationDate(new Date());
        ret.setLastModifiedDate(new Date());
        if (includeOperation) {
            ret.setOperation(operation);
        }
        return ret;
    }

    /**
     *
     */
    private void annotateSourceFeatures(AnnotatedSeq entryClone, AnnotatedSeq insert, AnnotatedSeq donor, int insertPos) {
        Feture feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", insert.getName()));

        int start = insertPos;
        int end = insertPos + insert.getLength() - 1;
        if (end > entryClone.getLength()) {
            end = end % entryClone.getLength();
        }
        Lucation lucation = new Lucation(start, end, true);
        feture.setLucation(lucation);
        entryClone.getFetureSet().add(feture);

        feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", donor.getName()));
        feture.getQualifierSet().add("note=Donor vector");
        start = insertPos + insert.getLength();
        end = insertPos - 1;
        if (start > entryClone.getLength()) {
            start = start % entryClone.getLength();
        }
        if (end < 1) {
            end = entryClone.getLength();
        }
        lucation = new Lucation(start, end, true);
        feture.setLucation(lucation);
        entryClone.getFetureSet().add(feture);
    }

    private Feture createFeture(AttSite rSite) {
        Loc loc = rSite.getLoc();

        Feture feture = new Feture();
        feture.setKey(rSite.getBaseName());
        feture.addQualifier(String.format("label=%s", rSite.getBaseName()));
        Lucation lucation = new Lucation();
        lucation.setContiguousMin(loc.getStart());
        lucation.setContiguousMax(loc.getEnd());
        lucation.setStrand(loc.isStrand());
        feture.setLucation(lucation);
        return feture;
    }

    public AnnotatedSeq createByProduct(AnnotatedSeq pcrProduct, AnnotatedSeq donor) {
        return null;
    }

    private AnnotatedSeq FlipDonorIfNeeded(AnnotatedSeq as) {
        AnnotatedSeq ret = null;
        boolean flip = false;
        AttSiteList siteList = attSiteService.getAttBSites(as, true);

        if (siteList.contains("attL1") && siteList.contains("attL2")) {
            AttSite site = siteList.get("attL1");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL1") && siteList.contains("attL5r")) { // pro2
            AttSite site = siteList.get("attL1");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL5") && siteList.contains("attL2")) { // pro2
            AttSite site = siteList.get("attL5");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL1") && siteList.contains("attL4")) { // pro3
            AttSite site = siteList.get("attL1");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL4r") && siteList.contains("attL3r")) { // pro3
            AttSite site = siteList.get("attL4r");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL3") && siteList.contains("attL2")) { // pro3
            AttSite site = siteList.get("attL3");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL1") && siteList.contains("attL5r")) { // pro4
            AttSite site = siteList.get("attL1");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL5") && siteList.contains("attL4")) { // pro4
            AttSite site = siteList.get("attL5");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL4r") && siteList.contains("attL3r")) { // pro4
            AttSite site = siteList.get("attL4r");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL3") && siteList.contains("attL2")) { // pro4
            AttSite site = siteList.get("attL3");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL4") && siteList.contains("attL1r")) { // 3 frag
            AttSite site = siteList.get("attL4");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL1") && siteList.contains("attL2")) { // 3 frag
            AttSite site = siteList.get("attL1");
            flip = !site.getLoc().isStrand();
        } else if (siteList.contains("attL2r") && siteList.contains("attL3")) { // 3 frag
            AttSite site = siteList.get("attL2r");
            flip = !site.getLoc().isStrand();
        }

        ret = flip ? as.flip() : as;
        return ret;
    }

    private AnnotatedSeq FlipPcrProductIfNeeded(AnnotatedSeq as) {
        AnnotatedSeq ret = null;
        boolean flip = false;
        AttSite first = null;
        AttSite second = null;
        AttSiteList siteList = attSiteService.getAttBSites(as, true);
        if (siteList.contains("attB1") && siteList.contains("attB2")) {
            first = siteList.get("attB1");
            second = siteList.get("attB2");
        } else if (siteList.contains("attB1") && siteList.contains("attB5r")) { // pro2
            first = siteList.get("attB1");
            second = siteList.get("attB5r");
        } else if (siteList.contains("attB5") && siteList.contains("attB2")) { // pro2
            first = siteList.get("attB5");
            second = siteList.get("attB2");
        } else if (siteList.contains("attB1") && siteList.contains("attB4")) { // pro3
            first = siteList.get("attB1");
            second = siteList.get("attB4");
        } else if (siteList.contains("attB4r") && siteList.contains("attB3r")) { // pro3
            first = siteList.get("attB4r");
            second = siteList.get("attB3r");
        } else if (siteList.contains("attB3") && siteList.contains("attB2")) { // pro3
            first = siteList.get("attB3");
            second = siteList.get("attB2");
        } else if (siteList.contains("attB1") && siteList.contains("attB5r")) { // pro4
            first = siteList.get("attB1");
            second = siteList.get("attB5r");
        } else if (siteList.contains("attB5") && siteList.contains("attB4")) { // pro4
            first = siteList.get("attB5");
            second = siteList.get("attB4");
        } else if (siteList.contains("attB4r") && siteList.contains("attB3r")) { // pro4
            first = siteList.get("attB4r");
            second = siteList.get("attB3r");
        } else if (siteList.contains("attB3") && siteList.contains("attB2")) { // pro4
            first = siteList.get("attB3");
            second = siteList.get("attB2");
        } else if (siteList.contains("attB4") && siteList.contains("attB1r")) { // 3 frag
            first = siteList.get("attB4");
            second = siteList.get("attB1r");
        } else if (siteList.contains("attB1") && siteList.contains("attB2")) { // 3 frag
            first = siteList.get("attB1");
            second = siteList.get("attB2");
        } else if (siteList.contains("attB2r") && siteList.contains("attB3")) { // 3 frag
            first = siteList.get("attB2r");
            second = siteList.get("attB3");
        }

        flip = first.getLoc().getStart() > second.getLoc().getStart();
        ret = flip ? as.flip() : as;
        return ret;
    }
}
