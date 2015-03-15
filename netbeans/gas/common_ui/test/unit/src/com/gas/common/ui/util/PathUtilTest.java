/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.awt.geom.GeneralPath;
import java.io.FileNotFoundException;
import javax.print.PrintException;

/**
 *
 * @author dq
 */
public class PathUtilTest {
    //@Test

    public void testReverse() throws FileNotFoundException, PrintException {
        GeneralPath path = new GeneralPath();
        path.moveTo(4, 0);
        path.lineTo(4, 1);
        path.lineTo(4, 2);
        path.curveTo(4, 2, 4, 16, 5, 9);

        String str = UIUtil.toString(path);
        System.out.println(str);
        GeneralPath reversed = PathUtil.reverse(path);
        str = UIUtil.toString(reversed);
        System.out.println(str);
    }

    //@Test
    public void testMirror() {
        GeneralPath path = new GeneralPath();
        path.moveTo(0, 1);
        path.lineTo(2, 3);
        System.out.println(UIUtil.toString(path));
        GeneralPath mirrored = PathUtil.flipHorizontally(path);
        System.out.println(UIUtil.toString(mirrored));
    }
}
