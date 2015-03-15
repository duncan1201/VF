/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.core.CharList;
import com.gas.common.ui.misc.Loc;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;
import org.openide.util.Exceptions;

/**
 *
 * @author dunqiang
 */
public class BioUtil {

    public static final Character GAP = '-';
    static final String[] DNAs = {
        "A", //	Adenine
        "B", //	C, T, U, or G (not A)        
        "C", //	Cytosine
        "D", //	A, T, U, or G (not C)        
        "G", //	Guanine
        "H", //	A, T, U, or C (not G)        
        "K", //	T, U, or G
        "M", //	C or A    
        "N", //Any base (A, C, G, T, or U)  
        "R", //	Purine (A or G)     
        "S", //	C or G    
        "T", //	Thymine        
        "U", //	Uracil
        "V", //	A, C, or G (not T, not U)
        "W", //	T, U, or A
        "Y" //	Pyrimidine (C, T, or U)
    };
    static final Map<Character, char[]> degenerateDNAs = new HashMap<Character, char[]>();
    final static Map<String, String> dnaCompMap = new HashMap<String, String>();
    static final String[] PROTEINs = {
        "A", //.................Ala.................Alanine
        "B", //.................Asx.................Aspartic acid or Asparagine
        "C", //.................Cys.................Cysteine
        "D", //.................Asp.................Aspartic Acid
        "E", //.................Glu.................Glutamic Acid
        "F", //.................Phe.................Phenylalanine
        "G", //.................Gly.................Glycine
        "H", //.................His.................Histidine
        "I", //.................Ile.................Isoleucine
        "K", //.................Lys.................Lysine
        "L", //.................Leu.................Leucine
        "M", //.................Met.................Methionine
        "N", //.................Asn.................Asparagine
        "P", //.................Pro.................Proline
        "Q", //.................Gln.................Glutamine
        "R", //.................Arg.................Arginine
        "S", //.................Ser.................Serine
        "T", //.................Thr.................Threonine
        "V", //.................Val.................Valine
        "W", //.................Trp.................Tryptophan
        "X", //.................Xaa.................Any amino acid
        "Y", //.................Tyr.................Tyrosine
        "Z" //.................Glx.................Glutamine or Glutamic acid        
    };

    static {
        Arrays.sort(DNAs);
        Arrays.sort(PROTEINs);
        // init the DNA comp map
        dnaCompMap.put("A", "T");
        dnaCompMap.put("B", "V");
        dnaCompMap.put("C", "G");
        dnaCompMap.put("D", "H");
        dnaCompMap.put("G", "C");
        dnaCompMap.put("H", "D");
        dnaCompMap.put("K", "M");
        dnaCompMap.put("M", "K");
        dnaCompMap.put("N", "N");
        dnaCompMap.put("R", "Y");
        dnaCompMap.put("S", "S");
        dnaCompMap.put("T", "A");
        dnaCompMap.put("U", "A");
        dnaCompMap.put("V", "B");
        dnaCompMap.put("W", "W");
        dnaCompMap.put("Y", "R");
        dnaCompMap.put("-", "-");

        // init the degenerate DNAs
        degenerateDNAs.put('A', new char[]{'A'});
        degenerateDNAs.put('C', new char[]{'C'});
        degenerateDNAs.put('G', new char[]{'G'});
        degenerateDNAs.put('T', new char[]{'T'});
        degenerateDNAs.put('U', new char[]{'U'});
        degenerateDNAs.put('W', new char[]{'A', 'T'});
        degenerateDNAs.put('S', new char[]{'C', 'G'});
        degenerateDNAs.put('M', new char[]{'A', 'C'});
        degenerateDNAs.put('K', new char[]{'G', 'T'});
        degenerateDNAs.put('R', new char[]{'A', 'G'});
        degenerateDNAs.put('Y', new char[]{'C', 'T'});
        degenerateDNAs.put('B', new char[]{'C', 'G', 'T'});
        degenerateDNAs.put('D', new char[]{'A', 'G', 'T'});
        degenerateDNAs.put('H', new char[]{'A', 'C', 'T'});
        degenerateDNAs.put('V', new char[]{'A', 'C', 'G'});
        degenerateDNAs.put('N', new char[]{'A', 'C', 'G', 'T'});
    }

    public static char complement(char c) {
        return complement(Character.toString(c)).charAt(0);
    }

    public static String complement(String code) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            String c = dnaCompMap.get(code.substring(i, i + 1).toUpperCase(Locale.ENGLISH));
            ret.append(c);
        }
        return ret.toString();
    }

    public static int normalizeCircularPos(int pos, int totalPos) {
        if (pos <= 0) {
            while (pos <= 0) {
                pos += totalPos;
            }
        } else if (pos > totalPos) {
            pos = pos % totalPos;
        }
        return pos;
    }

    /*
     * a<->g, c<->t
     */
    public static boolean isTransition(char one, char two) {
        boolean ret = false;
        one = Character.toUpperCase(one);
        two = Character.toUpperCase(two);

        ret = (one != two) && ((isPurine(one) && isPurine(two)) || (isPyridimine(one) && isPyridimine(two)));

        return ret;
    }

    public static boolean isGap(char from, char to) {
        return to == '-';
    }

    public static boolean isGaps(String s) {
        boolean ret = true;
        for (int i = 0; i < s.length(); i++) {
            boolean isGap = isGap(s.charAt(i));
            if (isGap) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static boolean isGap(char c) {
        return c == '-';
    }

    public static boolean isInsertion(String from, String to) {
        boolean ret = false;
        if (from.length() > 0 && to.length() > 0) {
            ret = isInsertion(from.charAt(0), to.charAt(0));
        }
        return ret;
    }

    public static boolean isInsertion(char from, char to) {
        boolean ret = false;
        ret = from == '-' && to != '-';
        return ret;
    }

    public static boolean isAnyNucleotide(String one, String two) {
        boolean ret = false;
        if (one.length() > 0 && two.length() > 0) {
            ret = isAnyNucleotide(one.charAt(0), two.charAt(0));
        }
        return ret;
    }

    public static boolean isAnyNucleotide(Character one, Character two) {
        boolean ret = false;
        two = Character.toUpperCase(two);
        ret = !one.equals('-') && two.equals('N');
        return ret;
    }

    public static boolean isTransition(String one, String two) {
        boolean ret = false;
        if (one.length() > 0 && two.length() > 0) {
            ret = isTransition(one.charAt(0), two.charAt(0));
        } else {
        }
        return ret;
    }

    public static boolean isTransverion(String one, String two) {
        boolean ret = false;
        if (one.length() > 0 && two.length() > 0) {
            ret = isTransverion(one.charAt(0), two.charAt(0));
        } else {
        }
        return ret;
    }

    public static boolean isTransverion(char one, char two) {
        return ((isPurine(one) && isPyridimine(two)) || (isPurine(two) && isPyridimine(one)));
    }

    public static boolean isPurine(char a) {
        return a == 'a' || a == 'A' || a == 'G' || a == 'g';
    }

    public static Boolean isPyridimine(char a) {
        return a == 'c' || a == 'C' || a == 't' || a == 'T';
    }

    public static List<String> getNonAmbiguousDNAs(String ambiguousDNAs) {
        List<String> _ret = new ArrayList<String>();
        List<StringBuilder> ret = new ArrayList<StringBuilder>();
        if (isAmbiguousDNAs(ambiguousDNAs)) {
            List<CharList> allBases = new ArrayList<CharList>();
            for (int i = 0; i < ambiguousDNAs.length(); i++) {
                char base = ambiguousDNAs.charAt(i);
                char[] exactBases = BioUtil.getExactBases(base);
                CharList charList = new CharList(exactBases);
                allBases.add(charList);
            }

            for (int posIndex = 0; posIndex < allBases.size(); posIndex++) {
                CharList bases = allBases.get(posIndex);
                if (ret.isEmpty()) {
                    for (int i = 0; i < bases.size(); i++) {
                        StringBuilder tmp = new StringBuilder();
                        tmp.append(bases.get(i));
                        ret.add(tmp);
                    }
                } else {
                    // increase the size if necessary
                    final int newSize = ret.size() * bases.size();
                    if (newSize > ret.size()) {
                        List<StringBuilder> tmp = new ArrayList<StringBuilder>();
                        while (tmp.size() < newSize) {
                            for (int i = 0; i < ret.size(); i++) {
                                tmp.add(new StringBuilder(ret.get(i).toString()));
                            }
                        }
                        ret = tmp;
                    }

                    final int groupSize = newSize / bases.size();
                    for (int i = 0; i < ret.size(); i++) {
                        int baseIndex = i / groupSize;
                        ret.get(i).append(bases.get(baseIndex));
                    }
                }
            }

            for (StringBuilder sb : ret) {
                _ret.add(sb.toString());
            }
            Collections.sort(_ret);
        } else {
            _ret.add(ambiguousDNAs);
        }
        return _ret;
    }

    private static void print(List<StringBuilder> test) {
        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i));
        }
    }

    public static char[] getExactBases(Character degenerateBase) {
        char[] ret = degenerateDNAs.get(Character.toUpperCase(degenerateBase));
        return ret;
    }

    public static String reverseComplement(List<Character> iupacCodes) {
        String str = StrUtil.toString(iupacCodes);
        return reverseComplement(str);
    }

    /**
     * @return String reverse complement of the given DNA sequence in uppercase
     */
    public static String reverseComplement(String iupacCodes) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < iupacCodes.length(); i++) {
            String c = dnaCompMap.get(iupacCodes.substring(i, i + 1).toUpperCase(Locale.ENGLISH));
            ret.append(c);
        }
        return ret.reverse().toString();
    }

    public static String reverseComplementByName(String dnaName) {
        String ret = null;
        Character iupac = toIUPAC(dnaName);
        try {
            SymbolList symbolList = DNATools.reverseComplement(DNATools.createDNA(iupac.toString()));
            Symbol symbol = symbolList.symbolAt(1);
            ret = symbol.getName();
        } catch (IllegalAlphabetException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalSymbolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public static String subsequence(String str, int start, int end) {
        String ret = null;
        if (str.length() >= end && str.length() >= start) {
            if (start <= end) {
                ret = str.substring(start - 1, end);
            } else {
                ret = str.substring(start - 1) + str.substring(0, end);
            }
        }
        return ret;
    }

    public static boolean areDNAs(String str) {
        boolean ret = true;
        for (int i = 0; i < str.length(); i++) {
            boolean isDNA = isDNA(str.charAt(i));
            if (!isDNA) {
                ret = false;
            }
        }
        return ret;
    }

    public static boolean isDNA(String iupacCode) {
        return Arrays.binarySearch(DNAs, iupacCode.toUpperCase(Locale.ENGLISH)) > -1;
    }

    public static boolean isDNA(Character iupacCode) {
        return Arrays.binarySearch(DNAs, iupacCode.toString().toUpperCase(Locale.ENGLISH)) > -1;
    }

    public static boolean isAmbiguousDNAs(String iupacCodes) {
        return !areNonambiguousDNAs(iupacCodes);
    }

    public static boolean areNonambiguousDNAs(String iupacCodes) {
        for (int i = 0; i < iupacCodes.length(); i++) {
            char c = iupacCodes.charAt(i);
            boolean nonambiguous = isNonambiguousDNA(c);
            if (!nonambiguous) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNonambiguousDNA(Character iupacCode) {
        boolean ret = false;
        if (iupacCode == 'A' || iupacCode == 'a'
                || iupacCode == 'T' || iupacCode == 't'
                || iupacCode == 'C' || iupacCode == 'c'
                || iupacCode == 'G' || iupacCode == 'g'
                || iupacCode == 'U' || iupacCode == 'u') {
            ret = true;
        }
        return ret;
    }

    public static boolean isProtein(Character iupacCode) {
        return Arrays.binarySearch(PROTEINs, iupacCode.toString().toUpperCase(Locale.ENGLISH)) > -1;
    }

    public static boolean areProteins(String str) {
        boolean ret = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean isProtein = isProtein(c);
            if (!isProtein) {
                ret = false;
                break;
            }
        }
        return ret;
    }
    static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(BioUtil.class.getResourceAsStream("NCBI_URLs.txt"));
        } catch (IOException ex) {
            Logger.getLogger(BioUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static URI getNCBIURI(String db, String accession) {
        URI ret = null;
        String urlPattern = properties.getProperty(db.toUpperCase(Locale.ENGLISH));
        if (urlPattern != null) {
            String url = String.format(urlPattern, accession);
            ret = CommonUtil.getURI(url);
        }
        return ret;
    }

    public static boolean isDNAByGuess(String seq) {
        double dnaCount = 0;
        for (int i = 0; i < seq.length(); i++) {
            char ch = seq.charAt(i);
            if (ch == 'A' || ch == 'a'
                    || ch == 'T' || ch == 't'
                    || ch == 'C' || ch == 'c'
                    || ch == 'G' || ch == 'g'
                    || ch == 'U' || ch == 'u'
                    || ch == 'N' || ch == 'n') {
                dnaCount++;
            }
        }
        return dnaCount / seq.length() > 0.8;
    }

    public static char toIUPAC(String nucleotideName) {
        if (nucleotideName.equals(DNATools.a().getName())) {
            return 'a';
        } else if (nucleotideName.equals(DNATools.b().getName())) {
            return 'b';
        } else if (nucleotideName.equals(DNATools.c().getName())) {
            return 'c';
        } else if (nucleotideName.equals(DNATools.d().getName())) {
            return 'd';
        } else if (nucleotideName.equals(DNATools.g().getName())) {
            return 'g';
        } else if (nucleotideName.equals(DNATools.h().getName())) {
            return 'h';
        } else if (nucleotideName.equals(DNATools.k().getName())) {
            return 'k';
        } else if (nucleotideName.equals(DNATools.m().getName())) {
            return 'm';
        } else if (nucleotideName.equals(DNATools.n().getName())) {
            return 'n';
        } else if (nucleotideName.equals(DNATools.r().getName())) {
            return 'r';
        } else if (nucleotideName.equals(DNATools.s().getName())) {
            return 's';
        } else if (nucleotideName.equals(DNATools.t().getName())) {
            return 't';
        } else if (nucleotideName.equals(DNATools.v().getName())) {
            return 'v';
        } else if (nucleotideName.equals(DNATools.w().getName())) {
            return 'w';
        } else if (nucleotideName.equals(DNATools.y().getName())) {
            return 'y';
        } else {
            throw new IllegalArgumentException(String.format("Nucleotide '%s' unrecognized!", nucleotideName));
        }
    }

    public static Loc find(String nucleotides, String target) {
        nucleotides = nucleotides.toUpperCase(Locale.ENGLISH);
        target = target.toUpperCase(Locale.ENGLISH);

        Loc ret = null;
        int index = nucleotides.indexOf(target);
        if (index < 0) {
            String rc = reverseComplement(nucleotides);
            index = rc.indexOf(target);
            if (index > -1) {
                ret = new Loc();
                ret.setStrand(false);
                int startPos = index + target.length();
                ret.setStart(LocUtil.flip(startPos, nucleotides.length()));
                ret.setEnd(ret.getStart() + target.length() - 1);
            }
        } else {
            ret = new Loc();
            ret.setStart(index + 1);
            ret.setEnd(index + target.length());
            ret.setStrand(true);
        }
        return ret;
    }

    public static Character getAmbiguity(CharList _chars) {
        Character ret = null;
        CharList chars = _chars.replace('u', 't').replace('U', 'T');
        if (chars.containsIgnoreCase('a', 'c', 'g', 't')) {
            ret = 'N';
        } else if (chars.containsIgnoreCase('c', 'g', 't')) {
            ret = 'B';
        } else if (chars.containsIgnoreCase('a', 'g', 't')) {
            ret = 'D';
        } else if (chars.containsIgnoreCase('a', 'c', 't')) {
            ret = 'H';
        } else if (chars.containsIgnoreCase('a', 'c', 'g')) {
            ret = 'V';
        } else if (chars.containsIgnoreCase('a', 't')) {
            ret = 'W';
        } else if (chars.containsIgnoreCase('c', 'g')) {
            ret = 'S';
        } else if (chars.containsIgnoreCase('a', 'c')) {
            ret = 'M';
        } else if (chars.containsIgnoreCase('g', 't')) {
            ret = 'K';
        } else if (chars.containsIgnoreCase('a', 'g')) {
            ret = 'R';
        } else if (chars.containsIgnoreCase('c', 't')) {
            ret = 'Y';
        } else if (chars.containsIgnoreCase('c')) {
            ret = 'C';
        } else if (chars.containsIgnoreCase('t')) {
            ret = 'T';
        } else if (chars.containsIgnoreCase('a')) {
            ret = 'A';
        } else if (chars.containsIgnoreCase('g')) {
            ret = 'G';
        } else {
        }
        return ret;
    }
}
