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
public interface IAttSiteService {

    public String SD = "GAAGGAGATA";
    public String SD_postfix = "GA";
    public String KOZAK = "ACCATG";
    public String START_CODON = "ATG";
    public String STOP_CODON = "TAG";
           
    AnnotatedSeq addAttBSite(AnnotatedSeq as, PrimerAdapter leftAdapter, boolean sd, boolean kozak, boolean fuseForward, boolean startCodon, PrimerAdapter rightAdapter, boolean fuseReverse, boolean stopCodon);  
    AttSiteList getAttSites(AnnotatedSeq as, Character clazz, boolean includeNested);
    AttSiteList getAttBSites(AnnotatedSeq as, boolean primerAdapter);
    AttSiteList getAttRSites(AnnotatedSeq as);
    AttSiteList getAttLSites(AnnotatedSeq as);
    AttSiteList getAttLRSites(AnnotatedSeq as);
    AttSiteList getAttPSites(AnnotatedSeq as);
    boolean isAttbPcrProduct(AnnotatedSeq as);
    boolean isDonorVector(AnnotatedSeq as);
    boolean isDestVector(AnnotatedSeq as);
    boolean isEntryClone(AnnotatedSeq as);    
}
