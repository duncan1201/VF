/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.exceptionhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Template;

/**
 *
 * @author dq
 */
public class NewFunctionExceptionHandler extends Handler implements Callable<JButton> {

    private JButton reviewIssueButton;
    private NewFunctionActionListener newFunctionActionListener = new NewFunctionActionListener();
    
    @Override
    public void publish(LogRecord record) {
        if(record.getThrown() != null){
            newFunctionActionListener.setLogRecord(record);
        }
    }

    @Override
    public void flush() {        
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public JButton call() throws Exception {
        if(reviewIssueButton == null){
            reviewIssueButton = new JButton("Review and Submit Issue");
            reviewIssueButton.addActionListener(newFunctionActionListener);
        }
        return reviewIssueButton;
    }
    
    private class NewFunctionActionListener implements ActionListener {

        private LogRecord logRecord;
        private IEmailService emailSvc = Lookup.getDefault().lookup(IEmailService.class);

        public NewFunctionActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Close our Uncaught Exception Dialog first.
            SwingUtilities.windowForComponent(reviewIssueButton).setVisible(false);
            Throwable throwable = logRecord.getThrown();
            
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            throwable.printStackTrace(writer);            
            emailSvc.sendEmail(emailSvc.getReportBugsEmail(), "Exception occurred", stringWriter.toString());
            System.out.println();
        }

        public void setLogRecord(LogRecord logRecord) {
            this.logRecord = logRecord;
        }
    }
}
