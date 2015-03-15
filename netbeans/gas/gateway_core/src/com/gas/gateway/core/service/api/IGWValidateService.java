/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.domain.core.as.AnnotatedSeq;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IGWValidateService {
    
    public static String[] PRO1_INSERT = {"B1-B2"};
    public static String[] PRO2_INSERT = {"B1-B5", "B5-B2"};
    public static String[] PRO3_INSERT = {"B1-B4", "B4-B3", "B3-B2"};
    public static String[] PRO4_INSERT = {"B1-B5", "B5-B4", "B4-B3", "B3-B2"};
    public static String[] FRAG3_INSERT = {"B4-B1", "B1-B2", "B2-B3"};
    
    public static String[] PRO1_ENTRY = {"L1-L2"};
    public static String[] PRO2_ENTRY = {"L1-R5", "L5-L2"};
    public static String[] PRO3_ENTRY = {"L1-L4", "R4-R3", "L3-L2"};
    public static String[] RPO4_ENTRY = {"L1-R5", "L5-L4", "R4-R3", "L3-L2"};
    public static String[] FRAG3_ENTRY = {"L4-R1", "L1-L2", "R2-L3"};
    
    public static String[] PRO1_DONOR = {"P1-P2"};
    public static String[] PRO2_DONOR = {"P1-P5R", "P5-P2"};
    public static String[] PRO3_DONOR = {"P1-P4", "P4R-P3R", "P3-P2"};
    public static String[] PRO4_DONOR = {"P1-P5R", "P5-P4", "P4R-P3R", "P3-P2"};
    public static String[] FRAG3_DONOR = {"P4-P1R", "P1-P2", "P2R-P3"};
    
    public static String[] PRO1_ENTRY_CLONES = {"L1-L2"};
    public static String[] PRO2_ENTRY_CLONES = {"L1-R5", "L5-L2"};
    public static String[] PRO3_ENTRY_CLONES = {"L1-L4", "R4-R3", "L3-L2"};
    public static String[] PRO4_ENTRY_CLONES = {"L1-R5", "L5-L4", "R4-R3", "L3-L2"};
    public static String[] FRAG3_ENTRY_CLONES = {"L4-R1", "L1-L2", "R2-L3"};        
    
    Integer indexOfDestR1R2Vector(List<AnnotatedSeq> seqs);
    Integer indexOfDestVector(List<AnnotatedSeq> seqs);
    Integer indexOfDonorVector(List<AnnotatedSeq> seqs);
    boolean isDonorVector(AnnotatedSeq donor);
    boolean isDestVector(AnnotatedSeq dest);
    List<AnnotatedSeq> getInserts(List<AnnotatedSeq> seqs);
    List<AnnotatedSeq> getDonors(List<AnnotatedSeq> seqs);
    GW_STATE validateBP(List<AnnotatedSeq> seqs);
    GW_STATE validateLR(List<AnnotatedSeq> seqs);
    GW_STATE validateAddAttbSites(AnnotatedSeq seq);
    GW_STATE validateOneClick(List<AnnotatedSeq> seqs);
}
