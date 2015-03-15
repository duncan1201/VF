package com.gas.primer3.core.api;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.primer3.UserInput;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class BoulderIOUtil {

    public static Map<String, String> parse(Class clazz, String name){
        return parse(clazz.getResourceAsStream(name));
    }
    
    public static Map<String, String> parse(InputStream inputStream) {
        return parse(inputStream, false);
    }

    public static Map<String, String> parse(InputStream inputStream, boolean sorted) {
        Map<String, String> ret;
        if (sorted) {
            ret = new TreeMap<String, String>();
        } else {
            ret = new LinkedHashMap<String, String>();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.indexOf('=') > -1 && !line.equals("=")) {
                    ret.put(line.substring(0, line.indexOf('=')), line.substring(1 + line.indexOf('=')));
                }
            }
        } catch (IOException io) {
        }
        return ret;
    }
    
    public static String toString(Map<String, String> data){
        StringBuilder builder = new StringBuilder();
        SortedSet<String> keys = new TreeSet(new UserInput.KeyComparator());
        keys.addAll(data.keySet());
        Iterator<String> names = keys.iterator();
        while (names.hasNext()) {
            String name = names.next();
            String value = data.get(name);
            builder.append(name);
            builder.append('=');
            builder.append(value);
            builder.append('\n');
            if(name.equals("P3_FILE_TYPE")){
                builder.append("\n");
            }
        }
        builder.append('=');
        return builder.toString();
    }
    
    public static void toFile(Map<String, String> data, File file){
        String str = toString(data);
        FileHelper.toFile(file, str);
    }

    public static File toFile(Map<String, String> data) {
        File file = FileHelper.getUniqueFile("boulder", ".txt");

        String str = toString(data);
        
        FileHelper.toFile(file, str);

        return file;
    }

    /**
     * 
     */
    public static Map<String, String> parse(File file) {
        Map<String, String> ret = new LinkedHashMap<String, String>();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ret = parse(fileInputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }        
}
