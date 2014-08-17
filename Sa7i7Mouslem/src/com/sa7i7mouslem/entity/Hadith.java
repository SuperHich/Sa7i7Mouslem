package com.sa7i7mouslem.entity;

public class Hadith extends SABEntity {
	
	private int titleId;
	private String text;
	private String file;
	private int bookId;
	private String link;
	private int pageId;
	private boolean isDownload = false;
	private boolean isFavorite = false;
	private boolean haveComment = false;
	private boolean isShared = false;
	
	private boolean isShown = false;
	private boolean isBottomLayoutShown = false;
	private boolean isPlaying = false;
	private boolean isDownloading = false;
	
	public int getTitleId() {
		return titleId;
	}
	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public boolean isDownload() {
		return isDownload;
	}
	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}
	public boolean isFavorite() {
		return isFavorite;
	}
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public boolean isHaveComment() {
		return haveComment;
	}
	public void setHaveComment(boolean haveComment) {
		this.haveComment = haveComment;
	}
	public boolean isShared() {
		return isShared;
	}
	public void setShared(boolean isShared) {
		this.isShared = isShared;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID " + getId() + "\n");
		sb.append("BookID " + getBookId() + "\n");
		sb.append("TitleID " + getTitleId() + "\n");
		sb.append("Text " + getText() + "\n");
		sb.append("file " + getFile() + "\n");
		sb.append("Link " + getLink() + "\n");
		sb.append("Page " + getPageId() + "\n");
		sb.append("isDownload " + isDownload() + "\n");
		sb.append("isFavorite " + isFavorite() + "\n");
		sb.append("haveComment " + isHaveComment() + "\n");
		sb.append("isShared " + isShared());
		return sb.toString();
	}
	public boolean isShown() {
		return isShown;
	}
	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}
	public boolean isBottomLayoutShown() {
		return isBottomLayoutShown;
	}
	public void setBottomLayoutShown(boolean isBottomLayoutShown) {
		this.isBottomLayoutShown = isBottomLayoutShown;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public boolean isDownloading() {
		return isDownloading;
	}
	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}
	
}
