package de.applejuicenet.client.gui.tables.upload;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import de.applejuicenet.client.shared.*;
import de.applejuicenet.client.gui.tables.TreeTableModelAdapter;
import de.applejuicenet.client.shared.dac.*;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/gui/tables/upload/Attic/UploadTableCellRenderer.java,v 1.5 2003/12/29 16:04:17 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Offizielles GUI für den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: General Public License</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: UploadTableCellRenderer.java,v $
 * Revision 1.5  2003/12/29 16:04:17  maj0r
 * Header korrigiert.
 *
 * Revision 1.4  2003/10/21 14:08:45  maj0r
 * Mittels PMD Code verschoenert, optimiert.
 *
 * Revision 1.3  2003/08/30 19:44:32  maj0r
 * Auf JTreeTable umgebaut.
 *
 * Revision 1.2  2003/08/11 14:42:13  maj0r
 * Versions-Icon-Beschaffung in die Klasse Version verschoben.
 *
 * Revision 1.1  2003/08/09 10:57:14  maj0r
 * UploadTabelle weitergeführt.
 *
 * Revision 1.6  2003/07/06 20:00:19  maj0r
 * DownloadTable bearbeitet.
 *
 * Revision 1.5  2003/07/04 06:43:51  maj0r
 * Diverse Änderungen am DownloadTableModel.
 *
 * Revision 1.4  2003/07/03 19:11:16  maj0r
 * DownloadTable überarbeitet.
 *
 * Revision 1.3  2003/07/02 13:54:34  maj0r
 * JTreeTable komplett überarbeitet.
 *
 * Revision 1.2  2003/07/01 18:41:39  maj0r
 * Struktur verändert.
 *
 * Revision 1.1  2003/07/01 18:34:28  maj0r
 * Struktur verändert.
 *
 * Revision 1.4  2003/06/10 12:31:03  maj0r
 * Historie eingefügt.
 *
 *
 */

public class UploadTableCellRenderer
        extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        Object obj = ( (TreeTableModelAdapter) table.getModel()).nodeForRow(row);
        Color background = table.getBackground();
        Color foreground = table.getForeground();
        if (obj.getClass()==MainNode.class){
            if (column==0){
                JPanel returnPanel = new JPanel(new BorderLayout());
                JLabel image = new JLabel();
                JLabel text = new JLabel();
                if (isSelected) {
                    returnPanel.setBackground(table.getSelectionBackground());
                    returnPanel.setForeground(table.getSelectionForeground());
                    image.setBackground(table.getSelectionBackground());
                    text.setBackground(table.getSelectionBackground());
                    image.setForeground(table.getSelectionForeground());
                    text.setBackground(table.getSelectionForeground());
                }
                else {
                    returnPanel.setBackground(background);
                    returnPanel.setForeground(foreground);
                    image.setBackground(table.getBackground());
                    text.setBackground(table.getBackground());
                    image.setForeground(table.getForeground());
                    text.setBackground(table.getForeground());
                }
                image.setIcon(((MainNode)obj).getConvenientIcon());
                text.setText(" " + (String)value);
                text.setFont(table.getFont());
                returnPanel.add(image, BorderLayout.WEST);
                returnPanel.add(text, BorderLayout.CENTER);
                return returnPanel;
            }
            else
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        UploadDO uploadDO = (UploadDO) obj;
        switch (column){
            case 0:{
                JLabel text = new JLabel();
                text.setOpaque(true);
                if (isSelected) {
                    text.setBackground(table.getSelectionBackground());
                }
                else {
                    text.setBackground(table.getBackground());
                }
                text.setText("   " + (String)value);
                text.setFont(table.getFont());
                return text;
            }
            case 4:{
                String prozent = uploadDO.getDownloadPercentAsString();
                JProgressBar progress = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                                                         100);
                int pos = prozent.indexOf('.');
                String balken = prozent;
                if (pos!=-1)
                    balken = balken.substring(0, pos);
                progress.setValue(Integer.parseInt(balken));
                progress.setString(prozent + " %");
                progress.setStringPainted(true);
                return progress;
            }
            case 6:{
                JPanel returnPanel = new JPanel(new BorderLayout());
                JLabel image = new JLabel();

                JLabel serverName = new JLabel();

                if (isSelected) {
                    returnPanel.setBackground(table.getSelectionBackground());
                    returnPanel.setForeground(table.getSelectionForeground());
                    image.setBackground(table.getSelectionBackground());
                    serverName.setBackground(table.getSelectionBackground());
                    image.setForeground(table.getSelectionForeground());
                    serverName.setBackground(table.getSelectionForeground());
                }
                else {
                    returnPanel.setBackground(background);
                    returnPanel.setForeground(foreground);
                    image.setBackground(table.getBackground());
                    serverName.setBackground(table.getBackground());
                    image.setForeground(table.getForeground());
                    serverName.setBackground(table.getForeground());
                }

                if (uploadDO.getVersion() == null) {
                    return returnPanel;
                }
                else {
                    image.setIcon(uploadDO.getVersion().getVersionIcon());
                }
                serverName.setText("  " + uploadDO.getVersion().getVersion());
                serverName.setFont(table.getFont());
                returnPanel.add(image, BorderLayout.WEST);
                returnPanel.add(serverName, BorderLayout.CENTER);
                return returnPanel;
            }
            default:{
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }
}