/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui;

import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.button.TwoStateButton;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.popupButton.PopupBtn;
import com.gas.common.ui.misc.BusyLabel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pdb.util.PDBParser;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.ui.util.api.IEFetchService;
import com.gas.domain.ui.util.api.IEFetchServiceFactory;
import com.gas.domain.core.pubmed.util.INCBIPubmedArticleSetParser;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.entrez.core.ESearch.api.ESearchCmdResult;
import com.gas.entrez.core.ESearch.api.IESearchCmdService;
import com.gas.entrez.core.ESummary.api.ESummaryResult;
import com.gas.entrez.core.ESummary.api.IESummaryCmd;
import com.gas.entrez.core.ui.tmpdb.api.ITmpDbService;
import com.gas.pdb.core.fetch.api.IFetchPDBService;
import com.gas.pdb.core.fetch.api.IFetchPDBServiceFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author dq
 */
public class SearchCtrlPanel extends JPanel {

    private PopupBtn fieldsButton;
    TwoStateButton searchButton;
    Folder folder;
    private JTextField anyTextField;
    protected BusyLabel busyLabel;
    protected int downloadedCount;
    protected int totalCount;
    protected ExecutorService executorService;
    protected boolean cancelled;
    private WeakReference<SearchPanel> searchPanelRef;
    public final static String RECORD_NO_FORMAT = "%d records found";
    public final static String DOWNLOADED_FORMAT = "%d records found. Downloaded %d of %d";
    public final static String DOWNLOADING_FORMAT = "%d records found. Downloading %d of %d...";
    public final static String COMPLETED_FORMAT = "%d records found. Download Complete";

    public SearchCtrlPanel(Folder folder) {
        this.folder = folder;
        initComponents();

        hookupListeners();
    }

    private void initComponents() {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;

        JLabel label = new JLabel("Any Field:");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(label, c);

        anyTextField = new JTextField();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(anyTextField, c);

        searchButton = getSearchButton();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        add(searchButton, c);

        busyLabel = new BusyLabel();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.insets = new Insets(0, 3, 0, 3);
        add(busyLabel, c);

        fieldsButton = getBasicButton();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        add(fieldsButton, c);
    }

    private void hookupListeners() {
        anyTextField.addActionListener(new SearchCtrlPanelListeners.TextFieldListener(this));
        searchButton.addActionListener("Search", new SearchCtrlPanelListeners.SearchActionListener(this));
        searchButton.addActionListener("Cancel", new SearchCtrlPanelListeners.CancelActionListener(this));
    }

    private PopupBtn getBasicButton() {
        if (fieldsButton == null) {
            fieldsButton = new PopupBtn("More Fields");

            AdvancedSearchPanel advancedSearchPanel = new AdvancedSearchPanel(folder.getName());

            fieldsButton.setPopupContent(advancedSearchPanel);

        }
        return fieldsButton;
    }

    SearchPanel getSearchPanel() {
        if (searchPanelRef == null) {
            searchPanelRef = new WeakReference<SearchPanel>(UIUtil.getParent(this, SearchPanel.class));
        }
        return searchPanelRef.get();
    }

    private TwoStateButton getSearchButton() {
        if (searchButton == null) {
            searchButton = new TwoStateButton("Search", "Cancel");
            searchButton.setIcon(ImageHelper.createImageIcon(ImageNames.MAGNIFIER_16));
        }
        return searchButton;
    }

    protected void doSearch(final String term) {
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                busyLabel.setBusy(true);

                getSearchPanel().setStatusLine("Searching NCBI...");
                getSearchPanel().setBusy(true);

                IESearchCmdService searchService = Lookup.getDefault().lookup(IESearchCmdService.class);
                searchService.setTerm(term);
                searchService.setDb(folder.getName());
                searchService.setRetmax(10000);

                ESearchCmdResult searchResult = null;
                try{
                    searchResult = searchService.sendRequest(ESearchCmdResult.class);
                }catch (UnknownHostException uhe){
                    getSearchPanel().setBusy(false);
                    busyLabel.setBusy(false);
                    getSearchPanel().setStatusLine("");                   
                    searchButton.setText(searchButton.getState1());
                    String msg = String.format(CNST.ERROR_FORMAT, "Could not connect to NCBI", "Please check your Internet connection");
                    DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                    m.setTitle("Could not connect to NCBI");
                    DialogDisplayer.getDefault().notify(m);
                    return;
                }
                totalCount = searchResult.getCount();
                getSearchPanel().setStatusLine(String.format(RECORD_NO_FORMAT, totalCount));
                if (searchResult.getCount() == 0) {
                    getSearchPanel().setBusy(false);
                    busyLabel.setBusy(false);
                }
                if (folder.getName().equalsIgnoreCase("structure")) {
                    doSummaryPDB(searchResult);
                } else {
                    doFetch(searchResult);
                }
            }
        });
    }

    private void doSummaryPDB(ESearchCmdResult searchResult) {
        List<String> idList = searchResult.getIdList(List.class);
        String concatenated = StrUtil.toString(idList, ",");
        IESummaryCmd cmd = Lookup.getDefault().lookup(IESummaryCmd.class);
        cmd.setDb("structure");
        cmd.setId(concatenated);
        ESummaryResult ret = cmd.sendRequest(ESummaryResult.class);
        List<String> pdbIds = ret.getNamedItem("PdbAcc");
        doFetchPDB(pdbIds);
    }

    private void doFetchPDB(List<String> pdbIds) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        downloadedCount = 0;
        for (String id : pdbIds) {
            FetchPDBTask t = new FetchPDBTask(id);
            executorService.submit(t);
        }
    }

    protected void clearTable() {
        SearchPanel searchPanel = UIUtil.getParent(SearchCtrlPanel.this, SearchPanel.class);
        int beforeRowCount = searchPanel.getDynamicTableModel().getRowCount();
        searchPanel.getDynamicTableModel().clear();
        int afterRowCount = searchPanel.getDynamicTableModel().getRowCount();
        if (beforeRowCount != afterRowCount) {
            searchPanel.getDynamicTableModel().fireTableDataChanged();
        }
    }

    private void doFetch(ESearchCmdResult searchResult) {
        if (cancelled) {
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Set<List<String>> idListSet = searchResult.getIdListSet(3);
        Iterator<List<String>> itr = idListSet.iterator();
        while (itr.hasNext()) {
            List<String> ids = itr.next();
            FetchTask ft = new FetchTask(ids);
            executorService.submit(ft);
        }        
    }

    private class FetchPDBTask extends SwingWorker<Void, Void> {

        private String pdbId;
        IFolderElementList fetched;

        public FetchPDBTask(String pdbId) {
            this.pdbId = pdbId;
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (cancelled) {
                return null;
            }
            TopComponent tc = UIUtil.getParent(SearchCtrlPanel.this, TopComponent.class);

            getSearchPanel().setStatusLine(String.format(DOWNLOADING_FORMAT, totalCount, downloadedCount + 1, totalCount));
            UIUtil.setTopCompName(tc, String.format(DOWNLOADING_FORMAT, totalCount, downloadedCount + 1, totalCount));
            IFetchPDBServiceFactory factory = Lookup.getDefault().lookup(IFetchPDBServiceFactory.class);
            IFetchPDBService service = factory.create();
            String str = service.sendRequest(pdbId, String.class);
            ITmpDbService dbService = Lookup.getDefault().lookup(ITmpDbService.class);
            dbService.put(folder.getName(), pdbId, str);
            fetched = parse(str, PDBDoc.class);


            return null;
        }

        @Override
        protected void done() {
            if (fetched == null || fetched.isEmpty()) {
                return;
            }
            if(cancelled){
                updateUICancelled(totalCount, downloadedCount);
                return;
            }
            downloadedCount += fetched.size();

            if (downloadedCount < totalCount) {
                getSearchPanel().setStatusLine(String.format(DOWNLOADING_FORMAT, totalCount, downloadedCount + 1, totalCount));
            } else {
                updateUICompleted(totalCount);
            }            
            // update the banner
            SearchPanel searchPanel = UIUtil.getParent(SearchCtrlPanel.this, SearchPanel.class);
            Folder folder = searchPanel.getFolder();
            folder.addObjects(fetched);
            BannerTC.getInstance().updateFolder(folder);

            // update the explorer
            Folder exFolder = ExplorerTC.getInstance().getFolder(folder.getAbsolutePath());
            exFolder.addObjects(fetched);
            ExplorerTC.getInstance().updateFolder(exFolder);
        }
    }

    private class FetchTask extends SwingWorker<Void, Void> {

        private List<String> uids;
        IFolderElementList fetched;

        public FetchTask(List<String> uids) {
            this.uids = uids;
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (cancelled) {
                return null;
            }
            String concatenated = StrUtil.toString(uids, ",");
            getSearchPanel().setStatusLine(String.format(DOWNLOADING_FORMAT, totalCount, downloadedCount + 1, totalCount));
            IEFetchServiceFactory fetchServiceFactory = Lookup.getDefault().lookup(IEFetchServiceFactory.class);
            IEFetchService fetchService = fetchServiceFactory.create();
            fetchService.setDb(folder.getName());
            fetchService.setIds(concatenated);
            List<String> fetchedStr = fetchService.sendRequest(String.class);
            ITmpDbService dbService = Lookup.getDefault().lookup(ITmpDbService.class);


            fetched = parse(fetchedStr.get(0), getRtnType(folder.getName()));
            List<String> ids4Db = getIds4Db(fetched);

            dbService.put(folder.getName(), ids4Db, fetchedStr.get(0));

            return null;
        }

        @Override
        protected void done() {
            if (fetched == null || fetched.isEmpty()) {
                return;
            }
            if(cancelled){
                updateUICancelled(totalCount, downloadedCount);
                return;
            }     
            downloadedCount += fetched.size();
            if (downloadedCount < totalCount) {
                getSearchPanel().setStatusLine(String.format(DOWNLOADING_FORMAT, totalCount, downloadedCount + 1, totalCount));
            } else {
                updateUICompleted(totalCount);
            }            
            // update the banner
            SearchPanel searchPanel = UIUtil.getParent(SearchCtrlPanel.this, SearchPanel.class);
            Folder folder = searchPanel.getFolder();
            folder.addObjects(fetched);
            BannerTC.getInstance().updateFolder(folder);

            // update the explorer
            Folder exFolder = ExplorerTC.getInstance().getFolder(folder.getAbsolutePath());
            exFolder.addObjects(fetched);
            ExplorerTC.getInstance().updateFolder(exFolder);
        }
    }
    
    void updateUICancelled(int totalCount, int downloadedCount){
        busyLabel.setBusy(false);
        getSearchPanel().setStatusLine(String.format(DOWNLOADED_FORMAT, totalCount, downloadedCount, totalCount));
        getSearchPanel().setBusy(false);
    }
    
    void updateUICompleted(int totalCount) {
        busyLabel.setBusy(false);
        getSearchButton().setText(getSearchButton().getState1());
        getSearchPanel().setStatusLine(String.format(COMPLETED_FORMAT, totalCount));
        getSearchPanel().setBusy(false);
    }

    private List<String> getIds4Db(List list) {
        List<String> ret = new ArrayList<String>();
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                if (obj instanceof AnnotatedSeq) {
                    AnnotatedSeq as = (AnnotatedSeq) obj;
                    String accession = as.getAccession();
                    ret.add(accession);
                } else if (obj instanceof PubmedArticle) {
                    PubmedArticle pa = (PubmedArticle) obj;
                    String pmid = pa.getPmid();
                    ret.add(pmid);
                } else {
                    throw new IllegalArgumentException(String.format("class '%s' not supported", obj.getClass().toString()));
                }
            }
        }
        return ret;
    }

    <T> IFolderElementList parse(String str, Class<T> retType) {
        IFolderElementList ret = new IFolderElementList();

        if (retType.isAssignableFrom(AnnotatedSeq.class)) {
            List<T> tmp = AnnotatedSeqParser.parse(str, new FlexGenbankFormat(), false, retType);
            for (int i = 0; i < tmp.size(); i++) {
                AnnotatedSeq as = (AnnotatedSeq) tmp.get(i);
                if (as.getLength() == 0) {
                    tmp.remove(i);
                    i--;
                } else {
                    ret.add(as);
                }
            }
        } else if (retType.isAssignableFrom(PubmedArticle.class)) {
            INCBIPubmedArticleSetParser parser = Lookup.getDefault().lookup(INCBIPubmedArticleSetParser.class);
            List<T> tmp = parser.parse(str, false, retType);
            for (T t : tmp) {
                ret.add((IFolderElement) t);
            }
        } else if (retType.isAssignableFrom(PDBDoc.class)) {
            PDBParser parser = new PDBParser();
            List<T> tmp = parser.parse(str, false, retType);
            for (T t : tmp) {
                ret.add((IFolderElement) t);
            }
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported", retType.toString()));
        }
        return ret;
    }

    private Class getRtnType(String db) {
        Class ret = null;
        if (db.equalsIgnoreCase("protein") || db.equalsIgnoreCase("nucleotide")) {
            ret = AnnotatedSeq.class;
        } else if (db.equalsIgnoreCase("pubmed")) {
            ret = PubmedArticle.class;
        } else {
            throw new IllegalArgumentException(String.format("database '%s' not supported", db));
        }
        return ret;
    }

    protected String getTerm() {
        StringBuilder ret = new StringBuilder();
        String anyText = anyTextField.getText().trim();
        ret.append(anyText);
        AdvancedSearchPanel asp = (AdvancedSearchPanel) getBasicButton().getPopupContent();
        String term = asp.getTerm(!anyText.isEmpty());
        ret.append(term);
        return ret.toString();
    }
}
