/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import com.gas.domain.core.aln.IAlnIOService;
import com.gas.domain.core.misc.api.INewickIOService;
import com.gas.domain.core.misc.api.Newick;
import java.io.File;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = INewickIOService.class)
public class NewickIOService implements INewickIOService {

    @Override
    public Newick parse(File file) {
        NewickParser parser = new NewickParser();
        return parser.parse(file);
    }

    @Override
    public Newick parse(Class clazz, String name) {
        NewickParser parser = new NewickParser();
        return parser.parse(clazz, name);
    }

    public Newick parse(String newickStr) {
        NewickParser parser = new NewickParser();
        return parser.parse(newickStr);
    }
}
