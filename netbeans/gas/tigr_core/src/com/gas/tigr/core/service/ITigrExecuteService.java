/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.service;

import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TIGRSettings;
import com.gas.domain.core.tigr.TigrProject;
import java.util.Collection;

/**
 *
 * @author dq
 */
public interface ITigrExecuteService {
    TigrProject assembly(Collection<Kromatogram> kromatograms, byte[] qual, TIGRSettings settings) ;
    <T> T assembly(byte[] seq, byte[] qual, TIGRSettings settings, Class<T> retType) ;
}
