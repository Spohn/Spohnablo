//------------------------------------------------------
//Jon Spohn (SingleMap.java)
//This class represents an individual map, the wall, mob,
//and trap coordinates for the map.
//-------------------------------------------------------
 
import java.util.*;
import java.awt.Point;

public class SingleMap {
	
	ArrayList wallPoints,trapLocations,mobLocations;
	private Random random;
	private int WIDTH, LENGTH;
	private int numMobs, numTraps;
	private WallTemplates WT;
	private Point p;
	Point ExitLocation;
	int fitness = 0;
	
	//------------------------------------------------------------------------------
    public SingleMap(int width, int length, int nm, int nt,WallTemplates wt) {
    	this.random = new Random();
    	this.WIDTH = width;
    	this.LENGTH = length;
    	this.numMobs = nm;
    	this.numTraps = nt;
    	this.wallPoints = new ArrayList();
    	this.mobLocations = new ArrayList();
    	this.trapLocations = new ArrayList();
    	this.WT = wt;
    	
    	//set up walls for each 'square'
    	this.setWalls();
    	
    	//set up mob locations
    	for(int i = 0; i < numMobs; i++){
    		this.setMobLoc();
    	}
    	//set up trap locations
    	for(int i = 0; i < numTraps; i++){
    		this.setTrapLoc();
    	}
    	//set up the exit point
    	this.setExitLoc();
    }
    //------------------------------------------------------------------------------
    public void setWalls(){
    	//System.out.println("in setWalls()");
    	//pick from a template and add the points in that template into our array of wall points
    	//template positions all start from point 0,0 to 80,60 so when adding to our array will will edit
    	//the points depending on which 'square' of the dungeon we are adding this template to.
    	//E.G. the first template added will keep the same coordinates. moving down the rows we will add 
    	//80 * row # to every x coordinate, when moving down the columns we add 60 * column # to each y coordinate
    	for(int i = 0; i < WIDTH / 80; i++){
    		//System.out.println("in setWalls() first loop" + i );
    		for(int j = 0; j < LENGTH / 60; j++){
    			//System.out.println("in setWalls() second loop");
    			//System.out.println(j);
    			//Pick a random template from WT
    			int r = random.nextInt(WT.TemplateList.size());
    			//Add WIDTH * 80 to the X coordinate - Add LENGTH * 60 to the Y coordinate
    			for(int k = 0; k < ((ArrayList)WT.TemplateList.get(r)).size(); k++)
    			{
	    			p = new Point( ((Point)((ArrayList)WT.TemplateList.get(r)).get(k)) );
	    			p.setLocation((int)p.getX() + (i * 80),(int)p.getY() + (j * 60));
	    			//add new coordinate to wallPoints
	    			wallPoints.add(new Point(p));
	    			//System.out.println("in setWalls() adding template");
    			}
    		}
    	}
    }
    //------------------------------------------------------------------------------
    public void setExitLoc(){
    	//System.out.println("in set exit");
    	boolean bool;
    	int x, y;
    	do{
    		bool = true;
	    	//randomly find a location not occupied by a wall and set exit coordinates there
	    	//make sure we are not spawned on top of a wall
		   	do{
		   		x = this.random.nextInt(WIDTH);
		   	}while(x % 5 != 0);
		   	do{
		   		y = this.random.nextInt(LENGTH);
		   	}while(y % 5 != 0);
	    	Point p = new Point(x,y);
	    	
	    	//check to see if this if this is at a wall, if its not then set bool to false
	    	for(int i = 0; i < wallPoints.size(); i++){
		    	if(p.equals((Point)wallPoints.get(i))){
		    		//keep going!
		    	}
		    	else{
		    		ExitLocation = new Point(x,y);
		    		bool = false;
		    		break;
		    	}
	    	}
	    	
    	}while(bool);
    }
    //------------------------------------------------------------------------------
    public void setMobLoc(){
    	//System.out.println("in mob loc");
    	boolean bool;
    	int x, y;
    	do{
    		bool = true;
	    	//randomly find a location not occupied by a wall and set mob coordinates there
	    	do{
	    		x = this.random.nextInt(WIDTH);
	    	}while(x % 5 != 0);
	    	do{
	    		y = this.random.nextInt(LENGTH);
	    	}while(y % 5 != 0);
	    	Point p = new Point(x,y);
	    	
	    	//check to see if this if this is at a wall, if its not then set bool to false
	    	for(int i = 0; i < wallPoints.size(); i++){
		    	if(p.equals((Point)wallPoints.get(i))){
		    		//keep going!
		    	}
		    	else{
		    		mobLocations.add(p);
		    		bool = false;
		    		break;
		    	}
	    	}
	    	
    	}while(bool);
    }
    //------------------------------------------------------------------------------
    public void setTrapLoc(){
    	//System.out.println("in trap loc");
    	boolean bool;
    	boolean bool1;
    	int x, y;
    	//randomly find a location not occupied by a wall and set trap coordinates there
    	do{
    		bool = true;
    		bool1 = true;
	    	//randomly find a location not occupied by a wall and set trap coordinates there
	    	do{
	    		x = this.random.nextInt(WIDTH);
	    	}while(x % 5 != 0);
	    	do{
	    		y = this.random.nextInt(LENGTH);
	    	}while(y % 5 != 0);
	    	Point p = new Point(x,y);
	    	
	    	//check to see if this if this is at a wall and mob, if its not then set bool to false
	    	for(int i = 0; i < wallPoints.size(); i++){
		    	if(p.equals((Point)wallPoints.get(i))){
		    		//keep going!
		    	}
		    	else{
		    		bool = false;
		    		break;
		    	}
	    	}
	    	for(int i = 0; i < mobLocations.size(); i++){
		    	if(p.equals((Point)mobLocations.get(i))){
		    		//keep going!
		    	}
		    	else{
		    		if(bool == false){
		    			trapLocations.add(p);
			    		bool1 = false;
			    		break;
		    		}
		    	}
	    	}
    	}while(bool == true || bool1 == true);
    }
    //------------------------------------------------------------------------------
}