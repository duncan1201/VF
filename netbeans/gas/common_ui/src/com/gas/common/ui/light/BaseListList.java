/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.MathUtil;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class BaseListList extends ArrayList<BaseList> {

    /**
     * @param row: 1-based
     */
    public String getName(Integer row) {
        String ret = null;
        if (row >= 1 && row <= size()) {
            ret = get(row - 1).getName();
        }
        return ret;
    }

    public String getName(Point pt) {
        String ret = null;
        for (BaseList b : this) {
            if (b.getRect().contains(pt)) {
                ret = b.getName();
                break;
            }
        }
        return ret;
    }

    public Point getPos(Point ptParent) {
        Point ret = null;
        for (int i = 0; i < size(); i++) {
            BaseList tList = get(i);
            Rectangle2D.Double tListRect = tList.getRect();
            if(ptParent.y <= tListRect.getMaxY() && ptParent.y >= tListRect.getMinY()){
                Integer caretPos = tList.getCaretPos(ptParent.x);
                ret = new Point(caretPos, i + 1);
                break;
            }            
        }
        return ret;
    }

    /**
     * @return 1-based row no
     */
    public Integer getRowNo(String rowName) {
        Integer ret = null;
        for (int i = 0; i < size(); i++) {
            BaseList b = get(i);
            if (b.getName().equals(rowName)) {
                ret = i + 1;
                break;
            }
        }
        return ret;
    }

    public int getLength() {
        if (isEmpty()) {
            return 0;
        } else {
            return iterator().next().size();
        }
    }

    public Rectangle2D getRect() {
        Rectangle2D ret = null;
        for (int i = 0; i < size(); i++) {
            BaseList baseList = get(i);
            Rectangle2D rect = baseList.getRect();
            if (rect == null) {
                return null;
            }
            if (ret == null) {
                ret = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            } else {
                Rectangle2D.union(ret, rect, ret);
            }
        }
        return ret;
    }

    /**
     * @param selection: 1-based
     */
    public void setSelection(Loc2D selection) {
        if (selection != null) {
            int rowStart = selection.getMinY();
            int rowEnd = selection.getMaxY();

            for (int row = 1; row <= size(); row++) {
                BaseList tList = get(row - 1);
                if (row >= rowStart && row <= rowEnd) {
                    int x1 = selection.x1;
                    int x2 = selection.x2;

                    tList.setSelection(new Loc(Math.min(x1, x2), Math.max(x1, x2)));
                } else {
                    tList.setSelection(null);
                }
            }
        } else {
            for (BaseList tList : this) {
                tList.setSelection(null);
            }
        }
    }

    public Point getCaretLocation(Point pos) {
        Point ret = null;
        if (pos.y <= size() && pos.y >= 1) {
            BaseList textList = get(pos.y - 1);
            ret = textList.getCaretLoc(pos.x);
        }
        return ret;
    }

    public Point getCaretLocation(Point ptParent, int posOffset) {
        return getCaretLocation(ptParent, posOffset, 0);
    }

    public Point getCaretLocation(Point ptParent, int posOffset, int verticalOffset) {
        Point ret = null;

        for (int i = 0; i < size(); i++) {
            BaseList tList = get(i);
            boolean contains = tList.getRect().contains(ptParent);
            if (contains) {
                int caretX = tList.getCaretX(ptParent.x, posOffset);
                int caretY = MathUtil.round(get(i + verticalOffset).getRect().getY());

                ret = new Point(caretX, caretY);

                break;
            }
        }
        return ret;
    }

    public Integer getRowNo(double yParent) {
        Integer ret = null;
        for (int i = 0; i < size(); i++) {
            BaseList tList = get(i);
            Rectangle2D rect = tList.getRect();
            if (yParent >= rect.getMinY() && yParent <= rect.getMaxY()) {
                ret = i + 1;
                break;
            }
        }
        return ret;
    }

    public void setColorProvider(IColorProvider v) {
        for (BaseList t : this) {
            t.setColorProvider((IColorProvider) v);
        }
    }
}
