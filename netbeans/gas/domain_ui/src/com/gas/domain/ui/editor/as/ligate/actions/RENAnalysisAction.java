/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate.actions;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.editor.as.IASEditor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.windows.TopComponent;

/**
 *
 * @author dq
 */
public class RENAnalysisAction extends AbstractAction{

    public RENAnalysisAction(){
        super("Find Restriction Sites");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = UIUtil.getSelectedEditor();
        if(tc == null || !(tc instanceof IASEditor)){
            return;
        }
        IASEditor asEditor = (IASEditor)tc;
        asEditor.displayRENAnalysisPanel();
        asEditor.getMolPane().performRENAnalysis();
    }
    
    public static boolean getEnablement(){
        boolean ret ;
        TopComponent tc = UIUtil.getSelectedEditor();
        if (tc != null) {
            if (tc instanceof IASEditor) {
                IASEditor editor = (IASEditor)tc;
                AnnotatedSeq as = editor.getAnnotatedSeq();
                ret = !editor.getMolPane().isShowingRENResultPage() && as.isNucleotide();
            } else {                
                ret = false;
            }
        } else {            
            ret = false;
        }    
        return ret;
    }
    
}
