/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class StrUtilTest {

    public StrUtilTest() {
    }

    //@Test
    public void testToString() {
        InputStream inputStream = StrUtilTest.class.getResourceAsStream("tree.nex");
        String str = StrUtil.toString(inputStream);
        System.out.println(str);
    }        

    //@Test
    public void testPrivateKey() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidKeyException {
//

        String text = "This is a test!";
        byte[] plainText = text.getBytes("UTF8");
//
// get a DES private key
        System.out.println("\nStart generating DES key");
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        Key key = keyGen.generateKey();
        System.out.println("Finish generating DES key");
//
// get a DES cipher object and print the provider
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        System.out.println("\n" + cipher.getProvider().getInfo());
//
// encrypt using the key and the plaintext
        System.out.println("\nStart encryption");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherBytes = cipher.doFinal(plainText);

        System.out.println("Finish encryption: ");
        System.out.println(CipherUtil.toHex(cipherBytes));
//
// decrypt the ciphertext using the same key
        System.out.println("\nStart decryption");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] newPlainText = cipher.doFinal(cipherBytes);
        System.out.println("Finish decryption: ");
        System.out.println(new String(newPlainText, "UTF8"));
    }

    //@Test
    public void testOK() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        System.out.println("testOK");
        Provider[] providers = Security.getProviders() ;
        for(Provider p: providers){
            System.out.println(p.getName());           
            System.out.println(p.getInfo());
        }
        String plainText = "plain Texts";
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair keyPair = keyGen.genKeyPair();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        byte[] cipherText = cipher.doFinal(plainText.getBytes(Charset.forName("UTF-8")));
        System.out.println("Cipher Text:");
        System.out.println(new String(cipherText, Charset.forName("UTF-8")));
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        byte[] newPlainText = cipher.doFinal(cipherText);
        System.out.println("Clear Text:");
        System.out.println(new String(newPlainText, Charset.forName("UTF-8")));
        System.out.println("testOK");
    }

    //@Test
    public void testSplits() {
        String s = "0123";
        int perLine = 9;
        StrUtil instance = new StrUtil();
        List expResult = null;
        List<String> result = instance.toList(s, perLine);
        System.out.print("");
    }

    //@Test
    public void testReplaceAll() {
        String a = "abc+";
        System.out.println(a);
    }

    //@Test
    public void testExtract() {
        String line = "25|qq";
        String regex = "25\\|(.+)";
        boolean match = line.matches(regex);
        System.out.println(match);

        line = "ABCDEa123(1)";
        regex = "(.+)\\([0-9]+\\)";
        String extracted = StrUtil.extract(regex, line);
        System.out.println(extracted);

        line = "considered 16046, GC content failed 3734, low tm 7140, high tm 1595, high hairpin stability 1, long poly-x seq 183, ok 3393";
        regex = "considered [0-9]+";
        extracted = StrUtil.extract(regex, line);
        System.out.println(extracted);

        line = "AAAQQabac2a";
        extracted = line.replaceAll("[a-zA-Z]", "");
        System.out.println(extracted);
    }
    
    //@Test
    public void test_indexOf(){
        String data = "[rgb=-1213213]";
        int index = data.indexOf('(');
        System.out.println(index);
    }
    
    @Test
    public void test_match(){
        String reg = "##(.+)-STARt##";
        String seq = "1111##ABC-START##22222##ABC-END##33333";        
        //System.out.println(seq.matches(reg));
        
        String extracted = StrUtil.extract(reg, seq);
        
        int splits[] = StrUtil.indexOfReg(reg, seq);
        
        System.out.println(extracted);
        String edition = "1.2";
        List<String> splits2 = StrUtil.tokenize(edition, ".");
        for(String s: splits2){
            System.out.println(s);
        }
        String osArch = System.getProperty("os.arch");
        //System.out.println("osArch="+osArch);
    }
}
