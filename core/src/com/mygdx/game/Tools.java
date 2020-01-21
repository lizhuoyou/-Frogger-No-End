// this file contains the tool object
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Tools{
	// this object has a series of tools to make the project easier to construct.
	private int graphHeight;            // it is screen height.
	private boolean wIsPressed, sIsPressed, aIsPressed, dIsPressed, mouseLeftClicked;     // record if a key is pressed

	public Tools(int height) {
		graphHeight = height;
		wIsPressed = false;
		sIsPressed = false;
		aIsPressed = false;
		dIsPressed = false;
	}

	public int getMouseX() {
		return Gdx.input.getX();
	}

	public int getMouseY() {
		// it edits the mouse's y value so it starts from the down left.
		return graphHeight - Gdx.input.getY();
	}

	public boolean pressedWK() {
		// check if w key is pressed
		// returns true once the key is pressed down and then leased.
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			wIsPressed = true;
			return false;
		}

		if (wIsPressed) {
			wIsPressed = false;
			return true;
		}

		return false;
	}

	public boolean pressedSK() {
		// check if s key is clicked
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			sIsPressed = true;
			return false;
		}

		if (sIsPressed) {
			sIsPressed = false;
			return true;
		}

		return false;
	}

	public boolean pressedAK() {
		// check if a key is clicked
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			aIsPressed = true;
			return false;
		}

		if (aIsPressed) {
			aIsPressed = false;
			return true;
		}

		return false;
	}

	public boolean pressedDK() {
		// check if d key is clicked
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			dIsPressed = true;
			return false;
		}

		if (dIsPressed) {
			dIsPressed = false;
			return true;
		}

		return false;
	}

	public boolean mouseleftClick() {
		//  check if left mouse key is pressed.
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			mouseLeftClicked = true;
			return false;
		}

		if (mouseLeftClicked) {
			mouseLeftClicked = false;
			return true;
		}

		return false;
	}

	public Sprite loadSprite(String st, float width, float height) {
		// load a single sprite
		Sprite sp = new Sprite(new Texture(st));
		sp.setSize(width, height);
		return sp;
	}

	public Sprite[] loadSprites(String[] stl, float width, float height) {
		// load a sprite array to use.
		int amount = stl.length;
		Sprite[] textures = new Sprite[amount];

		for (int i = 0; i < amount; i++) {
			textures[i] = new Sprite(new Texture(stl[i]));
			textures[i].setSize(width, height);
		}

		return textures;
	}

	public boolean checkSpriteCollision(Sprite sp1, Sprite sp2) {
		// check if two sprites collide.
		Rectangle r1 = sp1.getBoundingRectangle();
		Rectangle r2 = sp2.getBoundingRectangle();
		if (r1.overlaps(r2)) {
			return true;
		}
		return false;
	}

	public boolean mouseIsInRange(Sprite p) {
		// check if mouse is in range.
		if (p.getBoundingRectangle().contains(getMouseX(), getMouseY())) {
			return true;
		}
		return false;
	}
}