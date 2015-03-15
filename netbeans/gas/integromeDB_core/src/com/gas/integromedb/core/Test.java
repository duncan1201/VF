/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.integromedb.core;

/**
 *
 * @author dunqiang
 */
import com.gas.integromedb.core.api.ITest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 * Test class to find objects by keyword and organism and then
 * list top synonyms for each of them
 * @author Sergey Kozhenkov
 */
@ServiceProvider(service = ITest.class)
public class Test implements ITest{

    public void test() {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://integromedb.org/integrome"));
            config.setEnabledForExtensions(true);
            XmlRpcClient client = new XmlRpcClient();
            // use Commons HttpClient as transport
            //client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
            client.setTransportFactory(new XmlRpcSunHttpTransportFactory(client));
            // set configuration
            client.setConfig(config);

            Object[] params = new Object[]{"p53", "Homo sapiens"};
            Object[] result = (Object[]) client.execute("Integrome.find", params);
            if (result != null) {
                for (Object rec : result) {
                    HashMap<String, String> map = (HashMap<String, String>) rec;
                    long id = Long.parseLong(map.get("id"));
                    System.out.println("Object: " + map.get("name") + " (" + id + "), type: " + map.get("type") + ", organism: " + map.get("organism"));

                    params = new Object[]{"" + id};
                    Object[] synonyms = (Object[]) client.execute("Integrome.get_top_synonyms", params);
                    if (synonyms != null) {
                        System.out.println(Arrays.toString(synonyms));
                    }
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlRpcException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}