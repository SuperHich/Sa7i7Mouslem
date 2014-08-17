package com.sa7i7mouslem.utils;

import com.sa7i7mouslem.externals.SABDataBase;
import com.sa7i7mouslem.externals.SABManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


/**
 * This is Super Scaler
 *
 */
@SuppressLint("NewApi")
public class MySuperScaler extends FragmentActivity {
	
	private static final String TAG = MySuperScaler.class.getSimpleName();
	public static float scale ;
	public static boolean scaled = false;
	
	protected Activity thisAct ;
	
	public static int screen_width;
	public static int screen_height;
	public static int my_font_size ;
	
	protected boolean isFirstStart = true;
	
	public static boolean isTablet ;
	public SABDataBase sabDB;
	public SABManager sabManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		sabDB = new SABDataBase(this);
		sabManager = SABManager.getInstance(this);
		
		memoryAnalyser();
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;
		
		isTablet = isTablet(this);
		
		Log.e("SCREEN WIDTH *****", String.valueOf(screen_width));
		Log.e("SCREEN Height *****", String.valueOf(screen_height));
		Log.e("SCALE *****", String.valueOf(scale));
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		memoryAnalyser();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
		super.onWindowFocusChanged(hasFocus);
		View container=getWindow().getDecorView().findViewById(android.R.id.content);

		if(container.getTag()==null){
			View rootView=((ViewGroup)container).getChildAt(0);
			float xScale = (float) container.getWidth() / rootView.getWidth();
			float yScale = (float) container.getHeight() / rootView.getHeight();
			 scale = Math.min(xScale, yScale);
			scaleViewAndChildren(rootView, scale);
			
			
			
			container.setTag("IsScaled");
			
			Log.e("SCALEEEEED", Boolean.toString(scaled));
		}

	}

	public static void scaleViewAndChildren(View root, float scale) {
		ViewGroup.LayoutParams layoutParams = root.getLayoutParams();

		scaled = false ;
		
		if (layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT
				&& layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
			layoutParams.width *= scale;
		}
		if (layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT
				&& layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
			layoutParams.height *= scale;
		}

		if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
			marginParams.leftMargin *= scale;
			marginParams.rightMargin *= scale;
			marginParams.topMargin *= scale;
			marginParams.bottomMargin *= scale;
		}

		root.setLayoutParams(layoutParams);

		root.setPadding((int) (root.getPaddingLeft() * scale),
				(int) (root.getPaddingTop() * scale),
				(int) (root.getPaddingRight() * scale),
				(int) (root.getPaddingBottom() * scale));

		if (root instanceof TextView) {
			TextView textView = (TextView) root;
			textView.setTextSize(textView.getTextSize() * scale);
		}
		

		if (root instanceof ViewGroup) {
			ViewGroup groupView = (ViewGroup) root;
			for (int cnt = 0; cnt < groupView.getChildCount(); ++cnt)
				scaleViewAndChildren(groupView.getChildAt(cnt), scale);
		}
	}

	public static void scaleBackViewAndChildren(View root, float scale) {
		ViewGroup.LayoutParams layoutParams = root.getLayoutParams();

		if (layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT
				&& layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
			layoutParams.width /= scale;
		}
		if (layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT
				&& layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
			layoutParams.height /= scale;
		}

		if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
			marginParams.leftMargin /= scale;
			marginParams.rightMargin /= scale;
			marginParams.topMargin /= scale;
			marginParams.bottomMargin /= scale;
		}

		root.setLayoutParams(layoutParams);

		root.setPadding((int) (root.getPaddingLeft() / scale),
				(int) (root.getPaddingTop() / scale),
				(int) (root.getPaddingRight() / scale),
				(int) (root.getPaddingBottom() / scale));

		

		if (root instanceof ViewGroup) {
			ViewGroup groupView = (ViewGroup) root;
			for (int cnt = 0; cnt < groupView.getChildCount(); ++cnt)
				scaleViewAndChildren(groupView.getChildAt(cnt), scale);
		}
	}
		
		
		
	public void onBackPressed() {
		super.onBackPressed();
		
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		thisAct = this;
		
		if(sabDB == null)
			sabDB = new SABDataBase(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		thisAct = null;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(sabDB != null){
			sabDB.close();
			sabDB = null;
		}
	}
	
    protected double tabletInchSize(){
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float widthInInches = metrics.widthPixels / metrics.xdpi;
		float heightInInches = metrics.heightPixels / metrics.ydpi;
		
		double sizeInInches = Math.sqrt(Math.pow(widthInInches, 2) + Math.pow(heightInInches, 2));
		//0.5" buffer for 7" devices
	//	boolean is7inchTablet = sizeInInches >= 6.5 && sizeInInches <= 7.5; 
		
		Log.e("//////////////// SIZE IN INCH ////////////////", String.valueOf(sizeInInches));
		
		return sizeInInches ;
	}
	
    protected void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
    
	public void memoryAnalyser(){
		
		Log.i(TAG,"... Memory Analyser check test ");
		Runtime r = Runtime.getRuntime();
		long mem0 = r.totalMemory();
		Log.v(TAG,"Total memory is: " + (int)(mem0 / (1024*1024)) + " MB"); 
		long mem1 = r.freeMemory();
		Log.v(TAG,"Initial free memory: " + (int)(mem1 / (1024*1024)) + " MB");
		long mem2 = r.maxMemory();
		Log.v(TAG,"Max memory: " + (int)(mem2 / (1024*1024)) + " MB");

		Log.v(TAG,"Memory usage : " + (int)((mem0*100)/mem2) + " %");
		
		Log.v(TAG,"Memory allocated : " + (int)((mem0 - mem1) / (1024*1024)) + " MB");
		
		long mem_alloc = Debug.getNativeHeapAllocatedSize();
		Log.v(TAG,"Native Heap memory Allocated : " + (int)(mem_alloc / (1024*1024)) + " MB");
	}
	
	protected Drawable createCopy(Bitmap b){
		Config config = Config.ARGB_8888;
		Bitmap bm = b.copy(config, false);
		return new BitmapDrawable(getResources(), bm);
	}
}
