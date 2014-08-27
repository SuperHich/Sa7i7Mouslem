package com.sa7i7mouslem.externals;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sa7i7mouslem.adapters.IFragmentNotifier;
import com.sa7i7mouslem.entity.Book;
import com.sa7i7mouslem.entity.Chapter;
import com.sa7i7mouslem.entity.Hadith;
import com.sa7i7mouslem.entity.Page;

public class SAMManager {

	static final String TAG = "SABManager";
	
	private static final String URL_BASE 		= "http://radio.alssunnah.com/hadeeth/api/";
	private static final String URL_BOOK 		= URL_BASE + "book/?page=";
	private static final String URL_BOOK_ENT 	= URL_BASE + "book_ent/?page=";
	private static final String URL_AHADITH 	= URL_BASE + "ahadith/?page=";

	public static String SoundPath;
	private IDownloadComplete downloadNotifier;
	private IFragmentNotifier fragmentNotifier, fragmentNotifier2;
	
	private static SAMManager mInstance = null;
	private static SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private JSONParser jsonParser;
	private Context mContext;
	
	private ArrayList<Page> pages = new ArrayList<Page>();
	private ArrayList<Book> books;
	private ArrayList<Chapter> chapters;
	private ArrayList<Hadith> ahadith;
	
	private HashMap<Integer, ArrayList<Chapter>> chaptersMap = new HashMap<Integer, ArrayList<Chapter>>();
	private HashMap<Integer, ArrayList<Hadith>> ahadithMap = new HashMap<Integer, ArrayList<Hadith>>();
	
	public SAMManager(Context context) {
		
		mContext = context;
		jsonParser = new JSONParser();
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		editor = settings.edit();
	}

	public synchronized static SAMManager getInstance(Context context) {
		if (mInstance == null)
			mInstance = new SAMManager(context);

		return mInstance;
	}

	
	public ArrayList<Book> getBooksByPage(int pageNb) {
		ArrayList<Book> books = new ArrayList<Book>();
		JSONArray array = jsonParser.getJSONFromUrl(URL_BOOK.concat(String.valueOf(pageNb)));
		if (array != null) 
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject bObj = array.getJSONObject(i);
				Book book = new Book();
				book.setId(Integer.valueOf(bObj.getString("ID")));
				book.setName(bObj.getString("BookName"));
				book.setChapters(chaptersMap.get(book.getId()));
				
				Log.i(TAG, book.toString());
				
				books.add(book);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		this.books = books;
		
		return books;
	}
	
	public ArrayList<Chapter> getChaptersByPage(int pageNb) {

		chaptersMap.clear();
		
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		JSONArray array = jsonParser.getJSONFromUrl(URL_BOOK_ENT.concat(String.valueOf(pageNb)));
		if (array != null) 
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject cObj = array.getJSONObject(i);
				Chapter chapter = new Chapter();
				chapter.setId(Integer.valueOf(cObj.getString("ID")));
				chapter.setName(cObj.getString("Name"));
				chapter.setBookId(Integer.valueOf(cObj.getString("BOOK_ID")));
				chapter.setAhadith(ahadithMap.get(chapter.getId()));
				
				Log.i(TAG, chapter.toString());
				
				ArrayList<Chapter> list = chaptersMap.get(chapter.getBookId());
				if(list == null)
				{
					list = new ArrayList<Chapter>();
				}
				
				list.add(chapter);
				chaptersMap.put(chapter.getBookId(), list);
				
				chapters.add(chapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		this.chapters = chapters;
		
		return chapters;
	}
	
	public ArrayList<Hadith> getAhadithByPage(int pageNb) {
		
		ahadithMap.clear();
		
		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		JSONArray array = jsonParser.getJSONFromUrl(URL_AHADITH.concat(String.valueOf(pageNb)));
		
		if (array != null) 
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject cObj = array.getJSONObject(i);
				Hadith hadith = new Hadith();
				hadith.setId(Integer.valueOf(cObj.getString("ID")));
				hadith.setTitleId(Integer.valueOf(cObj.getString("TitleID")));
				hadith.setText(cObj.getString("HadithText"));
				hadith.setBookId(Integer.valueOf(cObj.getString("BOOK")));
				hadith.setFile(cObj.getString("file"));
				
				Log.i(TAG, hadith.toString());
				
				ArrayList<Hadith> list = ahadithMap.get(hadith.getTitleId());
				if(list == null)
				{
					list = new ArrayList<Hadith>();
				}
				
				list.add(hadith);
				ahadithMap.put(hadith.getTitleId(), list);
				
				ahadith.add(hadith);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		this.ahadith = ahadith;
		
		return ahadith;
	}

	public ArrayList<Page> getPages() {
		return pages;
	}

	public void setPages(ArrayList<Page> pages) {
		this.pages = pages;
	}

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public ArrayList<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}

	public ArrayList<Hadith> getAhadith() {
		return ahadith;
	}

	public void setAhadith(ArrayList<Hadith> ahadith) {
		this.ahadith = ahadith;
	}

	public IDownloadComplete getDownloadNotifier() {
		return downloadNotifier;
	}

	public void setDownloadNotifier(IDownloadComplete fragmentNotifier) {
		this.downloadNotifier = fragmentNotifier;
	}
	
	public IFragmentNotifier getFragmentNotifier() {
		return fragmentNotifier;
	}

	public void setFragmentNotifier(IFragmentNotifier fragmentNotifier) {
		this.fragmentNotifier = fragmentNotifier;
	}

	public IFragmentNotifier getFragmentNotifier2() {
		return fragmentNotifier2;
	}

	public void setFragmentNotifier2(IFragmentNotifier fragmentNotifier2) {
		this.fragmentNotifier2 = fragmentNotifier2;
	}
	
}