/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode.api;

/**
 *
 * @author dq
 */
public class Codon {

    private Boolean stopCodon;
    private Boolean startCodon;
    private String nucleotides;

    public Codon() {
    }

    public Codon(String nucleotides, Boolean startCodon, Boolean stopCodon) {
        this.nucleotides = nucleotides;
        this.startCodon = startCodon;
        this.stopCodon = stopCodon;
    }

    public String getNucleotides() {
        return nucleotides;
    }

    public void setNucleotides(String nucleotides) {
        this.nucleotides = nucleotides;
    }

    public boolean isStartCodon() {
        return startCodon;
    }

    public void setStartCodon(Boolean startCodon) {
        this.startCodon = startCodon;
    }

    public boolean isStopCodon() {
        return stopCodon;
    }

    public void setStopCodon(boolean stopCodon) {
        this.stopCodon = stopCodon;
    }

    @Override
    public String toString() {
        return nucleotides.toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Codon other = (Codon) obj;
        if ((this.nucleotides == null) ? (other.nucleotides != null) : !this.nucleotides.equals(other.nucleotides)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.nucleotides != null ? this.nucleotides.hashCode() : 0);
        return hash;
    }
}
