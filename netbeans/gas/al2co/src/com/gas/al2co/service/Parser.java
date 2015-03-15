/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.al2co.service;

import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.util.StrUtil;
import java.io.*;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class Parser {
    
    public static FloatList parse(File file){
        FloatList ret = new FloatList();
        try {
            FileInputStream s = new FileInputStream(file);
            ret = parse(s);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
    
    public static FloatList parse(InputStream stream){
        FloatList ret = new FloatList() ;
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        try {
            while((line = r.readLine()) != null){
                line = line.trim();
                List<String> tokens = StrUtil.tokenize(line, " ");
                if(tokens.isEmpty()){
                    continue;
                }
                boolean isInt = StrUtil.isInteger(tokens.get(0));
                if(!isInt){
                    break;
                }
                Float c = Float.parseFloat(tokens.get(2));
                ret.add(c);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
}
