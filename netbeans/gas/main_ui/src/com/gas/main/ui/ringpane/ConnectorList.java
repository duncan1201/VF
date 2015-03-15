/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.as.Feture;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class ConnectorList extends ArrayList<Connector> {
    
    public void paint(Graphics2D g){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(int i = 0; i < size(); i++){
            Connector connector = get(i);
            connector.paint2(g);
        }
    }
    
    public void setSeedColor(String featureKey, Color color){
        for(Connector c: this){
            Label label = (Label)c.getData();
            if(label != null){
                Ring ring = (Ring)label.getData();
                if(ring != null){
                    Feture feture = (Feture)ring.getData();
                    if(feture.getKey().equalsIgnoreCase(featureKey)){
                        c.setForeground(color);
                    }
                }
            }
        }
    }
    
    public Connector updateSelection(Label label){
        Connector ret = null;
        Iterator<Connector> cItr = iterator();
        while (cItr.hasNext()) {
            Connector conn = cItr.next();
            Object data = conn.getData();
            if(data == label){
                ret = conn;                
            }           
            conn.setSelected(data == label);
        }
        return ret;
    }
    
    public void setSelected(Connector selected){
        Iterator<Connector> itr = iterator();
        while(itr.hasNext()){
            Connector con = itr.next();
            con.setSelected(con == selected);
        }
    }
    
    public Connector getConnector(Label label){
        Connector ret = null;
        Iterator<Connector> itr = iterator();
        while(itr.hasNext()){
            Connector conn = itr.next();
            if(conn.getData().equals(label)){
                ret = conn;
                break;
            }
        }
        return ret;
    }
}
