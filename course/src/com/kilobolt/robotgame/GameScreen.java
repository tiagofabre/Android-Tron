package com.kilobolt.robotgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.graphics.Color;
import android.graphics.Paint;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.Screen;
import com.kilobolt.robotgame.Snake.DirectionSnake;

public class GameScreen extends Screen {
	public static GameScreen instance;
	
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	// Variable Setup

	private static Background bg1, bg2;	//imgem de fundo
	//private static Robot robot;
	public static Heliboy hb, hb2;
	public static Snake snake; 
	public static SnakeIA enemy;
	private final float updateInterval = 1f;
	private float updateTime = 0;
	private Image currentSprite, character, character2, character3, heliboy,
	heliboy2, heliboy3, heliboy4, heliboy5, snakeImg, enemyImg;
	private Animation anim, hanim;
	public int[][] screen;
	
	public int winner;	//identificador do vencedor
	
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();

	int livesLeft = 1;
	Paint paint, paint2 , paint3;

	public GameScreen(Game game) {
		super(game);
		instance = this;
		// Initialize game objects here

		screen = new int[52][33];
		
		for(int i=0; i < screen.length; i++)
		{
			for(int j=0; j < screen[0].length; j++)
			{
				screen[i][j] = 0;
			}
		}
		
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		
		hb = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);

		snake = new Snake();
		snake.idSnake = 1;
		snake.SnakePosition(5, 5);

		snake.human = true;
		
		enemy = new SnakeIA();
		enemy.idSnake = 2;
		enemy.SnakePosition(46, 15);

		enemy.direction = DirectionSnake.LEFT;
		enemy.moveLeft();
		enemy.human = false;
		
		snakeImg = Assets.snakeImg;
		enemyImg = Assets.snakeImg2;
		character = Assets.character;
		character2 = Assets.character2;
		character3 = Assets.character3;

		heliboy = Assets.heliboy;
		heliboy2 = Assets.heliboy2;
		heliboy3 = Assets.heliboy3;
		heliboy4 = Assets.heliboy4;
		heliboy5 = Assets.heliboy5;

		anim = new Animation();
		hanim = new Animation();

		loadMap();

		// Defining a paint object
		paint = new Paint();
		paint2 = new Paint();
		paint3 = new Paint();
	}

	private void loadMap() {
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0;

		Scanner scanner = new Scanner(SampleGame.map);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			// no more lines to read
			if (line == null) {
				break;
			}

			if (!line.startsWith("!")) {
				lines.add(line);
				width = Math.max(width, line.length());

			}
		}
		height = lines.size();

		for (int j = 0; j < 12; j++) {
			String line = (String) lines.get(j);
			for (int i = 0; i < width; i++) {

				if (i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, Character.getNumericValue(ch));
					tilearray.add(t);
				}
			}
		}
	}

	public void setPositionSnake(int snakeId, int x, int y)
	{
		if(x > 50 || y > 31 || x < 0 || y < 0) //Saiu do cenário
		{
			state = GameState.GameOver;
			return;
		}
		
		if(screen[x][y] == 0)//esta vazio
		{
			screen[x][y] = snakeId;
		}
		else if(screen[x][y] == snakeId)//batem nela mesma
		{
			state = GameState.GameOver;
			winner = snakeId;
		}
		else if(screen[x][y] != 0) //bateu em outra coisa
		{
			state = GameState.GameOver;
			winner = snakeId;
		}
	}
	

	//METODO QUE GERENCIA QUAL UPDATE DEVE SER CHAMADO COMO POR EXEMPLO O DO INGAME
	// O DO GAME OVER, PAUSE OU TELA INICIAL
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		// We have four separate update methods in this example.
		// Depending on the state of the game, we call different update methods.
		// Refer to Unit 3's code. We did a similar thing without separating the
		// update methods.

		if (state == GameState.Ready)
			updateReady(touchEvents);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents) {

		// This example starts with a "Ready" screen.
		// When the user touches the screen, the game begins.
		// state now becomes GameState.Running.
		// Now the updateRunning() method will be called!

		if (touchEvents.size() > 0)
			state = GameState.Running;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

		updateTime += deltaTime*0.1f;
		if(updateTime >= updateInterval)
		{
			snake.move();
			enemy.Think();
			updateTime = 0;
		}
		
		//1. All touch input is handled here:
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) 
		{
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) 
			{
				if (inBounds(event, 65, 285, 65, 65)) {
					snake.moveUp();
				}

				else if (inBounds(event, 0, 350, 65, 65)) {
					snake.moveLeft();
				}

				else if (inBounds(event, 65, 415, 65, 65)){
					snake.moveDown();

				}
				else if (inBounds(event, 130, 350, 65, 65)){

					snake.moveRight();
				}
			}
		}

		updateTiles();
		hb.update();
		hb2.update();
		bg1.update();
		bg2.update();
		animate();

	}
	//METODO QUE VERIFICA SE A POSIÇÃO DO EVENTO PASSADO ESTA CONTINDO NO OBJETO
	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	// METODO QUE FICA ESCUTANDO OS EVENTOS DA TELA DE PAUSE
	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 800, 240)) {

					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}

				if (inBounds(event, 0, 240, 800, 240)) {
					nullify();
					goToMenu();
				}
			}
		}
	}
	//METODO QUE FICA ESCUTANDO OS EVENTOS DA TELA DE GAME OVER
	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 0, 0, 1280, 720)) {
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}

	}

	private void updateTiles() {

		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}

	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		for(int i=0; i < screen.length;i++)
		{
			for(int j=0; j < screen[0].length; j++)
			{
				if(screen[i][j] == 1)
				{
					g.drawImage(snakeImg, i*15 , j*15);
				}
				if(screen[i][j] == 2)
				{
					g.drawImage(enemyImg, i*15 , j*15);
				}
			}
		}
		
		
		for(int i=0; i <= screen.length+1; i++)
		{
			if(i == 0 || i == screen.length)
				g.drawLine(15*i, 0, 15*i, 1280, Color.RED);
		}
		for(int i=0; i < screen[0].length+1; i++)
		{
			if(i == screen[0].length)
				g.drawLine(0, 15*i-1, 1280, 15*i, Color.RED);
		}
		
		g.drawLine(0, 0, 1280, 0, Color.RED);
		
		
		// Secondly, draw the UI above the game elements.
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();

	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			if (t.type != 0) {
				g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
			}
		}
	}

	public void animate() {
		anim.update(10);
		hanim.update(50);
	}

	private void nullify() {

		// Set all variables to null. You will be recreating them in the
		// constructor.
		paint = null;
		bg1 = null;
		bg2 = null;
		
		hb = null;
		hb2 = null;
		currentSprite = null;
		character = null;
		character2 = null;
		character3 = null;
		heliboy = null;
		heliboy2 = null;
		heliboy3 = null;
		heliboy4 = null;
		heliboy5 = null;
		anim = null;
		hanim = null;

		// Call garbage collector to clean up memory.
		System.gc();

	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap to Start.", 400, 240, paint);

	}
	//ADICIONA A IMAGEM DOS BOTOES IN GAME NA TELA (UTILIZA ATLAS)
	Graphics gDown;
	Graphics gLeft;
	Graphics gRight;
	Graphics gUp;

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		gDown  = game.getGraphics();
		gLeft  = game.getGraphics();
		gRight = game.getGraphics();
		gUp    = game.getGraphics();

		gDown.drawImage(Assets.button, 65, 285, 0, 0, 65, 65);//baixo
		gLeft.drawImage(Assets.button, 0, 350, 0, 65, 65, 65);//esquerda
		gRight.drawImage(Assets.button, 130, 350, 0, 65, 65, 65);//direita
		gUp.drawImage(Assets.button, 65, 415, 0, 130, 65, 65);//cima
	}

	//MOSTRA OS BOTOES DA TELA DE PAUSE
	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Resume", 400, 165, paint2);
		g.drawString("Menu", 400, 360, paint2);

	}
	//MOSTRA OS BOTOES DA TELA DE GAMEOVER
	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.WHITE);
		g.drawString("GAME OVER.", 400, 240, paint2);
		String gameoverString;
		if(winner == 1)
			gameoverString = "VOCÊ PERDEU";
		else
			gameoverString = "VOCÊ GANHOU";
		g.drawString(gameoverString, 400, 290, paint3);
		
		g.drawString("Tap to return.", 400, 320, paint);
	}

	//MUDA ESTADOS DA QUE VAO GERENCIAR QUAL TELA ESTA SELECIONADA
	@Override
	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;
	}

	@Override
	public void resume() {
		if (state == GameState.Paused)
			state = GameState.Running;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		pause();
	}

	private void goToMenu() {
		// TODO Auto-generated method stub
		game.setScreen(new MainMenuScreen(game));

	}

	public static Background getBg1() {
		// TODO Auto-generated method stub
		return bg1;
	}

	public static Background getBg2() {
		// TODO Auto-generated method stub
		return bg2;
	}



}