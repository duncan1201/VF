/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.core.StringList;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author dq
 */
public class AnnotatedSeqList extends ArrayList<AnnotatedSeq> {

    public AnnotatedSeqList() {
    }

    public AnnotatedSeqList(List<AnnotatedSeq> data) {
        addAll(data);
    }

    public boolean areBasesIdentical() {
        String cur = null;
        for (AnnotatedSeq as : this) {
            String tmp = as.getSiquence().getData().toUpperCase(Locale.ENGLISH);
            if (cur == null) {
                cur = tmp;
            } else {
                if (!cur.equals(tmp)) {
                    return false;
                }
            }
        }
        return true;
    }

    public StringList getNames() {
        StringList ret = new StringList();
        for (AnnotatedSeq as : this) {
            String name = as.getName();
            ret.add(name);
        }
        return ret;
    }

    public AnnotatedSeq first() {
        if (isEmpty()) {
            return null;
        } else {
            return get(0);
        }
    }

    public AnnotatedSeq prev(AnnotatedSeq as) {
        AnnotatedSeq ret = null;
        if (size() > 1) {
            for (int i = 0; i < size(); i++) {
                if (as == get(i)) {
                    if (i - 1 > -1) {
                        ret = get(i - 1);
                    } else {
                        ret = get(size() - 1);
                    }
                }
            }
        }
        return ret;
    }

    public void moveLeft(AnnotatedSeq as) {
        if(size() <= 1 ){
            return;
        }        
        for (int i = 0; i < size(); i++) {
            AnnotatedSeq _as = get(i);
            if (_as == as) {
                _as = this.remove(i);
                if (i > 0) {
                    this.add(i - 1, _as);
                } else {
                    this.add(_as);
                }
                break;
            }
        }
    }

    public void moveRight(AnnotatedSeq as) {
        if(size() <= 1 ){
            return;
        }
        for (int i = 0; i < size(); i++) {
            AnnotatedSeq _as = get(i);
            if (_as == as) {
                _as = this.remove(i);                
                if(i + 1 == size()){
                    this.add(_as);
                }else if (i + 1 < size()) {
                    this.add(i + 1, _as);
                } else {
                    this.add(0, _as);
                }
                break;
            }
        }        
    }

    public AnnotatedSeq next(AnnotatedSeq as) {
        AnnotatedSeq ret = null;
        if (size() > 1) {
            for (int i = 0; i < size(); i++) {
                if (as == get(i)) {
                    if (i + 1 < size()) {
                        ret = get(i + 1);
                    } else {
                        ret = get(0);
                    }
                }
            }
        }
        return ret;
    }
    
    public boolean isLast(AnnotatedSeq as){
        return last() == as;
    }

    public AnnotatedSeq last() {
        if (isEmpty()) {
            return null;
        } else {
            return get(size() - 1);
        }
    }
}
