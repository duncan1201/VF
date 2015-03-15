/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Operation;
import com.gas.topo.core.api.IBluntTOPOService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IBluntTOPOService.class)
public class BluntTOPOService implements IBluntTOPOService {

    private final String DESC_FORMAT = "a recombinant vector created from %s and %s";
    private final String NOT_VALID_VECTOR = String.format("<html>Not a valid blunt TOPO%s vector. Sequence <b>5´-CCCTT-3'</b> not found at both ends</html>", Unicodes.TRADEMARK);

    @Override
    public String isVectorValid(final AnnotatedSeq as) {        
        return isVectorValid(as, String.class);
    }

    private boolean containsCCCTT(String seq) {
        return seq.equalsIgnoreCase("aaggg") || seq.equalsIgnoreCase("ccctt");
    }

    @Override
    public List<AnnotatedSeq> clone(AnnotatedSeq insert, AnnotatedSeq vector) {
        IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        Operation operation = new Operation();
        operation.setNameEnum(Operation.NAME.TOPO);
        operation.setDate(new Date());
        final String insertFolderPath = folderService.loadWithParents(insert.getFolder().getHibernateId()).getAbsolutePath();
        final String vectorFolderPath = folderService.loadWithParents(vector.getFolder().getHibernateId()).getAbsolutePath();

        Operation.Participant partInsert = new Operation.Participant(insertFolderPath + "\\" + insert.getName(), true);
        operation.addParticipant(partInsert);
        Operation.Participant partVector = new Operation.Participant(vectorFolderPath + "\\" + vector.getName(), true);
        operation.addParticipant(partVector);

        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        AnnotatedSeq clone = AsHelper.concatenate(insert, vector);
        clone = AsHelper.circularize(clone);
        clone.setOperation(operation.clone());
        clone.setDesc(String.format(DESC_FORMAT, insert.getName(), vector.getName()));
        ret.add(clone);
        annotateFeatures(clone, insert, vector);

        final AnnotatedSeq flipped = insert.flip();
        AnnotatedSeq cloneR = AsHelper.concatenate(flipped, vector);
        cloneR = AsHelper.circularize(cloneR);
        cloneR.setOperation(operation.clone());
        cloneR.setDesc(String.format(DESC_FORMAT, insert.getName(), vector.getName()));
        annotateFeatures(cloneR, flipped, vector);
        ret.add(cloneR);
        return ret;
    }

    private void annotateFeatures(AnnotatedSeq clone, AnnotatedSeq insert, AnnotatedSeq vector) {
        Feture source = new Feture();
        source.setKey(FetureKeyCnst.Parent);
        source.getQualifierSet().add(String.format("sub_clone=%s", insert.getName()));
        Lucation lucation = new Lucation(1, insert.getLength(), true);
        source.setLucation(lucation);
        clone.getFetureSet().add(source);

        source = new Feture();
        source.setKey(FetureKeyCnst.Parent);
        source.getQualifierSet().add(String.format("sub_clone=%s", vector.getName()));
        clone.getFetureSet().add(source);
        lucation = new Lucation(insert.getLength() + 1, insert.getLength() + vector.getLength(), true);
        source.setLucation(lucation);
    }

    @Override
    public String isInsertValid(AnnotatedSeq insert) {
        return isInsertValid(insert, String.class);
    }

    @Override
    public <T> T isVectorValid(AnnotatedSeq as, Class<T> retType) {
        T ret = null;
        String msg = null;
        final String data = as.getSiquence().getData();
        if (data.length() < 6) {
            msg = NOT_VALID_VECTOR;
            return ret;
        }
        final String startSeq = as.getSiquence().getData(1, 5);
        final String endSeq = as.getSiquence().getData(as.getLength() - 4, as.getLength());
        if (as.isCircular()) {
            msg = String.format(CNST.ONE_LINE_ERROR_FORMAT, "The vector is not linearized", "Please linearize the vector");
            if(String.class.isAssignableFrom(retType)){
                ret = (T)msg;
            }else if(IBluntTOPOService.STATE.class.isAssignableFrom(retType)){
                ret = (T)IBluntTOPOService.STATE.NOT_LINEAR;
            }
        } else if(as.getOverhangSize() > 0){
            msg = "The vector is not blunt-ended";
            if(String.class.isAssignableFrom(retType)){
                ret = (T)msg;
            }else if(IBluntTOPOService.STATE.class.isAssignableFrom(retType)){
                ret = (T)IBluntTOPOService.STATE.NOT_BLUNT_ENDED;
            }
        } else if (!containsCCCTT(startSeq) || !containsCCCTT(endSeq) || as.getOverhangSize() > 0) {
            msg = NOT_VALID_VECTOR;
            if(String.class.isAssignableFrom(retType)){
                ret = (T)msg;
            }else if(IBluntTOPOService.STATE.class.isAssignableFrom(retType)){
                ret = (T)IBluntTOPOService.STATE.NO_CCCTT;
            }
        }else {
            if(IBluntTOPOService.STATE.class.isAssignableFrom(retType)){
                ret = (T)IBluntTOPOService.STATE.VALID;
            }
        }
        return ret;
    }

    @Override
    public <T> T isInsertValid(AnnotatedSeq insert, Class<T> retType) {
        T ret2 = null;
        String ret = null;
        final Overhang startOverhang = insert.getStartOverhang();
        final Overhang endOverhang = insert.getEndOverhang();
        if (insert.isCircular()) {
            ret = String.format(CNST.ONE_LINE_ERROR_FORMAT, "The insert is not linear", "Please choose a linear blunt-ended DNA");
            if (String.class.isAssignableFrom(retType)) {
                ret2 = (T) ret;
            } else if (IBluntTOPOService.STATE.class.isAssignableFrom(retType)) {
                ret2 = (T) IBluntTOPOService.STATE.NOT_LINEAR;
            }
        } else if (startOverhang != null || endOverhang != null) {
            ret = String.format(CNST.ONE_LINE_ERROR_FORMAT, "The insert is not blunt-ended", "Please choose a linear blunt-ended DNA");
            if (String.class.isAssignableFrom(retType)) {
                ret2 = (T) ret;
            } else if (IBluntTOPOService.STATE.class.isAssignableFrom(retType)) {
                ret2 = (T) IBluntTOPOService.STATE.NOT_BLUNT_ENDED;
            }
        } else {
            if (IBluntTOPOService.STATE.class.isAssignableFrom(retType)) {
                ret2 = (T) IBluntTOPOService.STATE.VALID;
            }
        }
        return ret2;
    }
}
