/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.banner.BannerTC;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class MSAEditorListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            MSAEditor editor = (MSAEditor) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("msa")) {
                editor.getMSAPane().setMsa(editor.getMsa());
                UIUtil.setTopCompName(editor, editor.getMsa().getName());

                if (editor.getMsa().getHibernateId() != null) {
                    BannerTC.getInstance().setCheckRowByHibernateId(editor.getMsa().getHibernateId(), true);
                } else {
                    BannerTC.getInstance().setCheckRow(editor.getMsa(), true);
                }
            }
        }
    }
}
