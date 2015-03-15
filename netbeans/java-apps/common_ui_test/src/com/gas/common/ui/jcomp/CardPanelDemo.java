package com.gas.common.ui.jcomp;



import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 * Simple Demo of the CardPanel class: shows off most of the API: 
 * CardPanel.show("myCardName") and the CardPanel.showXXXCard
 * methods.
 */
public class CardPanelDemo
{
    private static final int nCards = 15;
    private static final Class[] noParams = {};
    private static final Object[] noArgs = {};

final static CardPanel cardPanel = new CardPanel(); 



    public static void main(String[] args) 
    {
	
	JToolBar toolBar = new JToolBar();
	JMenuBar menuBar = new JMenuBar();
	JMenu showMenu = new JMenu("Select a Card");
	
	/* Add nCards Card instances to the cardPanel and build
	 * the showMenu items while we're at it - one for each card.
	 */

	for(int i = 0; i < nCards; i++) {
	    final String cardName = "Card" + i;
	    Action showCardAction = new AbstractAction(cardName) {
		public void actionPerformed(ActionEvent e) {
		    cardPanel.show(cardName);
		}
	    };
            JPanel card = new JPanel(new BorderLayout());
            card.add(new JLabel(i+""), BorderLayout.CENTER);
	    cardPanel.add(card, cardName);
	    showMenu.add(showCardAction);
	}
	menuBar.add(showMenu);

	/* For each of the no-arg showXXXCard CardPanel methods, build
	 * an Action that calls the method.
	 */

	String[] methodNames = 
            {"showNextCard", "showPreviousCard", "showFirstCard", "showLastCard"};

	for(int i = 0; i < methodNames.length; i++) {
	    final String name = methodNames[i];
	    Action invokeMethodAction = new AbstractAction(" " + name + " ") {
		public void actionPerformed(ActionEvent ignore) {
		    try {
			cardPanel.show("Card9");
		    }
                    
		    catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    };
	    toolBar.add(new JButton(invokeMethodAction));
	    toolBar.addSeparator();
	}

	/* Application boilerplate.
	 */

	JFrame f = new JFrame("CardPanel Demo");

	WindowListener l = new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	};
	f.addWindowListener(l); 

	Container contentPane = f.getContentPane();
	contentPane.add(cardPanel, BorderLayout.CENTER);
	contentPane.add(toolBar, BorderLayout.NORTH);
	f.setJMenuBar(menuBar);
	f.pack();
	f.setVisible(true);
    }
}
