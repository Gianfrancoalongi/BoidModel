
/*
	******************************************************************
	Boid model
	An extremely simple boid model bird.
	
	Gianfranco Alongi AKA zenon
	20071226
	******************************************************************
*/

import java.awt.*;
import javax.swing.*;
import java.lang.Math;

public class Boid {

	/* ----------------------------------------- */
	public 		int[] 	x_pos;			//Position
	public 		int[]	vel;			//Velocity	
	public 		int		radius;			//View radius
	
	/* ----------------------------------------- */
	public Boid(int[] pos, 
					int[] vel,
					int rad){
		x_pos 		= new int[2];
		this.vel	= new int[2];
		x_pos[0]	= pos[0];
		x_pos[1]	= pos[1];
		this.vel[0]	= vel[0];
		this.vel[1]	= vel[1];
		radius		= rad;					
	}
	
	/* Calculate the distance to another boid */
	public int distance(int[] pos){
		int xdiff	= x_pos[0]-pos[0];
		int ydiff	= x_pos[1]-pos[1];
		int a		= (int) Math.sqrt( (xdiff*xdiff) + (ydiff*ydiff) );
		return a;
	}
	
	//How to paint the boid
	public void paint(Graphics g){
		//Draw the boid with it's vector
		g.setColor(Color.red);
		g.fillOval(x_pos[0]-3,x_pos[1]-3,6,6);
		g.drawLine(x_pos[0],x_pos[1],x_pos[0]+vel[0]*5,x_pos[1]+vel[1]*5);
		
		//Draw its view distance
		g.drawOval(x_pos[0]-(radius/2),x_pos[1]-(radius/2),radius,radius);
		g.setColor(Color.black);
	}
		
}
