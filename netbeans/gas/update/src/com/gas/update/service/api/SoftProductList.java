/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service.api;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class SoftProductList extends ArrayList<SoftProduct>{
    
    public SoftProductList(){}
    
    public SoftProductList(JSONArray a){
        Iterator itr = a.iterator();
        while(itr.hasNext()){
            JSONObject o = (JSONObject)itr.next();
            SoftProduct sp = new SoftProduct(o);
            add(sp);
        }
    }
    
    public SoftProductList filter(String name, Boolean win64, Boolean win32, Boolean mac, String curVersion){
        SoftProductList ret = new SoftProductList();
        for(SoftProduct p: this){
            if(win64 != null && win64 && !p.getSupportWin64()){
                continue;
            }
            if(win32 != null && win32 && !p.getSupportWin32()){
                continue;
            }
            if(mac != null && mac && !p.getSupportMac()){
                continue;
            }
            ISoftProductService spSvc = Lookup.getDefault().lookup(ISoftProductService.class);            
            if(curVersion != null && spSvc.compareEdition(curVersion, p.getEdition()) >= 0){
                continue;
            }
            if(name != null && !name.equalsIgnoreCase(p.getName())){
                continue;
            }
            ret.add(p);
        }
        return ret;
    }
    
    public SoftProduct getLatestEdition(){
        ISoftProductService svc = Lookup.getDefault().lookup(ISoftProductService.class);
        SoftProduct ret = null;
        for(SoftProduct p: this){
            if(ret == null){
                ret = p;
            }else{
                int compare = svc.compareEdition(ret, p);
                if(compare < 0){
                    ret = p;
                }
            }
        }
        return ret;
    }
}
