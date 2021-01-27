package com.jerbee.physcs;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class particle {
	
	private double x,y;
	private double radius;
	private double backSpeed;
	private	Color [] cols = new Color[3];
	private Color fColor ;
	private Random t = new Random();
	
	public particle(double pWidth, double pHeight) {
		x = pWidth;
		y = t.nextInt((int)pHeight);
		cols[0] = Color.rgb(122, 122, 122);
		cols[1] = Color.rgb(80, 80, 80);
		cols[2] = Color.rgb(180, 180, 180);
		radius = t.nextInt(4);
		backSpeed = t.nextDouble()  * 5.6;
		fColor = cols[t.nextInt(3)];
	}
	
	public void draw(GraphicsContext g, double canvasW) {
		x-=backSpeed;
		if(x >= canvasW) return;
		g.setFill(fColor);
		g.fillOval(x,y,radius * 2, radius * 2);
	}
	
	public double getX() {return x;}
	public double getRadius() {return radius * 2;}

}
