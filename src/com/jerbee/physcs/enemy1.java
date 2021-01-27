package com.jerbee.physcs;


import java.io.FileInputStream;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class enemy1 {
	
	double x,y,helth;
	double canvasW;
	double deltaX = 1.5;
	double deltaY = 0.01;
	private boolean Debugging = false;
	Random rand = new Random();
	Image img;
	
	public enemy1(double spawnX, double spawnY, double helth, double canvasW) {
		x = spawnX;
		y = spawnY;
		this.canvasW = canvasW;
		this.helth = helth;
		try {
			img = new Image(new FileInputStream("src/res/enemy1.png"));
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void draw(GraphicsContext g) {
		x-=  deltaX;
		if(deltaY >= 2.0) deltaY = 0.05;
		if(deltaY <= -2.0) deltaY = -0.05;
		y += (deltaY += rand.nextInt(2) % 2 == 0? 0.05 : -0.05);
		if(y <= 70 ) y += deltaY += 1.1;
		if(y >= 600) y += deltaY += -1.1;
		
		if(x >= canvasW) return;
		g.drawImage(img, x, y);
		g.setStroke(Color.rgb(180,89,89));
		if(Debugging) {
			g.strokeRect(x-5, y, 50, 25);
			g.setFont(new Font("consolas",15));
			g.strokeText(String.format("Enemy %.1f",deltaY),x, y-5);
			g.strokeLine(x,y+15,x-30,y + 15 + deltaY* 8);
		}
	}
	
	public void setDebugging(boolean t) {
		this.Debugging = t;
	}
	
	public double [] damagePoint() {
		double [] t = {x,y};
		return t;
	}
}
