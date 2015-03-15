/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.ta;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.topo.core.openmol.ChooseDataPanel;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class AbstractCloningPanelActions {
    static class OpenInsertAction extends AbstractAction{

        private AbstractCloningPanel cloningPanel;
        
        OpenInsertAction(AbstractCloningPanel panel, String text, Icon icon){
            super(text, icon);    
            this.cloningPanel = panel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            IAnnotatedSeqService service = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
            final ChooseDataPanel panel = new ChooseDataPanel();
            IChooseDataPanelValidator[] validator = cloningPanel.getInsertValidators();
            panel.setValidators(Arrays.asList(validator));
            panel.setIncludeTypes(Folder.TYPE.DNA, Folder.TYPE.RNA);
            DialogDescriptor descriptor = new DialogDescriptor(panel, "Choose Insert");
            panel.setDialogDescriptor(descriptor);
            panel.validateInput();
            
            Integer answer = (Integer)DialogDisplayer.getDefault().notify(descriptor);
            if(answer.equals(DialogDescriptor.OK_OPTION)){
                List list = panel.getDynamicTable().getSelectedObjects();
                if(list.size() >= 1){
                    AnnotatedSeq as = (AnnotatedSeq)list.get(0);
                    AnnotatedSeq full = service.getFullByHibernateId(as.getHibernateId());
                    cloningPanel.setInsert(full); 
                    cloningPanel.validateInput();
                }
            }
        }
    }
    
    static class OpenVectorAction extends AbstractAction{

        private IAnnotatedSeqService service = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
        private AbstractCloningPanel cloningPanel;
        
        OpenVectorAction(AbstractCloningPanel panel, String text, Icon icon){
            super(text, icon);
            this.cloningPanel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ChooseDataPanel panel = new ChooseDataPanel();
            IChooseDataPanelValidator[] validators = cloningPanel.getVectorValidators();
            panel.setValidators(Arrays.asList(validators));
            panel.setIncludeTypes(Folder.TYPE.DNA, Folder.TYPE.RNA);
            DialogDescriptor descriptor = new DialogDescriptor(panel, "Choose Vector");
            panel.setDialogDescriptor(descriptor);
            panel.validateInput();
            
            Integer answer = (Integer)DialogDisplayer.getDefault().notify(descriptor);
            if(answer.equals(DialogDescriptor.OK_OPTION)){
                List list = panel.getDynamicTable().getSelectedObjects();
                if(list.size() >= 1){
                    AnnotatedSeq as = (AnnotatedSeq)list.get(0);
                    AnnotatedSeq full = service.getFullByHibernateId(as.getHibernateId());
                    cloningPanel.setVector(full);
                    cloningPanel.validateInput();
                }
            }
        }
    }    
}
