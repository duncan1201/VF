/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced.api;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dq
 */
public interface ISubsetImportService {

    Map<String, List<String>> importOligoSubsets(File dbDir);
    
    Map<String, List<String>> importProteinSubsets(File dbDir);

    Map<String, List<String>> importNucleotideSubsets(File dbDir);
}
