package com.kilobolt.robotgame;


import java.util.*;
import java.util.ArrayList;
import android.graphics.Rect;


public class Snake {

	public final float velocity =  15;
	public float lifetime = 0;
	public int x[] = new int[2000];
	public int y[] = new int[2000];
	public int dots = 3;
	public enum DirectionSnake{LEFT,RIGHT,UP,DOWN};
	public DirectionSnake direction = DirectionSnake.RIGHT;
	public boolean human;
	
	public int idSnake;
	protected int currentPositionX;
	protected int currentPositionY;
	
	public void SnakePosition(int xx, int yy)
	{
		GameScreen.instance.screen[xx][yy] = idSnake;
		currentPositionX = xx;
		currentPositionY = yy;
	}


	public void AddLifeTick()
	{
		lifetime++;
	}

	public void move() 
	{
		if (direction == DirectionSnake.LEFT) {
			currentPositionX = currentPositionX-1;
		}

		else if (direction == DirectionSnake.RIGHT) {
			currentPositionX = currentPositionX+1;
		}

		else if (direction == DirectionSnake.UP) {
			currentPositionY = currentPositionY-1;
		}

		else if (direction == DirectionSnake.DOWN) {
			currentPositionY = currentPositionY+1;
		}
		
		GameScreen.instance.setPositionSnake(idSnake, currentPositionX, currentPositionY);
	}

	public void moveRight() {
		if(direction != DirectionSnake.LEFT)
			direction = DirectionSnake.RIGHT;
	}

	public void moveLeft() {
		if(direction != DirectionSnake.RIGHT)
			direction = DirectionSnake.LEFT;
	}

	public void moveUp() {
		if(direction != DirectionSnake.DOWN)
			direction = DirectionSnake.UP;
	}

	public void moveDown() {
		if(direction != DirectionSnake.UP)
			direction = DirectionSnake.DOWN;
	}

}
