/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.primer3;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class OverlapPrimer implements Cloneable {

    Integer hibernateId;
    private String name;
    private String seq;//5'->3'
    private int overlapLength;
    private Set<OligoElement> oligoElements = new HashSet<OligoElement>();
    private Set<FlappyEnd> flappyEnds = new HashSet<FlappyEnd>();

    public OverlapPrimer() {
    }

    @Override
    public OverlapPrimer clone() {
        OverlapPrimer ret = CommonUtil.cloneSimple(this);
        ret.setOligoElements(CommonUtil.copyOf(oligoElements));
        ret.setFlappyEnds(CommonUtil.copyOf(flappyEnds));
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getSeq() {
        return seq;
    }

    /**
     * 5'->3' direction
     */
    public String getAnnealingSeq() {
        return getOligoElement().getSeq();
    }

    /**
     * 5'---->3'
     */
    public String getFlappyEndSeq() {
        String ret = null;
        int lengthAnnealing = getAnnealingSeq().length();
        if (isForward()) {
            ret = StrUtil.sub(seq, 1, seq.length() - lengthAnnealing);
        } else {
            ret = StrUtil.sub(seq, 1, seq.length() - lengthAnnealing);
        }
        return ret;
    }

    /**
     * 5'->3'
     */
    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int calculateStart(int totalLength) {
        int ret = 0;
        OligoElement oe = getOligoElement();
        int startAnneal = oe.calculateStart();
        if (oe.isForwardPrimer()) {
            int lengthFlappy = getFlappyEnd().getLength();
            ret = startAnneal - lengthFlappy;
            if (ret < 1) {
                ret = ret + totalLength;
            }
        } else {
            ret = startAnneal;
        }
        return ret;
    }

    public int calculateEnd(int totalLength) {
        int ret = 0;
        OligoElement oe = getOligoElement();
        int endAnneal = oe.calculateEnd();
        if (oe.isForwardPrimer()) {
            ret = endAnneal;
        } else {
            int length = getFlappyEnd().getLength();
            ret = endAnneal + length;
            if (ret > totalLength) {
                ret = ret % totalLength;
            }
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public boolean isForward() {
        return getOligoElement().isForwardPrimer();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlappyEnd(FlappyEnd flappyEnd) {
        flappyEnds.clear();
        flappyEnds.add(flappyEnd);
    }

    public FlappyEnd getFlappyEnd() {
        if (flappyEnds.isEmpty()) {
            return null;
        } else {
            FlappyEnd ret = flappyEnds.iterator().next();
            return ret;
        }
    }

    protected Set<FlappyEnd> getFlappyEnds() {
        return flappyEnds;
    }

    protected void setFlappyEnds(Set<FlappyEnd> flappyEnds) {
        this.flappyEnds = flappyEnds;
    }

    public int getOverlapLength() {
        return overlapLength;
    }

    public void setOverlapLength(int overlapLength) {
        this.overlapLength = overlapLength;
    }

    public OligoElement getOligoElement() {
        if (oligoElements.isEmpty()) {
            return null;
        } else {
            OligoElement ret = oligoElements.iterator().next();
            return ret;
        }
    }

    public String getProblems() {
        String problems = getOligoElement().getProblems();
        return problems;
    }
    
    public List<String> getProblemList(){
        String problems = getOligoElement().getProblems();
        List<String> ret = StrUtil.tokenize(problems, ";");
        return ret;
    }

    public String getProblemsHtml() {
        String problems = getOligoElement().getProblems();
        List<String> problemList = StrUtil.tokenize(problems, ";");
        StringBuilder ret = new StringBuilder();
        ret.append("<html bgcolor=#ffffe1>");
        if (!problemList.isEmpty()) {
            ret.append("Warnings:<i>(Please note: blah blah blah blah)</i>");
            ret.append("<ul>");
            for (String problem : problemList) {
                ret.append("<li>");
                ret.append("<font size=-1>");
                ret.append(problem);
                ret.append("</font>");
                ret.append("</li>");
            }
            ret.append("</ul>");
        }
        ret.append("</html>");
        return ret.toString();
    }

    public int getProblemCount() {
        OligoElement oe = getOligoElement();
        return oe.getProblemCount();
    }

    public void setOligoElement(OligoElement oligoElement) {
        oligoElements.clear();
        oligoElements.add(oligoElement);
    }

    protected Set<OligoElement> getOligoElements() {
        return oligoElements;
    }

    protected void setOligoElements(Set<OligoElement> oligoElements) {
        this.oligoElements = oligoElements;
    }

    public int getLength() {
        return getFlappyEnd().length + getOligoElement().getLength();
    }

    public static class FlappyEnd implements Cloneable {

        Integer hibernateId;
        private int length;
        private String seq; // 5'->3'

        public FlappyEnd() {
        }

        public FlappyEnd(int length) {
            this.length = length;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        @Override
        public FlappyEnd clone() {
            FlappyEnd ret = CommonUtil.cloneSimple(this);
            return ret;
        }

        public String getSeq() {
            return seq;
        }

        /**
         * 5'->3'
         */
        public void setSeq(String seq) {
            this.seq = seq;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
}
