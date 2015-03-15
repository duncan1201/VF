/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.service.api;

/**
 *
 * @author dunqiang
 */
public interface IDefaultDatabaseService {       
    
   void initDefaultDatabaseSchema();

   void startDefaultDatabaseServer();

   void stopDefaultDatabaseServer();
   
   boolean isDefaultDatabaseRunning();
   
   boolean isDefaultDatabaseSchemaPresent();
   
}
