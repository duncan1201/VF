/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

import com.gas.domain.core.api.IExternalURIService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IExternalURIService.class)
public class ExternalURIService implements IExternalURIService {

    private final Map<String, String> db2uri = new HashMap<String, String>();

    public ExternalURIService() {
        db2uri.put("BGD", "http://bovinegenome.org/genepages/btau40/genes/%s");
        db2uri.put("CCDS", "http://www.ncbi.nlm.nih.gov/CCDS/CcdsBrowse.cgi?REQUEST=CCDS&DATA=%s");
        db2uri.put("CDD", "http://www.ncbi.nlm.nih.gov/Structure/cdd/cddsrv.cgi?uid=%s");
        db2uri.put("GENEID", "http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=Retrieve&dopt=full_report&list_uids=%s");
        db2uri.put("EC_NUMBER", "http://enzyme.expasy.org/EC/%s");
        db2uri.put("HGNC", "http://www.genenames.org/data/hgnc_data.php?hgnc_id=%s");
        db2uri.put("HPRD", "http://www.hprd.org/protein/%s");
        db2uri.put("MIM", "http://omim.org/entry/%s");
        db2uri.put("TAXON", "http://www.ncbi.nlm.nih.gov/genome/sts/sts.cgi?uid=%s");
        db2uri.put("UNISTS", "http://www.ncbi.nlm.nih.gov/genome/sts/sts.cgi?uid=%s");
    }

    @Override
    public boolean isBrowsingSupported(String db) {
        boolean ret = false;
        Iterator<String> itr = db2uri.keySet().iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            if (str.equalsIgnoreCase(db)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public URI getURI(String db, String id) {
        URI ret = null;
        final String template = db2uri.get(db.toUpperCase(Locale.ENGLISH));
        if (template != null) {
            final String uri = String.format(template, id);
            try {
                ret = new URI(uri);
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return ret;
    }
}
