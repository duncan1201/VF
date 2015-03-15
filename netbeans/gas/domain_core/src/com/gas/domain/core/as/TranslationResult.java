/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.core.IntSet;
import com.gas.common.ui.util.CommonUtil;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;

/**
 *
 * @author dunqiang
 */
public class TranslationResult implements Cloneable {

    public static final String HEADER_PREFIX = "Translation_Result";
    static final Character DELIMITER = '@';
    private Integer hibernateId;
    private String id;
    private Integer startPos; // start position in DNA's coordinate
    private Integer frame; // possible values are {1,2,3,-1,-2,-3}
    private String tableName;
    private String data;
    private transient String displayName;

    public TranslationResult() {
    }

    public TranslationResult(String content) {
        String[] splits = content.split(DELIMITER.toString());
        int i = 0;
        startPos = CommonUtil.parseInt(splits[i++]);
        frame = CommonUtil.parseInt(splits[i++]);
        tableName = splits[i++];
        data = splits[i++];
    }        
    
    public static IntSet getFrames(Collection<TranslationResult> trs){
        IntSet ret = new IntSet();
        for(TranslationResult tr: trs){
            ret.add(tr.getFrame());
        }
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    @Override
    public Object clone() {
        TranslationResult ret = CommonUtil.cloneSimple(this);
        return ret;
    }

    /*
     * id=startPos@endPos@frame@tableName@data
     */
    public static TranslationResult parse(String d) {
        TranslationResult ret = new TranslationResult();
        String[] splits = d.split("=");
        ret.id = splits[0];

        String v = splits[1];

        splits = v.split(DELIMITER.toString());
        int i = 0;
        ret.startPos = CommonUtil.parseInt(splits[i++]);
        ret.frame = CommonUtil.parseInt(splits[i++]);
        ret.tableName = splits[i++];
        ret.data = splits[i++];

        return ret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getEndPos() {
        int ret = startPos + data.length() * 3 - 1;
        return ret;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getFrame() {
        return frame;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public static String generateHeader() {
        StringBuilder ret = new StringBuilder();
        ret.append(HEADER_PREFIX);
        ret.append(' ');
        ret.append(UUID.randomUUID().toString());
        return ret.toString();
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = String.format("Frame %d", frame);
        }
        return displayName;
    }

    /*
     * id=startPos@endPos@frame@tableName@data
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (id == null || id.isEmpty()) {
            id = generateHeader();
        }
        ret.append(id);
        ret.append("=");
        ret.append(startPos);
        ret.append(DELIMITER);
        ret.append(frame);
        ret.append(DELIMITER);
        ret.append(tableName);
        ret.append(DELIMITER);
        ret.append(data);
        return ret.toString();
    }

    public static class FrameComparator implements Comparator<TranslationResult> {

        private boolean ascending;

        public FrameComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(TranslationResult o1, TranslationResult o2) {
            int ret = 0;
            if (o1.getFrame() > o2.getFrame()) {
                if (ascending) {
                    ret = 1;
                } else {
                    ret = -1;
                }
            } else if (o1.getFrame() < o2.getFrame()) {
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