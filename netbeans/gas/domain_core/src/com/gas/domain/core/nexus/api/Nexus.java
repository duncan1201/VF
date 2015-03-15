/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus.api;

import com.gas.domain.core.nexus.BlkList;
import java.util.*;

/**
 *
 * @author dq
 */
public class Nexus {
    
    BlkList blks = new BlkList();

    public Nexus() {
    }
    
    public BlkList getBlks() {
        return blks;
    }
    
    public boolean containsBlk(String blkName){
        Iterator<Blk> itr = blks.iterator();
        while(itr.hasNext()){
            Blk blk = itr.next();
            if(blk.getName().equalsIgnoreCase(blkName)){
                return true;
            }
        }
        return false;
    }        
    
    public Blk getBlk(String blkName){
        Blk ret = null;
        Iterator<Blk> itr = blks.iterator();
        while(itr.hasNext()){
            Blk blk = itr.next();
            if(blk.getName().equalsIgnoreCase(blkName)){
                ret = blk;
                break;
            }
        }
        return ret;
    }

    public void setBlks(BlkList blks) {
        this.blks = blks;
    }
    
    

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("#NEXUS");
        ret.append("\n\n");        
        ret.append(blks.toString());
        return ret.toString();
    }

    public static class Block {

        private String name;
        private Set<Command> commands;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append("\nbegin");
            ret.append(' ');
            ret.append(name);
            ret.append(";\n");
            List<Command> cmds = new ArrayList<Command>(commands);
            Collections.sort(cmds, new CommandSorter());
            Iterator<Command> itr = cmds.iterator();
            while (itr.hasNext()) {
                Command cmd = itr.next();
                ret.append(cmd.toString());
            }
            ret.append(";\n");
            return ret.toString();
        }
    }

    public static class Command {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class GenericCmd extends Command {

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    private static class CommandSorter implements Comparator<Command> {

        private final String[] names = {"DIMENSIONS", "FORMAT", "MATRIX"};
        private List<String> nameList;

        public CommandSorter() {
            nameList = Arrays.asList(names);
        }

        @Override
        public int compare(Command o1, Command o2) {
            String name1 = o1.getName().toUpperCase(Locale.ENGLISH);
            Integer index1 = nameList.indexOf(name1);
            String name2 = o2.getName().toUpperCase(Locale.ENGLISH);
            Integer index2 = nameList.indexOf(name2);
            return index1.compareTo(index2);
        }
    }
}
