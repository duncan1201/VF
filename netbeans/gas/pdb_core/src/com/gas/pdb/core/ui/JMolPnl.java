/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;
import org.jmol.api.JmolStatusListener;
import org.jmol.i18n.GT;
import org.jmol.popup.JmolPopup;
import org.jmol.viewer.Viewer;

/**
 *
 * @author dq
 */
public class JMolPnl extends JPanel {

    private Viewer viewer;
    JmolPopup jmolpopup;

    public JMolPnl() {
        setLayout(new BorderLayout());
        JmolPanel jmolPanel = new JmolPanel();
        add(jmolPanel, BorderLayout.CENTER);
        viewer = jmolPanel.getViewer();   
        //viewer.setBooleanProperty("disablePopupMenu", false);
        boolean s = viewer.getBooleanProperty("disablePopupMenu");
        viewer.evalString(String.format(JMolScripts.BACKGROUND, 0,0,0));
    }

    public void renderScreenImage(Graphics grphcs, Dimension dmnsn, Rectangle rctngl) {
        viewer.renderScreenImage(grphcs, dmnsn, rctngl);
    }

    public String evalFile(String string) {
        return viewer.evalFile(string);
    }

    public String evalString(String string) {
        return viewer.evalString(string);
    }

    public void openStringInline(String string) {
        viewer.openStringInline(string);
    }

    public void openDOM(Object o) {
        viewer.openDOM(o);
    }

    public void openFile(String string) {
        viewer.openFile(string);
    }

    public String getOpenFileError() {
        return viewer.getOpenFileError();
    }

    private class JmolPanel extends JPanel {

        Viewer viewer;
        JmolAdapter adapter;

        public JmolPanel() {
            adapter = new SmarterJmolAdapter(null);
            viewer = (Viewer)JmolSimpleViewer.allocateSimpleViewer(this, adapter);
            viewer.setJmolStatusListener(new MyJmolStatusListener());
        }

        public Viewer getViewer() {
            return viewer;
        }
        final Dimension currentSize = new Dimension();
        final Rectangle rectClip = new Rectangle();

        @Override
        public void paint(Graphics g) {
            getSize(currentSize);
            g.getClipBounds(rectClip);
            viewer.renderScreenImage(g, currentSize, rectClip);
        }
    }
    
private class MyJmolStatusListener implements JmolStatusListener {
    public void notifyFileLoaded(String fullPathName, String fileName,
                                 String modelName, Object clientFile,
                                 String errorMsg) {
      if (errorMsg != null) {
        JOptionPane.showMessageDialog(null,
          fullPathName + '\n' + errorMsg,
          GT._("File not loaded"),
          JOptionPane.ERROR_MESSAGE);
        return;
      }
      jmolpopup = JmolPopup.newJmolPopup(viewer);
      jmolpopup.updateComputedMenus();
      if (fullPathName == null) {
        // a 'clear/zap' operation
        return;
      }
      String title = "Jmol";
      if (modelName != null && fileName != null)
	  title = fileName + " - " + modelName;
      else if (fileName != null)
	  title = fileName;
      else if (modelName != null)
	  title = modelName;
      
    }

    public void notifyFrameChanged(int frameNo) {
        // don't do anything
    }

    public void setStatusMessage(String statusMessage) {
      System.out.println("setStatusMessage:" + statusMessage);
    }

    public void scriptEcho(String strEcho) {
    }

    public void scriptStatus(String strStatus) {
    }

    public void notifyScriptTermination(String strStatus, int msWalltime) {
    }

    public void handlePopupMenu(int x, int y) {
      jmolpopup.show(x, y);
    }

    public void notifyMeasurementsChanged() {
      //measurementTable.updateTables();
    }

    public void notifyAtomPicked(int atomIndex, String strInfo) {
    }

    public void showUrl(String url) {
    }

    public void showConsole(boolean showConsole) {
    }
  }    
    
}
