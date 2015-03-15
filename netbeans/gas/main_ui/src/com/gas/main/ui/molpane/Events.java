/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import java.util.EventObject;

/**
 *
 * @author dunqiang
 */
public class Events {

    
    public static class GraphPanelPosChangedEvent extends EventObject{
        
        public Integer pos; /* The position is the insertion point */
        
        public GraphPanelPosChangedEvent(Object source, Integer pos){
            super(source);
            this.pos = pos;
        }
    }
    
    public static class BrickDeletedEvent extends EventObject {

        public BrickDeletedEvent(Object source) {
            super(source);
        }
    }

    public static class BrickInsertedEvent extends EventObject {

        public BrickInsertedEvent(Object source) {
            super(source);
        }
    }
    
    public static class CaretDownEvent extends EventObject{
        
        private Integer insertPoint ;
        
        public CaretDownEvent(Object source){
            super(source);
        }
        
        public CaretDownEvent(Object source, Integer insertPoint){
            super(source);
            this.insertPoint = insertPoint;
        }

        public Integer getInsertPoint() {
            return insertPoint;
        }

        public void setInsertPoint(Integer insertPoint) {
            this.insertPoint = insertPoint;
        }
        
        
    }
}
