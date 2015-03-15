/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import com.gas.domain.core.nexus.api.Nexus;
import com.gas.domain.core.nexus.data.NexusData;
import org.junit.*;

/**
 *
 * @author dq
 */
public class NexusIOServiceTest {

    public NexusIOServiceTest() {
    }

    /**
     * Test of parse method, of class NexusIOService.
     */
    //@Test
    public void testParse_data_blk_only() {
        System.out.println("testParse_data_blk_only");
        Class clazz = NexusData.class;
        String name = "data_blk_only.nex";
        NexusIOService instance = new NexusIOService();
        Nexus result = instance.parse(clazz, name);
        System.out.print("");
    }

    @Test
    public void testParse_tree_tax_character_blk_only() {
        System.out.println("testParse_tree_blk_only");
        Class clazz = NexusData.class;
        String name = "tree_tax_character.nex";
        NexusIOService instance = new NexusIOService();
        Nexus result = instance.parse(clazz, name);
        System.out.print(result.toString());
    }

    //@Test
    public void testParse_mrbayes_tree_tax_only() {
        System.out.println("testParse_mrbayes_tree_tax_only");
        Class clazz = NexusData.class;
        String name = "mrbayes_tree_tax.nex";
        NexusIOService instance = new NexusIOService();
        Nexus result = instance.parse(clazz, name);
        System.out.print("");
    }
}
