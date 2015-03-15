/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.filesystem;

import java.util.Comparator;

/**
 *
 * @author dq
 */
public class FolderComparators {
        
    /*
     * The expected order is:
     * 1. Sample Data
     * 2. User defined Data
     * 3. Recycle bin
     */
    public static class MyDataFolderTreeComparator implements Comparator<Folder> {

        public MyDataFolderTreeComparator() {
        }

        @Override
        public int compare(Folder o1, Folder o2) {
            int ret = 0;
            String name1 = o1.getName();
            String name2 = o2.getName();


            if (name1.equals(FolderNames.RECYCLE_BIN)) {
                ret = 1;
            } else if (name2.equals(FolderNames.RECYCLE_BIN)) {
                ret = -1;
            } else {
                ret = name1.compareToIgnoreCase(name2);
            }
            return ret;
        }
    }
    
    public static class MyDataFolderTreeNameComparator implements Comparator<String> {

        public MyDataFolderTreeNameComparator() {
        }

        @Override
        public int compare(String o1, String o2) {
            int ret = 0;

            if (o1.equals(FolderNames.RECYCLE_BIN)) {
                ret = 1;
            } else if (o2.equals(FolderNames.RECYCLE_BIN)) {
                ret = -1;
            } else {
                ret = o1.compareToIgnoreCase(o2);
            }
            return ret;
        }
    }    
}
