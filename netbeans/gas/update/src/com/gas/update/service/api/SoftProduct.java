/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service.api;

import com.gas.common.ui.util.StrUtil;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author dq
 */
public class SoftProduct {

    private JSONObject obj;

    public SoftProduct(JSONObject obj) {
        this.obj = obj;
    }

    public String getName() {
        return (String) obj.get("name");
    }

    public String getEdition() {
        return (String) obj.get("edition");
    }

    public String getReleaseNotesURL() {
        return (String) obj.get("releaseNotesURL");
    }

    public String getInstallerURL() {
        return (String) obj.get("installerURL");
    }

    public Date getReleaseDate() {
        Number number = (Number) obj.get("releaseTime");
        return new Date(number.longValue());
    }

    public Boolean getSupportWin64() {
        String os = (String) obj.get("os");
        return os != null && os.equalsIgnoreCase("win64");
    }

    public Boolean getSupportWin32() {
        String os = (String) obj.get("os");
        return os != null && os.equalsIgnoreCase("win32");
    }

    public Boolean getSupportMac() {
        String os = (String) obj.get("os");
        return os != null && os.equalsIgnoreCase("mac");
    }

    public static class Sorter implements Comparator<SoftProduct> {

        public int compare(String edition, String edition2) {
            int ret = 0;

            List<String> e1 = StrUtil.tokenize(edition, ".");
            List<String> e2 = StrUtil.tokenize(edition2, ".");

            for (int i = 0; i < Math.max(e1.size(), e2.size()); i++) {

                if (i >= e1.size()) {
                    ret = -1;
                    break;
                } else if (i >= e2.size()) {
                    ret = 1;
                    break;
                } else {
                    Integer one = Integer.parseInt(e1.get(i));
                    Integer two = Integer.parseInt(e2.get(i));
                    ret = one.compareTo(two);
                    if (ret != 0) {
                        break;
                    }
                }
            }

            return ret;
        }

        @Override
        public int compare(SoftProduct o, SoftProduct o2) {
            int ret = o.getName().compareTo(o2.getName());
            if (ret == 0) {
                ret = compare(o.getEdition(), o2.getEdition());                
            }
            return ret;
        }
    }
}
