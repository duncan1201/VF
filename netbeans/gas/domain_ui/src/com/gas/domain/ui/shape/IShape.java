/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.as.Lucation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

/**
 *
 * @author dunqiang
 */
public interface IShape {

    public enum TEXT_LOC {
        ABOVE, BELOW, BEFORE, AFTER, INSIDE, NONE
    };   

    void setTextLoc(TEXT_LOC textLoc);
    
    TEXT_LOC getTextLoc();
    
    Dimension getSize();
    
    Color getSeedColor();
    
    float getHighlightRoundness();
    
    boolean isMouseHover();
    
    void setArrowPaths(GeneralPathList arrowPaths);
    
    GeneralPathList getArrowPaths();
    
    int getRoundness();

    int getArrowCapWidth();

    int getBarHeight();

    Boolean getForward();

    void setForward(Boolean forward);

    Font getTextFont();
    
    Loc getLoc();
    
    Object getData();
        
    String getDisplayText();
    
    void setSeedColor(Color color);
    
    Lucation getLuc();
    
    boolean isSelected();
    
    void setSelected(boolean selected);
        
    void setTextFont(Font font);
    
    int getTextLeftPadding();
    
    int getTextRightPadding();
    
    int getTextTopPadding();
    
    int getTextBottomPadding();
    
    void setBounds(Rectangle bounds);
    
    void setBounds(int x, int y, int width, int height);
    
    int getVerticalPadding();
    
    Rectangle getBounds();
    
}
