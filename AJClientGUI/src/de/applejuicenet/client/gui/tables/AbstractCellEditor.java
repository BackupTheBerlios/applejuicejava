package de.applejuicenet.client.gui.tables;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.EventObject;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/tables/Attic/AbstractCellEditor.java,v 1.5 2003/12/29 16:04:17 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: AbstractCellEditor.java,v $
 * Revision 1.5  2003/12/29 16:04:17  maj0r
 * Header korrigiert.
 *
 * Revision 1.4  2003/10/21 14:08:45  maj0r
 * Mittels PMD Code verschoenert, optimiert.
 *
 * Revision 1.3  2003/07/02 13:54:34  maj0r
 * JTreeTable komplett �berarbeitet.
 *
 *
 */

public class AbstractCellEditor implements CellEditor {

    protected EventListenerList listenerList = new EventListenerList();

    public Object getCellEditorValue() { return null; }
    public boolean isCellEditable(EventObject e) { return true; }
    public boolean shouldSelectCell(EventObject anEvent) { return false; }
    public boolean stopCellEditing() { return true; }
    public void cancelCellEditing() {}

    public void addCellEditorListener(CellEditorListener l) {
	listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
	listenerList.remove(CellEditorListener.class, l);
    }

    protected void fireEditingStopped() {
	Object[] listeners = listenerList.getListenerList();
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==CellEditorListener.class) {
		((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
	    }
	}
    }

    protected void fireEditingCanceled() {
	Object[] listeners = listenerList.getListenerList();
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==CellEditorListener.class) {
		((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
	    }
	}
    }
}
