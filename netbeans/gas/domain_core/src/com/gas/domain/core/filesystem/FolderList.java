/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.filesystem;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class FolderList extends ArrayList<Folder> {
    public Folder getDepest(){
        Folder ret = null;
        Iterator<Folder> itr = iterator();
        while(itr.hasNext()){
            Folder folder = itr.next();
            if(ret == null || ret.getDepth() < folder.getDepth()){
                ret = folder;
            }
        }
        return ret;
    }
}
