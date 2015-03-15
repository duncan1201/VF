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
class ColorProviderCnsts {

    private static final Color midBlue = new Color(100, 100, 255);
    protected static final String[] aa = {"A", "R", "N", "D", "C",
        "Q", "E", "G", "H", "I",
        "L", "K", "M", "F", "P",
        "S", "T", "W", "Y", "V",
        "B", "Z", "X", "_", "*", ".", " "};
    // This is hydropathy index
    // Kyte, J., and Doolittle, R.F., J. Mol. Biol.
    // 1157, 105-132, 1982
    protected static final float[] hyd = {1.8f, -4.5f, -3.5f, -3.5f, 2.5f,
        -3.5f, -3.5f, -0.4f, -3.2f, 4.5f,
        3.8f, -3.9f, 1.9f, 2.8f, -1.6f,
        -0.8f, -0.7f, -0.9f, -1.3f, 4.2f,
        -3.5f, -3.5f, -0.49f, 0.0f};
    protected static final float hydmax = 4.5f;
    protected static final float hydmin = -3.9f;
    protected static final Color[] taylor = {new Color(204, 255, 0), // A Greenish-yellowy-yellow
        new Color(0, 0, 255), // R Blueish-bluey-blue
        new Color(204, 0, 255), // N Blueish-reddy-blue
        new Color(255, 0, 0), // D Reddish-reddy-red
        new Color(255, 255, 0), // C Yellowish-yellowy-yellow
        new Color(255, 0, 204), // Q Reddish-bluey-red
        new Color(255, 0, 102), // E Blueish-reddy-red
        new Color(255, 153, 0), // G Yellowy-reddy-yellow
        new Color(0, 102, 255), // H Greenish-bluey-blue
        new Color(102, 255, 0), // I Greenish-yellowy-green
        new Color(51, 255, 0), // L Yellowish-greeny-green
        new Color(102, 0, 255), // K Reddish-bluey-blue
        new Color(0, 255, 0), // M Greenish-greeny-green
        new Color(0, 255, 102), // F Blueish-greeny-green
        new Color(255, 204, 0), // P Reddish-yellowy-yellow
        new Color(255, 51, 0), // S Yellowish-reddy-red
        new Color(255, 102, 0), // T Reddish-yellowy-red
        new Color(0, 204, 255), // W Blueish-greeny-green
        new Color(0, 255, 204), // Y Greenish-bluey-green
        new Color(153, 255, 0), // V Yellowish-greeny-yellow
        Color.BLACK, // B
        Color.BLACK, // Z
        Color.BLACK, // X
        Color.BLACK, // -
        Color.BLACK, // *
        Color.BLACK // .
    };
    // Zappo
    protected static final Color[] zappo = {Color.pink, // A
        midBlue, // R
        Color.green, // N
        Color.red, // D
        Color.yellow, // C
        Color.green, // Q
        Color.red, // E
        Color.magenta, // G
        midBlue,// Color.red, // H
        Color.pink, // I
        Color.pink, // L
        midBlue, // K
        Color.pink, // M
        Color.orange, // F
        Color.magenta, // P
        Color.green, // S
        Color.green, // T
        Color.orange, // W
        Color.orange, // Y
        Color.pink, // V
        Color.white, // B
        Color.white, // Z
        Color.white, // X
        Color.white, // -
        Color.BLACK, // *
        Color.white, // .
        Color.white // ' '
    };
}
