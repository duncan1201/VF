/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.common;

/**
 *
 * @author dq
 */
public interface ITree {

    public enum SHAPE {
        
        NONE, CIRCLE, SQUARE;
        
        public static SHAPE get(String str){
            SHAPE ret = null;
            if(str.equalsIgnoreCase(NONE.toString())){
                ret = NONE;
            }else if(str.equalsIgnoreCase(CIRCLE.toString())){
                ret = CIRCLE;
            }else if(str.equalsIgnoreCase(SQUARE.toString())){
                ret = SQUARE;
            }else{
                throw new IllegalArgumentException(String.format("Unknown argument:%s", str));
            }
            return ret;
        }
    };
    
    public enum TRANSFORM {
        NONE, EQUAL, CLADOGRAM;
        
        public static TRANSFORM get(String str){
            TRANSFORM ret = null;
            if(str.equalsIgnoreCase(NONE.toString())){
                ret = NONE;
            }else if(str.equalsIgnoreCase(EQUAL.toString())){
                ret = EQUAL;
            }else if(str.equalsIgnoreCase(CLADOGRAM.toString())){
                ret = CLADOGRAM;
            }
            return ret;
        }
    }
    void setTransform(TRANSFORM transform);
    
    void setLineWidth(int lineWidth);
    void setEdgeLabelVisible(boolean visible);
    String[] getLengthAttributeNames();
    void setSelectedLengthAttribute(String selectedNameAttribute);
    void setSigDigits(Integer sigDigits);
    
    void setNodeShape(SHAPE nodeShape);        
    void setNodeLabelVisible(boolean nodeLabelVisible);        
    String[] getNameAttributeNames();
    void setSelectedNameAttribute(String selectedNameAttribute);
    void setFontSize(Float fontSize);
}
