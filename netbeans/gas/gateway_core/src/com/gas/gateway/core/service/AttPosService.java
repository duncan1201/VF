/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service;

import com.gas.gateway.core.service.api.AttSite;
import com.gas.gateway.core.service.api.IAttPosService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IAttPosService.class)
public class AttPosService implements IAttPosService {

    private Reaction[] reactions = {
        // BP reactions
        new Reaction(AttSite.attB1, AttSite.attP1_pDNOR221, 9, 83), // pro1
        new Reaction(AttSite.attB1, AttSite.attP1_pro, 9, 83),// pro2
        new Reaction(AttSite.attB2, AttSite.attP2_pDONR221, 18, 151), // pro1
        new Reaction(AttSite.attB5r, AttSite.attP5, 14, 89), //pro2
        new Reaction(AttSite.attB5, AttSite.attP5, 9, 83), //pro2, pro4
        new Reaction(AttSite.attB4, AttSite.attP4, 18, 151), //pro3, pro4
        new Reaction(AttSite.attB4r, AttSite.attP4, 10, 145), //pro3
        new Reaction(AttSite.attB3r, AttSite.attP3, 15, 90), //pro3
        new Reaction(AttSite.attB3, AttSite.attP3, 9, 83), //pro3              
        new Reaction(AttSite.attB4_3FRAG, AttSite.attP4_3FRAG, 9, 83), //3 frag
        new Reaction(AttSite.attB1r, AttSite.attP1r, 15, 90), //3 frag
        new Reaction(AttSite.attB2r, AttSite.attP2_3FRAG, 9, 144), //3 frag      
        new Reaction(AttSite.attB3, AttSite.attP3_3FRAG, 15, 151), //3 frag  
        
        // LR reactions
        new Reaction(AttSite.attL1_221, AttSite.attR1, 84, 9), //pro 1
        
        new Reaction(AttSite.attL1_pro, AttSite.attR1, 83, 9), //pro 2
        new Reaction(AttSite.attL2, AttSite.attR2, 15, 118), //pro 2
        new Reaction(AttSite.attR5, AttSite.attL5, 8, 83), //pro 2
        
        // pro 3
        new Reaction(AttSite.attL4, AttSite.attR4_pro, 8, 111), //pro 3      
        new Reaction(AttSite.attR3_pro, AttSite.attL3, 8, 83), //pro 3 
        
        // pro 4 no new recations for pro4
        
        // 3 frag
        new Reaction(AttSite.attR1_3FRAG, AttSite.attL1_221, 15, 91), //3 frag
        new Reaction(AttSite.attL2, AttSite.attR2_3FRAG, 8, 144), //3 frag
        new Reaction(AttSite.attL4_3FRAG, AttSite.attR4_pro, 83, 8), //3 frag  
        new Reaction(AttSite.attL3_3FRAG, AttSite.attR3_3FRAG, 8, 112), //3 frag 
    }; 

    @Override
    public Integer getAttPos(AttSite site, AttSite site2) {
        Reaction reaction = getReaction(site, site2);
        if (reaction != null) {
            return reaction.getAttPos();
        } else {
            return null;
        }
    }

    @Override
    public Integer getAttPos2(AttSite site, AttSite site2) {
        Reaction reaction = getReaction(site, site2);
        if (reaction != null) {
            return reaction.getAttPos2();
        } else {
            return null;
        }
    }

    private Reaction getReaction(AttSite site, AttSite site2) {
        Reaction ret = null;
        for (Reaction r : reactions) {
            if (r.site.getBaseName().equalsIgnoreCase(site.getBaseName()) 
                    && r.site2.getBaseName().equalsIgnoreCase(site2.getBaseName())
                    && r.site.getVariation().equalsIgnoreCase(site.getVariation())
                    && r.site2.getVariation().equalsIgnoreCase(site2.getVariation())) {
                ret = r;
                break;
            }
        }
        return ret;
    }

    protected static class Reaction {

        protected AttSite site;
        protected AttSite site2;
        protected Integer attPos;
        protected Integer attPos2;

        protected Reaction(AttSite site, AttSite site2, Integer attPos, Integer attPos2) {
            this.site = site;
            this.site2 = site2;
            this.attPos = attPos;
            this.attPos2 = attPos2;
        }

        public Integer getAttPos() {
            return attPos;
        }

        public Integer getAttPos2() {
            return attPos2;
        }
    }
}
