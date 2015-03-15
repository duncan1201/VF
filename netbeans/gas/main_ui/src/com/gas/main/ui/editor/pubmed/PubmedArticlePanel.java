/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.editor.pubmed;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.pubmed.PubmedArticle;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author dunqiang
 */
public class PubmedArticlePanel extends JPanel{
    
    private PubmedArticle article;
    private JEditorPane titlePane;
    private JLabel journalInfo;
    private JXHyperlink doiLink ;
    private JEditorPane abstractPane;
    private JEditorPane pmidPane;
    
    public PubmedArticlePanel(){
        setLayout(new VerticalLayout(5));
        setBackground(Color.WHITE);
        UIUtil.setLeftInsets(this, 10);
        UIUtil.setRightInsets(this, 40);
        UIUtil.setTopInsets(this, 10);
        
        
        titlePane = new JEditorPane();
        float fontSize = FontUtil.getDefaultMenuFontSize();
        fontSize+=2;
        Font newFont = titlePane.getFont().deriveFont(Font.BOLD).deriveFont(fontSize);
        titlePane.setFont(newFont);
        titlePane.setEditable(false);
        add(titlePane);
        
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linkPanel.setBackground(null);
        fontSize = FontUtil.getDefaultFontSize();
        fontSize++;
        journalInfo = new JLabel();
        newFont = journalInfo.getFont().deriveFont(fontSize);
        journalInfo.setFont(newFont);
        journalInfo.setText("");
        linkPanel.add(journalInfo);
        
        doiLink = new JXHyperlink(new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e) {                
                URI uri = URI.create("http://dx.doi.org/"+getArticle().getDoi());
                CommonUtil.browse(uri);
            }
        });       
        doiLink.setFont(newFont);
        linkPanel.add(doiLink);
        
        add(linkPanel);
        
        fontSize = FontUtil.getDefaultFontSize();
        fontSize+=2;
        abstractPane = new JEditorPane();
        newFont = abstractPane.getFont().deriveFont(fontSize);
        abstractPane.setFont(newFont);               
        abstractPane.setEditable(false);
        add(abstractPane);
        
        pmidPane = new JEditorPane();
        pmidPane.setEditable(false);
        add(pmidPane);
    }
    
    private String createDOILinkText(PubmedArticle article){
        StringBuilder ret = new StringBuilder();   
        String doi = article.getDoi();
        if(doi != null){
            ret.append(" ");
            ret.append("doi");
            ret.append(article.getDoi());
        }
        return ret.toString();        
    }
    
    private String createJournalInfoText(PubmedArticle article){
        StringBuilder ret = new StringBuilder();
        String ISOAbbreviation = article.getISOAbbreviation() ; 
        String journalTitle = article.getJournalTitle();
        ret.append(ISOAbbreviation == null? ISOAbbreviation : journalTitle);
        ret.append(",");
        ret.append("volume ");
        ret.append(article.getVolume());
        ret.append(", ");
        ret.append("pages ");
        ret.append(article.getPagination());
        ret.append(" ");
        ret.append("(");
        ret.append(article.getPubYear());
        ret.append(")");        
 
        return ret.toString();
    }

    public PubmedArticle getArticle() {
        return article;
    }

    public void setArticle(PubmedArticle article) {
        this.article = article;
        titlePane.setText(article.getTitle());
        abstractPane.setText(article.getAbstractTxt());
        String txt = createJournalInfoText(article);
        journalInfo.setText(txt);        
        txt = createDOILinkText(article);
        if(!txt.isEmpty()){
            doiLink.setText(txt);
        }
        pmidPane.setText(String.format("PMID: %s", article.getPmid()));
    }
    
    
    
}
