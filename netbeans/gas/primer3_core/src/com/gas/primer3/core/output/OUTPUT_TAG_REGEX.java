package com.gas.primer3.core.output;

public class OUTPUT_TAG_REGEX {

    public static final String PRIMER_LEFT_EXPLAIN = "PRIMER_LEFT_EXPLAIN";
    public static final String PRIMER_INTERNAL_EXPLAIN = "PRIMER_INTERNAL_EXPLAIN";
    public static final String PRIMER_RIGHT_EXPLAIN = "PRIMER_RIGHT_EXPLAIN";
    public static final String PRIMER_PAIR_EXPLAIN = "PRIMER_PAIR_EXPLAIN";
    public static final String PRIMER_LEFT_NUM_RETURNED = "PRIMER_LEFT_NUM_RETURNED";
    public static final String PRIMER_LEFT_SEQ = "PRIMER_LEFT_[0-9]+_SEQUENCE";
    public static final String PRIMER_INTERNAL_SEQ = "PRIMER_INTERNAL_[0-9]+_SEQUENCE";    
    public static final String PRIMER_RIGHT_SEQ = "PRIMER_RIGHT_[0-9]+_SEQUENCE";
    

    public static final String PRIMER_LEFT = "PRIMER_LEFT_[0-9]+|PRIMER_LEFT";
    public static final String PRIMER_RIGHT = "PRIMER_RIGHT_[0-9]+|PRIMER_RIGHT";
    public static final String PRIMER_INTERNAL = "PRIMER_INTERNAL_[0-9]+|PRIMER_INTERNAL";        
    
    public static final String PRIMER_LEFT_GC_PERCENT = "PRIMER_LEFT_[0-9]+_GC_PERCENT";
    public static final String PRIMER_RIGHT_GC_PERCENT = "PRIMER_RIGHT_[0-9]+_GC_PERCENT";
    public static final String PRIMER_INTERNAL_GC_PERCENT = "PRIMER_INTERNAL_[0-9]+_GC_PERCENT";
    
    public static final String PRIMER_LEFT_HAIRPIN = "PRIMER_LEFT_[0-9]+_HAIRPIN_TH";
    public static final String PRIMER_INTERNAL_HAIRPIN = "PRIMER_INTERNAL_[0-9]+_HAIRPIN_TH";
    public static final String PRIMER_RIGHT_HAIRPIN = "PRIMER_RIGHT_[0-9]+_HAIRPIN_TH";
    
    public static final String PRIMER_LEFT_PROBLEMS = "PRIMER_LEFT_[0-9]+_PROBLEMS";    
    public static final String PRIMER_INTERNAL_PROBLEMS = "PRIMER_INTERNAL_[0-9]+_PROBLEMS";
    public static final String PRIMER_RIGHT_PROBLEMS = "PRIMER_RIGHT_[0-9]+_PROBLEMS";
    
    public static final String PRIMER_LEFT_SELF_ANY = "PRIMER_LEFT_[0-9]+_SELF_ANY|PRIMER_LEFT_[0-9]+_SELF_ANY_TH";
    public static final String PRIMER_RIGHT_SELF_ANY = "PRIMER_RIGHT_[0-9]+_SELF_ANY|PRIMER_RIGHT_[0-9]+_SELF_ANY_TH";
    public static final String PRIMER_INTERNAL_SELF_ANY = "PRIMER_INTERNAL_[0-9]+_SELF_ANY|PRIMER_INTERNAL_[0-9]+_SELF_ANY_TH";
    
    public static final String PRIMER_LEFT_SELF_END = "PRIMER_LEFT_[0-9]+_SELF_END|PRIMER_LEFT_[0-9]+_SELF_END_TH";
    public static final String PRIMER_RIGHT_SELF_END = "PRIMER_RIGHT_[0-9]+_SELF_END|PRIMER_RIGHT_[0-9]+_SELF_END_TH";
    public static final String PRIMER_INTERNAL_SELF_END = "PRIMER_INTERNAL_[0-9]+_SELF_END|PRIMER_INTERNAL_[0-9]+_SELF_END_TH";    
    
    public static final String PRIMER_LEFT_TM = "PRIMER_LEFT_[0-9]+_TM";
    public static final String PRIMER_INTERNAL_TM = "PRIMER_INTERNAL_[0-9]+_TM";
    public static final String PRIMER_RIGHT_TM = "PRIMER_RIGHT_[0-9]+_TM";        
    
    public static final String PRIMER_PAIR_COMPL_ANY = "PRIMER_PAIR_[0-9]+_COMPL_ANY_TH";
    public static final String PRIMER_PAIR_COMPL_END = "PRIMER_PAIR_[0-9]+_COMPL_END_TH";
    /*
     * PRIMER_PAIR_PRODUCT_SIZE necessary?
     */
    public static final String PRIMER_PAIR_PRODUCT_SIZE = "PRIMER_PAIR_[0-9]+_PRODUCT_SIZE";
    public static final String PRIMER_PRODUCT_SIZE = "PRIMER_PRODUCT_SIZE_[0-9]";
    
    public static final String PRIMER_TASK = "PRIMER_TASK";
    
    public static final String SEQUENCE_TEMPLATE_REGEX = "SEQUENCE_TEMPLATE";
    public static final String SEQUENCE_TARGET_REGEX = "SEQUENCE_TARGET";
    public static final String SEQUENCE_INTERNAL_EXCLUDED_REGION = "SEQUENCE_INTERNAL_EXCLUDED_REGION";
}
