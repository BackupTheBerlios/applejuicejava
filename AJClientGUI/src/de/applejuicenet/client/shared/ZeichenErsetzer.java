package de.applejuicenet.client.shared;

/**
 * <p>Title: AppleJuice Client-GUI</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Maj0r
 * @version 1.0
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