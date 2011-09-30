package com.derekerdmann.androidtalk;

import java.util.Date;

/**
 * Represents a single question from StackOverflow
 * @author Derek Erdmann
 */
public class Question {

	private String title;
	private Date created;
	private int upvotes;
	private int downvotes;
	
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public int getUpvotes() {
		return upvotes;
	}
	
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	
	public int getDownvotes() {
		return downvotes;
	}
	
	public void setDownvotes(int downvotes) {
		this.downvotes = downvotes;
	}
	
}
