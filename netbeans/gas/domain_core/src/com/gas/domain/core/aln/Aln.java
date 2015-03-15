/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.aln;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dq
 */
public class Aln {

    private String header;
    private Map<String, String> entries = new HashMap<String, String>();
    private final String BLANKS = "     ";
    private String conservation;

    public Aln() {
    }

    public Aln(Map<String, String> entries) {
        this.entries = entries;
    }

    public StringList getEntryNames() {
        StringList ret = new StringList();
        ret.addAll(entries.keySet());
        return ret;
    }

    @Override
    public String toString() {

        StringBuilder ret = new StringBuilder();
        ret.append(header);
        ret.append("\n\n");
        StringList entryNames = getEntryNames();
        String longest = entryNames.longest();
        Collections.sort(entryNames);
        if (entryNames.isEmpty()) {
            return ret.toString();
        }
        String sampleData = entries.get(entryNames.get(0));
        int PER_ROW = 60;
        final int rows = (int) Math.ceil(sampleData.length() * 1.0 / PER_ROW);
        for (int row = 0; row < rows; row++) {
            for (int i = 0; i < entryNames.size(); i++) {
                String name = entryNames.get(i);
                name = StrUtil.append(name, ' ', longest.length() - name.length());
                ret.append(name);
                ret.append(BLANKS);
                String data = entries.get(entryNames.get(i));
                String subData;
                if (PER_ROW * (row + 1) <= sampleData.length()) {
                    subData = data.substring(PER_ROW * row, PER_ROW * (row + 1));
                } else {
                    subData = data.substring(PER_ROW * row);
                }
                ret.append(subData);
                ret.append('\n');
            }
            if (conservation != null) {
                String subCons;
                if (PER_ROW * (row + 1) <= conservation.length()) {
                    subCons = conservation.substring(PER_ROW * row, PER_ROW * (row + 1));
                } else {
                    subCons = conservation.substring(PER_ROW * row);
                }
                subCons = StrUtil.insertFront(subCons, ' ', BLANKS.length() + longest.length());
                ret.append(subCons);
            }
            ret.append('\n');
            ret.append('\n');
        }

        return ret.toString();
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getConservation() {
        return conservation;
    }

    public void setConservation(String conservation) {
        this.conservation = conservation;
    }

    public void add(String name, String data) {
        if (!entries.containsKey(name)) {
            entries.put(name, "");
        }
        String oldData = entries.get(name);
        String newData = oldData + data;
        entries.put(name, newData);
    }

    public Map<String, String> getEntries() {
        Map<String, String> ret = new LinkedHashMap<String, String>();
        List<String> keys = new ArrayList<String>(entries.keySet());
        Collections.sort(keys);
        Iterator<String> itr = keys.iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String value = entries.get(key);
            ret.put(key, value);
        }
        return ret;
    }

    public int getEntryCount() {
        return entries.size();
    }

    public int getLength() {
        if (entries.isEmpty()) {
            return 0;
        } else {
            return entries.values().iterator().next().length();
        }
    }

    public void appendConservation(String c) {
        if (this.conservation == null) {
            this.conservation = "";
        }
        this.conservation += c;
    }

    public int getConservationLength() {
        if (conservation == null) {
            return 0;
        } else {
            return conservation.length();
        }
    }
}
