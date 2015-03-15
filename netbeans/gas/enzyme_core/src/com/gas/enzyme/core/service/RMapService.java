/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.service;

import com.gas.common.ui.core.LocList;
import com.gas.domain.core.as.Feture;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.SimpleMatchResult;
import com.gas.common.ui.util.BioUtil;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.core.ren.IRMapService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dunqiang
 */
@ServiceProvider(service = IRMapService.class)
public class RMapService implements IRMapService {

    /*
     * @param sequence the target "DNA", not "RNA", sequence 
     */
    @Override
    public RMap findRM(String sequence, boolean circular, String renListName, RENSet rens, Integer minOccurence, Integer maxOccurence, LocList locList, Boolean allow) {
        if (minOccurence != null && minOccurence < 1) {
            throw new IllegalArgumentException("Must set MinOccurence > 1");
        } else if (minOccurence != null && maxOccurence != null && minOccurence > maxOccurence) {
            throw new IllegalArgumentException("maxOccurence must >= minOccurence");
        }
        sequence = sequence.toLowerCase();
        sequence = sequence.replaceAll("u", "t");

        RMap ret = new RMap();
        Iterator<REN> renItr = rens.iterator();

        while (renItr.hasNext()) {
            REN ren = renItr.next();
            List<RMap.Entry> entries = _findRM(sequence, ren);
            if (circular) {
                entries.addAll(findRMCrossingOrigin(sequence, ren));
            }
            ret.getEntries().addAll(entries);
        }

        // filter out the restriction sites that are out of range
        ret.getAnalysisDetails().setTotalCandidates(ret.getEntries().size());
        Iterator<RMap.Entry> itr = ret.getEntries().iterator();        
        while(itr.hasNext()){
            RMap.Entry entry = itr.next();
            if (locList == null || locList.isEmpty() || allow == null) {
                // ok;
            } else if (Boolean.TRUE.equals(allow) && locList.intersect(new Loc(entry.getStart(), entry.getEnd()))) {
                // ok;
            } else if (Boolean.FALSE.equals(allow) && !locList.intersect(new Loc(entry.getStart(), entry.getEnd()))) {
                // ok;
            } else{
                ret.getAnalysisDetails().incrementOutOfRangeCount();
                itr.remove();
            }
        }

        if (minOccurence != null && maxOccurence != null) {
            ret.filter(minOccurence, maxOccurence);
        }
        ret.getInputParams().setMinOccurence(minOccurence);
        ret.getInputParams().setMaxOccurence(maxOccurence);
        ret.getInputParams().setRenListName(renListName);
        ret.getInputParams().setRenNames(rens.getNames());
        ret.getInputParams().setAllow(allow);
        if (!locList.isEmpty()) {
            ret.getInputParams().setStartPos(locList.get(0).getStart());
            ret.getInputParams().setEndPos(locList.get(0).getEnd());
        }
        return ret;
    }

    private List<RMap.Entry> findRMCrossingOrigin(String sequence, REN ren) {
        List<RMap.Entry> ret = new ArrayList<RMap.Entry>();
        String recogSite = ren.getRecognitionSite();

        String region = sequence.substring(sequence.length() - recogSite.length() + 1, sequence.length()) + sequence.substring(0, recogSite.length() - 1);

        List<MatchResult> matchResultsForward = _findMatchResultCrossingOrigin(sequence.length(), region, Pattern.compile(ren.getForwardRegex(), Pattern.CASE_INSENSITIVE));

        List<MatchResult> matchResultsReverse = _findMatchResultCrossingOrigin(sequence.length(), region, Pattern.compile(ren.getReverseRegex(), Pattern.CASE_INSENSITIVE));

        List<MatchResult> matchResults = new ArrayList<MatchResult>();
        matchResults.addAll(matchResultsForward);
        matchResults.addAll(matchResultsReverse);

        for (MatchResult matchResult : matchResults) {
            int start = matchResult.start();
            int end = matchResult.end();

            RMap.Entry entry = createEntry(ren, start, end);
            ret.add(entry);
        }
        return ret;
    }

    private List<MatchResult> _findMatchResultCrossingOrigin(int seqLength, String region, Pattern pattern) {

        List<MatchResult> ret = new ArrayList<MatchResult>();

        Matcher matcher = pattern.matcher(region);
        boolean found = matcher.find();
        while (found) {
            ret.add(new SimpleMatchResult(seqLength - (region.length() / 2 - matcher.start()), matcher.end() - region.length() / 2));
            found = matcher.find();
        }

        return ret;
    }

    /**
     * start, end; start, end
     */
    private List<MatchResult> _findMatchResult(String sequence, Pattern pattern) {
        List<MatchResult> ret = new ArrayList<MatchResult>();

        Matcher matcher = pattern.matcher(sequence);
        boolean found = matcher.find();
        while (found) {

            ret.add(matcher.toMatchResult());

            found = matcher.find();
        }
        return ret;
    }

    private List<RMap.Entry> _findRM(String sequence, REN ren) {
        List<RMap.Entry> ret = new ArrayList<RMap.Entry>();

        List<MatchResult> matchResultsForward = _findMatchResult(sequence, Pattern.compile(ren.getForwardRegex(), Pattern.CASE_INSENSITIVE));

        List<MatchResult> matchResultsReverse = _findMatchResult(sequence, Pattern.compile(ren.getReverseRegex(), Pattern.CASE_INSENSITIVE));

        List<MatchResult> matchResults = new ArrayList<MatchResult>();
        matchResults.addAll(matchResultsForward);
        matchResults.addAll(matchResultsReverse);

        for (MatchResult matchResult : matchResults) {
            int start = matchResult.start();
            int end = matchResult.end();

            RMap.Entry entry = createEntry(ren, start, end);
            ret.add(entry);
        }
        return ret;
    }

    private RMap.Entry createEntry(REN ren, int start, int end) {

        RMap.Entry entry = new RMap.Entry();
        entry.setName(ren.getName());

        entry.setUpstreamCutPos(ren.getUpstreamCutPos());
        entry.setDownstreamCutPos(ren.getDownstreamCutPos());
        entry.setDownstreamCutType(ren.getDownstreamEndType());
        entry.setOverhang(ren.getOverhang());
        entry.setVisual(ren.getVisual());
        entry.setStart(start + 1);
        entry.setEnd(end);
        entry.setSeq(ren.getRecognitionSite());

        return entry;
    }

    @Override
    public List<Feture> toFeatures(RMap rmap) {
        return rmap.toFetures();
    }
}
