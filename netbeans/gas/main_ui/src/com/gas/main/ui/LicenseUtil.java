/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.appservice.api.IAppService;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CipherUtil;
import com.gas.common.ui.util.UIUtil;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class LicenseUtil {

    final static String ALGO = "RSA";
    static String REGIS_EXPIRED_FORMAT = "<html>Cannot activate the registration code<br/><br/>The registration code is only valid for product released before %s</html>";
    final static StringList PUBLIC_DOMAINS = new StringList("hotmail.com", "gmail.com", "outlook.com", "yahoo.com");
    final static String PUBLIC_KEY = "30819f300d06092a864886f70d010101050003818d00308189028181009dcd8deb2ea3148389bb7eee6a815471ab8bd75c63455c2fc10e45de22a1ac4b2dabea0fd17ad42c3cb1d5ede7ea89de36adcaf288d17f642ed13524739e4a6f03a9b63275e423f55d58109d5b5a98564fbf27fa54803abecb79b131bd51159923882758a9acefbff963aa6c962998fd7eeb6b162e0d08617ef68b129e9466910203010001";
    final static String PRIVATE_KEY = "30820276020100300d06092a864886f70d0101010500048202603082025c020100028181009dcd8deb2ea3148389bb7eee6a815471ab8bd75c63455c2fc10e45de22a1ac4b2dabea0fd17ad42c3cb1d5ede7ea89de36adcaf288d17f642ed13524739e4a6f03a9b63275e423f55d58109d5b5a98564fbf27fa54803abecb79b131bd51159923882758a9acefbff963aa6c962998fd7eeb6b162e0d08617ef68b129e9466910203010001028180430b46349987e7fb4ad542448133997101f553f688a6bb57fea9d5bd5ef15986474d3710a96b8a219157ed103aec46cf066428498b1f9704356fe5282b872110060d53bb5aa50bfa82d7b437eed6a1eb166b22381621eac6b514572a6b49ef9c2a75b2535456cc7da9d667f645c9ea53c879c751656c1edc4867d78ef09db691024100db1ec3d1918836d8ec76a4749010d5a2ee4b8642934f8d8242db6b2b403623846476a1655429ee2b265573c23b03971923e931822e970d26f2d1aaaf28b6ab97024100b85cd018c0763279ae04eb97b85c8bd9f4b86f31bd0d829b61920fc140c9f00092eb73d8de0aec8117466426ca81653efbbe5fdeaf0dca9b500ce610327c6417024048a8f6fe33681be7eae19cbf1a56bac1008c02c568dbe46ce186a90efa87f95719894d13b0c906b5404cedc9e68fd636fbf583f94b41c396bac653f72965099902410094498ad3add2242d3cec1636d58212b6bfa0ad475b618780c50cff81184494b75174f29c2794afbff1619be29b95524343a74f6344781a53ac2283d5d955b607024055a4fe74a3993f871f04eab20ddb52b3ad67199f941046dfd7832418d9969a7bd764421bb8e0938ee0772ccce64199e1c6085bfcac99d380902d4fe5ed96e3cc";
    final static String PUBLIC_KEY_LOCAL = "30819f300d06092a864886f70d010101050003818d00308189028181009d6651a7195d9ccdb103c8003cb4fc6289647cec4e6a6d93a57f3d9eebbc17d41a19abbab9fbec8d0d6321dfcbeaa1ad98e579ac6d90a830961ac41119f1a17908a3afc571627461cca27e1f5987db39d9a162ab44f907492562ba23f40da54d268ad671da5a9872743bc9e22b956bb3b72644c9275fd18ec1d388233986adab0203010001";
    final static String PRIVATE_KEY_LOCAL = "30820276020100300d06092a864886f70d0101010500048202603082025c020100028181009d6651a7195d9ccdb103c8003cb4fc6289647cec4e6a6d93a57f3d9eebbc17d41a19abbab9fbec8d0d6321dfcbeaa1ad98e579ac6d90a830961ac41119f1a17908a3afc571627461cca27e1f5987db39d9a162ab44f907492562ba23f40da54d268ad671da5a9872743bc9e22b956bb3b72644c9275fd18ec1d388233986adab020301000102818042d28091cf5c05c8f180a8752950b0e16870e7f42eaa5250df6ef08b3a8b45167fea2c14ff066bfbca162ef8630afe8ec39d90955a829254e63500ce3dd46e7757b37493cabd9a41ae1598716783d78900937295267c5df12f2839716c01e2eb8025df91b12b9641909df78e98f2286a170beb1044c1210419031e605252a019024100dccb67acfa1d16ed0883473c8e2c1a72d9d55c3ca22262027ed3526fc881552ab6f1970739bfefcc4fd3a93f96ecf0515e77b0f2530789c8a16988718b9e8b07024100b67f3438163e18a5f5f87470d4b6ad53ebc7d652e04021a1c475b12195a20e42c6e2a0e30d7ee2dcc9ca4aa44996d978b5b8fb9b483a6110c17e8ce13237cb3d024100bbe2d769aa3900de5a4524649273535a72a60e6486700dc923d57e2c238a7347f2ffe8ce3c4569b10a290ab8531d736f3a9d4dd517f4ad3a2ea91873cf24ee170240081904dc2840811f7c1696341b7c2f9cb5ad9c58f9125f6baa27fec01abfa21a5c70d9eab94294166fedc5fa1c39d15485dbbb6e5c7a1aac7fa979ded4379d91024034f8f5e6d1bd3fdc042cb3e20ff64ce8fe30c23ae5fa163f63a5865063a5ab81eff82492129d9d79e7c226800ec14092910c835e6b7d711696b05e4ed8673ee6";

    static boolean isValidEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex < 0 || atIndex == 0 || atIndex == email.length() - 1) {
            return false;
        }
        int dotIndex = email.indexOf(".", atIndex);
        if (dotIndex < 0 || dotIndex == email.length() - 1) {
            return false;
        }
        return true;
    }

    static boolean isPublicEmail(String email) {
        int index = email.indexOf("@");
        if (index + 1 < email.length()) {
            String domain = email.substring(index + 1);
            if (PUBLIC_DOMAINS.isPrefixOf(domain, true)) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    static String encrypt(String clear) {
        String ret = null;

        try {
            PrivateKey privateKey = getPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            byte[] cipherText = cipher.doFinal(clear.getBytes(Charset
                    .forName("UTF-8")));
            ret = CipherUtil.toHex(cipherText);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }    

    static void unexpectedServerError() {
        String msg = "Unexpected Error";
        DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
        m.setTitle("Unexpected Error");
        DialogDisplayer.getDefault().notify(m);
    }

    /**
     * <ul>If it's a trial license, 
     *  <li>validate it </li>
     *  <li>then set the appropriate mode</li>
     * </ul>
     * <ul>If it's an activation code, 
     *  <li>validate it</li>
     *  <li>if invalid, display msg</li>
     *  <li>then set the appropriate mode</li>
     * </ul>
     * else, display error msg
     */
    static void processSecretCode(String secret) {
        final String TITLE = "Invalid License";
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        boolean isTrialLicense = TrialLicense.isTrialLicense(secret);
        boolean isActivation = ActivationCode.isActivationCode(secret);
        boolean isFreeAcademicLicense = FreeAcademicLicense.isFreeAcademicLicense(secret);
        if (isTrialLicense) {
            TrialLicense tl = TrialLicense.parse(secret);
            boolean expired = tl.isExpired();
            if (expired) {
                setBasicMode();
            } else {
                long time = tl.getExpiredTime() - Calendar.getInstance().getTimeInMillis();
                setTrialMode(time);
            }
        } else if (isActivation) {
            Date rDate = appService.getReleaseDate();
            ActivationCode ac = ActivationCode.parse(secret);
            boolean fingerprintGood = ac.getFingerprint().equals(DiskUtil.getComputerFingerprint());
            Date maxReleaseDate = new Date(ac.getProductMaxReleaseTime());
            boolean releaseDateGood = maxReleaseDate.getTime() >= rDate.getTime();
            if (fingerprintGood && releaseDateGood) {
                setNormalMode();
            } else {
                String msg = "";
                if (!fingerprintGood) {
                    msg = MSG.NOT_FOR_THIS_COMPUTER;
                } else if (!releaseDateGood) {
                    String maxDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(maxReleaseDate);
                    msg = String.format("The license cannot be used for VectorFriends %s, released after %s", appService.getCurrentVersion(), maxDate);
                }
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                setBasicMode();
            }
        } else if(isFreeAcademicLicense){
            FreeAcademicLicense freeAcademicLicense = FreeAcademicLicense.parse(secret);
            String fp = freeAcademicLicense.getFingerprint();
            String fpSelf = DiskUtil.getComputerFingerprint();
            if(!fp.equals(fpSelf)){
                String msg = "The computer id in the license doesnot match your computer id";
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(TITLE);
                DialogDisplayer.getDefault().notify(m);
                setBasicMode();
            }else{
                setNormalMode();
            }
        } else {
            DialogDescriptor.Message m = new DialogDescriptor.Message("Unrecognized registration code or trial license", DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
            setBasicMode();
        }
    }

    static void setNormalMode() {
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        JFrame frame = (JFrame) WindowManager.getDefault().getMainWindow();
        frame.setTitle(String.format(MSG.s_s, appService.getAppName(), appService.getCurrentVersion()));
        LicenseService ls = LicenseService.getInstance();
        ls.setBasicMode(false);
    }

    static void setTrialMode(long remainingTime) {
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        JFrame frame = (JFrame) WindowManager.getDefault().getMainWindow();
        frame.setTitle(String.format(MSG.s_s_Expired_in, appService.getAppName(), appService.getCurrentVersion(), MyDateUtil.toString(remainingTime)));
        LicenseService ls = LicenseService.getInstance();
        ls.setBasicMode(false);
    }

    static void setBasicMode() {
        IAppService appService = Lookup.getDefault().lookup(IAppService.class);
        LicenseService ls = LicenseService.getInstance();
        JFrame frame = (JFrame) WindowManager.getDefault().getMainWindow();
        frame.setTitle(String.format(MSG.s_s_basic_mode, appService.getAppName(), appService.getCurrentVersion()));
        ls.setBasicMode(true);
    }

    static String decrypt(String secret) {
        byte[] bytes = HexUtil.toBytes(secret);
        return decrypt(bytes);
    }

    static String decrypt(byte[] secret) {
        String ret = null;
        if(secret == null){
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
            byte[] clearBytes = cipher.doFinal(secret);
            ret = new String(clearBytes, Charset.forName("UTF-8"));
            System.out.println();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            System.out.println();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.out.println();
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.out.println();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
        } finally {
            return ret;
        }
    }

    private static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            IAppService appService = Lookup.getDefault().lookup(IAppService.class);
            if (appService.isProduction()) {
                privateKey = KeyFactory.getInstance(ALGO)
                        .generatePrivate(
                        new PKCS8EncodedKeySpec(HexUtil.toBytes(PRIVATE_KEY)));
            } else {
                privateKey = KeyFactory.getInstance(ALGO)
                        .generatePrivate(
                        new PKCS8EncodedKeySpec(HexUtil.toBytes(PRIVATE_KEY_LOCAL)));
            }
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return privateKey;
    }

    private static PublicKey getPublicKey() {
        PublicKey ret = null;
        try {
            IAppService appService = Lookup.getDefault().lookup(IAppService.class);
            byte[] publicKeyBytes;
            if(appService.isProduction()){
                publicKeyBytes = HexUtil.toBytes(PUBLIC_KEY);
            }else{
                publicKeyBytes = HexUtil.toBytes(PUBLIC_KEY_LOCAL);
            }
            KeyFactory keyFactory = KeyFactory.getInstance(ALGO);
            ret = keyFactory.generatePublic(
                    new X509EncodedKeySpec(publicKeyBytes));

        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }
}
