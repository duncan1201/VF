package com.gas.entrez.core.ESummary.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ESummaryResult {

    private List<DocSum> docSums = new ArrayList<DocSum>();

    public List<DocSum> getDocSums() {
        return docSums;
    }

    public void setDocSums(List<DocSum> docSums) {
        this.docSums = docSums;
    }
    
    public List<String> getNamedItem(String name){
        List<String> ret = new ArrayList<String>();
        Iterator<DocSum> itr = docSums.iterator();
        while(itr.hasNext()){
            DocSum docSum = itr.next();
            Iterator<Item> itemItr = docSum.getItems().iterator();
            while(itemItr.hasNext()){
                Item item = itemItr.next();
                if(item.getName().equals(name)){
                    ret.add(item.getText());
                }
            }
        }
        return ret;
    }

    public static class DocSum {

        private String id;
        private List<Item> items = new ArrayList<Item>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    public static class Item {

        private String name;
        private String type;
        private String text;
        private List<Item> items = new ArrayList<Item>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }
}
