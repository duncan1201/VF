/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.ta;

import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import com.gas.topo.core.validators.TAInsertValidator;
import com.gas.topo.core.validators.TAVectorValidator;
import java.awt.Insets;
import javax.swing.BorderFactory;

/**
 *
 * @author dq
 */
public class TACloningPanel extends AbstractCloningPanel {

    public TACloningPanel(){
    }
    
    @Override
    protected String getVectorContentPanelTitle() {
        return String.format("TOPO%s Vector", Unicodes.TRADEMARK);
    }

    @Override
    protected String getInsertContentPanelTitle() {
        return String.format("TOPO%s Insert", Unicodes.TRADEMARK);
    }

    @Override
    protected String getInsertContentPanelTitle(AnnotatedSeq insert) {
        return String.format("TOPO%s Insert: %s", Unicodes.TRADEMARK, insert.getName());
    }

    @Override
    protected String getVectorContentPanelTitle(AnnotatedSeq vector) {
        return String.format("TOPO%s Vector: %s", Unicodes.TRADEMARK, vector.getName());
    }

    @Override
    public IChooseDataPanelValidator[] getInsertValidators() {
        IChooseDataPanelValidator[] ret = new IChooseDataPanelValidator[1];
        ret[0] = new TAInsertValidator();
        return ret;
    }

    @Override
    public IChooseDataPanelValidator[] getVectorValidators() {
        IChooseDataPanelValidator[] ret = new IChooseDataPanelValidator[1];
        ret[0] = new TAVectorValidator();
        return ret;
    }
}
