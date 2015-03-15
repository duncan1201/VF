/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.api;

import com.gas.gateway.core.service.api.AttSite;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import javax.swing.Icon;

/**
 *
 * @author dq
 */
public interface IAttBSiteUIServices {
    Color getBackgroundColor(String name);
    Color getForegroundColor(AttSite site);
    Image createImage(AttSite left, AttSite right, boolean leftDirection, boolean rightDirection);
    Icon createIcon(AttSite left, AttSite right, boolean leftDirection, boolean rightDirection);
}
