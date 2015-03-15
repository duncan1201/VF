/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.service.api;

import com.gas.domain.core.msa.clustalw.ClustalTreeParam;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;

/**
 *
 * @author dq
 */
public interface IClustalwService {

    public enum STATE {

        VALID(),
        INFILE_EMPTY(),
        PROFILE1_EMPTY(),
        PROFILE2_EMPTY(),
        TYPE_EMPTY(),
        OUT_FILE_EMPTY(),
        OUTPUT_EMPTY(),
        INVALID();

        STATE() {
        }
    };

    public enum CLUSTERING {

        NJ, UPGMA
    };

    public enum OUTPUTTREE {

        NJ, PHYLIP, DIST, NEXUS
    };


    MSA msa(ClustalwParam msaParams);

    Newick phylogeneticTree(ClustalTreeParam params);

    STATE validate(ClustalTreeParam params);

    STATE validate(ClustalwParam params);
}
