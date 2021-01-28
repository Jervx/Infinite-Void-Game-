package com.jerbee.Void;

import java.io.File;
import java.io.FileInputStream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Character {
	
	private Image img,img_f;
	private int isFiring = 0;
	
	public Character() {
		try {
			img = new Image(new FileInputStream("src/res/character.png"));
			img = new Image(new File("src/res/character.png").toURI().toString(),img.getWidth()/3,img.getHeight()/3,false,false);
		}catch(Exception e) {System.out.println(e);}
	}
	
	public void draw(GraphicsContext g, double x, double y) {
		//g.drawImage(isFiring <= 0? img: img_f, x, y);
		g.drawImage( img, x, y);
		isFiring -= 1;
	}
	
	public void setFiring(int t){isFiring = t;}
}
