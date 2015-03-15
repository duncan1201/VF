/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.misc.Loc;
import org.junit.*;

/**
 *
 * @author dq
 */
public class LocUtilTest {

    public LocUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIntersect() {
        Object[][] testData = {
            // 1st non cross-border, 2nd non cross-border
            {1, 100, 101, 200, null},// no intersect
            {1, 100, 1, 50, new Loc(1, 50)}, // the second one is a subset of the first
            {1, 100, 90, 100, new Loc(90, 100)}, // the second one is a subset of the first
            {50, 100, 2, 200, new Loc(50, 100)}, // the second one is a superset of the first
            {50, 100, 70, 200, new Loc(70, 100)}, // intersect
            {50, 100, 20, 80, new Loc(50, 80)}, // intersect

            // 1st non cross-border, 2nd cross-border
            {50, 101, 40, 30, new Loc(50, 101)}, // intersect

            // cross-border
            {900, 100, 101, 200, null},// no intersect
            {900, 100, 90, 200, new Loc(90, 100)},// intersect start portion
            {900, 100, 1000, 1100, new Loc(1000, 1100)},// intersect end portion
            {900, 100, 1000, 90, new Loc(1000, 90)}, // the second one is a subset of the first one
        };
        for (Object[] data : testData) {
            Integer start = (Integer) data[0];
            Integer end = (Integer) data[1];
            if (end.intValue() == 101) {
                System.out.print("");
            }
            Integer start2 = (Integer) data[2];
            Integer end2 = (Integer) data[3];

            Loc expected = (Loc) data[4];
            LocList loc = LocUtil.intersect(start, end, start2, end2);
            if (expected == null) {
                Assert.assertTrue(loc.isEmpty());
            } else {
                Assert.assertEquals(loc.get(0).getStart(), expected.getStart());
                Assert.assertEquals(loc.get(0).getEnd(), expected.getEnd());
            }
        }
    }

    @Test
    public void testIsSupersetOf() {

        Object[][] testData = {
            {1, 100, 5, 50, null, true},// happy cases, no cross border cases
            {6, 100, 5, 50, null, false},// p1 and p2 has an overlap. no cross border cases
            {100, 90, 5, 50, null, true},// pt 1 cross border, pt 2 linear subset of p1
            {100, 90, 100, 150, null, true},// pt 1 cross border, pt 2 linear subset of p1
            {100, 90, 100, 80, null, true},// pt 1 cross border,  Pt 2 cross border, subset of p1
            {100, 90, 101, 180, null, true},// pt 1 cross border, pt 2 linear, no overlaps
            {100, 90, 99, 10, null, false},// both cross border, pt2 overlaps, but not subset of p1 
            {100, 10, 1, 11, null, false},// Pt1 cross border, pt2  linear, overlaps, but not subset of p1
            {1, 120, 90, 10, 120, true},// Pt1 full length, Pt2 cross border, subset of p1
        };

        for (Object[] data : testData) {
            Integer totalPos = (Integer) data[4];
            Boolean expected = (Boolean) data[5];

            Boolean result;
            if (totalPos == null) {
                result = LocUtil.isSupersetOf((Integer) data[0], (Integer) data[1], (Integer) data[2], (Integer) data[3]);
            } else {
                result = LocUtil.isSupersetOf((Integer) data[0], (Integer) data[1], (Integer) data[2], (Integer) data[3], false);
            }

            junit.framework.Assert.assertEquals(expected.booleanValue(), result.booleanValue());
        }
    }
}
