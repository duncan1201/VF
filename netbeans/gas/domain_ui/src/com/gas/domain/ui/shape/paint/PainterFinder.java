/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.paint;

import com.gas.domain.core.as.Feture;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PainterFinder {
        
    static final Map<String, IPainter> painterMap = new HashMap<String, IPainter>();
    static IPainter defaultPainter;
    
    static {        
        for(IPainter painter: (Collection<IPainter>)Lookup.getDefault().lookupAll(IPainter.class)){
            painterMap.put(painter.getFetureType(), painter);
            if(painter.isDefault()){
                defaultPainter = painter;                
            }
        }
    }
    
    public static IPainter getDefaultPainter(){
        return defaultPainter;
    }
    
    public static IPainter findPainter(String fetureType){
        if(fetureType == null || fetureType.isEmpty()){
            return defaultPainter;
        }else{
            if(painterMap.containsKey(fetureType)){
                return painterMap.get(fetureType);
            }else{
                return defaultPainter;
            }
        }        
    }
}
