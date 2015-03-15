/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.gateway.core.service.api.IAttSiteService;
import com.gas.gateway.core.service.api.IGWValidateService;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class Sorters {

    protected static class InsertSorter implements Comparator<AnnotatedSeq> {

        List<String> pro2List;
        List<String> pro3List;
        List<String> pro4List;
        List<String> frag3List;
        private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);

        public InsertSorter() {
            pro2List = Arrays.asList(IGWValidateService.PRO2_INSERT);
            pro3List = Arrays.asList(IGWValidateService.PRO3_INSERT);
            pro4List = Arrays.asList(IGWValidateService.PRO4_INSERT);
            frag3List = Arrays.asList(IGWValidateService.FRAG3_INSERT);
        }

        @Override
        public int compare(AnnotatedSeq o1, AnnotatedSeq o2) {
            int ret = 0;
            String shortNames = attSiteService.getAttBSites(o1, true).getShortNames().toUpperCase(Locale.ENGLISH);
            String shortNames2 = attSiteService.getAttBSites(o2, true).getShortNames().toUpperCase(Locale.ENGLISH);

            if (pro2List.contains(shortNames) && pro2List.contains(shortNames2)) {
                Integer index = pro2List.indexOf(shortNames);
                Integer index2 = pro2List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (pro3List.contains(shortNames) && pro3List.contains(shortNames2)) {
                Integer index = pro3List.indexOf(shortNames);
                Integer index2 = pro3List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (pro4List.contains(shortNames) && pro4List.contains(shortNames2)) {
                Integer index = pro4List.indexOf(shortNames);
                Integer index2 = pro4List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (frag3List.contains(shortNames) && frag3List.contains(shortNames2)) {
                Integer index = frag3List.indexOf(shortNames);
                Integer index2 = frag3List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            }
            return ret;
        }
    }

    protected static class EntryCloneSorter implements Comparator<AnnotatedSeq> {

        List<String> pro2List;
        List<String> pro3List;
        List<String> pro4List;
        List<String> frag3List;
        private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
        //L1-R5, L5-L2
        //L1-L4, R4-R3, L3-L2
        //L1-R5, L5-L4, R4-R3, L3-L2  
        //L4-R1, L1-L2, R2-L3

        public EntryCloneSorter() {
            pro2List = Arrays.asList(IGWValidateService.PRO2_ENTRY);
            pro3List = Arrays.asList(IGWValidateService.PRO3_ENTRY);
            pro4List = Arrays.asList(IGWValidateService.RPO4_ENTRY);
            frag3List = Arrays.asList(IGWValidateService.FRAG3_ENTRY);
        }

        @Override
        public int compare(AnnotatedSeq o1, AnnotatedSeq o2) {
            int ret = 0;
            String shortNames = attSiteService.getAttLRSites(o1).getShortNames().toUpperCase(Locale.ENGLISH);
            String shortNames2 = attSiteService.getAttLRSites(o2).getShortNames().toUpperCase(Locale.ENGLISH);

            if (pro2List.contains(shortNames) && pro2List.contains(shortNames2)) {
                Integer index = pro2List.indexOf(shortNames);
                Integer index2 = pro2List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (pro3List.contains(shortNames) && pro3List.contains(shortNames2)) {
                Integer index = pro3List.indexOf(shortNames);
                Integer index2 = pro3List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (pro4List.contains(shortNames) && pro4List.contains(shortNames2)) {
                Integer index = pro4List.indexOf(shortNames);
                Integer index2 = pro4List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (frag3List.contains(shortNames) && frag3List.contains(shortNames2)) {
                Integer index = frag3List.indexOf(shortNames);
                Integer index2 = frag3List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            }

            return ret;
        }
    }

    protected static class DonorSorter implements Comparator<AnnotatedSeq> {

        private IAttSiteService attSiteService = Lookup.getDefault().lookup(IAttSiteService.class);
        List<String> pro2List;
        List<String> pro3List;
        List<String> pro4List;
        List<String> frag3List;

        public DonorSorter() {
            pro2List = Arrays.asList(IGWValidateService.PRO2_DONOR);
            pro3List = Arrays.asList(IGWValidateService.PRO3_DONOR);
            pro4List = Arrays.asList(IGWValidateService.PRO4_DONOR);
            frag3List = Arrays.asList(IGWValidateService.FRAG3_DONOR);
        }

        @Override
        public int compare(AnnotatedSeq o1, AnnotatedSeq o2) {
            int ret = 0;
            String shortNames = attSiteService.getAttPSites(o1).getShortNames().toUpperCase(Locale.ENGLISH);
            String shortNames2 = attSiteService.getAttPSites(o2).getShortNames().toUpperCase(Locale.ENGLISH);
            if (pro2List.contains(shortNames) && pro2List.contains(shortNames2)) {
                Integer index = pro2List.indexOf(shortNames);
                Integer index2 = pro2List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (pro3List.contains(shortNames) && pro3List.contains(shortNames2)) {
                Integer index = pro3List.indexOf(shortNames);
                Integer index2 = pro3List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (pro4List.contains(shortNames) && pro4List.contains(shortNames2)) {
                Integer index = pro4List.indexOf(shortNames);
                Integer index2 = pro4List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else if (frag3List.contains(shortNames) && frag3List.contains(shortNames2)) {
                Integer index = frag3List.indexOf(shortNames);
                Integer index2 = frag3List.indexOf(shortNames2);
                ret = index.compareTo(index2);
            } else {
                throw new IllegalArgumentException();
            }
            return ret;
        }
    }
}
