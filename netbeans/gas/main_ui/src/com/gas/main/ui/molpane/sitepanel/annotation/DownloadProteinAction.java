/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.util.api.IEFetchService;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class DownloadProteinAction extends AbstractAction {
    
    IEFetchService service = Lookup.getDefault().lookup(IEFetchService.class);
    
    private String db;
    private String id;    
    
    public DownloadProteinAction(){
        service.setDb("protein");
        service.setIds("NP_001193471.1");        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> asList = service.sendRequest(AnnotatedSeq.class);
        if(!asList.isEmpty()){
            OpenASEditorAction action = new OpenASEditorAction(asList.get(0));
            action.actionPerformed(null);
        }
    }
}
