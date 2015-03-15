/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.primer3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dq
 */
public class UserInputList extends ArrayList<UserInput> {

    public UserInputList(Collection<UserInput> userInputs){
        addAll(userInputs);
    }
    
    public List<String> getNames(){
        List<String> ret = new ArrayList<String>();
        for(UserInput userInput: this){
            ret.add(userInput.getName());
        }
        return ret;
    }
}
