package de.applejuicenet.client.fassade.shared;

/**
 * $Header:
 * /cvsroot/applejuicejava/ajcorefassade/src/de/applejuicenet/client/fassade/shared/ShareEntry.java,v
 * 1.1 2004/12/03 07:57:12 maj0r Exp $
 * 
 * <p>
 * Titel: AppleJuice Client-GUI
 * </p>
 * <p>
 * Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten
 * appleJuice-Core
 * </p>
 * <p>
 * Copyright: General Public License
 * </p>
 * 
 * @author: Maj0r <aj@tkl-soft.de>
 * 
 */

public class ShareEntry {
	public static final int SUBDIRECTORY = 0;

	public static final int SINGLEDIRECTORY = 1;

	private static final String sSUBDIRECTORY = "subdirectory";

	private static final String sSINGLEDIRECTORY = "singledirectory";

	private String dir;

	private int shareMode;

	public ShareEntry(String dir, int shareMode) {
		this.dir = dir;
		this.shareMode = shareMode;
	}

	public ShareEntry(String dir, String shareMode) {
		this.dir = dir;
		setShareMode(shareMode);
	}

	public String toString() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setShareMode(int shareMode) {
		this.shareMode = shareMode;
	}

	private void setShareMode(String shareMode) {
		if (shareMode.compareToIgnoreCase(sSUBDIRECTORY) == 0) {
			this.shareMode = SUBDIRECTORY;
		} else if (shareMode.compareToIgnoreCase(sSINGLEDIRECTORY) == 0) {
			this.shareMode = SINGLEDIRECTORY;
		} else {
			this.shareMode = -1;
		}
	}

	public String getDir() {
		return dir;
	}

	public int getShareMode() {
		return shareMode;
	}
}