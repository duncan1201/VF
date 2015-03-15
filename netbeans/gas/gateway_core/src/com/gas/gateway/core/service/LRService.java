/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.common.ui.misc.Loc;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.FetureSet;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.filesystem.Folder;
import com.gas.gateway.core.service.api.*;
import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ILRService.class)
public class LRService implements ILRService {

    private String MUL_FRAG_TPT = "pEXP/%s/%d entry clones";
    private String ONE_FRAG_TPT = "pEXP/%s/%s";
    private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
    private IAttPosService attPosSerivce = Lookup.getDefault().lookup(IAttPosService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);

    @Override
    public AnnotatedSeq createExpressionClone(List<AnnotatedSeq> seqs) {
        AnnotatedSeq ret = null;
        Integer index = validateService.indexOfDestVector(seqs);
        AnnotatedSeq destVector = seqs.remove(index.intValue());
        ret = createExpressionClone(new AnnotatedSeqList(seqs), destVector, true);
        return ret;
    }

    private void setName(List<AnnotatedSeq> entryClones, AnnotatedSeq dest, AnnotatedSeq exp) {
        if (entryClones.size() > 2) {
            final String destName = dest.getName();
            exp.setName(String.format(MUL_FRAG_TPT, destName, entryClones.size()));
        } else {
            AnnotatedSeq entryClone = entryClones.get(0);
            exp.setName(String.format(ONE_FRAG_TPT, dest.getName(), entryClone.getName()));
        }
    }

    /**
     *
     */
    @Override
    public AnnotatedSeq createExpressionClone(AnnotatedSeqList entryClones, AnnotatedSeq dest, boolean includeOperatioin) {
        dest = flipDestVector(dest);
        entryClones = flipEntryClonesIfNecessary(entryClones);
        Collections.sort(entryClones, new Sorters.EntryCloneSorter());
        AnnotatedSeq entryClone = recombineEntryClones(entryClones);
        AnnotatedSeq ret = recombine(entryClone, dest);
        ret.getFetureSet().removeByFetureKeysStartingWith("attl", "attr");
        String desc = "";
        if (entryClones.size() == 1) {
            desc = String.format("an expression clone created from %s and %s", dest.getName(), entryClones.get(0).getName());
        } else if (entryClones.size() > 1) {
            desc = String.format("an expression clone created from %s and %d entry clones", dest.getName(), entryClones.size());
        }
        ret.setDesc(desc);
        ret.setCreationDate(new Date());
        ret.setLastModifiedDate(new Date());
        setName(entryClones, dest, ret);
        removeDonorSourceAnnotation(ret);

        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        if (includeOperatioin) {
            Operation operation = new Operation();
            operation.setNameEnum(Operation.NAME.GW_LR);
            operation.setDate(new Date());

            final String folderPathDest = folderService.loadWithParents(dest.getFolder().getHibernateId()).getAbsolutePath();
            Operation.Participant partDest = new Operation.Participant(folderPathDest + "\\" + dest.getName(), true);
            operation.getParticipants().add(partDest);

            for (AnnotatedSeq ec : entryClones) {
                final String folderPath = folderService.loadWithParents(ec.getFolder().getHibernateId()).getAbsolutePath();
                Operation.Participant partEC = new Operation.Participant(folderPath + "\\" + ec.getName(), true);
                operation.getParticipants().add(partEC);
            }
            ret.setOperation(operation);
        }

        ret.setCircular(true);
        annotateAttbSites(ret, entryClones, dest);
        AsHelper.clearAsResults(ret);
        return ret;
    }

    private void removeDonorSourceAnnotation(AnnotatedSeq as) {
        FetureSet tmp = as.getFetureSet().getByFetureKeys("source").getByNotes("donor vector");
        as.getFetureSet().remove(tmp);
    }

    private void annotateAttbSites(AnnotatedSeq as, AnnotatedSeqList entryClones, AnnotatedSeq destVector) {
        AttSiteList sites = attSiteService.getAttBSites(as, false);
        for (AttSite site : sites) {
            Feture feture = new Feture();
            feture.setKey(site.getName());
            Loc loc = site.getLoc();
            feture.setLucation(new Lucation(loc.getStart(), loc.getEnd(), loc.isStrand()));
            as.getFetureSet().add(feture);
        }
        // pro2
        if (entryClones.size() == 2) {
            as.getFetureSet().removeByFetureKeys("attB5r");
        } else if (entryClones.size() == 3) {
        } else if (entryClones.size() == 4) {
            as.getFetureSet().removeByFetureKeys("attB5r");
            as.getFetureSet().removeByFetureKeys("attB5r");
        }
    }

    private AnnotatedSeq recombine(AnnotatedSeq entryClone, AnnotatedSeq destVector) {
        AnnotatedSeq ret = null;
        AttSiteList sites = attSiteService.getAttLSites(entryClone);
        AttSite lSite = sites.first();
        AttSite lSite2 = sites.last();

        sites = attSiteService.getAttLRSites(destVector);
        AttSite rSite = sites.first();
        AttSite rSite2 = sites.last();

        Integer attPos = attPosSerivce.getAttPos(lSite, rSite);
        int subStart = lSite.getLoc().getStart() + attPos - 1;
        attPos = attPosSerivce.getAttPos(lSite2, rSite2);
        int subEnd = lSite2.getLoc().getStart() + attPos - 2;

        AnnotatedSeq subEntryClone = entryClone.subAs(subStart, subEnd);
        attPos = attPosSerivce.getAttPos2(lSite, rSite);
        int removeStart = rSite.getLoc().getStart() + attPos - 1;
        attPos = attPosSerivce.getAttPos2(lSite2, rSite2);
        int removeEnd = rSite2.getLoc().getStart() + attPos - 2;

        AnnotatedSeq subDestVector = AsHelper.createByRemoving(destVector, removeStart, removeEnd, false);
        subDestVector.getFetureSet().removeByFetureKeys(FetureKeyCnst.Source);
        ret = AsHelper.createNewByInsertAs(subDestVector, subEntryClone, removeStart, false);
        // annotate the destination vector
        annotateDestVector(destVector.getName(), removeStart, subEntryClone.getLength(), ret);

        return ret;
    }

    private void annotateDestVector(String destVectorName, int insertPos, int insertLength, AnnotatedSeq ret) {
        Feture feture = new Feture();
        feture.setKey(FetureKeyCnst.Parent);
        feture.getQualifierSet().add(String.format("sub_clone=%s", destVectorName));
        int start = insertPos + insertLength;
        if (start > ret.getLength()) {
            start = start % ret.getLength();
            if (start == 0) {
                start = ret.getLength();
            }
        }
        int end = insertPos - 1;
        if (end < 1) {
            end = ret.getLength();
        }

        Lucation luc = new Lucation(start, end, true);
        feture.setLucation(luc);
        ret.getFetureSet().add(feture);
    }

    private AnnotatedSeq recombineEntryClones(List<AnnotatedSeq> entryClones) {
        AnnotatedSeq ret = null;
        for (AnnotatedSeq cur : entryClones) {
            AttSiteList curSites = attSiteService.getAttLRSites(cur);
            if (ret == null) {
                AnnotatedSeq subCur = cur.subAs(curSites.first().getLoc().getStart(), curSites.last().getLoc().getEnd());
                ret = subCur;
                continue;
            }
            AttSiteList retSites = attSiteService.getAttLRSites(ret);
            AttSite curLastSite = retSites.last();
            AttSite nextFirstSite = curSites.first();
            Integer attPos = attPosSerivce.getAttPos(curLastSite, nextFirstSite);
            AnnotatedSeq subRet = AsHelper.subAs(ret, 1, curLastSite.getLoc().getStart() + attPos - 2);

            Integer attPos2 = attPosSerivce.getAttPos2(curLastSite, nextFirstSite);
            AnnotatedSeq subCur = AsHelper.subAs(cur, nextFirstSite.getLoc().getStart() + attPos2 - 1, curSites.last().getLoc().getEnd());
            ret = AsHelper.concatenate(subRet, subCur);
        }
        return ret;
    }

    private AnnotatedSeq flipDestVector(AnnotatedSeq dest) {
        String[] sites = {"R1-R2", "R4-R3"};
        List<String> SITE_LIST = Arrays.asList(sites);
        AnnotatedSeq ret = null;
        AttSiteList siteList = attSiteService.getAttRSites(dest);
        String shortNames = siteList.getShortNames();
        if (!SITE_LIST.contains(shortNames)) {
            ret = dest.flip();
        } else {
            ret = dest;
        }
        return ret;
    }

    private AnnotatedSeqList flipEntryClonesIfNecessary(AnnotatedSeqList entryClones) {
        AnnotatedSeqList ret = new AnnotatedSeqList();
        for (AnnotatedSeq as : entryClones) {
            boolean flip = checkReversed(as);
            if (flip) {
                AnnotatedSeq flippedAs = as.flip();
                ret.add(flippedAs);
            } else {
                ret.add(as);
            }
        }
        return ret;
    }

    private boolean checkReversed(AnnotatedSeq as) {
        boolean flip = false;
        String[] sites = {"L1-R5", "L5-L2",
            "L1-L4", "R4-R3", "L3-L2",
            "L1-R5", "L5-L4", "R4-R3", "L3-L2",
            "L4-R1", "L1-L2", "R2-L3"};
        AttSiteList siteList = attSiteService.getAttLRSites(as);
        String reversedShortName = siteList.getShortNames(true).toUpperCase(Locale.ENGLISH);
        if (Arrays.binarySearch(sites, reversedShortName) > -1) {
            flip = true;
        }
        return flip;
    }
}
