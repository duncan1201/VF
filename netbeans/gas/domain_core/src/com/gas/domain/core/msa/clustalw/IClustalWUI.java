/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.clustalw;

/**
 *
 * @author dq
 */
public interface IClustalWUI {

    /**
     *
     */
    ClustalwParam getClustalwParam();

    void setMsaParam(ClustalwParam clustalwParams);

    String getProfile1();

    String getProfile2();
}
