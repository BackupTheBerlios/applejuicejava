package de.applejuicenet.client.gui.controller;

import java.util.*;

import org.w3c.dom.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import de.applejuicenet.client.shared.*;
import de.applejuicenet.client.shared.dac.*;
import de.applejuicenet.client.gui.listener.LanguageListener;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/controller/Attic/ModifiedXMLHolder.java,v 1.35 2003/09/30 16:35:11 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <AJCoreGUI@maj0r.de>
 *
 * $Log: ModifiedXMLHolder.java,v $
 * Revision 1.35  2003/09/30 16:35:11  maj0r
 * Suche begonnen und auf neues ID-Listen-Prinzip umgebaut.
 *
 * Revision 1.34  2003/09/14 06:37:39  maj0r
 * Moeglichen NullPointer behoben.
 *
 * Revision 1.33  2003/09/13 11:30:41  maj0r
 * Neuen Listener fuer Geschwindigkeitsanzeigen eingebaut.
 *
 * Revision 1.32  2003/09/11 09:41:16  maj0r
 * Nullpointer behoben.
 *
 * Revision 1.31  2003/09/11 06:54:15  maj0r
 * Auf neues Sessions-Prinzip umgebaut.
 * Sprachenwechsel korrigert, geht nun wieder flott.
 *
 * Revision 1.30  2003/09/10 15:30:48  maj0r
 * Begonnen auf neue Session-Struktur umzubauen.
 *
 * Revision 1.29  2003/09/06 08:34:23  maj0r
 * Nullpointer behoben.
 * Dank an Fumpi.
 *
 * Revision 1.28  2003/09/02 19:29:26  maj0r
 * Einige Stellen synchronisiert und Nullpointer behoben.
 * Version 0.21 beta.
 *
 * Revision 1.27  2003/09/01 15:50:51  maj0r
 * Wo es moeglich war, DOs auf primitive Datentypen umgebaut.
 *
 * Revision 1.26  2003/08/31 11:06:22  maj0r
 * CheckInProgress geaendert.
 *
 * Revision 1.25  2003/08/22 10:03:11  maj0r
 * Threadverwendung korrigiert.
 *
 * Revision 1.24  2003/08/21 15:13:29  maj0r
 * Auf Thread umgebaut.
 *
 * Revision 1.23  2003/08/19 15:57:21  maj0r
 * Gesamtgeschwindigkeit wird nun angezeigt.
 *
 * Revision 1.22  2003/08/18 17:11:26  maj0r
 * Alte Uploads wurden nicht entfernt. Korrigiert.
 *
 * Revision 1.21  2003/08/18 14:54:11  maj0r
 * Alte Eintraege loeschen.
 *
 * Revision 1.20  2003/08/16 17:50:06  maj0r
 * Diverse Farben k�nnen nun manuell eingestellt bzw. deaktiviert werden.
 * DownloaduebersichtTabelle kann deaktiviert werden.
 *
 * Revision 1.19  2003/08/15 14:46:30  maj0r
 * Refactoring.
 *
 * Revision 1.18  2003/08/14 20:08:42  maj0r
 * Tree fuer Shareauswahl eingefuegt, aber noch nicht fertiggestellt.
 *
 * Revision 1.17  2003/08/10 21:08:18  maj0r
 * Diverse �nderungen.
 *
 * Revision 1.16  2003/08/09 10:57:54  maj0r
 * Upload- und DownloadTabelle weitergef�hrt.
 *
 * Revision 1.15  2003/08/08 05:35:52  maj0r
 * Nullpointer behoben.
 *
 * Revision 1.14  2003/08/05 20:47:06  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.13  2003/08/05 05:11:59  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.12  2003/08/03 19:54:05  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.11  2003/08/02 12:03:38  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.10  2003/07/03 19:11:16  maj0r
 * DownloadTable �berarbeitet.
 *
 * Revision 1.9  2003/07/01 14:59:28  maj0r
 * Keyverwendung bei HashSets und HashMaps korrigiert.
 * Server-IDs werden nun abgeglichen, alte werden entfernt.
 *
 * Revision 1.8  2003/06/30 20:35:50  maj0r
 * Code optimiert.
 *
 * Revision 1.7  2003/06/22 19:00:27  maj0r
 * Basisklasse umbenannt.
 *
 * Revision 1.6  2003/06/22 16:24:09  maj0r
 * Umrechnung korrigiert.
 *
 * Revision 1.5  2003/06/10 12:31:03  maj0r
 * Historie eingef�gt.
 *
 *
 */

public class ModifiedXMLHolder
        extends WebXMLParser implements LanguageListener {
    private HashMap sourcenZuDownloads = new HashMap();

    private HashMap serverMap = new HashMap();
    private HashMap downloadMap = new HashMap();
    private HashMap uploadMap = new HashMap();
    private HashMap searchMap = new HashMap();
    private NetworkInfo netInfo;
    private String[] status = new String[6];

    private int connectedWithServerId = -1;
    private int tryConnectToServer = -1;

    private String verbunden;
    private String nichtVerbunden;
    private String verbinden;
    private String keinServer;

    private boolean reloadInProgress = false;
    private Logger logger;

    public ModifiedXMLHolder() {
        super("/xml/modified.xml", "");
        LanguageSelector.getInstance().addLanguageListener(this);
        logger = Logger.getLogger(getClass());
    }

    public HashMap getServer() {
        return serverMap;
    }

    public HashMap getUploads() {
        return uploadMap;
    }

    public HashMap getDownloads() {
        return downloadMap;
    }

    public HashMap getSearchs() {
        return searchMap;
    }

    public NetworkInfo getNetworkInfo() {
        return netInfo;
    }

    public synchronized void update(String sessionId) {
        reload("&session=" + sessionId);
        updateIDs();
        updateServer();
        updateDownloads();
        updateNetworkInfo();
        updateUploads();
        updateSuche();
    }

    public void reload(String parameters) {
        if (reloadInProgress)
            return;
        else {
            reloadInProgress = true;
            super.reload(parameters);
            reloadInProgress = false;
        }
    }

    public void update() {
        throw new RuntimeException();
    }

    public String[] getStatusBar() {
        NodeList nodes = document.getElementsByTagName("information");
        try {
            if (tryConnectToServer != -1) {
                ServerDO serverDO = (ServerDO) serverMap.get(new MapSetStringKey(Integer.toString(tryConnectToServer)));
                if (serverDO != null) {
                    status[0] = verbinden;
                    status[1] = serverDO.getName();
                }
            }
            else if (connectedWithServerId != -1) {
                ServerDO serverDO = (ServerDO) serverMap.get(new MapSetStringKey(Integer.toString(connectedWithServerId)));
                if (serverDO != null) {
                    status[0] = verbunden;
                    status[1] = serverDO.getName();
                }
            }
            else {
                status[0] = nichtVerbunden;
                status[1] = keinServer;
            }
            status[4] = netInfo.getExterneIP();
            if (nodes.getLength() == 0) {
                return status; //Keine Ver�nderung seit dem letzten Abrufen
            }
            Element e = (Element) nodes.item(0); //Es gibt nur ein information-Element
            long credits = Long.parseLong(e.getAttribute("credits"));
            long up = Long.parseLong(e.getAttribute("uploadspeed"));
            long down = Long.parseLong(e.getAttribute("downloadspeed"));
            status[2] = " in: " + getBytesSpeed(down) + " out: " + getBytesSpeed(up);
            long uptotal = Long.parseLong(e.getAttribute("sessionupload"));
            long downtotal = Long.parseLong(e.getAttribute("sessiondownload"));
            status[3] = " in: " + bytesUmrechnen(downtotal) + " out: " + bytesUmrechnen(uptotal);
            status[4] = netInfo.getExterneIP();
            status[5] = " Credits: " + bytesUmrechnen(credits);
        }
        catch (Exception ex) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", ex);
        }
        return status;
    }

    public HashMap getSpeeds(){
        HashMap speeds = new HashMap();
        try{
            NodeList nodes = document.getElementsByTagName("information");
            if (nodes.getLength()>0){
                Element e = (Element) nodes.item(0);
                if (e!=null){
                    speeds.put(new MapSetStringKey("uploadspeed"), new Long(e.getAttribute("uploadspeed")));
                    speeds.put(new MapSetStringKey("downloadspeed"), new Long(e.getAttribute("downloadspeed")));
                    speeds.put(new MapSetStringKey("credits"), new Long(e.getAttribute("credits")));
                    speeds.put(new MapSetStringKey("sessionupload"), new Long(e.getAttribute("sessionupload")));
                    speeds.put(new MapSetStringKey("sessiondownload"), new Long(e.getAttribute("sessiondownload")));
                }
            }
        }
        catch (Exception ex) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", ex);
        }
        return speeds;
    }

    private String getBytesSpeed(long bytes) {
        if (bytes == 0) {
            return "0 KB/s";
        }
        String result = bytesUmrechnen(bytes) + "/s";
        return result;
    }

    private String bytesUmrechnen(long bytes) {
        boolean minus = false;
        if (bytes < 0) {
            minus = true;
            bytes *= -1;
        }
        if (bytes == 0) {
            return "0 MB";
        }
        long faktor = 1;
        if (bytes < 1024l) {
            faktor = 1;
        }
        else if (bytes / 1024l < 1024l) {
            faktor = 1024l;
        }
        else if (bytes / 1048576l < 1024l) {
            faktor = 1048576l;
        }
        else if (bytes / 1073741824l < 1024l) {
            faktor = 1073741824l;
        }
        else {
            faktor = 1099511627776l;
        }
        if (minus) {
            bytes *= -1;
        }
        double umgerechnet = (double) bytes / (double) faktor;
        String result = Double.toString(umgerechnet);
        int pos = result.indexOf(".");
        if (pos != -1) {
            if (pos + 2 < result.length())
                result = result.substring(0, pos + 3);
            result = result.replace('.', ',');
        }
        if (faktor == 1) {
            result += " Bytes";
        }
        else if (faktor == 1024l) {
            result += " kb";
        }
        else if (faktor == 1048576l) {
            result += " MB";
        }
        else if (faktor == 1073741824l) {
            result += " GB";
        }
        else {
            result += " TB";
        }
        return result;
    }

    private void updateIDs() {
        try {
            Element e = null;
            String id = null;
            NodeList nodes = document.getElementsByTagName("removed");
            nodes = nodes.item(0).getChildNodes();
            int size = nodes.getLength();
            MapSetStringKey toRemoveKey;
            DownloadDO downloadDO;
            for (int i = 0; i < size; i++) {
                e = (Element) nodes.item(i);
                id = e.getAttribute("id");
                toRemoveKey = new MapSetStringKey(id);
                if (uploadMap.containsKey(toRemoveKey)) {
                    uploadMap.remove(toRemoveKey);
                    continue;
                }
                else if (downloadMap.containsKey(toRemoveKey)) {
                    downloadDO = (DownloadDO) downloadMap.get(sourcenZuDownloads.get(toRemoveKey));
                    if (downloadDO!=null){
                        DownloadSourceDO[] sourcen = downloadDO.getSources();
                        if (sourcen!=null){
                            for (int y = 0; y < sourcen.length; y++) {
                                sourcenZuDownloads.remove(new MapSetStringKey(sourcen[y].getId()));
                            }
                        }
                    }
                    downloadMap.remove(toRemoveKey);
                    continue;
                }
                else if (serverMap.containsKey(toRemoveKey)) {
                    serverMap.remove(toRemoveKey);
                    continue;
                }
                else if (sourcenZuDownloads.containsKey(toRemoveKey)) {
                    downloadDO = (DownloadDO) sourcenZuDownloads.get(toRemoveKey);
                    downloadDO.removeSource(id);
                    sourcenZuDownloads.remove(toRemoveKey);
                    continue;
                }
            }
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    private void updateSuche() {
        try {
            NodeList nodes = document.getElementsByTagName("search");
            int size = nodes.getLength();
            if (size>0){
                Element e;
                int id;
                String suchtext;
                int offeneSuchen;
                int gefundeneDateien;
                int durchsuchteClients;
                MapSetStringKey key;
                Search aSearch;
                for (int i = 0; i < size; i++) {
                    e = (Element) nodes.item(i);
                    id = Integer.parseInt(e.getAttribute("id"));
                    key = new MapSetStringKey(id);
                    suchtext = e.getAttribute("searchtext");
                    String temp = e.getAttribute("opensearchs");
                    offeneSuchen = Integer.parseInt(temp);
                    temp = e.getAttribute("sumsearches");
                    durchsuchteClients = Integer.parseInt(temp);
                    temp = e.getAttribute("foundfiles");
                    gefundeneDateien = Integer.parseInt(temp);
                    if (searchMap.containsKey(key)) {
                        aSearch = (Search) searchMap.get(key);
                        aSearch.setDurchsuchteClients(durchsuchteClients);
                        aSearch.setGefundenDateien(gefundeneDateien);
                        aSearch.setOffeneSuchen(offeneSuchen);
                        aSearch.setSuchText(suchtext);
                    }
                    else{
                        aSearch = new Search(id);
                        aSearch.setDurchsuchteClients(durchsuchteClients);
                        aSearch.setGefundenDateien(gefundeneDateien);
                        aSearch.setOffeneSuchen(offeneSuchen);
                        aSearch.setSuchText(suchtext);
                        searchMap.put(key, aSearch);
                    }

                }
            }
            nodes = document.getElementsByTagName("searchentry");
            size = nodes.getLength();
            if (size>0){
                Element e;
                int id;
                int searchid;
                String checksum;
                long groesse;
                MapSetStringKey key;
                Search aSearch;
                Search.SearchEntry searchEntry;
                Element innerElement;
                NodeList childNodes;
                String dateiName;
                int haeufigkeit;
                Search.SearchEntry.FileName filename;
                for (int i = 0; i < size; i++) {
                    e = (Element) nodes.item(i);
                    id = Integer.parseInt(e.getAttribute("id"));
                    searchid = Integer.parseInt(e.getAttribute("searchid"));
                    key = new MapSetStringKey(searchid);
                    checksum = e.getAttribute("checksum");
                    groesse = Long.parseLong(e.getAttribute("size"));
                    aSearch = (Search) searchMap.get(key);
                    if (aSearch!=null){
                        searchEntry = aSearch.new SearchEntry(id, checksum, groesse);
                        childNodes = nodes.item(0).getChildNodes();
                        int nodesSize = childNodes.getLength();
                        for (int y = 0; y < nodesSize; y++) {
                            if (childNodes.item(y).getNodeType()==Node.ELEMENT_NODE){
                                innerElement = (Element) childNodes.item(y);
                                if (innerElement.getNodeName().compareToIgnoreCase("filename")==0){
                                    dateiName = innerElement.getAttribute("name");
                                    haeufigkeit = Integer.parseInt(innerElement.getAttribute("user"));
                                    filename = searchEntry.new FileName(dateiName, haeufigkeit);
                                    searchEntry.addFileName(filename);
                                }
                            }
                        }
                        aSearch.addSearchEntry(searchEntry);
                    }
                }
            }
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    private void updateDownloads() {
        try {
            Element e = null;
            int id;
            int shareid;
            String hash = null;
            long fileSize;
            long sizeReady;
            String temp = null;
            int status;
            String filename = null;
            String targetDirectory = null;
            int powerDownload;
            int temporaryFileNumber;
            NodeList nodes = document.getElementsByTagName("download");
            int size = nodes.getLength();
            DownloadDO downloadDO = null;
            MapSetStringKey key;
            synchronized (downloadMap) {
                for (int i = 0; i < size; i++) {
                    e = (Element) nodes.item(i);
                    id = Integer.parseInt(e.getAttribute("id"));
                    key = new MapSetStringKey(id);
                    if (downloadMap.containsKey(key)) {
                        downloadDO = (DownloadDO) downloadMap.get(key);
                        downloadDO.setShareId(Integer.parseInt(e.getAttribute("shareid")));
                        downloadDO.setHash(e.getAttribute("hash"));
                        downloadDO.setGroesse(Long.parseLong(e.getAttribute("size")));
                        downloadDO.setReady(Long.parseLong(e.getAttribute("ready")));
                        temp = e.getAttribute("status");
                        downloadDO.setStatus(Integer.parseInt(temp));
                        downloadDO.setFilename(e.getAttribute("filename"));
                        downloadDO.setTargetDirectory(e.getAttribute("targetdirectory"));
                        temp = e.getAttribute("powerdownload");
                        downloadDO.setPowerDownload(Integer.parseInt(temp));
                        temp = e.getAttribute("temporaryfilenumber");
                        downloadDO.setTemporaryFileNumber(Integer.parseInt(temp));
                    }
                    else {
                        shareid = Integer.parseInt(e.getAttribute("shareid"));
                        hash = e.getAttribute("hash");
                        fileSize = Long.parseLong(e.getAttribute("size"));
                        sizeReady = Long.parseLong(e.getAttribute("ready"));
                        temp = e.getAttribute("status");
                        status = Integer.parseInt(temp);
                        filename = e.getAttribute("filename");
                        targetDirectory = e.getAttribute("targetdirectory");
                        temp = e.getAttribute("powerdownload");
                        powerDownload = Integer.parseInt(temp);
                        temp = e.getAttribute("temporaryfilenumber");
                        temporaryFileNumber = Integer.parseInt(temp);

                        downloadDO = new DownloadDO(id, shareid, hash, fileSize, sizeReady, status, filename,
                                targetDirectory, powerDownload, temporaryFileNumber);

                        downloadMap.put(new MapSetStringKey(id), downloadDO);
                    }
                }
            }
            int directstate;
            nodes = document.getElementsByTagName("user");
            size = nodes.getLength();
            int downloadFrom;
            int downloadTo;
            int actualDownloadPosition;
            int speed;
            int downloadId;
            Version version = null;
            String versionNr = null;
            String nickname = null;
            int queuePosition;
            int os;
            DownloadSourceDO downloadSourceDO = null;
            for (int i = 0; i < size; i++) {
                e = (Element) nodes.item(i);
                id = Integer.parseInt(e.getAttribute("id"));
                temp = e.getAttribute("status");
                status = Integer.parseInt(temp);
                temp = e.getAttribute("directstate");
                directstate = Integer.parseInt(temp);
                if (status == DownloadSourceDO.UEBERTRAGUNG) {
                    temp = e.getAttribute("downloadfrom");
                    downloadFrom = Integer.parseInt(temp);
                    temp = e.getAttribute("downloadto");
                    downloadTo = Integer.parseInt(temp);
                    temp = e.getAttribute("actualdownloadposition");
                    actualDownloadPosition = Integer.parseInt(temp);
                    temp = e.getAttribute("speed");
                    speed = Integer.parseInt(temp);
                }
                else {
                    downloadFrom = -1;
                    downloadTo = -1;
                    actualDownloadPosition = -1;
                    speed = 0;
                }
                versionNr = e.getAttribute("version");
                if (versionNr.compareToIgnoreCase("0.0.0.0") == 0) {
                    version = null;
                }
                else {
                    temp = e.getAttribute("operatingsystem");
                    os = Integer.parseInt(temp);
                    version = new Version(versionNr, os);
                }
                temp = e.getAttribute("queueposition");
                queuePosition = Integer.parseInt(temp);
                temp = e.getAttribute("powerdownload");
                powerDownload = Integer.parseInt(temp);
                filename = e.getAttribute("filename");
                nickname = e.getAttribute("nickname");
                temp = e.getAttribute("downloadid");
                downloadId = Integer.parseInt(temp);
                downloadSourceDO = new DownloadSourceDO(id, status, directstate, downloadFrom, downloadTo, actualDownloadPosition,
                        speed, version, queuePosition, powerDownload, filename, nickname, downloadId);
                key = new MapSetStringKey(downloadId);
                downloadDO = (DownloadDO) downloadMap.get(key);
                if (downloadDO != null) {
                    downloadDO.addOrAlterSource(downloadSourceDO);
                    sourcenZuDownloads.put(new MapSetStringKey(id), downloadDO);
                }
            }
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    private void updateUploads() {
        try {
            NodeList nodes = document.getElementsByTagName("upload");
            int size = nodes.getLength();
            Element e = null;
            int shareId;
            UploadDO upload = null;
            int id;
            int os;
            String versionsNr = null;
            Version version = null;
            int prioritaet;
            String nick = null;
            String status = null;
            long uploadFrom;
            long uploadTo;
            long actualUploadPos;
            int speed;
            MapSetStringKey idKey = null;
            synchronized (uploadMap) {
                HashMap share = ApplejuiceFassade.getInstance().getShare(false);
                ShareDO shareDO;
                for (int i = 0; i < size; i++) {
                    e = (Element) nodes.item(i);
                    id = Integer.parseInt(e.getAttribute("id"));
                    idKey = new MapSetStringKey(id);
                    if (uploadMap.containsKey(idKey)) {
                        upload = (UploadDO) uploadMap.get(idKey);
                        upload.setShareFileID(Integer.parseInt(e.getAttribute("shareid")));
                        upload.setPrioritaet(Integer.parseInt(e.getAttribute("priority")));
                        upload.setNick(e.getAttribute("nick"));
                        upload.setStatus(Integer.parseInt(e.getAttribute("status")));
                        upload.setUploadFrom(Long.parseLong(e.getAttribute("uploadfrom")));
                        upload.setUploadTo(Long.parseLong(e.getAttribute("uploadto")));
                        upload.setActualUploadPosition(Long.parseLong(e.getAttribute("actualuploadposition")));
                        upload.setSpeed(Integer.parseInt(e.getAttribute("speed")));
                    }
                    else {
                        shareId = Integer.parseInt(e.getAttribute("shareid"));
                        versionsNr = e.getAttribute("version");
                        if (versionsNr.compareToIgnoreCase("0.0.0.0") == 0) {
                            version = null;
                        }
                        else {
                            os = Integer.parseInt(e.getAttribute("operatingsystem"));
                            version = new Version(versionsNr, os);
                        }
                        prioritaet = Integer.parseInt(e.getAttribute("priority"));
                        nick = e.getAttribute("nick");
                        status = e.getAttribute("status");
                        uploadFrom = Long.parseLong(e.getAttribute("uploadfrom"));
                        uploadTo = Long.parseLong(e.getAttribute("uploadto"));
                        actualUploadPos = Long.parseLong(e.getAttribute("actualuploadposition"));
                        speed = Integer.parseInt(e.getAttribute("speed"));
                        upload = new UploadDO(id, shareId, version, status, nick,
                                uploadFrom, uploadTo, actualUploadPos,
                                speed, prioritaet);
                        shareDO = (ShareDO) share.get(new MapSetStringKey(shareId));
                        if (upload != null && shareDO != null) {
                            /*wenns die passende Sharedatei aus irgendeinem Grund nicht geben sollte,
                            wird dieser Upload auch nicht angezeigt*/
                            upload.setDateiName(shareDO.getShortfilename());
                            uploadMap.put(idKey, upload);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    private void updateServer() {
        try {
            NodeList nodes = document.getElementsByTagName("server");
            int size = nodes.getLength();
            Element e = null;
            String id_key = null;
            int id;
            String name = null;
            String host = null;
            long lastseen;
            String port = null;
            ServerDO server = null;
            MapSetStringKey key;
            ServerDO serverDO;
            for (int i = 0; i < size; i++) {
                e = (Element) nodes.item(i);
                id_key = e.getAttribute("id");
                key = new MapSetStringKey(id_key);
                if (serverMap.containsKey(key)) {
                    serverDO = (ServerDO) serverMap.get(key);
                    serverDO.setName(e.getAttribute("name"));
                    serverDO.setHost(e.getAttribute("host"));
                    serverDO.setTimeLastSeen(Long.parseLong(e.getAttribute("lastseen")));
                    serverDO.setPort(e.getAttribute("port"));
                }
                else {
                    id = Integer.parseInt(id_key);
                    name = e.getAttribute("name");
                    host = e.getAttribute("host");
                    lastseen = Long.parseLong(e.getAttribute("lastseen"));
                    port = e.getAttribute("port");
                    server = new ServerDO(id, name, host, port, lastseen);
                    serverMap.put(new MapSetStringKey(id_key), server);
                }
            }
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    private void updateNetworkInfo() {
        try {
            NodeList nodes = document.getElementsByTagName("networkinfo");
            if (nodes.getLength() == 0) {
                return; //Keine Ver�nderung seit dem letzten Abrufen
            }
            Element e = (Element) nodes.item(0); //Es gibt nur ein Netzerkinfo-Element
            String users = e.getAttribute("users");
            String dateien = e.getAttribute("files");
            String dateigroesse = e.getAttribute("filesize");
            int tryConnectToServer = Integer.parseInt(e.getAttribute("tryconnecttoserver"));
            int connectedWithServerId = Integer.parseInt(e.getAttribute("connectedwithserverid"));
            boolean firewalled = (e.getAttribute("firewalled").compareToIgnoreCase(
                    "true") == 0) ? true : false;
            String externeIP = e.getAttribute("ip");
            if (this.tryConnectToServer != tryConnectToServer) {
                Object alterServer = serverMap.get(new MapSetStringKey(Integer.toString(this.tryConnectToServer)));
                if (alterServer != null) {
                    ((ServerDO) alterServer).setTryConnect(false);
                }
                if (tryConnectToServer != -1) {
                    ServerDO serverDO = (ServerDO) serverMap.get(new MapSetStringKey(Integer.toString(tryConnectToServer)));
                    serverDO.setTryConnect(true);
                }
                this.tryConnectToServer = tryConnectToServer;
            }
            //if (this.connectedWithServerId != connectedWithServerId){
            Object alterServer = serverMap.get(new MapSetStringKey(Integer.toString(this.connectedWithServerId)));
            if (alterServer != null) {
                ((ServerDO) alterServer).setConnected(false);
            }
            if (connectedWithServerId != -1) {
                ServerDO serverDO = (ServerDO) serverMap.get(new MapSetStringKey(Integer.toString(connectedWithServerId)));
                serverDO.setConnected(true);
            }
            this.connectedWithServerId = connectedWithServerId;
            //}
            netInfo = new NetworkInfo(users, dateien, dateigroesse, firewalled,
                    externeIP, tryConnectToServer, connectedWithServerId);
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }

    public void fireLanguageChanged() {
        try {
            LanguageSelector languageSelector = LanguageSelector.getInstance();
            verbunden = languageSelector.getFirstAttrbuteByTagName(new String[]{
                "javagui", "mainform", "verbunden"});
            verbinden = languageSelector.getFirstAttrbuteByTagName(new String[]{
                "javagui", "mainform", "verbinden"});
            keinServer = languageSelector.getFirstAttrbuteByTagName(new String[]{
                "javagui", "mainform", "keinserver"});
            nichtVerbunden = languageSelector.getFirstAttrbuteByTagName(new String[]{
                "javagui", "mainform", "nichtverbunden"});
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR))
                logger.error("Unbehandelte Exception", e);
        }
    }
}