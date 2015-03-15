/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

import com.gas.domain.ui.editor.IExportActionFactory;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public abstract class AbstractTree extends JComponent implements ITree {

    static IExportActionFactory factory = Lookup.getDefault().lookup(IExportActionFactory.class);
    private JPopupMenu popupMenu;
    
    public AbstractTree() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.isPopupTrigger()){
                    System.out.println();
                    if(popupMenu == null){
                        popupMenu = createPopupMenu();
                    }
                    
                    popupMenu.show(AbstractTree.this, e.getX(), e.getY());
                }
            }
        });                
    }
    
    private JPopupMenu createPopupMenu(){
        JPopupMenu ret = new JPopupMenu();
        Action action = factory.create("Export...");
        ret.add(action);        
        return ret;
    }
}
