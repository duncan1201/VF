/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.validators;

import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.api.IDirTOPOService;
import com.gas.topo.core.openmol.ChooseDataPanel;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import java.lang.ref.WeakReference;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class DirInsertValidator implements IChooseDataPanelValidator {

    private String errorMsg = "";
    
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IDirTOPOService topoService = Lookup.getDefault().lookup(IDirTOPOService.class);
    
    private WeakReference<ChooseDataPanel> ref;
    
    public DirInsertValidator() {}
    
    public DirInsertValidator(WeakReference<ChooseDataPanel> ref) {
        this.ref = ref;
    }

    @Override
    public boolean validate() {
        boolean ret = true;
        IDirTOPOService.STATE state = null;
        List objs = ref.get().getDynamicTable().getSelectedObjects();
        if (objs == null || objs.isEmpty()) {
            errorMsg = "No data selected";
            ret = false;
        }else{
            AnnotatedSeq as = (AnnotatedSeq) objs.get(0);
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            state = topoService.isInsertValid(full);
            if(state == IDirTOPOService.STATE.NO_CACC_END){
                errorMsg = String.format("The insert has no 'CACC' at the 5' end");;                
            }else if(state == IDirTOPOService.STATE.TOO_MANY_CACC_ENDS){
                errorMsg = "The insert has >= 75% identity to 'CACC' at both 5' ends, and will lose the directionality.";               
            }else if(state == IDirTOPOService.STATE.GT_75_END){
                errorMsg = "The insert has >= 75% identity to 'CACC' at both 5' ends, and will lose the directionality.";
            }else if(state == IDirTOPOService.STATE.NOT_LINEAR){
                errorMsg = String.format("The insert is not linear", "");
            }else if(state == IDirTOPOService.STATE.NOT_BLUNT_ENDED){
                errorMsg = String.format("The insert is not blunt-ended", "");
            }else if(state != IDirTOPOService.STATE.VALID){
                errorMsg = String.format("No a valid directional TOPO%s insert", Unicodes.TRADEMARK);
            }
            ret = state == IDirTOPOService.STATE.VALID;
        }
        if(ret){
            errorMsg = "";
        }
            
        return ret;
    }
    
        @Override
    public void setRef(WeakReference<ChooseDataPanel> ref) {
        this.ref = ref;
    }

 
    @Override
    public String getFriendlyMessage() {
        return errorMsg;
    }
    
}
