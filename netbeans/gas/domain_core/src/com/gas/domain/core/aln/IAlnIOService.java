/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.aln;

import com.gas.domain.core.aln.Aln;
import java.io.File;
import java.io.InputStream;

/**
 *
 * @author dq
 */
public interface IAlnIOService {

    Aln parse(String content);

    Aln parse(InputStream inputStream);

    Aln parse(Class clazz, String name);

    Aln parse(File file);
}
