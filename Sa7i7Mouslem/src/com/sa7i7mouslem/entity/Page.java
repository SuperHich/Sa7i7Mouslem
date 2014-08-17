package com.sa7i7mouslem.entity;

import java.util.ArrayList;

public class Page extends SABEntity{
	
	private ArrayList<Book> books;

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID " + getId() + "\n");
		sb.append("books size " + (books != null ? books.size() : "0"));
		return sb.toString();
	}

}
