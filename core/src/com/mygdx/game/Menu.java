// Menu.java
// by Lizhuo You and Yanbo Hou
// this is the data base behind the loading page of the game, contains login, intro and game start
package com.mygdx.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Menu {
	//sprites on the menu
	private String img = "pictures/menu.jpg";
	private String login = "pictures/login.png";
	private String help = "pictures/help.png";
	private String start = "pictures/start.png";
	private String back = "pictures/back.png";
	//contain all the user has recorded
	private List<User> users = new ArrayList<User>();
	//file of introduction
	private final File intro = new File("intro.txt");

	public Menu() {
		init();
	}

	//read from the txt file to get all the data before
	public void init() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("users.txt"));
			users = (List<User>) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//save all the user in the arraylist
	public void saveUser() {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("users.txt"));
			oos.writeObject(users);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<User> getUsers() {
		return users;
	}

	//read the intro.txt file
	public String showIntro() {
		FileInputStream fis = null;
		String info = "";
		try {
			fis = new FileInputStream(intro);
			byte[] b = new byte[1024];
			int len;
			while ((len = fis.read(b)) != -1) {
				String str = new String(b, 0, len);
				info += str;
			}
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return info;
	}

	public String getImg() {
		return img;
	}

	public String getLogin() {
		return login;
	}

	public String getHelp() {
		return help;
	}

	public String getStart() {
		return start;
	}

	public String getBack() {
		return back;
	}

}
