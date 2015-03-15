/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.pdb;

import com.gas.common.ui.util.StrUtil;

/**
 *
 * @author dq
 */
public class END {

    public final static String RECORD_NAME = "END ";

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        StrUtil.replace(ret, RECORD_NAME.trim(), 1, 6, true);
        StrUtil.replace(ret, '\n', 81, 81, true);
        return ret.toString();
    }
}
