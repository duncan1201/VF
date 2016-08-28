/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.database;

import com.gas.common.ui.jcomp.TitledSeparator;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.backup.api.BackupOption;
import com.gas.database.core.backup.api.IBackupService;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class DbConfigPanel extends JPanel {
 
    private IDbConnSettingsService svc = Lookup.getDefault().lookup(IDbConnSettingsService.class);
    private IBackupService backupSvc = Lookup.getDefault().lookup(IBackupService.class);
    private final Insets INSETS_DEFAULT = UIUtil.getDefaultInsets();
    private final Insets INSETS_ITEM = new Insets(0, INSETS_DEFAULT.left * 2, 0, INSETS_DEFAULT.right);
    private JButton changeDbFileBtn;
    private JLabel lastBackupLabel;
    private JButton chooseBackupDirBtn;
    private JLabel backupLocLabel;
    private JLabel freeSpaceLabel;
    
    private JRadioButton alertOptionBtn;
    private JRadioButton autoOptionBtn;
    private JRadioButton manualOptionBtn;
    private JButton backupToBackupDirBtn;
    private JButton restoreFromBackupDirBtn;
    
    public static final String TITLE = "Database Configuration";
    
    public static final String CMD_CHANGE_DB_FILE = "changeDbFile";
    public static final String CMD_CHANGE_BACKUP_DIR = "changeBackupDir";
    public static final String CMD_ALERT_OPTION = BackupOption.ALERT_BEFORE_CLOSING.name();
    public static final String CMD_AUTO_OPTION = BackupOption.AUTO_BEFORE_CLOSING.name();
    public static final String CMD_MANUAL_OPTION = BackupOption.MANUAL.name();
    public static final String CMD_BACKUP_TO_BACKUP_DIR = "backupToBackupDir";
    public static final String CMD_RESTORE_BACKUP_DIR = "restoreFromBackupDir";
    
    public DbConfigPanel(){
        setOpaque(true);
        setBackground(Color.WHITE);
        createComponents();
        initComponents();
        hookupListeners();         
    }
    
    void updateLastBackupTime(){
        Long lastBackupTime = backupSvc.getLastBackupTime();
        if(lastBackupTime == null){
            lastBackupLabel.setText("No backup");
        }else{
            SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd hh:mm a");            
            lastBackupLabel.setText(String.format("Last backup was: %s", format.format(new Date(lastBackupTime))));
        }    
    }
    
    private void initComponents(){
        updateLastBackupTime();
        
        BackupOption backupOption = backupSvc.getBackupOption();
        if(backupOption == null){
            backupSvc.saveBackupOption(backupSvc.getDefaultBackupOption());
            backupOption = backupSvc.getBackupOption();
        }
        
        alertOptionBtn.setSelected(backupOption == BackupOption.ALERT_BEFORE_CLOSING);
        autoOptionBtn.setSelected(backupOption == BackupOption.AUTO_BEFORE_CLOSING);
        manualOptionBtn.setSelected(backupOption == BackupOption.MANUAL);
        
        //
        File file = backupSvc.getBackupDir();
        if(file == null){
            backupSvc.updateBackupDir(backupSvc.getDefaultBackupDir(), false);
            file = backupSvc.getBackupDir();
        }
        backupLocLabel.setText(file.getAbsolutePath());
               
        freeSpaceLabel.setText(getSizeForDisplay(file.getFreeSpace()));                
    }
    
    private void hookupListeners(){
        DbConfigPanelListeners.ActionListner c = new DbConfigPanelListeners.ActionListner(this);
        
        changeDbFileBtn.addActionListener(c);
        chooseBackupDirBtn.addActionListener(c);
        
        alertOptionBtn.addActionListener(c);
        autoOptionBtn.addActionListener(c);
        manualOptionBtn.addActionListener(c);
        
        backupToBackupDirBtn.addActionListener(c);
        
        restoreFromBackupDirBtn.addActionListener(c);
    }
    
    private JPanel createInfoPanel(){
        JPanel ret = new JPanel();
        ret.setOpaque(false);
        TitledBorder border = BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Database Config", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font"));
        Font oldFont = border.getTitleFont();        
        border.setTitleFont(oldFont.deriveFont(Font.BOLD));        
        ret.setBorder(border);
        
        ret.setLayout(new GridBagLayout());
        GridBagConstraints c;
                
        JPanel basicInfoPanel = createBasicInfo();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INSETS_DEFAULT;
        ret.add(basicInfoPanel, c);
        
        JPanel backupOptionPanel = createBackupOptionsPanel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INSETS_DEFAULT;
        ret.add(backupOptionPanel, c);
        
        JPanel backupDirPanel = createBackupDirection();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = INSETS_DEFAULT;
        ret.add(backupDirPanel, c);        
        return ret;
    }
    
    private void createComponents(){        
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = gridy++;
        add(createInfoPanel(), c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = gridy++;
        add(createBackupPanel(), c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = gridy++;
        add(createRestorePanel(), c);
    }
    
    private JPanel createBackupPanel(){
        JPanel ret = new JPanel();
        ret.setOpaque(false);
        TitledBorder border = BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Backup", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font"));
        Font oldFont = border.getTitleFont();        
        border.setTitleFont(oldFont.deriveFont(Font.BOLD));        
        ret.setBorder(border);
        
        ret.setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        backupToBackupDirBtn = new JButton("Backup to the backup directory...");        
        backupToBackupDirBtn.setActionCommand(CMD_BACKUP_TO_BACKUP_DIR);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        ret.add(backupToBackupDirBtn, c);
        
        
        return ret;
    }
    
    private JPanel createRestorePanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        TitledBorder border = BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Restore", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font"));
        Font oldFont = border.getTitleFont();        
        border.setTitleFont(oldFont.deriveFont(Font.BOLD));        
        ret.setBorder(border);
                
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        restoreFromBackupDirBtn = new JButton("Restore from the default backup directory...");
        restoreFromBackupDirBtn.setActionCommand(CMD_RESTORE_BACKUP_DIR);
        ret.add(restoreFromBackupDirBtn, c);
        return ret;
    }    
    
    private JPanel createBackupDirection(){
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();        
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 3;
        TitledSeparator separator = new TitledSeparator("Where to back up", false);
        separator.setOpaque(false);
        ret.add(separator, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = INSETS_ITEM;
        ret.add(new JLabel("Location:"), c);
                    
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        backupLocLabel = new JLabel();
        ret.add(backupLocLabel, c);
       
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, INSETS_DEFAULT.left, 0, 0);
        chooseBackupDirBtn = new JButton("Browse");
        chooseBackupDirBtn.setActionCommand(CMD_CHANGE_BACKUP_DIR);
        UIUtil.setSizeToSmall(chooseBackupDirBtn);
        ret.add(chooseBackupDirBtn, c);
        
        //
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = INSETS_ITEM;
        ret.add(new JLabel("Free space:"), c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 2;
        freeSpaceLabel = new JLabel();
        ret.add(freeSpaceLabel, c);
        return ret;
    }    
    
    void updateBackupDir(File file){
        backupLocLabel.setText(file.getAbsolutePath());
        freeSpaceLabel.setText(getSizeForDisplay(file.getFreeSpace()));
    }
    
    private JPanel createBasicInfo(){
        DbConnSettings settings = svc.getDbConnSettings();
        File file = settings.getDatabaseFile();
        
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        GridBagConstraints c;        
        int gridy = 0;      
        
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        TitledSeparator separator = new TitledSeparator("Database File Info", false);
        separator.setOpaque(false);
        ret.add(separator, c);      
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = INSETS_ITEM;
        ret.add(new JLabel("Location:"), c);        
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;        
        JLabel filePathField = new JLabel(file.getAbsolutePath());        
        ret.add(filePathField, c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, INSETS_DEFAULT.left, 0, 0);
        changeDbFileBtn = new JButton("Browse");
        UIUtil.setSizeToSmall(changeDbFileBtn);        
        changeDbFileBtn.setActionCommand(CMD_CHANGE_DB_FILE);
        ret.add(changeDbFileBtn, c);
        
        //
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = INSETS_ITEM;
        ret.add(new JLabel("Size:"), c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        JLabel fileSizeLabel = new JLabel(getSizeForDisplay(file.length()));
        ret.add(fileSizeLabel, c);
        
        //   
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 3;
        lastBackupLabel = new JLabel("Last backup was two weeks ago");
        ret.add(lastBackupLabel, c);
        
        return ret;
    }
    
    private JPanel createBackupOptionsPanel(){        
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        TitledSeparator separator = new TitledSeparator("When to back up", false);
        separator.setOpaque(false);
        ret.add(separator, c);
        
        alertOptionBtn = new JRadioButton("Alert before closing");
        alertOptionBtn.setActionCommand(CMD_ALERT_OPTION);
        c = new GridBagConstraints();
        c.gridx = 0;    
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INSETS_ITEM;
        ret.add(alertOptionBtn, c);        
        
        autoOptionBtn = new JRadioButton("Automatic backup before closing");
        autoOptionBtn.setActionCommand(CMD_AUTO_OPTION);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        c.insets = INSETS_ITEM;
        ret.add(autoOptionBtn, c);
        
        manualOptionBtn = new JRadioButton("Manual backup");
        manualOptionBtn.setActionCommand(CMD_MANUAL_OPTION);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;       
        c.insets = INSETS_ITEM;
        ret.add(manualOptionBtn, c);        
        
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(alertOptionBtn);
        bGroup.add(autoOptionBtn);
        bGroup.add(manualOptionBtn);
        return ret;
    }
    
    private String getSizeForDisplay(long lengthInBytes){
        DecimalFormat formater = new DecimalFormat("#.##");
        
        final long Kilobyte = 1024;
        final long MB = 1024 * Kilobyte;
        final long GB = 1024 * MB;
        
        if(lengthInBytes < Kilobyte){
            return String.format("%d bytes", lengthInBytes);
        }else if(lengthInBytes >= Kilobyte && lengthInBytes < MB){            
            return String.format("%s KB", formater.format(1.0 * lengthInBytes / Kilobyte));
        }else if (lengthInBytes >= MB && lengthInBytes < GB){
            return String.format("%s MB", formater.format(1.0 * lengthInBytes / MB));
        }else {
            return String.format("%s GB", formater.format(1.0 * lengthInBytes / GB));
        }
    }
}
