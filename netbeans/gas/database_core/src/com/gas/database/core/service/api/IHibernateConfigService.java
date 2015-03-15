/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.service.api;

import java.util.List;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dunqiang
 */
public interface IHibernateConfigService {
   
   Configuration getDefaultConfiguration();
      
}
