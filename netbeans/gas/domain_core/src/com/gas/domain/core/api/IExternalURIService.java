/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.api;

import java.net.URI;

/**
 *
 * @author dq
 */
public interface IExternalURIService {

    boolean isBrowsingSupported(String db);

    URI getURI(String db, String id);
}
