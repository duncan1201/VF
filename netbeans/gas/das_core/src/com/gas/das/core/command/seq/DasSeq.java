package com.gas.das.core.command.seq;

import java.util.ArrayList;
import java.util.List;

public class DasSeq {

    private List<Seq> seqs = new ArrayList<Seq>();

    public List<Seq> getSeqs() {
        return seqs;
    }

    public static class Seq {

        private String id;
        private Integer start;
        private Integer stop;
        private String data;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getStop() {
            return stop;
        }

        public void setStop(Integer stop) {
            this.stop = stop;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
