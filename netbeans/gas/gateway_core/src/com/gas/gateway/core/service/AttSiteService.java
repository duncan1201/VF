/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.gateway.core.service.api.AttSiteList;
import com.gas.gateway.core.service.api.AttSite;
import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Qualifier;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.as.ParentLoc;
import com.gas.gateway.core.service.api.*;
import static com.gas.gateway.core.service.api.IAttSiteService.SD_postfix;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IAttSiteService.class)
public class AttSiteService implements IAttSiteService {

    IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    private AnnotatedSeq getRightAs(PrimerAdapter adapter, boolean stopCodon, boolean fuse) {
        final AttSite attSite = adapter.getAttSite();
        AnnotatedSeq ret = new AnnotatedSeq();
        StringBuilder builder = new StringBuilder();

        if (stopCodon) {
            builder.append(IAttSiteService.STOP_CODON);
        }
        if(fuse){
            builder.append(adapter.getPrefix());
        }

        final String str = attSite.getSeq();

        Feture feture = new Feture(attSite.getName());
        feture.getQualifierSet().add(new Qualifier(String.format("label=%s", adapter.getName())));
        Lucation luc = new Lucation();
        String attSiteName = attSite.getName().toUpperCase(Locale.ENGLISH);
        luc.setStrand(attSiteName.endsWith("R"));
        luc.setContiguousMin(builder.length() + 1);
        luc.setContiguousMax(luc.getStart() + str.length() - 1);
        feture.setLucation(luc);
        ret.getFetureSet().add(feture);
        builder.append(str);
        builder.append(adapter.getPostfix());

        ret.setSequence(builder.toString());
        ret.setCircular(false);
        return ret;
    }

    public AttSite getAttSite(AnnotatedSeq as, AttSite target, boolean includeNested) {
        AttSite ret = null;
        List<AttSite> attSites = getAttSites(as, includeNested);
        for (AttSite attSite : attSites) {
            if (target.getName().equalsIgnoreCase(attSite.getName())) {
                ret = attSite;
            }
        }
        return ret;
    }

    @Override
    public AttSiteList getAttLRSites(AnnotatedSeq as) {
        AttSiteList ret = new AttSiteList();
        AttSiteList lSites = getAttLSites(as);
        AttSiteList rSites = getAttRSites(as);
        ret.addAll(lSites);
        ret.addAll(rSites);
        return ret;
    }

    @Override
    public AttSiteList getAttLSites(AnnotatedSeq as) {
        AttSiteList ret = getAttSites(as, 'L', false);
        return ret;
    }

    @Override
    public AttSiteList getAttRSites(AnnotatedSeq as) {
        AttSiteList ret = getAttSites(as, 'R', false);
        return ret;
    }

    @Override
    public AttSiteList getAttSites(AnnotatedSeq as, Character clazz, boolean includeNested) {
        AttSiteList attSiteList = new AttSiteList(getAttSites(as, includeNested));
        AttSiteList ret = new AttSiteList();
        ret.setAttSites(attSiteList.getAttSites(clazz));
        return ret;
    }

    private AnnotatedSeq getLeftAs(PrimerAdapter adapter, boolean sd, boolean kozak, boolean startCodon, boolean fuse) {
        AnnotatedSeq ret = new AnnotatedSeq();
        final AttSite attSite = adapter.getAttSite();
        StringBuilder builder = new StringBuilder();
        builder.append(adapter.getPrefix());
        // "attB" feture
        Feture feture = new Feture(attSite.getName());
        feture.getQualifierSet().add(new Qualifier(String.format("label=%s", adapter.getName())));
        Lucation luc = new Lucation();
        luc.setContiguousMin(builder.length() + 1);
        luc.setContiguousMax(luc.getStart() + attSite.length() - 1);
        String attSiteName = adapter.getAttSite().getName().toString().toUpperCase(Locale.ENGLISH);
        luc.setStrand(!attSiteName.endsWith("R"));
        feture.setLucation(luc);
        ret.getFetureSet().add(feture);

        builder.append(attSite.getSeq());
        if (fuse) {
            builder.append(adapter.getPostfix());
        }

        if (sd) {
            feture = new Feture();
            feture.setKey(FetureKeyCnst.SD);
            luc = new Lucation();
            luc.setContiguousMin(builder.length() + 1);
            luc.setContiguousMax(luc.getStart() + IAttSiteService.SD.length() - 1);
            luc.setStrand(true);
            feture.setLucation(luc);
            builder.append(IAttSiteService.SD);
            ret.getFetureSet().add(feture);
        }
        if (kozak) {
            feture = new Feture();
            feture.setKey(FetureKeyCnst.KOZAK);
            luc = new Lucation();
            luc.setStrand(true);
            luc.setContiguousMin(builder.length() + 1);
            luc.setContiguousMax(luc.getStart() + IAttSiteService.KOZAK.length() - 1);
            feture.setLucation(luc);
            ret.getFetureSet().add(feture);
            builder.append(IAttSiteService.KOZAK);
        } else if (startCodon) {
            builder.append(IAttSiteService.START_CODON);
        }

        ret.setSequence(builder.toString());
        ret.setCircular(false);
        return ret;
    }

    @Override
    public AttSiteList getAttPSites(AnnotatedSeq as) {
        AttSiteList siteList = getAttSites(as, 'P', false);
        Loc loc = getCCDB(as);
        if (loc == null) {
            loc = getCCDA(as);
        }
        if (loc == null) {
            return siteList;
        }
        loc.setTotalPos(as.getLength());
        List<AttSite> sites = siteList;
        for (AttSite site : sites) {
            Loc siteLoc = site.getLoc();
            Boolean towards = siteLoc.towards(loc.center());
            site.setToInsert(towards);
        }
        return siteList;
    }

    private Loc getCCDA(AnnotatedSeq as) {
        Loc ret = null;
        List<Feture> fetures = as.getFetureSet().getByFeturesByKeys(FetureKeyCnst.MISC_FEATURE);
        Iterator<Feture> itr = fetures.iterator();

        while (itr.hasNext()) {
            Feture feture = itr.next();
            Qualifier qualifier = feture.getQualifierSet().getQualifier("label");
            if (qualifier != null && qualifier.getValue().equalsIgnoreCase("ccda")) {
                ret = feture.getLucation().toLoc();
                break;
            }
        }
        return ret;
    }

    private Loc getCCDB(AnnotatedSeq as) {
        Loc ret = null;
        List<Feture> fetures = as.getFetureSet().getByFeturesByKeys("CDS");
        Iterator<Feture> itr = fetures.iterator();

        while (itr.hasNext()) {
            Feture feture = itr.next();
            Qualifier qualifier = feture.getQualifierSet().getQualifier("label");
            if (qualifier != null && qualifier.getValue().equalsIgnoreCase("ccdb")) {
                ret = feture.getLucation().toLoc();
                break;
            }
        }
        return ret;
    }

    @Override
    public AttSiteList getAttBSites(AnnotatedSeq as, boolean primerAdapter) {
        AttSiteList ret = new AttSiteList();

        List<PrimerAdapter> adapters = PrimerAdapter.getAll();
        for (PrimerAdapter adapter : adapters) {
            String targetSeq = null;

            final AttSite attSite = adapter.getAttSite();
            if (primerAdapter) {
                if (adapter.isForward()) {
                    targetSeq = adapter.getPrefix() + attSite.getSeq();
                } else {
                    targetSeq = attSite.getSeq() + adapter.getPostfix();
                }
            } else {
                targetSeq = attSite.getSeq();
            }
            LocList locList = AsHelper.findSeq(as, targetSeq);
            Iterator<Loc> itr = locList.iterator();
            while (itr.hasNext()) {
                Loc loc = itr.next();
                if (attSite.isReverseComp()) {
                    loc.setStrand(loc.isStrand());
                }
                if (adapter.isForward()) {
                    if (primerAdapter) {
                        if (loc.isStrand()) {
                            loc.setStart(loc.getStart() + adapter.getPrefix().length());
                        } else {
                            loc.setEnd(loc.getEnd() - adapter.getPrefix().length());
                        }
                    }
                } else {
                    if (primerAdapter) {
                        if (loc.isStrand()) {
                            loc.setEnd(loc.getEnd() - adapter.getPostfix().length());
                        } else {
                            loc.setStart(loc.getStart() + adapter.getPostfix().length());
                        }
                    }
                }
                AttSite clone = attSite.clone();
                if (clone.isReverseComp()) {
                    clone = clone.flip();
                    loc.setStrand(!loc.isStrand());
                }
                clone.setLoc(loc);
                if (!ret.contains(clone.getName())) {
                    ret.add(clone);
                }
            }
        }

        return ret;
    }

    @Override
    public boolean isAttbPcrProduct(AnnotatedSeq as) {
        boolean ret = false;
        if (as.isCircular()) {
            ret = false;
        }
        AttSiteList attSites = getAttBSites(as, true);

        return ret;
    }

    @Override
    public boolean isEntryClone(AnnotatedSeq as) {
        boolean ret = false;
        throw new UnsupportedOperationException();
        //return ret;
    }

    @Override
    public boolean isDestVector(AnnotatedSeq as) {
        boolean ret = false;
        throw new UnsupportedOperationException();
        //return ret;
    }

    @Override
    public boolean isDonorVector(AnnotatedSeq as) {
        boolean ret = true;
        if (as == null) {
            return false;
        }
        List<AttSite> sites = getAttSites(as, false);
        for (AttSite site : sites) {
            Character clazz = site.getClazz();
            if (clazz.toString().equalsIgnoreCase("p")) {
                ret = true;
            }
        }
        return ret;
    }

    protected List<AttSite> getAttSites(AnnotatedSeq as, boolean includeNested) {
        List<AttSite> ret = new ArrayList<AttSite>();
        LocList locList2 = new LocList();
        List<AttSite> attSites = AttSite.getAllSites();
        for (AttSite attSite : attSites) {
            final String clazz = attSite.getClazz().toString();
            String siteSeq = attSite.getSeq();

            LocList locList = AsHelper.findSeq(as, siteSeq);
            Iterator<Loc> itr = locList.iterator();
            while (itr.hasNext()) {
                Loc loc = itr.next();
                if (!includeNested) {
                    if (!locList2.isSupersetOf(loc)) {
                        locList2.add(loc);
                        ret.add(new AttSite(attSite, loc));
                    }
                } else {
                    ret.add(new AttSite(attSite, loc));
                }

            }
        }

        return ret;
    }

    @Override
    public AnnotatedSeq addAttBSite(AnnotatedSeq as, PrimerAdapter leftAdapter, boolean sd, boolean kozak, boolean fuseForward, boolean startCodon, PrimerAdapter rightAdapter, boolean fuseReverse, boolean stopCodon) {

        final String folderPath = folderService.loadWithParents(as.getFolder().getHibernateId()).getAbsolutePath();

        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.ADD_ATTB);
        operation.setDate(new Date());
        Operation.Participant part = new Operation.Participant(folderPath + "\\" + as.getName(), true);
        operation.addParticipant(part);

        AnnotatedSeq leftAs = getLeftAs(leftAdapter, sd, kozak, startCodon, fuseForward);
        AnnotatedSeq rightAs = getRightAs(rightAdapter, stopCodon, fuseReverse);
        as.getParentLocSet().clear();

        ParentLoc parentLoc = new ParentLoc();
        parentLoc.setTotalPos(as.getLength());
        parentLoc.setStart(1);
        parentLoc.setEnd(as.getLength());
        parentLoc.setOffset(leftAs.getLength());

        AnnotatedSeq ret = AsHelper.concatenate(as, rightAs);
        ret = AsHelper.concatenate(ret.flip(), leftAs.flip());

        ret.getParentLocSet().add(parentLoc);
        ret = ret.flip();
        ret.setOperation(operation);

        return ret;
    }
}
