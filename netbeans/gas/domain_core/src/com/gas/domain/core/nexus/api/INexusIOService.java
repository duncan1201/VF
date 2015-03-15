/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus.api;

import java.io.File;

/**
 *
 * @author dq
 */
public interface INexusIOService {
    Nexus parse(Class clazz, String name);
    Nexus parse(File file);
}
