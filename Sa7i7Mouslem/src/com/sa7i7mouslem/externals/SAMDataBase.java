package com.sa7i7mouslem.externals;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.sa7i7mouslem.entity.Book;
import com.sa7i7mouslem.entity.Chapter;
import com.sa7i7mouslem.entity.Comment;
import com.sa7i7mouslem.entity.Hadith;

/**
 * Sa7i7 Al Boukhari
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class SAMDataBase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "moslem.db";
    private static final int DATABASE_VERSION = 1;
    
    public SAMDataBase(Context context) {
    	super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }
    
    public ArrayList<Book> getAllBooks() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTables = "BOOKTable";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, null, null, null, null, null, null);

		ArrayList<Book> books = new ArrayList<Book>();
		if(c.moveToFirst()){
			do{
				Book book = new Book();
				book.setId(c.getInt(0));
				book.setName(c.getString(1));
				book.setBookId(c.getInt(2));
				
				Log.i("", book.toString());
				
				books.add(book);
				
			}while(c.moveToNext());
		}
		
		c.close();
		return books;

	}
    
    public ArrayList<Chapter> getAllBabsFromBook(int bookId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTables = "BABTable";
		
		String whereClause = "BOOKID = ?";
		String[] whereArgs = {String.valueOf(bookId)};

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Chapter> babs = new ArrayList<Chapter>();
		if(c.moveToFirst()){
			do{
				Chapter chapter = new Chapter();
				chapter.setId(c.getInt(0));
				chapter.setBabId(c.getInt(1));
				chapter.setName(c.getString(2));
				chapter.setBookId(c.getInt(3));
				
				Log.i("", chapter.toString());
				
				babs.add(chapter);
				
			}while(c.moveToNext());
		}
		
		c.close();
		return babs;

	}
    
    public ArrayList<Chapter> getAllBabsWithPage(int page) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTables = "BABTable";
		
		String whereClause = "Page = ?";
		String[] whereArgs = {String.valueOf(page)};

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Chapter> babs = new ArrayList<Chapter>();
		if(c.moveToFirst()){
			do{
				Chapter chapter = new Chapter();
				chapter.setId(c.getInt(0));
				chapter.setBabId(c.getInt(1));
				chapter.setName(c.getString(2));
				chapter.setBookId(c.getInt(3));
				
				Log.i("", chapter.toString());
				
				babs.add(chapter);
				
			}while(c.moveToNext());
		}
		
		c.close();
		return babs;

	}
       
    public ArrayList<Chapter> getAllBabs() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTables = "BABTable";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, null, null, null, null, null, null);

		ArrayList<Chapter> babs = new ArrayList<Chapter>();
		if(c.moveToFirst()){
			do{
				Chapter chapter = new Chapter();
				chapter.setId(c.getInt(0));
				chapter.setBabId(c.getInt(1));
				chapter.setName(c.getString(2));
				chapter.setBookId(c.getInt(3));
				
				Log.i("", chapter.toString());
				
				babs.add(chapter);
				
			}while(c.moveToNext());
		}
		
		c.close();
		return babs;

	}
    
    public ArrayList<Hadith> getAllHadithsWithPage(int page) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "page = ?";
		String[] whereArgs = {String.valueOf(page)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				Hadith hadith = new Hadith();
				hadith.setId(c.getInt(0));
				hadith.setTitleId(c.getInt(1));
				hadith.setText(c.getString(2));
				hadith.setLink(c.getString(3));
				hadith.setFile(c.getString(4));
				hadith.setFavorite(c.getInt(5) == 1 ? true:false);
				hadith.setDownload(c.getInt(6) == 1 ? true:false);
				hadith.setPageId(c.getInt(7));
				hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
				hadith.setShared(c.getInt(9) == 1 ? true:false);
				
				Log.i("", hadith.toString());

				ahadith.add(hadith);
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public ArrayList<Hadith> getAllHadithsWithBabId(int bab) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "BABID = ?";
		String[] whereArgs = {String.valueOf(bab)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				Hadith hadith = new Hadith();
				hadith.setId(c.getInt(0));
				hadith.setTitleId(c.getInt(1));
				hadith.setText(c.getString(2));
				hadith.setLink(c.getString(3));
				hadith.setFile(c.getString(4));
				hadith.setFavorite(c.getInt(5) == 1 ? true:false);
				hadith.setDownload(c.getInt(6) == 1 ? true:false);
				hadith.setPageId(c.getInt(7));
				hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
				hadith.setShared(c.getInt(9) == 1 ? true:false);
				
				Log.i("", hadith.toString());

				ahadith.add(hadith);
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}

    public ArrayList<Hadith> getFavoriteHadiths() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "IsFavorite = ?";
		String[] whereArgs = {String.valueOf(1)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				Hadith hadith = new Hadith();
				hadith.setId(c.getInt(0));
				hadith.setTitleId(c.getInt(1));
				hadith.setText(c.getString(2));
				hadith.setLink(c.getString(3));
				hadith.setFile(c.getString(4));
				hadith.setFavorite(c.getInt(5) == 1 ? true:false);
				hadith.setDownload(c.getInt(6) == 1 ? true:false);
				hadith.setPageId(c.getInt(7));
				hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
				hadith.setShared(c.getInt(9) == 1 ? true:false);
				
				Log.i("", hadith.toString());

				ahadith.add(hadith);
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public ArrayList<Hadith> getCommentedHadiths() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "HaveComment = ?";
		String[] whereArgs = {String.valueOf(1)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				Hadith hadith = new Hadith();
				hadith.setId(c.getInt(0));
				hadith.setTitleId(c.getInt(1));
				hadith.setText(c.getString(2));
				hadith.setLink(c.getString(3));
				hadith.setFile(c.getString(4));
				hadith.setFavorite(c.getInt(5) == 1 ? true:false);
				hadith.setDownload(c.getInt(6) == 1 ? true:false);
				hadith.setPageId(c.getInt(7));
				hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
				hadith.setShared(c.getInt(9) == 1 ? true:false);
				
				Log.i("", hadith.toString());

				ahadith.add(hadith);
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public ArrayList<Hadith> searchHadithWithText(String toSearchText) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = null;
		String[] whereArgs = null;
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				String cleanText = cleanPonctuation(c.getString(2));
				String cleanSearch = cleanPonctuation(toSearchText);
				Log.v("", "cleanText " + cleanText);
				if(cleanText.contains(cleanSearch)){
					Hadith hadith = new Hadith();
					hadith.setId(c.getInt(0));
					hadith.setTitleId(c.getInt(1));
					hadith.setText(c.getString(2));
					hadith.setLink(c.getString(3));
					hadith.setFile(c.getString(4));
					hadith.setFavorite(c.getInt(5) == 1 ? true:false);
					hadith.setDownload(c.getInt(6) == 1 ? true:false);
					hadith.setPageId(c.getInt(7));
					hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
					hadith.setShared(c.getInt(9) == 1 ? true:false);

					Log.i("", hadith.toString());

					ahadith.add(hadith);

					if (ahadith.size() == 300)
					{
						break;
					}
				}
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public ArrayList<Hadith> searchHadithFromFavoriteWithText(String toSearchText) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "IsFavorite = ?";
		String[] whereArgs = {String.valueOf("1")};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				String cleanText = cleanPonctuation(c.getString(2));
				String cleanSearch = cleanPonctuation(toSearchText);
				Log.v("", "cleanText " + cleanText);
				if(cleanText.contains(cleanSearch)){
					Hadith hadith = new Hadith();
					hadith.setId(c.getInt(0));
					hadith.setTitleId(c.getInt(1));
					hadith.setText(c.getString(2));
					hadith.setLink(c.getString(3));
					hadith.setFile(c.getString(4));
					hadith.setFavorite(c.getInt(5) == 1 ? true:false);
					hadith.setDownload(c.getInt(6) == 1 ? true:false);
					hadith.setPageId(c.getInt(7));
					hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
					hadith.setShared(c.getInt(9) == 1 ? true:false);

					Log.i("", hadith.toString());

					ahadith.add(hadith);

					if (ahadith.size() == 300)
					{
						break;
					}
				}
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public ArrayList<Hadith> searchHadithFromCommentedWithText(String toSearchText) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "HaveComment = ?";
		String[] whereArgs = {String.valueOf("1")};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				String cleanText = cleanPonctuation(c.getString(2));
				String cleanSearch = cleanPonctuation(toSearchText);
				Log.v("", "cleanText " + cleanText);
				if(cleanText.contains(cleanSearch)){
					Hadith hadith = new Hadith();
					hadith.setId(c.getInt(0));
					hadith.setTitleId(c.getInt(1));
					hadith.setText(c.getString(2));
					hadith.setLink(c.getString(3));
					hadith.setFile(c.getString(4));
					hadith.setFavorite(c.getInt(5) == 1 ? true:false);
					hadith.setDownload(c.getInt(6) == 1 ? true:false);
					hadith.setPageId(c.getInt(7));
					hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
					hadith.setShared(c.getInt(9) == 1 ? true:false);

					Log.i("", hadith.toString());

					ahadith.add(hadith);

					if (ahadith.size() == 300)
					{
						break;
					}
				}
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public ArrayList<Hadith> searchHadithByBabWithText(String toSearchText, int babId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "HadithTable";
		
		String whereClause = "BABID = ?";
		String[] whereArgs = {String.valueOf(babId)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Hadith> ahadith = new ArrayList<Hadith>();
		if(c.moveToFirst())
			do{
				String cleanText = cleanPonctuation(c.getString(2));
				String cleanSearch = cleanPonctuation(toSearchText);
				Log.v("", "cleanText " + cleanText);
				if(cleanText.contains(cleanSearch)){
					Hadith hadith = new Hadith();
					hadith.setId(c.getInt(0));
					hadith.setTitleId(c.getInt(1));
					hadith.setText(c.getString(2));
					hadith.setLink(c.getString(3));
					hadith.setFile(c.getString(4));
					hadith.setFavorite(c.getInt(5) == 1 ? true:false);
					hadith.setDownload(c.getInt(6) == 1 ? true:false);
					hadith.setPageId(c.getInt(7));
					hadith.setHaveComment(c.getInt(8) == 1 ? true:false);
					hadith.setShared(c.getInt(9) == 1 ? true:false);

					Log.i("", hadith.toString());

					ahadith.add(hadith);

					if (ahadith.size() == 300)
					{
						break;
					}
				}
			}while (c.moveToNext());
		
		c.close();
		return ahadith;

	}
    
    public boolean setFavoriteHadith(int hadithId, boolean isFavorite){    	
    	SQLiteDatabase db = getWritableDatabase();

		String sqlTable = "HadithTable";
		
		ContentValues values = new ContentValues();
		values.put("IsFavorite", isFavorite ? 1:0);
		
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(hadithId)};
		
		long updatedRow = db.update(sqlTable, values, whereClause, whereArgs);
		
		return updatedRow > 0;
    }
    
    public boolean isHadithFavorite(int hadithId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"IsFavorite"}; 
		String sqlTables = "HadithTable";
		
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(hadithId)};

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, whereClause, whereArgs, null, null, null);

		boolean isFav = false;
		if(c.moveToFirst()){
			isFav = c.getInt(0) == 1 ? true:false;
		}
		
		c.close();
		return isFav;

	}
    
    public boolean setHaveCommentToHadith(int hadithId, boolean haveComment){    	
    	SQLiteDatabase db = getWritableDatabase();

		String sqlTable = "HadithTable";
		
		ContentValues values = new ContentValues();
		values.put("HaveComment", haveComment ? 1:0);
		
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(hadithId)};
		
		long updatedRow = db.update(sqlTable, values, whereClause, whereArgs);
		
		return updatedRow > 0;
    }
    
    public boolean setIsSharedToHadith(int hadithId){    	
    	SQLiteDatabase db = getWritableDatabase();

		String sqlTable = "HadithTable";
		
		ContentValues values = new ContentValues();
		values.put("ISShared", 1);
		
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(hadithId)};
		
		long updatedRow = db.update(sqlTable, values, whereClause, whereArgs);
		
		return updatedRow > 0;
    }
    
    public boolean setPathDownloadHadith(int hadithId, String path){    	
    	SQLiteDatabase db = getWritableDatabase();

		String sqlTable = "HadithTable";
		
		ContentValues values = new ContentValues();
		values.put("IsDownload", 1);
		values.put("SoundfilePath", path);
		
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(hadithId)};
		
		long updatedRow = db.update(sqlTable, values, whereClause, whereArgs);
		
		return updatedRow > 0;
    }
    
    
    public ArrayList<Comment> getCommentsWithHadithID(int hadithId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "COMMENTS";
		
		String whereClause = "hadithId = ?";
		String[] whereArgs = {String.valueOf(hadithId)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<Comment> comments = new ArrayList<Comment>();
		if(c.moveToFirst())
			do{
				Comment comment = new Comment();
				comment.setId(c.getInt(0));
				comment.setHadithId(c.getInt(1));
				comment.setText(c.getString(2));
				comment.setTitle(c.getString(3));
				
				Log.i("", comment.toString());

				comments.add(comment);
			}while (c.moveToNext());
		
		c.close();
		return comments;

	}
    
    public ArrayList<Comment> getAllComments() {

  		SQLiteDatabase db = getReadableDatabase();
  		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

  		String sqlTable = "COMMENTS";
  		
  		qb.setTables(sqlTable);
  		Cursor c = qb.query(db, null, null, null, null, null, null);

  		ArrayList<Comment> comments = new ArrayList<Comment>();
  		if(c.moveToFirst())
  			do{
  				Comment comment = new Comment();
  				comment.setId(c.getInt(0));
  				comment.setHadithId(c.getInt(1));
  				comment.setText(c.getString(2));
  				comment.setTitle(c.getString(3));
  				
  				Log.i("", comment.toString());

  				comments.add(comment);
  			}while (c.moveToNext());
  		
  		c.close();
  		return comments;

  	}
    
    public boolean addComment(int hadithId, Comment comment){    	
    	SQLiteDatabase db = getWritableDatabase();

    	String sqlTable = "COMMENTS";
    	
		ContentValues values = new ContentValues();
		values.put("hadithId", hadithId);
		values.put("CommentTitle", comment.getTitle());
		values.put("CommentText", comment.getText());
		
		long insertedId = db.insertWithOnConflict(sqlTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		if(insertedId != -1)
			setHaveCommentToHadith(hadithId, true);
		
		return insertedId != -1;
    }

    public boolean removeComment(Comment comment){    	
    	SQLiteDatabase db = getWritableDatabase();

    	String sqlTable = "COMMENTS";
    	
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(comment.getId())};
		
		long insertedId = db.delete(sqlTable, whereClause, whereArgs);
		
		if(getCommentsWithHadithID(comment.getHadithId()).size() == 0)
			setHaveCommentToHadith(comment.getHadithId(), false);
		
		return insertedId > 0;
    }
    
    public boolean updateComment(Comment comment){    	
    	SQLiteDatabase db = getWritableDatabase();

		String sqlTable = "COMMENTS";
		
		ContentValues values = new ContentValues();
		values.put("CommentText", comment.getText());
		values.put("CommentTitle", comment.getTitle());
		
		String whereClause = "ID = ?";
		String[] whereArgs = {String.valueOf(comment.getId())};
		
		long insertedId = db.update(sqlTable, values, whereClause, whereArgs);
		
		return insertedId > 0;
    }
    
    public boolean isHadithCommented(int hadithId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "COMMENTS";
		
		String whereClause = "hadithId = ?";
		String[] whereArgs = {String.valueOf(hadithId)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		boolean isCommented = false;
		if(c.moveToFirst())
			isCommented = true;
		
		c.close();
		return isCommented;
	}
    
    public static String formatHadith(String text){
    	String header = "<span lang=\"AR\" dir=\"RTL\" style=\"font-size:13.5pt; font-family:\"Simplified Arabic\";mso-ascii-font-family:\"Times New Roman\"; mso-fareast-font-family:\"Times New Roman\";mso-hansi-font-family:\"Times New Roman\"; mso-ansi-language:EN-US;mso-fareast-language:EN-US;mso-bidi-language:AR\">";
    	String tail = "</span>";
    	
    	return header + text + tail;
    }
    
    public static String cleanPonctuation(String text){
    	text = text.replace("َ", ""); // fatha
    	text = text.replace("ُ", ""); // dhamma
        text = text.replace("ِ", ""); // kasra
        text = text.replace("ً", ""); // fatha double
        text = text.replace("ٌ", ""); // dhamma double
        text = text.replace("ٍ", ""); // kasra double
        text = text.replace("ْ", ""); // soukoun
        text = text.replace("ّ", ""); // chadda
        return text;
    }
    
}