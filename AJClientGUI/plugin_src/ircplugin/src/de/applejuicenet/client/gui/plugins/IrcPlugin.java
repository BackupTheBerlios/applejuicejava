package de.applejuicenet.client.gui.plugins;

import java.awt.BorderLayout;

import de.applejuicenet.client.gui.plugins.ircplugin.XdccIrc;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/plugin_src/ircplugin/src/de/applejuicenet/client/gui/plugins/Attic/IrcPlugin.java,v 1.4 2003/10/27 14:10:07 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: IrcPlugin.java,v $
 * Revision 1.4  2003/10/27 14:10:07  maj0r
 * Header eingefuegt.
 *
 *
 */

public class IrcPlugin extends PluginConnector {

    public IrcPlugin() {
        setLayout(new BorderLayout());
        add(new XdccIrc(), BorderLayout.CENTER);
        initIcon();
    }

    public void fireLanguageChanged() {
    }

    /*Wird automatisch aufgerufen, wenn neue Informationen vom Server eingegangen sind.
      �ber den DataManger k�nnen diese abgerufen werden.*/
    public void fireContentChanged(int type, Object content) {
    }

    public void registerSelected() {
    }

    public String getTitle() {
        return "ajIRC";
    }

    public String getAutor() {
        return "Maj0r";
    }

    public String getBeschreibung() {
        return "Dies ist das absolut erste Plugin f�r das appleJuice-Java-GUI.\r\n\r\n"
                + "Der IRC-Client unterst�tzt das Besuchen mehrerer R�ume, Queries, usw.";
    }

    public String getVersion() {
        return "1.23";
    }

    public boolean istReiter() {
        return true;
    }
}