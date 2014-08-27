package com.sa7i7mouslem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.sa7i7mouslem.utils.MySuperScaler;
import com.sa7i7mouslem.utils.SAMFonts;
import com.sa7i7mouslem.utils.Utils;


/**
 * Sa7i7 Al Boukhari
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

@SuppressLint("HandlerLeak")
public class SplashHome extends MySuperScaler {
	
	private static final String TAG = "SplashHome";
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 3000;
	
	public static final int MESSAGE_START = 1;
	public static final int MESSAGE_FINISH = 2;
	
	private RelativeLayout principal_layout;
	
	private Handler splashHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
//			case MESSAGE_START :
//				
//				new AsyncTask<Integer, Void, Boolean>() {
//			         
//			         ProgressDialog pd;
//			         
//			         protected void onPreExecute() {
//			          pd = new ProgressDialog(SplashHome.this);
//			          pd.setCancelable(false);
//			          pd.setMessage("Loading data...");
//			          pd.show();
//			         };
//
//			   @Override
//			   protected Boolean doInBackground(Integer... params) {
//			    
//			    SABManager sabManager = SABManager.getInstance(SplashHome.this);
//			    
//			    int counter = -1;
//			    do{
//			     counter++;
//			     
//			     sabManager.getAhadithByPage(counter);
//
//			     sabManager.getChaptersByPage(counter);
//
//			     ArrayList<Book> books = sabManager.getBooksByPage(counter);
//
//			     Page page = new Page();
//			     page.setId(counter);
//			     page.setBooks(books);
//			     
//			     Log.i("MainActivity", page.toString());
//			     
//			     sabManager.getPages().add(page);
//			     
//			    }while(sabManager.getPages().get(counter).getBooks().size() >= 50);
//
//			    return true;
//			   }
//			   
//			   protected void onPostExecute(Boolean result) {
//			    pd.dismiss();
//			    
//			    Message msg = Message.obtain();
//				msg.what = MESSAGE_FINISH;
//			    splashHandler.sendMessageDelayed(msg, SPLASHTIME);
//			    
//			   };
//			  }.execute();
//				
//				break;
				
			case MESSAGE_FINISH :
				
				SplashHome.this.startActivity(new Intent(SplashHome.this, MainActivity.class));
				Utils.animateSlide(SplashHome.this);
				SplashHome.this.finish();
				
				break;

			}
			

			super.handleMessage(msg);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashhome);
		
		SAMFonts.InitSAMFonts(this);
		
		principal_layout = (RelativeLayout) findViewById(R.id.principal_layout);
		
		Message msg = Message.obtain();
		msg.what = MESSAGE_FINISH;
	    splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		
		
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			splashHandler.removeMessages(STOPSPLASH);
		}
		return super.onKeyDown(keyCode, event);

	}	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Utils.cleanViews(principal_layout);
	}
	


}