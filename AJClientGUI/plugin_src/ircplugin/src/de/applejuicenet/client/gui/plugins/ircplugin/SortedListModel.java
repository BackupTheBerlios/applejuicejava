package de.applejuicenet.client.gui.plugins.ircplugin;

import javax.swing.*;
import java.util.*;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/plugin_src/ircplugin/src/de/applejuicenet/client/gui/plugins/ircplugin/SortedListModel.java,v 1.2 2003/10/27 16:01:24 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: SortedListModel.java,v $
 * Revision 1.2  2003/10/27 16:01:24  maj0r
 * Benutzerliste wird nun bei Ver�nderung aktualisiert und halbwegs richtig sortiert (Status wird noch nicht ber�cksichtigt).
 *
 * Revision 1.1  2003/09/12 06:32:17  maj0r
 * Nur verschoben.
 *
 * Revision 1.2  2003/08/28 15:53:02  maj0r
 * NullPointer behoben und Header eingefuegt.
 *
 *
 */

public class SortedListModel extends AbstractListModel {

    private SortedSet model;

    public SortedListModel() {
        model = new TreeSet(new StringComparator());
    }

    public int getSize() {
        return model.size();
    }

    public Object getElementAt(int index) {
        if (index < model.size())
            return model.toArray()[index];
        else
            return null;
    }

    public void add(Object element) {
        if (model.add(element))
        {
            fireIntervalAdded(this, 0, getSize());
        }
    }

    public void clear() {
        model.clear();
        fireIntervalRemoved(this, 0, getSize());
    }

    public boolean contains(Object element) {
        return model.contains(element);
    }

    public Object firstElement() {
        return model.first();
    }

    public Object lastElement() {
        return model.last();
    }

    public boolean remove(Object element) {
        boolean removed = model.remove(element);
        if (removed)
        {
            fireIntervalRemoved(this, 0, getSize());
        }
        return removed;
    }

    private class StringComparator implements Comparator{
        public int compare(Object o1, Object o2) {
            if (o1.getClass()==String.class && o2.getClass()==String.class){
                return ((String)o1).compareToIgnoreCase((String)o2);
            }
            else{
                if (o1.hashCode()==o2.hashCode()){
                    return 0;
                }
                else if (o1.hashCode()<=o2.hashCode()){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        }
    }
}
