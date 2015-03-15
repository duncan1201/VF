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
public class DirVectorValidator implements IChooseDataPanelValidator {

    private String errorMsg = "";
    private final String NO_SELECTION_ERROR_MSG = "No data selected";
    private final String NOT_A_VALID_VECTOR = String.format("Not a valid directional TOPO%s vector", Unicodes.TRADEMARK);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IDirTOPOService topoService = Lookup.getDefault().lookup(IDirTOPOService.class);
    private WeakReference<ChooseDataPanel> ref;
    
    public DirVectorValidator(){}
    
    public DirVectorValidator(WeakReference<ChooseDataPanel> ref){
        this.ref = ref;
    }
    
    @Override
    public boolean validate() {
        boolean ret = false;
        List objs = ref.get().getDynamicTable().getSelectedObjects();
        if (objs == null || objs.isEmpty()) {
            errorMsg = NO_SELECTION_ERROR_MSG;
            ret = false;
        }else{
            AnnotatedSeq as = (AnnotatedSeq) objs.get(0);
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            IDirTOPOService.STATE state = topoService.isVectorValid(full);
            if(state == IDirTOPOService.STATE.NOT_LINEAR){
                errorMsg = "The selection is not linearized";                
            }else if(state == IDirTOPOService.STATE.TOO_MANY_OVERHANGSs){
                errorMsg = "The selection has two overhangs";                
            }else if(state == IDirTOPOService.STATE.NO_VALID_OVERHANG || state == IDirTOPOService.STATE.NO_OVERHANG){
                errorMsg = "The selection does not contain one and only one 3' overhang 5'-GGTG-3'";                
            }else if(state == IDirTOPOService.STATE.NO_CCCTT){
                errorMsg = "The selection does not contain sequences 5'-CCCTT-3' at both ends";
            }else if(state != IDirTOPOService.STATE.VALID){
                errorMsg = NOT_A_VALID_VECTOR;                
            }
            ret = IDirTOPOService.STATE.VALID == state;
            
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
