/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class CommonUtilTest {

    public CommonUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //@Test
    public void testCopyOf_Set() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("copyOf");
        Set<A> o = new HashSet<A>();
        o.add(new A(1));
        o.add(new A(8));
        o.add(new A(10));

        Set result = CommonUtil.copyOf(o);
        Assert.assertEquals(result.size(), o.size());
    }

    //@Test
    public void testCopyOf_Set_Integer() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("copyOf");
        Set<Integer> o = new HashSet<Integer>();
        o.add(54);
        o.add(7);
        o.add(12);

        Set result = CommonUtil.copyOf(o);
        Assert.assertEquals(result.size(), o.size());
    }

    //@Test
    public void testCopyOf_Set_String() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("copyOf");
        Set<String> o = new HashSet<String>();
        o.add("124");
        o.add("34");
        o.add("-12");

        Set result = CommonUtil.copyOf(o);
        Assert.assertEquals(result.size(), o.size());
    }

    @Test
    public void copy_parameter_object_object() {
        A t1 = new A();
        t1.hibernateId = 1;
        t1.a = 2;
        t1.s = "TEST";
        t1.b = new B();
        t1.b.hibernateId = 100;
        t1.b.value = 101;

        A t2 = CommonUtil.cloneSimple(t1);
        System.out.println(t2.hibernateId);
        System.out.println(t2.a);
        System.out.println(t2.s);

    }

    static class A implements Cloneable {

        Integer hibernateId;
        Integer a;
        String s;
        Integer p;
        B b;

        public A() {
        }

        private Integer getP() {
            return p;
        }

        private void setP(Integer p) {
            this.p = p;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public A(Integer a) {
            this.a = a;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }
    }

    static class B {

        Integer hibernateId;
        int value;
        int[] ints;

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
