package de.applejuicenet.client.gui.tables.share;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.applejuicenet.client.gui.controller.ApplejuiceFassade;
import de.applejuicenet.client.gui.tables.Node;
import de.applejuicenet.client.shared.IconManager;
import de.applejuicenet.client.shared.MapSetStringKey;
import de.applejuicenet.client.shared.dac.ShareDO;
import java.util.ArrayList;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/tables/share/Attic/ShareNode.java,v 1.12 2003/12/16 18:30:06 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <AJCoreGUI@maj0r.de>
 *
 * $Log: ShareNode.java,v $
 * Revision 1.12  2003/12/16 18:30:06  maj0r
 * Nun ist es auch wieder plattformunabhaengig.
 *
 * Revision 1.11  2003/12/16 17:05:54  maj0r
 * Sharetabelle auf vielfachen Wunsch komplett �berarbeitet.
 *
 * Revision 1.10  2003/10/21 14:08:45  maj0r
 * Mittels PMD Code verschoenert, optimiert.
 *
 * Revision 1.9  2003/09/01 15:50:52  maj0r
 * Wo es moeglich war, DOs auf primitive Datentypen umgebaut.
 *
 * Revision 1.8  2003/08/27 11:19:30  maj0r
 * Prioritaet setzen und aufheben vollstaendig implementiert.
 * Button f�r 'Share erneuern' eingefuehrt.
 *
 * Revision 1.7  2003/08/26 06:20:10  maj0r
 * Anpassungen an muhs neuen Tree.
 *
 * Revision 1.6  2003/08/25 19:28:52  maj0r
 * Anpassungen an muhs neuen Tree.
 *
 * Revision 1.5  2003/08/25 07:23:25  maj0r
 * Kleine Korrekturen.
 *
 * Revision 1.4  2003/08/24 14:59:59  maj0r
 * Version 0.14
 * Diverse Aenderungen.
 *
 * Revision 1.3  2003/08/15 14:44:48  maj0r
 * Schreibfehler.
 *
 * Revision 1.2  2003/08/14 20:08:42  maj0r
 * Tree fuer Shareauswahl eingefuegt, aber noch nicht fertiggestellt.
 *
 * Revision 1.1  2003/07/02 13:54:34  maj0r
 * JTreeTable komplett �berarbeitet.
 *
 *
 */

public class ShareNode
    implements Node {
    private static ImageIcon leafIcon;
    private static ImageIcon treeIcon;

    static {
        IconManager im = IconManager.getInstance();
        leafIcon = im.getIcon("treeRoot");
        treeIcon = im.getIcon("tree");
    }

    private ShareDO shareDO;
    private HashMap children = new HashMap();
    private ShareNode parent;
    private String path;

    public ShareNode(ShareNode parent, ShareDO shareDO) {
        this.parent = parent;
        path = "";
        if (parent != null) {
            String bisherigerPath = getCompletePath();
            String restPath = shareDO.getFilename();
            while (restPath.indexOf(ApplejuiceFassade.separator)==0){
                restPath = restPath.substring(1);
            }
            if (bisherigerPath.length()!=0 && restPath.substring(0, bisherigerPath.
                length()).compareTo(bisherigerPath)==0){
                restPath = restPath.substring(bisherigerPath.
                    length());
            }
            if (restPath.substring(0, 1).compareTo(ApplejuiceFassade.separator)==0){
                restPath = restPath.substring(1);
            }
            int pos = restPath.indexOf(ApplejuiceFassade.separator);
            if (pos != -1) {
                path = restPath.substring(0, pos);
            }
            else {
                this.shareDO = shareDO;
            }
        }
    }

    public ShareNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return (shareDO != null);
    }

    public String getPath() {
        return path;
    }

    public String getCompletePath() {
        if (parent != null) {
            String parentPath = parent.getCompletePath();
            if (parentPath.length()==0){
                return path;
            }
            else{
                return parentPath + ApplejuiceFassade.separator + path;
            }
        }
        else {
            return path;
        }
    }

    public Icon getConvenientIcon() {
        if (path.length() == 0) {
            return leafIcon;
        }
        return treeIcon;
    }

    public ShareNode addChild(ShareDO shareDOtoAdd) {
        String bisherigerPath = getCompletePath();
        String restPath = shareDOtoAdd.getFilename();
        while (restPath.indexOf(ApplejuiceFassade.separator)==0){
            restPath = restPath.substring(1);
        }
        restPath = restPath.substring(bisherigerPath.length());
        int pos = restPath.indexOf(ApplejuiceFassade.separator);
        while (pos == 0) {
            restPath = restPath.substring(pos + 1);
            pos = restPath.indexOf(ApplejuiceFassade.separator);
        }
        ShareNode childNode = null;
        if (pos != -1) {
            String tmpPath = restPath.substring(0, pos);
            MapSetStringKey aKey = new MapSetStringKey(tmpPath);
            if (children.containsKey(aKey)) {
                childNode = (ShareNode) children.get(aKey);
                childNode.addChild(shareDOtoAdd);
            }
            else {
                childNode = new ShareNode(this, shareDOtoAdd);
                children.put(aKey, childNode);
                childNode.addChild(shareDOtoAdd);
            }
        }
        else {
            childNode = new ShareNode(this, shareDOtoAdd);
            children.put(new MapSetStringKey(shareDOtoAdd.getId()), childNode);
        }
        return childNode;
    }

    public ShareDO getDO() {
        return shareDO;
    }

    public void setParent(ShareNode parentNode) {
        parent = parentNode;
    }

    public String toString() {
        if (isLeaf() && parent != null) {
            return getDO().getShortfilename();
        }
        else if (parent != null) {
            return path;
        }
        else {
            return "";
        }
    }

    public HashMap getChildrenMap() {
        return children;
    }

    public void removeChild(ShareNode toRemove) {
        children.remove(new MapSetStringKey(toRemove.getDO().getId()));
    }

    public void removeAllChildren() {
        children.clear();
    }

    protected Object[] getChildren() {
        ShareNode[] shareNodes = (ShareNode[]) children.values().toArray(new ShareNode[children.size()]);
        return sort(shareNodes);
    }

    private Object[] sort(ShareNode[] childNodes) {
        int n = childNodes.length;
        ShareNode tmp;
        for (int i = 0; i < n - 1; i++) {
            int k = i;
            for (int j = i + 1; j < n; j++) {
                if (childNodes[j].toString().compareToIgnoreCase(childNodes[k].toString()) < 0) {
                    k = j;
                }
            }
            tmp = childNodes[i];
            childNodes[i] = childNodes[k];
            childNodes[k] = tmp;
        }
        return childNodes;
    }

    public void setPriority(int prio) {
        if (isLeaf()) {
            shareDO.setPrioritaet(prio);
            ApplejuiceFassade.getInstance().setPrioritaet(shareDO.getId(), prio);
        }
        else {
            Iterator it = children.values().iterator();
            while (it.hasNext()) {
                ( (ShareNode) it.next()).setPriority(prio);
            }
        }
    }
}
