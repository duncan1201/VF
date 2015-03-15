/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui.tmpdb.api;

import java.util.List;

/**
 *
 * @author dq
 */
public interface ITmpDbService {

    <T> T get(String db, String id, Class<T> clazz);

    void put(final String db, List<String> ids, String contents);

    void put(final String db, String id, String contents);
   
}
