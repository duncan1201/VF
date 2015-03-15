/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.vfaligner.service;

import com.gas.common.ui.util.ReflectHelper;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.vfmsa.AlignType;
import com.gas.domain.core.msa.vfmsa.IVfMsaService;
import com.gas.domain.core.msa.vfmsa.SubMatrix;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SubstitutionMatrixHelper;
import org.biojava3.alignment.template.AlignedSequence;
import org.biojava3.alignment.template.Profile;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.AccessionID;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.template.Compound;
import org.biojava3.core.sequence.template.Sequence;
import org.biojava3.core.util.ConcurrencyTools;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IVfMsaService.class)
public class VfMsaService implements IVfMsaService{
    
    @Override
    public MSA getMSA(Map<String, String> seqs, VfMsaParam param, boolean isAminoAcid){
        MSA ret;
        List<DNASequence> dnaSeqs = new ArrayList<DNASequence>();
        List<ProteinSequence> proteinSeqs = new ArrayList<ProteinSequence>();
        Iterator<String> keyItr = seqs.keySet().iterator();
        while(keyItr.hasNext()){
            String key = keyItr.next();
            String seq = seqs.get(key);
            if(!isAminoAcid){
                DNASequence dnaSeq = new DNASequence(seq);                
                dnaSeq.setAccession(new AccessionID(key));
                dnaSeq.setDNAType(DNASequence.DNAType.UNKNOWN);
                dnaSeqs.add(dnaSeq);
            }else{
                ProteinSequence proteinSeq = new ProteinSequence(seq);
                proteinSeq.setAccession(new AccessionID(key));
                proteinSeqs.add(proteinSeq);
            }
        }        
        if(!dnaSeqs.isEmpty()){
            ret = _getMSA(dnaSeqs, param);
        }else{
            ret = _getMSA(proteinSeqs, param);
        }
        ret.setVfMsaParam(param);
        return ret;
    }
    
    @Override
    public MSA getMSA(List<AnnotatedSeq> seqs, VfMsaParam param){
        Boolean isAminoAcid = null;
        Map<String, String> seqMap = new LinkedHashMap<String, String>();
        for(AnnotatedSeq seq: seqs){       
            if(isAminoAcid == null){
                isAminoAcid = seq.isProtein();
            }
            seqMap.put(seq.getName(), seq.getSiquence().getData());
        }
        return getMSA(seqMap, param, isAminoAcid);
    }
        
    <S extends Sequence<C>, C extends Compound> MSA _getMSA(List<S> seqs, VfMsaParam param){
        MSA ret = new MSA();
        SimpleGapPenalty simpleGapPenalty = new SimpleGapPenalty();
        simpleGapPenalty.setExtensionPenalty((short)param.getExtPenalty());
        simpleGapPenalty.setOpenPenalty((short)param.getOpenPenalty());
        
        Alignments.PairwiseSequenceScorerType scoreType = null;
        if(param.getAlignTypeEnum() == AlignType.SMITH_WATERMAN){
            if(param.isIdenticalOnly()){
                scoreType = Alignments.PairwiseSequenceScorerType.LOCAL_IDENTITIES;
            }else{
                scoreType = Alignments.PairwiseSequenceScorerType.LOCAL_SIMILARITIES;
            }            
        }else if(param.getAlignTypeEnum() == AlignType.NEEDLEMAN_WUNSCH){
            if(param.isIdenticalOnly()){
                scoreType = Alignments.PairwiseSequenceScorerType.GLOBAL_IDENTITIES;
            }else{
                scoreType = Alignments.PairwiseSequenceScorerType.GLOBAL_SIMILARITIES;
            }
        }else{
            throw new IllegalArgumentException();
        }
        SubMatrix subMatrix = param.getMatrix();
        String matrixName = param.getMatrix().getInternalName();
        SubstitutionMatrix matrix;
        if(subMatrix.isAminoAcid()){
            Method method = ReflectHelper.getDeclaredMethod(SubstitutionMatrixHelper.class, "getAminoAcidCompoundSubstitutionMatrix", String.class);
            method.setAccessible(true);
            matrix = (SubstitutionMatrix)ReflectHelper.invoke(null, method, matrixName);
        }else {
            Method method = ReflectHelper.getDeclaredMethod(SubstitutionMatrixHelper.class, "getNucleotideCompoundSubstitutionMatrix", String.class);
            method.setAccessible(true);
            matrix = (SubstitutionMatrix)ReflectHelper.invoke(null, method, matrixName);
        }
        
        Object csSeqs = seqs.get(0).getCompoundSet();
        Object csMatrix = matrix.getCompoundSet();
        
        List<Object> settingList = new ArrayList<Object>();
        settingList.add(simpleGapPenalty);
        settingList.add(scoreType);
        if(csSeqs == csMatrix){
            settingList.add(matrix);
        }        
        
        Profile<S, C> profile = Alignments.getMultipleSequenceAlignment(seqs, settingList.toArray(new Object[settingList.size()]));
        
        ConcurrencyTools.shutdown();
        
        int size = profile.getSize();
        for(int i = 1; i <= size; i++){
            AlignedSequence<S, C> seq = profile.getAlignedSequence(i);
            AccessionID accession = seq.getAccession();
            String id = accession.getID();
            String str = seq.getSequenceAsString();
            ret.addEntry(id, str);
        }
        
        return ret;
    }
}