package de.applejuicenet.client.gui.tables.download;

import de.applejuicenet.client.gui.controller.*;
import de.applejuicenet.client.shared.*;
import de.applejuicenet.client.gui.tables.AbstractTreeTableModel;
import de.applejuicenet.client.gui.tables.TreeTableModel;
import de.applejuicenet.client.gui.listener.LanguageListener;
import de.applejuicenet.client.shared.dac.*;
import de.applejuicenet.client.gui.trees.WaitNode;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/tables/download/Attic/DownloadModel.java,v 1.25 2003/12/30 13:55:20 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: DownloadModel.java,v $
 * Revision 1.25  2003/12/30 13:55:20  maj0r
 * Neuen DownloadSourceStatus indirekteVerbindungAbgelehnt eingebaut.
 *
 * Revision 1.24  2003/12/29 16:04:17  maj0r
 * Header korrigiert.
 *
 * Revision 1.23  2003/12/28 10:49:22  maj0r
 * Parsing von Speichergroessen static gemacht.
 *
 * Revision 1.22  2003/12/17 17:03:37  maj0r
 * In der Downloadtabelle nun ein Warteicon angezeigt, bis erstmalig Daten geholt wurden.
 *
 * Revision 1.21  2003/12/16 14:52:16  maj0r
 * An Schnittstellenerweiterung angepasst.
 *
 * Revision 1.20  2003/11/03 20:57:03  maj0r
 * Sortieren nach Status eingebaut.
 *
 * Revision 1.19  2003/11/03 15:45:26  maj0r
 * Optimierungen.
 *
 * Revision 1.18  2003/10/21 14:08:45  maj0r
 * Mittels PMD Code verschoenert, optimiert.
 *
 * Revision 1.17  2003/10/18 18:44:16  maj0r
 * Neuen Userstatus "Warteschlange voll" hinzugefuegt.
 *
 * Revision 1.16  2003/10/16 12:06:51  maj0r
 * Diverse Schoenheitskorrekturen.
 *
 * Revision 1.15  2003/10/06 12:08:18  maj0r
 * Tabellenausgabe korrigiert.
 *
 * Revision 1.14  2003/09/03 10:29:16  maj0r
 * Statusausgabe geaendert.
 *
 * Revision 1.13  2003/09/02 16:06:26  maj0r
 * Downloadbaum komplett umgebaut.
 *
 * Revision 1.12  2003/09/01 15:50:51  maj0r
 * Wo es moeglich war, DOs auf primitive Datentypen umgebaut.
 *
 * Revision 1.11  2003/08/30 19:44:04  maj0r
 * Ausgabe geaendert.
 *
 * Revision 1.10  2003/08/25 09:28:13  maj0r
 * getChildCount() eingefuehrt.
 *
 * Revision 1.9  2003/08/18 18:19:18  maj0r
 * DownloadStatus in der Anzeige verfeinert.
 *
 * Revision 1.8  2003/08/10 21:08:18  maj0r
 * Diverse �nderungen.
 *
 * Revision 1.7  2003/08/09 10:56:38  maj0r
 * DownloadTabelle weitergef�hrt.
 *
 * Revision 1.6  2003/07/06 20:00:19  maj0r
 * DownloadTable bearbeitet.
 *
 * Revision 1.5  2003/07/04 15:25:38  maj0r
 * Version erh�ht.
 * DownloadModel erweitert.
 *
 * Revision 1.4  2003/07/03 19:11:16  maj0r
 * DownloadTable �berarbeitet.
 *
 * Revision 1.3  2003/07/02 13:54:34  maj0r
 * JTreeTable komplett �berarbeitet.
 *
 * Revision 1.2  2003/07/01 18:41:39  maj0r
 * Struktur ver�ndert.
 *
 * Revision 1.1  2003/07/01 18:34:28  maj0r
 * Struktur ver�ndert.
 *
 * Revision 1.17  2003/06/10 12:31:03  maj0r
 * Historie eingef�gt.
 *
 *
 */

public class DownloadModel
    extends AbstractTreeTableModel implements LanguageListener{

  static protected String[] cNames = {"", "", "", "", "", "", "", "", "", ""};

  //Download-Stati
  private static String suchen = "";
  private static String laden = "";
  private static String keinPlatz = "";
  private static String fertigstellen = "";
  private static String fehlerBeimFertigstellen = "";
  private static String fertig = "";
  private static String abbrechen = "";
  private static String abgebrochen = "";

  //Source-Stati
  private static String ungefragt = "";
  private static String versucheZuVerbinden = "";
  private static String ggstZuAlteVersion = "";
  private static String kannDateiNichtOeffnen = "";
  private static String warteschlange = "";
  private static String keineBrauchbarenParts = "";
  private static String uebertragung = "";
  private static String nichtGenugPlatz = "";
  private static String fertiggestellt = "";
  private static String keineVerbindungMoeglich = "";
  private static String pausiert = "";
  private static String position = "";
  private static String versucheIndirekt = "";
  private static String warteschlangeVoll = "";
  private static String eigenesLimitErreicht = "";
  private static String indirekteVerbindungAbgelehnt = "";

  static protected Class[] cTypes = {
      TreeTableModel.class, String.class, String.class, String.class, String.class,
      String.class, String.class, String.class, String.class, String.class};

  public DownloadModel() {
    super(new DownloadRootNode());
    LanguageSelector.getInstance().addLanguageListener(this);
  }

  protected Object[] getChildren(Object node) {
      if (node.getClass()!=DownloadDO.class && node.getClass()!=DownloadSourceDO.class
          && node.getClass()!=WaitNode.class){
          return ((DownloadNode)node).getChildren();
      }
      else if (node.getClass()==DownloadDO.class){
          return ((DownloadDO)node).getSources();
      }
      return null;
  }

  public int getChildCount(Object node) {
      if (node.getClass()!=DownloadDO.class && node.getClass()!=DownloadSourceDO.class
          && node.getClass()!=WaitNode.class){
          return ((DownloadNode)node).getChildCount();
      }
      else if (node.getClass()==DownloadDO.class){
          return ((DownloadDO)node).getSources().length;
      }
      return 0;
  }

  public Object getChild(Object node, int i) {
      Object[] obj = getChildren(node);
      if (obj==null || i>obj.length-1)
        return null;
      return obj[i];
  }

  public int getColumnCount() {
    return cNames.length;
  }

  public String getColumnName(int column) {
    return cNames[column];
  }

  public Class getColumnClass(int column) {
    return cTypes[column];
  }

  public Object getValueAt(Object node, int column) {
    if (node.getClass()==DownloadMainNode.class){
        if (((DownloadMainNode)node).getType()==DownloadMainNode.ROOT_NODE){
            DownloadDO downloadDO = ((DownloadMainNode)node).getDownloadDO();
            if (downloadDO.getStatus()==DownloadDO.SUCHEN_LADEN){
                switch (column) {
                  case 0:
                        return downloadDO.getFilename();
                  case 1:
                        return getStatus(downloadDO);
                  case 2:
                        return DownloadModel.parseGroesse(downloadDO.getGroesse());
                  case 3:
                        return DownloadModel.parseGroesse(downloadDO.getBereitsGeladen());
                  case 4:
                        return getSpeedAsString(downloadDO.getSpeedInBytes());
                  case 5:
                        return downloadDO.getRestZeitAsString();
                  case 7:
                        return DownloadModel.parseGroesse(downloadDO.getGroesse()-downloadDO.getBereitsGeladen());
                  case 8:
                        return powerdownload(downloadDO.getPowerDownload());
                  default:
                    return "";
                }
            }
            else{
                switch (column) {
                  case 0:
                        return downloadDO.getFilename();
                  case 1:
                        return getStatus(downloadDO);
                  case 2:
                        return DownloadModel.parseGroesse(downloadDO.getGroesse());
                  case 3:
                        return DownloadModel.parseGroesse(downloadDO.getBereitsGeladen());
                  case 7:
                        if (downloadDO.getStatus()==DownloadDO.PAUSIERT){
                            return DownloadModel.parseGroesse(downloadDO.getGroesse()-downloadDO.getBereitsGeladen());
                        }
                        else{
                            return "";
                        }
                  case 8:
                        if (downloadDO.getStatus()==DownloadDO.PAUSIERT){
                            return powerdownload(downloadDO.getPowerDownload());
                        }
                        else{
                            return "";
                        }
                  default:
                    return "";
                }
            }
        }
        else{
            return "";
        }
    }
    else if (node.getClass()==DownloadDirectoryNode.class){
          switch (column) {
            case 0:
                return ((DownloadDirectoryNode)node).getVerzeichnis();
            default:
              return "";
          }
    }
    else if (node.getClass()==DownloadSourceDO.class){
        DownloadSourceDO downloadSourceDO = (DownloadSourceDO) node;
        if (downloadSourceDO == null) {
          return "";
        }
          switch (column) {
            case 0:
                return downloadSourceDO.getFilename();
            case 1:
                return getStatus(downloadSourceDO);
            case 2:
                return DownloadModel.parseGroesse(downloadSourceDO.getSize());
            case 3:
                return DownloadModel.parseGroesse(downloadSourceDO.getBereitsGeladen());
            case 4:
                  {
                      if (downloadSourceDO.getStatus()!=DownloadSourceDO.UEBERTRAGUNG)
                          return "";
                      else
                          return getSpeedAsString((long)downloadSourceDO.getSpeed());
                  }
            case 5:
                  return downloadSourceDO.getRestZeitAsString();
            case 6:
                  break;
            case 7:
                  return DownloadModel.parseGroesse(downloadSourceDO.getNochZuLaden());
            case 8:
                  return powerdownload(downloadSourceDO.getPowerDownload());
            case 9:
              if (downloadSourceDO.getVersion() != null) {
                return downloadSourceDO.getVersion().getVersion();
              }
              else {
                return "";
              }
            default:
              return "";
          }
    }
    return null;
  }

    private String getStatus(Object objectDO){
        if (objectDO.getClass()==DownloadDO.class){
            return DownloadModel.getStatusForDownload((DownloadDO)objectDO);
        }
        else if (objectDO.getClass()==DownloadSourceDO.class){
            return DownloadModel.getStatusForSource((DownloadSourceDO)objectDO);
        }
        else
            return "";
    }

    public static String getStatusForSource(DownloadSourceDO downloadSourceDO){
        switch(downloadSourceDO.getStatus()){
            case DownloadSourceDO.UNGEFRAGT:
                    return ungefragt;
            case DownloadSourceDO.VERSUCHE_ZU_VERBINDEN:
                    return versucheZuVerbinden;
            case DownloadSourceDO.GEGENSTELLE_HAT_ZU_ALTE_VERSION:
                    return ggstZuAlteVersion;
            case DownloadSourceDO.GEGENSTELLE_KANN_DATEI_NICHT_OEFFNEN:
                    return kannDateiNichtOeffnen;
            case DownloadSourceDO.IN_WARTESCHLANGE:
                {
                    String temp = position;
                    temp = temp.replaceFirst("%d", Integer.toString(downloadSourceDO.getQueuePosition()));
                    return warteschlange + " " + temp;
                }
            case DownloadSourceDO.KEINE_BRAUCHBAREN_PARTS:
                    return keineBrauchbarenParts;
            case DownloadSourceDO.UEBERTRAGUNG:
                    return uebertragung;
            case DownloadSourceDO.NICHT_GENUEGEND_PLATZ_AUF_DER_PLATTE:
                    return nichtGenugPlatz;
            case DownloadSourceDO.FERTIGGESTELLT:
                    return fertiggestellt;
            case DownloadSourceDO.KEINE_VERBINDUNG_MOEGLICH:
                    return keineVerbindungMoeglich;
            case DownloadSourceDO.PAUSIERT:
                    return pausiert;
            case DownloadSourceDO.VERSUCHE_INDIREKT:
                    return versucheIndirekt;
            case DownloadSourceDO.WARTESCHLANGE_VOLL:
                    return warteschlangeVoll;
            case DownloadSourceDO.EIGENES_LIMIT_ERREICHT:
                    return eigenesLimitErreicht;
            case DownloadSourceDO.INDIREKTE_VERBINDUNG_ABGELEHNT:
                    return indirekteVerbindungAbgelehnt;

            default:
                return "";
        }
    }

    public static String getStatusForDownload(DownloadDO downloadDO){
        switch(downloadDO.getStatus()){
            case DownloadDO.PAUSIERT:
                return pausiert;
            case DownloadDO.ABBRECHEN:
                return abbrechen;
            case DownloadDO.AGBEGROCHEN:
                return abgebrochen;
            case DownloadDO.FERTIG:
                return fertig;
            case DownloadDO.FEHLER_BEIM_FERTIGSTELLEN:
                return fehlerBeimFertigstellen;
            case DownloadDO.NICHT_GENUG_PLATZ_FEHLER:
                return keinPlatz;
            case DownloadDO.SUCHEN_LADEN:
                {
                    DownloadSourceDO[] sources = downloadDO.getSources();
                    String result = "";
                    int uebertragung = 0;
                    int warteschlange = 0;
                    int status;
                    for (int i=0; i<sources.length; i++){
                        status = sources[i].getStatus();
                        if (status==DownloadSourceDO.UEBERTRAGUNG){
                            uebertragung++;
                            result =  laden;
                        }
                        else if (status==DownloadSourceDO.IN_WARTESCHLANGE){
                            warteschlange++;
                        }
                    }
                    if (result.length()==0)
                        result = suchen;
                    return result + " " + (warteschlange + uebertragung) + "/" + sources.length + " (" + uebertragung +")";
                }
            case DownloadDO.FERTIGSTELLEN:
                return fertigstellen;
            default:
                return "";
        }
    }

    private String powerdownload(int pwdl){
        if (pwdl==0)
            return "1:1,0";
        double power = pwdl;
        power = power / 10 + 1;
        String temp = Double.toString(power);
        temp = temp.replace('.', ',');
        return "1:" + temp;
    }

    public static String parseGroesse(long groesse){
        double share = Double.parseDouble(Long.toString(groesse));
        int faktor;
        if (share == 0) {
          return "";
        }
        if (share < 1024) {
          return groesse + " Bytes";
        }
        else if (share / 1024 < 1024) {
          faktor = 1024;
        }
        else if (share / 1048576 < 1024) {
          faktor = 1048576;
        }
        else if (share / 1073741824 < 1024) {
          faktor = 1073741824;
        }
        else {
          faktor = 1;
        }
        share = share / faktor;
        String result = Double.toString(share);
        if (result.indexOf(".") + 3 < result.length())
        {
            result = result.substring(0, result.indexOf(".") + 3);
        }
        result = result.replace('.', ',');
        if (faktor == 1024) {
          result += " KB";
        }
        else if (faktor == 1048576) {
          result += " MB";
        }
        else if (faktor == 1073741824) {
          result += " GB";
        }
        else {
          result += " ??";
        }
        return result;
    }

    private String getSpeedAsString(long speed){
        if (speed==0)
            return "0 Bytes/s";
        double size = speed;
        int faktor = 1;
        if (size < 1024)
            faktor = 1;
        else
            faktor = 1024;

        size = size / faktor;
        String s = Double.toString(size);
        if (s.indexOf(".") + 3 < s.length()){
            s = s.substring(0, s.indexOf(".") + 3);
        }
        if (faktor==1){
            s += " Bytes/s";
        }
        else{
            s += " kb/s";
        }
        return s;
    }

    public void fireLanguageChanged() {
        LanguageSelector languageSelector = LanguageSelector.getInstance();
        ungefragt = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat1"}));
        versucheZuVerbinden = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat2"}));
        ggstZuAlteVersion = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat3"}));
        kannDateiNichtOeffnen = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat4"}));
        warteschlange = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat5"}));
        keineBrauchbarenParts = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat6"}));
        uebertragung = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat7"}));
        nichtGenugPlatz = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat8"}));
        fertiggestellt = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestat14"}));
        keineVerbindungMoeglich = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", ""}));
        pausiert = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat13"}));
        position = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat51"}));
        versucheIndirekt = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "userstat10"}));
        eigenesLimitErreicht = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"javagui", "downloadform", "eigeneslimiterreicht"}));
        indirekteVerbindungAbgelehnt = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"javagui", "downloadform", "indverbindungabgelehnt"}));

        suchen = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestatlook"}));
        laden = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestattransfer"}));
        keinPlatz = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestat1"}));
        fertigstellen = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestat12"}));
        fertig = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestat14"}));
        abbrechen = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestat15"}));
        abgebrochen = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"mainform", "queue", "queuestat17"}));
        warteschlangeVoll = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"javagui", "downloadform", "warteschlangevoll"}));
        fehlerBeimFertigstellen = ZeichenErsetzer.korrigiereUmlaute(languageSelector.getFirstAttrbuteByTagName(new String[] {"javagui", "downloadform", "fehlerbeimfertigstellen"}));
    }
}