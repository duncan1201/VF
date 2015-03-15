/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.ren.RMap;

/**
 *
 * @author dq
 */
public interface IDigestAction  {
    void setSelectedEntries(RMap.EntryList entries);
    void setAs(AnnotatedSeq as);
}
