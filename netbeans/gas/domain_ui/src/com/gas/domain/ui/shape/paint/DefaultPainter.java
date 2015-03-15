/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.shape.paint;

import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.painter.LinearGradientPainterFactory;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.PathCreator;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;
import javax.swing.JComponent;
import org.biojavax.bio.seq.CompoundRichLocation;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IPainter.class)
public class DefaultPainter extends BasePainter implements IPainter {
    
    @Override
    public String getFetureType(){
        return "default";
    }
    
    @Override
    public boolean isDefault(){
        return true;
    }  
}
