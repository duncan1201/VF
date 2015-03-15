/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.util.api;

import java.util.List;

/**
 *
 * @author dq
 */
public interface IEFetchService {
    public enum RETTYPE {

        _abstract("abstract"), 
        docsum("docsum"),
        full_record("native"), 
        fasta("fasta"),
        gb("gb"), 
        gp("gp"), 
        gbwithparts("gbwithparts"), 
        gbc("gbc"),
        gpc("gpc"), 
        est("est"), 
        gss("gss"), 
        seqid("seqid"), 
        acc("acc"),
        none(""),        
        ft("ft");
        
        private String str;

        RETTYPE(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    };

    public enum RETMODE {

        xml("xml"), html("html"), text("text"), asn1("asn.1"), none("");
        private String str;

        RETMODE(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    };


    String getComplexity();

    String getDb();

    String getEmail();

    String getIds();

    String getRetmax();

    String getRetmode();

    String getRetstart();

    String getRettype();

    String getSeqStart();

    String getSeqStop();

    String getStrand();

    String getTool();

    <T> List<T> sendRequest(Class<T> clazz) ;

    void setComplexity(String complexity);

    void setDb(String db);

    void setEmail(String email);

    void setIds(String id);

    void setRetmax(String retmax);

    void setRetmode(RETMODE retmode);

    void setRetstart(String retstart);

    void setRettype(RETTYPE rettype);

    void setSeqStart(String seq_start);

    void setSeqStop(String seq_stop);

    void setStrand(String strand);

    void setTool(String tool);
    
}
