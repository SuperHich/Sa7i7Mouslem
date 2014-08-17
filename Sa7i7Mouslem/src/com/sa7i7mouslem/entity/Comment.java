package com.sa7i7mouslem.entity;

public class Comment {
	
	private int id;
	private int hadithId;
	private String title;
	private String text;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHadithId() {
		return hadithId;
	}
	public void setHadithId(int hadithId) {
		this.hadithId = hadithId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID " + getId() + "\n");
		sb.append("HadithID " + getHadithId() + "\n");
		sb.append("Title " + getTitle() + "\n");
		sb.append("Text " + getText());
		return sb.toString();
	}
}
