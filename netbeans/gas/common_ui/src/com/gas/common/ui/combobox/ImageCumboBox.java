/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.combobox;

import java.awt.Component;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author dq
 */
public class ImageCumboBox extends JComboBox {

    private List<Option> options = new ArrayList<Option>();

    public ImageCumboBox() {
        super();
        setRenderer(new Renderer());
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if (pName.equals("options")) {
                    DefaultComboBoxModel model = (DefaultComboBoxModel) getModel();
                    model.removeAllElements();
                    for (int i = 0; i < options.size(); i++) {
                        model.insertElementAt(i, i);
                    }
                }
            }
        });
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        List<Option> old = this.options;
        this.options = options;
        firePropertyChange("options", old, this.options);
    }

    public static class Option {

        private Image image;
        private String text;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private class Renderer extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return this;
        }
    }
}
