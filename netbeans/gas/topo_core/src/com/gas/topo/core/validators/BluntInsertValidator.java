/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.validators;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
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
public class BluntInsertValidator implements IChooseDataPanelValidator {

    private IBluntTOPOService topoService = Lookup.getDefault().lookup(IBluntTOPOService.class);
    private WeakReference<ChooseDataPanel> ref;
    private final String NO_SELECTION = "No data selected";
    private final String NOT_BLUNT_END = "The molecule is not blunt-ended";
    private String errorMsg = "";
    
    @Override
    public void setRef(WeakReference<ChooseDataPanel> ref) {
        this.ref = ref;
    }

    @Override
    public boolean validate() {
        boolean ret = true;
        List list = ref.get().getDynamicTable().getSelectedObjects();
        if(list == null || list.isEmpty()){
            ret = false;
            errorMsg = NO_SELECTION;
        }else if(list.size() == 1){
            AnnotatedSeq as = (AnnotatedSeq)list.get(0);            
            String msg = topoService.isInsertValid(as);
            if(msg != null){
                ret = false;
                errorMsg = msg;
            }
        }
        return ret;
    }

    @Override
    public String getFriendlyMessage() {
        return errorMsg;
    }
    
}
