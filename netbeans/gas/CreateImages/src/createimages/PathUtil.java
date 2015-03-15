/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author dq
 */
public class PathUtil {

    public static Point2D relativelyLineTo(GeneralPath generalPath, double x, double y){
        Point2D p2d = generalPath.getCurrentPoint();
        Point2D.Double newPt = new Point2D.Double(p2d.getX() + x, p2d.getY() + y);
        generalPath.lineTo(newPt.getX(), newPt.getY());
        return generalPath.getCurrentPoint();
    }
    
    public static boolean isEmpty(GeneralPath path) {
        boolean ret = true;
        if (path != null) {
            ret = path.getCurrentPoint() == null;
        }
        return ret;
    }    
    
    public static GeneralPath transform(AffineTransform at, GeneralPath p) {
        GeneralPath ret = new GeneralPath();
        PathIterator itr = p.getPathIterator(at);
        while (!itr.isDone()) {
            double[] coords = new double[6];
            int type = itr.currentSegment(coords);
            if (type == PathIterator.SEG_CLOSE) {
                ret.closePath();
            } else if (type == PathIterator.SEG_CUBICTO) {
                ret.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
            } else if (type == PathIterator.SEG_LINETO) {
                ret.lineTo(coords[0], coords[1]);
            } else if (type == PathIterator.SEG_MOVETO) {
                ret.moveTo(coords[0], coords[1]);
            } else if (type == PathIterator.SEG_QUADTO) {
                ret.quadTo(coords[0], coords[1], coords[2], coords[3]);
            }
            itr.next();
        }
        return ret;
    }    
    
    /**
     * won't change the shape but reverse the order of the points
     */
    public static GeneralPath reverse(final GeneralPath generalPath) {
        GeneralPath ret = new GeneralPath();
        LinkedHashMap<Integer, double[]> a = new LinkedHashMap<Integer, double[]>();
        List<Integer> typeList = new ArrayList<Integer>();
        List<double[]> coordsList = new ArrayList<double[]>();
        PathIterator itr = generalPath.getPathIterator(null);
        while (!itr.isDone()) {
            double[] coords = new double[6];
            int type = itr.currentSegment(coords);
            typeList.add(type);
            coordsList.add(coords);
            itr.next();
        }

        for (int i = typeList.size() - 1; i > 0; i--) {
            int curType = typeList.get(i);
            double[] curCoords = coordsList.get(i);
            double[] prevCoords = null;
            Integer prevType = null;
            if (i - 1 > -1) {
                prevCoords = coordsList.get(i - 1);
                prevType = typeList.get(i - 1);
            } else {
                break;
            }
            boolean empty = isEmpty(ret);
            if (empty) {
                Point2D lastPoint = generalPath.getCurrentPoint();
                ret.moveTo(lastPoint.getX(), lastPoint.getY());
            }
            Double prevX = null;
            Double prevY = null;
            if (prevType.intValue() == PathIterator.SEG_LINETO
                    || prevType.intValue() == PathIterator.SEG_MOVETO) {
                prevX = prevCoords[0];
                prevY = prevCoords[1];
            } else if (prevType.intValue() == PathIterator.SEG_CUBICTO) {
                prevX = prevCoords[4];
                prevY = prevCoords[5];
            } else if (prevType.intValue() == PathIterator.SEG_QUADTO) {
                prevX = prevCoords[2];
                prevY = prevCoords[3];
            } else if (prevType.intValue() == PathIterator.SEG_CLOSE) {
                Point2D firstPt = getFirstPoint(generalPath);
                prevX = firstPt.getX();
                prevY = firstPt.getY();
            }
            if (curType == PathIterator.SEG_CLOSE) {
                Point2D firstPt = getFirstPoint(generalPath);
                ret.lineTo(firstPt.getX(), firstPt.getY());
            } else if (curType == PathIterator.SEG_CUBICTO) {
                ret.curveTo(curCoords[2], curCoords[3], curCoords[0], curCoords[1], prevX, prevY);
            } else if (curType == PathIterator.SEG_LINETO) {
                ret.lineTo(prevX, prevY);
            } else if (curType == PathIterator.SEG_MOVETO) {
                ret.moveTo(prevX, prevY);
            } else if (curType == PathIterator.SEG_QUADTO) {
                ret.quadTo(curCoords[0], curCoords[1], prevX, prevY);
            }
        }

        return ret;
    }

    public static Point2D getFirstPoint(GeneralPath generalPath) {
        PathIterator itr = generalPath.getPathIterator(null);
        Point2D ret = null;
        while (!itr.isDone()) {
            double[] tmp = new double[6];
            itr.currentSegment(tmp);
            ret = new Point2D.Double(tmp[0], tmp[1]);
            break;
        }
        return ret;
    }
    
    /**
     * A
     * |
     * |
     * |
     * V
     */
    public static GeneralPath flipVertically(GeneralPath path){
        GeneralPath ret = new GeneralPath();
        Rectangle2D bounds = path.getBounds2D();
        final double yFactor = 2 * bounds.getY() + bounds.getHeight();
        PathIterator itr = path.getPathIterator(null);
        while (!itr.isDone()) {
            double[] coords = new double[6];
            int type = itr.currentSegment(coords);
            if (type == PathIterator.SEG_CLOSE) {
                ret.closePath();
            } else if (type == PathIterator.SEG_CUBICTO) {
                ret.curveTo(coords[0], yFactor - coords[1], coords[2], yFactor - coords[3], coords[4], yFactor - coords[5]);
            } else if (type == PathIterator.SEG_LINETO) {
                ret.lineTo(coords[0], yFactor - coords[1]);
            } else if (type == PathIterator.SEG_MOVETO) {
                ret.moveTo(coords[0], yFactor - coords[1]);
            } else if (type == PathIterator.SEG_QUADTO) {
                ret.quadTo(coords[0], yFactor - coords[1], coords[2], yFactor - coords[3]);
            }
            itr.next();
        }
        return ret;
    }
    
    public static GeneralPath translate(double deltaX, double deltaY, final GeneralPath path){
        GeneralPath ret = new GeneralPath();
        PathIterator itr = path.getPathIterator(null);
        while (!itr.isDone()) {
            double[] coords = new double[6];
            int type = itr.currentSegment(coords);
            if (type == PathIterator.SEG_CLOSE) {
                ret.closePath();
            } else if (type == PathIterator.SEG_CUBICTO) {
                ret.curveTo(deltaX + coords[0], deltaY + coords[1], deltaX + coords[2], deltaY + coords[3], deltaX + coords[4], deltaY + coords[5]);
            } else if (type == PathIterator.SEG_LINETO) {
                ret.lineTo(deltaX + coords[0], deltaY + coords[1]);
            } else if (type == PathIterator.SEG_MOVETO) {
                ret.moveTo(deltaX + coords[0], deltaY + coords[1]);
            } else if (type == PathIterator.SEG_QUADTO) {
                ret.quadTo(deltaX + coords[0], deltaY + coords[1], deltaX + coords[2], deltaY + coords[3]);
            }
            itr.next();
        }
        return ret;
    }
    
    public static int getMaxX(GeneralPath path){
        Rectangle rect = path.getBounds();
        return rect.x + rect.width;
    }
    
    public static int getMinX(GeneralPath path){
        Rectangle rect = path.getBounds();
        return rect.x;    
    }
    
    /**
     * <--->
     */
    public static GeneralPath flipHorizontally(final GeneralPath path) {
        GeneralPath ret = new GeneralPath();
        Rectangle2D bounds = path.getBounds2D();
        double xFactor = 2 * bounds.getX() + bounds.getWidth();
        PathIterator itr = path.getPathIterator(null);
        while (!itr.isDone()) {
            double[] coords = new double[6];
            int type = itr.currentSegment(coords);
            if (type == PathIterator.SEG_CLOSE) {
                ret.closePath();
            } else if (type == PathIterator.SEG_CUBICTO) {
                ret.curveTo(xFactor - coords[0], coords[1], xFactor - coords[2], coords[3], xFactor - coords[4], coords[5]);
            } else if (type == PathIterator.SEG_LINETO) {
                ret.lineTo(xFactor - coords[0], coords[1]);
            } else if (type == PathIterator.SEG_MOVETO) {
                ret.moveTo(xFactor - coords[0], coords[1]);
            } else if (type == PathIterator.SEG_QUADTO) {
                ret.quadTo(xFactor - coords[0], coords[1], xFactor - coords[2], coords[3]);
            }
            itr.next();
        }
        return ret;
    }    
}
