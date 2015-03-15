/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service.api;

/**
 *
 * @author dq
 */
public interface ISoftProductService {
    void checkNewRelease(boolean showProgress, boolean quiteOnNoUpdates);
    boolean isCheckAtStartup();
    void setCheckAtStartup(boolean check);
    int compareEdition(SoftProduct sp1, SoftProduct sp2);
    int compareEdition(String e1, String e2);
}
