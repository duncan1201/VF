/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.service;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.LocUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.ParentLoc;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Operation;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RMap;
import com.gas.enzyme.core.service.api.IDigestService;
import java.io.File;
import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IDigestService.class)
public class DigestService implements IDigestService {

    @Override
    public List<AnnotatedSeq> digest(AnnotatedSeq as) {
        return digest(as, as.getRmap().getEntries());
    }

    /*
     * @param siteStart start restriction site
     * @param siteEnd end restriction site
     * return the segment between the two restriction sites
     */
    private AnnotatedSeq _digest(RMap.Entry siteStart, RMap.Entry siteEnd, AnnotatedSeq as) {
        if (!as.isCircular() && siteStart.getStart() > siteEnd.getStart()) {
            throw new IllegalArgumentException("entryA.getStart() > entryB.getStart()");
        }
        final int[] cutPosStart = siteStart.getDownstreamCutPos();
        final int[] cutPosEnd = siteEnd.getDownstreamCutPos();
        final int cutTypeStart = siteStart.getDownstreamCutType();
        final int cutTypeEnd = siteEnd.getDownstreamCutType();

        final int start = siteStart.getStart() + Math.min(cutPosStart[0], cutPosStart[1]);
        final int end = siteEnd.getStart() + Math.max(cutPosEnd[0], cutPosEnd[1]) - 1;

        AnnotatedSeq ret = AsHelper.subAs(as, start, end);
        ret.setRead(false);
        ParentLoc parentLoc = new ParentLoc(start, end);
        parentLoc.setTotalPos(as.getLength());
        ret.getParentLocSet().add(parentLoc);
        if (cutTypeStart != REN.BLUNT) {
            // start overhang           
            Overhang overhang = new Overhang();
            overhang.setFivePrime(cutTypeStart == REN.OVERHANG_5PRIME);
            overhang.setName(siteStart.getName());
            overhang.setLength(siteStart.getOverhangLength());
            overhang.setStrand(overhang.isFivePrime());
            ret.setStartOverhang(overhang);
        }

        if (cutTypeEnd != REN.BLUNT) {
            // end overhang
            Overhang overhang = new Overhang();
            overhang.setFivePrime(cutTypeEnd == REN.OVERHANG_5PRIME);
            overhang.setName(siteEnd.getName());
            overhang.setLength(siteEnd.getOverhangLength());
            overhang.setStrand(!overhang.isFivePrime());
            ret.setEndOverhang(overhang);
        }
        return ret;
    }

    private List<AnnotatedSeq> _digest(AnnotatedSeq as, RMap.Entry entry) {
        if (as.isCircular()) {
            return _digestCircular(entry, as);
        } else {
            return _digestLinear(entry, as);
        }
    }

    private List<AnnotatedSeq> _digestCircular(RMap.Entry entry, AnnotatedSeq as) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();

        int[] cutPos = entry.getDownstreamCutPos();

        final int newStart = entry.getStart() + Math.min(cutPos[0], cutPos[1]);
        AnnotatedSeq frag = AsHelper.linearize(as, entry);
        frag.setRead(false);
        
        final int newEnd = entry.getStart() + Math.max(cutPos[0], cutPos[1]) - 1;
        ParentLoc parentLoc = new ParentLoc(newStart, as.getLength());       
        parentLoc.setTotalPos(as.getLength());
        frag.getParentLocSet().add(parentLoc);
        
        parentLoc = new ParentLoc(1, newEnd);
        parentLoc.setTotalPos(as.getLength());
        final int offset = as.getLength() - newStart + 1;
        parentLoc.setOffset(offset);
        frag.getParentLocSet().add(parentLoc);
        
        ret.add(frag);

        return ret;
    }

    private List<AnnotatedSeq> _digestLinear(RMap.Entry entry, AnnotatedSeq as) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();

        int[] cutPos = entry.getDownstreamCutPos();
        int cutType = entry.getDownstreamCutType();

        int end = entry.getStart() + Math.max(cutPos[0], cutPos[1]) - 1;
        AnnotatedSeq frag = AsHelper.subAs(as, 1, end);
        frag.setRead(false);
        ParentLoc parentLoc = new ParentLoc(1, end);
        parentLoc.setTotalPos(as.getLength());
        frag.getParentLocSet().add(parentLoc);
        if (cutType != REN.BLUNT) {
            // end overhang
            Overhang overhang = new Overhang();
            overhang.setName(entry.getName());
            overhang.setLength(entry.getOverhangLength());
            overhang.setFivePrime(cutType == REN.OVERHANG_5PRIME);
            overhang.setStrand(overhang.isThreePrime());
            frag.setEndOverhang(overhang);
        }
        ret.add(frag);

        int start = entry.getStart() + Math.min(cutPos[0], cutPos[1]);
        end = as.getLength();
        AnnotatedSeq frag2 = AsHelper.subAs(as, start, end);
        parentLoc = new ParentLoc(start, end);
        parentLoc.setTotalPos(as.getLength());
        frag2.getParentLocSet().add(parentLoc);
        if (cutType != REN.BLUNT) {
            // start overhang
            Overhang overhang = new Overhang();
            overhang.setName(entry.getName());
            overhang.setLength(entry.getOverhangLength());
            overhang.setFivePrime(cutType == REN.OVERHANG_5PRIME);
            overhang.setStrand(overhang.isFivePrime());
            frag2.setStartOverhang(overhang);
        }
        ret.add(frag2);
        return ret;
    }

    @Override
    public List<AnnotatedSeq> digest(AnnotatedSeq as, Collection<RMap.Entry> entries) {
        List<AnnotatedSeq> ret = _sortThenDigest(as, entries);
        RMap.Entry entry = null;
        if (entries.size() == 1) {
            Iterator<RMap.Entry> itr = entries.iterator();
            while (itr.hasNext()) {
                entry = itr.next();
            }
        }
        
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        
        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.Digestion);
        operation.setDate(new Date());        
        final String absolutePath = folderService.loadWithParents(as.getFolder().getHibernateId()).getAbsolutePath();
        Operation.Participant participant = new Operation.Participant(absolutePath + '\\' + as.getName(), true);
        operation.addParticipant(participant);

        for (int i = 0; i < ret.size(); i++) {
            if (entries.size() == 1) {
                ret.get(i).setName(String.format("Digestion of %s by %s fragment %d", ret.get(i).getName(), entry.getName(), i + 1));
            } else {
                ret.get(i).setName(String.format("Digestion of %s fragment %d", ret.get(i).getName(), i + 1));
            }
            ret.get(i).setOperation(operation.clone());
            ret.get(i).setRead(false);
            ret.get(i).setFolder(null);
        }
        return ret;
    }

    private List<AnnotatedSeq> _sortThenDigest(AnnotatedSeq as, Collection<RMap.Entry> entries) {
        List<RMap.Entry> entryList = new ArrayList<RMap.Entry>(entries);
        Collections.sort(entryList, new RMap.StartPosComparator());
        Iterator<RMap.Entry> itr = entries.iterator();
        return _digest(as, itr);
    }

    private List<AnnotatedSeq> _digest(AnnotatedSeq as, Iterator<RMap.Entry> itr) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        RMap.Entry first = null;
        RMap.Entry prev = null;
        while (itr.hasNext()) {
            RMap.Entry cur = itr.next();
            if (first == null) {
                first = cur;
            }
            if (prev != null) {
                ret.add(_digest(prev, cur, as));
                if (!itr.hasNext()) {
                    if (!as.isCircular()) {
                        List<AnnotatedSeq> frags = _digest(as, cur);
                        ret.add(frags.get(frags.size() - 1));
                    } else {
                        AnnotatedSeq digested = _digest(cur, first, as);
                        ret.add(digested);
                    }
                }
            } else {
                List<AnnotatedSeq> frags = _digest(as, cur);
                if (!as.isCircular()) {
                    if (itr.hasNext()) {
                        ret.add(frags.get(0));
                    } else {
                        ret.addAll(frags);
                    }
                } else {
                    if (!itr.hasNext()) {
                        ret.addAll(frags);
                    }
                }
            }
            prev = cur;
        }
        return ret;
    }

    @Override
    public List<AnnotatedSeq> digestSeparately(AnnotatedSeq as) {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        RMap rmap = as.getRmap();
        StringList entryNames = rmap.getEntryNames();
        entryNames.removeDuplicates();
        Iterator<String> nameItr = entryNames.iterator();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            Set<RMap.Entry> entries = rmap.getSortedEntries(new StringList(new String[]{name}));
            List<AnnotatedSeq> asList = _sortThenDigest(as, new ArrayList<RMap.Entry>(entries));
            for (int i = 0; i < asList.size(); i++) {
                AnnotatedSeq _as = asList.get(i);
                _as.setName(String.format("Digestion of %s by %s fragment 1", _as.getName(), name, i + 1));
                _as.setFolder(null);
            }
        }
        return ret;
    }
}
