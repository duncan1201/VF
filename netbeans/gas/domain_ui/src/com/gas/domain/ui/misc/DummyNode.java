/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.misc;

import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author dunqiang
 */
public class DummyNode extends AbstractNode {

    SaveCookie saveCookie;
    InstanceContent content;

    public DummyNode(InstanceContent content) {
        super(Children.LEAF);
        this.content = content;
    }

    public SaveCookie getSaveCookie() {
        return saveCookie;
    }

    public void setSaveCookie(SaveCookie saveCookie) {
        this.saveCookie = saveCookie;
    }

    //We will call this method, i.e., dummyNode.fire(),
    //from a document listener set on the text field:
    public void fire(boolean modified) {
        if (modified) {
            //If the text is modified,
            //we add the SaveCookie implementation to the cookieset:
            content.add(saveCookie);
            //getCookieSet().assign(SaveCookie.class, saveCookie);
        } else {
            //Otherwise, we make no assignment
            //getCookieSet().assign(SaveCookie.class);
            content.remove(saveCookie);
        }
    }
}