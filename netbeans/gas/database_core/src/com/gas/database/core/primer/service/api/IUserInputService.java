/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.primer.service.api;

import com.gas.domain.core.primer3.UserInput;
import java.io.File;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IUserInputService {

    void create(UserInput ui);

    boolean contains(String name);
        
    void delete(UserInput userInput);
        
    List<UserInput> getAll(boolean full);
    
    UserInput mergeData(UserInput to, UserInput from);
    
    UserInput merge(UserInput userInput);
    
    void merge(List<UserInput> userInput);
    
    UserInput getFullByName(String name);
    
    UserInput getFullByHibernateId(Integer hibernateId);
    
    void export(UserInput userInput, File file);
    
    UserInput importFromFile(File file);
    
    UserInput getFavorite();
    
    void setFavorite(UserInput userInput);
    
}
