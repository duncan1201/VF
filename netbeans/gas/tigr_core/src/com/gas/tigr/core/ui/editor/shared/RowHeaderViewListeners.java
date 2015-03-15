/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.light.Text;
import com.gas.tigr.core.ui.ckpanel.ChromatogramComp2;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 *
 * @author dq
 */
class RowHeaderViewListeners {
    
    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            RowHeaderView src = (RowHeaderView)evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if(name.equals("reads")){
                src.textList.clear();
                List<ChromatogramComp2.Read> reads = (List<ChromatogramComp2.Read>)v;
                for(ChromatogramComp2.Read read: reads){
                    Text text = new Text();
                    String nameSeq = read.getNameNoExt(); 
                    text.setStr(nameSeq);
                    src.textList.add(text);
                }
            }
        }
    }
}
