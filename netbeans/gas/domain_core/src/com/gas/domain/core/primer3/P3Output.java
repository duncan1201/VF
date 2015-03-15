package com.gas.domain.core.primer3;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.Feture;
import java.util.*;

public class P3Output implements Cloneable {

    static final String regexConsider = "considered [0-9]+";
    static final String regexOk = "ok [0-9]+";
    private Integer hibernateId;
    private String primerTask;
    /* do not need to persist*/
    private String sequenceTemplate;
    private Integer targetStart;
    private Integer targetLength;
    private Integer internalExcludedStart;
    private Integer internalExcludedLength;
    private String primerPairExplain;
    private String primerLeftExplain;
    private String primerRightExplain;
    private String primerInternalExplain;
    private Set<Oligo> oligos = new HashSet<Oligo>();
    private Set<UserInput> userInputs = new HashSet<UserInput>();

    public P3Output() {
    }

    @Override
    public P3Output clone() {
        P3Output ret = CommonUtil.cloneSimple(this);
        ret.setOligos(CommonUtil.copyOf(oligos));

        ret.setUserInputs(CommonUtil.copyOf(userInputs));

        return ret;
    }

    public String getSequenceTemplate() {
        return sequenceTemplate;
    }

    public void setSequenceTemplate(String sequenceTemplate) {
        this.sequenceTemplate = sequenceTemplate;
    }
    
    public void setOligoStart(int start) {
        for (Oligo o : oligos) {
            o.setStart(start);
        }
    }

    public void setOligoLength(int length) {
        for (Oligo o : oligos) {
            o.setLength(length);
        }
    }

    public boolean containsLeftOligoElement() {
        boolean ret = false;
        Iterator<Oligo> itr = oligos.iterator();
        while (itr.hasNext()) {
            Oligo oligo = itr.next();
            if (oligo.getLeft() != null) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public boolean containsRightOligoElement() {
        boolean ret = false;
        Iterator<Oligo> itr = oligos.iterator();
        while (itr.hasNext()) {
            Oligo oligo = itr.next();
            if (oligo.getRight() != null) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /**
     * for hibernate use only
     */
    protected Set<UserInput> getUserInputs() {
        return userInputs;
    }

    /**
     * for hibernate use only
     */
    protected void setUserInputs(Set<UserInput> userInputs) {
        this.userInputs = userInputs;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public UserInput getUserInput() {
        if (userInputs.isEmpty()) {
            return null;
        } else {
            return userInputs.iterator().next();
        }
    }

    public void setUserInput(UserInput userInput) {
        this.userInputs.clear();
        this.userInputs.add(userInput);
    }

    public void updateConc() {
        UserInput userInput;
        if (!userInputs.isEmpty()) {
            userInput = userInputs.iterator().next();
            for (Oligo oligo : oligos) {
                oligo.updateConc(userInput);
            }
        }
    }

    public P3Output sub(int start, int end) {
        P3Output ret = P3OutputHelper.sub(this, start, end);
        return ret;
    }

    public String getPrimerTask() {
        return primerTask;
    }

    public void setPrimerTask(String primerTask) {
        this.primerTask = primerTask;
    }

    public String getReagentsHtml() {
        return getReagentsHtml(true);
    }

    public String getReagentsHtml(boolean includeHtmlTag) {
        if (userInputs.isEmpty()) {
            return "";
        }
        UserInput userInput = userInputs.iterator().next();
        StringBuilder ret = new StringBuilder();
        if (includeHtmlTag) {
            ret.append("<html>");
        }
        List<String> reagentsStr = null;
        if (userInput.pickLeftPrimer() || userInput.pickRightPrimer()) {
            reagentsStr = new ArrayList<String>();
            reagentsStr.add(String.format("Monovalent cation conc.:%s mM", userInput.get("PRIMER_SALT_MONOVALENT")));
            reagentsStr.add(String.format("Divalent cation conc.:%s mM", userInput.get("PRIMER_SALT_DIVALENT")));
            reagentsStr.add(String.format("dNTP conc.:%s mM", userInput.get("PRIMER_DNTP_CONC")));
            reagentsStr.add(String.format("Annealing oligo conc.:%s nM", userInput.get("PRIMER_DNA_CONC")));

            String htmlList = StrUtil.toHtmlList(reagentsStr);
            ret.append("Reagents for forward/reverse primers:");
            ret.append(htmlList);
        }
        if (userInput.pickInternalOligo()) {
            reagentsStr = new ArrayList<String>();
            reagentsStr.add(String.format("Monovalent cation conc.:%s mM", userInput.get("PRIMER_INTERNAL_SALT_MONOVALENT")));
            reagentsStr.add(String.format("Divalent cation conc.:%s mM", userInput.get("PRIMER_INTERNAL_SALT_DIVALENT")));
            reagentsStr.add(String.format("dNTP conc.:%s mM", userInput.get("PRIMER_INTERNAL_DNTP_CONC")));
            reagentsStr.add(String.format("Annealing oligo conc.:%s nM", userInput.get("PRIMER_INTERNAL_DNA_CONC")));

            String htmlList = StrUtil.toHtmlList(reagentsStr);
            ret.append("Reagents for DNA probes:");
            ret.append(htmlList);
        }
        if (includeHtmlTag) {
            ret.append("</html>");
        }
        return ret.toString();
    }

    public String getExplainsInHtml() {
        return getExplainsInHtml(null, true);
    }

    public String getExplainsInHtml(String title) {
        return getExplainsInHtml(title, true);
    }

    public String getExplainsInHtml(String title, boolean includeHtmlTag) {
        Map<String, String> explains = getExplains();
        StringBuilder ret = new StringBuilder();
        if (includeHtmlTag) {
            ret.append("<html>");
        }
        if (title != null && !title.isEmpty()) {
            ret.append("<center><b>");
            ret.append(title);
            ret.append("</b></center>");
        }
        if (explains.containsKey("LEFT")) {
            ret.append(toTitle("Forward Primer", explains.get("LEFT")));
            ret.append(toUnorderedList(explains.get("LEFT")));
        }
        if (explains.containsKey("INTERNAL")) {
            ret.append(toTitle("DNA Probe", explains.get("INTERNAL")));
            ret.append(toUnorderedList(explains.get("INTERNAL")));
        }
        if (explains.containsKey("RIGHT")) {
            ret.append(toTitle("Reverse Primer", explains.get("RIGHT")));
            ret.append(toUnorderedList(explains.get("RIGHT")));
        }
        if (explains.containsKey("PAIR")) {
            ret.append(toTitle("Pair", explains.get("PAIR")));
            ret.append(toUnorderedList(explains.get("PAIR")));
        }
        if (includeHtmlTag) {
            ret.append("</html>");
        }
        return ret.toString();
    }

    /**
     * @param msg: considered 12417, GC content failed 1119, low tm 5299, high
     * tm 1816, high any compl 10, high end compl 5, high hairpin stability
     * 2349, long poly-x seq 129, high template mispriming score 1, ok 1689
     */
    private String toUnorderedList(String msg) {
        StringBuilder ret = new StringBuilder();
        ret.append("<ul>");
        final String[] splits = msg.split(",");
        for (String split : splits) {
            split = split.trim();
            if (split.startsWith("ok") || split.startsWith("considered")) {
                continue;
            }
            ret.append("<li>");
            ret.append(split);
            ret.append("</li>");
        }
        ret.append("</ul>");
        return ret.toString();
    }

    private String toTitle(String title, String msg) {

        final String consider = StrUtil.extract(regexConsider, msg);
        final String ok = StrUtil.extract(regexOk, msg);
        StringBuilder ret = new StringBuilder();
        ret.append(title);
        ret.append(' ');
        ret.append("(");
        ret.append(consider);
        ret.append(",");
        ret.append(ok);
        ret.append(")");
        return ret.toString();
    }

    public boolean containsExplains() {
        return !getExplains().isEmpty();
    }

    /**
     * @return possible keys: LEFT, RIGHT, PAIR, INTERNAL
     */
    public Map<String, String> getExplains() {
        Map<String, String> ret = new HashMap();
        if (getPrimerLeftExplain() != null && !getPrimerLeftExplain().isEmpty()) {
            ret.put("LEFT", getPrimerLeftExplain());
        }
        if (getPrimerRightExplain() != null && !getPrimerRightExplain().isEmpty()) {
            ret.put("RIGHT", getPrimerRightExplain());
        }
        if (getPrimerPairExplain() != null && !getPrimerPairExplain().isEmpty()) {
            ret.put("PAIR", getPrimerPairExplain());
        }
        if (getPrimerInternalExplain() != null && !getPrimerInternalExplain().isEmpty()) {
            ret.put("INTERNAL", getPrimerInternalExplain());
        }
        return ret;
    }

    public void clear() {
        primerLeftExplain = "";
        primerInternalExplain = "";
        primerRightExplain = "";
        primerPairExplain = "";
        oligos.clear();
    }

    public String getPrimerLeftExplain() {
        return primerLeftExplain;
    }

    public void setPrimerLeftExplain(String primerLeftExplain) {
        this.primerLeftExplain = primerLeftExplain;
    }

    public String getPrimerRightExplain() {
        return primerRightExplain;
    }

    public String getPrimerInternalExplain() {
        return primerInternalExplain;
    }

    public void setPrimerInternalExplain(String primerInternalExplain) {
        this.primerInternalExplain = primerInternalExplain;
    }

    public void setPrimerRightExplain(String primerRightExplain) {
        this.primerRightExplain = primerRightExplain;
    }

    public Integer getInternalExcludedStart() {
        return internalExcludedStart;
    }

    public void setInternalExcludedStart(Integer internalExcludedStart) {
        this.internalExcludedStart = internalExcludedStart;
    }

    public Integer getInternalExcludedLength() {
        return internalExcludedLength;
    }

    public void setInternalExcludedLength(Integer internalExcludedLength) {
        this.internalExcludedLength = internalExcludedLength;
    }

    public Integer getTargetStart() {
        return targetStart;
    }

    public void setTargetStart(Integer targetStart) {
        this.targetStart = targetStart;
    }

    public Integer getTargetLength() {
        return targetLength;
    }

    public void setTargetLength(Integer targetLength) {
        this.targetLength = targetLength;
    }

    public String getPrimerPairExplain() {
        return primerPairExplain;
    }

    public void setPrimerPairExplain(String primerPairExplain) {
        this.primerPairExplain = primerPairExplain;
    }

    public List<Oligo> getSortedOligos() {
        List<Oligo> ret = new ArrayList<Oligo>();
        ret.addAll(oligos);
        Collections.sort(ret, new Oligo.NoComparator());
        return ret;
    }

    public Set<Oligo> getOligos() {
        return oligos;
    }

    public Set<Oligo> getAdditionalOligos() {
        Set<Oligo> ret = new HashSet<Oligo>();
        if (!oligos.isEmpty()) {
            ret.addAll(oligos);
            Oligo oligo = getOligoByNo(0);
            if (oligo != null) {
                ret.remove(oligo);
            }
        }
        return ret;
    }

    public void setOligos(Set<Oligo> oligos) {
        this.oligos = oligos;
    }

    /**
     * @param no: 0-based
     */
    public Oligo getOligoByNo(int no) {
        Oligo ret = null;
        Iterator<Oligo> itr = oligos.iterator();
        while (itr.hasNext()) {
            Oligo oligo = itr.next();
            if (oligo.getNo().equals(no)) {
                ret = oligo;
                break;
            }
        }
        return ret;
    }
    
    /**
     * @param no: 0-based
     */
    public OligoElement getOligoElement(int no, Oligo.WHICH which){
        OligoElement ret = null;
        Iterator<Oligo> itr = oligos.iterator();
        while(itr.hasNext()){
            Oligo oligo = itr.next();
            if(no ==oligo.getNo().intValue()){
                if(which == Oligo.WHICH.left){
                    ret = oligo.getLeft();
                }else if(which == Oligo.WHICH.right){
                    ret = oligo.getRight();
                }else if(which == Oligo.WHICH.internal){
                    ret = oligo.getInternal();
                }
                break;
            }
        }
        return ret;
    }

    public int getOligoSize() {
        return oligos.size();
    }

    public void touchAll() {
        Iterator<Oligo> itr = oligos.iterator();
        while (itr.hasNext()) {
            itr.next().touchAll();
        }
        if (userInputs != null && !userInputs.isEmpty()) {
            for (UserInput userInput : userInputs) {
                userInput.touchAll();
            }
        }
    }

    public List<Feture> toFetures(Integer totalPos, Boolean circular) {
        List<Feture> ret = P3OutputHelper.toFetures(this, totalPos, circular);
        return ret;
    }
}
