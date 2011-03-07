
/*
	******************************************************************
	Boid model simulator.
	By Gianfranco Alongi AKA zenon
		gianfranco@alongi.se
		20071227
	******************************************************************
*/		
		

import java.awt.*;
import javax.swing.*;
import java.lang.Thread;
import java.util.Random;
import java.lang.Math;

public class Model extends JPanel implements Runnable {

	/* -------------------------------------------- */
	private Thread 		clock;			//PSO ticker
	private Boid[]		boids;			//Boids
	private int height	= 600;			//Height of window
	private int width	= 600;			//Width of window
	private int b_n;					//Amount of boids
	
	private int xmax	= 600;
	private int xmin	= 0;
	private int vmax	= 6;			//Maximum speed
	
	private int simstep = 10;			//Simulation sleep time
	private boolean hasstopped;
	private boolean bounces;			//Bounces of edges?
			
	private Random  engine;				//Random engine	
	
	//Simulation parameters
	private double	Cc;					//Cohersion constant
	private double	Cl;					//Alignment constant
	private double	Cs;					//Separation constant
	private int		range;				//Boid visual range
		
	/* -------------------------------------------- */
	public Model(	int n,
					double co,
					double al,
					double sep,
					int ran){
		bounces		= true;
		b_n			= n;
		Cc			= co;
		Cl			= al;
		Cs			= sep;
		range		= ran;
		engine		= new Random();
		boids		= new Boid[b_n];
		
		//Initialize each boid
		for(int k = 0; k < b_n; k++){	
			int[] a = {	engine.nextInt(xmax),engine.nextInt(xmax)};
			int[] b = { (int) Math.pow(-1,engine.nextInt(2))*engine.nextInt(vmax),
						(int) Math.pow(-1,engine.nextInt(2))*engine.nextInt(vmax)};
			boids[k] = new Boid(a,b,range);
		}
					
		//Panel setting
		setSize(width,height);
		setVisible(true);

		//Setup
		hasstopped	= false;
		clock		= new Thread(this);
	}
	
	//Start this...
	public void start(){ 
		if(hasstopped){
			clock.resume();
		}else{
			clock.start();
		}
	}
	
	//Stop it
	public void stop(){ 
		clock.suspend();
		hasstopped=true;
	}
	
	//Runtime
	public void run(){
		while(true){
			try{
				clock.sleep(simstep);
															
				//Update boid velocity and position for each boid
				for(int k = 0; k < b_n; k++){
				
					//Collect visibility sphere boids
					//------------------------------------------------
					Boid[] sphere		= new Boid[b_n];
					int viewed			= 0;					
					for(int a = 0; a < b_n; a++){
						if(a != k){
							if(	boids[k].distance(boids[a].x_pos) <=
								(boids[k].radius/2)){
								sphere[viewed]	= boids[a];
								viewed			= viewed+1;
							}
						}
					}
					//------------------------------------------------
					
					//Calculate centre of mass for sphere and boid
					//------------------------------------------------
					double[] rho	= {0,0};
					for(int a = 0; a < viewed; a++){
						rho[0] += sphere[a].x_pos[0] / viewed;
						rho[1] += sphere[a].x_pos[1] / viewed;
					}
					//------------------------------------------------
					
					//Calculate cohersion vector
					//------------------------------------------------
					double[] cohersion = {0,0};
					if(viewed == 0){
						cohersion[0] = 0;
						cohersion[1] = 0;
					}else{
						cohersion[0] = rho[0] - boids[k].x_pos[0];
						cohersion[1] = rho[1] - boids[k].x_pos[1];
					}
					//------------------------------------------------
					
					//Calculate alignment vector
					//------------------------------------------------
					double[] align		= {0,0};
					for(int a = 0; a < viewed; a++){
						align[0]	+= sphere[a].vel[0] / viewed;
						align[1]	+= sphere[a].vel[1] / viewed;
					}
					//------------------------------------------------
					
					//Calculate separation vector
					//------------------------------------------------
					double[] separation = {0,0};
					for(int a = 0; a < viewed; a++){
						separation[0]	+= boids[k].x_pos[0] - sphere[a].x_pos[0];
						separation[1]	+= boids[k].x_pos[1] - sphere[a].x_pos[1];
					}
					//------------------------------------------------
					
					//Calculate acceleration vector
					//------------------------------------------------
					double[] acc = {0,0};
					acc[0]	+= cohersion[0]*Cc + align[0]*Cl + separation[0]*Cs;
					acc[1]	+= cohersion[1]*Cc + align[1]*Cl + separation[1]*Cs;
					//------------------------------------------------
					
					//Update the position
					boids[k].x_pos[0] = boids[k].x_pos[0] + boids[k].vel[0];
					boids[k].x_pos[1] = boids[k].x_pos[1] + boids[k].vel[1];
					
					//Sane bounding
					if(boids[k].x_pos[0] < xmin) {
						if(bounces){
							boids[k].vel[0] = - boids[k].vel[0];
						}
						boids[k].x_pos[0] = xmin;
					}
					if(boids[k].x_pos[1] < xmin) {
						if(bounces){
							boids[k].vel[1] = - boids[k].vel[1];
						}
						boids[k].x_pos[1] = xmin;
					}
					if(boids[k].x_pos[0] > xmax) {
						if(bounces){
							boids[k].vel[0] = - boids[k].vel[0];
						}
						boids[k].x_pos[0] = xmax;
					}
					if(boids[k].x_pos[1] > xmax) {
						if(bounces){
							boids[k].vel[1] = - boids[k].vel[1];
						}
						boids[k].x_pos[1] = xmax;
					}


					//Update the velocity					
					boids[k].vel[0] = (int) (boids[k].vel[0] + acc[0]);
					boids[k].vel[1] = (int) (boids[k].vel[1] + acc[1]);
									
					//Sane bounding
					if(boids[k].vel[0] > vmax) boids[k].vel[0] = vmax;
					if(boids[k].vel[1] > vmax) boids[k].vel[1] = vmax;
					if(boids[k].vel[0] < -vmax) boids[k].vel[0] = -vmax;
					if(boids[k].vel[1] < -vmax) boids[k].vel[1] = -vmax;					
				}
				
				repaint();
			}catch(InterruptedException ie){System.out.println("stopped");}
		}
	}
	
	public void paint(Graphics g)
	{
		Image im	= createImage(width,height);
		Graphics g2 = im.getGraphics();
						
		//Paint all boids
		for(int p = 0; p < b_n; p++){
			boids[p].paint(g2);
		}		
		g.drawImage(im,0,0,null);
	}
}
