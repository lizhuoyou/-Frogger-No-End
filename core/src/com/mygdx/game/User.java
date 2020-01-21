// User.java
// by Lizhuo You and Yanbo Hou
// the class user is used to create user object to save their score and username
package com.mygdx.game;
import java.io.Serializable;

public class User implements Serializable{
	//simple user class. It is to input or output user's data
	private String username;
	private int score;

	public User(String username) {
		super();
		this.username = username;
		score = 0;
	}

	public String getUsername() {
		return username;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
