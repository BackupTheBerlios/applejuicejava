package de.applejuicenet.client.shared;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/shared/Attic/ZeichenErsetzer.java,v 1.5 2003/06/10 12:31:03 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <AJCoreGUI@maj0r.de>
 *
 * $Log: ZeichenErsetzer.java,v $
 * Revision 1.5  2003/06/10 12:31:03  maj0r
 * Historie eingef�gt.
 *
 *
 */

public abstract class ZeichenErsetzer {
  public static String korrigiereUmlaute(String text) {
    if (text == null) {
      return "";
    }
    String result = text.replaceAll("&uuml;", "�");
    result = result.replaceAll("&Uuml;", "�");
    result = result.replaceAll("&auml;", "�");
    result = result.replaceAll("&Auml;", "�");
    result = result.replaceAll("&ouml;", "�");
    result = result.replaceAll("&Ouml;", "�");
    result = result.replaceAll("&szlig;", "�");
    result = result.replaceAll("&amp;", "");
    result = result.replaceAll("&lt;", "<");
    result = result.replaceAll("&gt;", ">");
    return result;
  }

  public static String korrigiereUmlaute(String text, boolean revers) {
    if (text == null) {
      return "";
    }
    if (!revers) {
      return korrigiereUmlaute(text);
    }
    String result = text.replaceAll("�", "&uuml;");
    result = result.replaceAll("�", "&Uuml;");
    result = result.replaceAll("�", "&auml;");
    result = result.replaceAll("�", "&Auml;");
    result = result.replaceAll("�", "&ouml;");
    result = result.replaceAll("�", "&Ouml;");
    result = result.replaceAll("�", "&szlig;");
    result = result.replaceAll("&", "&amp;");
    result = result.replaceAll("<", "&lt;");
    result = result.replaceAll(">", "&gt;");
    return result;
  }
}