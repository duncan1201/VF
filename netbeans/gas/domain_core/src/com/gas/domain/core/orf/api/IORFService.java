/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.orf.api;

import com.gas.common.ui.core.LocList;
import java.util.Map;

/**
 *
 * @author dq
 */
public interface IORFService {

    Map<Integer, LocList> find(ORFParam params);

    ORFResult findORFResult(ORFParam params);
}
