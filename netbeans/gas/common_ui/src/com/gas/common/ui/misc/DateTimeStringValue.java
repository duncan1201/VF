/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author dunqiang
 */
public class DateTimeStringValue implements org.jdesktop.swingx.renderer.StringValue {

    private Integer dateStyle;
    private Integer timeStyle;

    /**
     *
     */
    public DateTimeStringValue(Integer dateStyle, Integer timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        if (dateStyle == null && timeStyle == null) {
            throw new IllegalArgumentException("At least one of the parameter must be non-null");
        }
    }

    public DateTimeStringValue() {
        this(DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    @Override
    public String getString(Object value) {
        if (value == null) {
            return "";
        } else if (!(value instanceof Date)) {
            return value.toString();
        }
        Date date = (Date) value;
        DateFormat format = DateFormat.getDateInstance(dateStyle,
                Locale.ENGLISH);
        if (dateStyle != null && timeStyle != null) {
            format = DateFormat.getDateTimeInstance(dateStyle, timeStyle,
                    Locale.ENGLISH);
        } else if (dateStyle != null) {
            format = DateFormat.getDateInstance(dateStyle,
                    Locale.ENGLISH);
        } else if (timeStyle != null) {
            format = DateFormat.getTimeInstance(timeStyle,
                    Locale.ENGLISH);
        }
        return format.format(date);
    }
}
