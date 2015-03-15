/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.util;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.editor.as.OpenASEditorAction;
import com.gas.domain.ui.util.api.IDownloadService;
import com.gas.domain.ui.util.api.IEFetchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IDownloadService.class)
public class DownloadService implements IDownloadService {

    IEFetchService service = Lookup.getDefault().lookup(IEFetchService.class);
    private Map<AUTHORITY, String[]> supported = new HashMap<AUTHORITY, String[]>();

    public DownloadService() {
        supported.put(AUTHORITY.NCBI, new String[]{"protein"});
    }

    public boolean isDownloadSupported(AUTHORITY authority, String db) {
        return false;
    }

    @Override
    public void downloadAndOpen(String db, String id) {
        if (db.equalsIgnoreCase("protein")) {
            service.setDb(db);
            service.setIds(id);
            List<AnnotatedSeq> list = service.sendRequest(AnnotatedSeq.class);
            if (!list.isEmpty()) {
                OpenASEditorAction action = new OpenASEditorAction(list.get(0));
                action.actionPerformed(null);
            }
        }
    }
}
