package com.sa7i7mouslem.entity;

import java.util.ArrayList;

public class Book extends SABEntity{
	
	private int bookId;
	private String name;
	private ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID " + getId() + "\n");
		sb.append("Name " + getName() + "\n");
//		sb.append("chapters size " + (chapters != null ? chapters.size() : "0"));
		return sb.toString();
	}

}
