/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.ColorGenerator;
import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
class EndsViewList extends ArrayList<EndsView> {

    EndsViewList() {
    }

    EndsViewList(AnnotatedSeqList asList, boolean circular) {
        List<Color> colors = ColorGenerator.generate(asList.size());
        for (int i = 0; i < asList.size(); i++) {
            AnnotatedSeq as = asList.get(i);
            Color bgColor = colors.get(i);
            EndsView v;
            if (circular) {
                if (asList.isLast(as)) {
                    v = new EndsView(as, true);
                } else {
                    v = new EndsView(as);
                }
            } else {
                v = new EndsView(as);
            }
            v.bgColor = bgColor;
            add(v);
        }
    }

    void setSelected(AnnotatedSeq as) {
        for (EndsView e : this) {
            e.setSelected(e.getAs() == as);
        }
    }

    void setSelected(String asName) {
        for (EndsView e : this) {
            if (e.getAs() != null) {
                e.setSelected(e.getAs().getName().equals(asName));
            } else {
                e.setSelected(false);
            }
        }
    }

    EndsView getSelected() {
        EndsView ret = null;
        for (EndsView e : this) {
            if (e.isSelected()) {
                ret = e;
                break;
            }
        }
        return ret;
    }

    EndsView getEndsView(Point pt) {
        EndsView ret = null;
        for (EndsView e : this) {
            boolean contains = e.contains(pt);
            if (contains) {
                ret = e;
                break;
            }
        }
        return ret;
    }

    void paint(Graphics2D g, int x, int ySeqMiddle) {
        int xInit = x;
        boolean circular = isCircular();
        if (circular) {
            EndsView eCircular = getCircular();
            int linearWidth = getLinearWidth();
            eCircular.paintCircular(g, xInit, xInit + linearWidth, ySeqMiddle, size() + "");
            for (int i = 0; i < size(); i++) {
                EndsView e = get(i);
                if (!e.isCircular()) {
                    x = e.paintLinear(g, x, ySeqMiddle, (i + 1 ) + "");
                }
            }
            EndsView selected = getSelected();
            if (selected != null) {
                if (!selected.isCircular()) {
                    selected.paintLinear(g);
                }
            }
        } else {
            for (int i = 0; i < size(); i++) {
                EndsView e = get(i);
                x = e.paintLinear(g, x, ySeqMiddle, (i + 1 ) + "");
            }
            EndsView selected = getSelected();
            if (selected != null) {
                selected.paintLinear(g);
            }
        }

    }

    EndsView getCircular() {
        EndsView ret = null;
        for (EndsView e : this) {
            if (e.isCircular()) {
                ret = e;
                break;
            }
        }
        return ret;
    }

    boolean isCircular() {
        for (EndsView e : this) {
            boolean circular = e.isCircular();
            if (circular) {
                return true;
            }
        }
        return false;
    }

    int getDesiredWidth() {
        int ret = 0;
        for (EndsView v : this) {
            ret += v.getDesiredWidth();
        }
        return ret;
    }

    /**
     * <pre>
     * @return the linear width that includs the starting or ending overhangs
     * -----
     *   -----
     * </pre>
     */
    int getLinearWidth() {
        int ret = 0;

        EndsViewList tmpsLinear = new EndsViewList();
        for (EndsView v : this) {
            if (!v.isCircular()) {
                tmpsLinear.add(v);
            }
        }

        for (EndsView v : tmpsLinear) {
            ret += v.getDesiredWidth();
        }
        Integer charWidth = null;
        int overhangLength = 0;
        for (EndsView v : tmpsLinear) {
            if (!v.isCircular()) {
                if (charWidth == null) {
                    FontMetrics fm = FontUtil.getFontMetrics(v.font);
                    charWidth = fm.charWidth('A');
                }
                if (!tmpsLinear.isFirst(v) && !tmpsLinear.isLast(v)) {
                    overhangLength += v.getStartOverhangLength();
                    overhangLength += v.getEndOverhangLength();
                } else if (tmpsLinear.isFirst(v) && !tmpsLinear.isLast(v)) {
                    overhangLength += v.getEndOverhangLength();
                } else if (tmpsLinear.isLast(v) && !tmpsLinear.isFirst(v)) {
                    overhangLength += v.getStartOverhangLength();
                }
            }
        }
        ret = ret - (overhangLength / 2) * charWidth;
        return ret;
    }

    private boolean isLast(EndsView e) {
        if (!isEmpty()) {
            return get(size() - 1) == e;
        } else {
            return false;
        }
    }

    private boolean isFirst(EndsView e) {
        if (!isEmpty()) {
            return get(0) == e;
        } else {
            return false;
        }
    }
}