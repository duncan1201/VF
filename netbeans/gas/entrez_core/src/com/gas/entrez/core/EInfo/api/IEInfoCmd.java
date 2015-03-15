/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EInfo.api;

import java.util.List;

/**
 *
 * @author dunqiang
 */
public interface IEInfoCmd {
    String getDb();
    void setDb(String db);
    <T> T sendRequest(Class<T> clazz);
    List<EInfoResult> getPreloaded();
    EInfoResult getPreloaded(String db);
}