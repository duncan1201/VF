/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.blunt;

import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import com.gas.topo.core.ta.AbstractCloningPanel;
import com.gas.topo.core.validators.BluntInsertValidator;
import com.gas.topo.core.validators.BluntVectorValidator;

/**
 *
 * @author dq
 */
class BluntCloningPanel extends AbstractCloningPanel {

    BluntCloningPanel(){
    }
    
    @Override
    protected String getVectorContentPanelTitle() {
        String ret = String.format("TOPO%s Vector", Unicodes.TRADEMARK);
        //String ret = "Vector";
        return ret;
    }

    @Override
    protected String getVectorContentPanelTitle(AnnotatedSeq vector) {
        String ret = String.format("TOPO%s Vector: %s", Unicodes.TRADEMARK, vector.getName());
        //String ret = String.format("Vector: %s", vector.getName());
        return ret;
    }

    @Override
    protected String getInsertContentPanelTitle() {
        String ret = String.format("TOPO%s Insert", Unicodes.TRADEMARK);
        //String ret = "Insert";
        return ret;
    }

    @Override
    protected String getInsertContentPanelTitle(AnnotatedSeq insert) {
        String ret = String.format("TOPO%s Insert: %s", Unicodes.TRADEMARK, insert.getName());
        //String ret = String.format("Insert: %s", insert.getName());
        return ret;
    }

    @Override
    public IChooseDataPanelValidator[] getInsertValidators() {
        IChooseDataPanelValidator[] ret = new IChooseDataPanelValidator[1];
        ret[0] = new BluntInsertValidator();
        return ret;
    }

    @Override
    public IChooseDataPanelValidator[] getVectorValidators() {
        IChooseDataPanelValidator[] ret = new IChooseDataPanelValidator[1];
        ret[0] = new BluntVectorValidator();
        return ret;
    }
}
