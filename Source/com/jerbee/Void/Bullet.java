package com.jerbee.Void;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Bullet {
	
	private double x,y,from;
	private double textY;
	private double damage;
	private Random rand = new Random();
	private Image img,trail1,trail2,trail3;
	private double DeltaX = 3.0;
	private double DeltaX2 = 2.5;
	private double DeltaY = 0.01;
	private boolean Debugging = false;
	
	int update = 0;

	public double getDmg(){ return damage; }
	
	public Bullet(double x, double y, double damage) {
		this.x = x;
		this.y = y;
		textY = y - 30 - rand.nextInt(100);
		this.damage = damage;
		from = x;
		try {
			img = new Image(new FileInputStream("src/res/cbullet.png"));
			trail1 = new Image(new FileInputStream("src/res/exhaustFire_1.png"));
			trail2 = new Image(new FileInputStream("src/res/exhaustFire_2.png"));
			trail3 = new Image(new FileInputStream("src/res/exhaustFire_0.png"));
			
			img = new Image(new File("src/res/cbullet.png").toURI().toString(),img.getWidth()/7,img.getHeight()/7,false,false);
			trail1 = new Image(new File("src/res/exhaustFire_1.png").toURI().toString(),img.getWidth()/1.5,img.getHeight()/1.5,false,false);
			trail2 = new Image(new File("src/res/exhaustFire_2.png").toURI().toString(),img.getWidth()/1.5,img.getHeight()/1.5,false,false);
			trail3 = new Image(new File("src/res/exhaustFire_0.png").toURI().toString(),img.getWidth()/1.5,img.getHeight()/1.5,false,false);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void draw(GraphicsContext g, double canvW) {

		x+=(DeltaX+=0.1);
		y+=(DeltaY+=rand.nextInt(2) % 2 == 0 ? 0.08:-0.08);
		update++;
		
		if(x >= canvW) return;
		
		if(update % 2 == 0) g.drawImage(trail1, x-trail1.getWidth(), y+2);
		else if(update % 3 == 0) g.drawImage(trail2, x-trail2.getWidth(),y+2);
		else g.drawImage(trail3, x-trail3.getWidth(),y+2);
		
		g.drawImage(img,x,y);
		g.setStroke(Color.rgb(89, 89, 89));
		if(Debugging) {
			g.strokeRect(x-20, y, 50, 15);
			g.setFont(new Font("consolas",15));
			g.strokeText(String.format("Bullet %.1f",DeltaY),from+=(DeltaX2+=0.03),y-30);
			g.strokeLine(from+15, y - 25, x - 15, y);
			g.fillOval(x-25, y+5, 3 * 2, 3 * 2);
			g.setLineWidth(1.5);
			g.strokeLine(x,y+8,x+60+DeltaX+0.08,y+8+DeltaY * 8);
			g.setLineWidth(1);
		}
	}

	public double getX() {return x;}

	public double getY() {return y;}

	public double getDamage() {return damage;}
	public void setDebugging(boolean t) {
		this.Debugging = t;
	}
	
	public double [] getDamagePoint() {
		double [] point = {x + img.getWidth()/7,y};
		return point;
	}
}
