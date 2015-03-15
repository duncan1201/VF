/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ace;

/**
 *
 * @author dq
 */
public class ACETags {

    public static final String AS_TAG = "AS ";
    /*
     CO <contig name> <# of bases> <# of reads in contig> <# of base segments in contig> <U or C>

     This defines the contig.  The U or C indicates whether the contig has
     been complemented from the way phrap originally created it.  Thus this
     is always U for an ace file created by phrap.

     The contig sequence follows.  It includes pads--"*" characters which
     are inserted by phrap in order to make room for some read that has an
     extra base at that position.  (Note: any position which counts the *'s is
     referred to as a "padded position".  A position that does not count
     *'s is referred to as "unpadded position".)     
     */
    public static final String CO_TAG = "CO ";
    /*
     BQ

     This starts the list of base qualities for the unpadded consensus
     bases.  (NB: annoyingly, no qualities are given for *'s in the
     consensus.)  The contig is the one from the previous CO, hence no name
     is needed here.     
     */
    public static final String BQ_TAG = "BQ";
    /*
     AF <read name> <C or U> <padded start consensus position>

     This defines the location of the read within the contig.
     C or U means complemented or uncomplemented.  
     <padded start consensus position> means the position of the
     beginning of the read, in terms of consensus bases which start at 1
     and do count *'s.      
     */
    public static final String AF_TAG = "AF ";
    /*
     BS <padded start consensus position> <padded end consensus position> <read name>

     The BS line (base segment) indicates which read phrap has chosen to be
     the consensus at a particular position.

     BS lines are now optional since they don't make much sense for
     assemblers other than phrap.

     If you choose to to write BS lines, I suggest you choose any read
     which matches the consensus perfectly over the stretch of bases.
     There must not be any two BS lines that intersect.  Each unpadded base
     must be included in some BS line.     
     */
    public static final String BS_TAG = "BS ";
    /*
     RD <read name> <# of padded bases> <# of whole read info items> <# of read tags>
     Below RD is the sequence of bases for the read.  The sequence includes
     *'s and is in the orientation that phrap needed to align it against
     the consensus (thus it might be complemented from the direction it was
     sequenced).       
     */
    public static final String RD_TAG = "RD ";
    /*
     QA <qual clipping start> <qual clipping end> <align clipping start> <align clipping end>

     This line indicates which part of the read is the high quality segment
     (if there is any) and which part of the read is aligned against the
     consensus.  These positions are offsets (and count *'s) from the left
     end of the read (left, as shown in Consed).  Hence for bottom strand
     reads, the offsets are from the end of the read.  The offsets are
     1-based.  That is, if the left-most base is in the aligned,
     high-quality region, <qual clipping start> = 1 and <align clipping
     start> = 1 (not zero).  If the entire read is low quality, then <qual
     clipping start> and <qual clipping end> will both be -1.     
     */
    public static final String QA_TAG = "QA ";
    /*
     DS CHROMAT_FILE: <name of chromat file> PHD_FILE: <name of phd file> TIME: <date/time of the phd file> CHEM: <prim, term, unknown, etc> DYE: <usually ET, big, etc> TEMPLATE: <template name> DIRECTION: <fwd or rev>

     This line must contain information that matches the phd file.  If you
     are writing an ace file, pay particular attention to this line.  Make
     sure that Consed can read your ace file without reporting any errors.

     For next-gen reads, without chromats or phd files, the DS lines look
     like this:

     DS VERSION: 1 TIME: Wed Dec 24 11:21:50 2008 CHEM: solexa

     with just these 3 pieces of information: VERSION, TIME, and CHEM     
     */
    public static final String DS_TAG = "DS ";
}
