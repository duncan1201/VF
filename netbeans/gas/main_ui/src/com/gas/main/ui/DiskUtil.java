/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

/**
 *
 * @author dq
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.h2.util.IOUtils;
import org.openide.util.Utilities;

class DiskUtil {

    static String getComputerFingerprint() {
        if (Utilities.isWindows()) {
            return getFingerprintWindows();
        } else if (Utilities.isMac()) {
            return getFingerprintMac();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    static String getFingerprintMac() {
        String ret = null;
        String cmd = "ioreg -l | awk '/IOPlatformSerialNumber/ { print $4;}'";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        pb.redirectErrorStream(true);
        Process process = null;
        try {
            process = pb.start();
            String s;
            // read from the process's combined stdout & stderr
            BufferedReader stdout = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            while ((s = stdout.readLine()) != null) {
                ret = StringUtils.strip(s, "\"");
            }
            System.out.println("Exit value: " + process.waitFor());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (process != null) {
                IOUtils.closeSilently(process.getInputStream());
                IOUtils.closeSilently(process.getOutputStream());
                IOUtils.closeSilently(process.getErrorStream());
            }
        }
        return ret;
    }

    static String getFingerprintWindows() {
        String driveLetter = getInstalledDriveLetterWindows();
        String ret = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\"" + driveLetter + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                ret += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ret = ret.trim();
        if(ret.startsWith("-")){
            ret = ret.substring(1);
        }
        return ret.trim();
    }

    static String getInstalledDriveLetterWindows() {
        URL url = DiskUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String urlPath = url.getPath();
        int indexStart = urlPath.indexOf(":") + 2;
        String driveLetter = urlPath.substring(indexStart, indexStart + 1);

        return driveLetter;
    }
}
