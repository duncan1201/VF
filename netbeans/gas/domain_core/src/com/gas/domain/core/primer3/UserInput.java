package com.gas.domain.core.primer3;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CommonUtil;
import java.util.*;

public class UserInput implements Cloneable {

    public static final String PRIMER_DNA_CONC = "PRIMER_DNA_CONC";
    public static final String PRIMER_SALT_MONOVALENT = "PRIMER_SALT_MONOVALENT";
    public static final String PRIMER_DNTP_CONC = "PRIMER_DNTP_CONC";
    public static final String PRIMER_SALT_DIVALENT = "PRIMER_SALT_DIVALENT";
    public static final String PRIMER_SALT_CORRECTIONS = "PRIMER_SALT_CORRECTIONS";
    public static final String PRIMER_TM_FORMULA = "PRIMER_TM_FORMULA";
    public static final String PRIMER_INTERNAL_DNA_CONC = "PRIMER_INTERNAL_DNA_CONC";
    public static final String PRIMER_INTERNAL_SALT_MONOVALENT = "PRIMER_INTERNAL_SALT_MONOVALENT";
    public static final String PRIMER_INTERNAL_DNTP_CONC = "PRIMER_INTERNAL_DNTP_CONC";
    public static final String PRIMER_INTERNAL_SALT_DIVALENT = "PRIMER_INTERNAL_SALT_DIVALENT";
    public static final String[] TM_NAMES = {PRIMER_DNA_CONC,
        PRIMER_SALT_MONOVALENT,
        PRIMER_DNTP_CONC,
        PRIMER_SALT_DIVALENT,
        PRIMER_SALT_CORRECTIONS,
        PRIMER_TM_FORMULA,
        PRIMER_INTERNAL_DNA_CONC,
        PRIMER_INTERNAL_SALT_MONOVALENT,
        PRIMER_INTERNAL_DNTP_CONC,
        PRIMER_INTERNAL_SALT_DIVALENT
    };
    private Integer hibernateId;
    private String name;
    private boolean favorite;
    private Date updatedDate;
    private Map<String, String> data = new LinkedHashMap<String, String>();

    public UserInput() {
        this.updatedDate = new Date();
    }

    @Override
    public UserInput clone() {
        UserInput ret = CommonUtil.cloneSimple(this);
        ret.resetData(CommonUtil.copyOf(data));
        return ret;
    }
    
    public Float getFloat(String key){
        String ret = getData().get(key);
        if(ret == null || ret.isEmpty()){
            return null;
        }else{
            return Float.parseFloat(ret);
        }        
    }

    public Integer getInt(String key) {
        String ret = getData().get(key);
        if (ret == null || ret.isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(ret);
        }
    }

    public void removeEmptyParams() {
        List<String> toBeRemoved = new ArrayList<String>();

        Iterator<String> itr = data.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            if (data.get(key).isEmpty()) {
                toBeRemoved.add(key);
            }
        }

        itr = toBeRemoved.iterator();
        while (itr.hasNext()) {
            data.remove(itr.next());
        }
    }

    public void createP3_File_ID_ifNeeded() {
        if (!data.containsKey("P3_FILE_ID")) {
            data.put("P3_FILE_ID", "");
        }
    }

    public String[] toArray() {
        return getData().values().toArray(new String[getData().size()]);
    }

    public void keepsOnly(List<String> names) {
        keepsOnly(names.toArray(new String[names.size()]));
    }

    public void keepsOnly(String... names) {
        final StringList nameList = new StringList(names);
        List<String> toBeRemoved = new ArrayList<String>();

        Iterator<String> itr = data.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            if (!nameList.containsIgnoreCase(key)) {
                toBeRemoved.add(key);
            }
        }

        itr = toBeRemoved.iterator();
        while (itr.hasNext()) {
            data.remove(itr.next());
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        String ret = "";
        if (data != null && data.containsKey("P3_FILE_ID")) {
            ret = data.get("P3_FILE_ID");
        }
        return ret;
    }

    public void setDescription(String desc) {
        if (data != null) {
            data.put("P3_FILE_ID", desc);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public void touchAll() {
        Iterator<String> itr = getData().keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            get(key);
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public void enablePickAnyway() {
        data.put("PRIMER_PICK_ANYWAY", "1");
    }

    public void resetData(Map<String, String> d) {
        data.clear();
        data.putAll(d);
    }

    /**
     * for hibernate use only
     */
    protected void setData(Map<String, String> data) {
        this.data = data;
    }

    public void updateData(Map<String, String> _data) {
        Iterator<String> itr = _data.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String datum = _data.get(key);
            this.data.put(key, datum);
        }
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        } else {
            return "";
        }
    }

    /**
     * boolean; default 1
     */
    public boolean isThermodynamicModels() {
        String v = get("PRIMER_THERMODYNAMIC_OLIGO_ALIGNMENT");
        return v == null || v.isEmpty() || v.equals("1");
    }

    public void setPrimerTask(String task) {
        set("PRIMER_TASK", task);
    }

    public void setThermodynamicModels(boolean use) {
        if (use) {
            set("PRIMER_THERMODYNAMIC_OLIGO_ALIGNMENT", "1");
        } else {
            set("PRIMER_THERMODYNAMIC_OLIGO_ALIGNMENT", "0");
        }
    }

    /**
     * Too long, do not need to save
     */
    public void removeSequenceTemplate() {
        this.data.remove("SEQUENCE_TEMPLATE");
    }

    public void setSequenceTemplate(String t) {
        this.data.put("SEQUENCE_TEMPLATE", t);
    }

    public void removeSettingsFileTag() {
        this.data.remove("P3_FILE_ID");
        this.data.remove("P3_FILE_TYPE");
    }

    public void removeEntries(String... keys) {
        for (String key : keys) {
            this.data.remove(key);
        }
    }

    /**
     * boolean; default 1
     */
    public boolean pickLeftPrimer() {
        String v = get("PRIMER_PICK_LEFT_PRIMER");
        return v == null || v.isEmpty() || v.equals("1");
    }

    /**
     * boolean; default 1
     */
    public boolean pickRightPrimer() {
        String v = get("PRIMER_PICK_RIGHT_PRIMER");
        return v == null || v.isEmpty() || v.equals("1");
    }

    /**
     * boolean; default 0
     */
    public boolean pickInternalOligo() {
        String v = get("PRIMER_PICK_INTERNAL_OLIGO");
        return v != null && v.equals("1");
    }

    public void setPickInternalOligo(boolean pick) {
        data.put("PRIMER_PICK_INTERNAL_OLIGO", pick ? "1" : "0");
    }

    public Integer getVersion() {
        if (data.containsKey("SEQUENCE_TEMPLATE")) {
            return 4;
        } else if (data.containsKey("SEQUENCE")) {
            return 3;
        }
        return null;
    }

    public static class KeyComparator implements Comparator<String> {

        static final List<String> ORDERING = new ArrayList<String>();

        static {
            ORDERING.add("P3_FILE_TYPE");
            ORDERING.add("P3_FILE_ID");
        }

        @Override
        public int compare(String o1, String o2) {
            int ret = 0;
            if (ORDERING.contains(o1)
                    && ORDERING.contains(o2)) {
                int index1 = ORDERING.indexOf(o1);
                int index2 = ORDERING.indexOf(o2);
                ret = index1 - index2;
            } else if (ORDERING.contains(o1)
                    || ORDERING.contains(o2)) {
                ret = -1;
            } else {
                ret = o1.compareTo(o2);
            }
            return ret;
        }
    }
}
