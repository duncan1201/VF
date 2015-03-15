/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.domain.core.aln.Aln;
import com.gas.domain.core.nexus.BlkStmt;
import com.gas.domain.core.nexus.api.Blk;
import com.gas.domain.core.nexus.api.Nexus;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dq
 */
public class MSAHelper {

    protected static Aln toAln(MSA msa) {
        Aln ret = new Aln();

        return ret;
    }

    public static void touchAll(MSA msa) {
        msa.getConsensusParam().isIgnoreGaps();
        if (msa.getSeqLogoParam() != null) {
            msa.getSeqLogoParam().isSmallSampleCorrection();
        }
        Iterator<MSA.Entry> itr = msa.getEntries().iterator();
        while (itr.hasNext()) {
            itr.next();
        }
        if (msa.getClustalwParam() != null) {
            msa.getClustalwParam().touchAll();
        }
        msa.getMsaSetting().touchAll();
        if(msa.getMuscleParam() != null){
            msa.getMuscleParam().touchAll();
        }
        if(msa.getVfMsaParam() != null){
            msa.getVfMsaParam().touchAll();
        }
        if (msa.getRichConsensus() != null) {
            msa.getRichConsensus().touchAll();
        }
    }
    
    public static Nexus toNexus(MSA msa) {
        Nexus ret = new Nexus();
        Set<MSA.Entry> entries = msa.getEntries();
        Blk blk = toBlk(entries, msa.isDnaByGuess());
        ret.getBlks().add(blk);

        blk = createTreeBlock(msa);
        if (blk != null) {
            ret.getBlks().add(blk);
        }

        return ret;
    }

    private static Blk createTreeBlock(MSA msa) {
        Blk blk = null;
        if (msa.getNewickStr() != null) {
            blk = new Blk("trees");
            BlkStmt stmt = new BlkStmt("tree");
            stmt.setArgs(String.format("tree_1=%s", msa.getNewickStr()));
            blk.getStmts().add(stmt);
        }
        return blk;
    }

    private static Blk toBlk(Set<MSA.Entry> entries, boolean dna) {
        Blk ret = new Blk();
        ret.setName("characters");

        BlkStmt stmt = new BlkStmt("matrix");
        StringBuilder sb = new StringBuilder();
        Iterator<MSA.Entry> itr = entries.iterator();
        while (itr.hasNext()) {
            MSA.Entry entry = itr.next();
            sb.append(entry.getName());
            sb.append(' ');
            sb.append(entry.getData());
            if (itr.hasNext()) {
                sb.append('\n');
            }
        }
        stmt.setArgs(sb.toString());
        ret.getStmts().add(stmt);

        if (!entries.isEmpty()) {
            MSA.Entry entry = entries.iterator().next();
            int lengthData = entry.getData().length();
            stmt = new BlkStmt("dimensions");
            stmt.setArgs(String.format("nchar=%d", lengthData));
            ret.getStmts().add(stmt);
        }

        stmt = new BlkStmt("format");
        stmt.setArgs(String.format("datatype=%s", dna ? "dna" : "protein"));
        ret.getStmts().add(stmt);
        return ret;
    }    
}
