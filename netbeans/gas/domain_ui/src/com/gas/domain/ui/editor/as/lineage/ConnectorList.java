/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.lineage;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class ConnectorList extends ArrayList<Connector> {

    public void paint(Graphics2D g) {
        Iterator<Connector> itr = iterator();
        while (itr.hasNext()) {
            Connector connector = itr.next();
            connector.paint(g);
        }
    }
}
