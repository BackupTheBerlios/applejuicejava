package de.applejuicenet.client.fassade.controller.xml;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/ajcorefassade/src/de/applejuicenet/client/fassade/controller/xml/GetObjectXMLHolder.java,v 1.2 2005/01/18 17:34:32 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 */

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.applejuicenet.client.fassade.controller.CoreConnectionSettingsHolder;
import de.applejuicenet.client.fassade.controller.dac.DownloadDO;
import de.applejuicenet.client.fassade.controller.dac.DownloadSourceDO;
import de.applejuicenet.client.fassade.controller.dac.InformationDO;
import de.applejuicenet.client.fassade.controller.dac.ServerDO;
import de.applejuicenet.client.fassade.controller.dac.ShareDO;
import de.applejuicenet.client.fassade.controller.dac.UploadDO;
import de.applejuicenet.client.fassade.shared.Version;

public class GetObjectXMLHolder extends WebXMLParser {

	public GetObjectXMLHolder(CoreConnectionSettingsHolder coreHolder) {
		super(coreHolder, "/xml/getobject.xml", "");
	}

	public void update() {
	}

	private Object getShareObject(NodeList nodes) {
		int id_key;
		String filename = null;
		String shortfilename = null;
		long size;
		String checksum = null;
		ShareDO shareDO = null;
		int prioritaet;
		Element e = null;
		e = (Element) nodes.item(0);
		id_key = Integer.parseInt(e.getAttribute("id"));
		filename = e.getAttribute("filename");
		shortfilename = e.getAttribute("shortfilename");
		size = Long.parseLong(e.getAttribute("size"));
		checksum = e.getAttribute("checksum");
		prioritaet = Integer.parseInt(e.getAttribute("priority"));
        long lastAsked = Long.parseLong(e.getAttribute("lastasked"));
        long askCount = Long.parseLong(e.getAttribute("askcount"));
        long searchCount = Long.parseLong(e.getAttribute("searchcount"));
        shareDO = new ShareDO(id_key, filename, shortfilename, size, checksum,
                              prioritaet, lastAsked, askCount, searchCount);
		return shareDO;
	}

	private Object getDownloadObject(NodeList nodes) {
		DownloadDO downloadDO = null;
		Element e = (Element) nodes.item(0);
		int id = Integer.parseInt(e.getAttribute("id"));
		int shareid = Integer.parseInt(e.getAttribute("shareid"));
		String hash = e.getAttribute("hash");
		long fileSize = Long.parseLong(e.getAttribute("size"));
		long sizeReady = Long.parseLong(e.getAttribute("ready"));
		String temp = e.getAttribute("status");
		int status = Integer.parseInt(temp);
		String filename = e.getAttribute("filename");
		String targetDirectory = e.getAttribute("targetdirectory");
		temp = e.getAttribute("powerdownload");
		int powerDownload = Integer.parseInt(temp);
		temp = e.getAttribute("temporaryfilenumber");
		int temporaryFileNumber = Integer.parseInt(temp);
		downloadDO = new DownloadDO(id, shareid, hash, fileSize, sizeReady,
				status, filename, targetDirectory, powerDownload,
				temporaryFileNumber);
		return downloadDO;
	}

	private Object getInformationObject(NodeList nodes) {
		long sessionUpload;
		long sessionDownload;
		long credits;
		long uploadSpeed;
		long downloadSpeed;
		long openConnections;
		Element e = (Element) nodes.item(0);
		int id = Integer.parseInt(e.getAttribute("id"));
		credits = Long.parseLong(e.getAttribute("credits"));
		uploadSpeed = Long.parseLong(e.getAttribute("uploadspeed"));
		downloadSpeed = Long.parseLong(e.getAttribute("downloadspeed"));
		openConnections = Long.parseLong(e.getAttribute("openconnections"));
		sessionUpload = Long.parseLong(e.getAttribute("sessionupload"));
		sessionDownload = Long.parseLong(e.getAttribute("sessiondownload"));
		InformationDO information = new InformationDO(id, sessionUpload,
				sessionDownload, credits, uploadSpeed, downloadSpeed,
				openConnections);
		return information;
	}

	private Object getUploadObject(NodeList nodes) {
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
		long lastConnection;
		int speed;
		int directstate;
		double loaded;
		e = (Element) nodes.item(0);
		id = Integer.parseInt(e.getAttribute("id"));
		shareId = Integer.parseInt(e.getAttribute("shareid"));
		versionsNr = e.getAttribute("version");
		if (versionsNr.compareToIgnoreCase("0.0.0.0") == 0) {
			version = null;
		} else {
			os = Integer.parseInt(e.getAttribute("operatingsystem"));
			version = new Version(versionsNr, os);
		}
		prioritaet = Integer.parseInt(e.getAttribute("priority"));
		nick = e.getAttribute("nick");
		status = e.getAttribute("status");
		uploadFrom = Long.parseLong(e.getAttribute("uploadfrom"));
		uploadTo = Long.parseLong(e.getAttribute("uploadto"));
		actualUploadPos = Long
				.parseLong(e.getAttribute("actualuploadposition"));
		speed = Integer.parseInt(e.getAttribute("speed"));
		directstate = Integer.parseInt(e.getAttribute("directstate"));
		lastConnection = Long.parseLong(e.getAttribute("lastconnection"));
		loaded = Double.parseDouble(e.getAttribute("loaded"));
		upload = new UploadDO(id, shareId, version, status, nick, uploadFrom,
				uploadTo, actualUploadPos, speed, prioritaet, directstate,
				lastConnection, loaded);
		return upload;
	}

	private Object getDownloadSourceObject(NodeList nodes) {
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
		Element e = (Element) nodes.item(0);
		int id = Integer.parseInt(e.getAttribute("id"));
		String temp = e.getAttribute("status");
		int status = Integer.parseInt(temp);
		temp = e.getAttribute("directstate");
		int directstate = Integer.parseInt(temp);
		if (status == DownloadSourceDO.UEBERTRAGUNG) {
			temp = e.getAttribute("downloadfrom");
			downloadFrom = Integer.parseInt(temp);
			temp = e.getAttribute("downloadto");
			downloadTo = Integer.parseInt(temp);
			temp = e.getAttribute("actualdownloadposition");
			actualDownloadPosition = Integer.parseInt(temp);
			temp = e.getAttribute("speed");
			speed = Integer.parseInt(temp);
		} else {
			downloadFrom = -1;
			downloadTo = -1;
			actualDownloadPosition = -1;
			speed = 0;
		}
		versionNr = e.getAttribute("version");
		if (versionNr.compareToIgnoreCase("0.0.0.0") == 0) {
			version = null;
		} else {
			temp = e.getAttribute("operatingsystem");
			os = Integer.parseInt(temp);
			version = new Version(versionNr, os);
		}
		temp = e.getAttribute("queueposition");
		queuePosition = Integer.parseInt(temp);
		temp = e.getAttribute("powerdownload");
		int powerDownload = Integer.parseInt(temp);
		String filename = e.getAttribute("filename");
		nickname = e.getAttribute("nickname");
		temp = e.getAttribute("downloadid");
		downloadId = Integer.parseInt(temp);
        temp = e.getAttribute("source");
        int herkunft = Integer.parseInt(temp);
		DownloadSourceDO downloadSourceDO = new DownloadSourceDO(id, status,
				directstate, downloadFrom, downloadTo, actualDownloadPosition,
				speed, version, queuePosition, powerDownload, filename,
				nickname, downloadId, herkunft);
		return downloadSourceDO;
	}

	private Object getServerObject(NodeList nodes) {
		Element e = null;
		String id_key = null;
		int id;
		String name = null;
		String host = null;
		long lastseen;
		String port = null;
		e = (Element) nodes.item(0);
		id_key = e.getAttribute("id");
		id = Integer.parseInt(id_key);
		name = e.getAttribute("name");
		host = e.getAttribute("host");
		lastseen = Long.parseLong(e.getAttribute("lastseen"));
		port = e.getAttribute("port");
		int versuche = Integer.parseInt(e.getAttribute("connectiontry"));
		ServerDO server = new ServerDO(id, name, host, port, lastseen, versuche);
		return server;
	}

	public Object getObjectByID(int id) {
		try {
			reload("id=" + id, false);
			NodeList nodes = document.getElementsByTagName("share");
			if (nodes.getLength() == 1) {
				return getShareObject(nodes);
			}
			nodes = document.getElementsByTagName("download");
			if (nodes.getLength() == 1) {
				return getDownloadObject(nodes);
			}
			nodes = document.getElementsByTagName("upload");
			if (nodes.getLength() == 1) {
				return getUploadObject(nodes);
			}
			nodes = document.getElementsByTagName("user");
			if (nodes.getLength() == 1) {
				return getDownloadSourceObject(nodes);
			}
			nodes = document.getElementsByTagName("server");
			if (nodes.getLength() == 1) {
				return getServerObject(nodes);
			}
			nodes = document.getElementsByTagName("information");
			if (nodes.getLength() == 1) {
				getInformationObject(nodes);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
