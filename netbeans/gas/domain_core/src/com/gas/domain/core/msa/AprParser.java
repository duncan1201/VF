/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class AprParser {
    
    private static final String IxAlignment = "obj|IxAlignment";
    
    public static Apr parse(File file){
        Apr apr = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            apr = parse(inputStream);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return apr;
    }
    
    public static Apr parse(InputStream inputStream){
        Apr ret = new Apr();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = r.readLine();
            while(line != null){
                line = line.trim();
                if(line.startsWith(IxAlignment)){
                    int indexColon = line.indexOf(":");
                    int indexI = line.lastIndexOf("|");
                    if(indexColon < -1 || indexI < -1){
                        System.out.println();
                    }
                    String name = line.substring(indexI + 1);
                    String data = line.substring(indexColon + 1, indexI);
                    ret.getData().put(name, data);
                }
                line = r.readLine();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
}
