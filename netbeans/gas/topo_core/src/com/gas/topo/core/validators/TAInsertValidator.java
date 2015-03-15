/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.validators;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.api.ITOPOTAService;
import com.gas.topo.core.openmol.ChooseDataPanel;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import java.lang.ref.WeakReference;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class TAInsertValidator implements IChooseDataPanelValidator {

    private WeakReference<ChooseDataPanel> ref;
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private ITOPOTAService taTopoService = Lookup.getDefault().lookup(ITOPOTAService.class);
    private String errorMsg = "";
    private String INVALID_TA_INSERT_ERROR_MSG = String.format("Not a valid TOPO%s TA Insert", Unicodes.TRADEMARK);
    private String NO_SELECTION_ERROR_MSG = "No data selected";

    public TAInsertValidator() {}
    
    public TAInsertValidator(WeakReference<ChooseDataPanel> ref) {
        this.ref = ref;
    }

    @Override
    public void setRef(WeakReference<ChooseDataPanel> ref) {
        this.ref = ref;
    }
    
    @Override
    public boolean validate() {
        boolean ret = true;
        List objs = ref.get().getDynamicTable().getSelectedObjects();
        if (objs == null || objs.isEmpty()) {
            errorMsg = NO_SELECTION_ERROR_MSG;
            ret = false;
        } else if (objs.size() == 1) {
            
            AnnotatedSeq as = (AnnotatedSeq) objs.get(0);
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            ITOPOTAService.STATE state = taTopoService.isInsertValid(full, ITOPOTAService.STATE.class);
            if(state == ITOPOTAService.STATE.NOT_LINEAR){
                errorMsg = String.format(CNST.ONE_LINE_ERROR_FORMAT, "Not linear", ITOPOTAService.INSERT_ONE_LINE_INSTRUCTION);
                ret = false;
            }else if(state == ITOPOTAService.STATE.NO_3_A_OVERHANGS){
                errorMsg = String.format(CNST.ONE_LINE_ERROR_FORMAT, "no 3' A overhangs at both ends", ITOPOTAService.INSERT_ONE_LINE_INSTRUCTION);
                ret = false;
            }
        }

        return ret;
    }

    @Override
    public String getFriendlyMessage() {
        return errorMsg;
    }
}
