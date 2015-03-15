
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;

public class ArrowScrolling {

    JLabel label;

    private JScrollPane getContent(BufferedImage image) {
        label = new ScrollingLabel(new ImageIcon(image), JLabel.CENTER);
        label.setFocusable(true);
        return new JScrollPane(label);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\tmp\\download.jpg");
        BufferedImage image = javax.imageio.ImageIO.read(file);
        ArrowScrolling test = new ArrowScrolling();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test.getContent(image));
        f.setSize(400, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
        test.label.requestFocusInWindow();
    }
}

class ScrollingLabel extends JLabel implements Scrollable {

    public ScrollingLabel(ImageIcon icon, int horizontalAlignment) {
        super(icon, horizontalAlignment);
        registerKeys();
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        Dimension d = ((JViewport) getParent()).getExtentSize();
        return (orientation == SwingConstants.HORIZONTAL)
                ? d.width
                : d.height;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        return 50;
    }

    private void registerKeys() {
        int c = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(c);
        ActionMap actionMap = getActionMap();
        int ctrl = InputEvent.CTRL_DOWN_MASK;
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_UP, ctrl, false);
        inputMap.put(ks, "UP");
        actionMap.put("UP", upAction);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ctrl, false);
        inputMap.put(ks, "DOWN");
        actionMap.put("DOWN", downAction);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ctrl, false);
        inputMap.put(ks, "LEFT");
        actionMap.put("LEFT", leftAction);
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ctrl, false);
        inputMap.put(ks, "RIGHT");
        actionMap.put("RIGHT", rightAction);
    }
    private Action upAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            JViewport viewport = (JViewport) getParent();
            Rectangle r = viewport.getViewRect();
            int inc = getScrollableBlockIncrement(r,
                    SwingConstants.VERTICAL, -1);
            Point p = r.getLocation();
            p.y = (p.y - inc >= 0) ? p.y - inc : 0;
            viewport.setViewPosition(p);
        }
    };
    private Action downAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            JViewport viewport = (JViewport) getParent();
            Rectangle r = viewport.getViewRect();
            int inc = getScrollableBlockIncrement(r,
                    SwingConstants.VERTICAL, 1);
            Point p = r.getLocation();
            int maxY = viewport.getView().getHeight() - r.height;
            p.y = (p.y + inc <= maxY) ? p.y + inc : maxY;
            viewport.setViewPosition(p);
        }
    };
    private Action leftAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            JViewport viewport = (JViewport) getParent();
            Rectangle r = viewport.getViewRect();
            int inc = getScrollableBlockIncrement(r,
                    SwingConstants.HORIZONTAL, -1);
            Point p = r.getLocation();
            p.x = (p.x - inc >= 0) ? p.x - inc : 0;
            viewport.setViewPosition(p);
        }
    };
    private Action rightAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            JViewport viewport = (JViewport) getParent();
            Rectangle r = viewport.getViewRect();
            int inc = getScrollableBlockIncrement(r,
                    SwingConstants.HORIZONTAL, 1);
            Point p = r.getLocation();
            int maxX = viewport.getView().getWidth() - r.width;
            p.x = (p.x + inc <= maxX) ? p.x + inc : maxX;
            viewport.setViewPosition(p);
        }
    };
}