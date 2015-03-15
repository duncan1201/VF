/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui;

import com.gas.domain.ui.ren.api.IRENAnalysisPanel;
import com.gas.domain.ui.ren.api.IRENAnalysisPanelFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IRENAnalysisPanelFactory.class)
public class RENAnalPanelFactory implements IRENAnalysisPanelFactory {

    @Override
    public IRENAnalysisPanel create() {
        IRENAnalysisPanel ret = new RENAnalPanel();
        return ret;
    }
    
}
