//----------------------------------------------------------------------------------
//Jonathan Spohn (FinalProject.java) 12/06/08
//This is my final project for my Artificial Intelligence class
//It is a top down rpg game that is greatly inspired by the
//Diablo series. It will feature a genetic algorithm to generate
//the dungeons; the 'best' generated will be used. The games objective is
//for the user to control his character through the dungeon and to find the exit
//to the next level. Mobs (enemy monsters) will be controled by a learning classifier
//as the game progresses and the user travels through more dungeons the classifier
//should make the Mob make better decisions resulting in a more difficult level.
//------------------------------------------------------------------------------------
 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;

public class FinalProject extends JFrame implements Runnable, ActionListener{
	
	private static ArrayList mapList;
	private static ArrayList mobs;
	private dungeonPanel drawPanel;
	private static final int NUM_MAPS = 15;
	private static final int LENGTH = 780;
	private static final int WIDTH = 1040;
	private static int NUM_MOBS = 20;
	private static final int NUM_TRAPS = 50;
	private static WallTemplates WT;
	private static Hero hero;
	private static int level = 1;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem fog,quit,trap;
	private static boolean FOG = true, TRAP = false;

    public FinalProject() {
    	super("Spohnablo"); //name the Frame

    	//create the menu
    	menuBar = new JMenuBar(); //create the menu bar
    	menu = new JMenu("Options");
		fog = new JMenuItem("Toggle Fog of War");
		trap = new JMenuItem("Toggle Traps");
		quit = new JMenuItem("Quit");
		menuBar.add(menu);
		menu.add(fog);
		menu.add(trap);
		menu.add(quit);
		fog.addActionListener(this);
		quit.addActionListener(this);
		trap.addActionListener(this);
		
    	//create the draw panel
    	drawPanel = new dungeonPanel(WIDTH,LENGTH); 
		drawPanel.setBackground(Color.black);
		
		//add everything to the frame
		this.setJMenuBar(menuBar);
		this.add(drawPanel);
    	this.setSize(1200,850);
		this.setVisible(true);
    }
    //Event listener for the menu items----------------------------------------------
    public void actionPerformed(ActionEvent e) 
	{
        if (e.getSource() == fog) //if draw is selected
        {
        	if(FOG){
        		FOG = false;
        	}
        	else{
        		FOG = true;
        	}
        }
        if (e.getSource() == trap) //if draw is selected
        {
        	if(TRAP){
        		TRAP = false;
        	}
        	else{
        		TRAP = true;
        	}
        }
        if (e.getSource() == quit) //if quit is selected
        {
        	System.exit(0);
        }
    }
    //Main method executed when program initlized------------------------------------------
    public static void main(String[] args){
    	//set the number of mobs to fight
    	int num = Integer.parseInt(JOptionPane.showInputDialog("Enter Number Of Mobs"));
    	NUM_MOBS = num;
    	mobs = new ArrayList();
    	//Create the wall templates
    	WT = new WallTemplates(WIDTH,LENGTH);
    	//run random map generator, parameter: # of maps desired
    	MapGenerator mapGen = new MapGenerator(NUM_MAPS,WIDTH,LENGTH,NUM_MOBS,NUM_TRAPS,WT);
    	//store best 10 maps returned from the generator
    	mapList = mapGen.getMaps();
    	//Create the Hero
    	hero = new Hero(5,5);
    	//Create the mobs at each location specified by the map
    	createMobs();
    	
    	//begin game by instantiating the FinalProject class
    	System.out.println("STARTING GAME!");
    	FinalProject game = new FinalProject();
    	game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	(new Thread(game)).start();
    }
    //------------------------------------------------------------
    //Create the mob objects with the points designated by the current map
    public static void createMobs(){
    	for(int i = 0; i < NUM_MOBS; i++){
    		Mob m = new Mob(   (int)((Point)((SingleMap)mapList.get(level)).mobLocations.get(i)).getX(),(int)((Point)((SingleMap)mapList.get(level)).mobLocations.get(i)).getY(),(SingleMap)mapList.get(level),hero,(Point)((SingleMap)mapList.get(level)).ExitLocation );
    		mobs.add(m);
    		(new Thread(m)).start();
    	}
    }
    //------------------------------------------------------------
    //Infinite loop refreshing the game until the user dies.
    public void run(){
    	while(true){
	    	drawPanel.repaint(); //repaint the panel
	    	if(hero.health <= 0){
	    		System.out.println("You Lost.");
	    		System.exit(0);
	    	}
	    	try{
	    		 Thread.sleep(10); //sleep
	    	}
	    	catch(InterruptedException e){
	    	}
    	}
    }
    //------------------------------------------------------------
    //extended JPanel to draw the dungeon
    private class dungeonPanel extends JPanel
	{
		private int prefwid, prefht;

		//constructor for the panel
		public dungeonPanel(int pwid, int pht)
		{
			prefwid = pwid;
			prefht = pht;
			
			addKeyListener(new MyKeyListener());
			addMouseListener(
				new MouseAdapter(){
					public void mouseEntered(MouseEvent e)
					{
						requestFocus();
					}
				}); 
		}
		
		public Dimension getPreferredSize()
		{
			return new Dimension(prefwid,prefht);
		}
		public boolean isFocusable()
		{
			return true;
		}
		
		//used to paint
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			
			//Draw Border
			g2d.setColor(Color.GREEN);
			g2d.fillRect(0,0,1040,1);
			g2d.fillRect(0,0,1,780);
			g2d.fillRect(1040,0,1,780);
			g2d.fillRect(0,780,1040,1);
							
			//Draw the exit
			if(FOG){
				if(  Math.abs((int)hero.p.getX() - (int)((SingleMap)mapList.get(level)).ExitLocation.getX()) < 100   && Math.abs((int)hero.p.getY() - (int)((SingleMap)mapList.get(level)).ExitLocation.getY()) < 75)
				{
					g2d.setColor(Color.MAGENTA);
					g2d.fill( new Ellipse2D.Double( (int)((SingleMap)mapList.get(level)).ExitLocation.getX(),(int)((SingleMap)mapList.get(level)).ExitLocation.getY(),5,5 ));
					if( (int)hero.p.getX() == (int)((SingleMap)mapList.get(level)).ExitLocation.getX() && (int)hero.p.getY() == (int)((SingleMap)mapList.get(level)).ExitLocation.getY() )
					{
						//Advance to the next level
						nextLevel();
					}
			    }
				
				//Draw Walls if in range of hero
				for(int i = 0; i < ((SingleMap)mapList.get(level)).wallPoints.size(); i++){
					if(  Math.abs((int)hero.p.getX() - (int)((Point)((SingleMap)mapList.get(level)).wallPoints.get(i)).getX()) < 100   && Math.abs((int)hero.p.getY() - (int)((Point)((SingleMap)mapList.get(level)).wallPoints.get(i)).getY()) < 75)
					{
						g2d.setColor(Color.WHITE);
						g2d.fillRect(  (int)((Point)((SingleMap)mapList.get(level)).wallPoints.get(i)).getX(), (int)((Point)((SingleMap)mapList.get(level)).wallPoints.get(i)).getY(),1,1);
					}
				}
				
				//Draw mobs if in range of hero and if they're alive
				for(int i = 0; i < NUM_MOBS; i++){
					if(   Math.abs( (int)hero.p.getX() - (int)((Mob)mobs.get(i)).p.getX()) < 100  && Math.abs((int)hero.p.getY() - (int)((Mob)mobs.get(i)).p.getY()) < 75  )
					{
						if(((Mob)mobs.get(i)).health > 0){
							g2d.setColor(Color.RED);
							g2d.fill( new Ellipse2D.Double( (int)((Mob)mobs.get(i)).p.getX(),   (int)((Mob)mobs.get(i)).p.getY(),5,5));
						}
					}
				}
				
				//Draw Hero -- change color and enter combat if mob and hero occupy same space
				g2d.setColor(Color.BLUE);
				for(int i = 0; i < NUM_MOBS; i++){
					if( (int)hero.p.getX() == (int)((Mob)mobs.get(i)).p.getX() && (int)hero.p.getY() == (int)((Mob)mobs.get(i)).p.getY())
					{
						if(((Mob)mobs.get(i)).health > 0){
							g2d.setColor(Color.ORANGE);
							//Enter Combat!
							((Mob)mobs.get(i)).Combat();
							hero.mobCombat();
						}
					}
				}
				
				//Check to see if Hero has stepped on a trap
				for(int i = 0; i < NUM_TRAPS; i++){
					if( (int)hero.p.getX() == (int)((Point)((SingleMap)mapList.get(level)).trapLocations.get(i)).getX() && (int)hero.p.getY() == (int)((Point)((SingleMap)mapList.get(level)).trapLocations.get(i)).getY())
					{
						//Do trap Damage
						hero.trapDamage();
					}
				}
				g2d.fill( new Ellipse2D.Double( (int)hero.p.getX(),(int)hero.p.getY(),5,5));
			}
			else{
				
				//Draw the Exit
				g2d.setColor(Color.MAGENTA);
				g2d.fill( new Ellipse2D.Double( (int)((SingleMap)mapList.get(level)).ExitLocation.getX(),(int)((SingleMap)mapList.get(level)).ExitLocation.getY(),5,5 ));
				if( (int)hero.p.getX() == (int)((SingleMap)mapList.get(level)).ExitLocation.getX() && (int)hero.p.getY() == (int)((SingleMap)mapList.get(level)).ExitLocation.getY() )
				{
					//Advance to the next level
					nextLevel();
				}
			    
				//Draw Walls 
				for(int i = 0; i < ((SingleMap)mapList.get(level)).wallPoints.size(); i++){
					
					
						g2d.setColor(Color.WHITE);
						g2d.fillRect(  (int)((Point)((SingleMap)mapList.get(level)).wallPoints.get(i)).getX(), (int)((Point)((SingleMap)mapList.get(level)).wallPoints.get(i)).getY(),1,1);
					
				}
				
				//Draw mobs if they're alive
				for(int i = 0; i < NUM_MOBS; i++){
					
					
						if(((Mob)mobs.get(i)).health > 0){
							g2d.setColor(Color.RED);
							g2d.fill( new Ellipse2D.Double( (int)((Mob)mobs.get(i)).p.getX(),   (int)((Mob)mobs.get(i)).p.getY(),5,5));
						}
					
				}
				
				//Draw Hero -- change color and enter combat if mob and hero occupy same space
				g2d.setColor(Color.BLUE);
				for(int i = 0; i < NUM_MOBS; i++){
					if( (int)hero.p.getX() == (int)((Mob)mobs.get(i)).p.getX() && (int)hero.p.getY() == (int)((Mob)mobs.get(i)).p.getY())
					{
						if(((Mob)mobs.get(i)).health > 0){
							g2d.setColor(Color.ORANGE);
							//Enter Combat!
							((Mob)mobs.get(i)).Combat();
							hero.mobCombat();
						}
					}
				}
				
				//Check to see if Hero has stepped on a trap
				for(int i = 0; i < NUM_TRAPS; i++){
					if( (int)hero.p.getX() == (int)((Point)((SingleMap)mapList.get(level)).trapLocations.get(i)).getX() && (int)hero.p.getY() == (int)((Point)((SingleMap)mapList.get(level)).trapLocations.get(i)).getY())
					{
						//Do trap Damage
						hero.trapDamage();
					}
				}
				g2d.fill( new Ellipse2D.Double( (int)hero.p.getX(),(int)hero.p.getY(),5,5));
			}
			
			//Draw Health on side
			g2d.drawString("Health: " + Integer.toString(hero.health),1050,100);
			
			if(TRAP){
				//Draw traps
				for(int i = 0; i < NUM_TRAPS; i++){
					g2d.setColor(Color.GREEN);
					g2d.fill( new Ellipse2D.Double( (int)((Point)((SingleMap)mapList.get(level)).trapLocations.get(i)).getX(),   (int)((Point)((SingleMap)mapList.get(level)).trapLocations.get(i)).getY(),5,5));
				}
			}	
		}
	}
	//-------------------------------------------------------------
	//Proceed to the next level
	public void nextLevel(){
		//Increase the level number
		if(level++ == NUM_MAPS){
			//Game has reached its end
			System.out.println("You Beat Spohnablo!");
			System.exit(0);
		}
		
		//reset hero health and location
		hero.health = 150;
		hero.p.setLocation(5,5);
		
		//we need to start the new level, Give the mobs new locations and reset health
		for(int i = 0; i < NUM_MOBS; i++){
			((Mob)mobs.get(i)).hero = hero;
			((Mob)mobs.get(i)).map = (SingleMap)mapList.get(level);
			((Mob)mobs.get(i)).exit = (Point)((SingleMap)mapList.get(level)).ExitLocation;
			((Mob)mobs.get(i)).health = 15 + (5 * level);
			((Mob)mobs.get(i)).str += 1;
			((Mob)mobs.get(i)).p.setLocation(  (int)((Point)((SingleMap)mapList.get(level)).mobLocations.get(i)).getX(),  (int)((Point)((SingleMap)mapList.get(level)).mobLocations.get(i)).getY() );
		}
	}
	//-----------------------------------------------------------
    //Adds key listeners to the arrow keys so you can control the
    //snake once you click inside the panel
    private class MyKeyListener extends KeyAdapter
    {
    	public void keyPressed(KeyEvent e)
    	{
    		if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
    		{
    			//move down if no wall is there
    			if( !((SingleMap)mapList.get(level)).wallPoints.contains(new Point((int)hero.p.getX(),(int)hero.p.getY() + 5)) && (int)hero.p.getY() + 5 <= 775)
    				hero.p.setLocation((int)hero.p.getX(),(int)hero.p.getY() + 5);
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
    		{
    			//move up if no wall is there
    			if( !((SingleMap)mapList.get(level)).wallPoints.contains(new Point((int)hero.p.getX(),(int)hero.p.getY() - 5)) && (int)hero.p.getY() + 5 >= 10)
    				hero.p.setLocation((int)hero.p.getX(),(int)hero.p.getY() - 5);
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
    		{
    			//move right if no wall is there
    			if( !((SingleMap)mapList.get(level)).wallPoints.contains(new Point((int)hero.p.getX() + 5,(int)hero.p.getY())) && (int)hero.p.getX() + 5 <= 1035)
    				hero.p.setLocation((int)hero.p.getX() + 5,(int)hero.p.getY());
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
    		{
    			//move left if no wall is there
    			if( !((SingleMap)mapList.get(level)).wallPoints.contains(new Point((int)hero.p.getX() - 5,(int)hero.p.getY())) && (int)hero.p.getX() + 5 >= 10)
    				hero.p.setLocation((int)hero.p.getX() - 5,(int)hero.p.getY());
    		}

    	}
    }
}