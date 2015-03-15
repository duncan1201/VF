package com.gas.domain.core.orf;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.domain.core.orf.api.IORFService;
import com.gas.domain.core.orf.api.ORF;
import com.gas.domain.core.orf.api.ORFParam;
import com.gas.domain.core.orf.api.ORFResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IORFService.class)
public class ORFService implements IORFService {

    @Override
    public Map<Integer, LocList> find(ORFParam params) {
        List<String> errorMsg = params.validate();
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(errorMsg.get(0));
        }
        Map<Integer, LocList> ret = new HashMap<Integer, LocList>();
        Set<Integer> frames = params.getFrames();
        for (Integer frame : frames) {
            LocList locList = _findORFs(params, frame);
            ret.put(frame, locList);
        }
        return ret;
    }

    private LocList _findORFs(ORFParam params, int frame) {
        final int offset = Math.abs(frame) - 1;
        LocList ret = new LocList();
        Set<String> startCodons = params.getStartCodons();
        Set<String> stopCodons = params.getStopCodons();
        List<Loc> locStarts = new ArrayList<Loc>();
        LocList locStops = new LocList();
        String seq = params.getSequence();
        final int seqLength = seq.length();
        if (frame < 0) {
            seq = BioUtil.reverseComplement(seq);
        }
        if (params.isCircular()) {
            seq = seq + seq;
        }
        for (int i = 0; i + offset + 3 < seq.length(); i = i + 3) {
            String triplet = seq.substring(i + offset, i + offset + 3);
            if (startCodons.contains(triplet)) {
                if (params.isCircular()) {
                    if (i + offset < seq.length() / 2) {
                        locStarts.add(new Loc(i + offset + 1, i + offset + 3));
                    }
                } else {
                    locStarts.add(new Loc(i + offset + 1, i + offset + 3));
                }
            }
            if (stopCodons.contains(triplet)) {
                locStops.add(new Loc(i + offset + 1, i + offset + 3));
            }
        }

        Collections.sort(locStarts);
        Collections.sort(locStops);

        Loc lastOrf = null;
        Iterator<Loc> startItr = locStarts.iterator();
        while (startItr.hasNext()) {
            Loc start = startItr.next();
            if (lastOrf != null && start.getStart() <= lastOrf.getEnd()) {
                startItr.remove();
                continue;
            }

            Iterator<Loc> stopItr = locStops.iterator();
            while (stopItr.hasNext()) {
                Loc stop = stopItr.next();
                if (start.getEnd() < stop.getStart()) {
                    Loc locOrf = null;

                    locOrf = new Loc(start.getStart(), stop.getStart() - 1, frame > 0);

                    if (locOrf.getEnd() - locOrf.getStart() >= params.getMinLength() && !locStops.intersect(locOrf)) {
                        if (params.isStopCodonIncluded()) {
                            locOrf.setEnd(locOrf.getEnd() + 3);
                        }
                        if (frame < 0) {
                            locOrf.setStart(LocUtil.flip(locOrf.getStart(), seqLength));
                            locOrf.setEnd(LocUtil.flip(locOrf.getEnd(), seqLength));
                        }

                        ret.add(new Loc(Math.min(locOrf.getStart(), locOrf.getEnd()), Math.max(locOrf.getStart(), locOrf.getEnd()), locOrf.isStrand()));

                        lastOrf = locOrf;
                        break;
                    }
                } else {
                    stopItr.remove();
                }
            }
        }

        return ret;
    }

    @Override
    public ORFResult findORFResult(ORFParam params) {
        Map<Integer, LocList> locListMap = find(params);
        ORFResult ret = toORFResult(locListMap);
        ret.setOrfParams(params.clone());
        return ret;
    }

    private ORFResult toORFResult(Map<Integer, LocList> locListMap) {
        ORFResult ret = new ORFResult();
        Iterator<Integer> itr = locListMap.keySet().iterator();
        while (itr.hasNext()) {
            Integer frame = itr.next();
            LocList locList = locListMap.get(frame);
            Iterator<Loc> locItr = locList.iterator();
            while (locItr.hasNext()) {
                Loc loc = locItr.next();
                ORF orf = new ORF();
                orf.setStartPos(loc.getStart());
                orf.setEndPos(loc.getEnd());
                orf.setLength(Math.abs(loc.getEnd() - loc.getStart() + 1));
                orf.setStrand(loc.isStrand());
                orf.setFrame(frame);

                ret.getOrfSet().add(orf);
            }
        }
        return ret;
    }
}