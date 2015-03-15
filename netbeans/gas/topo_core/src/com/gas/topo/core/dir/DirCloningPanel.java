/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.dir;

import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import com.gas.topo.core.ta.AbstractCloningPanel;
import com.gas.topo.core.validators.DirInsertValidator;
import com.gas.topo.core.validators.DirVectorValidator;

/**
 *
 * @author dq
 */
public class DirCloningPanel extends AbstractCloningPanel {

    public DirCloningPanel(){
    }
    
    @Override
    protected String getVectorContentPanelTitle() {
        return String.format("TOPO%s Vector", Unicodes.TRADEMARK);
    }

    @Override
    protected String getVectorContentPanelTitle(AnnotatedSeq vector) {
        return String.format("TOPO%s Vector: %s", Unicodes.TRADEMARK, vector.getName());
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
    public IChooseDataPanelValidator[] getInsertValidators() {
        IChooseDataPanelValidator[] ret = new IChooseDataPanelValidator[1];
        ret[0] = new DirInsertValidator();
        return ret;
    }

    @Override
    public IChooseDataPanelValidator[] getVectorValidators() {
        IChooseDataPanelValidator[] ret = new IChooseDataPanelValidator[1];
        ret[0] = new DirVectorValidator();
        return ret;
    }
 
}
