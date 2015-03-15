/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.swing.SwingUtilities;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.OperationContainer;
import org.netbeans.api.autoupdate.OperationException;
import org.netbeans.api.autoupdate.OperationSupport;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateManager;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;

/**
 *
 * @author dq
 */
public class NewClass {
        private Runnable doCheck = new Runnable() {

        @Override
        public void run() {
            if (SwingUtilities.isEventDispatchThread()) {
                RequestProcessor.getDefault().post(doCheck);
                return;
            }
            //if (timeToCheck ()) {
            List<UpdateUnitProvider> providers = UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true);
            Boolean refresh = null;
            Boolean enabled = null;
            try {                
                enabled = providers.get(0).isEnabled();
                refresh = providers.get(0).refresh(null, true);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            List<UpdateUnit> abds = providers.get(0).getUpdateUnits();
            //UpdateUnitProviderFactory.getUpdateUnitProviders();
            Collection<UpdateElement> elements4update = doRealCheck();
            OperationContainer<InstallSupport> container = getContainerForUpdate(elements4update);
            boolean approved = allLicensesApproved(container);
            if(approved){
                InstallSupport support = container.getSupport();
                InstallSupport.Validator validator = doDownload(container);
                doInstall(support, validator);
            }
            //}
        }
    };

    public Collection<UpdateElement> doRealCheck() {
        Collection<UpdateElement> elements4update = new HashSet<UpdateElement>();
        List<UpdateUnit> updateUnits = UpdateManager.getDefault().getUpdateUnits();
        for (UpdateUnit unit : updateUnits) {
            if (unit.getInstalled() != null) { // means the plugin already installed
                if (!unit.getAvailableUpdates().isEmpty()) { // has updates
                    elements4update.add(unit.getAvailableUpdates().get(0)); // add plugin with highest version
                }
            } else {
                System.out.println();
            }
        }
        return elements4update;
    }

    private OperationContainer<InstallSupport> getContainerForUpdate(Collection<UpdateElement> elements4update) {
        OperationContainer<InstallSupport> container = OperationContainer.createForUpdate();
        for (UpdateElement element : elements4update) {
            if (container.canBeAdded(element.getUpdateUnit(), element)) {
                OperationContainer.OperationInfo<InstallSupport> operationInfo = container.add(element);
                if (operationInfo == null) {
                    continue;
                }
                container.add(operationInfo.getRequiredElements());
            }
        }
        return container;
    }

    private boolean allLicensesApproved(OperationContainer<InstallSupport> container) {
        if (!container.listInvalid().isEmpty()) {
            return false;
        }
        for (OperationContainer.OperationInfo<InstallSupport> info : container.listAll()) {
            String license = info.getUpdateElement().getLicence();
            if (!isLicenseApproved(license)) {
                return false;
            }
        }
        return true;
    }

    private boolean isLicenseApproved(String license) {
        // place your code there
        return false;
    }

    public InstallSupport.Validator doDownload(OperationContainer<InstallSupport> container) {
        InstallSupport install = container.getSupport();
        ProgressHandle downloadHandle = ProgressHandleFactory.createHandle("dummy-download-handle");
        InstallSupport.Validator ret = null;
        try {
            ret = install.doDownload(downloadHandle, true);
        } catch (OperationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public OperationSupport.Restarter doInstall(InstallSupport support, InstallSupport.Validator validator) {
        ProgressHandle validateHandle = ProgressHandleFactory.createHandle("dummy-validate-handle");
        org.netbeans.api.autoupdate.InstallSupport.Installer installer; 
        OperationSupport.Restarter ret = null;
        try {
            installer = support.doValidate(validator, validateHandle); // validates all plugins are correctly downloaded
            ProgressHandle installHandle = ProgressHandleFactory.createHandle("dummy-install-handle");
            ret = support.doInstall(installer, installHandle);
        } catch (OperationException ex) {
            Exceptions.printStackTrace(ex);
        }                
        return ret;
    }

    public void doRestartLater(InstallSupport support, OperationSupport.Restarter restarter) {
        support.doRestartLater(restarter);
    }

    public void doRestartNow(InstallSupport support, OperationSupport.Restarter restarter) throws OperationException {
        ProgressHandle installHandle = ProgressHandleFactory.createHandle("dummy-install-handle");
        support.doRestart(restarter, installHandle);
    }
}
