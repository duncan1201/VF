/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESummary.api;

/**
 *
 * @author dunqiang
 */
public interface IESummaryCmd {

    String getDb();

    String getEmail();

    String getId();

    String getRetmax();

    String getRetmode();

    String getRetstart();

    String getTool();

    String getVersion();

    <T> T sendRequest(Class<T> retType);

    void setDb(String db);

    void setEmail(String email);

    void setId(String id);

    void setRetmax(String retmax);

    void setRetmode(String retmode);

    void setRetstart(String retstart);

    void setTool(String tool);

    void setVersion(String version);
    
}
