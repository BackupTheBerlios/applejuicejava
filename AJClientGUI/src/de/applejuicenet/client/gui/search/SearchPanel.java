package de.applejuicenet.client.gui.search;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.applejuicenet.client.fassade.ApplejuiceFassade;
import de.applejuicenet.client.fassade.shared.ZeichenErsetzer;
import de.applejuicenet.client.gui.RegisterI;
import de.applejuicenet.client.gui.components.GuiController;
import de.applejuicenet.client.gui.components.TklPanel;
import de.applejuicenet.client.gui.controller.LanguageSelector;
import de.applejuicenet.client.gui.listener.LanguageListener;
import de.tklsoft.gui.controls.TKLButton;
import de.tklsoft.gui.controls.TKLLabel;
import de.tklsoft.gui.controls.TKLTextField;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/search/SearchPanel.java,v 1.10 2005/03/04 13:47:53 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r [aj@tkl-soft.de]
 *
 */

public class SearchPanel
    extends TklPanel
    implements LanguageListener, RegisterI {

    private SearchResultTabbedPane resultPanel = new SearchResultTabbedPane();
    private TKLButton btnStartStopSearch = new TKLButton();
    private TKLTextField suchbegriff = new TKLTextField();
    private TKLLabel suchen = new TKLLabel();
    private TKLLabel bearbeitung = new TKLLabel();
    private Logger logger;

    public SearchPanel(GuiController guiController) {
        super(guiController);
        logger = Logger.getLogger(getClass());
        try {
            init();
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
            }
        }
    }
    
    public TKLButton getStartStopBtn(){
        return btnStartStopSearch;       
    }

    public TKLTextField getSuchbegriffTxt(){
        return suchbegriff;       
    }

    public TKLLabel getBearbeitungLbl(){
        return bearbeitung;       
    }
    
    public SearchResultTabbedPane getSearchResultTabbedPane(){
        return resultPanel;      
    }
    
    private void init() throws Exception {
        setLayout(new BorderLayout());
        LanguageSelector.getInstance().addLanguageListener(this);
        JPanel panel3 = new JPanel();
        JPanel leftPanel = new JPanel();
        panel3.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        panel3.add(suchen, constraints);
        constraints.gridx = 1;
        panel3.add(suchbegriff, constraints);
        constraints.gridy = 1;
        panel3.add(btnStartStopSearch, constraints);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.add(bearbeitung);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel3.add(panel2, constraints);
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(panel3, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);

        add(resultPanel, BorderLayout.CENTER);

        suchbegriff.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnStartStopSearch.doClick();
                }
            }
        });
    }

    public void fireLanguageChanged() {
        try {
            LanguageSelector languageSelector = LanguageSelector.getInstance();
            suchen.setText(ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.searchlbl.caption")) + ": ");
            btnStartStopSearch.setText(ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.searchbtn.searchcaption")));

            String[] resultTexte = new String[9];
            resultTexte[0] = (ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.javagui.searchform.offenesuchen")));
            resultTexte[1] = (ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.javagui.searchform.gefundenedateien")));
            resultTexte[2] = (ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.javagui.searchform.durchsuchteclients")));
            resultTexte[3] = (ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.Getlink3.caption")));
            resultTexte[4] = (ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.cancelsearch.caption")));            
            resultTexte[5] = ZeichenErsetzer.korrigiereUmlaute(languageSelector
    				.getFirstAttrbuteByTagName(".root.javagui.downloadform.bereitsgeladen"));
            resultTexte[6] = ZeichenErsetzer.korrigiereUmlaute(languageSelector
    				.getFirstAttrbuteByTagName(".root.javagui.downloadform.falscherlink"));
            resultTexte[7] = ZeichenErsetzer.korrigiereUmlaute(languageSelector
    				.getFirstAttrbuteByTagName(".root.javagui.downloadform.sonstigerlinkfehlerlang"));
            resultTexte[8] = ZeichenErsetzer.korrigiereUmlaute(languageSelector
    				.getFirstAttrbuteByTagName(".root.mainform.caption"));

            
            String[] columns = new String[3];
            columns[0] = ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.searchs.col0caption"));
            columns[1] = ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.searchs.col1caption"));
            columns[2] = ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.searchs.col2caption"));

            SearchResultPanel.setTexte(resultTexte, columns);

            for (int i = 0; i < resultPanel.getComponentCount(); i++) {
                ( (SearchResultPanel) resultPanel.getComponentAt(i)).
                    aendereSprache();
            }
        }
        catch (Exception e) {
            logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
        }
    }
}
