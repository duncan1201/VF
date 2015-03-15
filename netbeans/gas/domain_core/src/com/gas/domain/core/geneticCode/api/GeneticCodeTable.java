/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode.api;

import java.util.*;

/**
 *
 * @author dq
 */
public class GeneticCodeTable {

    private String name;
    private Integer id;
    private Set<Character> aminoAcids = new HashSet<Character>();
    private Map<Codon, Character> codon2aa = new HashMap<Codon, Character>();
    private Map<String, Codon> nucleotides2codon = new HashMap<String, Codon>();
    private Map<Character, CodonList> aa2Codons = new HashMap<Character, CodonList>();

    public Integer getId() {
        return id;
    }
    
    public CodonList getCodons(Character aminoAcid){        
        return aa2Codons.get(aminoAcid);        
    }

    private Codon getCodon(String nucleotides) {
        Codon ret = nucleotides2codon.get(nucleotides.toUpperCase(Locale.ENGLISH));
        return ret;
    }
    
    private int getEndIndex(String nucleotides, int start) {
        int ret = start;
        int letterCount = 0;
        int i = start;
        for (; i < nucleotides.length() && letterCount < 3; i++) {
            char c = nucleotides.charAt(i);
            if (Character.isLetter(c)) {
                letterCount++;
            }
            if (letterCount == 3) {
                break;
            }
        }
        ret = i;
        return ret;
    }

    public String translate(String nucleotides) {
        nucleotides = nucleotides.replaceAll("U", "T");
        StringBuilder ret = new StringBuilder();
        StringBuilder triplet = new StringBuilder();
        for (int i = 0; i < nucleotides.length(); i = i + 1) {

            char c = nucleotides.charAt(i);
            if (Character.isLetter(c)) {
                triplet.append(c);
            } else {
                ret.append('-');
            }
            if (triplet.length() == 3) {
                Codon codon = getCodon(triplet.toString());
                Character aa = translate(codon);
                ret.append(aa);
                triplet = new StringBuilder();
            }
        }
        return ret.toString();
    }

    private int letterCount(String triplet) {
        int ret = 0;
        for (int i = 0; i < triplet.length(); i++) {
            char c = triplet.charAt(i);
            if (Character.isLetter(c)) {
                ret++;
            }
        }
        return ret;
    }

    private int gapCount(String triplet) {
        int ret = 0;
        for (int i = 0; i < triplet.length(); i++) {
            char c = triplet.charAt(i);
            if (!Character.isLetter(c)) {
                ret++;
            }
        }
        return ret;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Codon c, Character aa) {
        codon2aa.put(c, aa);
        nucleotides2codon.put(c.getNucleotides(), c);
        aminoAcids.add(aa);
        if(!aa2Codons.containsKey(aa)){
            CodonList codonList = new CodonList();
            aa2Codons.put(aa, codonList);
        }
        
        aa2Codons.get(aa).add(c);
    }
    
    public Set<Character> getAminoAcids(){
        return aminoAcids;
    }

    public CodonList getStartCodons() {
        CodonList ret = new CodonList();
        Iterator<Codon> itr = codon2aa.keySet().iterator();
        while (itr.hasNext()) {
            Codon c = itr.next();
            if (c.isStartCodon()) {
                ret.add(c);
            }
        }
        return ret;
    }

    public CodonList getStopCodons() {
        CodonList ret = new CodonList();
        Iterator<Codon> itr = codon2aa.keySet().iterator();
        while (itr.hasNext()) {
            Codon c = itr.next();
            if (c.isStopCodon()) {
                ret.add(c);
            }
        }
        return ret;
    }

    private Character translate(Codon c) {
        Character ret = codon2aa.get(c);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(id);
        ret.append(".");
        ret.append(name);
        return ret.toString();
    }

    public static class NameComparator implements Comparator<GeneticCodeTable> {

        @Override
        public int compare(GeneticCodeTable o1, GeneticCodeTable o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public static class TableIdComparator implements Comparator<GeneticCodeTable> {

        @Override
        public int compare(GeneticCodeTable o1, GeneticCodeTable o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
