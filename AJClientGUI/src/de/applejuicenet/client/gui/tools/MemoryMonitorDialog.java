package de.applejuicenet.client.gui.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/tools/Attic/MemoryMonitorDialog.java,v 1.1 2003/11/04 13:14:50 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: MemoryMonitorDialog.java,v $
 * Revision 1.1  2003/11/04 13:14:50  maj0r
 * Memory-Monitor eingebaut.
 *
 *
 */

public class MemoryMonitorDialog extends JDialog{
    private MemoryMonitor memoryMonitorPanel;

    public MemoryMonitorDialog(Dialog parent){
        super(parent, false);
        init();
    }

    public MemoryMonitorDialog(Frame parent){
        super(parent, false);
        init();
    }

    private void init(){
        setTitle("aj Memory Monitor");
        memoryMonitorPanel = new MemoryMonitor();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                memoryMonitorPanel.stopMemoryMonitor();
            }
        });
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(memoryMonitorPanel, BorderLayout.CENTER);
        pack();
        setSize(new Dimension(200,200));
    }

    public void show(){
        super.show();
        memoryMonitorPanel.startMemoryMonitor();
    }
}
