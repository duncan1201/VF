/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.mrbayes.ui;

import com.gas.phylo.mrbayers.api.IMrBayesPanel;
import com.gas.phylo.mrbayers.api.MrBayesParam;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class MrBayesPanel extends JPanel implements IMrBayesPanel{
    
    public MrBayesPanel(){
    }
    
    @Override
    public MrBayesParam getMrBayersParam(){
        MrBayesParam ret = new MrBayesParam();
        return ret;
    }
}
