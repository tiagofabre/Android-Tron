package com.kilobolt.robotgame;

import com.kilobolt.robotgame.GameScreen.GameState;

public class SnakeIA extends Snake{

	int randomNum;
	public final int stepsToThink = 5;
	private int currentStep;
	public void Think()
	{
		currentStep++;
		if(currentStep > stepsToThink)
		{
			RandomDirection();
		}
		
		
		//verifica se vai para fora da tela
		if(currentPositionX+1 > 50
				&& direction == DirectionSnake.RIGHT)//direita
		{
			if(currentPositionY == 0)
				direction = DirectionSnake.DOWN;
			else if(currentPositionY == 30)
				direction = DirectionSnake.UP;
			else
			{
				centerBorder();
				return;
			}
			move();
			return;
		}
		
		else if(currentPositionX-1 < 0 
				&& direction == DirectionSnake.LEFT)//esquerda
		{
			if(currentPositionY == 0)
				direction = DirectionSnake.DOWN;
			else if(currentPositionY == 30)
				direction = DirectionSnake.UP;
			else
			{
				centerBorder();
				return;
			}
			move();
			return;
		}
		
		else if(currentPositionY-1 < 0
				&& direction == DirectionSnake.UP)//cima
		{
			if(currentPositionX == 50)
				direction = DirectionSnake.LEFT;
			else if(currentPositionX == 0)
				direction = DirectionSnake.RIGHT;
			else
			{
				centerBorder();
				return;
			}
			move();
			return;
		}
		
		else if(currentPositionY > 30 
				&& direction == DirectionSnake.DOWN)//baixo
		{
			if(currentPositionX == 50)
				direction = DirectionSnake.LEFT;
			else if(currentPositionX == 0)
				direction = DirectionSnake.RIGHT;
			else
			{
				centerBorder();
				return;
			}
			move();
			return;
		}

		// verifica se vai se comer
		if(GameScreen.instance.screen[currentPositionX+1][currentPositionY]  == idSnake
				&& direction == DirectionSnake.RIGHT)//direita
		{
			DecisionUpDown();
		}
		
		else if(GameScreen.instance.screen[currentPositionX-1][currentPositionY]  == idSnake
				&& direction == DirectionSnake.LEFT)//esquerda
		{
			DecisionUpDown();
		}
		
		else if(GameScreen.instance.screen[currentPositionX][currentPositionY-1]  == idSnake
				&& direction == DirectionSnake.UP)//cima
		{
			DecisionLeftRight();
		}
		
		else if(GameScreen.instance.screen[currentPositionX][currentPositionY+1]  == idSnake
				&& direction == DirectionSnake.DOWN)//baixo
		{
			DecisionLeftRight();
		}
		
		
		
	
		move();
	}
	
	private void RandomDirection()
	{
		randomNum = 0 + (int)(Math.random()*5); 
		currentStep = randomNum;
		
		if(randomNum%5 == 0)
		{
			if(direction != DirectionSnake.LEFT)
				direction = DirectionSnake.RIGHT;
			else
				RandomDirection();
		}
		else if(randomNum%4 == 0)
		{
			if(direction != DirectionSnake.RIGHT)
				direction = DirectionSnake.LEFT;
			else
				RandomDirection();
		}
		else if(randomNum%3 == 0)
		{
			if(direction != DirectionSnake.UP)
				direction = DirectionSnake.DOWN;
			else
				RandomDirection();
		}
		else if (randomNum%2 == 0) {
		
			if(direction != DirectionSnake.DOWN)
				direction = DirectionSnake.UP;
			else
				RandomDirection();
		}
	}
	
	private void centerBorder()
	{
		if(GameScreen.instance.enemy.x[0]+velocity > 750
				&& direction == DirectionSnake.RIGHT)//direita
		{
			DecisionUpDown();
		}
		
		else if(GameScreen.instance.enemy.x[0]-velocity < 0 
				&& direction == DirectionSnake.LEFT)//esquerda
		{
			DecisionUpDown();
		}
		
		else if(GameScreen.instance.enemy.y[0]-velocity < 0
				&& direction == DirectionSnake.UP)//cima
		{
			DecisionLeftRight();
		}
		
		else if(GameScreen.instance.enemy.y[0]+velocity > 480 
				&& direction == DirectionSnake.DOWN)//baixo
		{
			DecisionLeftRight();
		}
		
		move();
	}
	
	private void DecisionUpDown()
	{
		randomNum = 0 + (int)(Math.random()*10); 
		if (randomNum%2 ==0) 
		{
			direction = DirectionSnake.UP;
		}
		else
		{
			direction = DirectionSnake.DOWN;
		}
	}
	
	private void DecisionLeftRight()
	{
		randomNum = 0 + (int)(Math.random()*10); 
		
		if (randomNum%2 ==0) 
		{
			direction = DirectionSnake.LEFT;
		}
		else
		{
			direction = DirectionSnake.RIGHT;
		}
	}
	
}
