/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class CountriesTest {
    @Test
    public void testGetFont() throws IOException {
        InputStream inputStream = CountriesTest.class.getResourceAsStream("countries.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> lines = new ArrayList<String>();
        while((line = r.readLine()) != null){
            line = line.trim();
            lines.add(line);
        }
        //var states = [['SG', 'Singapore']];
        StringBuilder b = new StringBuilder();
        b.append('[');
        Iterator<String> itr = lines.iterator();
        while(itr.hasNext()){
            String l = itr.next();
            String name = l.substring(0, l.length() - 2).trim();
            String code = l.substring(l.length() - 2, l.length());
            b.append(String.format("['%s', '%s']", code, name));
            if(itr.hasNext()){
                b.append(", ");
            }
        }
        b.append(']');
        System.out.println(b.toString());
    }
}
