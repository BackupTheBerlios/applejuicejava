package de.applejuicenet.client.shared;

import java.util.*;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/shared/Attic/AJSettings.java,v 1.3 2003/06/10 12:31:03 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <AJCoreGUI@maj0r.de>
 *
 * $Log: AJSettings.java,v $
 * Revision 1.3  2003/06/10 12:31:03  maj0r
 * Historie eingef�gt.
 *
 *
 */

public class AJSettings {
  private String nick;
  private long port;
  private long xmlPort;
  private boolean allowBrowse;
  private long maxUpload;
  private long maxDownload;
  private int speedPerSlot;
  private String incomingDir;
  private String tempDir;
  private HashSet shareDirs;

  public AJSettings(String nick, long port, long xmlPort,
                    boolean allowBrowse, long maxUpload, long maxDownload,
                    int speedPerSlot, String incomingDir, String tempDir,
                    HashSet shareDirs) {
    this.nick = nick;
    this.port = port;
    this.xmlPort = xmlPort;
    this.allowBrowse = allowBrowse;
    this.maxUpload = maxUpload;
    this.maxDownload = maxDownload;
    this.speedPerSlot = speedPerSlot;
    this.incomingDir = incomingDir;
    this.tempDir = tempDir;
    this.shareDirs = shareDirs;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public long getPort() {
    return port;
  }

  public void setPort(long port) {
    this.port = port;
  }

  public long getXMLPort() {
    return xmlPort;
  }

  public void setXMLPort(long xmlPort) {
    this.xmlPort = xmlPort;
  }

  public boolean isBrowseAllowed() {
    return allowBrowse;
  }

  public void setBrowseAllowed(boolean allow) {
    this.allowBrowse = allow;
  }

  public long getMaxUpload() {
    return maxUpload;
  }

  public long getMaxUploadInKB() {
    return maxUpload / 1024;
  }

  public void setMaxUpload(long maxUpload) {
    this.maxUpload = maxUpload;
  }

  public long getMaxDownload() {
    return maxDownload;
  }

  public long getMaxDownloadInKB() {
    return maxDownload / 1024;
  }

  public void setMaxDownload(long maxDownload) {
    this.maxDownload = maxDownload;
  }

  public int getSpeedPerSlot() {
    return speedPerSlot;
  }

  public void setSpeedPerSlot(int speedPerSlot) {
    this.speedPerSlot = speedPerSlot;
  }

  public String getIncomingDir() {
    return incomingDir;
  }

  public void setIncomingDir(String incomingDir) {
    this.incomingDir = incomingDir;
  }

  public String getTempDir() {
    return tempDir;
  }

  public void setTempDir(String tempDir) {
    this.tempDir = tempDir;
  }

  public HashSet getShareDirs() {
    return shareDirs;
  }

  public void setShareDirs(HashSet shareDirs) {
    this.shareDirs = shareDirs;
  }
}