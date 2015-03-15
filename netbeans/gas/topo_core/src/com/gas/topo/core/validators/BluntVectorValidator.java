/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.validators;

import com.gas.common.ui.util.Unicodes;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.api.IBluntTOPOService;
import com.gas.topo.core.openmol.ChooseDataPanel;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import java.lang.ref.WeakReference;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class BluntVectorValidator implements IChooseDataPanelValidator {

    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IBluntTOPOService topoService = Lookup.getDefault().lookup(IBluntTOPOService.class);
    private WeakReference<ChooseDataPanel> ref;
    private String errorMsg;
    private final String NO_SELECTION = "No data selected";
    private final String NOT_VALID_VECTOR = String.format("Not a valid zero blunt TOPO%s vector", Unicodes.TRADEMARK);

    @Override
    public void setRef(WeakReference<ChooseDataPanel> ref) {
        this.ref = ref;
    }

    @Override
    public boolean validate() {
        boolean ret = true;
        List list = ref.get().getDynamicTable().getSelectedObjects();
        if (list == null || list.isEmpty()) {
            ret = false;
            errorMsg = NO_SELECTION;
        } else {
            final AnnotatedSeq vector = (AnnotatedSeq) list.get(0);
            final AnnotatedSeq full = asService.getFullByHibernateId(vector.getHibernateId());
            errorMsg = topoService.isVectorValid(full);
            ret = errorMsg == null || errorMsg.isEmpty();

        }
        return ret;
    }

    @Override
    public String getFriendlyMessage() {
        return errorMsg;
    }
}
