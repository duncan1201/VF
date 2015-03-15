/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.database;

import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.backup.api.IBackupService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DateFormatter;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class RestoreFromBackupDirPanel extends JPanel {
    
    private static IBackupService backupSvc = Lookup.getDefault().lookup(IBackupService.class);
    private JComboBox dbFilesCombo;
    
    RestoreFromBackupDirPanel(){
        createComponents();
        initComponents();
        hookupListeners();
    }
    
    public File getSelectedFile(){
        Item item = (Item)dbFilesCombo.getSelectedItem();
        return item.getFile();
    }
    
    private void createComponents(){
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel("Choose a backup to restore(a software restart is required)");
        add(label, c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        dbFilesCombo = new JComboBox();
        UIUtil.setPreferredWidthByPrototype(dbFilesCombo, "my Db 2015 Dec 11 23:12 AM xxxx");
        add(dbFilesCombo, c);       
    }
    
    private void initComponents(){
        File[] backupDbFiles = backupSvc.getDbFilesFromBackupDir();        
        ComboBoxModel model = createComboBoxModel(backupDbFiles);
        dbFilesCombo.setModel(model);
    }
    
    private ComboBoxModel createComboBoxModel(File[] backupDbFiles){
        Item[] items = new Item[backupDbFiles.length];
        for(int i = 0; i < backupDbFiles.length; i++){
            items[i] = new Item(backupDbFiles[i]);
        }
        Arrays.sort(items);        
        ComboBoxModel ret = new DefaultComboBoxModel(items);
        return ret;
    }
    
    private void hookupListeners(){}
    
    static class Item implements Comparable<Item> {
    
        private File file;
        
        public Item(File file){
            this.file = file;
        }
        
        public File getFile(){
            return file;
        }
        
        @Override
        public String toString(){
            long lastModified = file.lastModified();
            SimpleDateFormat f = new SimpleDateFormat("yyyy MMM dd hh:mm a");
            String dateStr = f.format(new Date(lastModified));
            
            String fileName = backupSvc.getOriginalFileName(file);
            StringBuilder ret = new StringBuilder();
            ret.append(fileName);
            ret.append("  ");
            ret.append(dateStr);
            return ret.toString();
        }

        @Override
        public int compareTo(Item o) {
            long oLastModified = o.getFile().lastModified();
            long lastModified = getFile().lastModified();
            int ret;
            if(lastModified != oLastModified){
                ret = lastModified > oLastModified? -1: 1;
            }else{
                ret = 0;
            }
            return ret;
        }
    }
}
