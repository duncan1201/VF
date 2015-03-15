/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.tigr.service.api;

import com.gas.domain.core.tigr.Kromatogram;

/**
 *
 * @author dq
 */
public interface IKromatogramService {
    void persist(Kromatogram kromatogram);
    Kromatogram merge(Kromatogram kromatogram);
    Kromatogram getFullByHibernateId(String hibernateId);
}
