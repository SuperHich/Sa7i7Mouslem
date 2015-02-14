package com.sa7i7mouslem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sa7i7mouslem.adapters.AhadithAdapter;
import com.sa7i7mouslem.adapters.IFragmentNotifier;
import com.sa7i7mouslem.adapters.IHadtihListener;
import com.sa7i7mouslem.entity.Chapter;
import com.sa7i7mouslem.entity.Hadith;
import com.sa7i7mouslem.externals.IDownloadNotifier;
import com.sa7i7mouslem.externals.SAMDataBase;
import com.sa7i7mouslem.externals.SAMDownloadManager;
import com.sa7i7mouslem.externals.SAMManager;
import com.sa7i7mouslem.mediaplayer.IMediaPlayerNotifier;
import com.sa7i7mouslem.mediaplayer.SABMediaPlayer;
import com.sa7i7mouslem.utils.LoadMoreListView;
import com.sa7i7mouslem.utils.LoadMoreListView.OnLoadMoreListener;
import com.sa7i7mouslem.utils.MySuperScaler;
import com.sa7i7mouslem.utils.SAMFonts;
import com.sa7i7mouslem.utils.Utils;


public class AhadithFragment extends ListFragment implements IHadtihListener, IMediaPlayerNotifier, IDownloadNotifier, IFragmentNotifier, OnMenuItemClickListener{

	public static final String ARG_AHADITH = "ahadith_type";
	public static final String ARG_AHADITH_SEARCH = "ahadith_search_type";
	public static final String ARG_AHADITH_KEYWORD_TEXT = "ahadith_keyword";
	public static final String ARG_BAB_ID = "bab_id";
	public static final String ARG_BOOK_ID = "book_id";
	
	public static final int TYPE_AHADITH_KEYWORD_ID = 10;
	public static final int TYPE_AHADITH_BY_BAB = 20;
	public static final int TYPE_AHADITH_BY_BOOK = 30;
	
	public static final int TEXT_SIZE_MIN = 5;
	public static final int TEXT_SIZE_MAX = 45;
	private static final String TAG = AhadithFragment.class.getSimpleName();
	
	private AhadithAdapter adapter;
	private ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
	private int ahadith_typeId = 0, ahadith_search_typeId = 1, bab_id = 1, book_id = -1;
	private String ahadith_keyword;
	private SABMediaPlayer sabPlayer;
	private int pageId = 0;
	
	private int positionToUpdate, positionToListen = -1;
	private SAMDataBase sabDB;
	private SAMDownloadManager sabDownloadManager;
	private TextView txv_emptyList;
	private TextView mTxvProgress;
	private SeekBar mSeekBar;
	private int lastTotalTime;
	private String hadithText;
	
	private ImageView btn_minus, btn_plus;
	private TextView txv_size, txv_size_name;
	private SAMManager mManager;

	private boolean isContinueMode = false;

	public AhadithFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		sabDB = ((MainActivity)getActivity()).sabDB;
		sabDownloadManager = new SAMDownloadManager(activity, this);
		mManager = SAMManager.getInstance(getActivity());
		mManager.setFragmentNotifier(this);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		sabPlayer.setNotifier(null);
		mManager.setFragmentNotifier(null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ahadith, container, false);
		ahadith_typeId = getArguments().getInt(ARG_AHADITH);
		ahadith_search_typeId = getArguments().getInt(ARG_AHADITH_SEARCH);
		ahadith_keyword = getArguments().getString(ARG_AHADITH_KEYWORD_TEXT);
		bab_id = getArguments().getInt(ARG_BAB_ID);
		book_id = getArguments().getInt(ARG_BOOK_ID);
		
		txv_emptyList = (TextView) rootView.findViewById(R.id.txv_emptyList);
		
		btn_minus = (ImageView) rootView.findViewById(R.id.btn_minus);
		btn_plus = (ImageView) rootView.findViewById(R.id.btn_plus);
		txv_size = (TextView) rootView.findViewById(R.id.txv_size);
		txv_size_name = (TextView) rootView.findViewById(R.id.txv_size_name);
		
		txv_emptyList.setTypeface(SAMFonts.getMOHANDFont());
		txv_size.setTypeface(SAMFonts.getMOHANDFont());
		txv_size_name.setTypeface(SAMFonts.getMOHANDFont());
		
		btn_minus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int size = mManager.getTextSize();
				int realSize = mManager.getRealTextSize();
				if(size-1 >= TEXT_SIZE_MIN)
				{
					mManager.setTextSizes(size-1);
					mManager.setRealTextSizes(realSize+1);
					
					refreshTextSize();
				}
			}
		});
		
		btn_plus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int size = mManager.getTextSize();
				int realSize = mManager.getRealTextSize();
				if(size+1 <= TEXT_SIZE_MAX)
				{
					mManager.setTextSizes(size+1);
					mManager.setRealTextSizes(realSize-1);
					
					refreshTextSize();
				}
			}
		});
		
		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(rootView, MySuperScaler.scale);


		adapter = new AhadithAdapter(getActivity(), R.layout.hadith_list_item, ahadith, this);
		refreshTextSize();
		
		return rootView;
	}
	
	private void refreshTextSize(){
		
		adapter.setTextSize(mManager.getRealTextSize());
		txv_size.setText(String.valueOf(mManager.getTextSize()));
		
		adapter.notifyDataSetChanged();
	}


	private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			try{
				Thread.sleep(1000);
			}catch(Exception e){}
			
			pageId += 1;
			ahadith.addAll(sabDB.getAllHadithsWithPage(pageId));

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// We need notify the adapter that the data have been changed
			adapter.notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}
	}
	
	private void initAhadith(){

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				ahadith.clear();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					switch (ahadith_typeId) {
					case 0:
						ahadith.addAll(sabDB.getFavoriteHadiths());				
						break;
					case 1:
						ahadith.addAll(sabDB.getAllHadithsWithPage(pageId));				
						break;
					case 2:
						ahadith.addAll(sabDB.getCommentedHadiths());				
						break;
					case TYPE_AHADITH_KEYWORD_ID:
						switch (ahadith_search_typeId) {
						case 0:
							ahadith.addAll(sabDB.searchHadithFromFavoriteWithText(ahadith_keyword));
							break;
						case 1:
							ahadith.addAll(sabDB.searchHadithWithText(ahadith_keyword));
							break;
						case 2:
							ahadith.addAll(sabDB.searchHadithFromCommentedWithText(ahadith_keyword));
							break;
						default:
							ahadith.addAll(sabDB.searchHadithByBabWithText(ahadith_keyword, bab_id));
							break;
						}
						
						adapter.setSearchKeyWord(ahadith_keyword);
						
						if(ahadith.size() > 0)
							Toast.makeText(getActivity(), getString(R.string.we_found) + " " + ahadith.size() + " " + getString(R.string.hadith), Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getActivity(), getString(R.string.no_hadith_found) + " \"" + ahadith_keyword  + "\"" , Toast.LENGTH_SHORT).show();
							
						break;
					case TYPE_AHADITH_BY_BAB:
						ahadith.addAll(sabDB.getAllHadithsWithBabId(bab_id));
						break;
					case TYPE_AHADITH_BY_BOOK:
						ArrayList<Chapter> chapters = sabDB.getAllBabsFromBook(book_id);
						for (Chapter chapter : chapters) {
							ahadith.addAll(sabDB.getAllHadithsWithBabId(chapter.getBabId()));
						}
						break;
					default:
						break;
					}

				} catch (Exception e) {

					e.printStackTrace();

				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				adapter.notifyDataSetChanged();
				toggleEmptyMessage();
				
				new Handler(new Callback() {
					
					@Override
					public boolean handleMessage(Message msg) {
						if(ahadith_typeId == TYPE_AHADITH_BY_BOOK && !ahadith.isEmpty()){
							ignorePopup = true;
							isContinueMode = true;
							onHadithListen(0);
						}
						return false;
					}
				}).sendMessageDelayed(new Message(), 1000);
			}
		}.execute();
		
	}
	
	private void toggleEmptyMessage() {
		if(ahadith.size() == 0)
			txv_emptyList.setVisibility(View.VISIBLE);
		else
			txv_emptyList.setVisibility(View.GONE);
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		sabPlayer = ((MainActivity) getActivity()).getSabPlayer();
		sabPlayer.setNotifier(this);
		
		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		((LoadMoreListView) getListView()).setFooterDividersEnabled(false);
		
		if(ahadith_typeId == 1)
			((LoadMoreListView) getListView()).setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					// Do the work to load more items at the end of list
					// here
					new LoadDataTask().execute();
				}
			});
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Hadith hadith = ahadith.get(position);
				
				if(mManager.getBookMarkPosition() != hadith.getId())
					mManager.setBookMarkPosition(hadith.getId());
				else
					mManager.setBookMarkPosition(-1);
				
				adapter.notifyDataSetChanged();
				
				return true;
			}
		});
		
		initAhadith();
		
	}
	
	@Override
	public void onHadithTextClicked(int position) {
		hadithText = ahadith.get(position).getText();
		View view = getSelecedView(position);
		if(view == null)
		{
			Log.w("", "Unable to get view for desired position, because it's not being displayed on screen.");
			return;
		}
		showMenu(view);
	}
	
	public void showMenu(View v) {
	    PopupMenu popup = new PopupMenu(getActivity(), v);

	    // This activity implements OnMenuItemClickListener
	    popup.setOnMenuItemClickListener(this);
	    popup.inflate(R.menu.context_menu);
	    popup.show();
	}

	@Override
	public void onHadithShowMore(int position) {
		Hadith hadith = ahadith.get(position);
		ahadith.get(position).setShown(!hadith.isShown());

		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onHadithPause(int position) {
		
		View view = getSelecedView(position);
		if(view == null)
		{
			Log.w("", "Unable to get view for desired position, because it's not being displayed on screen.");
			return;
		}
		
		Button btn_pause = (Button) view.findViewById(R.id.btn_pause);
		
		if(sabPlayer.isPlaying()){
			sabPlayer.pause();
			btn_pause.setBackgroundResource(R.drawable.play);
		}
		else{
			sabPlayer.resume();
			btn_pause.setBackgroundResource(R.drawable.stop);
		}
	}
	
	private void cleanPreviousPlayer(int position){
		sabPlayer.stop();
		ahadith.get(position).setBottomLayoutShown(false);
		ahadith.get(position).setPlaying(false);
		mSeekBar.setProgress(0);
	}

	@Override
	public void onHadithListen(int position) {
		
		if(!isPopupShown && !ignorePopup)
		{	
			showListenPopup(position);
			return;
		}else
			isPopupShown = false;
		
		Hadith hadith = ahadith.get(position);
		if(!hadith.isBottomLayoutShown())
		{
			if(positionToListen != -1)
				cleanPreviousPlayer(positionToListen);
			
			View view = getSelecedView(position);
			if(view == null)
			{
				Log.w("", "Unable to get view for desired position, because it's not being displayed on screen.");
				return;
			}
			
			positionToListen = position;

			RelativeLayout bottom_layout = (RelativeLayout) view.findViewById(R.id.bottom_layout);
			bottom_layout.setBackgroundResource(R.drawable.player_bg);
			
			Button btn_pause = (Button) view.findViewById(R.id.btn_pause);
			btn_pause.setVisibility(View.VISIBLE);

			mSeekBar = (SeekBar) view.findViewById(R.id.seekbar_progress);
			mTxvProgress = (TextView) view.findViewById(R.id.txv_progress);

			int size = (int) MySuperScaler.screen_width / 22 ;
			mTxvProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
			mTxvProgress.setText("00:00");
			
			ShapeDrawable thumb = new ShapeDrawable(new RectShape());
			thumb.getPaint().setColor(Color.rgb(0, 0, 0));
			thumb.setIntrinsicHeight(-80);
			thumb.setIntrinsicWidth(30);
			mSeekBar.setThumb(thumb);
			mSeekBar.setEnabled(false);
			
			boolean shouldShow = true;
			if(hadith.isDownload())
			{
				sabPlayer.playFromSdcardWithCompletion(hadith.getFile());
			}else if(Utils.isOnline(getActivity()))
			{
//				String mp3 = "http://tondeapel.net/wp-content/uploads/2012/09/Iphone_Ringtone.mp3";
//				String mp3 = "http://prophetmuhammadforall.com/media/audios/Bismillah.mp3";
				String mp3 = hadith.getLink();
				sabPlayer.playFromUrlWithCompletion(mp3);
			}else{
				Toast.makeText(getActivity(), R.string.error_internet_connexion, Toast.LENGTH_LONG).show();
				shouldShow = false;
			}

			ahadith.get(position).setBottomLayoutShown(shouldShow);
			ahadith.get(position).setPlaying(shouldShow);

		}
		else{
			sabPlayer.stop();
			ahadith.get(position).setBottomLayoutShown(false);
			ahadith.get(position).setPlaying(false);
			mSeekBar.setProgress(0);
			
			ignorePopup = false;
		}
			    
	    
	    adapter.notifyDataSetChanged();
	}

	private View getSelecedView(int position) {
		// getSelectedView
		int firstPosition = getListView().getFirstVisiblePosition() - getListView().getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = position - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= getListView().getChildCount()) {
//			Log.w("", "Unable to get view for desired position, because it's not being displayed on screen.");
			return null;
		}
		// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
		View view = getListView().getChildAt(wantedChild);
		return view;
	}

	@Override
	public void onHadithDownload(int position) {
		
		if(sabDownloadManager.isDownloading())
		{
			Toast.makeText(getActivity(), R.string.wait_other_download, Toast.LENGTH_LONG).show();
			return;
		}
		
		View view = getSelecedView(position);
		if(view == null)
		{
			Log.w("", "Unable to get view for desired position, because it's not being displayed on screen.");
			return;
		}
		
		positionToUpdate = position;
		
		RelativeLayout bottom_layout = (RelativeLayout) view.findViewById(R.id.bottom_layout);
		bottom_layout.setBackgroundResource(R.drawable.downloader_bg);
		
		mSeekBar = (SeekBar) view.findViewById(R.id.seekbar_progress);
		mTxvProgress = (TextView) view.findViewById(R.id.txv_progress);
		
		Button btn_pause = (Button) view.findViewById(R.id.btn_pause);
		btn_pause.setVisibility(View.GONE);
		
		int size = (int) MySuperScaler.screen_width / 22 ;
		mTxvProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		mTxvProgress.setText("0%");
		
		ShapeDrawable thumb = new ShapeDrawable(new RectShape());
	    thumb.getPaint().setColor(Color.rgb(0, 0, 0));
	    thumb.setIntrinsicHeight(-80);
	    thumb.setIntrinsicWidth(30);
	    mSeekBar.setThumb(thumb);
	    mSeekBar.setMax(100);
		
		Hadith hadith = ahadith.get(position);
		String soundFile = hadith.getLink();
//		String soundFile = "http://prophetmuhammadforall.com/media/audios/Bismillah.mp3";
		
		boolean isCanceled = true;
		
		if(!hadith.isDownload())
			if(Utils.isOnline(getActivity()))
				if(!sabDownloadManager.initializeDownload(soundFile))
		    	{
		    		Toast.makeText(getActivity(), R.string.already_exist, Toast.LENGTH_LONG).show();
		    	}else
		    		isCanceled = false;
			else
				Toast.makeText(getActivity(), R.string.error_internet_connexion, Toast.LENGTH_LONG).show();
				
		else
			//Hadith sound already downloaded
			Toast.makeText(getActivity(), R.string.already_exist, Toast.LENGTH_LONG).show();
		
		if(!hadith.isBottomLayoutShown() && !isCanceled)
		{
//			if(ahadith.get(position).isPlaying()){
//				sabPlayer.stop();
//				ahadith.get(position).setPlaying(false);
//			}
			
			ahadith.get(position).setBottomLayoutShown(true);
			ahadith.get(position).setDownloading(true);
			adapter.notifyDataSetChanged();
		}
		
	}

	@Override
	public void onHadithFavorite(int position) {

		Hadith hadith = ahadith.get(position);
		boolean newFavStatus = !hadith.isFavorite();
		if(sabDB.setFavoriteHadith(hadith.getId(), newFavStatus))
		{
			if (ahadith_typeId == 0) {
				initAhadith();
			}else
				ahadith.get(position).setFavorite(newFavStatus);

			adapter.notifyDataSetChanged();

			if(newFavStatus)
				Toast.makeText(getActivity(), R.string.added_to_fav, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity(), R.string.removed_from_fav, Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public void onHadithComment(int position) {
		((MainActivity) getActivity()).gotoCommentsFragment(ahadith.get(position));
	}

	@Override
	public void onHadithShare(int position) {
		if(Utils.isOnline(getActivity())){
			Hadith hadith = ahadith.get(position);
			shareHadith(hadith.getText());
			if(sabDB.setIsSharedToHadith(hadith.getId())){
				ahadith.get(position).setShared(true);
				adapter.notifyDataSetChanged();
			}
			
		}else{
			Toast.makeText(getActivity(), R.string.error_internet_connexion, Toast.LENGTH_LONG).show();
		}
	}

	private void shareHadith(String text){

		text = text.replace(System.getProperty("line.separator"), " ");
		
		String shareBody = text;
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));

	}

	@Override
	public void onCompletion() {
		mSeekBar.setProgress(mSeekBar.getMax());
		
		ahadith.get(positionToListen).setBottomLayoutShown(false);
		ahadith.get(positionToListen).setPlaying(false);
		adapter.notifyDataSetChanged();
		
		nextHadith();
		
	}
	
	private void nextHadith(){
		if(positionToListen+1 < ahadith.size() && isContinueMode)
		{
			ignorePopup = true;
			onHadithListen(positionToListen+1);
		}else
			ignorePopup = false;
	}
	
	@Override
	public void onConfigProgress(int totalTime) {
		lastTotalTime = totalTime;
		mSeekBar.setMax(totalTime);
		mTxvProgress.setText("00:00");
	}

	@Override
	public void onProgressPlayer(final int progress) {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				refreshListItemViews(positionToListen);
				mSeekBar.setMax(lastTotalTime);
				
				mSeekBar.setProgress(progress);
				mTxvProgress.setText(MillisToTime(progress));
			}
		});
	}
	
	@Override
	public void onErrorPlayer() {
		Toast.makeText(getActivity(), R.string.player_error, Toast.LENGTH_LONG).show();
		
		cleanPreviousPlayer(positionToListen);
		adapter.notifyDataSetChanged();
		
		nextHadith();
	}
	
	private String MillisToTime(int timeInMillis){
		String min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(timeInMillis));
		int m = Integer.valueOf(min);
		
		String sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(timeInMillis));
		
		int s = Integer.valueOf(sec);
		int secToSet = s % 60;
		
		s = secToSet;
		
		min = String.valueOf(m);
		sec = String.valueOf(s);
		
		if(sec.length() == 1)
			sec = "0" + sec;
		
		if(min.length() == 1)
			min = "0" + m;
		
		return min + ":" + sec;
	}

	@Override
	public void onDownloadComplete(String path) {
		
		Toast.makeText(getActivity(), R.string.download_success, Toast.LENGTH_LONG).show();
		
		if (sabDB.setPathDownloadHadith(ahadith.get(positionToUpdate).getId(), path))
		{
			ahadith.get(positionToUpdate).setDownload(true);
			ahadith.get(positionToUpdate).setFile(path);
			ahadith.get(positionToUpdate).setBottomLayoutShown(false);
			ahadith.get(positionToUpdate).setDownloading(false);
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onProgressDownload(final int progress) {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				refreshListItemViews(positionToUpdate);
				mSeekBar.setMax(100);
				
				mSeekBar.setProgress(progress);
				mTxvProgress.setText(progress + "%");
			}
		});
	}

	@Override
	public void onErrorDownload() {
		sabDownloadManager.cancelDownload();
		
		Toast.makeText(getActivity(), R.string.download_error, Toast.LENGTH_LONG).show();
		
		ahadith.get(positionToUpdate).setBottomLayoutShown(false);
		ahadith.get(positionToUpdate).setDownloading(false);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void requestRefrech() {
		
		initAhadith();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		
		getListView().setEnabled(enabled);
		getListView().setClickable(enabled);
		
		adapter.setEnabled(enabled);
	}
	
	@Override
	public void unregisterFromPlayer() {
		sabPlayer.setNotifier(null);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(sabPlayer.isPlaying())
			sabPlayer.stop();
	}
	
	private void refreshListItemViews(int position){
		View view = getSelecedView(position);
		if(view == null)
		{
			Log.w("", "Unable to get view for desired position, because it's not being displayed on screen.");
			return;
		}
		mSeekBar = (SeekBar) view.findViewById(R.id.seekbar_progress);
		mTxvProgress = (TextView) view.findViewById(R.id.txv_progress);
	}
	
	private void copyHadithToClipboard(int position){
		
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		    clipboard.setText(hadithText);
		} else {
		    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE); 
		    android.content.ClipData clip = android.content.ClipData.newPlainText("text label",hadithText);
		    clipboard.setPrimaryClip(clip);
		}
		
		Toast.makeText(getActivity(), getString(R.string.hadith_copied), Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.hadith_copy:
	            copyHadithToClipboard(0);
	            return true;
	        default:
	            return false;
	    }
	}
	
	boolean isPopupShown = false;
	boolean ignorePopup = false;
	public void showListenPopup(final int position){
		CharSequence[] listen_options = new CharSequence[]{getString(R.string.listen_single), 
				getString(R.string.listen_continue), getString(R.string.cancel)};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder
		.setTitle(R.string.choose_listen_mode)
		.setItems(listen_options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					isPopupShown = true;
					isContinueMode = false;
					onHadithListen(position);
					return;
				case 1:
					isPopupShown = true;
					isContinueMode = true;
					onHadithListen(position);
					return;
				default:
					isPopupShown = false;
					return;
				}
			}
		});

		// Create the AlertDialog object and return it
		final AlertDialog alert = builder.create();
		alert.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button btnPositive = alert.getButton(Dialog.BUTTON_POSITIVE);
				btnPositive.setTypeface(SAMFonts.getMOHANDFont());

				int dialogTitle = getResources().getIdentifier( "alertTitle", "id", "android" );
				TextView txv_title = (TextView) alert.findViewById(dialogTitle);
//				TextView txv_message = (TextView) alert.findViewById(android.R.id.message);
				
				txv_title.setTypeface(SAMFonts.getMOHANDFont()); 
//				txv_message.setTypeface(SVRFonts.getAqua()); 
			}
		});

		alert.show();
	}
	
}
