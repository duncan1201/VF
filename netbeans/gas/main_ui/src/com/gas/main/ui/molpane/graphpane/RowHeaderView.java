/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.common.ui.light.VerticalRulerComp;
import com.gas.common.ui.linePlot.LinePlotComp;
import com.gas.common.ui.linePlot.LinePlotCompMap;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author dunqiang
 */
public class RowHeaderView extends JComponent {

    private WeakHashMap<String, Header> headerMap = new WeakHashMap<String, Header>();
    private Map<String, VerticalRulerComp> verticalRulers = new HashMap<String, VerticalRulerComp>();

    RowHeaderView() {
        super();
        setLayout(new Layout());
    }

    void reset() {
        headerMap.clear();
        verticalRulers.clear();
        removeAll();
    }

    Header getHeader(String name) {
        Header header = headerMap.get(name);
        if (header == null) {
            header = new Header(name);
            headerMap.put(name, header);
        }
        return header;
    }
    
    VerticalRulerComp getVerticalRuler(String name){
        VerticalRulerComp ruler = verticalRulers.get(name);
        if(ruler == null){
            if(name.equals(LinePlotCompMap.GC)){
                ruler = VerticalRulerComp.createPredefined(LinePlotCompMap.GC);
            }else{
            
            }
            if(ruler != null){
                add(ruler);
                verticalRulers.put(name, ruler);
            }
        }
        return ruler;
    }

    void setPreferredWidth(int width) {
        Dimension size = getPreferredSize();
        size.width = width;
        setPreferredSize(size);
    }

    void setPreferredHeight(int height) {
        Dimension size = getPreferredSize();
        size.height = height;
        setPreferredSize(size);
    }

    static class Header extends JPanel implements PropertyChangeListener {

        private String text;
        private String displayText;
        private JLabel label;

        Header(String text) {
            super();
            this.text = text;
            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);

            label = new JLabel();
            layout.setConstraints(label, new GridBagConstraints(0, 0,//int gridx, int gridy,
                    1, 1, //int gridwidth, int gridheight,
                    0, 0,//double weightx, double weighty,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,//int anchor, int fill,
                    new Insets(0, 0, 0, 0),
                    0, 0//int ipadx, int ipady)
                    ));
            add(label);
            addPropertyChangeListener(this);
        }
        
        @Override
        public void setForeground(Color c){
            super.setForeground(c);
            if(this.label != null){
                this.label.setForeground(c);
            }
        }

        Font getLabelFont() {
            return label.getFont();
        }

        String getText() {
            return text;
        }

        void setText(String text) {
            this.text = text;
        }

        final void setDisplayText(String displayText) {
            String old = this.displayText;
            this.displayText = displayText;
            firePropertyChange("displayText", old, displayText);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getSize().width, getSize().height);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (propertyName.equals("displayText")) {
                String txt = (String) evt.getNewValue();
                label.setText(txt);
                label.setToolTipText(txt);
            }
        }
    }

    private class Layout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension ret = new Dimension();
            return ret;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            final Dimension size = parent.getSize();
            GraphPane graphScrollPane = UIUtil.getParent(RowHeaderView.this, GraphPane.class);
            Map<String, Boolean> showHideMap = graphScrollPane.getTrackVisibilityMap();
            Map<String, JComponent> trackMap = graphScrollPane.getGraphPanel().getTrackMap();
            LinePlotCompMap linePlotCompMap = graphScrollPane.getGraphPanel().getLinePlotCompMap();
            Iterator<String> keyItr = trackMap.keySet().iterator();

            int height = 0;

            while (keyItr.hasNext()) {
                String key = keyItr.next().toUpperCase(Locale.ENGLISH);
                Boolean visible = showHideMap.get(key);
               
                JComponent track = trackMap.get(key);
                Rectangle rect = track.getBounds();
                rect.width = size.width;

                Header header = getHeader(key);
                if(key.startsWith("FRAME")){
                    header.setForeground(Color.GRAY);
                }else{
                    header.setForeground(Color.BLACK);
                }
                if(!SwingUtilities.isDescendingFrom(header, parent)){
                    add(header);
                }
                rect.x = 0;
                header.setBounds(rect);

                Font labelFont = header.getLabelFont();
                int fontHeight = FontUtil.getFontMetrics(labelFont).getHeight();

                if (fontHeight > rect.height) {
                    header.setDisplayText("");
                } else {
                    header.setDisplayText(key);
                }

                header.setBackground(track.getBackground());


                if (visible != null && visible) {

                    if (height < rect.y + rect.height) {
                        height += rect.height;
                    }
                }
            }
            
            keyItr = linePlotCompMap.keySet().iterator();
            while(keyItr.hasNext()){
                String key = keyItr.next();
                LinePlotComp linePlotComp = linePlotCompMap.get(key);
                VerticalRulerComp verticalRuler = getVerticalRuler(key);
                Rectangle bounds = linePlotComp.getBounds();
                bounds.width = size.width;
                bounds.x = 0;
                verticalRuler.setBounds(bounds);
                verticalRuler.setBackground(linePlotComp.getBackground());
                verticalRuler.repaint();
                height += bounds.height;
            }

            removeHeaders(parent, trackMap.keySet());
            setPreferredHeight(height);
            setPreferredWidth(getSize().width);
        }
        
        private void removeHeaders(Container parent, Set<String> trackNames){
            Iterator<String> itr = headerMap.keySet().iterator();
            while(itr.hasNext()){
                String text = itr.next();
                if(!trackNames.contains(text)){
                    Header header = headerMap.get(text);
                    parent.remove(header);
                    itr.remove();
                }
            }
        }
    }
}
