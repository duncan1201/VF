/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class IFolderElementList extends ArrayList<IFolderElement> {

    public IFolderElementList(){}
    
    public IFolderElementList(IFolderElement fe){
        add(fe);
    }
    
    public IFolderElementList(List<IFolderElement> list){
        addAll(list);
    }
    
    public StringList getNames(){
        StringList ret = new StringList();
        Iterator<IFolderElement> itr = iterator();
        while(itr.hasNext()){
            IFolderElement fe = itr.next();
            ret.add(fe.getName());
        }
        return ret;
    }
    
    public void remove(StringList strList) {
        if(strList.isEmpty()){
            return;
        }
        Iterator<IFolderElement> itr = iterator();
        while (itr.hasNext()) {
            IFolderElement e = itr.next();
            if (strList.containsIgnoreCase(e.getName())) {
                itr.remove();
            }
        }
    }
}
