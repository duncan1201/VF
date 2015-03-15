/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.appservice.api;

import java.io.File;
import java.util.Date;

/**
 *
 * @author dq
 */
public interface IAppService {
    public enum OS_TYPE {win64, win32, mac};   
    /**
     * it should NOT change for different versions
     */    
    String getAppName();
    String getCurrentVersion();
    OS_TYPE getOS();
    Date getReleaseDate();   
    boolean isProduction();
}
