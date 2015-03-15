/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.primer.service;

import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.primer.UserInputDAO;
import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.primer3.UserInput;
import com.gas.primer3.core.api.BoulderIOUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IUserInputService.class)
public class UserInputService implements IUserInputService {

    private static IHibernateConfigService configService = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private static Configuration config;

    static {
        config = configService.getDefaultConfiguration();
    }

    @Override
    public List<UserInput> getAll(boolean full) {
        UserInputDAO dao = new UserInputDAO(config);
        List<UserInput> ret = dao.getAll(full);
        return ret;
    }

    @Override
    public void merge(List<UserInput> userInputs) {
        UserInputDAO dao = new UserInputDAO(config);
        dao.merge(userInputs);
    }

    @Override
    public UserInput merge(UserInput userInput) {
        UserInputDAO dao = new UserInputDAO(config);
        return dao.merge(userInput);
    }

    @Override
    public UserInput getFavorite() {
        UserInput userInput = null;
        UserInputDAO dao = new UserInputDAO(config);
        List<UserInput> userInputs = dao.getFavorite();

        if (!userInputs.isEmpty()) {
            userInput = userInputs.get(0);
        }
        return userInput;
    }

    @Override
    public void setFavorite(UserInput userInput) {
        UserInputDAO dao = new UserInputDAO(config);
        dao.setFavorite(userInput);
    }

    @Override
    public void create(UserInput ui) {
        UserInputDAO dao = new UserInputDAO(config);
        dao.create(ui);
    }

    @Override
    public boolean contains(String name) {
        UserInputDAO dao = new UserInputDAO(config);
        long count = dao.countByName(name);
        return count != 0;
    }

    @Override
    public void delete(UserInput userInput) {
        UserInputDAO dao = new UserInputDAO(config);
        dao.delete(userInput);
    }

    @Override
    public UserInput getFullByName(String name) {
        UserInputDAO dao = new UserInputDAO(config);
        return dao.getFullByName(name);
    }

    @Override
    public UserInput getFullByHibernateId(Integer hibernateId) {
        UserInputDAO dao = new UserInputDAO(config);
        return dao.getFullByHibernateId(hibernateId);
    }

    /**
     *
     */
    @Override
    public void export(UserInput userInput, File file) {
        UserInput cloned = userInput.clone();
        cloned.set("P3_FILE_TYPE", "settings");
        cloned.createP3_File_ID_ifNeeded();
        final String content = BoulderIOUtil.toString(cloned.getData());
        StringBuilder builder = new StringBuilder();
        builder.append("Primer3 File - http://primer3.sourceforge.net\n");
        builder.append(content);
        FileHelper.toFile(file, builder.toString());
    }

    @Override
    public UserInput importFromFile(File file) {
        UserInput ret = new UserInput();
        Map<String, String> data = BoulderIOUtil.parse(file);
        replacePRIMER_THERMODYNAMIC_ALIGNMENT(data);
        ret.resetData(data);
        return ret;
    }

    /**
     * The tag PRIMER_THERMODYNAMIC_ALIGNMENT was replaced by two new tags:
     * PRIMER_THERMODYNAMIC_OLIGO_ALIGNMENT which governs hairpin and
     * oligo-oligo interactions. (default: 1)
     * PRIMER_THERMODYNAMIC_TEMPLATE_ALIGNMENT which governs the oligo-template
     * interactions and, (default: 0)
     */
    private static void replacePRIMER_THERMODYNAMIC_ALIGNMENT(Map<String, String> data) {
        if (data.containsKey("PRIMER_THERMODYNAMIC_ALIGNMENT")) {
            String v = data.get("PRIMER_THERMODYNAMIC_ALIGNMENT");
            data.put("PRIMER_THERMODYNAMIC_OLIGO_ALIGNMENT", v);
            data.put("PRIMER_THERMODYNAMIC_TEMPLATE_ALIGNMENT", v);
            data.remove("PRIMER_THERMODYNAMIC_ALIGNMENT");
        }
    }

    @Override
    public UserInput mergeData(UserInput to, UserInput from) {
        UserInputDAO dao = new UserInputDAO(config);
        Map<String, String> dataTo = to.getData();
        Map<String, String> dataFrom = from.getData();
        Iterator<String> itr = dataFrom.keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            dataTo.put(key, dataFrom.get(key));
        }
        to.setUpdatedDate(new Date());        
        return dao.merge(to);
    }
}
