/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.caret;

import com.gas.common.ui.misc.Loc2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

/**
 *
 * @author dunqiang
 */
public interface ICaretParent {

    /*
     * @param x: x-coordinate on screen @param y: y-coordinate on screen @param
     * onScreen: whether to return on screen coordinate or not
     */
    Point getCaretLocation(Point ptScreen);

    Point getCaretLocationByPos(Point pos);

    Point getCaretPos(Point ptScreen);

    void caretMoved(int direction, boolean isShiftDown, boolean success);

    Point getCaretMaxPos();
    
    Integer getTotalPos();

    Integer getCaretWidth();

    Integer getCaretRadius();

    Integer getCaretHeight();

    void fireCaretMoveEvent(CaretMoveEvent event);

    void addListener(ICaretParentListener listener);

    /*
     * @param forward: if true, it will delete the character after the caret and
     * the caret remains where it was; if false, it will delete the char before
     * the caret and the caret will move backward by one
     *
     */
    void delete(Point loc, boolean forward);

    /*
     * @ret 1-based
     */
    Point getCaretPos();
    
    void insert(Point caretPos, Character c);
    
    void insert(Point caretPos, String str);

    void replace(Loc2D caretParentSelection, String c);

    Loc2D getSelection();

    JCaret getCaret();

    /**
     * @return in degrees
     */
    Double getCaretAngleByPos(Integer pos);

    boolean isCircular();

    boolean isEditingAllowed();

    boolean isInputValid(String str);
    
    boolean isInputValid(KeyEvent e);
}
