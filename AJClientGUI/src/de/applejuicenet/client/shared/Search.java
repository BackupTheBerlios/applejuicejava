package de.applejuicenet.client.shared;

import java.util.Vector;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/applejuicejava/Repository/AJClientGUI/src/de/applejuicenet/client/shared/Attic/Search.java,v 1.1 2003/09/30 16:35:11 maj0r Exp $
 *
 * <p>Titel: AppleJuice Client-GUI</p>
 * <p>Beschreibung: Erstes GUI f�r den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: Search.java,v $
 * Revision 1.1  2003/09/30 16:35:11  maj0r
 * Suche begonnen und auf neues ID-Listen-Prinzip umgebaut.
 *
 *
 */

public class Search {
    private int id;
    private String suchText;
    private int offeneSuchen;
    private int gefundenDateien;
    private int durchsuchteClients;
    private Vector searchEntries = new Vector();

    public Search(int id) {
        this.id = id;
    }

    public String getSuchText() {
        return suchText;
    }

    public void setSuchText(String suchText) {
        this.suchText = suchText;
    }

    public int getOffeneSuchen() {
        return offeneSuchen;
    }

    public void setOffeneSuchen(int offeneSuchen) {
        this.offeneSuchen = offeneSuchen;
    }

    public int getGefundenDateien() {
        return gefundenDateien;
    }

    public void setGefundenDateien(int gefundenDateien) {
        this.gefundenDateien = gefundenDateien;
    }

    public int getDurchsuchteClients() {
        return durchsuchteClients;
    }

    public void setDurchsuchteClients(int durchsuchteClients) {
        this.durchsuchteClients = durchsuchteClients;
    }

    public int getId() {
        return id;
    }

    public void addSearchEntry(SearchEntry searchEntry){
        if (!searchEntries.contains(searchEntry)){
            searchEntries.add(searchEntry);
        }
    }

    public SearchEntry[] getSearchEntries(){
        return (SearchEntry[]) searchEntries.toArray(new SearchEntry[searchEntries.size()]);
    }

    public class SearchEntry{
        private int id;
        private String checksumme;
        private long groesse;
        private Vector fileNames = new Vector();

        public SearchEntry(int id, String checksumme, long groesse) {
            this.id = id;
            this.checksumme = checksumme;
            this.groesse = groesse;
        }

        public int getId() {
            return id;
        }

        public String getChecksumme() {
            return checksumme;
        }

        public long getGroesse() {
            return groesse;
        }

        public void addFileName(FileName fileName){
            if (!fileNames.contains(fileName)){
                fileNames.add(fileName);
            }
        }

        public FileName[] getFileNames(){
            return (FileName[]) fileNames.toArray(new FileName[fileNames.size()]);
        }

        public class FileName{
            private String dateiName;
            private int haeufigkeit;

            public FileName(String dateiName, int haeufigkeit) {
                this.dateiName = dateiName;
                this.haeufigkeit = haeufigkeit;
            }

            public String getDateiName() {
                return dateiName;
            }

            public int getHaeufigkeit() {
                return haeufigkeit;
            }
        }
    }
}
