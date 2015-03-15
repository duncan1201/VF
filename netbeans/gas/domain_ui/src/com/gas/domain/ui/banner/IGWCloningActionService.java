/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import javax.swing.Action;

/**
 *
 * @author dq
 */
public interface IGWCloningActionService {
    Action createAnnotateAttSitesAction();
    Action createAddAttBSitesAction();
    Action createOneClickAction();
    Action createLRAction();
    Action createBPAction();
    boolean getAddAttBSitesActionEnablement();
}
