package com.example.followme.modules;


public class User {
	
	private String sUserText; 
	private Boolean bUserOnline;
	
	public User() {
		sUserText = "";
		bUserOnline = false;
	}
	
	public User(String userName, boolean userOnline) {
		sUserText = userName;
		bUserOnline = userOnline;
	}

	public String getUserText() {
		return sUserText;
	}

	public void setUserText(String sUserText) {
		this.sUserText = sUserText;
	}

	public Boolean isUserOnline() {
		return bUserOnline;
	}

	public void setUserOnline(Boolean bUserOnline) {
		this.bUserOnline = bUserOnline;
	}
	
	
	
	

}
