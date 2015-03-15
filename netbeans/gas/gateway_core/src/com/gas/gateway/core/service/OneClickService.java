/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.Operation;
import com.gas.gateway.core.service.api.IBPService;
import com.gas.gateway.core.service.api.IGWValidateService;
import com.gas.gateway.core.service.api.ILRService;
import com.gas.gateway.core.service.api.IOneClickService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IOneClickService.class)
public class OneClickService implements IOneClickService{
    
    private IBPService bpService = Lookup.getDefault().lookup(IBPService.class);
    private ILRService lrService = Lookup.getDefault().lookup(ILRService.class);
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IGWValidateService validateService = Lookup.getDefault().lookup(IGWValidateService.class);
    
    /*
     * PCR inserts(B site) + donor vectors(p sites) + destination vector(r sites) 
     */
    @Override
    public AnnotatedSeq createExpressionClone(List<AnnotatedSeq> seqs){
        List<AnnotatedSeq> seqsNoDest = new ArrayList<AnnotatedSeq>(seqs);
        Integer destIndex = validateService.indexOfDestVector(seqs);
        AnnotatedSeq dest = seqsNoDest.remove(destIndex.intValue());
        List<AnnotatedSeq> entryClones = bpService.createEntryClones(seqsNoDest, false);
        
        AnnotatedSeq exp = lrService.createExpressionClone(new AnnotatedSeqList(entryClones), dest, false);
        
        Operation operation = new Operation();
        operation.setDate(new Date());
        operation.setNameEnum(Operation.NAME.GW_ONE_CLICK);
        
        for(AnnotatedSeq seq: seqs){
            String folderPath = folderService.loadWithParents(seq.getFolder().getHibernateId()).getAbsolutePath();
            String abPath = String.format("%s\\%s", folderPath, seq.getName());
            Operation.Participant p = new Operation.Participant(abPath);
            operation.addParticipant(p);
        }
        
        exp.setOperation(operation);
        return exp;
    }
}
