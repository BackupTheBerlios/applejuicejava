package de.applejuicenet.client.gui.plugins;

import javax.swing.ImageIcon;
import java.util.HashMap;
import java.awt.BorderLayout;
import de.applejuicenet.client.gui.plugins.ircplugin.XdccIrc;

/**
 * <p>Title: AppleJuice Client-GUI</p>
 * <p>Description: ajIRC-Plugin</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Maj0r
 * @version 1.0
 */

public class AppleJuicePlugin extends PluginConnector {

  public AppleJuicePlugin(){
    setLayout(new BorderLayout());
    add(new XdccIrc(), BorderLayout.CENTER);
  }

  public void fireLanguageChanged(){
//    System.out.println("Sprache wurde ge�ndert");
  }

  public void fireContentChanged(int type, HashMap content){
//    System.out.println("Neue Daten vom Server");
  }

  public void registerSelected(){
//    System.out.println("Reiter wurde angeklickt.");
  }

  public String getTitle(){
    return "ajIRC";
  }

  public String getAutor(){
    return "Maj0r";
  }

  public String getBeschreibung(){
    return "Dies ist das absolut erste Plugin f�r die appleJuice-Java-GUI.\r\n\r\n"
        + "Der IRC-Client unterst�tzt das Besuchen mehrerer R�ume, Queries, usw.";
  }

  public String getVersion(){
    return "1.0";
  }

  public boolean istReiter(){
    return true;
  }
}