package com.kilobolt.robotgame;

import com.kilobolt.robotgame.GameScreen.GameState;

public class SnakeIA extends Snake{

	int randomNum;
	public final int stepsToThink = 10;
	private int currentStep;
	private boolean thinked;
	
	public void Think()
	{
		currentStep++;
		thinked = false;
		//verifica se vai para fora da tela
		if(currentPositionX+1 > 50 && direction == DirectionSnake.RIGHT)//direita
		{
			if(currentPositionY == 0)
				direction = DirectionSnake.DOWN;
			else if(currentPositionY == 31)
				direction = DirectionSnake.UP;
			else
			{
				centerBorder();
				
				return;
			}
			move();
			return;
		}
		
		else if(currentPositionX-1 < 0 && direction == DirectionSnake.LEFT)//esquerda
		{
			if(currentPositionY == 0)
				direction = DirectionSnake.DOWN;
			else if(currentPositionY == 31)
				direction = DirectionSnake.UP;
			else
			{
				centerBorder();
				
				return;
			}
			move();
			return;
		}
		
		else if(currentPositionY-1 < 0 && direction == DirectionSnake.UP)//cima
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
		
		else if(currentPositionY+1 > 31 && direction == DirectionSnake.DOWN)//baixo
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
		if(direction == DirectionSnake.RIGHT)//direita
		{
			if(GameScreen.instance.screen[currentPositionX+1][currentPositionY]  == idSnake)
			{
				if(GameScreen.instance.screen[currentPositionX][currentPositionY+1]  == idSnake)
					direction = DirectionSnake.UP;
				else
					direction = DirectionSnake.DOWN;
				
				thinked = true;
			}
		}
		if(direction == DirectionSnake.LEFT)//esquerda
		{
			if(GameScreen.instance.screen[currentPositionX-1][currentPositionY]  == idSnake)
			{
				if(GameScreen.instance.screen[currentPositionX][currentPositionY+1]  == idSnake)
					direction = DirectionSnake.UP;
				else
					direction = DirectionSnake.DOWN;
				
				thinked = true;
			}
		}
		if(direction == DirectionSnake.UP)//cima
		{
			if(GameScreen.instance.screen[currentPositionX][currentPositionY-1]  == idSnake)
			{
				if(GameScreen.instance.screen[currentPositionX+1][currentPositionY]  == idSnake)
					direction = DirectionSnake.LEFT;
				else
					direction = DirectionSnake.RIGHT;
				
				thinked = true;
			}
		}
		if(direction == DirectionSnake.DOWN)//baixo
		{
			if(GameScreen.instance.screen[currentPositionX][currentPositionY+1]  == idSnake)
			{
				if(GameScreen.instance.screen[currentPositionX+1][currentPositionY]  == idSnake)
					direction = DirectionSnake.LEFT;
				else
					direction = DirectionSnake.RIGHT;
				
				thinked = true;
			}
		}

		move();
		
		if(thinked)
			return;
		
		if(currentStep > stepsToThink)
		{
			RandomDirection();
		}
	}
	
	private void RandomDirection()
	{
		randomNum = 0 + (int)(Math.random()*5); 
		currentStep = randomNum;
		
		if(randomNum%5 == 0)
		{
			if(direction != DirectionSnake.LEFT)
			{
				if(currentPositionX+1 > 50)
				{
					RandomDirection();
					return;
				}
				if(GameScreen.instance.screen[currentPositionX+1][currentPositionY]  == idSnake)
				{
					RandomDirection();
					return;
				}
			
				direction = DirectionSnake.RIGHT;
			}
			else
			{
				RandomDirection();
			}
		}
		else if(randomNum%4 == 0)
		{
			if(direction != DirectionSnake.RIGHT)
			{
				if(currentPositionX-1 < 0)
				{
					RandomDirection();
					return;
				}
				if(GameScreen.instance.screen[currentPositionX-1][currentPositionY]  == idSnake)
				{
					RandomDirection();
					return;
				}
			
				direction = DirectionSnake.LEFT;
			}
			else
				RandomDirection();
		}
		else if(randomNum%3 == 0)
		{
			if(direction != DirectionSnake.UP)
			{
				if(currentPositionY+1 < 0)
				{
					RandomDirection();
					return;
				}
				if(GameScreen.instance.screen[currentPositionX+1][currentPositionY]  == idSnake)
				{
					RandomDirection();
					return;
				}
				
				direction = DirectionSnake.DOWN;
			}
			else
				RandomDirection();
		}
		else if (randomNum%2 == 0) {
		
			if(direction != DirectionSnake.DOWN)
			{
				if(currentPositionX-1 < 0)
				{
					RandomDirection();
					return;
				}
				if(GameScreen.instance.screen[currentPositionX-1][currentPositionY]  == idSnake)
				{
					RandomDirection();
					return;
				}
				direction = DirectionSnake.UP;
			}
			else
				RandomDirection();
		}
	}
	
	private void centerBorder()
	{
		if(currentPositionX+1 > 50
				&& direction == DirectionSnake.RIGHT)//direita
		{
			DecisionUpDown();
		}
		
		else if(currentPositionX-1 < 0 
				&& direction == DirectionSnake.LEFT)//esquerda
		{
			DecisionUpDown();
		}
		
		else if(currentPositionY-1 < 0
				&& direction == DirectionSnake.UP)//cima
		{
			DecisionLeftRight();
		}
		
		else if(currentPositionY+1 > 31 
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
