/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.Timer;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public abstract class AbstractSavableEditor extends TopComponent implements ISavableEditor {

    protected InstanceContent ic = new InstanceContent();
    private AbstractSavable mySavable;
    protected Timer timerSelectedFolder;

    public static AbstractSavableEditor getEditorByHibernateId(String hibernateId) {
        AbstractSavableEditor ret = null;
        TopComponent[] tcs = UIUtil.getEditorMode().getTopComponents();
        for (TopComponent tc : tcs) {
            if (tc instanceof AbstractSavableEditor) {
                AbstractSavableEditor ase = (AbstractSavableEditor) tc;
                IFolderElement fe = ase.getFolderElement();
                String hid = fe.getHibernateId();
                if (hid != null && hid.equals(hibernateId)) {
                    ret = ase;
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public boolean canClose() {
        if (getMySavableClass() == null) {
            return true;
        } else if (getLookup().lookup(getMySavableClass()) != null) {
            NotifyDescriptor.Confirmation cf = new NotifyDescriptor.Confirmation(String.format("Do you want to save the changes to \"%s\"?", getName()), "", NotifyDescriptor.YES_NO_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
            cf.setTitle("Save the changes");
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(cf);
            if (answer.equals(NotifyDescriptor.CANCEL_OPTION)) {
                return false;
            } else {
                if (answer.equals(NotifyDescriptor.OK_OPTION)) {
                    try {
                        mySavable.save();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    IMySavable iMySavable = (IMySavable) mySavable;
                    iMySavable.handleCloseWithoutSaving();
                }
                return true;
            }
        } else {
            return true;
        }
    }

    public abstract IFolderElement getFolderElement();

    public abstract String getStatusLineText();

    @Override
    public void componentShowing() {
        checkLayout();
    }
    
    private void checkLayout() {        
        boolean isEditor = WindowManager.getDefault().isEditorTopComponent(this);
        if (!isEditor) {
            String msg = "<html>Oops! Something wrong with the layout!<br/><br/>Please click Ok to reset the layout</html>";
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle("Oops");
            DialogDisplayer.getDefault().notify(m);
            startResetWindowTimer();
        }
    }

    private void startResetWindowTimer() {
        Timer timerResetWindow = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isEditor = WindowManager.getDefault().isEditorTopComponent(AbstractSavableEditor.this);
                if(!isEditor){
                    CommonUtil.resetWindows(); 
                }
            }
        });
        timerResetWindow.setRepeats(false);
        timerResetWindow.start();
    }

    @Override
    public void componentActivated() {
        if (getFolderElement() == null) {
            return;
        }        
        BannerTC.getInstance().setSelected(getFolderElement());
        if (getStatusLineText() != null) {
            StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().setText(getStatusLineText());
        }
        startSelectFolderTimer();
    }

    protected void startSelectFolderTimer() {
        timerSelectedFolder = new Timer(130, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Folder folder = getFolderElement().getFolder();
                if (folder != null) {
                    ExplorerTC.getInstance().setSelectedFolder(folder.getHibernateId());
                }
            }
        });
        timerSelectedFolder.setRepeats(false);
        timerSelectedFolder.start();
    }

    protected void cancelTimer() {
        if (timerSelectedFolder != null) {
            timerSelectedFolder.stop();
        }
    }

    @Override
    public void setCanSave() {
        if (getLookup().lookup(getMySavableClass()) == null) {
            try {
                Constructor cnst = getMySavableClass().getConstructor(this.getClass());

                mySavable = (AbstractSavable) cnst.newInstance(this);

                setMySavable(mySavable);
                ic.add(mySavable);
                UIUtil.styleTopCompName(this, true, true);
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SecurityException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void setMySavable(AbstractSavable savable) {
        this.mySavable = savable;
    }
}
