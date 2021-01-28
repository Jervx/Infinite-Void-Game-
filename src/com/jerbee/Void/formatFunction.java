package com.jerbee.Void;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class formatFunction implements Initializable{

	@FXML Canvas canvas;
	@FXML Button saveButton;
	Random rand = new Random();

	double floor;
	double rightBound;
	double charX;
	double charY;
	double charHeight = 30;
	double charWidth = 55;
	double damageLevel;
	double fireRate = 0.5;
	int kills = 0;
	int score = 0;

	int HealthBar [] = {1,1,1,1,1};
	int healthptr = HealthBar.length-1;

	boolean debuggingMode = false;

	int counter = 0;
	int allowedFire = 8;
	double canfire = allowedFire;
	GraphicsContext g;

	List <Bullet> bullets = new ArrayList<Bullet>();
	List <particle> particles = new ArrayList<particle>();
	List <enemy1> enemy1s = new ArrayList<enemy1>();
	Character spaceShip = new Character();

	int initEngine = 0;
	String gameStat = "";
	String elimStat = "-";
	String firedStat = "Not Firing";
	int eliminatedRefresh = 0;
	boolean paused = false;

	public void clearCLI() { for(int x = 0; x < 20 ;x++)System.out.println(); }

	public void cliPlay() {
		clearCLI();
		eliminatedRefresh--;
		if(eliminatedRefresh < 0)elimStat="-";
		System.out.printf("Game:%s\nHit Status: %s\nGun Status: %s",gameStat,elimStat,firedStat);
	}

	public void update() {

		if(HealthBar[0] == 0) {
			g.strokeText("YOU DIED PRESS T TO RESTART",canvas.getWidth()/2-100,canvas.getHeight()/2-50);
			return;
		}

		cliPlay();

		g.setFill(Color.LIGHTGRAY);

		if(paused) {
			g.fillText("Game Paused Press P to Resume", canvas.getWidth()/2-125,canvas.getHeight()/2-50);
			return;
		}

		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


		//g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		g.setFill(Color.WHITESMOKE);
		g.setFont(new Font("consolas",16));
		g.fillText(String.format("Kills: %d",kills), 10, 20);

		g.setStroke(Color.rgb(89, 89, 89));
		g.strokeText("Score:"+score,10,20);
		g.strokeText("Health: ",130,20);
		g.strokeText(String.format("Fire Rate: %.3f",fireRate),320,20);

		int t = 195;

		for(int x = 0; x < particles.size();x++) {
			particles.get(x).draw(g,canvas.getWidth());
			if(particles.get(x).getX() <= 0 - particles.get(x).getRadius()) {
				particles.remove(x);
				addSpaceParticles(1);
			}
		}

		g.setFill(Color.rgb(112, 112, 112));
		for(int x = 0; x < bullets.size(); x++) {
			bullets.get(x).draw(g,canvas.getWidth());
			bullets.get(x).setDebugging(debuggingMode);
			if(bullets.get(x).getX() >= canvas.getWidth() + 30)
				bullets.remove(x);
		}

		for(int x = 0; x < enemy1s.size(); x++) {
			enemy1s.get(x).draw(g);
			enemy1s.get(x).setDebugging(debuggingMode);

			double enx = enemy1s.get(x).x , eny = enemy1s.get(x).y;

			if(enemy1s.get(x).x < -30) {
				enemy1s.remove(x);
				addEnemy(1);
			}else if((enx >= charX-74 && enx <= charX+75) && (eny >= charY-35 && eny <= charY+120)) {
				HealthBar[healthptr--] = 0;
				enemy1s.remove(x);
				addEnemy(1);
				g.setStroke(Color.rgb(20,20,20));
			}
		}

		spaceShip.draw(g, charX-35, charY-35);
		if(debuggingMode) {
			g.strokeText("Debuggin view Enabled", canvas.getWidth()-250, 20);
			g.setStroke(Color.rgb(20,20,20));
			g.strokeRect(charX-35,charY-35,charHeight+75,charWidth+105);
			g.strokeOval(charX-35,charY-35,3,3);
			g.strokeOval(charX+75,charY+120,5,5);
		}

		checkCollision();

		for(int x = 0; x < HealthBar.length; x++) {
			if(HealthBar[x] == 0) break;
			g.setFill(Color.rgb(100,100,100));
			g.fillRect(t,10,10,10);
			t = t+20;
		}
	}

	public void checkCollision() {
		for(int x = 0; x < enemy1s.size();x++) {
			double [] enemyPoint = enemy1s.get(x).damagePoint();
			for(int y = 0; y < bullets.size(); y++) {
				double [] bulletPoint = bullets.get(y).getDamagePoint();
				if((bulletPoint[0] >= enemyPoint[0] && bulletPoint[0] <= enemyPoint[0]+50) && (bulletPoint[1] >= enemyPoint[1]-15 && bulletPoint[1] <= enemyPoint[1]+28)) {
					kills++;
					score++;
					elimStat = String.format("Enemy eliminated Coord [ %.2f %.2f ]",enemy1s.get(x).x,enemy1s.get(x).y);
					eliminatedRefresh = 1000;
					enemy1s.remove(x);
					bullets.remove(y);
					fireRate += 0.001;
					addEnemy(1);
					break;
				}
			}
		}
	}

	boolean frun = true;

	public void addSpaceParticles(int n){
		for(int x = 0; x < n; x++)
			particles.add(new particle(frun ? rand.nextInt((int)canvas.getWidth()) : canvas.getWidth(),canvas.getHeight()));
		frun = false;
	}

	public void addEnemy(int n) {
		for(int x = 0; x < n; x++) {
			double pos = rand.nextInt((int)canvas.getHeight()-32);
			enemy1s.add(new enemy1(rand.nextInt((int)canvas.getWidth())+canvas.getWidth(),pos > canvas.getHeight()-130? pos - 130 : (pos <= 70 ? 70 : pos) ,100,canvas.getWidth()));
		}
	}

	public void addBullet(int n, int mode) {
		for(int x = 0; x < n ; x++) {
			if(mode == 1) {
				bullets.add(new Bullet(charX+charWidth, charY+charHeight, damageLevel));
			}else if(mode == 2) {
				bullets.add(new Bullet(charX+charWidth, charY+charHeight-20, damageLevel));
				bullets.add(new Bullet(charX+charWidth, charY+charHeight+20, damageLevel));
			}else if(mode == 3) {
				bullets.add(new Bullet(charX+charWidth, charY+charHeight-35, damageLevel));
				bullets.add(new Bullet(charX+charWidth, charY+charHeight, damageLevel));
				bullets.add(new Bullet(charX+charWidth, charY+charHeight+35, damageLevel));
			}
		}
		canfire = 0;
	}

	public void engine() {
		final LongProperty lastUpdateTime = new SimpleLongProperty(0);

		initEngine++;
		gameStat = " Game Started";
		final AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if(HealthBar[0] == 0) {
					initEngine = 0;
					gameStat = " Player Died";
					cliPlay();
					stop();
				}
				if (lastUpdateTime.get() > 0) {
					long elapsedTime = timestamp - lastUpdateTime.get();
					g.setFill(Color.rgb(20, 20, 20));
					update();
					canfire = canfire < allowedFire ? canfire += fireRate: 8;
				}
				lastUpdateTime.set(timestamp);
			}
		};
		timer.start();
	}

	private void pauseGame() {
		paused = !paused;
		gameStat = paused?" Game Paused":" Game Resumes";
	}

	private void resetGame(URL arg0, ResourceBundle arg1) {
		if(initEngine == 1 || paused) return;

		bullets = new ArrayList<>();
		particles = new ArrayList<>();
		enemy1s = new ArrayList<>();

		damageLevel = 0;
		fireRate = 0.5;
		kills = 0;
		score = 0;

		HealthBar = new int[] {1,1,1,1,1};
		healthptr = 4;

		counter = 0;
		allowedFire = 8;
		canfire = allowedFire;

		addSpaceParticles(100);
		addEnemy(30);

		engine();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		damageLevel = 1;
		canvas.setWidth(880);
		g = canvas.getGraphicsContext2D();
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e->{
					if(canfire != allowedFire) return;
					if(e.getCode() == KeyCode.UP)charY -= 5;
					if(e.getCode() == KeyCode.DOWN)charY += 5;

					if(e.getCode() == KeyCode.A) {
						firedStat = " Firing";
						addBullet(1,1);
					}
					else if(e.getCode() == KeyCode.S && kills >= 100) {
						addBullet(1,2);
						firedStat = " Firing";
					}
					else if(e.getCode() == KeyCode.D && kills >= 10000) {
						firedStat = " Firing";
						addBullet(1,3);
					}

					if(e.getCode() == KeyCode.NUM_LOCK) debuggingMode = debuggingMode ? false : true;
					if(e.getCode() == KeyCode.O) addEnemy(1);
					if(e.getCode() == KeyCode.P) pauseGame();
					if(e.getCode() == KeyCode.T) resetGame(arg0,arg1);
					spaceShip.setFiring(5);
				}
		);
		canvas.setOnKeyReleased(e->{
			firedStat = " Not Firing";
		});
		canvas.setCursor(Cursor.NONE);
		canvas.setOnMouseMoved(ev->{
			charX = ev.getX();
			if(ev.getY()-charHeight <= 34 || ev.getY() >= 580) return;
			charY = ev.getY()-charHeight;
		});

		rightBound = canvas.getWidth() + 50;
		floor = canvas.getHeight()-50;

		charX = 10;
		charY = canvas.getHeight()/2 - charHeight;

		g.fillText("Press T to Start The Game", canvas.getWidth()/2-80,canvas.getHeight()/2-50);
	}
}
