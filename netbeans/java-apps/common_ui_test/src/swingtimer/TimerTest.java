/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingtimer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Timer;

/**
 *
 * @author dunqiang
 */
public class TimerTest {
    public static void main(String[] args){
        Timer t = new Timer(1000, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(new Date());
            }
        });
        t.start();
    }
}
