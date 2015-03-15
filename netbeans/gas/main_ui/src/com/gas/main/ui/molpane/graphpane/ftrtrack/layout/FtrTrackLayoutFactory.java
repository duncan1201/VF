/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane.ftrtrack.layout;

import java.awt.LayoutManager2;
import java.util.Collection;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class FtrTrackLayoutFactory {
    
    static Collection<IFtrTrackLayout> layouts;
    
    static {
        layouts = (Collection<IFtrTrackLayout>)Lookup.getDefault().lookupAll(IFtrTrackLayout.class);
    }
    
    public static LayoutManager2 getByFetureType(String fetureType){  
        for(Object layout: layouts){
            String ftrType = ((IFtrTrackLayout)layout).getFetureType();
            if(ftrType.equals(fetureType)){
                return (LayoutManager2)layout;
            }
        }
        LayoutManager2 ret = new AdjacentConservedLayout ();
        return ret;
    }
}
