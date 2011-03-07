
/*
	******************************************************************
	Boid model simulator interface.
	So simple. I know.
	
	Gianfranco Alongi	AKA zenon
		gianfranco@alongi.se
	20071227
	******************************************************************
*/	

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Example extends JFrame implements	ActionListener, 
												AdjustmentListener
{
	// Private objects ---------------------------------------------
	private Model model;				//The simulation

	private JButton		new_sim;		//New simulation
	private JButton		resume_sim;		//resume simulation
	private JButton		suspend_sim;	//suspend simulation
	
	private JScrollBar	bar_boids;		//Amount of boids
	private JScrollBar	bar_cohersion;	//Cohersion constant 
	private JScrollBar	bar_alignment;	//Alignment constant
	private JScrollBar	bar_separation;	//Separation constant
	private JScrollBar  bar_range;		//Visual range of boids
	
	private JLabel	label_boids;		//Labels
	private JLabel	label_cohersion;	//
	private JLabel	label_alignment;	//
	private JLabel	label_separation;	//
	private JLabel	label_range;		//

	/* ----------------------------------------------------------- */
	public Example()
	{	
		setSize(600,800);
		setLayout(new BorderLayout());
		
		//Control panel setup
		JPanel controls	= new JPanel(new GridLayout(3,5));
		new_sim			= new JButton("New sim");
		resume_sim		= new JButton("Resume");
		suspend_sim		= new JButton("Suspend");
		bar_boids		= new JScrollBar(JScrollBar.HORIZONTAL,
										 5,1,1,100);
		bar_cohersion	= new JScrollBar(JScrollBar.HORIZONTAL,
										 10,1,0,100);
		bar_alignment	= new JScrollBar(JScrollBar.HORIZONTAL,
										 10,1,0,100);
		bar_separation	= new JScrollBar(JScrollBar.HORIZONTAL,
										 10,1,0,100);
		bar_range		= new JScrollBar(JScrollBar.HORIZONTAL,
										 100,1,1,1000);

		label_boids		= new JLabel("Boids = "+bar_boids.getValue());
		label_cohersion	= new JLabel("Cohersion = "+ (double)
									(((double) bar_cohersion.getValue())/100));
		label_alignment	= new JLabel("Alignment = "+ (double)
									(((double) bar_alignment.getValue())/100));
		label_separation= new JLabel("Separation = "+ (double)
									(((double) bar_separation.getValue())/100));
		label_range		= new JLabel("Range = "+ bar_range.getValue());

		//Set up listeners
		new_sim.addActionListener(this);
		resume_sim.addActionListener(this);
		suspend_sim.addActionListener(this);
		
		bar_boids.addAdjustmentListener(this);
		bar_cohersion.addAdjustmentListener(this);
		bar_alignment.addAdjustmentListener(this);
		bar_separation.addAdjustmentListener(this);
		bar_range.addAdjustmentListener(this);

		controls.add(new_sim);
		controls.add(resume_sim);
		controls.add(suspend_sim);
		controls.add(new JPanel());
		controls.add(new JPanel());
		controls.add(bar_boids);
		controls.add(bar_cohersion);
		controls.add(bar_alignment);
		controls.add(bar_separation);
		controls.add(bar_range);
		controls.add(label_boids);
		controls.add(label_cohersion);
		controls.add(label_alignment);
		controls.add(label_separation);
		controls.add(label_range);
		
		newModel();
		
		setTitle("Boid model example. by Gianfranco Alongi");
		add(controls,BorderLayout.SOUTH);
		add(model,BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//Actions (button pressed and so forth)
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == new_sim){
			remove(model);
			newModel();
			add(model);
			repaint();
		}else
		if(ae.getSource() == resume_sim){
			model.start();
		}else
		if(ae.getSource() == suspend_sim){
			model.stop();
		}
	}

	//Adjusted values changed (scrollbar)
	public void adjustmentValueChanged(AdjustmentEvent ae){
		if(ae.getSource() == bar_boids){
				label_boids.setText("Boids = "+bar_boids.getValue());
		}else
		if(ae.getSource() == bar_cohersion){
				label_cohersion.setText("Cohersion = "+ (double)
									(((double) bar_cohersion.getValue())/100));
		}else
		if(ae.getSource() == bar_alignment){
				label_alignment.setText("Alignment = "+ (double)
									(((double) bar_alignment.getValue())/100));
		}else
		if(ae.getSource() == bar_separation){
				label_separation.setText("Separation = "+ (double)
									(((double) bar_separation.getValue())/100));
		}else
		if(ae.getSource() == bar_range){
				label_range.setText("Range = "+ bar_range.getValue());
		}
		repaint();
	}

	//Create a new model
	public void newModel()
	{
			model = new Model(bar_boids.getValue(),
						  (double) (((double) bar_cohersion.getValue())/100),
						  (double) (((double) bar_alignment.getValue())/100),
						  (double) (((double) bar_separation.getValue())/100),
						  bar_range.getValue());
	}

	//Simple main
	public static void main(String[] args){
		Example k = new Example();
	}
}
