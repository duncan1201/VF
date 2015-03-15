/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.layout;

import com.gas.domain.core.as.Feture;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;

/**
 *
 * @author liaodu
 */
public class LayoutHelperFinder {
    
    static final Map<String, ILayoutHelper> layoutHelpers = new HashMap<String, ILayoutHelper>();
    static ILayoutHelper defaultHelper = null;
    
    static{        
        Collection<ILayoutHelper> tmp = (Collection<ILayoutHelper>)Lookup.getDefault().lookupAll(ILayoutHelper.class);
        for(ILayoutHelper helper: tmp){
            layoutHelpers.put(helper.getFetureType(), helper);
            if(helper.isDefault()){
                defaultHelper = helper;
            }
        }
    }
    
    public static ILayoutHelper findDefault(){
        return defaultHelper;
    }
    
    public static ILayoutHelper find(Feture feture){     
        if(feture != null){
            boolean contains = layoutHelpers.containsKey(feture.getKey());
            if(contains){
                return layoutHelpers.get(feture.getKey()).newInstance();
            }else{
                return defaultHelper.newInstance();
            }
        }else{
            return defaultHelper.newInstance();
        }        
    }
}
