/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import java.awt.Color;

/**
 *
 * @author dq
 */
public interface IColorProvider {

    Color getColor(String s);

    Color getColor(Character c);

    String getName();// unique ID    

    boolean isForProtein();

    boolean isForNucleotide();

    boolean isBackgroundColor();
}
