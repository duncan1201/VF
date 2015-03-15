/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.matrix.api;

/**
 *
 * @author dq
 */
public interface IMatrixService {

    public static final String IUB = "IUB";
    public static final String CLUSTALW = "Clustalw";
    public static final String IDENTITY = "Identity";
    public static final String GONNET_PAM_80 = "Gonnet PAM 80";
    public static final String GONNET_PAM_120 = "Gonnet PAM 120";
    public static final String GONNET_PAM_250 = "Gonnet PAM 250";
    public static final String GONNET_PAM_350 = "Gonnet PAM 350";

    MatrixList getAllMatrices();

    Matrix getDefaultProteinMatrix();

    Matrix getDefaultDnaMatrix();
}
