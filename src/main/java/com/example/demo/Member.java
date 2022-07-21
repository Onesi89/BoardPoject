package com.example.demo;


public class Member {

	private int aid;
	private String userId;
	private String password;
	private String name;
	private String email;
	private String date;
	private String hash;
	
	//총 작성한 글 수
	private int totalWriteNumber;
	//댓글 수
	private int totalCommentNumber;
	
	

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getTotalWriteNumber() {
		return totalWriteNumber;
	}
	public void setTotalWriteNumber(int totalWriteNumber) {
		this.totalWriteNumber = totalWriteNumber;
	}
	public int getTotalCommentNumber() {
		return totalCommentNumber;
	}
	public void setTotalCommentNumber(int totalCommentNumber) {
		this.totalCommentNumber = totalCommentNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	
	}
}
