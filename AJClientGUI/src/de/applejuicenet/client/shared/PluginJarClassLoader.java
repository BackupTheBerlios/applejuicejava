package de.applejuicenet.client.shared;

import java.net.*;
import java.io.File;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

import de.applejuicenet.client.gui.plugins.*;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/shared/PluginJarClassLoader.java,v 1.12 2004/01/14 15:25:25 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI fuer den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r aj@tkl-soft.de>
 *
 * $Log: PluginJarClassLoader.java,v $
 * Revision 1.12  2004/01/14 15:25:25  maj0r
 * Loggerausgabe eingebaut.
 *
 * Revision 1.11  2004/01/14 15:19:59  maj0r
 * Laden von Plugins verbessert.
 * Muell oder nicht standardkonforme Plugins im Plugin-Ordner werden nun korrekt behandelt.
 *
 * Revision 1.10  2003/12/29 16:04:17  maj0r
 * Header korrigiert.
 *
 * Revision 1.9  2003/10/21 14:08:45  maj0r
 * Mittels PMD Code verschoenert, optimiert.
 *
 * Revision 1.8  2003/08/23 11:16:35  maj0r
 * Plattformunabhaengigkeit wieder hergestellt.
 *
 * Revision 1.7  2003/08/20 10:52:51  maj0r
 * JarClassloader korrigiert.
 *
 * Revision 1.6  2003/08/16 17:58:58  maj0r
 * Diverse Farben k�nnen nun manuell eingestellt bzw. deaktiviert werden.
 * DownloaduebersichtTabelle kann deaktiviert werden.
 *
 * Revision 1.5  2003/07/01 14:51:28  maj0r
 * Unn�tzen Krimskram entfernt.
 *
 * Revision 1.4  2003/06/10 12:31:03  maj0r
 * Historie eingef�gt.
 *
 *
 */

public class PluginJarClassLoader
        extends URLClassLoader {
    private URL url;
    private Logger logger;

    public PluginJarClassLoader(URL url) {
        super(new URL[]{url});
        this.url = url;
        logger = Logger.getLogger(getClass());
    }

    public PluginConnector getPlugin(String jar) throws Exception {
        File aJar = new File(jar);
        try{
            String theClassName = jar.substring(jar.lastIndexOf(File.
                separatorChar) + 1, jar.lastIndexOf(".jar"));
            loadClassBytesFromJar(aJar);
            Class cl = loadClass("de.applejuicenet.client.gui.plugins." +
                                 theClassName);
            Object aPlugin = cl.newInstance();
            return (PluginConnector) aPlugin;
        }
        catch(Exception e){
            if (logger.isEnabledFor(Level.INFO))
                logger.info("Plugin " + jar + " entspricht nicht dem Standard und wurde nicht geladen.");
            return null;
        }
    }

    private void loadClassBytesFromJar(File jar) throws Exception {
        if (!jar.isFile())
            return;

        JarFile jf = new JarFile(jar);
        String entryName;
        Vector classes = new Vector();

        for (Enumeration e = jf.entries(); e.hasMoreElements();)
        {
            ZipEntry entry = (ZipEntry) e.nextElement();
            entryName = entry.getName();
            if (entryName.indexOf(".class") == -1)
            {
                continue;
            }
            InputStream is = jf.getInputStream(entry);
            int l = (int) entry.getSize();
            byte[] buf = new byte[l];
            int read = 0;

            while (read < l)
            {
                int incr = is.read(buf, read, l - read);
                read += incr;
            }
            String name = entryName.replace('/', '.');
            name = name.replaceAll(".class", "");
            defineClass(name, buf, 0, buf.length);
            classes.add(name);
        }
    }
}