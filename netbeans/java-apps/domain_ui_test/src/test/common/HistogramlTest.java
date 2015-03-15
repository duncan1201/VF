/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.common;

import com.gas.common.ui.button.CumboBox;
import com.gas.common.ui.histogram.*;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.core.tigr.util.TigrProjectIO;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class HistogramlTest extends JFrame {

    Histogram kPanel;
    public HistogramlTest() {
        // Everything is as usual here        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        String txt = "";
        for (int i = 0; i < 25; i++) {
            txt += i % 10;
        }

        panel.setLayout(new BorderLayout());
        Condig condig = getCondig(1000, 2000);

        kPanel = new Histogram();
        kPanel.setData(condig.getQualities());
        
        panel.add(kPanel, BorderLayout.CENTER);
        panel.add(getControlPanel(), BorderLayout.SOUTH);

        panel.setSize(400, 400);
        this.setContentPane(panel);
        this.pack();
        
        this.setBounds(250, 250, this.getWidth(),
        this.getHeight());                 
    }
    
    private JPanel getControlPanel(){
        JPanel ret = new JPanel();
        CumboBox btn = null ;
        //btn = new JButton("Zoom in");     
        List<Object> options = new ArrayList<Object>();
        options.add("test1");
        options.add("test2");
        btn = new CumboBox(options);
        btn.setEnabled(true); 
        
        ret.add(btn);
                            
        return ret;
    }
    
    private Condig getCondig(int min, int max){
        Condig ret = null;
        TigrProject p = TigrProjectIO.read(new File("D:\\tmp\\tigr\\tigr_project.ser"));
        Iterator<Condig> itr = p.getUnmodifiableCondigs().iterator();
        while(itr.hasNext()){
            ret = itr.next();
            int length = ret.getLsequence().length();
            if(length > min && length < max){
                break;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        HistogramlTest test = new HistogramlTest();
        test.setVisible(true);
    }
}
