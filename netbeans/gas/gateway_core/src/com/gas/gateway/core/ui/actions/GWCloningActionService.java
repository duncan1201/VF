/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.actions;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.banner.IGWCloningActionService;
import java.util.List;
import javax.swing.Action;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IGWCloningActionService.class)
public class GWCloningActionService implements IGWCloningActionService{

    @Override
    public Action createAnnotateAttSitesAction() {
        return new AnnotateAttSitesAction();
    }

    @Override
    public Action createAddAttBSitesAction() {
        return new AddAttBSitesAction();
    }

    @Override
    public Action createOneClickAction() {
        return new OneClickAction();
    }

    @Override
    public Action createLRAction() {
        return new LRAction();
    }

    @Override
    public Action createBPAction() {
        return new BPAction();
    }

    @Override
    public boolean getAddAttBSitesActionEnablement() {
        int linearNucleotideCount = 0;
        
        List<AnnotatedSeq> checkedASs = BannerTC.getInstance().getSelectedObjects(AnnotatedSeq.class);
        for (AnnotatedSeq obj : checkedASs) {
            AnnotatedSeq as = (AnnotatedSeq) obj;
            if (AsHelper.isNucleotide(as)) {
                if (!as.isCircular()) {
                    linearNucleotideCount++;
                }
            }
        }
        return linearNucleotideCount > 0;
    }
    
}
