/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.api.IFetureKeyService;
import com.gas.domain.core.vector.advanced.api.IFeatListService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFeatListService.class)
public class FeatListService implements IFeatListService {

    static IFetureKeyService fetureKeyService = Lookup.getDefault().lookup(IFetureKeyService.class);
    private final String FILE_NAME = "FeatList.vdb";
    private final String ENTRY_START = "194";
    private final String ENTRY_END = "50";
    private final String FETURE_TYPE = "198\\|(.+)";
    private final String FETURE_LABELS = "199\\|(.+)";
    private Map<String, String> label2type = new HashMap<String, String>();
    private Map<Integer, List<String>> type2labels = new TreeMap<Integer, List<String>>();
    private Map<Integer, String> i2name = new HashMap<Integer, String>();

    public String getFeatureType(final String label) {
        if (label2type.isEmpty() || type2labels.isEmpty()) {
            initData();
        }
        String ret = null;
        Iterator<Integer> keyItr = type2labels.keySet().iterator();
        while (keyItr.hasNext()) {
            Integer key = keyItr.next();
            if (key.equals(4)) {
                System.out.print("");
            }
            List<String> labels = type2labels.get(key);
            if (labels.contains(label)) {
                ret = key.toString();
                break;
            }
        }
        return ret;
    }

    private void initData() {
        if (label2type.isEmpty()) {
            InputStream inputStream = FeatListService.class.getResourceAsStream(FILE_NAME);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            try {
                String fetureType = null;
                while ((line = r.readLine()) != null) {
                    line = line.trim();
                    if (line.matches(ENTRY_START)) {
                    } else if (line.matches(ENTRY_END)) {
                    } else if (line.matches(FETURE_TYPE)) {
                        fetureType = StrUtil.extract(FETURE_TYPE, line);
                        type2labels.put(Integer.parseInt(fetureType), new ArrayList<String>());
                    } else if (line.matches(FETURE_LABELS)) {
                        String labels = StrUtil.extract(FETURE_LABELS, line);
                        String[] splits = labels.split("\\|");
                        for (String label : splits) {
                            label2type.put(label, fetureType);
                            type2labels.get(Integer.parseInt(fetureType)).add(label);
                        }
                    }
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    private void initI2key(){
        i2name.put(4, FetureKeyCnst.CDS);
        i2name.put(5, FetureKeyCnst.CENTROMERE);
        i2name.put(9, FetureKeyCnst.Enhancer);
        i2name.put(21, FetureKeyCnst.MISC_FEATURE);
        i2name.put(25, FetureKeyCnst.PolyA_Signal);
        i2name.put(26, FetureKeyCnst.PolyA_Site);
        i2name.put(27, FetureKeyCnst.Primer);
        i2name.put(28, FetureKeyCnst.Primer_Binding_Site);
        i2name.put(29, FetureKeyCnst.Promoter_Eukaryotic);
        i2name.put(30, FetureKeyCnst.Promoter_Prokaryotic);
        i2name.put(31, FetureKeyCnst.Protein_Binding_Site);
        i2name.put(32, FetureKeyCnst.RBS);
        i2name.put(33, FetureKeyCnst.Replication_Origin);
        i2name.put(41, FetureKeyCnst.TATA_signal);
        i2name.put(43, FetureKeyCnst.Terminator);
        i2name.put(50, fetureKeyService.getFullByName("3'UTR").getName());
        i2name.put(54, fetureKeyService.getFullByName("mRNA").getName());
        i2name.put(60, FetureKeyCnst.Gene);
        i2name.put(61, fetureKeyService.getFullByName("exon").getName());
        i2name.put(86, FetureKeyCnst.MISC_RECOMBINATION);
        i2name.put(88, FetureKeyCnst.MISC_STRUCTURE);
        i2name.put(96, FetureKeyCnst.Variation);
        i2name.put(98, FetureKeyCnst.Source);
        i2name.put(200, FetureKeyCnst.Signal_Sequence);
        i2name.put(203, FetureKeyCnst.Mature_chain);
        i2name.put(205, FetureKeyCnst.Repetitive_region);
        i2name.put(207, FetureKeyCnst.Helical_region);
        i2name.put(208, FetureKeyCnst.Hydrogen_bonded_turn);
        i2name.put(209, FetureKeyCnst.BETA_STRAND_REGION);
        i2name.put(210, FetureKeyCnst.Transmembrane_region);
        i2name.put(235, FetureKeyCnst.Phosphorylation);
        i2name.put(238, FetureKeyCnst.Glycosylation);
        i2name.put(240, FetureKeyCnst.Conflict);
        i2name.put(242, FetureKeyCnst.Splicing_Variant);
        i2name.put(254, FetureKeyCnst.Disulfide);
        i2name.put(266, FetureKeyCnst.REGION_MISC);
        i2name.put(269, FetureKeyCnst.Source);
        i2name.put(270, FetureKeyCnst.Protein);
        i2name.put(274, FetureKeyCnst.OVERHANG);
        i2name.put(1000, FetureKeyCnst.DOMAIN_MISC);
        i2name.put(1001, FetureKeyCnst.Extracellular);
        i2name.put(1003, FetureKeyCnst.Connecting_Peptide);
        i2name.put(1004, FetureKeyCnst.Cytoplasmic);
        i2name.put(1007, FetureKeyCnst.BINDING_SITE_MISC);
    }

    @Override
    public String getFeatureKeyName(Integer i) {        
        if (i2name.isEmpty()){
            initI2key();
        }
        String ret = i2name.get(i);
        if (ret == null) {            
            ret = i.toString();
        }
        return ret;
    }
}
