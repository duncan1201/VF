/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.common.ui.misc.DateTimeStringValue;
import com.gas.common.ui.misc.EmptyStringValue;
import com.gas.common.ui.misc.TypeIconValue;
import com.gas.common.ui.misc.WrappingProvider2;
import com.gas.domain.core.IFolderElement;
import java.net.URI;
import org.jdesktop.swingx.renderer.StringValue;
import java.text.DateFormat;
import javax.swing.JLabel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.ToolTipHighlighter;
import org.jdesktop.swingx.renderer.HyperlinkProvider;
import org.jdesktop.swingx.renderer.LabelProvider;

/**
 *
 * @author dunqiang
 */
public class DynamicColumnFactoryCalibrator implements IColumnFactoryCalibrator {

    /**
     * This is data-unrelated. It NEEDs to be invoked before setting the table
     * model
     */
    public void configureDisplayProperties(JXTable table) {
        //<snip> JXTable display properties
        // show column control
        table.setColumnControlVisible(true);
        // replace grid lines with striping 
        table.setShowGrid(false, false);
        table.addHighlighter(HighlighterFactory.createSimpleStriping());
        // initialize preferred size for table's viewable area
        //table.setVisibleRowCount(10);
//        </snip>

        //<snip> JXTable column properties
        // create and configure a custom column factory
        CustomColumnFactory factory = new CustomColumnFactory();
        configureColumnFactory(factory);

        // set the factory before setting the table model
        table.setColumnFactory(factory);
//        </snip>

    }

    @Override
    public void configureColumnFactory(CustomColumnFactory factory) {

        factory.addComponentProvider(DynamicTableModel.ICON_COL, new WrappingProvider2(new TypeIconValue(), new EmptyStringValue()));

        DateTimeStringValue dateStringValue = new DateTimeStringValue(DateFormat.MEDIUM, DateFormat.SHORT);
        factory.addComponentProvider(DynamicTableModel.DATE_COL, new LabelProvider(dateStringValue, JLabel.LEFT));

        factory.addComponentProvider(DynamicTableModel.DOI_COL,
                new HyperlinkProvider(new DoiLinkAction(), String.class));


        factory.addComponentProvider(DynamicTableModel.DOI_COL,
                new HyperlinkProvider(new DoiLinkAction(), String.class));


        factory.addComponentProvider(DynamicTableModel.PDB_ID_COL,
                new HyperlinkProvider(new PDBLinkAction(), String.class));

        factory.addComparator(DynamicTableModel.LENGTH_COL, new Comparators.LengthColumnComparator());

        StringValue toolTip = new StringValue() {
            @Override
            public String getString(Object value) {
                if(value instanceof String){
                    return value.toString();
                }else{                    
                    return value == null ? "": value.toString();
                }
            }
        };
        ToolTipHighlighter tooltipHighlighter = new ToolTipHighlighter(toolTip);        
        factory.addHighlighter(DynamicTableModel.NAME_COL, tooltipHighlighter);
        factory.addHighlighter(DynamicTableModel.DESC_COL, tooltipHighlighter); 
        factory.addHighlighter(DynamicTableModel.ACCESSION_COL, tooltipHighlighter);
        factory.addHighlighter(DynamicTableModel.JOURNAL_TITLE_COL, tooltipHighlighter);
        factory.addHighlighter(DynamicTableModel.CLASSIFICATION_COL, tooltipHighlighter);
        factory.addHighlighter(DynamicTableModel.LENGTH_COL, tooltipHighlighter);
        factory.addHighlighter(DynamicTableModel.FORM_COL, tooltipHighlighter);
                
        factory.addHighlighter(DynamicTableModel.DOI_COL, new ToolTipHighlighter(new FormatStringValue(DoiLinkAction.URL_FORMAT)));
        factory.addHighlighter(DynamicTableModel.PDB_ID_COL, new ToolTipHighlighter(new FormatStringValue(PDBLinkAction.URL_FORMAT)));
       
        factory.addHighlighter(DynamicTableModel.DATE_COL, new ToolTipHighlighter(dateStringValue));
        
    }
}
