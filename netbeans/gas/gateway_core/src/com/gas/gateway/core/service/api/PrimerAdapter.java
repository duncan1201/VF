/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.ReflectHelper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dq
 */
public class PrimerAdapter implements Cloneable {
    
    public static final PrimerAdapter attB1 = new PrimerAdapter(AttSite.attB1, true, "GGGG", "TA", new String[]{"AA", "AG", "GA"});
    public static final PrimerAdapter attB1r = new PrimerAdapter(AttSite.attB1r, false, "G", "CCCC");
    //public static final PrimerAdapter attB1r_reverse = new PrimerAdapter(AttSite.attB1r, false, "N", "CCCC");
    public static final PrimerAdapter attB2 = new PrimerAdapter(AttSite.attB2.flip(), false, "T", "CCCC");
    public static final PrimerAdapter attB2r = new PrimerAdapter(AttSite.attB2r.flip(), true, "GGGG", "TA");
    public static final PrimerAdapter attB3 = new PrimerAdapter(AttSite.attB3, true, "GGGG", "CT");
    public static final PrimerAdapter attB3_3FRAG = new PrimerAdapter(AttSite.attB3.flip(), false, "C", "CCCC");
    public static final PrimerAdapter attB3r = new PrimerAdapter(AttSite.attB3r, false, "A", "CCCC");
    public static final PrimerAdapter attB4r = new PrimerAdapter(AttSite.attB4r.flip(), true, "GGGG", "CT");
    public static final PrimerAdapter attB4 = new PrimerAdapter(AttSite.attB4.flip(), false, "C", "CCCC");
    public static final PrimerAdapter attB4_3FRAG = new PrimerAdapter(AttSite.attB4_3FRAG, true, "GGGG", "TA");
    public static final PrimerAdapter attB5 = new PrimerAdapter(AttSite.attB5, true, "GGGG", "TG");
    public static final PrimerAdapter attB5r = new PrimerAdapter(AttSite.attB5r, false, "A", "CCCC");   
    
    private AttSite attSite;
    private boolean forward;
    private String prefix;
    private String postfix;
    private String[] forbidden;
    
    public PrimerAdapter() {}
    
    public PrimerAdapter(AttSite attSite, boolean forward, String prefix, String postfix){
        this(attSite, forward, prefix, postfix, null);
    }
    
    public PrimerAdapter(AttSite attSite, boolean forward, String prefix, String postfix, String[] forbidden){
        this.attSite = attSite;
        this.forward = forward;
        this.prefix = prefix;
        this.postfix = postfix;
        this.forbidden = forbidden;
    }
    
    @Override
    public PrimerAdapter clone(){
        PrimerAdapter ret = new PrimerAdapter();
        ret.attSite = this.attSite.clone();
        ret.forward = this.forward;
        ret.prefix = this.prefix;
        ret.postfix = this.postfix;
        if(this.forbidden != null){
            ret.forbidden = Arrays.copyOf(this.forbidden, this.forbidden.length);
        }
        return ret;
    }
    
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }    
    
    public static List<PrimerAdapter> getAll(){
        List<PrimerAdapter> ret = new ArrayList<PrimerAdapter>();
        List<Field> fields = ReflectHelper.getDeclaredFields(PrimerAdapter.class, PrimerAdapter.class, Boolean.TRUE);
        for(Field fd: fields){
            PrimerAdapter adapter = ReflectHelper.getStaticQuietly(fd, PrimerAdapter.class);
            ret.add(adapter);
        }
        return ret;
    }

    public AttSite getAttSite() {
        return attSite;
    }    

    public boolean isForward() {
        return forward;
    }
    
    public String getSeq(){
        StringBuilder ret = new StringBuilder();
        ret.append(prefix);
        ret.append(attSite.getSeq());
        ret.append(postfix);
        return ret.toString();
    }
    
    public String[] getForbidden(){
        return this.forbidden;
    }
    
    public String getName(){
        return attSite.getName();
    }

    public String getPostfix() {
        return postfix;
    }

    public String getPrefix() {
        return prefix;
    }
    
}
