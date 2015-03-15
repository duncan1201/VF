/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;

/**
 *
 * @author dunqiang
 */
public class RENComparators {

    public StringBuilder filter(String resource) throws IOException {
        InputStream inputStream = REN.class.getResourceAsStream(resource);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder ret = new StringBuilder();
        StringBuilder record = null;
        String line = null;
        boolean com = true;
        boolean recordEnds = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("<1>")) {
                record = new StringBuilder();
                com = true;
                recordEnds = false;
            } else if (line.startsWith("<7>")) {
                if (line.equals("<7>")) {
                    com = false;
                }
            } else if (line.isEmpty()) {

                if (com && !recordEnds) {
                    ret.append(record);
                    ret.append("\n");
                    recordEnds = true;
                    record = new StringBuilder();
                }
            }

            if (!recordEnds) {
                record.append(line);
                record.append("\n");
            }

        }

        return ret;
    }

    public static class NameComparator implements Comparator<REN> {

        private boolean ascending = true;

        public NameComparator() {
            this(true);
        }

        public NameComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(REN o1, REN o2) {
            int ret = 0;
            if (o1.getName().compareTo(o2.getName()) > 0) {
                if (ascending) {
                    ret = 1;
                } else {
                    ret = -1;
                }
            } else if (o1.getName().compareTo(o2.getName()) < 0) {
                if (ascending) {
                    ret = -1;
                } else {
                    ret = 1;
                }
            }
            return ret;
        }
    }
}
