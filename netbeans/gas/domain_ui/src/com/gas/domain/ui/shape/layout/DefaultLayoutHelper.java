/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.layout;

import com.gas.common.ui.util.MathUtil;
import com.gas.domain.ui.shape.IShape.TEXT_LOC;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.Arrow;
import com.gas.domain.ui.shape.IShape;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dunqiang
 */
@ServiceProvider(service = ILayoutHelper.class)
public class DefaultLayoutHelper extends BaseLayoutHelper implements ILayoutHelper {

    @Override
    public String getFetureType() {
        return "";
    }
    
    @Override
    public boolean isDefault(){
        return true;
    }

    @Override
    public ILayoutHelper newInstance() {
        return new DefaultLayoutHelper();
    }
}