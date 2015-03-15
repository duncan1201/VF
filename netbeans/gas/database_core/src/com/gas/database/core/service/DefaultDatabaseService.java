package com.gas.database.core.service;

import com.gas.database.core.conn.DbConnSettingsService;
import com.gas.database.core.service.api.IDefaultDatabaseService;
import org.hibernate.cfg.Configuration;

import com.gas.domain.core.filesystem.Folder;
import com.gas.database.core.DBUtil;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.H2ServerUtil;
import com.gas.database.core.SchemaExportHelper;
import com.gas.database.core.filesystem.service.FolderService;
import com.gas.database.core.schema.api.IDatabaseSchemaUpgradeService;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=IDefaultDatabaseService.class)
public class DefaultDatabaseService implements IDefaultDatabaseService {

   private static DefaultDatabaseService instance;

   public DefaultDatabaseService() {
   }

   public static DefaultDatabaseService getInstance() {
      if (instance == null) {
	 instance = new DefaultDatabaseService();
      }
      return instance;
   }

    @Override
   public boolean isDefaultDatabaseRunning() {
      return H2ServerUtil.isRunning();
   }

   @Override
   public void startDefaultDatabaseServer() {
      if (!H2ServerUtil.isRunning()) {
	 H2ServerUtil.startTcpServer();
      }
   }

   @Override
   public void stopDefaultDatabaseServer() {
      if (H2ServerUtil.isRunning()) {
	 H2ServerUtil.stopTcpServer();
      }
   }

    @Override
   public void initDefaultDatabaseSchema() {
      Configuration cfg = HibernateConfigService.getInstance().getDefaultConfiguration();
      SchemaExportHelper.justCreate(cfg);
   }

    @Override
   public boolean isDefaultDatabaseSchemaPresent() {
      DbConnSettings settings = DbConnSettingsService.getInstance().getDbConnSettings();
      boolean exists = DBUtil.isTablePresent(settings.getProperties(), IDatabaseSchemaUpgradeService.TALLE_AS);
      return exists;
   }
}
