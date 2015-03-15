/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author dq
 */
class OligoTreeNode extends DefaultMutableTreeNode {

    private boolean seq;
    private boolean forward;
    private boolean tm;
    private boolean gc;
    private boolean selfAny;
    private boolean selfEnd;
    private boolean hairpin;

    OligoTreeNode(Object obj) {
        super(obj);
        if(obj == null){
            setAllowsChildren(false);
        }else if (obj instanceof Set) {
            setAllowsChildren(true);
            createChildren((Set) obj);
        } else if (obj instanceof Oligo) {
            setAllowsChildren(true);
            createChildren((Oligo) obj);
        } else if (obj instanceof OligoElement) {
            setAllowsChildren(false);
            //setAllowsChildren(true);
            //createChildren((OligoElement) obj);
        } else if (obj instanceof String) {
            setAllowsChildren(false);
        } else if (obj instanceof Float) {
            setAllowsChildren(false);
        }
    }

    <T> OligoTreeNode getAncestor(Class<T> clazz) {
        OligoTreeNode ret = null;
        OligoTreeNode _parent = (OligoTreeNode) getParent();
        while (_parent != null) {
            Object _userObject = _parent.getUserObject();
            if (clazz.isAssignableFrom(_userObject.getClass())) {
                ret = _parent;
                break;
            }
            _parent = (OligoTreeNode) _parent.getParent();
        }
        return ret;
    }

    private void createChildren(OligoElement oe) {
        OligoTreeNode child;

        final NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        final NumberFormat nfPc = NumberFormat.getPercentInstance(Locale.ENGLISH);
        nfPc.setMinimumFractionDigits(1);
        nfPc.setMaximumFractionDigits(1);

        String _seq = oe.getSeq();
        String tail = oe.getTail();
        if (_seq != null && !_seq.isEmpty()) {
            if (tail != null && !tail.isEmpty()) {
                if(oe.getForward()){
                    child = new OligoTreeNode(String.format("%s-%s", tail, _seq));
                }else{
                    child = new OligoTreeNode(String.format("%s-%s", _seq, tail));
                }
            } else {
                child = new OligoTreeNode(_seq);
            }
            child.setSeq(true);
            child.setForward(oe.getForward());
            add(child);
        }

        Float _tm = oe.getTm();
        if (_tm != null) {
            child = new OligoTreeNode(_tm);
            child.setTm(true);
            add(child);
        }

        Float _gc = oe.getGc() / 100f;
        if (_gc != null) {
            child = new OligoTreeNode(_gc);
            child.setGc(true);
            add(child);
        }

        Float any = oe.getSelfAny();
        if (any != null) {
            child = new OligoTreeNode(any);
            child.setSelfAny(true);
            add(child);
        }

        Float end = oe.getSelfEnd();
        if (end != null) {
            child = new OligoTreeNode(end);
            child.setSelfEnd(true);
            add(child);
        }

        Float _hairpin = oe.getHairpin();
        if (_hairpin != null) {
            child = new OligoTreeNode(_hairpin);
            child.setHairpin(true);
            add(child);
        }

    }

    private void createChildren(Set<Oligo> oligos) {
        List<Oligo> list = new ArrayList<Oligo>(oligos);
        Collections.sort(list, new Oligo.NoComparator());
        for (Oligo oligo : list) {
            add(new OligoTreeNode(oligo));
        }
    }

    boolean isForward() {
        return forward;
    }

    void setForward(boolean forward) {
        this.forward = forward;
    }

    boolean isSeq() {
        return seq;
    }

    void setSeq(boolean seq) {
        this.seq = seq;
    }

    boolean isOligo() {
        Object o = super.getUserObject();
        return o instanceof Oligo;
    }

    boolean isOligoElement() {
        Object o = super.getUserObject();
        return o instanceof OligoElement;
    }

    boolean isString() {
        return super.getUserObject() instanceof String;
    }

    boolean isFloat() {
        return super.getUserObject() instanceof Float;
    }

    private void createChildren(Oligo oligo) {
        OligoElement left = oligo.getLeft();
        if (left != null) {
            OligoTreeNode child = new OligoTreeNode(left);
            add(child);
        }
        OligoElement internal = oligo.getInternal();
        if (internal != null) {
            OligoTreeNode child = new OligoTreeNode(internal);
            add(child);
        }
        OligoElement right = oligo.getRight();
        if (right != null) {
            OligoTreeNode child = new OligoTreeNode(right);
            add(child);
        }
    }

    boolean isTm() {
        return tm;
    }

    void setTm(boolean tm) {
        this.tm = tm;
    }

    boolean isCollection() {
        return getUserObject() instanceof Collection;
    }

    boolean isGc() {
        return gc;
    }

    void setGc(boolean gc) {
        this.gc = gc;
    }

    boolean isSelfAny() {
        return selfAny;
    }

    void setSelfAny(boolean selfAny) {
        this.selfAny = selfAny;
    }

    boolean isSelfEnd() {
        return selfEnd;
    }

    void setSelfEnd(boolean selfEnd) {
        this.selfEnd = selfEnd;
    }

    boolean isHairpin() {
        return hairpin;
    }

    void setHairpin(boolean hairpin) {
        this.hairpin = hairpin;
    }
}
