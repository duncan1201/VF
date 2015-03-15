/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.mrbayers.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class MrBayesParam implements PropertyChangeListener{
            
    private String nexus;
    
    private Nst nst = Nst.ONE;
    private Rates rates = Rates.Invgamma;
    private Integer sampleFreq = 100;
    private Integer printfreq = 100;
    private Integer diagnfreq = 1000;
    PropertyChangeSupport propertyChangeSupport;
    
    public MrBayesParam(){
        propertyChangeSupport = new PropertyChangeSupport(this);
    }    

    public Nst getNst() {
        return nst;
    }

    public void setNst(Nst nst) {
        Nst old = this.nst;
        this.nst = nst;
        this.propertyChangeSupport.firePropertyChange("nst", old, this.nst);
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        Rates old = rates;
        this.rates = rates;
        this.propertyChangeSupport.firePropertyChange("rates", old, this.rates);
    }
    
    
    
    public String toNexus(){
        StringBuilder ret = new StringBuilder();
        ret.append("begin mrbayes;\n");
        ret.append(String.format("lset Nst=%s Rates=%s;\n", nst.value, rates.toString()));
        ret.append(String.format("mcmc ngen=20000 samplefreq=%d printfreq=%d diagnfreq=%d", sampleFreq, printfreq, diagnfreq));
        ret.append("end;");
        return ret.toString();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.nexus = toNexus();
    }    

    public String getNexus() {
        return nexus;
    }

    public void setNexus(String nexus) {
        this.nexus = nexus;
    }
    
    
}
