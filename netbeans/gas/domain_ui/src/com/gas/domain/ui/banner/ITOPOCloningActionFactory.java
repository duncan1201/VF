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
public interface ITOPOCloningActionFactory {
    Action createTOPOCloningAction();
    Action createTOPOTACloningAction();
    Action createDirectionalTOPOInsertAction();
    Action createDirectionalTOPOCloningAction();
    Action createTAInsertAction();
}
