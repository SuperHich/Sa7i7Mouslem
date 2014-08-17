package com.sa7i7mouslem.externals;

import java.io.File;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.sa7i7mouslem.utils.Utils;

/**
 * AlMoufasserAlSaghir
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class SABDownloadManager {
	
	protected String URL_FILE;

	private static final String TAG = null;
	protected static SABDownloadManager mInstance;
	protected Context context;
	private long downloadID = -1;
	private boolean isDownloading = false;
	private DownloadManager downloadManager;
	private IDownloadNotifier notifier;
	
	private ProgressThread progressThread;
	
	protected String basePath;
	protected String thePath;
	protected String soundFilePath;
	
	protected String folderName;
	protected String soundFileName;
	protected String description;
	
	protected boolean isDataReady = true;
	protected boolean isNetworkOn = true;
	
	public synchronized static SABDownloadManager getInstance(Context context) {
		if (mInstance == null)
			mInstance = new SABDownloadManager(context);

		return mInstance;
	}
	
	private IntentFilter downloadCompleteIntentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
	private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	
	    	long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
	    	if (id != downloadID) {
	    	    Log.v(TAG, "Ingnoring unrelated download " + id);
	    	    return;
	    	}
	    	
	    	CheckDwnloadStatus();
	    	
	    	DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(id);
			Cursor cursor = downloadManager.query(query);

			// it shouldn't be empty, but just in case
			if (!cursor.moveToFirst()) {
			    Log.e(TAG, "Empty row");
			    return;
			}
			
	    	int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
	    	if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
	    	    Log.w(TAG, "Download Failed");
	    	    notifier.onErrorDownload();
	    	    isDownloading = false;
	    	    return;
	    	} 
	    	
	    	
	    	notifier.onDownloadComplete(soundFilePath);
	    	isDownloading = false;
	    }
	};

	
	public SABDownloadManager(Context context, IDownloadNotifier notifier) {

		this.context = context.getApplicationContext();
		downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		this.notifier = notifier;
		
	}

	public SABDownloadManager(Context context) {

		this.context = context.getApplicationContext();
		downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		notifier = (IDownloadNotifier) context;
		
	}
	
	public void setDownloadNotifier(Context context){
		notifier = (IDownloadNotifier) context;
	}
	
	public boolean initializeDownload(String url){
		
		URL_FILE = url;

		File d = context.getExternalFilesDir(null);

		if(d != null){
			basePath = d.getAbsolutePath() + File.separator;

			folderName = "sounds";
			soundFileName = getFileNameFromUrl(URL_FILE);

			thePath = basePath + folderName + File.separator;
			soundFilePath = thePath + soundFileName;

			description = "Downloading "+ soundFilePath;

			SABManager.SoundPath = soundFilePath;

			dirChecker(thePath);
			
			if(_isFileExist(soundFilePath))
				return false;

			if(!Utils.isOnline(context)){

				setNetworkOn(false);
				return false;
			}

			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL_FILE));

			// only download via WIFI
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
			request.setTitle(soundFileName);
			request.setDescription(description);

			// we just want to download silently
			request.setVisibleInDownloadsUi(false);
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			request.setDestinationInExternalFilesDir(context, folderName, soundFileName);

			// enqueue this request
			downloadID = downloadManager.enqueue(request);

			// when initialize
			context.registerReceiver(downloadCompleteReceiver, downloadCompleteIntentFilter);

			isDownloading = downloadID != -1;

			if(isDownloading){
				progressThread = new ProgressThread();
				progressThread.start();
			}

			return isDownloading;

		}

		isDataReady = false;
		return false;
	}
	
	public void queryDownloadState(long id){
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(id);
		Cursor cursor = downloadManager.query(query);

		// it shouldn't be empty, but just in case
		if (!cursor.moveToFirst()) {
		    Log.e(TAG, "Empty row");
		    return;
		}
	}
	
	public void cancelDownload(){
		if(downloadID != -1){
			// remove this request
			DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			isDownloading = downloadManager.remove(downloadID) == 0;

			if(progressThread != null){
				progressThread.interrupt();
				progressThread = null;
			}

			unregisterReceiver();
		}
	}
	
	
	public void unregisterReceiver(){
		// when exit
		context.unregisterReceiver(downloadCompleteReceiver);
	}

	private boolean _isFileExist(String path){
		File file = new File(path);
		if(file.exists())
			return true;

		return false;
	}
	
//	private void removeFile(String filePath){
//		try{
//			File file = new File(filePath);
//			if(file.isFile())
//				file.delete();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	private void clearAllFiles(){
		try{
			File dir = new File(basePath);
			for(File fd : dir.listFiles()){
				if(fd.isFile())
					fd.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void dirChecker(String dir) {
		File f = new File(dir);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}
	
	public static String getFileNameFromUrl(String url){
		String[] parts = url.split("/");
		return parts[parts.length - 1];
	}
	
	public boolean isDownloading(){
		return isDownloading;
	}

	public boolean isDataReady() {
		return isDataReady;
	}

	public void setDataReady(boolean isDataReady) {
		this.isDataReady = isDataReady;
	}

	public boolean isNetworkOn() {
		return isNetworkOn;
	}

	public void setNetworkOn(boolean isNetworkOn) {
		this.isNetworkOn = isNetworkOn;
	}

	
	class ProgressThread extends Thread{
		 @Override
	        public void run() {

	            while (isDownloading) {
	            	
	                DownloadManager.Query q = new DownloadManager.Query();
	                q.setFilterById(downloadID);

	                Cursor cursor = downloadManager.query(q);
	                if(cursor.moveToFirst()){
	                	int bytes_downloaded = cursor.getInt(cursor
	                			.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
	                	int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

//	                	if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
//	                		isDownloading = false;
//	                	}

	                	final int dl_progress = (int) ((double)bytes_downloaded / (double)bytes_total * 100f);

	                	notifier.onProgressDownload((int) dl_progress);
	                }
	                cursor.close();
	                
	                try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	            }

	        }
	}
	
	private void CheckDwnloadStatus(){

		// TODO Auto-generated method stub
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downloadID);
		Cursor cursor = downloadManager.query(query);
		if(cursor.moveToFirst()){
			int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
			int status = cursor.getInt(columnIndex);
			int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
			int reason = cursor.getInt(columnReason);

			switch(status){
				case DownloadManager.STATUS_FAILED:
				String failedReason = "";
				switch(reason){
					case DownloadManager.ERROR_CANNOT_RESUME:
					failedReason = "ERROR_CANNOT_RESUME";
					break;
					case DownloadManager.ERROR_DEVICE_NOT_FOUND:
					failedReason = "ERROR_DEVICE_NOT_FOUND";
					break;
					case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
					failedReason = "ERROR_FILE_ALREADY_EXISTS";
					break;
					case DownloadManager.ERROR_FILE_ERROR:
					failedReason = "ERROR_FILE_ERROR";
					break;
					case DownloadManager.ERROR_HTTP_DATA_ERROR:
					failedReason = "ERROR_HTTP_DATA_ERROR";
					break;
					case DownloadManager.ERROR_INSUFFICIENT_SPACE:
					failedReason = "ERROR_INSUFFICIENT_SPACE";
					break;
					case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
					failedReason = "ERROR_TOO_MANY_REDIRECTS";
					break;
					case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
					failedReason = "ERROR_UNHANDLED_HTTP_CODE";
					break;
					case DownloadManager.ERROR_UNKNOWN:
					failedReason = "ERROR_UNKNOWN";
					break;
				}

				notifier.onErrorDownload();
				Log.e(SABDownloadManager.class.getSimpleName(), "FAILED: " + failedReason);
//				Toast.makeText(context, "FAILED: " + failedReason, Toast.LENGTH_LONG).show();
				break;
				case DownloadManager.STATUS_PAUSED:
				String pausedReason = "";

				switch(reason){
					case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
					pausedReason = "PAUSED_QUEUED_FOR_WIFI";
					break;
					case DownloadManager.PAUSED_UNKNOWN:
					pausedReason = "PAUSED_UNKNOWN";
					break;
					case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
					pausedReason = "PAUSED_WAITING_FOR_NETWORK";
					break;
					case DownloadManager.PAUSED_WAITING_TO_RETRY:
					pausedReason = "PAUSED_WAITING_TO_RETRY";
					break;
				}
				
				Log.e(SABDownloadManager.class.getSimpleName(), "PAUSED: " + pausedReason);
//				Toast.makeText(context, "PAUSED: " + pausedReason,Toast.LENGTH_LONG).show();
				break;
				case DownloadManager.STATUS_PENDING:
				Toast.makeText(context,
					"PENDING",
					Toast.LENGTH_LONG).show();
				break;
				case DownloadManager.STATUS_RUNNING:
				Toast.makeText(context,
					"RUNNING",
					Toast.LENGTH_LONG).show();
				break;
				case DownloadManager.STATUS_SUCCESSFUL:

					Log.e(SABDownloadManager.class.getSimpleName(), "SUCCESSFUL");
//				Toast.makeText(context, "SUCCESSFUL", Toast.LENGTH_LONG).show();
				//GetFile();
				break;
			}
		}
	}

}
