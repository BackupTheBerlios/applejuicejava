package de.applejuicenet.client.gui.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import de.applejuicenet.client.AppleJuiceClient;
import de.applejuicenet.client.gui.AppleJuiceDialog;
import de.applejuicenet.client.gui.listener.DataUpdateListener;
import de.applejuicenet.client.shared.AJSettings;
import de.applejuicenet.client.shared.ConnectionSettings;
import de.applejuicenet.client.shared.LookAFeel;
import de.applejuicenet.client.shared.ProxySettings;
import de.applejuicenet.client.shared.Settings;
import de.applejuicenet.client.shared.XMLDecoder;
import de.applejuicenet.client.shared.exception.InvalidPasswordException;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/controller/PropertiesManager.java,v 1.45 2004/06/11 09:24:30 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r [Maj0r@applejuicenet.de]
 *
 */

class PropertiesManager
    extends XMLDecoder
    implements OptionsManager, PositionManager, ProxyManager{
    private static PropertiesManager instance = null;
    private static final String PROPERTIES_ERROR = "Fehler beim Zugriff auf die properties.xml. " +
                "Die Datei wird neu erstellt.";
    private static final String PROPERTIES_ERROR_MESSAGE = "properties.xml neu erstellt";
    private Logger logger;
    private Set settingsListener = new HashSet();
    private Set connectionSettingsListener = new HashSet();
    private Point mainXY;
    private Dimension mainDimension;
    private ProxySettings proxySettings;
    private ConnectionSettings connectionSettings = null;

    private int[] downloadWidths;
    private int[] uploadWidths;
    private int[] serverWidths;
    private int[] shareWidths;
    private boolean[] downloadVisibilities;
    private boolean[] uploadVisibilities;
    private int[] downloadIndex;
    private int[] uploadIndex;
    private Settings settings = null;

    private static String path;

    private boolean firstReadError = true;
    private boolean legal = false;

    private PropertiesManager(String propertiesPath) {
        super(propertiesPath);
        this.path = propertiesPath;
        logger = Logger.getLogger(getClass());
        init();
    }

    public static PropertiesManager getInstance() {
        if (instance == null) {
            instance = new PropertiesManager(ApplejuiceFassade.getPropertiesPath());
        }
        return instance;
    }

    public static PositionManager getPositionManager() {
        if (instance == null) {
            instance = new PropertiesManager(ApplejuiceFassade.getPropertiesPath());
        }
        return instance;
    }

    private void saveDom() {
        try {
            XMLSerializer xs = new XMLSerializer(new FileWriter(path),
                                                 new OutputFormat(document,
                "UTF-8", true));
            xs.serialize(document);
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(
                    "xml-Serialisierung fehlgeschlagen. properties.xml neu erstellt",
                    e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
        }
    }

    //ProxyManager-Interface

    public ProxySettings getProxySettings() {
        return proxySettings;
    }

    public void saveProxySettings(ProxySettings proxySettings) {
        this.proxySettings = proxySettings;
        setAttributeByTagName(new String[] {"options", "proxy", "use"}
                              , Boolean.toString(proxySettings.isUse()));
        setAttributeByTagName(new String[] {"options", "proxy", "host"}
                              , proxySettings.getHost());
        setAttributeByTagName(new String[] {"options", "proxy", "port"}
                              , Integer.toString(proxySettings.getPort()));
        setAttributeByTagName(new String[] {"options", "proxy", "userpass"}
                              , proxySettings.getUserpass());
        saveDom();
    }

    //OptionsManager-Interface

    public void addSettingsListener(DataUpdateListener listener) {
        if (! (settingsListener.contains(listener))) {
            settingsListener.add(listener);
        }
    }

    public void addConnectionSettingsListener(DataUpdateListener listener) {
        if (! (connectionSettingsListener.contains(listener))) {
            connectionSettingsListener.add(listener);
        }
    }

    private void informSettingsListener(Settings settings) {
        Iterator it = settingsListener.iterator();
        while (it.hasNext()) {
            ( (DataUpdateListener) it.next()).fireContentChanged(
                DataUpdateListener.SETTINGS_CHANGED, settings);
        }
    }

    private void informConnectionSettingsListener(ConnectionSettings settings) {
        Iterator it = connectionSettingsListener.iterator();
        while (it.hasNext()) {
            ( (DataUpdateListener) it.next()).fireContentChanged(
                DataUpdateListener.CONNECTION_SETTINGS_CHANGED, settings);
        }
    }

    public String getSprache() {
        try {
            return getFirstAttrbuteByTagName(new String[] {"options", "sprache"});
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return null;
        }
    }

    public String getDefaultTheme() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "defaulttheme"}); ;
            if (temp == null || temp.length() == 0) {
                throw new Exception("Kein Defaulttheme vorhanden.");
            }
            return temp;
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return "";
        }
    }

    public void setDefaultTheme(String themeShortName) {
        setAttributeByTagName(new String[] {"options", "defaulttheme"}
                              , themeShortName);
    }

    public LookAFeel[] getLookAndFeels() {
        try {
            ArrayList lookAndFeels = new ArrayList();
            String temp = ".";
            String temp2;
            int i=1;
            while (temp!= null && temp.length()>0){
                temp = getFirstAttrbuteByTagName(new String[] {"options",
                    "lookandfeels", "laf" + i, "value"}); ;
                if (temp!= null && temp.length()>0){
                    temp2 = getFirstAttrbuteByTagName(new String[] {"options",
                        "lookandfeels", "laf" + i, "name"}); ;
                    lookAndFeels.add(new LookAFeel(temp2, temp));
                }
                i++;
            }
            return (LookAFeel[])lookAndFeels.toArray(new LookAFeel[lookAndFeels.size()]);

        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
        }
        return null;
    }

    public LookAFeel getDefaultLookAndFeel() {
        try {
            LookAFeel[] looks = this.getLookAndFeels();
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "lookandfeels", "default", "name"});
            if (temp != null) {
                for (int i=0; i<looks.length; i++) {
                    if (temp.equals(looks[i].getName())) {
                        return looks[i];
                    }
                }
            }
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
        }
        return null;
    }

    public void setDefaultLookAndFeel(LookAFeel lookAFeel) {
        setAttributeByTagName(new String[] {"options", "lookandfeels", "default", "name"}
                              , lookAFeel.getName());
    }

    public String getStandardBrowser() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "browser", "file"}); ;
            if (temp == null) {
                return "";
            }
            else {
                return temp;
            }
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return "";
        }
    }

    public void setStandardBrowser(String browser) {
        setAttributeByTagName(new String[] {"options", "browser", "file"}
                              , browser);
        String temp = getStandardBrowser();
        if (temp.compareTo(browser) != 0) {
            AppleJuiceDialog.rewriteProperties = true;
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
        }
    }

    public boolean isErsterStart() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "firststart"}); ;
            if (temp == null || temp.length() == 0) {
                return true;
            }
            return new Boolean(temp).booleanValue();
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return false;
        }
    }

    public void setErsterStart(boolean ersterStart) {
        setAttributeByTagName(new String[] {"options", "firststart"}
                              , Boolean.toString(ersterStart));
    }

    public boolean shouldLoadPluginsOnStartup() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "loadplugins"}); ;
            if (temp == null || temp.length() == 0) {
                return true;
            }
            return new Boolean(temp).booleanValue();
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return false;
        }
    }

    public void loadPluginsOnStartup(boolean loadPluginsOnStartup) {
        setAttributeByTagName(new String[] {"options", "loadplugins"}
                              , Boolean.toString(loadPluginsOnStartup));
        shouldLoadPluginsOnStartup();
    }

    public boolean isThemesSupported() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "themes"}); ;
            if (temp == null || temp.length() == 0) {
                return true;
            }
            return new Boolean(temp).booleanValue();
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return false;
        }
    }

    public void enableThemeSupport(boolean enable) {
        setAttributeByTagName(new String[] {"options", "themes"}
                              , Boolean.toString(enable));
    }

    public int getVersionsinfoModus() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "versionsinfo"});
            int versioninfoModus = Integer.parseInt(temp);
            return versioninfoModus;
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return 1;
        }
    }

    public void setVersionsinfoModus(int versionsinfoModus) {
        if (versionsinfoModus < 0 || versionsinfoModus > 2) {
            versionsinfoModus = 1;
        }
        setAttributeByTagName(new String[] {"options", "versionsinfo"}
                              , Integer.toString(versionsinfoModus));
    }

    public int getLinkListenerPort() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "linklistenerport"}); ;
            if (temp == null || temp.length() == 0) {
                throw new Exception("Kein linklistenerport angegeben");
            }
            return new Integer(temp).intValue();
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return -1;
        }
    }

    public void setLinkListenerPort(int port) {
        setAttributeByTagName(new String[] {"options", "linklistenerport"}
                              , Integer.toString(port));
    }

    public boolean isSoundEnabled() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "sound"}); ;
            if (temp == null || temp.length() == 0) {
                return true;
            }
            return new Boolean(temp).booleanValue();
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return false;
        }
    }

    public void enableSound(boolean enable) {
        setAttributeByTagName(new String[] {"options", "sound"}
                              , Boolean.toString(enable));
    }

    public void setSprache(String sprache) {
        setAttributeByTagName(new String[] {"options", "sprache"}
                              , sprache.toLowerCase());
    }

    public boolean shouldShowConnectionDialogOnStartup() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "dialogzeigen"}); ;
            if (temp == null || temp.length() == 0) {
                throw new Exception("Veraltete xml");
            }
            return new Boolean(temp).booleanValue();
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return false;
        }
    }

    public void showConnectionDialogOnStartup(boolean show) {
        setAttributeByTagName(new String[] {"options", "dialogzeigen"}
                              , Boolean.toString(show));
    }

    public Level getLogLevel() {
        try {
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "logging", "level"});
            Level result = Level.OFF;
            if (temp.compareToIgnoreCase("INFO") == 0) {
                return Level.INFO;
            }
            else if (temp.compareToIgnoreCase("DEBUG") == 0) {
                return Level.DEBUG;
            }
            else if (temp.compareToIgnoreCase("WARN") == 0) {
                return Level.WARN;
            }
            else if (temp.compareToIgnoreCase("FATAL") == 0) {
                return Level.FATAL;
            }
            else if (temp.compareToIgnoreCase("ALL") == 0) {
                return Level.ALL;
            }
            else if (temp.compareToIgnoreCase("OFF") == 0) {
                return Level.OFF;
            }

            if (logger.isEnabledFor(Level.DEBUG)) {
                logger.debug("Aktueller Loglevel: " + result.toString());
            }
            return result;
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return null;
        }
    }

    public void setLogLevel(Level level) {
        if (level == null) {
            level = Level.OFF;
        }
        String temp = "OFF";
        if (level == Level.ALL) {
            temp = "ALL";
        }
        else if (level == Level.INFO) {
            temp = "INFO";
        }
        else if (level == Level.DEBUG) {
            temp = "DEBUG";
        }
        else if (level == Level.WARN) {
            temp = "WARN";
        }
        else if (level == Level.FATAL) {
            temp = "FATAL";
        }
        setAttributeByTagName(new String[] {"options", "logging", "level"}
                              , temp);
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(level);
        rootLogger.removeAllAppenders();
        if (level != Level.OFF) {
            try {
                FileAppender fileAppender = new FileAppender(AppleJuiceClient.
                    getLoggerHtmlLayout(),
                    AppleJuiceClient.getLoggerFileAppenderPath());
                rootLogger.addAppender(fileAppender);
            }
            catch (IOException ioe) {
                rootLogger.addAppender(new ConsoleAppender());
                ioe.printStackTrace();
            }
        }
        else {
            rootLogger.addAppender(new ConsoleAppender());
        }
        if (logger.isEnabledFor(Level.DEBUG)) {
            logger.debug("Loglevel geaendert in " + level.toString());
        }
    }

    public Settings getSettings() {
        try {
            if (settings == null){
                settings = new Settings();
            }
            Color downloadFertigHintergrundColor = null;
            Color quelleHintergrundColor = null;
            boolean farbenAktiv;
            boolean downloadUebersicht;
            boolean loadPlugins;
            String temp;
            farbenAktiv = getFirstAttrbuteByTagName(new String[] {"options", "farben",
                                             "aktiv"}).equals("true");
            temp = getFirstAttrbuteByTagName(new String[] {"options", "farben",
                                             "hintergrund", "downloadFertig"});
            if (temp.length() != 0) {
                downloadFertigHintergrundColor = new Color(Integer.parseInt(
                    temp));
            }
            temp = getFirstAttrbuteByTagName(new String[] {"options", "farben",
                                             "hintergrund", "quelle"});
            if (temp.length() != 0) {
                quelleHintergrundColor = new Color(Integer.parseInt(temp));
            }
            downloadUebersicht = getFirstAttrbuteByTagName(new String[] {"options",
                                             "download",
                                             "uebersicht"}).equals("true");
            loadPlugins = getFirstAttrbuteByTagName(new String[] {"options",
                                             "loadplugins"}).equals("true");
            settings.setFarbenAktiv(farbenAktiv);
            settings.setDownloadFertigHintergrundColor(downloadFertigHintergrundColor);
            settings.setQuelleHintergrundColor(quelleHintergrundColor);
            settings.setDownloadUebersicht(downloadUebersicht);
            settings.loadPluginsOnStartup(loadPlugins);
            return settings;
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return null;
        }
    }

    public void saveSettings(Settings settings) {
        setAttributeByTagName(new String[] {"options", "farben", "aktiv"}
                              ,
                              Boolean.toString(settings.isFarbenAktiv()));
        setAttributeByTagName(new String[] {"options", "farben", "hintergrund",
                              "downloadFertig"}
                              ,
                              Integer.toString(settings.
            getDownloadFertigHintergrundColor().getRGB()));
        setAttributeByTagName(new String[] {"options", "farben", "hintergrund",
                              "quelle"}
                              ,
                              Integer.toString(settings.
                                               getQuelleHintergrundColor().
                                               getRGB()));
        setAttributeByTagName(new String[] {"options", "download", "uebersicht"}
                              ,
                              Boolean.toString(settings.isDownloadUebersicht()));
        setAttributeByTagName(new String[] {"options", "loadplugins"}
                              ,
                              Boolean.toString(settings.
                                               shouldLoadPluginsOnStartup()));
        informSettingsListener(settings);
    }

    public ConnectionSettings getRemoteSettings() {
        try {
            if (connectionSettings == null){
                connectionSettings = new ConnectionSettings();
            }
            String host = "localhost";
            String passwort = "";
            int xmlPort = 9851;
            host = getFirstAttrbuteByTagName(new String[] {"options", "remote",
                                             "host"});
            passwort = getFirstAttrbuteByTagName(new String[] {"options",
                                                 "remote", "passwort"});
            xmlPort = Integer.parseInt(getFirstAttrbuteByTagName(new String[] {
                "options", "remote",
                "port"}));
            connectionSettings.setHost(host);
            if (passwort.length()==0){
                connectionSettings.setOldPassword("");
            }
            else{
                connectionSettings.setOldMD5Password(passwort);
            }
            connectionSettings.setXmlPort(xmlPort);
            return connectionSettings;
        }
        catch (Exception e) {
            AppleJuiceDialog.rewriteProperties = true;
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            return null;
        }
    }

    public void saveRemote(ConnectionSettings remote) throws
        InvalidPasswordException {
        setAttributeByTagName(new String[] {"options", "remote", "host"}
                              , remote.getHost());
        ApplejuiceFassade.setPassword(remote.getNewPassword());
        setAttributeByTagName(new String[] {"options", "remote", "passwort"}
                              ,
                              remote.getNewPassword());
        setAttributeByTagName(new String[] {"options", "remote", "port"}
                              ,
                              Integer.toString(remote.getXmlPort()));
        informConnectionSettingsListener(getRemoteSettings());
    }

    public void onlySaveRemote(ConnectionSettings remote) {
        setAttributeByTagName(new String[] {"options", "remote", "host"}
                              , remote.getHost());
        setAttributeByTagName(new String[] {"options", "remote", "passwort"}
                              , remote.getNewPassword());
        setAttributeByTagName(new String[] {"options", "remote", "port"}
                              , Integer.toString(remote.getXmlPort()));
        connectionSettings = remote;
    }

    public void saveAJSettings(AJSettings ajSettings) {
        ApplejuiceFassade.getInstance().saveAJSettings(ajSettings);
    }

    private boolean isVeralteteXML() {
        String xmlTest = getFirstAttrbuteByTagName(new String[] {"options", "lookandfeels", "default", "name"});
        if (xmlTest.length() == 0) {
            return true;
        }

        xmlTest = getFirstAttrbuteByTagName(new String[] {"options", "remote0", "host"});
        if (xmlTest.length() == 0) {
            return true;
        }

        return false;
    }


    protected void init() {
        try {
            if (isVeralteteXML()) {
                throw new Exception(
                    "Properties.xml hat altes Format. Wird neu erstellt.");
            }
            String temp = getFirstAttrbuteByTagName(new String[] {"options",
                "location", "x"});
            if (temp.length() != 0) {
                legal = true;
                int mainX = Integer.parseInt(getFirstAttrbuteByTagName(new
                    String[] {"options", "location", "x"}));
                int mainY = Integer.parseInt(getFirstAttrbuteByTagName(new
                    String[] {"options", "location", "y"}));
                mainXY = new Point(mainX, mainY);
                int mainWidth = Integer.parseInt(getFirstAttrbuteByTagName(new
                    String[] {"options", "location", "width"}));
                int mainHeight = Integer.parseInt(getFirstAttrbuteByTagName(new
                    String[] {"options", "location", "height"}));
                mainDimension = new Dimension(mainWidth, mainHeight);
            }
            String xmlTest = getFirstAttrbuteByTagName(new String[] {"options",
                "columns", "download", "column0", "width"});
            if (xmlTest.length() == 0) {
                throw new Exception(
                    "Properties.xml hat altes Format. Wird neu erstellt.");
            }
            downloadWidths = new int[10];
            downloadWidths[0] = Integer.parseInt(xmlTest);
            downloadWidths[1] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column1", "width"}));
            downloadWidths[2] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column2", "width"}));
            downloadWidths[3] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column3", "width"}));
            downloadWidths[4] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column4", "width"}));
            downloadWidths[5] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column5", "width"}));
            downloadWidths[6] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column6", "width"}));
            downloadWidths[7] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column7", "width"}));
            downloadWidths[8] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column8", "width"}));
            downloadWidths[9] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column9", "width"}));

            uploadWidths = new int[8];
            uploadWidths[0] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column0", "width"}));
            uploadWidths[1] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column1", "width"}));
            uploadWidths[2] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column2", "width"}));
            uploadWidths[3] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column3", "width"}));
            uploadWidths[4] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column4", "width"}));
            uploadWidths[5] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column5", "width"}));
            uploadWidths[6] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column6", "width"}));
            uploadWidths[7] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column7", "width"}));

            serverWidths = new int[5];
            serverWidths[0] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "server", "column0", "width"}));
            serverWidths[1] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "server", "column1", "width"}));
            serverWidths[2] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "server", "column2", "width"}));
            serverWidths[3] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "server", "column3", "width"}));
            xmlTest = getFirstAttrbuteByTagName(new String[] {"options",
                                                "columns",
                                                "server", "column4", "width"});
            if (xmlTest.length() == 0) {
                throw new Exception(
                    "Properties.xml hat altes Format. Wird neu erstellt.");
            }
            serverWidths[4] = Integer.parseInt(xmlTest);

            shareWidths = new int[3];
            shareWidths[0] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "share", "column0", "width"}));
            shareWidths[1] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "share", "column1", "width"}));
            shareWidths[2] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "share", "column2", "width"}));

            downloadVisibilities = new boolean[10];
            downloadVisibilities[0] = true;
            xmlTest = getFirstAttrbuteByTagName(new String[] {"options",
                                                "columns", "download",
                                                "column1", "visibility"});
            if (xmlTest.length() == 0) {
                throw new Exception(
                    "Properties.xml hat altes Format. Wird neu erstellt.");
            }
            downloadVisibilities[1] = new Boolean(xmlTest).booleanValue();
            downloadVisibilities[2] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column2",
                "visibility"})).booleanValue();
            downloadVisibilities[3] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column3",
                "visibility"})).booleanValue();
            downloadVisibilities[4] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column4",
                "visibility"})).booleanValue();
            downloadVisibilities[5] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column5",
                "visibility"})).booleanValue();
            downloadVisibilities[6] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column6",
                "visibility"})).booleanValue();
            downloadVisibilities[7] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column7",
                "visibility"})).booleanValue();
            downloadVisibilities[8] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column8",
                "visibility"})).booleanValue();
            downloadVisibilities[9] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column9",
                "visibility"})).booleanValue();

            uploadVisibilities = new boolean[8];
            uploadVisibilities[0] = true;
            uploadVisibilities[1] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column1",
                "visibility"})).booleanValue();
            uploadVisibilities[2] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column2",
                "visibility"})).booleanValue();
            uploadVisibilities[3] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column3",
                "visibility"})).booleanValue();
            uploadVisibilities[4] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column4",
                "visibility"})).booleanValue();
            uploadVisibilities[5] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column5",
                "visibility"})).booleanValue();
            uploadVisibilities[6] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column6",
                "visibility"})).booleanValue();
            uploadVisibilities[7] = new Boolean(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column7",
                "visibility"})).booleanValue();

            downloadIndex = new int[10];
            xmlTest = getFirstAttrbuteByTagName(new String[] {"options",
                                                "columns", "download",
                                                "column0", "index"});
            if (xmlTest.length() == 0) {
                throw new Exception(
                    "Properties.xml hat altes Format. Wird neu erstellt.");
            }
            downloadIndex[0] = Integer.parseInt(xmlTest);
            downloadIndex[1] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column1", "index"}));
            downloadIndex[2] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column2", "index"}));
            downloadIndex[3] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column3", "index"}));
            downloadIndex[4] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column4", "index"}));
            downloadIndex[5] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column5", "index"}));
            downloadIndex[6] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column6", "index"}));
            downloadIndex[7] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column7", "index"}));
            downloadIndex[8] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column8", "index"}));
            downloadIndex[9] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "download", "column9", "index"}));

            uploadIndex = new int[8];
            uploadIndex[0] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column0", "index"}));
            uploadIndex[1] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column1", "index"}));
            uploadIndex[2] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column2", "index"}));
            uploadIndex[3] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column3", "index"}));
            uploadIndex[4] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column4", "index"}));
            uploadIndex[5] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column5", "index"}));
            uploadIndex[6] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column6", "index"}));
            uploadIndex[7] = Integer.parseInt(getFirstAttrbuteByTagName(new
                String[] {"options", "columns", "upload", "column7", "index"}));
            boolean use = new Boolean(getFirstAttrbuteByTagName(new String[] {
                "options", "proxy", "use"})).booleanValue();
            String host = getFirstAttrbuteByTagName(new String[] {"options",
                "proxy", "host"});
            int port;
            try {
                port = Integer.parseInt(getFirstAttrbuteByTagName(new String[] {
                    "options", "proxy", "port"}));
            }
            catch (Exception e) {
                port = -1;
            }
            String userpass = getFirstAttrbuteByTagName(new String[] {"options",
                "proxy", "userpass"});
            proxySettings = new ProxySettings(use, host, port, userpass);
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(PROPERTIES_ERROR_MESSAGE, e);
            }
            if (firstReadError == true) {
                AppleJuiceDialog.restorePropertiesXml();
                AppleJuiceDialog.showInformation(PROPERTIES_ERROR);
                firstReadError = false;
                super.reload(this.path);
                init();
            }
            else {
                AppleJuiceDialog.rewriteProperties = true;
                AppleJuiceDialog.closeWithErrormessage(PROPERTIES_ERROR, false);
            }
        }
    }

    public void save() {
        try {
            setAttributeByTagName(new String[] {"options", "location", "x"}
                                  , mainXY.x);
            setAttributeByTagName(new String[] {"options", "location", "y"}
                                  , mainXY.y);
            setAttributeByTagName(new String[] {"options", "location", "width"}
                                  , mainDimension.width);
            setAttributeByTagName(new String[] {"options", "location", "height"}
                                  , mainDimension.height);

            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column0", "width"}
                                  , downloadWidths[0]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column1", "width"}
                                  , downloadWidths[1]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column2", "width"}
                                  , downloadWidths[2]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column3", "width"}
                                  , downloadWidths[3]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column4", "width"}
                                  , downloadWidths[4]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column5", "width"}
                                  , downloadWidths[5]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column6", "width"}
                                  , downloadWidths[6]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column7", "width"}
                                  , downloadWidths[7]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column8", "width"}
                                  , downloadWidths[8]);
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column9", "width"}
                                  , downloadWidths[9]);

            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column0", "width"}
                                  , uploadWidths[0]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column1", "width"}
                                  , uploadWidths[1]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column2", "width"}
                                  , uploadWidths[2]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column3", "width"}
                                  , uploadWidths[3]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column4", "width"}
                                  , uploadWidths[4]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column5", "width"}
                                  , uploadWidths[5]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column6", "width"}
                                  , uploadWidths[6]);
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column7", "width"}
                                  , uploadWidths[7]);

            setAttributeByTagName(new String[] {"options", "columns", "server",
                                  "column0", "width"}
                                  , serverWidths[0]);
            setAttributeByTagName(new String[] {"options", "columns", "server",
                                  "column1", "width"}
                                  , serverWidths[1]);
            setAttributeByTagName(new String[] {"options", "columns", "server",
                                  "column2", "width"}
                                  , serverWidths[2]);
            setAttributeByTagName(new String[] {"options", "columns", "server",
                                  "column3", "width"}
                                  , serverWidths[3]);
            setAttributeByTagName(new String[] {"options", "columns", "server",
                                  "column4", "width"}
                                  , serverWidths[4]);

            setAttributeByTagName(new String[] {"options", "columns", "share",
                                  "column0", "width"}
                                  , shareWidths[0]);
            setAttributeByTagName(new String[] {"options", "columns", "share",
                                  "column1", "width"}
                                  , shareWidths[1]);
            setAttributeByTagName(new String[] {"options", "columns", "share",
                                  "column2", "width"}
                                  , shareWidths[2]);

            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column1", "visibility"}
                                  , Boolean.toString(downloadVisibilities[1]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column2", "visibility"}
                                  , Boolean.toString(downloadVisibilities[2]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column3", "visibility"}
                                  , Boolean.toString(downloadVisibilities[3]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column4", "visibility"}
                                  , Boolean.toString(downloadVisibilities[4]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column5", "visibility"}
                                  , Boolean.toString(downloadVisibilities[5]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column6", "visibility"}
                                  , Boolean.toString(downloadVisibilities[6]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column7", "visibility"}
                                  , Boolean.toString(downloadVisibilities[7]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column8", "visibility"}
                                  , Boolean.toString(downloadVisibilities[8]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column9", "visibility"}
                                  , Boolean.toString(downloadVisibilities[9]));

            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column1", "visibility"}
                                  , Boolean.toString(uploadVisibilities[1]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column2", "visibility"}
                                  , Boolean.toString(uploadVisibilities[2]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column3", "visibility"}
                                  , Boolean.toString(uploadVisibilities[3]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column4", "visibility"}
                                  , Boolean.toString(uploadVisibilities[4]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column5", "visibility"}
                                  , Boolean.toString(uploadVisibilities[5]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column6", "visibility"}
                                  , Boolean.toString(uploadVisibilities[6]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column7", "visibility"}
                                  , Boolean.toString(uploadVisibilities[7]));

            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column0", "index"}
                                  , Integer.toString(downloadIndex[0]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column1", "index"}
                                  , Integer.toString(downloadIndex[1]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column2", "index"}
                                  , Integer.toString(downloadIndex[2]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column3", "index"}
                                  , Integer.toString(downloadIndex[3]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column4", "index"}
                                  , Integer.toString(downloadIndex[4]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column5", "index"}
                                  , Integer.toString(downloadIndex[5]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column6", "index"}
                                  , Integer.toString(downloadIndex[6]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column7", "index"}
                                  , Integer.toString(downloadIndex[7]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column8", "index"}
                                  , Integer.toString(downloadIndex[8]));
            setAttributeByTagName(new String[] {"options", "columns",
                                  "download", "column9", "index"}
                                  , Integer.toString(downloadIndex[9]));

            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column0", "index"}
                                  , Integer.toString(uploadIndex[0]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column1", "index"}
                                  , Integer.toString(uploadIndex[1]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column2", "index"}
                                  , Integer.toString(uploadIndex[2]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column3", "index"}
                                  , Integer.toString(uploadIndex[3]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column4", "index"}
                                  , Integer.toString(uploadIndex[4]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column5", "index"}
                                  , Integer.toString(uploadIndex[5]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column6", "index"}
                                  , Integer.toString(uploadIndex[6]));
            setAttributeByTagName(new String[] {"options", "columns", "upload",
                                  "column7", "index"}
                                  , Integer.toString(uploadIndex[7]));

            saveDom();
        }
        catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(ApplejuiceFassade.ERROR_MESSAGE, e);
            }
        }
    }

    public void setMainXY(Point p) {
        mainXY = p;
    }

    public Point getMainXY() {
        return mainXY;
    }

    public void setMainDimension(Dimension dimension) {
        mainDimension = dimension;
    }

    public Dimension getMainDimension() {
        return mainDimension;
    }

    public void setDownloadWidths(int[] widths) {
        downloadWidths = widths;
    }

    public boolean isLegal() {
        return legal;
    }

    public int[] getDownloadWidths() {
        return downloadWidths;
    }

    public int[] getUploadWidths() {
        return uploadWidths;
    }

    public void setUploadWidths(int[] uploadWidths) {
        this.uploadWidths = uploadWidths;
    }

    public int[] getServerWidths() {
        return serverWidths;
    }

    public void setServerWidths(int[] serverWidths) {
        this.serverWidths = serverWidths;
    }

    public int[] getShareWidths() {
        return shareWidths;
    }

    public void setShareWidths(int[] shareWidths) {
        this.shareWidths = shareWidths;
    }

    public void setDownloadColumnVisible(int column, boolean visible) {
        if (column != 0) {
            downloadVisibilities[column] = visible;
        }
    }

    public boolean[] getDownloadColumnVisibilities() {
        return downloadVisibilities;
    }

    public void setDownloadColumnIndex(int column, int index) {
        downloadIndex[column] = index;
    }

    public int[] getDownloadColumnIndizes() {
        return downloadIndex;
    }

    public void setUploadColumnIndex(int column, int index) {
        uploadIndex[column] = index;
    }

    public int[] getUploadColumnIndizes() {
        return uploadIndex;
    }

    public void setUploadColumnVisible(int column, boolean visible) {
        if (column != 0) {
            uploadVisibilities[column] = visible;
        }
    }

    public boolean[] getUploadColumnVisibilities() {
        return uploadVisibilities;
    }

    public ConnectionSettings[] getConnectionsSet() {
        ArrayList connectionSet = new ArrayList();
        for (int i = 0; i < 3; i++) {
            ConnectionSettings temp = new ConnectionSettings();
            temp.setHost(getFirstAttrbuteByTagName(new String[] {"options", "remote"+i, "host"}));
            temp.setXmlPort(Integer.parseInt(getFirstAttrbuteByTagName(new String[] {"options", "remote"+i, "port"})));
            if ("".equals(temp.getHost()))
                break;
            connectionSet.add(temp);
        }
        return (ConnectionSettings[])connectionSet.toArray(new ConnectionSettings[]{});
    }

    public void setConnectionsSet(ConnectionSettings[] set) {
        for (int i = 0; i < 3; i++) {
            if ((set.length-1 < i) || ("".equals(set[i].getHost()))) {
                setAttributeByTagName(new String[] {"options", "remote"+i, "host"}, "");
                setAttributeByTagName(new String[] {"options", "remote"+i, "port"}, "0");
            } else {
                setAttributeByTagName(new String[] {"options", "remote"+i, "host"}, set[i].getHost());
                setAttributeByTagName(new String[] {"options", "remote"+i, "port"}, Integer.toString(set[i].getXmlPort()));
            }
        }
    }
}
