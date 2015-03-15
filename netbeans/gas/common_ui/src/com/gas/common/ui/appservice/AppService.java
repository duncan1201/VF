package com.gas.common.ui.appservice;

import com.gas.common.ui.appservice.api.IAppService;
import com.gas.common.ui.util.DateUtil;
import java.io.File;
import java.util.Date;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IAppService.class)
public class AppService implements IAppService{

    private final static String appName = "VectorFriends";
    private final static String currentVersion = "1.2.0";
    private final static Date releaseDate = DateUtil.create(2015, 2, 13);        
    
    @Override
    public boolean isProduction(){
        return true;
    }
    
    @Override
    public String getAppName() {
        return appName;
    }        

    @Override
    public String getCurrentVersion() {
        return currentVersion;
    }

    @Override
    public Date getReleaseDate() {        
        return releaseDate;
    }

    @Override
    public OS_TYPE getOS() {
        OS_TYPE ret;
        boolean isMac = Utilities.isMac();
        boolean isWindows = Utilities.isWindows();
        if(isMac){
            ret = OS_TYPE.mac;
        }else if(isWindows){
            String osArch = System.getProperty("os.arch");
            if(osArch.equals("amd64")){
                ret = OS_TYPE.win64;
            }else if(osArch.equals("x86")){
                ret = OS_TYPE.win32;
            }else{
                ret = OS_TYPE.win64;
            }
        }else{
            throw new UnsupportedOperationException("Your OS is not supported");
        }
        return ret;
    }
}
