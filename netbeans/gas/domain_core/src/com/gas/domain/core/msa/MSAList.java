/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.core.StringSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class MSAList extends ArrayList<MSA> {

    public MSAList() {
    }

    public MSAList(List<MSA> msas) {
        addAll(msas);
    }

    public StringList getNames() {
        StringList ret = new StringList();
        for (MSA msa : this) {
            ret.add(msa.getName());
        }
        return ret;
    }

    public StringList getEntryNames() {
        StringList ret = new StringList();
        for (MSA msa : this) {
            ret.addAll(msa.getEntriesNames());
        }
        return ret;
    }

    public StringSet getDuplicateEntryNames() {
        StringList ret = new StringList();
        for (MSA msa : this) {
            StringList entryNames = msa.getEntriesNames();
            ret.addAll(entryNames);
        }
        return ret.getDuplicates();
    }

    public MSA get(String name) {
        MSA ret = null;
        for (MSA msa : this) {
            if (msa.getName().equals(name)) {
                ret = msa;
                break;
            }
        }
        return ret;
    }
}
