package de.applejuicenet.client.gui;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/Attic/DownloadDOOverviewPanel.java,v 1.34 2004/04/14 10:25:22 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: DownloadDOOverviewPanel.java,v $
 * Revision 1.34  2004/04/14 10:25:22  maj0r
 * [Maj0r] Bug #293 gefixt (Danke an dsp2004)
 * Bei Partlistanfragen an einen ueberlasteten Core kam es zu Fehlern.
 *
 * Revision 1.33  2004/03/05 15:49:38  maj0r
 * PMD-Optimierung
 *
 * Revision 1.32  2004/03/03 15:33:30  maj0r
 * PMD-Optimierung
 *
 * Revision 1.31  2004/02/26 10:38:26  maj0r
 * Partlistverwendung auf Singleton geaendert.
 *
 * Revision 1.30  2004/02/24 14:22:34  maj0r
 * Bug #242 gefixt (Danke an Kossi-Jaki)
 * Legende f�r Partliste um "aktive Uebertragung" erweitert.
 *
 * Revision 1.29  2004/02/21 18:20:30  maj0r
 * LanguageSelector auf SAX umgebaut.
 *
 * Revision 1.28  2004/02/18 18:43:04  maj0r
 * Von DOM auf SAX umgebaut.
 *
 * Revision 1.27  2004/02/15 18:43:44  maj0r
 * Ueberzeichnung der Partliste durch alten, noch aktiven Thread behoben.
 *
 * Revision 1.26  2004/02/12 16:36:38  maj0r
 * Aktualisierungintervall auf 2 Sekunden ge�ndert.
 *
 * Revision 1.25  2004/02/05 23:11:26  maj0r
 * Formatierung angepasst.
 *
 * Revision 1.24  2004/01/06 16:03:49  maj0r
 * Bug #13 umgesetzt (Danke an HabkeineMail)
 * Powerdownload-Werte werden jetzt bei Klick auf einen Download / Quelle im Powerdownloadfeld angezeigt.
 *
 * Revision 1.23  2004/01/06 16:03:00  maj0r
 * Bug #13 umgesetzt (Danke an HabkeineMail)
 * Powerdownload-Werte werden jetzt bei Klick auf einen Download / Quelle im Powerdownloadfeld angezeigt.
 *
 * Revision 1.22  2003/12/29 16:04:17  maj0r
 * Header korrigiert.
 *
 * Revision 1.21  2003/12/19 14:27:16  maj0r
 * Dau-Button zum Anzeigen der Partliste eingebaut.
 *
 * Revision 1.20  2003/12/16 09:06:40  maj0r
 * Partliste wird nun erst nach 2 Sekunden Wartezeit geholt, um ein erneutes Klicken behandeln zu k�nnen.
 *
 * Revision 1.19  2003/10/21 14:08:45  maj0r
 * Mittels PMD Code verschoenert, optimiert.
 *
 * Revision 1.18  2003/10/12 15:57:55  maj0r
 * Kleinere Bugs behoben.
 * Sortiert wird nun nur noch bei Klick auf den Spaltenkopf um CPU-Zeit zu sparen.
 *
 * Revision 1.17  2003/10/04 15:30:26  maj0r
 * Userpartliste hinzugefuegt.
 *
 * Revision 1.16  2003/09/09 12:28:14  maj0r
 * Wizard fertiggestellt.
 *
 * Revision 1.15  2003/09/04 10:13:49  maj0r
 * Logger eingebaut.
 *
 * Revision 1.14  2003/09/04 06:26:49  maj0r
 * Partlist korrigiert. Wird momentan beim Resize nicht neugezeichnet.
 *
 * Revision 1.13  2003/09/03 10:26:07  maj0r
 * NullPointer behoben und Logging eingefuehrt.
 *
 * Revision 1.12  2003/09/02 19:29:26  maj0r
 * Einige Stellen synchronisiert und Nullpointer behoben.
 * Version 0.21 beta.
 *
 * Revision 1.11  2003/09/01 18:00:15  maj0r
 * Wo es ging, DO auf primitiven Datentyp umgebaut.
 * Status "geprueft" eingefuehrt.
 *
 * Revision 1.10  2003/09/01 06:27:35  maj0r
 * Ueberarbeitet.
 *
 * Revision 1.9  2003/08/24 14:59:59  maj0r
 * Version 0.14
 * Diverse Aenderungen.
 *
 * Revision 1.8  2003/08/22 12:40:19  maj0r
 * Zeitaufwendiges Partliste holen in Thread ausgelagert.
 *
 * Revision 1.7  2003/08/16 17:49:55  maj0r
 * Diverse Farben k�nnen nun manuell eingestellt bzw. deaktiviert werden.
 * DownloaduebersichtTabelle kann deaktiviert werden.
 *
 * Revision 1.6  2003/08/15 14:46:30  maj0r
 * Refactoring.
 *
 * Revision 1.5  2003/08/12 16:23:36  maj0r
 * Kleine Layoutaenderung.
 *
 * Revision 1.4  2003/08/12 11:01:58  maj0r
 * Anzeige korrigiert.
 *
 * Revision 1.3  2003/08/11 18:19:43  maj0r
 * Korrektur: GridBagLayout kann nur 512 Components pro Zeile.
 *
 * Revision 1.2  2003/08/11 15:34:45  maj0r
 * Diverse �nderungen.
 *
 * Revision 1.1  2003/08/11 14:10:27  maj0r
 * DownloadPartList eingef�gt.
 * Diverse �nderungen.
 *
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import de.applejuicenet.client.gui.controller.ApplejuiceFassade;
import de.applejuicenet.client.gui.controller.LanguageSelector;
import de.applejuicenet.client.gui.listener.LanguageListener;
import de.applejuicenet.client.shared.ZeichenErsetzer;
import de.applejuicenet.client.shared.dac.DownloadDO;
import de.applejuicenet.client.shared.dac.DownloadSourceDO;
import de.applejuicenet.client.shared.dac.PartListDO;
import de.applejuicenet.client.shared.exception.WebSiteNotFoundException;

public class DownloadDOOverviewPanel
    extends JPanel
    implements LanguageListener{
    private DownloadPartListPanel actualDlOverviewTable;
    private JLabel actualDLDateiName = new JLabel();
    private JLabel label5 = new JLabel("aktive �bertragung");
    private JLabel label4 = new JLabel("Vorhanden");
    private JLabel label3 = new JLabel("Nicht vorhanden");
    private JLabel label2 = new JLabel("In Ordnung");
    private JLabel label1 = new JLabel("�berpr�ft");
    private Logger logger;
    private JButton holeListe = new JButton("Hole Partliste");
    private Thread partListWorkerThread = null;
    private DownloadPanel downloadPanel;

    public DownloadDOOverviewPanel(DownloadPanel parent) {
        logger = Logger.getLogger(getClass());
        try {
            downloadPanel = parent;
            actualDlOverviewTable = DownloadPartListPanel.getInstance();
            init();
            LanguageSelector.getInstance().addLanguageListener(this);
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
            }
        }
    }

    public void enableHoleListButton(boolean enable) {
        holeListe.setEnabled(enable);
    }

    private void init() {
        holeListe.setEnabled(false);
        setLayout(new BorderLayout());
        JPanel tempPanel1 = new JPanel();
        tempPanel1.setLayout(new FlowLayout());

        JLabel gelb = new JLabel("     ");
        gelb.setOpaque(true);
        gelb.setBackground(Color.YELLOW);
        tempPanel1.add(gelb);
        tempPanel1.add(label5);

        JLabel blau = new JLabel("     ");
        blau.setOpaque(true);
        blau.setBackground(Color.BLUE);
        tempPanel1.add(blau);
        tempPanel1.add(label4);

        JLabel red = new JLabel("     ");
        red.setOpaque(true);
        red.setBackground(Color.RED);
        tempPanel1.add(red);
        tempPanel1.add(label3);

        JLabel black = new JLabel("     ");
        black.setOpaque(true);
        black.setBackground(Color.BLACK);
        tempPanel1.add(black);
        tempPanel1.add(label2);

        JLabel green = new JLabel("     ");
        green.setOpaque(true);
        green.setBackground(Color.GREEN);
        tempPanel1.add(green);
        tempPanel1.add(label1);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(holeListe, BorderLayout.WEST);
        panel3.add(tempPanel1, BorderLayout.CENTER);

        add(panel3, BorderLayout.NORTH);
        actualDLDateiName.setPreferredSize(new Dimension(actualDLDateiName.
            getPreferredSize().width, 17));
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(actualDLDateiName, BorderLayout.NORTH);
        panel1.add(actualDlOverviewTable, BorderLayout.CENTER);
        add(panel1, BorderLayout.CENTER);
        holeListe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                holeListe.setEnabled(false);
                downloadPanel.tryGetPartList();
            }
        });
    }

    public void setDownloadDO(DownloadDO downloadDO) {
        try {
            if (downloadDO == null) {
                if (partListWorkerThread != null){
                    partListWorkerThread.interrupt();
                    partListWorkerThread = null;
                }
                actualDLDateiName.setText("");
                actualDlOverviewTable.setPartList(null);
            }
            else if (downloadDO.getStatus() != DownloadDO.FERTIGSTELLEN &&
                     downloadDO.getStatus() != DownloadDO.FERTIG) {
                final DownloadDO tempDO = downloadDO;
                if (partListWorkerThread != null){
                    partListWorkerThread.interrupt();
                }
                partListWorkerThread = new Thread() {
                    public void run() {
                        actualDLDateiName.setText(" " + tempDO.getFilename() +
                                                  " (" +
                                                  tempDO.getTemporaryFileNumber() +
                                                  ".data)");
                        actualDlOverviewTable.setPartList(null);
                        PartListDO partList = null;
                        while (!isInterrupted()) {
                            try{
                                partList = ApplejuiceFassade.getInstance().
                                    getPartList(tempDO);
                            }
                            catch(WebSiteNotFoundException wsnfE){
                                // Core ist wahrscheinlich zurzeit ueberlastet
                                partList = null;
                            }
                            if (isInterrupted()){
                                break;
                            }
                            if (partList == null) {
                                interrupt();
                                actualDLDateiName.setText("");
                                actualDlOverviewTable.setPartList(null);
                            }
                            else {
                                actualDlOverviewTable.setPartList(partList);
                                try {
                                    sleep(2000);
                                }
                                catch (InterruptedException iE) {
                                    interrupt();
                                }
                            }
                        }
                    }
                };
                partListWorkerThread.start();
            }
        }
        catch (Exception e) {
            if (partListWorkerThread != null){
                partListWorkerThread.interrupt();
                partListWorkerThread = null;
            }
            actualDLDateiName.setText("");
            actualDlOverviewTable.setPartList(null);
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
            }
        }
    }

    public void setDownloadSourceDO(DownloadSourceDO downloadSourceDO) {
        try {
            if (downloadSourceDO == null) {
                if (partListWorkerThread != null){
                    partListWorkerThread.interrupt();
                    partListWorkerThread = null;
                }
                actualDLDateiName.setText("");
                actualDlOverviewTable.setPartList(null);
            }
            else {
                final DownloadSourceDO tempDO = downloadSourceDO;
                if (partListWorkerThread != null){
                    partListWorkerThread.interrupt();
                }
                partListWorkerThread = new Thread() {
                    public void run() {
                        actualDLDateiName.setText(tempDO.getFilename() + " (" +
                                                  tempDO.getNickname() + ")");
                        actualDlOverviewTable.setPartList(null);
                        PartListDO partList;
                        try {
                            partList = ApplejuiceFassade.getInstance().
                                getPartList(tempDO);
                        }
                        catch (WebSiteNotFoundException ex) {
                            // Core ist wahrscheinlich zurzeit ueberlastet
                            partList = null;
                        }
                        if (partList == null) {
                            interrupt();
                            actualDLDateiName.setText("");
                            actualDlOverviewTable.setPartList(null);
                        }
                        else {
                            actualDlOverviewTable.setPartList(partList);
                        }
                    }
                };
                partListWorkerThread.start();
            }
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
            }
        }
    }

    public void fireLanguageChanged() {
        try {
            LanguageSelector languageSelector = LanguageSelector.getInstance();
            label5.setText(ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.javagui.downloadform.aktiveuebertragung")));
            label4.setText(ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.Label4.caption")));
            label3.setText(ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.Label3.caption")));
            label2.setText(ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.Label2.caption")));
            label1.setText(ZeichenErsetzer.korrigiereUmlaute(languageSelector.
                getFirstAttrbuteByTagName(".root.mainform.Label1.caption")));
            holeListe.setText(ZeichenErsetzer.korrigiereUmlaute(
                languageSelector.
                getFirstAttrbuteByTagName(".root.javagui.downloadform.partlisteanzeigen")));
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
            }
        }
    }
}
