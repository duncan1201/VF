/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package com.gas.domain.core.ren;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.Unicodes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.biojava.BioJavaHelper;
import org.biojava.bio.BioError;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.FiniteAlphabet;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.MotifTools;
import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;

/**
 * <code>RestrictionEnzyme</code> represents a restriction enzyme according to
 * the REBASE standard. The cut positions are indicated relative to the 5' end
 * of the recognition site and occur downstream of the given residue. Note that
 * some enzymes cut in more than one position and that cut positions may occur
 * outside the recognition site.
 *
 * @author Keith James
 * @author George Waldon
 * @since 1.3
 */
public final class REN implements Serializable {

    /**
     * <code>CUT_SIMPLE</code> a cut type where the enzyme cuts in one position
     * relative to the recognition site. This covers the vast majority of cases.
     */
    public static final int CUT_SIMPLE = 0;
    /**
     * <code>CUT_COMPOUND</code> a cut type where the enzyme cuts in two
     * positions relative to the recognition site.
     */
    public static final int CUT_COMPOUND = 1;
    /**
     * <code>OVERHANG_5PRIME</code> the sticky end type created by enzymes which
     * leave a 5' overhang (e.g. a stretch of single-stranded DNA with a free 5'
     * end).
     */
    public static final int OVERHANG_5PRIME = 0;
    /**
     * <code>OVERHANG_3PRIME</code> the sticky end type created by enzymes which
     * leave a 3' overhang (e.g. a stretch of single-stranded DNA with a free 3'
     * end).
     */
    public static final int OVERHANG_3PRIME = 1;
    /**
     * <code>BLUNT</code> the end type created by enzymes which leave a blunt
     * end.
     */
    public static final int BLUNT = 2;
    private Integer hibernateId;// for hibernate use only
    protected String name;
    protected String recognitionSite;
    protected int cutType;
    protected int[] downstreamCutPos;
    protected int[] upstreamCutPos;
    private double size = 0.0; // don't need to persist
    protected String forwardRegex;
    protected String reverseRegex;
    private String summary;

    // for hibernate use
    public REN() {
    }

    /**
     * Creates a new
     * <code>RestrictionEnzyme</code> which cuts within or downstream of the
     * recognition site. The cut position indices are <strong>always</strong> in
     * the same coordinate space as the recognition site.
     * <code>RestrictionEnzyme</code>s are immutable.
     *
     * @param name a <code>String</code> such as EcoRI.
     * @param site a <code>SymbolList</code> recognition site.
     * @param dsForward an <code>int</code> index in the forward strand (the
     * strand conventionally written <strong>5'</strong>-3') of the recognition
     * site at which the cut occurs. The cut occurs between this base and the
     * following one.
     * @param dsReverse an <code>int</code> index in the reverse strand (the
     * strand conventionally written <strong>3'</strong>-5') of the recognition
     * site at which the cut occurs. The cut occurs between this base and the
     * following one.
     *
     * @exception IllegalAlphabetException if an error occurs.
     */
    public REN(String name, String site,
            int dsForward, int dsReverse)
            throws IllegalAlphabetException {
        this(name, site,
                null,
                new int[]{dsForward, dsReverse});
        cutType = CUT_SIMPLE;
    }

    public REN(REN ren) {
        setName(ren.getName());
        setRecognitionSite(ren.getRecognitionSite());
        setCutType(getCutType());

        setForwardRegex(ren.getForwardRegex());
        setReverseRegex(ren.getReverseRegex());
        setSummary(ren.getSummary());
        setDownstreamCutPos(CommonUtil.copyOf(ren.getDownstreamCutPos()));
        setUpstreamCutPos(CommonUtil.copyOf(ren.getUpstreamCutPos()));
    }

    /*
     * For hibernate use only
     */
    public Integer getHibernateId() {
        return hibernateId;
    }

    public String getOverhang() {
        String ret = null;

        int[] cutPos = getDownstreamCutPos();
        int downEndType = getDownstreamEndType();
        if (downEndType == REN.OVERHANG_3PRIME || downEndType == REN.OVERHANG_5PRIME) {
            int min = Math.min(cutPos[0], cutPos[1]);
            int max = Math.max(cutPos[0], cutPos[1]);
            if (max <= recognitionSite.length()) {
                ret = recognitionSite.substring(min, max);
            }
        }

        return ret;
    }

    /*
     * For hibernate use only
     */
    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    /**
     * Creates a new
     * <code>RestrictionEnzyme</code> of the unusual type which cuts both
     * upstream and downstream of its recognition site. The cut position indices
     * are <strong>always</strong> in the same coordinate space as the
     * recognition site.
     *
     * @param name a <code>String</code> such as Bsp24I.
     * @param site a <code>SymbolList</code> recognition site.
     * @param usForward an <code>int</code> index in the forward strand (the
     * strand conventionally written <strong>5'</strong>-3' upstream of the
     * recognition site at which the cut occurs. The cut occurs between this
     * base and the following one.
     * @param usReverse an <code>int</code> index in the reverse strand (the
     * strand conventionally written <strong>3'</strong>-5) upstream of the
     * recognition site at which the cut occurs. The cut occurs between this
     * base and the following one.
     * @param dsForward an <code>int</code> index in the forward strand (the
     * strand conventionally written <strong>5'</strong>-3') downstream of the
     * recognition site at which the cut occurs. The cut occurs between this
     * base and the following one.
     * @param dsReverse an <code>int</code> index in the reverse strand (the
     * strand conventionally written <strong>3'</strong>-5') downstream of the
     * recognition site at which the cut occurs. The cut occurs between this
     * base and the following one.
     *
     * @exception IllegalAlphabetException if an error occurs.
     */
    public REN(String name, String site,
            int usForward, int usReverse,
            int dsForward, int dsReverse)
            throws IllegalAlphabetException {
        this(name, site,
                new int[]{usForward, usReverse},
                new int[]{dsForward, dsReverse});
        cutType = CUT_COMPOUND;
    }

    /**
     * Creates a new
     * <code>RestrictionEnzyme</code>.
     *
     * @param name a <code>String</code> name.
     * @param site a <code>SymbolList</code> site.
     * @param usCutPositions an <code>int []</code> array of optional upstream
     * indices.
     * @param dsCutPositions an <code>int []</code> array of downstream indices.
     *
     * @exception IllegalAlphabetException if an error occurs.
     */
    private REN(String name, String site,
            int[] usCutPositions,
            int[] dsCutPositions) {
        SymbolList siteSL = null;
        try {
            siteSL = DNATools.createDNA(site);
        } catch (IllegalSymbolException ex) {
            throw new IllegalArgumentException("RestrictionEnzyme site can only be a DNA SymbolList.");
        }


        this.name = name;
        this.recognitionSite = site;
        this.upstreamCutPos = usCutPositions;
        this.downstreamCutPos = dsCutPositions;

        forwardRegex = MotifTools.createRegex(siteSL);

        try {
            reverseRegex =
                    MotifTools.createRegex(DNATools.reverseComplement(siteSL));
        } catch (IllegalAlphabetException iae) {
            throw new BioError("RestrictionEnzyme site was not composed of a complementable Alphabet", iae);
        }

        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(" ");

        if (usCutPositions != null) {
            sb.append("(");
            sb.append(usCutPositions[0]);
            sb.append("/");
            sb.append(usCutPositions[1]);
            sb.append(") ");
        }

        try {
            for (int i = 1; i <= siteSL.length(); i++) {
                sb.append(Character.toUpperCase(DNATools.dnaToken(siteSL.symbolAt(i))));
            }
        } catch (IllegalSymbolException ise) {
            throw new BioError("RestrictionEnzyme site contained non-DNA Symbol", ise);
        }

        sb.append(" (");
        sb.append(dsCutPositions[0]);
        sb.append("/");
        sb.append(dsCutPositions[1]);
        sb.append(")");

        summary = sb.substring(0);
    }

    /**
     * <code>getName</code> returns the enzyme name.
     *
     * @return a <code>String</code>.
     */
    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    /**
     * <code>getRecognitionSite</code> returns the forward strand of the
     * recognition site.
     *
     * @return a <code>SymbolList</code>.
     */
    public String getRecognitionSite() {
        return recognitionSite;
    }

    public void setRecognitionSite(String site) {
        this.recognitionSite = site;
    }

    /**
     * <code>getForwardRegex</code> returns a regular expression which matches
     * the forward strand of the recognition site.
     *
     * @return a <code>String</code>.
     */
    public String getForwardRegex() {
        return forwardRegex;
    }

    // for hibernate use
    public void setForwardRegex(String reg) {
        this.forwardRegex = reg;
    }

    /**
     * <code>getReverseRegex</code> returns a regular expression which matches
     * the reverse strand of the recognition site.
     *
     * @return a <code>String</code>.
     */
    public String getReverseRegex() {
        return reverseRegex;
    }

    public void setReverseRegex(String reg) {
        this.reverseRegex = reg;
    }

    /**
     * <code>isPalindromic</code> returns true if the recognition site is
     * palindromic.
     *
     * @return a <code>boolean</code>.
     */
    public boolean isPalindromic() {
        return forwardRegex.equals(reverseRegex);
    }
    
    public boolean isAmbiguous(){
        return BioUtil.isAmbiguousDNAs(recognitionSite);        
    }
    
    public void abc(){
        if(isAmbiguous()){
            List<char[]> a = new ArrayList<char[]>();
            for(int i = 0; i < recognitionSite.length(); i++){
                char base = recognitionSite.charAt(i);
                char[] exactBases = BioUtil.getExactBases(base);
                a.add(exactBases);
            }
            
            int totalSize = 1;
            for(int i = 0; i < a.size(); i++){
                totalSize = totalSize * a.get(i).length;
            }
            
            StringBuilder b = new StringBuilder();
            for(int i = 0; i < a.size(); i++){
                int index = totalSize / (totalSize / a.get(i).length) - 1;
                b.append(a.get(i)[index]);
            }
            System.out.println(b.toString());
        }else{
        
        }
    }

    /**
     * <code>getCutType</code> returns the type of cut produced by the enzyme.
     * This will be one of either RestrictionEnzyme.CUT_SIMPLE (where it cuts in
     * one position relative to the recognition site i.e. the vast majority of
     * cases) or RestrictionEnzyme.CUT_COMPOUND (where it cuts in two
     * positions).
     *
     * @return an <code>int</code>.
     */
    public int getCutType() {
        return cutType;
    }

    public void setCutType(int cutType) {
        this.cutType = cutType;
    }

    /**
     * <code>getDownstreamCut</code> returns the cut site within or downstream
     * of the recognition site.
     *
     * @return an <code>int []</code> array with the position in the 5'-strand
     * at index 0 and the 3'-strand at index 1.
     */
    public int[] getDownstreamCutPos() {
        return downstreamCutPos;
    }

    public void setDownstreamCutPos(int[] downstreamCutPos) {
        this.downstreamCutPos = downstreamCutPos;
    }

    /**
     * <code>getUpstreamCut</code> returns the cut site upstream of the
     * recognition site.
     *
     * @return an <code>int []</code> array with the position in the 5'-strand
     * at index 0 and the 3'-strand at index 1. For example, Bsp24I will return
     * -8 and -13:
     *
     * 5' ^NNNNNNNNGACNNNNNNTGGNNNNNNNNNNNN^ 3' 3'
     * ^NNNNNNNNNNNNNCTGNNNNNNACCNNNNNNN^ 5'
     *
     * @exception BioException if the enzyme does not cleave on both sides of
     * its recognition site.
     */
    public int[] getUpstreamCutPos() {
        /*
         * if (cutType == CUT_SIMPLE) { throw new BioException(name + " does not
         * cut upstream of the recognition site"); }
         */

        return upstreamCutPos;
    }

    public void setUpstreamCutPos(int[] upstreamCutPos) {
        this.upstreamCutPos = upstreamCutPos;
    }

    /**
     * <code>getDownstreamEndType</code> returns the double-stranded end type
     * produced by the primary (intra-site or downstream) cut.
     *
     * @return an <code>int</code> equal to one of the constant fields
     * OVERHANG_5PRIME, OVERHANG_3PRIME or BLUNT.
     */
    public int getDownstreamEndType() {
        if (downstreamCutPos[0] > downstreamCutPos[1]) {
            return OVERHANG_3PRIME;
        } else if (downstreamCutPos[0] < downstreamCutPos[1]) {
            return OVERHANG_5PRIME;
        } else {
            return BLUNT;
        }
    }

    /**
     * <code>getUpstreamEndType</code> returns the double-stranded end type
     * produced by the secondary (upstream) cut.
     *
     * @return an <code>int</code> equal to one of the constant fields
     * OVERHANG_5PRIME, OVERHANG_3PRIME or BLUNT.
     *
     * @exception BioException if the enzyme does not cleave on both sides of
     * its recognition site.
     */
    public int getUpstreamEndType() throws BioException {
        if (cutType == CUT_SIMPLE) {
            throw new BioException(name + " does not cut upstream of the recognition site");
        }

        if (upstreamCutPos[0] > upstreamCutPos[1]) {
            return OVERHANG_3PRIME;
        } else if (upstreamCutPos[0] < upstreamCutPos[1]) {
            return OVERHANG_5PRIME;
        } else {
            return BLUNT;
        }
    }

    /**
     * The cutting size of a restriction enzyme is defined has the number of
     * nucleotides that are directly involved in the recognition sequence. The
     * size is ponderated as follow: 1 for a single nucleotide, 1/2 for a
     * degeneracy of 2, 1/4 for a degeneracy of 3, and 0 for any N nucleotides.
     */
    public synchronized double getCuttingSize() {
        if (size == 0) {
            SymbolList symbols = BioJavaHelper.toDNASymbolList(getRecognitionSite());
            double tempsize = 0;
            for (int i = 1; i <= symbols.length(); i++) {
                Symbol s = symbols.symbolAt(i);
                FiniteAlphabet a = (FiniteAlphabet) s.getMatches();
                int cs = a.size();
                if (cs == 1) {
                    tempsize++;
                } else if (cs == 2) {
                    tempsize += 0.5;
                } else if (cs == 3) {
                    tempsize += 0.25;
                }
            }
            size = tempsize;
        }
        return size;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ getForwardRegex().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof REN)
                && name.equals(((REN) o).getName());
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String s) {
        this.summary = s;
    }
    private String visual;

    public String getVisual() {
        if (visual == null) {
            StringBuilder builder = new StringBuilder();
            Integer forward = null;
            Integer reverse = null;

            if (downstreamCutPos != null && downstreamCutPos.length > 0) {
                forward = downstreamCutPos[0];
            }
            if (downstreamCutPos != null && downstreamCutPos.length > 1) {
                reverse = downstreamCutPos[1];
            }
            if (forward != null && reverse != null) {
                boolean threePrimeOverhang = false;
                boolean bluntEnd = false;
                if (forward == reverse) {
                    bluntEnd = true;
                } else if (forward > reverse) {
                    threePrimeOverhang = true;
                }

                if (bluntEnd) {
                    if (reverse < recognitionSite.length()) {
                        builder.append(recognitionSite.substring(0, reverse));
                        builder.append(Unicodes.UP_DOWN_ARROW);
                        builder.append(recognitionSite.substring(reverse));
                    } else {
                        builder.append(recognitionSite);
                        builder.append(String.format("(%d/%d)", forward - recognitionSite.length(), reverse - recognitionSite.length()));
                    }
                } else {
                    if (Math.max(forward, reverse) <= recognitionSite.length()) {
                        builder.append(recognitionSite.substring(0, Math.min(forward, reverse)));
                        builder.append(threePrimeOverhang ? Unicodes.UPWARDS_ARROW : Unicodes.DOWNWARDS_ARROW);
                        builder.append(recognitionSite.substring(Math.min(forward, reverse), Math.max(forward, reverse)));
                        builder.append(threePrimeOverhang ? Unicodes.DOWNWARDS_ARROW : Unicodes.UPWARDS_ARROW);
                        builder.append(recognitionSite.substring(Math.max(forward, reverse)));
                    } else {
                        builder.append(recognitionSite);
                        builder.append(String.format("(%d/%d)", forward - recognitionSite.length(), reverse - recognitionSite.length()));
                    }
                }
            }
            visual = builder.toString();
        }

        return visual.toString();
    }
}
