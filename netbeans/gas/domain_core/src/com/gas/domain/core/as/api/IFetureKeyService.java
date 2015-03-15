/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.api;

import com.gas.domain.core.as.FetureKey;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IFetureKeyService {

    void create(FetureKey fk);

    List<FetureKey> parse(InputStream inputStream);
    
    //List<FetureKey> parse(File inputStream);
    
    //List<FetureKey> getHardcodedFetureKeys();
    List<FetureKey> getAllInitialFetureKeys(File file);
    
    List<FetureKey> getAll();
    
    List<String> getAllNames();   
    
    List<String> getAllNamesWithQualifiers();
    
    List<String> getAllQualifiers() ;

    FetureKey getFullByName(String name);
    
    boolean isPresent(String name);
}

