/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.OverlapPrimer;
import com.gas.domain.ui.editor.as.gibson.PrimersPreview.DoubleBar;
import com.gas.domain.ui.editor.as.gibson.PrimersPreview.OligoShape;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 *
 * @author dq
 */
class PrimersPreviewListeners {
    
    static class MouseAdpt extends MouseAdapter {
        
        PrimersPreview primersPreview;
        
        MouseAdpt(PrimersPreview primersPreview){
            this.primersPreview = primersPreview;
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            GibsonAssemblyPanel panel = UIUtil.getParent(primersPreview, GibsonAssemblyPanel.class);
            DoubleBar doubleBar = primersPreview.bars.getDoubleBar(e.getPoint());
            OligoShape os = primersPreview.oligoShapes.getOligoShape(e.getPoint()); 
            if(doubleBar != null) {
                primersPreview.bars.setSelected(doubleBar);
                //primersPreview.setSelectedPrimerPair(doubleBar.as);
                primersPreview.oligoShapes.setSelected(null);
                panel.showCenterPanel(GibsonAssemblyPanel.SETTINGS_PANEL);
            } else if(os != null) {
                primersPreview.bars.setSelected(null);
                primersPreview.oligoShapes.setSelected(os);
                panel.primersTable.setSelectedRow(os.as.getName(), os.forward);
                List<OverlapPrimer> overlapPrimers = panel.getOverlapPrimers();
                if(overlapPrimers != null && !overlapPrimers.isEmpty()){
                    panel.showCenterPanel(GibsonAssemblyPanel.PRIMERS_DETAILS_PANEL);
                }
            } else {
                primersPreview.bars.setSelected(null);
                primersPreview.oligoShapes.clearSelection();
                panel.primersTable.clearSelection();
                panel.showCenterPanel(GibsonAssemblyPanel.SETTINGS_PANEL);
            }
            
            panel.moveLeftBtn.setEnabled(doubleBar != null);
            panel.moveRightBtn.setEnabled(doubleBar != null);
            primersPreview.repaint();
        }
        
        @Override
        public void mouseMoved(MouseEvent e){
            DoubleBar doubleBar = primersPreview.bars.getDoubleBar(e.getPoint());
            OligoShape os = primersPreview.oligoShapes.getOligoShape(e.getPoint());
            if(doubleBar != null || os != null){
                primersPreview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else{                
                primersPreview.setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}