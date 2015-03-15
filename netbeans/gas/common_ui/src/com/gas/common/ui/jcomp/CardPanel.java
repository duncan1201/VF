/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import com.gas.common.ui.IReclaimable;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.JPanel;

/**
 *
 * @author dunqiang
 */
public class CardPanel extends JPanel {

    private WeakHashMap<String, JPanel> cards = new WeakHashMap<String, JPanel>();
    private String currentCardName = null;

    public CardPanel() {
        setLayout(new CardLayout());
    }

    public void add(JPanel comp, String name) {
        super.add(comp, name);
        cards.put(name, comp);
        if (currentCardName == null) {
            currentCardName = name;
        }
    }

    public String getCurrentCardName() {
        return currentCardName;
    }

    public boolean containsCard(String name) {
        return getCardNames().contains(name);
    }

    public Set<String> getCardNames() {
        return cards.keySet();
    }

    public void show(String name) {
        boolean valid = getCardNames().contains(name);
        if (!valid) {
            throw new IllegalArgumentException("Card " + name + " not found!");
        }
        CardLayout layout = (CardLayout) getLayout();
        layout.show(this, name);
        currentCardName = name;
    }

    public JPanel getCard(String name) {
        return cards.get(name);
    }

    public JPanel getCurrentCard() {
        String name = getCurrentCardName();
        return getCard(name);
    }
}
