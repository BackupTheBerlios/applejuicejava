package de.applejuicenet.client.gui.controller.event;

import java.util.Vector;


public class DownloadDataPropertyChangeEvent extends DataPropertyChangeEvent{

	public DownloadDataPropertyChangeEvent(
			Object source, Object name, Object oldValue, Object newValue) {
		super(source, name, oldValue, newValue);
	}

	public DownloadDataPropertyChangeEvent(Vector dataPropertyChangeEvents) {
		super(dataPropertyChangeEvents);
	}
	
	public static final String DOWNLOAD_REMOVED = "DOWNLOAD_REMOVED";
	public static final String DOWNLOAD_ADDED = "DOWNLOAD_ADDED";
	
	public static final String STATUS_CHANGED = "STATUS_CHANGED";
	public static final String FILENAME_CHANGED = "FILENAME_CHANGED";
	public static final String DIRECTORY_CHANGED = "DIRECTORY_CHANGED";
	public static final String PWDL_CHANGED = "PWDL_CHANGED";
	public static final String READY_CHANGED = "READY_CHANGED";

}