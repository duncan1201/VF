/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Qualifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author dq
 */
public class FeatureTreeNode extends DefaultMutableTreeNode {

    public FeatureTreeNode(Object obj) {
        this(obj, true);
    }

    public FeatureTreeNode(Object obj, boolean allowChildren) {
        super(obj, allowChildren);
        if (allowChildren) {
            createChildren();
        }
    }

    @Override
    public FeatureTreeNode getChildAt(int i) {
        FeatureTreeNode ret = (FeatureTreeNode) super.getChildAt(i);
        return ret;
    }

    private void createChildren() {
        if (userObject == null) {
            return;
        }
        if (userObject instanceof Map) {
            Map<String, List<Feture>> map = (Map<String, List<Feture>>) userObject;
            Iterator<String> itr = map.keySet().iterator();
            while (itr.hasNext()) {
                final String key = itr.next();
                List<Feture> fetures = map.get(key);
                FeatureTreeNode child = new FeatureTreeNode(fetures);
                add(child);
            }
        } else if (userObject instanceof List) {
            List<Feture> fetures = (List<Feture>)userObject;
            Iterator<Feture> itr = fetures.iterator();
            while(itr.hasNext()){
                Feture feture = itr.next();
                FeatureTreeNode child = new FeatureTreeNode(feture);
                add(child);
            }
        } else if (userObject instanceof Feture) {
            Feture feture = (Feture) userObject;
            List<Qualifier> qs = new ArrayList<Qualifier>(feture.getQualifierSet().getQualifiers());
            Collections.sort(qs, new Qualifier.QualifierComparator());
            Iterator<Qualifier> qItr = qs.iterator();
            while (qItr.hasNext()) {
                Qualifier q = qItr.next();
                if(!q.isTranslation()){
                    FeatureTreeNode child = new FeatureTreeNode(q, false);
                    if(!q.isNote()){
                        add(child);
                    }else{
                        insert(child, 0);
                    }
                }
            }
        } else if(userObject instanceof Qualifier){
            
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
