/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.validators;

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
public class TAVectorValidator implements IChooseDataPanelValidator {

    private String errorMsg = "";
    private final String NO_DATA_SELECTED = "No data selected";
    private final String NOT_DNA = "The selection is not a DNA";
    private final String NOT_TOPO_TA_VECTOR = String.format("The selection is not a valid TOPO%s TA vector", Unicodes.TRADEMARK);
    private WeakReference<ChooseDataPanel> ref;
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private ITOPOTAService topoTaService = Lookup.getDefault().lookup(ITOPOTAService.class);

    public TAVectorValidator(){}
    
    public TAVectorValidator(WeakReference<ChooseDataPanel> ref) {
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
            errorMsg = NO_DATA_SELECTED;
            ret = false;
        } else if (objs.size() == 1) {
            Object obj = objs.get(0);
            if (!(obj instanceof AnnotatedSeq)) {
                errorMsg = NOT_DNA;
                ret = false;
            } else {
                AnnotatedSeq as = (AnnotatedSeq) obj;
                AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
                String msg = topoTaService.isVectorValid(full);
                if (msg != null) {
                    errorMsg = msg;
                    ret = false;
                }
            }
        }
        if (ret) {
            errorMsg = "";
        }
        return ret;
    }

    @Override
    public String getFriendlyMessage() {
        return errorMsg;
    }
}
