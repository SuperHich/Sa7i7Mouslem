package com.sa7i7mouslem;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sa7i7mouslem.AhadithSearchDialog.EditNameDialogListener;
import com.sa7i7mouslem.adapters.IMenuListener;
import com.sa7i7mouslem.adapters.MenuCustomAdapter;
import com.sa7i7mouslem.entity.Book;
import com.sa7i7mouslem.entity.Chapter;
import com.sa7i7mouslem.entity.Comment;
import com.sa7i7mouslem.entity.Hadith;
import com.sa7i7mouslem.externals.SAMDataBase;
import com.sa7i7mouslem.utils.MySuperScaler;



@SuppressLint({ "Recycle"})
public class MainActivity extends MySuperScaler implements IMenuListener, OnTouchListener, EditNameDialogListener{

	public static final String COMMENTS_FRAGMENT = "comments_fragment";
	public static final String ADD_COMMENT_FRAGMENT = "add_comment_fragment";
	public static final String EDIT_COMMENT_FRAGMENT = "edit_comment_fragment";
	public static final String FAVOURITE_FRAGMENT = "favourite_fragment";
	public static final String BOOKS_FRAGMENT = "books_fragment";
	public static final String ABWAB_FRAGMENT = "abwab_fragment";
	public static final String AHADITH_FRAGMENT = "ahadith_fragment";
	
	private String currentFragment;
	private boolean isFromBooksFragment = false;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private Button btn_menu, btn_search;

	public SAMDataBase sabDB;

	private ActionBarDrawerToggle mDrawerToggle;
	RelativeLayout mainView ;

	public static final int MESSAGE_START = 1;
	private int lastPosition = 1;
	private String lastText = "";
	private boolean isFirstStart = true;
	private int lastBabId = -1, lastBookId = -1;
	
	private ListFragment fragment, fragment1;
	private Fragment fragment2;
	
	private boolean isBackEnabled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sabDB = new SAMDataBase(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.right_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		MenuCustomAdapter adapter = new MenuCustomAdapter(this, getResources

				().obtainTypedArray(R.array.menu_drawables));


		mDrawerList.setAdapter(adapter);
		mDrawerList.setDivider(null);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mainView = (RelativeLayout) findViewById(R.id.content_frame);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 

				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				mainView.setTranslationX(- slideOffset * drawerView.getWidth());
				mDrawerLayout.bringChildToFront(drawerView);
				mDrawerLayout.requestLayout();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//		if (savedInstanceState == null) {
		//			selectItem(1);
		//		}

		btn_menu = (Button) findViewById(R.id.menu);
		btn_menu.setOnTouchListener(this);
		//		btn_menu.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
		//					mDrawerLayout.openDrawer(Gravity.RIGHT);
		//				else
		//					mDrawerLayout.closeDrawer(Gravity.RIGHT);		
		//			}
		//		});

		btn_search = (Button) findViewById(R.id.search);
		btn_search.setOnTouchListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if(sabDB == null){
			sabDB = new SAMDataBase(this);
		}

	}


	@Override
	protected void onStart() {
		super.onStart();

		if(isFirstStart){
//			Message msg = Message.obtain();
//			msg.what = MESSAGE_START;
//			mHandler.sendMessageDelayed(msg, 10);
			
			selectItem(lastPosition);
			scaled = true ;
			
			isFirstStart = false;
		}
	}


	@Override
	protected void onStop() {
		super.onStop();

		if(sabDB != null){
			sabDB.close();
			sabDB = null;
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			selectItem(position);
		}
	}


	private void selectItem(int position) {
		
		isFromBooksFragment = false;
		isBackEnabled = false;
		btn_menu.setBackgroundResource(R.drawable.menu);
		
		if(position > 4)
			return;

		lastPosition = position;
		
		
		Bundle args = null;
		// update the main content by replacing fragments
		
		switch (position) {
		case 0:
//			gotoFavouriteFragment();
//			fragment = new FavouriteAhadithFragment();
			fragment = new AhadithFragment();
			args = new Bundle();
			args.putInt(AhadithFragment.ARG_AHADITH, position);
			btn_search.setVisibility(View.VISIBLE);
			break;
		case 1:
			fragment = new AhadithFragment();
			args = new Bundle();
			args.putInt(AhadithFragment.ARG_AHADITH, position);
			btn_search.setVisibility(View.VISIBLE);
			break;
		case 2:
			fragment = new AhadithFragment();
			args = new Bundle();
			args.putInt(AhadithFragment.ARG_AHADITH, position);
			btn_search.setVisibility(View.VISIBLE);
			break;
		case 3:
			fragment = new AbwabFragment();
			currentFragment = ABWAB_FRAGMENT;
			btn_search.setVisibility(View.GONE);
			break;
		case 4:
			fragment = new BooksFragment();
			currentFragment = BOOKS_FRAGMENT;
			btn_search.setVisibility(View.GONE);
			break;
		default:
			fragment = new AhadithFragment();
			break;

		}
		
		
		if(args != null)
			fragment.setArguments(args);

//		if(position != 0)
			switchTab(fragment, false);

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);

	}


	private void switchTab(Fragment tab, boolean withAnimation) {
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.content_frame);

		final FragmentTransaction ft = fm.beginTransaction();
		if(withAnimation)
			ft.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
		
		if (fragment == null) {
			ft.add(R.id.content_frame, tab);

		} else {
			ft.replace(R.id.content_frame, tab);
			scaled = false ;
		}
		
		ft.commit();
		
	}

	@Override
	public void onMenuItemClicked(int position) {
		selectItem(position);
	}


//	private Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//
//			switch (msg.what) {
//			case MESSAGE_START :
//				selectItem(lastPosition);
//				break;
//
//			}
//			super.handleMessage(msg);
//
//		}};


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				Button view = (Button) v;
				view.getBackground().setColorFilter(0x77000000, 

						PorterDuff.Mode.SRC_ATOP);
				v.invalidate();
				break;
			}
			case MotionEvent.ACTION_UP: {
				
				Button view = (Button) v;
				view.getBackground().clearColorFilter();
				view.invalidate();

				switch (v.getId()) {
				case R.id.menu:
					if(isBackEnabled)
					{		
						onBackPressed();
					}
					else{
						if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
							mDrawerLayout.openDrawer(Gravity.RIGHT);
						else
							mDrawerLayout.closeDrawer(Gravity.RIGHT);		
					}
					break;
				case R.id.search:
					//show search dialog;
					showSearchDialog();
					break;
				default:
					break;
				}

			}
			case MotionEvent.ACTION_CANCEL: {
				Button view = (Button) v;
				view.getBackground().clearColorFilter();
				view.invalidate();
				break;
			}
			}
			return true;
		}

		private void showSearchDialog() {
			FragmentManager fm = getSupportFragmentManager();
			AhadithSearchDialog searchDialog = new AhadithSearchDialog(lastText);
			searchDialog.show(fm, "fragment_search_keyword");
		}

		@Override
		public void onFinishEditDialog(String inputText) {

			lastText = inputText;

			// update the main content by replacing fragments
			fragment = new AhadithFragment();
			Bundle args = new Bundle();
			args.putInt(AhadithFragment.ARG_AHADITH, AhadithFragment.TYPE_AHADITH_KEYWORD_ID);
			args.putInt(AhadithFragment.ARG_AHADITH_SEARCH, lastPosition);
			args.putString(AhadithFragment.ARG_AHADITH_KEYWORD_TEXT, inputText);
			args.putInt(AhadithFragment.ARG_BAB_ID, lastBabId);
			fragment.setArguments(args);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();

			ft.replace(R.id.content_frame, fragment);
			scaled = false ;
			ft.commit();

		}
		
		public void onBookItemClicked(Book book){
			
			isFromBooksFragment = true;
			isBackEnabled = true;
			currentFragment = ABWAB_FRAGMENT;
			btn_menu.setBackgroundResource(R.drawable.back_list);
			
			lastBookId = book.getBookId();
			
			// update the main content by replacing fragments
			fragment = new AbwabFragment();
			Bundle args = new Bundle();
			args.putInt(AbwabFragment.ARG_BOOKID, lastBookId);
			fragment.setArguments(args);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.left_in, R.anim.left_out);
			
			ft.replace(R.id.content_frame, fragment);
			scaled = false ;
			ft.commit();
			
		}
		
		public void onBabItemClicked(Chapter chapter){
			
			isBackEnabled = true;
			currentFragment = AHADITH_FRAGMENT;
			btn_menu.setBackgroundResource(R.drawable.back_list);
			btn_search.setVisibility(View.VISIBLE);
			
			lastBabId = chapter.getBabId();
			
			// update the main content by replacing fragments
			fragment = new AhadithFragment();
			Bundle args = new Bundle();
			args.putInt(AhadithFragment.ARG_AHADITH, AhadithFragment.TYPE_AHADITH_BY_BAB);
			args.putInt(AhadithFragment.ARG_BAB_ID, chapter.getBabId());
			fragment.setArguments(args);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.left_in, R.anim.left_out);
			
			ft.replace(R.id.content_frame, fragment);
			scaled = false ;
			ft.commit();
			
		}
		
		public void gotoFavouriteFragment(){

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.left_in, R.anim.left_out);

			fragment1 = (ListFragment) getSupportFragmentManager().findFragmentByTag(FAVOURITE_FRAGMENT);

			if(fragment1 == null){
				fragment1 = new FavouriteAhadithFragment();

				transaction.replace(R.id.fragment_view, fragment1, FAVOURITE_FRAGMENT);
				transaction.addToBackStack(FAVOURITE_FRAGMENT);
			}else{
				transaction.attach(fragment1);
			}

			scaled = false;
			transaction.commit();

			setEnabled(false);
		}

		public void gotoCommentsFragment(Hadith hadith){

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

			fragment1 = (ListFragment) getSupportFragmentManager().findFragmentByTag(COMMENTS_FRAGMENT);

			if(fragment1 == null){
				fragment1 = new CommentsFragment(hadith);

				transaction.replace(R.id.fragment_view, fragment1, COMMENTS_FRAGMENT);
				transaction.addToBackStack(COMMENTS_FRAGMENT);
			}else{
				transaction.attach(fragment1);
			}

			scaled = false;
			transaction.commit();

			setEnabled(false);
		}
		
		public void gotoAddEditCommentFragment(String fragmentTAG, Comment comment, int hadithId){

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

			fragment2 = getSupportFragmentManager().findFragmentByTag(fragmentTAG);

			if(fragment2 == null){
				if(fragmentTAG.equals(ADD_COMMENT_FRAGMENT))
					fragment2 = new AddCommentFragment(hadithId);
				else 
					fragment2 = new AddCommentFragment(comment);
				
				transaction.replace(R.id.new_fragment_view, fragment2, fragmentTAG);
				transaction.addToBackStack(fragmentTAG);
			}else{
				transaction.attach(fragment2);
			}

			scaled = false;
			transaction.commit();
			
			sabManager.getFragmentNotifier2().setEnabled(false);
		}
		
		public void setEnabled(boolean enabled){
			mDrawerLayout.setEnabled(enabled);
			btn_search.setEnabled(enabled);
			btn_menu.setEnabled(enabled);
			
			sabManager.getFragmentNotifier().setEnabled(enabled);
		}
		
		@Override
		public void onBackPressed() {
			
			if(isBackEnabled)
			{		
				if(currentFragment.equals(AHADITH_FRAGMENT))
				{
					//goto ABWAB FRAG
					fragment = new AbwabFragment();
					Bundle args = new Bundle();
					args.putInt(AbwabFragment.ARG_BOOKID, lastBookId);
					fragment.setArguments(args);
					currentFragment = ABWAB_FRAGMENT;
					btn_search.setVisibility(View.GONE);
					
					if(!isFromBooksFragment)
					{
						btn_menu.setBackgroundResource(R.drawable.menu);
						isBackEnabled = false;
					}

				}else if(currentFragment.equals(ABWAB_FRAGMENT) && isFromBooksFragment)
				{
					// goto BOOKs Frag
					fragment = new BooksFragment();
					currentFragment = BOOKS_FRAGMENT;
					
					btn_menu.setBackgroundResource(R.drawable.menu);
					isBackEnabled = false;
				}

				switchTab(fragment, true);
				
				return;
			}
			
			if(fragment2 != null){
				fragment2 = null;
				sabManager.getFragmentNotifier2().setEnabled(true);
			} 
			else if(fragment1 != null)
			{
				fragment1 = null;
				setEnabled(true);
			}
			else if(fragment != null){
				fragment = null;
			}
			
			super.onBackPressed();
		}
		
}