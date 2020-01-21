// MyGdxGame.java
// by Lizhuo You and Yanbo Hou
// this is a simple frogger game.
// Features:
// 1. To add more difficulty, we make the gme with infinite possibilities by
// 	  letting the program randomly set up the roads and the object
// 2. massive use of objects. We create user, menu, frogger,
// 	  turtle, vehicle, crocodile, tool, log, snake to make this whole game.
// 3. User has its own data that can record his highest score. The menu gives
// 	  you some tips to play this game and log in with your own account.
// 4. Guaranteed frequency by using time library.
// 5. Introduction makes the game user friendly.
// 6. Music is equipped with.
// Note:
//	  Turtle, vehicle, crocodile, log, snake are barriers on your way to succeed.
//	  Frogger is the object which is the only thing you can control.
//    The game is endless so you can always challenge yourself.


// load libraries and packages.
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sun.org.apache.regexp.internal.RE;
import java.awt.*;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter implements TextInputListener {
	// this is the main class that connects everything and display the game.
	private SpriteBatch batch;
	private Tools tools;
	private Sprite background, login, help, start, back; // sprites for pictures
	private String username = "";
	private User u;
	private Menu m;
	private boolean helpFlag = false; 	// if the help document is opened
	private boolean IntroEnd = false;
	private int mx, my;             // mouse x,y values.
	private int score;      // it tracks how many streets the frog has passed.
	private int shiftValue;         // this is how much the roads shift while frog is jumping.
	private long timeStart, timeNow;        // track run time so the program can be smooth.
	private Sprite[] roads, banks, frogs, turtlePics, crocodilePics, cars, trucks, logPics;   // sprites for pictures.
	private Sprite snakePic, restart;
	private String[] roadTypes = {"grass", "road4", "road6", "river5", "river7"};   // used to set roads.
	private String[] roadArray = new String[18];        // used to indicate roads.
	private Frogger frog;
	private BitmapFont font;
	private Sound sound;

	// array lists of the objects
	private ArrayList<vehicle> vehicles = new ArrayList<vehicle>();
	private ArrayList<log> logs = new ArrayList<log>();
	private ArrayList<crocodile> crocodiles = new ArrayList<crocodile>();
	private ArrayList<snake> snakes = new ArrayList<snake>();
	private ArrayList<turtle> turtles = new ArrayList<turtle>();

	private void loadGraphics(){
		// load the sprites for the game.

		// for intro and menu
		background = tools.loadSprite(m.getImg(), 1190, 700);
		login = tools.loadSprite(m.getLogin(), 180, 50);
		login.setCenter(595, 200);
		help = tools.loadSprite(m.getHelp(), 70, 70);
		help.setCenter(1155, 35);
		start = tools.loadSprite(m.getStart(), 200, 200);
		start.setCenter(595, 200);
		back = tools.loadSprite(m.getBack(), 100, 100);
		back.setCenter(1140, 50);

		// for actual gaming.
		roads = tools.loadSprites(new String[]{"pictures/roadSolid.png", "pictures/roadGrass.png",
				"pictures/roadRiver.png"}, 75, 75);
		banks = tools.loadSprites(new String[]{"pictures/bank1.png", "pictures/bank2.png","pictures/bank11.png",
				"pictures/bank22.png"},75, 75);

		frogs = tools.loadSprites(new String[]{"pictures/frog1.png", "pictures/frog2.png", "pictures/frog3.png",
				"pictures/frog4.png", "pictures/frog5.png", "pictures/frog6.png", "pictures/frogDie.png"}, 70, 70);
		turtlePics = tools.loadSprites(new String[]{"pictures/turtle1.png", "pictures/turtle2.png", "pictures/turtle3.png",
				"pictures/turtle4.png", "pictures/turtle5.png", "pictures/turtle6.png", "pictures/turtle7.png",
				"pictures/turtleHide1.png", "pictures/turtleHide2.png", "pictures/turtleHide3.png",
				"pictures/turtleHide4.png", "pictures/turtleHide4.png" , "pictures/turtleHide4.png"  }, 70, 70);
		crocodilePics = tools.loadSprites(new String[]{"pictures/crocodile1.png", "pictures/crocodile2.png"}, 180, 70);
		cars = tools.loadSprites(new String[]{"pictures/car1.png", "pictures/car2.png", "pictures/car3.png",
				"pictures/car11.png", "pictures/car22.png", "pictures/car33.png"}, 140, 70);
		trucks = tools.loadSprites(new String[]{"pictures/truck1.png", "pictures/truck2.png", "pictures/truck11.png",
				"pictures/truck22.png"}, 210, 70);
		Sprite log1 = tools.loadSprite("pictures/log1.png", 140, 70);
		Sprite log2 = tools.loadSprite("pictures/log2.png", 210, 70);
		logPics = new Sprite[]{log1, log2};
		restart = tools.loadSprite("pictures/replay.png", 100, 100);

		snakePic = tools.loadSprite("pictures/snake.png", 180, 70);
	}

	private void setRoads(){
		// this is to randomly set the initial road array. used at the start or the restart of the game.
		int roadIndex = 0;              // this is used to set the roads.
		roadArray[roadIndex] = "grass"; // the initial row of the roads must be grass.
		roadIndex++;

		while (roadIndex < 18) {        // the while loop and the restrictions ensures no repeated roads.
			String road = roadTypes[(int)(Math.random()*5)];
			if (road.equals("grass") && !roadArray[roadIndex - 1].equals("grass")) {
				roadArray[roadIndex] = "grass";
				roadIndex++;
			} else if (road.equals("road4") && (roadIndex < 14) && !roadArray[roadIndex - 1].equals("solid")) {
				for (int i = 0; i < 4; i++) {
					roadArray[roadIndex] = "solid";
					roadIndex++;
				}
			} else if (road.equals("road6") && (roadIndex < 12) && !roadArray[roadIndex - 1].equals("solid")) {
				for (int i = 0; i < 6; i++) {
					roadArray[roadIndex] = "solid";
					roadIndex++;
				}
			} else if (road.equals("river5") && (roadIndex < 13) && !roadArray[roadIndex - 1].equals("bankDownward")) {
				roadArray[roadIndex] = "bankUpward";
				roadIndex++;
				for (int i = 0; i < 3; i++) {
					roadArray[roadIndex] = "water";
					roadIndex++;
				}
				roadArray[roadIndex] = "bankDownward";
				roadIndex++;
			} else if (road.equals("river7") && (roadIndex < 11) && !roadArray[roadIndex - 1].equals("bankDownward")) {
				roadArray[roadIndex] = "bankUpward";
				roadIndex++;
				for (int i = 0; i < 5; i++) {
					roadArray[roadIndex] = "water";
					roadIndex++;
				}
				roadArray[roadIndex] = "bankDownward";
				roadIndex++;
			}

			if (roadIndex > 10) {
				while(roadIndex<18){
					roadArray[roadIndex] = " ";
					roadIndex++;
				}
				break;      // it's not necessary to fill the array with roads at the start.
			}
		}
	}

	private void updateRoads(){
		// this method add new roads to the game.
		String st;
		for (int i = 0; i < 17; i++) {
			st = roadArray[i+1];
			roadArray[i] = st;
		}
		roadArray[17] = " ";
		if(roadArray[11].equals(" ")){
			int roadIndex = 11;
			while (true){       // if adding roads is not successful, it makes another attempt till success.
				String road = roadTypes[(int)(Math.random()*5)];
				if (road.equals("grass") && !roadArray[9].equals("grass") && !roadArray[8].equals("grass")) {
					roadArray[roadIndex] = "grass";
					break;
				} else if (road.equals("road4")  && !roadArray[9].equals("solid") && !roadArray[8].equals("solid")) {
					for (int i = 0; i < 4; i++) {
						roadArray[roadIndex] = "solid";
						roadIndex++;
					}
					break;
				} else if (road.equals("road6") && !roadArray[8].equals("solid") && !roadArray[9].equals("solid")) {
					for (int i = 0; i < 6; i++) {
						roadArray[roadIndex] = "solid";
						roadIndex++;
					}
					break;
				} else if (road.equals("river5") && !roadArray[8].equals("bankDownward") && !roadArray[9].equals("bankDownward")) {
					roadArray[roadIndex] = "bankUpward";
					roadIndex++;
					for (int i = 0; i < 3; i++) {
						roadArray[roadIndex] = "water";
						roadIndex++;
					}
					roadArray[roadIndex] = "bankDownward";
					break;
				} else if (road.equals("river7") && !roadArray[8].equals("bankDownward") && !roadArray[9].equals("bankDownward")) {
					roadArray[roadIndex] = "bankUpward";
					roadIndex++;
					for (int i = 0; i < 5; i++) {
						roadArray[roadIndex] = "water";
						roadIndex++;
					}
					roadArray[roadIndex] = "bankDownward";
					break;
				}
			}

			updateSnakes();     // update the objects
			updateRiver();
			updateVehicles();
		}
	}

	private void setObjects(){
		// add the objects to each existing roads.
		int start = 1;
		while (start < 17) {
			if (roadArray[start].equals("solid")) {
				int roadLines;
				int direction = 0;

				if (roadArray[start+5].equals("solid")){      // ensure the structure of the road.
					roadLines = 6;
				} else {
					roadLines = 4;
				}

				for (int i = 0; i < roadLines; i++) {
					int randomShiftX = (int) (Math.random()*70);

					if(i >= roadLines/2){
						direction = 1;      // for the second half of the road the direction changes.
					}

					if ((int) (Math.random() * 2) == 0) {
						for(int j = 0; j< 2; j++){
							vehicles.add(new vehicle(-140 + 665 * j + randomShiftX, start * 70 + i * 70, direction, cars, batch, 0));
						}
					} else{
						for (int j = 0; j < 2; j++) {
							vehicles.add(new vehicle(-210 + 700 * j + randomShiftX, start * 70 + i * 70, direction, trucks, batch, 1));
						}
					}
				}

				start += roadLines;

			} else if (roadArray[start].equals("bankUpward")){
				String[] lines = {"turtle", "shortLog", "longLog"};     // three types of objects' road on water.
				int riverLines;
				if (roadArray[start+6].equals("bankDownward")){      // ensure the structure of the river.
					riverLines = 5;
				} else {
					riverLines = 3;
				}

				for (int i = 0; i < riverLines; i++) {
					int randomShiftX = (int) (Math.random()*70);
					String line = lines[(int) (Math.random() * 3)];         // randomly assign type of the objects' road
					if (line.equals("turtle")) {
						for (int j = 0; j < 6; j++) {
							turtles.add(new turtle(-70 + 210 * j + randomShiftX, start * 70 + i * 70 + 70, turtlePics, batch));
							turtles.add(new turtle(210 * j + randomShiftX, start * 70 + i * 70 + 70, turtlePics, batch));
						}
					} else if (line.equals("shortLog")) {
						for (int j = 0; j < 5; j++) {
							logs.add(new log(-140 + 266 * j + randomShiftX, start * 70 + i * 70 + 70, logPics, batch, 0));
						}
					} else {
						for (int j = 0; j < 2; j++) {
							logs.add(new log(-210 + 700 * j + randomShiftX, start * 70 + i * 70 + 70, logPics, batch, 1));
							crocodiles.add(new crocodile(160 + 700 * j + randomShiftX, start * 70 + i * 70 + 70, crocodilePics, batch));
						}
					}
				}

				start += riverLines + 2;

			} else if (roadArray[start].equals("grass")) {      // set snakes
				snakes.add(new snake(400, 70*start, snakePic, batch));
				snakes.add(new snake(800, 70*start, snakePic, batch));
				start++;
			} else {
				start++;
			}
		}
	}

	private void updateVehicles() {
		// add vehicles to the vehicle object array.
		if (roadArray[11].equals("solid")) {
			int roadLines;

			if (roadArray[16].equals("solid")){      // ensure the structure of the river.
				roadLines = 6;
			} else {
				roadLines = 4;
			}

			int direction = 0;      // indicates the direction of a vehicle

			for (int i = 0; i < roadLines; i++) {
				int randomShiftX = (int) (Math.random()*70);

				if(i >= roadLines/2){
					direction = 1;      // for the second half of the road the direction changes.
				}

				if ((int) (Math.random() * 2) == 0) {
					for(int j = 0; j< 2; j++){
						vehicles.add(new vehicle(-140 + 665 * j + randomShiftX, 11 * 70 + i * 70, direction, cars, batch, 0));
					}
				} else{
					for (int j = 0; j < 2; j++) {
						vehicles.add(new vehicle(-210 + 700 * j + randomShiftX, 11 * 70 + i * 70, direction, trucks, batch, 1));
					}
				}
			}
		}
	}

	private void updateRiver(){
		// add objects to turtle, log, crocodile array lists.
		if (roadArray[11].equals("bankUpward")){
			String[] lines = {"turtle", "shortLog", "longLog"};     // 3 types of water road
			int riverLines;
			if (roadArray[17].equals("bankDownward")){      // ensure the structure of the river.
				riverLines = 5;
			} else {
				riverLines = 3;
			}

			for (int i = 0; i < riverLines; i++) {
				int randomShiftX = (int) (Math.random()*70);        // randomly shift the line to right
				String line = lines[(int) (Math.random() * 3)];
				if (line.equals("turtle")) {
					for (int j = 0; j < 6; j++) {
						turtles.add(new turtle(-70 + 210 * j + randomShiftX, 12 * 70 + i * 70, turtlePics, batch));
						turtles.add(new turtle(210 * j + randomShiftX, 12 * 70 + i * 70, turtlePics, batch));
					}
				} else if (line.equals("shortLog")) {
					for (int j = 0; j < 5; j++) {
						logs.add(new log(-120 + 250 * j + randomShiftX, 12 * 70 + i * 70, logPics, batch, 0));
					}
				} else {
					for (int j = 0; j < 2; j++) {
						logs.add(new log(-210 + 700 * j + randomShiftX, 12 * 70 + i * 70, logPics, batch, 1));
						crocodiles.add(new crocodile(160 + 700 * j + randomShiftX, 12 * 70 + i * 70, crocodilePics, batch));
					}
				}
			}
		}
	}

	private void updateSnakes() {
		// it adds objects to snake array list
		if (roadArray[11].equals("grass")) {
			int randomShiftX = (int) (Math.random()*70);        // randomly shift the snakes
			snakes.add(new snake(400 + randomShiftX, 770, snakePic, batch));
			randomShiftX = (int) (Math.random()*70);
			snakes.add(new snake(800 + randomShiftX, 770, snakePic, batch));
		}
	}

	private void drawRoads(){
		// drawing the graphs
		for (int i = 0; i < 11; i++) {
			if (roadArray[i].equals("solid")) {
				for (int j = 0; j < 17; j++) {
					roads[0].setPosition(j*70, i*70-shiftValue);    // since frog goes up, the roads shift down.
					roads[0].draw(batch);
				}
			} else if(roadArray[i].equals("grass")){
				for (int j = 0; j < 17; j++) {
					roads[1].setPosition(j*70, i*70-shiftValue);
					roads[1].draw(batch);
				}
			} else if(roadArray[i].equals("water")){
				for (int j = 0; j < 17; j++) {
					roads[2].setPosition(j*70, i*70-shiftValue);
					roads[2].draw(batch);
				}
			} else if(roadArray[i].equals("bankUpward")){
				for (int j = 0; j < 17; j+=2) {     // since bank is made of two pics, the structure is different.
					banks[0].setPosition(j*70, i*70-shiftValue);
					banks[1].setPosition(j*70+70, i*70-shiftValue);
					banks[0].draw(batch);
					banks[1].draw(batch);
				}
			} else if(roadArray[i].equals("bankDownward")){
				for (int j = 0; j < 17; j+=2) {
					banks[2].setPosition(j*70, i*70-shiftValue);
					banks[3].setPosition(j*70+70, i*70-shiftValue);
					banks[2].draw(batch);
					banks[3].draw(batch);
				}
			}
		}
	}

	private void callObjects(){
		// call all the objects while the game is ongoing
		for (int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).moveDown(shiftValue);           // for screen shifting
			vehicles.get(i).drawAndUpdate();
			frog.collideWith(vehicles.get(i).getRect(), true, 0);  // check if the frog collides it.
			if (vehicles.get(i).yValue() < 0) {
				vehicles.remove(vehicles.get(i));       //remove the off screen objects
			}
		}

		for (int i = 0; i < snakes.size(); i++) {
			snakes.get(i).moveDown(shiftValue);
			snakes.get(i).drawAndUpdate();
			frog.collideWith(snakes.get(i).getRect(), true, 0);
			if (snakes.get(i).yValue() <= -70) {
				snakes.remove(snakes.get(i));
			}
		}

		for (int i = 0; i < turtles.size(); i++) {
			turtles.get(i).moveDown(shiftValue);
			turtles.get(i).drawAndUpdate();
			frog.collideWith(turtles.get(i).getRect(), turtles.get(i).dangerous(), turtles.get(i).movingSpeed());
			if (turtles.get(i).yValue() < -70) {
				turtles.remove(turtles.get(i));
			}
		}

		for (int i = 0; i < logs.size(); i++) {
			logs.get(i).moveDown(shiftValue);
			logs.get(i).drawAndUpdate();
			frog.collideWith(logs.get(i).getRect(), false, logs.get(i).movingSpeed());
			if (logs.get(i).yValue() <= -70) {
				logs.remove(logs.get(i));
			}
		}

		for (int i = 0; i < crocodiles.size(); i++) {
			crocodiles.get(i).moveDown(shiftValue);
			crocodiles.get(i).drawAndUpdate();
			frog.collideWith(crocodiles.get(i).getRect(), true, 0);
			if (crocodiles.get(i).yValue() <= -70) {
				crocodiles.remove(crocodiles.get(i));
			}
		}

	}

	@Override
	public void create() {
		// prepare for actual gaming
		Gdx.graphics.setWindowedMode(1190, 700);		// reset size.
		tools = new Tools(700);
		batch = new SpriteBatch();
		m = new Menu();

		loadGraphics();

		batch = new SpriteBatch();
		loadGraphics();
		frog = new Frogger(560, 0, frogs, batch);
		shiftValue = 0;

		mx = tools.getMouseX();
		my = tools.getMouseY();
		score = 0;
		setRoads();
		setObjects();

		font = new BitmapFont();
		font.setColor(new Color(0xff0000ff));

		sound = Gdx.audio.newSound(Gdx.files.internal("Game.mp3"));
		sound.loop();

		timeStart = System.currentTimeMillis();     // this is to ensure the fluency of the program.
	}

	@Override
	public void render() {
		if(IntroEnd){
			timeNow = System.currentTimeMillis();		// check time passed
			if (timeNow - timeStart > 20) {        // only after 10 ms the graph will refresh.
				timeStart = timeNow;
				mx = tools.getMouseX();
				my = tools.getMouseY();
			}

			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			if(frog.isAlive()) {
				if (frog.atRest()) {              // only if the frog is at rest it can take a new move
					if (tools.pressedWK()) {
						frog.jump("up");
					} else if (tools.pressedSK()) {
						frog.jump("down");
					} else if (tools.pressedAK()) {
						frog.jump("left");
					} else if (tools.pressedDK()) {
						frog.jump("right");
					}
				}

				for (int i = 0; i < 5; i++) {		// check if th frogger fell into water; if so, he dies
					if(roadArray[i].equals("water")){
						Rectangle rect = new Rectangle(0, i * 70 - shiftValue, 1190, 70);
						frog.collideWith(rect, true, 0);
					}
				}

				// drawing and updating
				batch.begin();

				drawRoads();

				callObjects();

				font.draw(batch, "Score:" + score, 0, Gdx.graphics.getHeight());		// show score

				int a = frog.displayFrogger();		// a is the changing on shift value
				batch.end();
				shiftValue += a;

				if (a == 0) {
					if (shiftValue == 70) {         // indicates a movement was mad.
						for (int i = 0; i < vehicles.size(); i++) {     // move down objects when the screen moves down
							vehicles.get(i).apdateY();
						}

						for (int i = 0; i < snakes.size(); i++) {
							snakes.get(i).apdateY();
						}

						for (int i = 0; i < turtles.size(); i++) {
							turtles.get(i).apdateY();
						}

						for (int i = 0; i < logs.size(); i++) {
							logs.get(i).apdateY();
						}

						for (int i = 0; i < crocodiles.size(); i++) {
							crocodiles.get(i).apdateY();
						}

						score++;        // start counting score after
						updateRoads();      // update the gaming data.
					}

					shiftValue = 0;     // if no downward change to screen, it should be 0.
				}
			} else  {           // if frogger died, the player should restart the game.
				u.setScore(score);  // set new score
				m.saveUser();

				batch.begin();
				Sprite sadface = tools.loadSprite("pictures/bigSad.png", 400, 400);
				sadface.setPosition(395,150);
				sadface.draw(batch);
				restart.setPosition(545,300);
				restart.draw(batch);

				batch.end();

				if (tools.mouseleftClick() && tools.mouseIsInRange(restart)) {       // after clicking, everything restarts
					// erase old things
					for (int i = 0; i < roadArray.length; i++) {
						roadArray[i] = " ";
					}
					frog = new Frogger(560, 0, frogs, batch);
					shiftValue = 0;
					score = 0;
					turtles.clear();
					crocodiles.clear();
					snakes.clear();
					vehicles.clear();
					logs.clear();

					// reconstruct
					setRoads();
					setObjects();
				}
			}
		} else {
			font.setColor(new Color(0, 0, 0, 1));
			batch.begin();
			background.draw(batch);
			// show username only if the user logged in
			if (!username.equals("")) {
				font.draw(batch, "Username:" + username, 0,
						Gdx.graphics.getHeight());
			}
			// In the page of introduction
			if (helpFlag) {
				back.draw(batch);
				String wrd = m.showIntro();
				font.getData().setScale(3f);

				// title
				font.draw(batch, "A little help from authors", 350, Gdx.graphics.getHeight() - 80);
				font.getData().setScale(2f);
				font.setColor(new Color(0xbfbfbfff));

				// line feeds
				for (int i = 0; i <= wrd.length() / 70; i++) {
					String s;
					if (i == wrd.length() / 70) {
						s = wrd.substring(i * 70);
					} else {
						s = wrd.substring(i * 70, (70 + i * 70));
					}
					font.draw(batch, s, 140, Gdx.graphics.getHeight() - 150 - i * 40);
				}

				// return to the main page
				if (tools.mouseleftClick() && tools.mouseIsInRange(back)) {
					helpFlag = false;
				}
			} else { 		// inside the main page
				// give login if no username
				if (username.equals("")) {
					login.draw(batch);
				} else { 		// or start the game
					start.draw(batch);
				}

				// give the option to the help page
				help.draw(batch);

				// mouse left click
				if (tools.mouseleftClick()) {

					// click the login, output the window only when no username
					if (tools.mouseIsInRange(login) && username.equals("")) {
						Gdx.input.getTextInput(this, "Login", "", "username");
					}

					//click the help button and change to help page
					if (tools.mouseIsInRange(help)) {
						helpFlag = true;
					}

					//click play then start the real game
					if (tools.mouseIsInRange(start) && !username.equals("")) {
						IntroEnd = true;
					}
				}
			}
			batch.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void input(String text) {
		//see if the user existed before, if not, create a new one
		username = text;
		boolean flag = true;
		for (int i = 0; i < m.getUsers().size(); i++) {
			if (m.getUsers().get(i).getUsername().equals(username)) {
				u = m.getUsers().get(i);
				score = m.getUsers().get(i).getScore();
				flag = false;
				break;
			}
		}
		if (flag) {
			m.getUsers().add(new User(username));
			score = 0;
		}
		m.saveUser();
	}

	@Override
	public void canceled() {
		username = "";
	}

}

class turtle{
	// this is a turtle object that swims oppositely to the water direction
	private int x, y;		// x, y location
	private int lefBound, rightBound;   // the bounds will indicate when the log disappear or reoccur.
	private Sprite[] sprites;
	private SpriteBatch batch;
	private double spriteIndex;
	private int downwardShift;

	public turtle(int x, int y, Sprite[] sprites, SpriteBatch batch){
		this.x = x;
		this.y = y;
		lefBound = -70;
		rightBound = 1190;
		this.sprites = sprites;
		spriteIndex = (int) (Math.random()*12);			// randomly set current sprite
		this.batch = batch;
		downwardShift = 0;
	}

	public void drawAndUpdate() {
		// this method draws on the batch and also update the value of the object.
		sprites[(int) spriteIndex].setPosition(x, y+downwardShift);
		sprites[(int) spriteIndex].draw(batch);

		if (x > lefBound) {
			x -= 1;			// turtle's speed is 1 pixel/refresh.
		} else {
			x = rightBound;
		}

		if (spriteIndex < 12) {		// update the sprite
			spriteIndex += 0.04;
		} else {
			spriteIndex = 0;
		}
	}

	public boolean dangerous() {
		// this indicates if the frog can stand on the turtle.
		if (spriteIndex < 10) {
			return false;
		}

		return true;
	}

	public void moveDown(int shiftValue){
		// when frog is jumping up, everything moves down
		downwardShift = -1*shiftValue;
	}

	public void apdateY() {
		// change v value
		y -= 70;
	}

	public int yValue() {
		return y + downwardShift;
	}

	public int movingSpeed() {
		// returns the turtle's moving speed which can change forgger's position.
		return -1;
	}

	public Rectangle getRect() {
		// get the turtle's rect
		sprites[(int) spriteIndex].setPosition(x, y + downwardShift);
		return sprites[(int) spriteIndex].getBoundingRectangle();
	}
}

class crocodile{
	// this is a crocodile object that swimming in the river and kills frog.
	private int x, y;
	private int lefBound, rightBound;   // the bounds will indicate when the log disappear or reoccur.
	private Sprite sprite1, sprite2;
	private SpriteBatch batch;
	private int downwardShift;
	private int changeSpriteInt;        // an int that records time and change crocodile's sprite

	public crocodile(int x, int y, Sprite[] sprites, SpriteBatch batch){
		this.x = x;
		this.y = y;
		sprite1 = sprites[0];
		sprite2 = sprites[1];
		this.batch = batch;
		lefBound = -210;
		rightBound = 1190;
		downwardShift = 0;
		changeSpriteInt = (int)(Math.random()*150);		// random initial sprite
	}

	public void drawAndUpdate() {
		// this method draws on the batch and also update the value of the object.
		if (x < rightBound) {
			x += 5;
		} else {
			x = lefBound;
		}

		// draw sprite based on changeSpriteInt and location
		if (changeSpriteInt < 100) {
			sprite1.setPosition(x, y + downwardShift);
			sprite1.draw(batch);
			changeSpriteInt++;
		} else {
			sprite2.setPosition(x, y + downwardShift);
			sprite2.draw(batch);
			if (changeSpriteInt < 150) {
				changeSpriteInt++;
			} else {
				changeSpriteInt = 0;
			}
		}
	}

	public void moveDown(int shiftValue){
		// when frog is jumping up, everything moves down
		downwardShift = -1*shiftValue;
	}

	public void apdateY() {
		// change v value
		y -= 70;
	}

	public int yValue() {
		return y + downwardShift;
	}

	public Rectangle getRect() {
		// get the crocodile's rect
		sprite1.setPosition(x, y + downwardShift);
		return sprite1.getBoundingRectangle();
	}
}

class snake{
	// this is a snake that runs on the grass and kills frog.
	private int x, y;
	private boolean goRight;        // direction the snake is going.
	private int lefBound, rightBound;   // the bounds will indicate when the log disappear or reoccur.
	private Sprite sprite1, sprite2;
	private SpriteBatch batch;
	private int downwardShift;

	public snake(int x, int y, Sprite sprite, SpriteBatch batch){
		this.x = x;
		this.y = y;
		sprite2 = new Sprite(sprite);		// two sprites are for different directions.
		sprite1 = new Sprite(sprite);
		sprite1.flip(true, false);
		this.batch = batch;
		lefBound = -180;
		rightBound = 1190;

		if ((int) (Math.random()*2) == 0) {
			goRight = true;
		} else {
			goRight = false;
		}

		downwardShift = 0;
	}

	public void drawAndUpdate() {
		// this method draws on the batch and also update the value of the object.
		if(goRight){
			if (x < rightBound) {
				x += 3;
			} else {
				goRight = false;
			}
			sprite1.setPosition(x, y + downwardShift);
			sprite1.draw(batch);
		} else {
			if (x > lefBound) {
				x -= 5;
			} else {
				goRight = true;
			}
			sprite2.setPosition(x, y + downwardShift);
			sprite2.draw(batch);
		}
	}

	public void moveDown(int shiftValue){
		// when frog is jumping up, everything moves down
		downwardShift = -1*shiftValue;
	}

	public void apdateY() {
		// change v value
		y -= 70;
	}

	public int yValue() {
		return y + downwardShift;
	}

	public Rectangle getRect(){
		// get the snake's rect
		sprite1.setPosition(x, y + downwardShift);
		return sprite1.getBoundingRectangle();
	}
}

class log{
	// this is the log that the frog can stand on.
	private int x, y;
	private int lefBound, rightBound;   // the bounds will indicate when the log disappear or reoccur.
	private Sprite sprite;
	private SpriteBatch batch;
	private int type;                   // type 0 is short log, 1 is long log.
	private int downwardShift;

	public log(int x, int y, Sprite[] sprites, SpriteBatch batch, int type){
		this.x = x;
		this.y = y;
		this.batch = batch;
		this.type = type;

		if (type == 0) {            // assign diff values based on type.
			sprite = sprites[0];
			lefBound = -140;
		} else {
			sprite = sprites[1];
			lefBound = -210;
		}

		rightBound = 1190;
		downwardShift = 0;
	}

	public void drawAndUpdate() {
		// this method draws on the batch and also update the value of the object.
		sprite.setPosition(x, y + downwardShift);
		sprite.draw(batch);
		if (x < rightBound) {
			if (type == 0) {    // long log goes faster than short one.
				x += 3;
			} else {
				x += 5;
			}
		} else {
			x = lefBound;
		}
	}

	public int movingSpeed() {
		// returns the log's moving speed which can change forgger's position.
		if (type == 0) {
			return 3;
		}
		return 5;
	}

	public void moveDown(int shiftValue){
		// when frog is jumping up, everything moves down
		downwardShift = -1*shiftValue;
	}

	public void apdateY() {
		// change v value
		y -= 70;
	}

	public int yValue() {
		return y + downwardShift;
	}

	public Rectangle getRect() {
		// get the log's rect
		sprite.setPosition(x, y + downwardShift);
		return sprite.getBoundingRectangle();
	}
}

class vehicle{
	// this is the vehicle that runs on the road.
	private int x, y;
	private int directionInt;       // 0 is left to right and 1 is right to left.
	private int lefBound, rightBound;   // the bounds will indicate when the vehicle disappear or reoccur.
	private Sprite sprite;
	private SpriteBatch batch;
	private int speed;      // speed is depending on the type of vehicle. car is faster.
	private int downwardShift;

	public vehicle(int x, int y, int directionInt, Sprite[] sprites, SpriteBatch batch, int type) {
		// type 0 is car, type 1 is truck
		this.x = x;
		this.y = y;
		this.directionInt = directionInt;
		this.batch = batch;
		if (type == 0) {
			if(directionInt == 0){
				sprite = sprites[(int) (Math.random() * 3)];		// 3 types of car
			}  else {
				sprite = sprites[(int) (Math.random() * 3) + 3];
			}
			lefBound = -140;
			speed = 8;
		} else {
			if(directionInt == 0){
				sprite = sprites[(int) (Math.random() * 2)];		// 2 types of truck
			}  else {
				sprite = sprites[(int) (Math.random() * 2) + 2];
			}
			lefBound = -210;
			speed = 6;
		}
		rightBound = 1190;
		downwardShift = 0;
	}

	public void drawAndUpdate() {
		// this method draws on the batch and also update the value of the object.
		sprite.setPosition(x, y+downwardShift);
		sprite.draw(batch);

		if(directionInt==0){
			if (x < rightBound) {
				x += speed;
			} else {
				x = lefBound;
			}
		} else {
			if (x > lefBound) {
				x -= speed;
			} else {
				x = rightBound;
			}
		}
	}

	public void moveDown(int shiftValue){
		// when frog is jumping up, everything moves down
		downwardShift = -1*shiftValue;
	}

	public void apdateY() {
		// change v value
		y -= 70;
	}

	public int yValue() {
		return y+downwardShift;
	}

	public Rectangle getRect() {
		// get the vehicle's rect
		sprite.setPosition(x, y+downwardShift);
		return sprite.getBoundingRectangle();
	}
}

class Frogger{
	private int frogX, frogY;
	private boolean frogAlive;
	private Sprite[] frogs;
	private int spriteIndex;
	private String direction;
	private SpriteBatch batch;

	public Frogger(int fx, int fy, Sprite[] frogs, SpriteBatch batch) {
		frogX = fx;
		frogY = fy;
		frogAlive = true;
		this.frogs = frogs;
		direction = "up";
		this.batch = batch;
	}

	public int displayFrogger() {
		// this draws frog based on its location and moving direction.
		// it also outputs how the roads should shift.
		int ans = 0;

		if (frogAlive) {
			frogs[spriteIndex].setPosition(frogX, frogY);

			if (direction.equals("left")) {                 // rotate the frogger.
				frogs[spriteIndex].setRotation((float)90);
			} else if (direction.equals("right")) {
				frogs[spriteIndex].setRotation((float)-90);
			} else if (direction.equals("down")) {
				frogs[spriteIndex].setRotation((float)180);
			} else {
				frogs[spriteIndex].setRotation((float)0);
			}

			frogs[spriteIndex].draw(batch);

			if(spriteIndex>0 && spriteIndex<6){     // check if the frog is moving and if the index is in proper range.
				ans = shiftPosition();
				if(!direction.equals("up")){
					ans = 0;
				}
				spriteIndex++;
			}

		} else {
			frogs[6].setPosition(frogX, frogY);
			frogs[6].draw(batch);
		}

		if (spriteIndex == 6) {     // when the sprite
			spriteIndex = 0;
		}

		return ans;
	}

	public void jump(String direction) {
		// it leads the the shift of the frogger.
		this.direction = direction;
		spriteIndex++;
	}

	private int shiftPosition() {
		// it is to shift the frog's position in the graph. If the frog is in the 5th row, it returns int to let the screen shift.
		if (direction.equals("up")) {
			if(frogY>=280){         // check if the frog has reached the 5th row.
				return 14;
			}
			frogY += 14;
		} else if (direction.equals("left")) {
			if (frogX > 0) {
				frogX -= 14;
			}
		} else if (direction.equals("right")) {
			if (frogX < 1120) {
				frogX += 14;
			}
		} else if (direction.equals("down")) {
			if (frogY > 0) {
				frogY -= 14;
			}
		}

		return 0;           // in other situations only the frog shift.
	}

	public void collideWith(Rectangle rect, boolean dangerous, int movingGroundValue){
		// check if frogger collides with something. If the thing is dangerous, frogger dies.
		// movingGroundValue is the moving speed of something such as log, which effects frogger's position.
		if (rect.contains((float) (frogX+35), (float) (frogY+35))) {
			if (dangerous) {
				frogAlive = false;
			} else {
				frogAlive = true;
				frogX += movingGroundValue;                 // frogger is carried away be something if value is not 0
				if (frogX >= 1190 || frogX <= -70) {        // if frogger is out of screen, frogger dies.
					frogAlive = false;
				}
			}
		}
	}

	public boolean atRest() {
		// return true if the frog is not moving.
		if (spriteIndex == 0) {     // the frogger uses 1st pic while at rest.
			return true;
		}
		return false;
	}

	public boolean isAlive() {
		// return if frogger is alive
		return frogAlive;
	}
}