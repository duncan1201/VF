package com.gas.domain.core.as;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import java.util.List;
import java.util.Set;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.LocationTools;
import org.biojavax.bio.seq.CompoundRichLocation;
import org.biojavax.bio.seq.Position;
import org.biojavax.bio.seq.RichLocation;
import org.biojavax.bio.seq.SimpleRichLocation;

public class LocationHelper {

    public static Location toLocation(Lucation lucation) {

        Location ret;
        if (lucation.isContiguous()) {
            ret = LocationTools.makeLocation2(lucation.getStart(), lucation.getEnd(), lucation.isFuzzyStart(), lucation.isFuzzyEnd(), lucation.getType());
            ret = RichLocation.Tools.enrich(ret);
            SimpleRichLocation rl = (SimpleRichLocation) ret;
            Boolean strand = lucation.getStrand();
            if (strand == null) {
                rl.setStrand(RichLocation.Strand.UNKNOWN_STRAND);
            } else if (strand) {
                rl.setStrand(RichLocation.Strand.POSITIVE_STRAND);
            } else {
                rl.setStrand(RichLocation.Strand.NEGATIVE_STRAND);
            }

        } else {
            List<Pozition> pozitions = lucation.getPozitions();
            List<Location> locs = toLocations(pozitions);
            ret = RichLocation.Tools.construct(locs);
            RichLocation richLoc = (RichLocation) ret;
            if (lucation.getStrand() != null) {
                if (lucation.getStrand()) {
                    richLoc.setStrand(RichLocation.Strand.POSITIVE_STRAND);
                } else {
                    richLoc.setStrand(RichLocation.Strand.NEGATIVE_STRAND);
                }
            } else {
                richLoc.setStrand(RichLocation.Strand.UNKNOWN_STRAND);
            }
            final String term = lucation.getTerm();
            if (CompoundRichLocation.getJoinTerm().toString().equals(term)) {
                richLoc.setTerm(CompoundRichLocation.getJoinTerm());
            } else if (CompoundRichLocation.getOrderTerm().toString().equals(term)) {
                richLoc.setTerm(CompoundRichLocation.getOrderTerm());
            }
        }
        return ret;
    }

    private static Location toLocation(Pozition poz) {
        Location ret = LocationTools.makeLocation2(poz.getStart(), poz.getEnd(), poz.isFuzzyStart(), poz.isFuzzyEnd(), poz.getType());
        return ret;
    }

    private static List<Location> toLocations(List<Pozition> pozitions) {
        List<Location> ret = new ArrayList<Location>();
        Iterator<Pozition> itr = pozitions.iterator();
        while (itr.hasNext()) {
            Pozition pozition = itr.next();
            Location location = toLocation(pozition);
            RichLocation richLoc = RichLocation.Tools.enrich(location);
            richLoc.setRank(pozition.getRank());
            Boolean strand = pozition.getStrand();
            if (strand == null) {
                richLoc.setStrand(RichLocation.Strand.UNKNOWN_STRAND);
            } else if (strand) {
                richLoc.setStrand(RichLocation.Strand.POSITIVE_STRAND);
            } else if (!strand) {
                richLoc.setStrand(RichLocation.Strand.NEGATIVE_STRAND);
            }
            ret.add(richLoc);
        }
        return ret;
    }

    public static Lucation toLucation(RichLocation richLoc) {
        Lucation ret = new Lucation();
        boolean contiguous = richLoc.isContiguous();
        if (contiguous) {
            Position minPos = richLoc.getMinPosition();
            Position maxPos = richLoc.getMaxPosition();

            Pozition poz = new Pozition();
            poz.setStart(minPos.getStart());
            poz.setFuzzyStart(minPos.getFuzzyStart());
            poz.setType(minPos.getType());

            poz.setEnd(maxPos.getEnd());
            poz.setFuzzyEnd(maxPos.getFuzzyEnd());
            poz.setType(maxPos.getType());

            ret.addPozition(poz);
            if (richLoc.getStrand().getName().equals("+")) {
                ret.setStrand(true);
            } else if (richLoc.getStrand().getName().equals("-")) {
                ret.setStrand(false);
            } else {
                throw new IllegalArgumentException(String.format("Unknown strand '%s'", richLoc.getStrand().getName()));
            }

        } else {
            Iterator<Location> locs = richLoc.blockIterator();
            while (locs.hasNext()) {
                SimpleRichLocation loc = (SimpleRichLocation) locs.next();
                Position minPos = loc.getMinPosition();
                Position maxPos = loc.getMaxPosition();
                if (loc.isContiguous()) {
                    Pozition poz = toPozition(loc);
                    poz.setFuzzyStart(minPos.getFuzzyStart());
                    poz.setFuzzyEnd(maxPos.getFuzzyEnd());

                    ret.addPozition(poz);
                    if (ret.getStrand() == null && poz.getStrand() != null) {
                        ret.setStrand(poz.getStrand());
                    }
                } else {
                    throw new IllegalArgumentException("Join location should be contiguous");
                }
            }

            ret.setTerm(richLoc.getTerm().toString());

            if (richLoc.getStrand().intValue() == 1) {
                ret.setStrand(true);
            } else if (richLoc.getStrand().intValue() == -1) {
                ret.setStrand(false);
            }
        }
        return ret;
    }

    private static Pozition toPozition(Location loc) {
        if (!loc.isContiguous()) {
            throw new IllegalArgumentException("Must be contiguous");
        }
        Pozition ret = new Pozition();
        ret.setStart(loc.getMin());
        ret.setEnd(loc.getMax());
        ret.setFuzzyStart(false);
        ret.setFuzzyEnd(false);
        ret.setType("..");

        if (loc instanceof RichLocation) {
            RichLocation rLoc = (RichLocation) loc;
            ret.setRank(rLoc.getRank());
            if (rLoc.getStrand().intValue() == 1) {
                ret.setStrand(true);
            } else if (rLoc.getStrand().intValue() == -1) {
                ret.setStrand(false);
            } else if (rLoc.getStrand().intValue() == 0) {
                ret.setStrand(null);
            }
        }

        return ret;
    }
}
