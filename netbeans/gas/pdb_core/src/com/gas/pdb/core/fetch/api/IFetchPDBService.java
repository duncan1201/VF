/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.fetch.api;

/**
 *
 * @author dq
 */
public interface IFetchPDBService {
    <T> T sendRequest(String pdbId, Class<T> retType);
}
