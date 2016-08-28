/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.msa.service.api.IMSAUIService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.domain.ui.dynamicTable.DynamicTableModel;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.database.core.api.IDomainUtil;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.ui.ToolbarSpacer;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import org.jdesktop.swingx.JXTable;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class TableActions {

    private static IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private static IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);

    public static class UnselectAllAction extends AbstractAction {

        private WeakReference<DynamicTable> ref;

        public UnselectAllAction(DynamicTable table) {
            super("Unselect All");
            ref = new WeakReference<DynamicTable>(table);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ref.get().updateCheckBoxs(false);
            BannerTC.getInstance().refreshHeader();
        }
    }

    public static class SelectAllAction extends AbstractAction {

        private WeakReference<DynamicTable> ref;

        public SelectAllAction(DynamicTable table) {
            super("Select All");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
            this.ref = new WeakReference<DynamicTable>(table);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ref.get().updateCheckBoxs(true);
            BannerTC.getInstance().refreshHeader();
        }
    }

    public static class FlipAction extends AbstractAction {

        private WeakReference<DynamicTable> tableRef;

        public FlipAction(DynamicTable table) {
            super("Flip");
            this.tableRef = new WeakReference<DynamicTable>(table);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DynamicTable table = tableRef.get();
            List objs = table.getCheckedObjects();

            boolean valid = validate(objs);
            if (!valid) {
                return;
            }

            AnnotatedSeq as = (AnnotatedSeq) objs.get(0);
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            Integer folderId = folder.getHibernateId();

            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            AnnotatedSeq flipped = AsHelper.flip(full);
            String pName = String.format("%s(reversed)", flipped.getName());
            pName = folder.getNewElementName(pName);
            flipped.setName(pName);

            String id = asService.save(flipped);

            Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(folderId);
            ExplorerTC.getInstance().updateFolder(updatedFolder);
            BannerTC.getInstance().updateFolder(updatedFolder);

            String title = "Flip";
            String msg = String.format("\"%s\" has been created. Do you want to open it?", flipped.getName());
            DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, pName, DialogDescriptor.YES_NO_OPTION);
            c.setTitle(title);
            Object answer = DialogDisplayer.getDefault().notify(c);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                full = asService.getFullByHibernateId(id);
                OpenASEditorAction action = new OpenASEditorAction(full);
                action.actionPerformed(null);
            }

        }

        private boolean validate(List objs) {
            boolean ret = true;
            String title = "Cannot perform flip operation";
            String msg = "Cannot perform flip operation";
            if (objs.isEmpty()) {
                ret = false;
                msg = String.format(CNST.MSG_FORMAT, "No molecule selected", "Please select one nucleotide");
            } else if (objs.size() > 1) {
                ret = false;
                msg = String.format(CNST.MSG_FORMAT, "More than one nucleotide selected", "Please select only one nucleotide");
            }

            if (!ret) {
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(title);
                DialogDisplayer.getDefault().notify(m);
            }
            return ret;
        }
    }

    public static class RecycleAction extends AbstractAction {

        private IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
        private WeakReference<DynamicTable> tableRef;
        private Folder folder;

        public RecycleAction(DynamicTable table) {
            super("Recycle", ImageHelper.createImageIcon(ImageNames.TRASH_BOX_16));
            this.tableRef = new WeakReference<DynamicTable>(table);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        }

        public Folder getFolder() {
            return folder;
        }

        public void setFolder(Folder folder) {
            this.folder = folder;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            ListSelectionModel model = tableRef.get().getSelectionModel();
            boolean isAdjusting = model.getValueIsAdjusting();
            if (!isAdjusting) {
                final IFolderElementList checkedObjs = tableRef.get().getCheckedObjects();
                if (checkedObjs.isEmpty()) {
                    return;
                }

                StringList names = domainUtil.getNames(checkedObjs);
                String msg = String.format("<html>Are you sure you want to move the following document%s to the Recycle Bin?</html>", names.size() > 1 ? "s" : "");
                final String title = "Move to the Recycle Bin";
                DelConfirmPanel delConfirmPanel = new DelConfirmPanel(msg, names);
                DialogDescriptor c = new DialogDescriptor(delConfirmPanel, title);
                delConfirmPanel.setDialogDescriptor(c);
                delConfirmPanel.validateInput();
                Integer answer = (Integer) DialogDisplayer.getDefault().notify(c);
                if (answer == JOptionPane.OK_OPTION) {
                    StringList unselectedText = delConfirmPanel.getUnselectedText();
                    checkedObjs.remove(unselectedText);
                    domainUtil.checkDesendants(checkedObjs, title);
                    if (checkedObjs.isEmpty()) {
                        return;
                    }
                    final Frame frame = WindowManager.getDefault().getMainWindow();
                    ProgressHelper.showProgressDialogAndRun(frame, "Moving 1 of 10 to recyele bin", new com.gas.common.ui.progress.ProgRunnable() {
                        DynamicTableModel tableModel;
                        Folder recycleBin;
                        Folder fromFolder = null;

                        @Override
                        public void run(ProgressHandle handle) {
                            if (checkedObjs == null || checkedObjs.isEmpty()) {
                                return;
                            }
                            Folder rootFolder = ExplorerTC.getInstance().getRootFolder();
                            recycleBin = rootFolder.getChild(FolderNames.MY_DATA).getChild(FolderNames.RECYCLE_BIN);
                            recycleBin = folderService.loadWithDataAndChildren(recycleBin.getHibernateId());
                            for (int i = 0; i < checkedObjs.size(); i++) {
                                Object o = checkedObjs.get(i);
                                handle.progress(String.format("Moving %d of %d to recyele bin", i + 1, checkedObjs.size()));
                                int progress = Math.round((i + 1) * 100f / checkedObjs.size());
                                handle.progress(progress);
                                IFolderElement fe = (IFolderElement) o;
                                if (fromFolder == null) {
                                    fromFolder = fe.getFolder(); // get the from folder here but don't reload from database                                    
                                }
                                domainUtil.move(fe, recycleBin);
                            }

                        }

                        @Override
                        public void done(ProgressHandle handle) {
                            if (fromFolder != null) {
                                fromFolder = folderService.loadWithDataAndChildren(fromFolder.getHibernateId()); // reload the from folder after moving
                                ExplorerTC.getInstance().updateFolder(fromFolder);
                                BannerTC.getInstance().updateFolder(fromFolder);
                            }

                            recycleBin = folderService.loadWithDataAndChildren(recycleBin.getHibernateId());
                            ExplorerTC.getInstance().updateFolder(recycleBin);
                            BannerTC.getInstance().updateFolder(recycleBin);

                            tableModel = (DynamicTableModel) tableRef.get().getModel();
                            tableModel.del(checkedObjs);
                            domainUtil.closeEditors(checkedObjs);
                        }
                    }, String.format("Moving %d item(s) to recycle bin...", checkedObjs.size()));
                }
            }
        }
    }

    public static class DelAction extends AbstractAction {

        private DynamicTable table;
        private Folder folder;
        private IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
        final static String TITLE = "Permanently Delete";

        public DelAction(DynamicTable table) {
            super(TITLE, ImageHelper.createImageIcon(ImageNames.MINUS_16));
            this.table = table;
            if (Utilities.isWindows()) {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.SHIFT_MASK));
            } else if (Utilities.isMac()) {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.SHIFT_MASK));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        public JXTable getTable() {
            return table;
        }

        public Folder getFolder() {
            return folder;
        }

        public void setFolder(Folder folder) {
            this.folder = folder;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = table.getSelectionModel();
            boolean isAdjusting = model.getValueIsAdjusting();
            if (!isAdjusting) {
                final IFolderElementList checkedObjs = table.getCheckedObjects();
                if (checkedObjs.isEmpty()) {
                    return;
                }

                StringList strList = domainUtil.getNames(checkedObjs);
                String msg = String.format("Are you sure you want to permanently delete the following document%s?", strList.size() > 1 ? "s" : "");
                final DelConfirmPanel confirmPanel = new DelConfirmPanel(msg, strList);
                DialogDescriptor d = new DialogDescriptor(confirmPanel, "Permanently Delete File");
                confirmPanel.setDialogDescriptor(d);
                confirmPanel.validateInput();
                Object answer = DialogDisplayer.getDefault().notify(d);
                if (answer.equals(JOptionPane.OK_OPTION)) {
                    Frame frame = WindowManager.getDefault().getMainWindow();
                    ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
                        @Override
                        public void run(ProgressHandle handle) {
                            handle.setIndeterminate(true);
                            StringList unselectedText = confirmPanel.getUnselectedText();
                            checkedObjs.remove(unselectedText);
                            if (checkedObjs.isEmpty()) {
                                return;
                            }
                            boolean isNCBIFolder = folder.isNCBIFolder();
                            boolean isRecycleBin = folder.isRecycleBin();
                            if (!isRecycleBin && !isNCBIFolder) {
                                handle.progress("Preparing to delete...");
                                domainUtil.checkDesendants(checkedObjs, "Permanently Delete File");
                            }
                            handle.progress("Deleting...");
                            delAll(folder, checkedObjs);
                        }

                        @Override
                        public void done(ProgressHandle handle) {
                            boolean isNCBIFolder = folder.isNCBIFolder();
                            Folder folderUpdated;
                            if (!isNCBIFolder) {
                                folderUpdated = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());
                            } else {
                                folderUpdated = folder;
                            }
                            ExplorerTC.getInstance().updateFolder(folderUpdated);
                            BannerTC.getInstance().updateFolder(folderUpdated);
                            BannerTC.getInstance().getFolderPanel().getTable().clearSelection();
                            BannerTC.getInstance().refreshHeader();
                            domainUtil.closeEditors(checkedObjs);
                        }
                    }, TITLE);
                }
            }
        }

        private void delAll(Folder folder, List<IFolderElement> fes) {
            if (folder.isNCBIFolder()) {
                for (IFolderElement fe : fes) {
                    folder.removeObject(fe);
                }
            } else {
                IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
                folderService.deleteData(folder, fes.toArray(new IFolderElement[fes.size()]));
            }
        }
    }

    public static class RestoreAction extends AbstractAction {

        IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);

        public RestoreAction(DynamicTable table) {
            super("Restore");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            IRestoreService restoreService = Lookup.getDefault().lookup(IRestoreService.class);
            final IFolderElementList checkedObjs = BannerTC.getInstance().getCheckedObjects();
            if (checkedObjs.isEmpty()) {
                return;
            }
            restoreService.restore(checkedObjs);
        }
    }

    @ActionID(category = "File",
            id = "com.gas.domain.ui.banner.TableActions$CutAction")
    @ActionRegistration(displayName = "#CTL_CutAction")
    @ActionReferences({
        @ActionReference(path = "Menu/File", position = 2000),
        @ActionReference(path = "Toolbars/CUSTOME", position = 3000)
    })
    @NbBundle.Messages("CTL_CutAction=Cut")
    public static class CutAction extends AbstractAction implements Presenter.Toolbar {

        public CutAction() {
            super("Cut", ImageHelper.createImageIcon(ImageNames.SCISSOR_BLUE_16));
            putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.SCISSOR_BLUE_24));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            IFolderPanel folderPanel = BannerTC.getInstance().getFolderPanel();
            DynamicTable table = folderPanel.getTable();
            Action action = table.getActionMap().get(TransferHandler.getCutAction().getValue(Action.NAME));
            action.actionPerformed(new ActionEvent(table,
                    ActionEvent.ACTION_PERFORMED,
                    null));
        }

        @Override
        public Component getToolbarPresenter() {
            JButton ret = new JButton();
            ret.setAction(this);
            ret.setVerticalTextPosition(SwingConstants.BOTTOM);
            ret.setHorizontalTextPosition(SwingConstants.CENTER);
            return ret;
        }
    }

    @ActionID(category = "File",
            id = "com.gas.domain.ui.banner.TabelActions$BeforeCutSpacer")
    @ActionRegistration(displayName = "#CTL_BeforeCutSpacer")
    @ActionReferences({
        @ActionReference(path = "Toolbars/CUSTOME", position = 2995)
    })
    @NbBundle.Messages("CTL_BeforeCutSpacer=")
    public static class BeforeCutSpacer extends ToolbarSpacer {
    }

    @ActionID(category = "File",
            id = "com.gas.domain.ui.banner.TableActions$CopyAction")
    @ActionRegistration(displayName = "#CTL_CopyAction")
    @ActionReferences({
        @ActionReference(path = "Menu/File", position = 2005),
        @ActionReference(path = "Toolbars/CUSTOME", position = 3005)
    })
    @NbBundle.Messages("CTL_CopyAction=Copy")
    public static class CopyAction extends AbstractAction implements Presenter.Toolbar {

        public CopyAction() {
            super("Copy", ImageHelper.createImageIcon(ImageNames.COPY_16));
            putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.COPY_24));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            IFolderPanel folderPanel = BannerTC.getInstance().getFolderPanel();
            DynamicTable table = folderPanel.getTable();
            Action action = table.getActionMap().get(TransferHandler.getCopyAction().getValue(Action.NAME));
            action.actionPerformed(new ActionEvent(table,
                    ActionEvent.ACTION_PERFORMED,
                    null));
        }

        @Override
        public Component getToolbarPresenter() {
            JButton ret = new JButton();
            ret.setAction(this);
            ret.setHorizontalTextPosition(SwingConstants.CENTER);
            ret.setVerticalTextPosition(SwingConstants.BOTTOM);
            return ret;
        }
    }

    @ActionID(category = "File",
            id = "com.gas.domain.ui.banner.TableActions$PasteAction")
    @ActionRegistration(displayName = "#CTL_PasteAction")
    @ActionReferences({
        @ActionReference(path = "Menu/File", position = 2010),
        @ActionReference(path = "Toolbars/CUSTOME", position = 3010)
    })
    @NbBundle.Messages("CTL_PasteAction=Paste")
    public static class PasteAction extends AbstractAction implements Presenter.Toolbar {

        public PasteAction() {
            super("Paste", ImageHelper.createImageIcon(ImageNames.PASTE_16));
            putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.PASTE_24));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            IFolderPanel folderPanel = BannerTC.getInstance().getFolderPanel();
            DynamicTable table = folderPanel.getTable();
            Action action = table.getActionMap().get(TransferHandler.getPasteAction().getValue(Action.NAME));
            action.actionPerformed(new ActionEvent(table,
                    ActionEvent.ACTION_PERFORMED,
                    null));
        }

        @Override
        public Component getToolbarPresenter() {
            JButton ret = new JButton();
            ret.setAction(this);
            ret.setVerticalTextPosition(SwingConstants.BOTTOM);
            ret.setHorizontalTextPosition(SwingConstants.CENTER);
            return ret;
        }
    }
}
