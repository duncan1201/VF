package com.gas.main.ui;



import com.gas.domain.ui.banner.BannerTC;
import junit.framework.Test;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.MainWindowOperator;
import org.netbeans.jellytools.TopComponentOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JSliderOperator;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbModuleSuite.Configuration;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dq
 */
public class OverallTest extends JellyTestCase {
    /** Constructor required by JUnit */
    public OverallTest(String name) {
        super(name);
    }

    /** Creates suite from particular test cases. You can define order of testcases here. */
    public static Test suite() {
        Configuration testConfig = NbModuleSuite.createConfiguration(OverallTest.class);
        testConfig = testConfig.addTest("testClear");
        testConfig = testConfig.clusters(".*").enableModules(".*");
        return NbModuleSuite.create(testConfig);
    }

    /** Called before every test case. */
    @Override
    public void setUp() {        
        System.out.println("########  "+getName()+"  #######");
        System.out.println("###############");
    }

    // Add test methods here, they have to start with 'test' name:

    /** Test clear button. */
    public void testClear() {
        MainWindowOperator mainWindow = MainWindowOperator.getDefault();
        String statusText = mainWindow.getStatusText();
        System.out.println();
        
        TopComponentOperator bannerOperator = new TopComponentOperator(BannerTC.ID);
        
        System.out.println();
    }

}
