/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ESearch.api;

import java.net.UnknownHostException;

/**
 *
 * @author dunqiang
 */
public interface IESearchCmdService {

    String getDatetype();

    String getDb();

    String getEmail();

    String getMaxdate();

    String getMindate();

    String getReldate();

    String getRetmode();

    String getRettype();

    String getTerm();

    String getTool();


    <T> T sendRequest(Class<T> clazz) throws UnknownHostException;

    void setDatetype(String datetype);

    void setDb(String db);

    void setEmail(String email);

    void setMaxdate(String maxdate);

    void setMindate(String mindate);

    void setReldate(String reldate);

    void setRetmode(String retmode);

    void setRettype(String rettype);

    void setTerm(String term);

    void setTool(String tool);
    
    Integer getRetmax() ;

    void setRetmax(Integer retmax) ;

    Integer getRetstart() ;

    void setRetstart(Integer retstart) ;
    
    
}
