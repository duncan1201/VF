/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.exceptionhandler;

import java.io.File;

/**
 *
 * @author dq
 */
public interface IEmailService {
    String getReportBugsEmail();
    boolean sendEmail(String to, String subject, String body, File attachment);
    boolean sendEmail(String to, String subject, String body);
}
