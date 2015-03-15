/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.as.service;

import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.api.IFetureKeyService;
import com.gas.domain.core.as.util.FetureKeyParser;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFetureKeyService.class)
public class FetureKeyService implements IFetureKeyService {

    private static Configuration configuration;

    static {
        IHibernateConfigService s = Lookup.getDefault().lookup(IHibernateConfigService.class);
        configuration = s.getDefaultConfiguration();
    }

    @Override
    public void create(FetureKey fk) {
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);
        dao.create(fk);
    }

    @Override
    public List<FetureKey> parse(InputStream inputStream) {
        return FetureKeyParser.parse(inputStream);
    }

    
    private List<FetureKey> parse(File file) {
        return FetureKeyParser.parse(file);
    }
    
    private List<FetureKey> merge(List<FetureKey> keys, List<FetureKey> keys2){
        List<FetureKey> ret = new ArrayList<FetureKey>();
        List<String> names = new ArrayList<String>();        
        for(FetureKey key: keys){
            ret.add(key);
            names.add(key.getName().toUpperCase(Locale.ENGLISH));
        }
        for(FetureKey key: keys2){
            final String name = key.getName().toUpperCase(Locale.ENGLISH);
            if(!names.contains(name)){
                ret.add(key);
                names.add(name);
            }
        }
        return ret;
    }
    
    private List<FetureKey> getHardcodedFetureKeys() {
        List<FetureKey> ret = FetureKeyCnst.getAll();
        return ret;
    }
    
    @Override
    public List<FetureKey> getAllInitialFetureKeys(File file){
        List<FetureKey> keys = parse(file);
        List<FetureKey> hard = getHardcodedFetureKeys();
        List<FetureKey> ret = merge(keys, hard);
        return ret;
    }

    @Override
    public List<FetureKey> getAll() {
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);
        return dao.getAll();
    }

    @Override
    public List<String> getAllQualifiers() {
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);
        return dao.getAllQualifiers();
    }

    @Override
    public FetureKey getFullByName(String name) {
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);

        return dao.getFullByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);
        long count = dao.getCount(name);
        return count != 0;
    }
    
    @Override
    public List<String> getAllNamesWithQualifiers(){
        List<String> ret = new ArrayList<String>();
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);
        ret.addAll(dao.getAllNamesWithQualifiers());
        return ret;    
    }

    @Override
    public List<String> getAllNames() {
        List<String> ret = new ArrayList<String>();
        FetureKeyDAO dao = new FetureKeyDAO();
        dao.setConfiguration(configuration);
        ret.addAll(dao.getAllNames());
        ret.addAll(FetureKeyCnst.getAllNames());
        return ret;
    }       

}
