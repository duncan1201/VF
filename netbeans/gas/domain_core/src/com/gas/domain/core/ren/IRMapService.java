/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import com.gas.common.ui.core.LocList;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dunqiang
 */
public interface IRMapService {

    RMap findRM(String sequence, boolean circular, String renListName, RENSet rens, Integer minOccurence, Integer maxOccurence, LocList locList, Boolean allow);

    List<Feture> toFeatures(RMap rmap);
}
