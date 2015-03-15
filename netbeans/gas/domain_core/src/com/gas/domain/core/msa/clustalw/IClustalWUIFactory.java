/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa.clustalw;

/**
 *
 * @author dq
 */
public interface IClustalWUIFactory {

    IClustalWUI create(boolean vertical, String profile1, String profile2);
}
