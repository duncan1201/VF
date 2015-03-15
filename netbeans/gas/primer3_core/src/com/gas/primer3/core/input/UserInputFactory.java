package com.gas.primer3.core.input;

import com.gas.common.ui.core.StringList;
import com.gas.domain.core.primer3.IUserInputFactory;
import com.gas.domain.core.primer3.UserInput;
import java.io.InputStream;
import java.util.Map;

import com.gas.primer3.core.api.BoulderIOUtil;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=IUserInputFactory.class)
public class UserInputFactory implements IUserInputFactory {

    private static final UserInput WEB_DEFAULT_SETTINGS = new UserInput();
    private static final UserInput P3WEB_V_3_0_0 = new UserInput();
    
    static {
        WEB_DEFAULT_SETTINGS.resetData(BoulderIOUtil.parse(UserInputFactory.class.getResourceAsStream("web_default_settings.txt")));
        P3WEB_V_3_0_0.resetData(BoulderIOUtil.parse(UserInputFactory.class, "settings_p3web_v3-0-0.txt"));
    }

    static UserInput getInput(InputStream inputStream) {
        UserInput ret = new UserInput();
        Map<String, String> data = BoulderIOUtil.parse(inputStream);
        ret.resetData(data);
        return ret;
    }

    public UserInput getWebDefaultSetting() {        
        return getP3WEB_V_3_0_0(false);
    }
    
    @Override
    public UserInput getP3WEB_V_3_0_0(){
        return getP3WEB_V_3_0_0(false);
    }
    
    public static UserInput getP3WEB_V_3_0_0(boolean checkPrimers){
        UserInput ret = new UserInput();
        ret.getData().putAll(P3WEB_V_3_0_0.getData());
        if(checkPrimers){
            StringList keeps = new StringList(UserInput.TM_NAMES);
            keeps.add("PRIMER_FIRST_BASE_INDEX");
            ret.keepsOnly(keeps.toArray(new String[keeps.size()]));
            ret.setPrimerTask("check_primers");
            ret.enablePickAnyway();
        }
        return ret;
    }
}
