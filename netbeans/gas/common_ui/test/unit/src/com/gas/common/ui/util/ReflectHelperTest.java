/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class ReflectHelperTest {

    public ReflectHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of setQuietly method, of class ReflectHelper.
     */
    @Test
    public void testSetQuietly_3args_2() {
        System.out.println("setQuietly");
        Field field = null;
        A a = new A();
        Class aClass = a.getClass();
        List<Field> fields = ReflectHelper.getDeclaredFields(aClass);
        B b = new B();
        int value = 0;
        System.out.println(a.getB());
        ReflectHelper.setQuietly(fields.get(0), a, b);
        System.out.println(a.getB());
    }

    static class A {

        B b;

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }
    }

    static class B {

        int value = 2;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
