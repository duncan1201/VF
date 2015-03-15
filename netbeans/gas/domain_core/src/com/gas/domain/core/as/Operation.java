/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.Unicodes;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dq
 */
public class Operation implements Cloneable {

    public enum NAME {        
        ADD_ATTB("Add AttB Sites"),        
        Convert2Oligo("Convert to oligo"),
        Digestion("Digestion"),
        DIR_TOPO(String.format("Directional TOPO%s Cloning", Unicodes.TRADEMARK)),
        DIR_TOPO_INSERT(String.format("Create Directional TOPO%s Insert", Unicodes.TRADEMARK)),
        Extraction("Extraction"),
        Extract_AA("Extract Amino Acids"),        
        GW_BP("BP Reaction"),
        GW_LR("LR Reaction"),
        GW_ONE_CLICK("One-Click Gateway"),
        Isothermal_Assembly("Isothermal Assembly"),
        Ligation("Ligation"),        
        PCR("PCR"),
        TOPO("TOPO Cloning"),
        TOPO_TA_INSERT(String.format("Create TOPO%s TA Insert", Unicodes.TRADEMARK)),
        TOPO_TA(String.format("TOPO%s TA Cloning", Unicodes.TRADEMARK));
                
        public String value;

        NAME(String v) {
            value = v;
        }

        static NAME get(String value) {
            NAME ret = null;
            NAME[] names = values();
            for (NAME name : names) {
                if (name.value.equalsIgnoreCase(value)) {
                    ret = name;
                }
            }
            return ret;
        }
    };
    private Integer hibernateId;
    private String name;
    private Date date;
    private Set<Participant> participants = new HashSet<Participant>();

    public Operation(){
        date = new Date();
    }
    
    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    /**
     * for hibernate use
     */
    protected String getName() {
        return name;
    }

    /**
     * for hibernate use
     */
    protected void setName(String name) {
        this.name = name;
    }

    public void setNameEnum(NAME name) {
        setName(name.value);
    }

    public NAME getNameEnum() {
        if (name != null) {
            return NAME.get(name);
        } else {
            return null;
        }
    }

    /**
     *
     * @return the leaf participant count
     */
    public int getLeafParticipantCount() {
        int ret = 0;
        Iterator<Participant> itr = getParticipants().iterator();
        while (itr.hasNext()) {
            Participant participant = itr.next();
            Operation opeChild = participant.getOperation();
            if (opeChild == null) {
                ret++;
            } else {
                ret += opeChild.getLeafParticipantCount();
            }
        }
        return ret;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void addParticipant(Participant p){
        this.participants.add(p);
    }

    public Set<Participant> getParticipants() {
        return participants;
    }
    
    public TreeSet<Participant> getSortedParticipants(){
        TreeSet<Participant> ret = new TreeSet<Participant>(participants);
        return ret;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public Operation clone() {
        Operation ret = CommonUtil.cloneSimple(this);
        ret.setName(getName());
        ret.setParticipants(CommonUtil.copyOf(participants));
        return ret;
    }

    public static class Participant implements Cloneable, Comparable<Participant>  {

        private Integer hibernateId;
        private transient Operation operation;
        private String absolutePath;
        private boolean active;

        public Participant() {
        }
        
        public Participant(String absolutePath){
            this(absolutePath, true);
        }

        public Participant(String absolutePath, boolean active) {
            this.absolutePath = absolutePath;
            this.active = active;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Operation getOperation() {
            return operation;
        }

        public void setOperation(Operation operation) {
            this.operation = operation;
        }

        public String getAbsolutePath() {
            return absolutePath;
        }

        public String getName() {
            if (absolutePath != null) {
                final int index = absolutePath.lastIndexOf("\\");
                if (index > -1) {
                    return absolutePath.substring(index + 1);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        public void setAbsolutePath(String absolutePath) {
            this.absolutePath = absolutePath;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public Participant clone() {
            Participant ret = CommonUtil.cloneSimple(this);
            return ret;
        }

        @Override
        public int compareTo(Participant o) {
            return this.getAbsolutePath().compareTo(o.getAbsolutePath());
        }

        public int getLeafParticipantCount(){
            Operation o = getOperation();
            if(o == null){
                return 1;
            }else{
                return o.getLeafParticipantCount();
            }
        }
        
    }
}
