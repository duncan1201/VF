/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.kromatogram.api;

import com.gas.domain.core.tigr.Kromatogram;

/**
 *
 * @author dq
 */
public interface IChromatogramEditor {
    Kromatogram getKromatogram();
    void setKromatogram(Kromatogram kromatogram);
}
