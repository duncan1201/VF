/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape;

import com.gas.common.ui.core.StringComparator;
import com.gas.common.ui.core.StringList;
import com.gas.domain.core.as.Feture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author dq
 */
public class IShapeListMap extends TreeMap<String, IShapeList> {

    public IShapeListMap() {
        super(new StringComparator());
    }

    public List<Object> getAllData() {
        List<Object> ret = new ArrayList<Object>();
        Iterator<IShapeList> itr = this.values().iterator();
        while (itr.hasNext()) {
            IShapeList arrowList = itr.next();
            ret.addAll(arrowList.getAllData());
        }
        return ret;
    }

    public IShapeListMap getByDisplayTexts(String... displayTexts) {
        IShapeListMap ret = new IShapeListMap();
        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            IShapeList arrowList = get(key);
            IShapeList returned = arrowList.getByDisplayTexts(displayTexts);
            ret.put(key, returned);
        }
        return ret;
    }

    public IShapeListMap getByLocation(int start, int end) {
        IShapeListMap ret = new IShapeListMap();
        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            IShapeList arrowList = get(key);
            ret.put(key, arrowList.getByLocation(start, end));
        }
        return ret;
    }

    public IShapeListMap getByTypes(String... keys) {
        StringList keyList = new StringList(keys);
        IShapeListMap ret = new IShapeListMap();
        Iterator<String> itr = keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            if (keyList.containsIgnoreCase(key)) {
                ret.put(key, get(key));
            }
        }
        return ret;
    }

    public void create(Collection<Feture> fetures) {
        if (fetures == null) {
            return;
        }
        clear();

        Iterator<Feture> fetureItr = fetures.iterator();
        while (fetureItr.hasNext()) {
            Feture feture = fetureItr.next();
            Arrow arrow = ArrowCreator.create(feture);
            IShapeList arrowList = get(feture.getKey());
            if (arrowList == null) {
                arrowList = new IShapeList();
            }
            arrowList.add(arrow);
            put(feture.getKey(), arrowList);
        }
    }
}

