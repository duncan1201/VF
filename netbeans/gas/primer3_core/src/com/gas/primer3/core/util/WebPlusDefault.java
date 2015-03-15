package com.gas.primer3.core.util;

import com.gas.primer3.core.api.BoulderIOUtil;
import com.gas.common.ui.util.FileHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.TreeMap;

public class WebPlusDefault {

    public static void main(String[] args) throws IOException {
        StringBuffer buffer = FileHelper.toStringBuffer(WebPlusDefault.class, "plus_default.txt");
        String contents = buffer.toString();
        BufferedReader reader = new BufferedReader(new StringReader(contents));
        String line;

        final String regex = "\\s=>\\s|,";
        Map<String, String> map = new TreeMap<String, String>();
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.startsWith("#") && line.length() > 0) {
                String[] splits = line.split(regex);
                /*
                for(String split: splits){
                System.out.print(split);
                System.out.print('\t');
                }
                 */
                splits[0] = splits[0].trim().replaceAll("\"", "");
                splits[1] = splits[1].trim().replaceAll("\"", "");
                map.put(splits[0], splits[1]);
                //System.out.print(splits[0].trim());
                //System.out.print('<');
                //System.out.print(splits[1]);
                //System.out.print('\t');
                //System.out.println(splits.length);
            }
        }
        File file = BoulderIOUtil.toFile(map);
        System.out.println(file.getAbsolutePath());
    }
}
