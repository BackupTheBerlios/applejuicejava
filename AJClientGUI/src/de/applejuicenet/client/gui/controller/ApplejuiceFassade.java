package de.applejuicenet.client.gui.controller;

import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import de.applejuicenet.client.gui.listener.*;
import de.applejuicenet.client.gui.tables.download.DownloadNode;
import de.applejuicenet.client.gui.trees.share.DirectoryNode;
import de.applejuicenet.client.shared.*;
import de.applejuicenet.client.shared.exception.*;
import de.applejuicenet.client.shared.dac.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/controller/Attic/ApplejuiceFassade.java,v 1.3 2003/08/17 16:13:11 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <AJCoreGUI@maj0r.de>
 *
 * $Log: ApplejuiceFassade.java,v $
 * Revision 1.3  2003/08/17 16:13:11  maj0r
 * Erstellen des DirectoryNode-Baumes korrigiert.
 *
 * Revision 1.2  2003/08/16 18:40:25  maj0r
 * Passworteingabe korrigiert.
 *
 * Revision 1.1  2003/08/15 14:46:30  maj0r
 * Refactoring.
 *
 * Revision 1.38  2003/08/12 06:12:19  maj0r
 * Version erh�ht.
 *
 * Revision 1.37  2003/08/11 14:10:28  maj0r
 * DownloadPartList eingef�gt.
 * Diverse �nderungen.
 *
 * Revision 1.36  2003/08/10 21:08:18  maj0r
 * Diverse �nderungen.
 *
 * Revision 1.35  2003/08/09 16:47:42  maj0r
 * Diverse �nderungen.
 *
 * Revision 1.34  2003/08/09 10:57:54  maj0r
 * Upload- und DownloadTabelle weitergef�hrt.
 *
 * Revision 1.33  2003/08/05 20:47:06  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.32  2003/08/05 05:11:59  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.31  2003/08/04 14:28:55  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.30  2003/08/03 19:54:05  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.29  2003/08/02 12:03:38  maj0r
 * An neue Schnittstelle angepasst.
 *
 * Revision 1.28  2003/07/04 15:25:38  maj0r
 * Version erh�ht.
 * DownloadModel erweitert.
 *
 * Revision 1.27  2003/07/03 19:11:16  maj0r
 * DownloadTable �berarbeitet.
 *
 * Revision 1.26  2003/07/01 15:00:00  maj0r
 * Keyverwendung bei HashSets und HashMaps korrigiert.
 *
 * Revision 1.25  2003/07/01 06:17:16  maj0r
 * Code optimiert.
 *
 * Revision 1.24  2003/06/24 14:32:27  maj0r
 * Klassen zum Sortieren von Tabellen eingef�gt.
 * Servertabelle kann nun spaltenweise sortiert werden.
 *
 * Revision 1.23  2003/06/24 12:06:49  maj0r
 * log4j eingef�gt (inkl. Bedienung �ber Einstellungsdialog).
 *
 * Revision 1.22  2003/06/22 20:34:25  maj0r
 * Konsolenausgaben hinzugef�gt.
 *
 * Revision 1.21  2003/06/13 15:07:30  maj0r
 * Versionsanzeige hinzugef�gt.
 * Da der Controllerteil refactort werden kann, haben Controller und GUI separate Versionsnummern.
 *
 * Revision 1.20  2003/06/10 12:31:03  maj0r
 * Historie eingef�gt.
 *
 *
 */

public class ApplejuiceFassade { //Singleton-Implementierung
    public static final String DATAMANAGER_VERSION = "0.1 Alpha";

    private HashSet downloadListener;
    private HashSet shareListener;
    private HashSet uploadListener;
    private HashSet serverListener;
    private HashSet networkInfoListener;
    private HashSet statusbarListener;
    private static ApplejuiceFassade instance = null;
    private ModifiedXMLHolder modifiedXML = null;
    private InformationXMLHolder informationXML = null;
    private ShareXMLHolder shareXML = null;
    private SettingsXMLHolder settingsXML = null;
    private DirectoryXMLHolder directoryXML = null;
    private Version coreVersion;
    private Timer modifiedTimer;
    private HashMap share = null;

    private static int checkInProgress = 0;

    private Logger logger;

    public void addDataUpdateListener(DataUpdateListener listener, int type) {
        HashSet listenerSet = null;
        if (type == DataUpdateListener.DOWNLOAD_CHANGED)
        {
            listenerSet = downloadListener;
        }
        else if (type == DataUpdateListener.NETINFO_CHANGED)
        {
            listenerSet = networkInfoListener;
        }
        else if (type == DataUpdateListener.SERVER_CHANGED)
        {
            listenerSet = serverListener;
        }
        else if (type == DataUpdateListener.SHARE_CHANGED)
        {
            listenerSet = shareListener;
        }
        else if (type == DataUpdateListener.UPLOAD_CHANGED)
        {
            listenerSet = uploadListener;
        }
        else if (type == DataUpdateListener.STATUSBAR_CHANGED)
        {
            listenerSet = statusbarListener;
        }
        else
        {
            return;
        }
        if (!(listenerSet.contains(listener)))
        {
            listenerSet.add(listener);
        }
    }

    private ApplejuiceFassade() {
        logger = Logger.getLogger(getClass());
        try
        {
            downloadListener = new HashSet();
            serverListener = new HashSet();
            uploadListener = new HashSet();
            shareListener = new HashSet();
            networkInfoListener = new HashSet();
            statusbarListener = new HashSet();

            //load XMLs
            modifiedXML = new ModifiedXMLHolder();
            informationXML = new InformationXMLHolder();
            directoryXML = new DirectoryXMLHolder();
            informationXML.reload("");
            shareXML = new ShareXMLHolder();

            String versionsTag = informationXML.getFirstAttrbuteByTagName(new String[]{
                "applejuice", "generalinformation", "version"}
                                                                          , true);
            coreVersion = new Version(versionsTag, Version.getOSTypByOSName((String) System.getProperties().get("os.name")));

            ActionListener modifiedAction = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    updateModifiedXML();
                }
            };
            modifiedTimer = new Timer(1000, modifiedAction);
            modifiedTimer.stop();
        }
        catch (Exception e)
        {
            if (logger.isEnabledFor(Level.FATAL))
                logger.fatal("Unbehandelte Exception", e);
        }
    }

    public void startXMLCheck() {
        if (!modifiedTimer.isRunning())
        {
            modifiedTimer.start();
        }
    }

    public void stopXMLCheck() {
        if (modifiedTimer.isRunning())
        {
            modifiedTimer.stop();
        }
    }

    public PartListDO getDownloadPartList(DownloadDO downloadDO) {
        DownloadPartListXMLHolder partlistXML = new DownloadPartListXMLHolder(downloadDO);
        return partlistXML.getPartList();
    }

    public AJSettings getAJSettings() {
        if (settingsXML == null)
        {
            settingsXML = new SettingsXMLHolder();
        }
        return settingsXML.getAJSettings();
    }

    public boolean saveAJSettings(AJSettings ajSettings) {
        String parameters = "";
        try
        {
            parameters = "Nickname=" +
                    URLEncoder.encode(ajSettings.getNick(), "UTF-8");
            parameters += "&XMLPort=" + Long.toString(ajSettings.getXMLPort());
            parameters += "&AllowBrowse=" +
                    (ajSettings.isBrowseAllowed() ? "true" : "false");
            parameters += "&MaxUpload=" + Long.toString(ajSettings.getMaxUpload());
            parameters += "&MaxDownload=" + Long.toString(ajSettings.getMaxDownload());
            parameters += "&Speedperslot=" +
                    Integer.toString(ajSettings.getSpeedPerSlot());
            parameters += "&Incomingdirectory=" +
                    URLEncoder.encode(ajSettings.getIncomingDir(), "UTF-8");
            parameters += "&Temporarydirectory=" +
                    URLEncoder.encode(ajSettings.getTempDir(), "UTF-8");
            parameters += "&maxconnections=" +
                    URLEncoder.encode(Long.toString(ajSettings.getMaxConnections()), "UTF-8");
            parameters += "&autoconnect=" +
                    URLEncoder.encode(new Boolean(ajSettings.isAutoConnect()).toString(), "UTF-8");
        }
        catch (UnsupportedEncodingException ex1)
        {
        }
        String result;
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.GET,
                                                  "/function/setsettings?password=" + password + "&" + parameters);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public HashMap getAllServer() {
        return modifiedXML.getServer();
    }

    public synchronized void updateModifiedXML() {
        if (checkInProgress!=0)
            return;
        checkInProgress++;
        modifiedXML.update();
        informDataUpdateListener(DataUpdateListener.SERVER_CHANGED);
        informDataUpdateListener(DataUpdateListener.DOWNLOAD_CHANGED);
        informDataUpdateListener(DataUpdateListener.UPLOAD_CHANGED);
        informDataUpdateListener(DataUpdateListener.NETINFO_CHANGED);
        informDataUpdateListener(DataUpdateListener.STATUSBAR_CHANGED);
        checkInProgress--;
    }

    public boolean resumeDownload(String id) {
        HashMap downloads = getDownloads();
        String result;
        DownloadDO downloadDO = (DownloadDO) downloads.get(new MapSetStringKey(id));
        if (downloadDO == null)
            return false;
        logger.info("Resume '" + downloadDO.getFilename() + "'...");
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.POST,
                                                  "/function/resumedownload?password=" + password + "&id=" + id);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public static boolean setPassword(String passwordAsMD5) {
        String result;
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.POST,
                                                  "/function/setpassword?password=" + password + "&newpassword=" + passwordAsMD5);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public boolean cancelDownload(String id) {
        HashMap downloads = getDownloads();
        String result;
        DownloadDO downloadDO = (DownloadDO) downloads.get(new MapSetStringKey(id));
        if (downloadDO == null)
            return false;
        logger.info("Cancel '" + downloadDO.getFilename() + "'...");
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.POST,
                                                  "/function/canceldownload?password=" + password + "&id=" + id);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public boolean cleanDownloadList() {
        logger.info("Clear list...");
        String result;
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.POST,
                                                  "/function/cleandownloadlist?password=" + password);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public boolean pauseDownload(String id) {
        HashMap downloads = getDownloads();
        String result;
        DownloadDO downloadDO = (DownloadDO) downloads.get(new MapSetStringKey(id));
        if (downloadDO == null)
            return false;
        logger.info("Pause '" + downloadDO.getFilename() + "'...");
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.POST,
                                                  "/function/pausedownload?password=" + password + "&id=" + id);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public boolean connectToServer(int id) {
        HashMap server = getAllServer();
        String id_key = Integer.toString(id);
        ServerDO serverDO = (ServerDO) server.get(new MapSetStringKey(id_key));
        if (serverDO == null)
        {
            System.out.print("Warnung: Server mit ID: " + id_key + " nicht gefunden!");
            return false;
        }
        else
        {
            String result;
            logger.info("Verbinde mit '" + serverDO.getName() + "'...");
            try
            {
                String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
                result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.POST,
                                                      "/function/serverlogin?password=" + password + "&id=" + id);
            }
            catch (WebSiteNotFoundException ex)
            {
                return false;
            }
            return true;
        }
    }

    public boolean setPrioritaet(int id, int prioritaet) {
        if (prioritaet < 1 || prioritaet > 250)
        {
            System.out.print("Warnung: Prioritaet muss 1<= x <=250 sein!");
            return false;
        }
        String id_key = Integer.toString(id);
        ShareDO shareDO = (ShareDO) share.get(new MapSetStringKey(id_key));
        if (shareDO == null)
        {
            System.out.print("Warnung: Share mit ID: " + id_key + " nicht gefunden!");
            return false;
        }
        else
        {
            String result;
            logger.info("Setze '" + shareDO.getShortfilename() + "' auf Prioritaet " + prioritaet + "...");
            try
            {
                String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
                result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.GET,
                                                      "/function/setpriority?password=" + password + "&id=" + id + "&priority=" + prioritaet);
            }
            catch (WebSiteNotFoundException ex)
            {
                return false;
            }
            return true;
        }
    }

    public boolean processLink(String link) {
        if (link == null || link.length() == 0)
        {
            System.out.print("Warnung: Ungueltiger Link uebergeben!");
            return false;
        }
        String result;
        logger.info("Downloade '" + link + "...");
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.GET,
                                                  "/function/processlink?password=" + password + "&link=" + link);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public boolean setPowerDownload(int id, int powerDownload) {
        if (powerDownload < 0 || powerDownload > 490)
        {
            System.out.print("Warnung: PowerDownload muss 0<= x <=490 sein!");
            return false;
        }
        HashMap download = getDownloads();
        String id_key = Integer.toString(id);
        DownloadDO downloadDO = (DownloadDO) download.get(new MapSetStringKey(id_key));
        if (downloadDO == null)
        {
            System.out.print("Warnung: Download mit ID: " + id_key + " nicht gefunden!");
            return false;
        }
        else
        {
            String result;
            logger.info("Setze '" + downloadDO.getFilename() + "' auf PowerDownload " + powerDownload + "...");
            try
            {
                String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
                result = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.GET,
                                                      "/function/setpowerdownload?password=" + password + "&id=" + id + "&powerdownload=" + powerDownload);
            }
            catch (WebSiteNotFoundException ex)
            {
                return false;
            }
            return true;
        }
    }

    private static String getHost() {
        String savedHost = OptionsManager.getInstance().getRemoteSettings().getHost();
        if (savedHost.length() == 0)
        {
            savedHost = "localhost";
        }
        return savedHost;
    }

    public NetworkInfo getNetworkInfo() {
        if (checkInProgress!=0)
            modifiedXML.getNetworkInfo();
        checkInProgress++;
        modifiedXML.update();
        checkInProgress--;
        return modifiedXML.getNetworkInfo();
    }

    public static boolean istCoreErreichbar() {
        try
        {
            String password = OptionsManager.getInstance().getRemoteSettings().getOldPassword();
            String testData = HtmlLoader.getHtmlXMLContent(getHost(), HtmlLoader.GET,
                                                           "/xml/information.xml?password=" + password);
        }
        catch (WebSiteNotFoundException ex)
        {
            return false;
        }
        return true;
    }

    public static ApplejuiceFassade getInstance() {
        if (instance == null)
        {
            instance = new ApplejuiceFassade();
        }
        return instance;
    }

    public Version getCoreVersion() {
        return coreVersion;
    }

    private void informDataUpdateListener(int type) {
        switch (type)
        {
            case DataUpdateListener.DOWNLOAD_CHANGED:
                {
                    HashMap content = modifiedXML.getDownloads();
                    if (content.size() == 0)
                    {
                        return;
                    }
                    Iterator it = downloadListener.iterator();
                    while (it.hasNext())
                    {
                        ((DataUpdateListener) it.next()).fireContentChanged(DataUpdateListener.
                                                                            DOWNLOAD_CHANGED, content);
                    }
                    break;
                }
            case DataUpdateListener.UPLOAD_CHANGED:
                {
                    HashMap content = modifiedXML.getUploads();
                    if (content.size() == 0)
                    {
                        return;
                    }
                    Iterator it = uploadListener.iterator();
                    while (it.hasNext())
                    {
                        ((DataUpdateListener) it.next()).fireContentChanged(DataUpdateListener.
                                                                            UPLOAD_CHANGED, content);
                    }
                    break;
                }
            case DataUpdateListener.SERVER_CHANGED:
                {
                    HashMap content = modifiedXML.getServer();
                    if (content.size() == 0)
                    {
                        return;
                    }
                    Iterator it = serverListener.iterator();
                    while (it.hasNext())
                    {
                        ((DataUpdateListener) it.next()).fireContentChanged(DataUpdateListener.
                                                                            SERVER_CHANGED, content);
                    }
                    break;
                }
            case DataUpdateListener.SHARE_CHANGED:
                {
                    HashMap content = shareXML.getShare();
                    if (content.size() == 0)
                    {
                        return;
                    }
                    Iterator it = shareListener.iterator();
                    while (it.hasNext())
                    {
                        ((DataUpdateListener) it.next()).fireContentChanged(DataUpdateListener.
                                                                            SHARE_CHANGED, content);
                    }
                    break;
                }
            case DataUpdateListener.NETINFO_CHANGED:
                {
                    NetworkInfo content = modifiedXML.getNetworkInfo();
                    Iterator it = networkInfoListener.iterator();
                    while (it.hasNext())
                    {
                        ((DataUpdateListener) it.next()).fireContentChanged(DataUpdateListener.
                                                                            NETINFO_CHANGED, content);
                    }
                    break;
                }
            case DataUpdateListener.STATUSBAR_CHANGED:
                {
                    String[] content = modifiedXML.getStatusBar();
                    Iterator it = statusbarListener.iterator();
                    while (it.hasNext())
                    {
                        ((DataUpdateListener) it.next()).fireContentChanged(DataUpdateListener.STATUSBAR_CHANGED, content);
                    }
                    break;
                }
            default:
                break;
        }
    }

    public HashMap getDownloads() {
        return modifiedXML.getDownloads();
    }

    public HashMap getUploads() {
        return modifiedXML.getUploads();
    }

    public HashMap getShare(boolean reinit) {
        if (share == null || reinit)
            share = shareXML.getShare();
        return share;
    }

    public static boolean isCheckInProgress() {
        return checkInProgress!=0;
    }

    public void getDirectory(String directory, DirectoryNode directoryNode){
        checkInProgress++;
        directoryXML.getDirectory(directory, directoryNode);
        checkInProgress--;
    }
}