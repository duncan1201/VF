/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui.tmpdb;

import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pdb.util.PDBParser;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.PubmedArticleSet;
import com.gas.domain.core.pubmed.util.INCBIPubmedArticleSetParser;
import com.gas.entrez.core.ui.tmpdb.api.ITmpDbService;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ITmpDbService.class)
public class TmpDbService implements ITmpDbService {

    private Map<String, File> files = new HashMap<String, File>();

    @Override
    public synchronized <T> T get(String db, String id, Class<T> clazz) {
        T ret = null;
        Iterator<String> itr = files.keySet().iterator();
        boolean found = false;
        File file = null;
        while (itr.hasNext() && !found) {
            String key = itr.next();
            String[] splits = key.split("-");
            String dbName = splits[0];
            if (dbName.equalsIgnoreCase(db)) {
                String idsStr = splits[1];
                String[] ids = idsStr.split(",");
                found = StrUtil.contains(ids, id, true);
                if (found) {
                    file = files.get(key);
                }
            }
        }

        if (file != null) {
            if (clazz.isAssignableFrom(AnnotatedSeq.class)) {
                ret = (T) AnnotatedSeqParser.singleParse(file, new FlexGenbankFormat(), id);
            } else if (clazz.isAssignableFrom(PubmedArticle.class)) {
                INCBIPubmedArticleSetParser parser = Lookup.getDefault().lookup(INCBIPubmedArticleSetParser.class);
                PubmedArticleSet set = parser.parse(file);
                ret = (T) set.getArticle(id);
            } else if (clazz.isAssignableFrom(PDBDoc.class)){                
                PDBParser parser = new PDBParser();
                ret = (T)parser.parse(file);
            }else {
                throw new IllegalArgumentException(String.format("not supported", clazz.toString()));
            }
        }

        return ret;
    }

    @Override
    public synchronized void put(final String db, List<String> ids, String contents) {
        File file = FileHelper.toFile(contents);
        String key = createKey(db, ids);
        files.put(key, file);
    }

    /*
     * Format of the key is "db-123-12121-12121", where "-" is the delimiter
     */
    private String createKey(String db, List<String> ids) {
        StringBuilder ret = new StringBuilder();
        String concanetated = StrUtil.toString(ids, ",");
        ret.append(db);
        ret.append('-');
        ret.append(concanetated);
        return ret.toString();
    }

    @Override
    public void put(String db, String id, String contents) {
        List<String> tmp = new ArrayList<String>();
        tmp.add(id); 
        put(db, tmp, contents);
    }
}
