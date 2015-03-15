/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update;

import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
// schedule refresh providers
        // install update checker when UI is ready (main window shown)
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
            }
        });
    }
}
