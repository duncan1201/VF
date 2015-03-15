/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.util.MathUtil;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class ConnectorsComp extends JComponent {
    
    private ConnectorList connectors;
    
    @Override
    public void paintComponent(Graphics g){
        Dimension size = getSize();
        if(size.width < 0 || size.height < 0){
            return ;
        }
        connectors.paint((Graphics2D)g);
    }
    
    public void setSeedColor(String featureKey, Color c){
        connectors.setSeedColor(featureKey, c);
        repaint();
    }

    public ConnectorList getConnectors() {
        return connectors;
    }    
    
    public Connector updateSelection(Label label){
        Connector ret = connectors.updateSelection(label);
        repaint();
        return ret;
    }

    public void setConnectors(ConnectorList connectors) {
        this.connectors = connectors;
    }
    
    public Connector getConnector(Label label){
        return connectors.getConnector(label);
    }
    
    public void setSelected(Connector selected){
        connectors.setSelected(selected);
    }
    
    public void layoutConnectors(RingGraphPanel ringGraphPanel){
        final Rectangle bounds = getBounds();
        final Point origin = new Point(Math.round(bounds.width * 0.5f), Math.round(bounds.height * 0.5f));       
        Iterator<Connector> itr = getConnectors().iterator();
        while (itr.hasNext()) {
            Connector connector = itr.next();
            Label label = (Label) connector.getData();
            Point lCenter = label.getCenter();
            Ring ring = (Ring) label.getData();           
            Double degree = MathUtil.getAngleInDegrees(lCenter, origin);  
            Point pt = null;
            Point ringPt = ring.getStartPt();
            ringPt = SwingUtilities.convertPoint(ringGraphPanel.getRingListMapComp(), ringPt, ringGraphPanel);
            if (degree >= 0 && degree <= 85) {
                pt = label.getLeftMiddle();
                pt.x -= 1;                        
            } else if (degree >= 85 && degree <= 95) {
                pt = label.getBottomMiddle();                                                   
            } else if (degree >= 95 && degree <= 250) {
                pt = label.getRightMiddle();                                                   
            } else if (degree >= 250 && degree <= 290) {
                pt = label.getTopMiddle();                               
            } else if (degree >= 290 && degree <= 360) {
                pt = label.getLeftMiddle();
                pt.x -= 1;
            }
            connector.setLine(new Line2D.Double(pt, ringPt));
        }        
    }
    
}
