package de.applejuicenet.client.fassade.controller.dac;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/ajcorefassade/src/de/applejuicenet/client/fassade/controller/dac/Attic/ServerDO.java,v 1.1 2004/12/03 07:57:12 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r [aj@tkl-soft.de]
 *
 */

public class ServerDO {
    private final int id;
    private String host;
    private String name;
    private String port;
    private long timeLastSeen;
    private int versuche;
    private boolean connected = false;
    private boolean tryConnect = false;
    private static SimpleDateFormat formatter = new SimpleDateFormat(
        "dd.MM.yyyy HH:mm:ss");

    public ServerDO(int id){
        this.id = id;
    }

    public ServerDO(int id, String name, String host, String port,
                    long lastSeen, int versuche) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.versuche = versuche;
        timeLastSeen = lastSeen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public long getTimeLastSeen() {
        return timeLastSeen;
    }

    public int getVersuche() {
        return versuche;
    }

    public void setVersuche(int versuche) {
        this.versuche = versuche;
    }

    public void setTimeLastSeen(long timeLastSeen) {
        this.timeLastSeen = timeLastSeen;
    }

    public int getID() {
        return id;
    }

    public String getIDasString() {
        return Integer.toString(id);
    }

    public String getTimeLastSeenAsString() {
        if (timeLastSeen == 0) {
            return "";
        }
        else {
            return formatter.format(new Date(timeLastSeen));
        }
    }

    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        return (id == ( (ServerDO) obj).getID());
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isTryConnect() {
        return tryConnect;
    }

    public void setTryConnect(boolean tryConnect) {
        this.tryConnect = tryConnect;
    }
}