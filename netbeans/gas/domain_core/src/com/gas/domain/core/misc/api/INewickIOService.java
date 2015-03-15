/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc.api;

import java.io.File;

/**
 *
 * @author dq
 */
public interface INewickIOService {

    Newick parse(File file);

    Newick parse(Class clazz, String name);

    Newick parse(String newickStr);
}
