package de.applejuicenet.client.gui;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/RegisterI.java,v 1.3 2003/12/17 11:06:15 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: RegisterI.java,v $
 * Revision 1.3  2003/12/17 11:06:15  maj0r
 * RegisterI erweitert, um auf Verlassen eines Tabs reagieren zu koennen.
 *
 * Revision 1.2  2003/06/10 12:31:03  maj0r
 * Historie eingefuegt.
 *
 *
 */

public interface RegisterI {
  public void registerSelected();
  public void lostSelection();
}