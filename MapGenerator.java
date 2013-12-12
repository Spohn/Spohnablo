//-----------------------------------------------------------------
//Jon Spohn (MapGenerator.java)
//This is the genetic algorithm class that will evolve a population
//of 'good', and playable maps for the Spohnablo game.
//-----------------------------------------------------------------

import java.util.*;
import java.io.*;
import java.awt.Point;

public class MapGenerator {
	
	private int MaxGen;
	private ArrayList mapList;
	private int WIDTH, LENGTH;
	private int numMobs, numTraps;
	private WallTemplates WT;
	private AStar as;
	//--------------------------------------------------------------------------------
    public MapGenerator(int maps,int width, int length,int nm, int nt, WallTemplates wt) {
    	this.WIDTH = width;
    	this.LENGTH = length;
    	this.numMobs = nm;
    	this.numTraps = nt;
    	this.mapList = new ArrayList();
    	this.MaxGen = maps;
    	this.WT = wt;
    	this.createMaps();
    	this.as = new AStar();
    }
    //--------------------------------------------------------------------------------
    public void createMaps(){
    	//create initial population
    	CreatePopulation(MaxGen);
 
    	//Run the Genetic Algorithm -- Testing has shown the GA doesnt really do much other
    	//than ensure randomness, it only slightly increases the fitness after each evolution
    	//Therefore to save time the GA does not need to run more than a small number of times
    	System.out.println("\nRUNNING MAP GEN... Please wait a few moments(Slow machines it can take some time");
    	for(int i = 0; i < 5; i++){
    		//determine fitness score for each schedule of current generation
    		Fitness(i);
    		
    		//display average fitness per generation (Optional, nothing shown to screen)
    		
    		
    		//selection - crossover or mutation and create new generation
    		CreateNewGen();
    	}
    }
    //--------------------------------------------------------------------------------
    //Create the Initial population of size N
    public void CreatePopulation(int n){
    	for(int j = 0; j < n; j++){
    		//System.out.println(j);
    		SingleMap m = new SingleMap(WIDTH,LENGTH,numMobs,numTraps,WT);

	    	mapList.add(m);
    	}
    }
    //--------------------------------------------------------------------------------
    public void Fitness(int g){
    	
    	//Determine the fitness a a map
    	for(int i = 0; i < mapList.size(); i++){
    		
    		//*****After testing A* has been deemed to slow for use in map generation*****
    		//as = new AStar();
	    	//Does a path from starting point of hero to exit exist?
	    	//Point p = as.findPath( new Point(5,5),(Point)((SingleMap)mapList.get(i)).ExitLocation ,(SingleMap)mapList.get(i) );
	    	
	    	//if(p.getX() == -1 && p.getY() == -1){
	    		//No path exist in this map - Penalty
	    	//	((SingleMap)mapList.get(i)).fitness += 100;
	    	//}else{ 
	    		//Path exist so map is good
	    	//	((SingleMap)mapList.get(i)).fitness -= 100;
	    	//}
	    	
	    	//How close are mobs to each other
	    	for(int k = 0; k < numMobs; k++){
	    		for(int j = 0; j < numMobs; j++){
	    			if(  Math.abs(((Point)((SingleMap)mapList.get(i)).mobLocations.get(k)).getX() - ((Point)((SingleMap)mapList.get(i)).mobLocations.get(j)).getX()) < 10 && Math.abs(((Point)((SingleMap)mapList.get(i)).mobLocations.get(k)).getY() - ((Point)((SingleMap)mapList.get(i)).mobLocations.get(j)).getY()) < 10 ){
	    				//Mobs are too close to each other
	    				((SingleMap)mapList.get(i)).fitness -= 1;
	    			}
	    			else{
	    				((SingleMap)mapList.get(i)).fitness += 1;
	    			}
	    		}
	    	}
	    	
	    	//How close are traps to each other
	    	for(int k = 0; k < numTraps; k++){
	    		for(int j = 0; j < numTraps; j++){
	    			if(  Math.abs(((Point)((SingleMap)mapList.get(i)).trapLocations.get(k)).getX() - ((Point)((SingleMap)mapList.get(i)).trapLocations.get(j)).getX()) < 10 && Math.abs(((Point)((SingleMap)mapList.get(i)).trapLocations.get(k)).getY() - ((Point)((SingleMap)mapList.get(i)).trapLocations.get(j)).getY()) < 10 ){
	    				//Mobs are too close to each other
	    				((SingleMap)mapList.get(i)).fitness -= 1;
	    			}
	    			else{
	    				((SingleMap)mapList.get(i)).fitness += 1;
	    			}
	    		}
	    	}
	    	
	    	//how close are mobs to the starting hero
	    	for(int j = 0; j < numMobs; j++){
	    			if(  Math.abs(((Point)((SingleMap)mapList.get(i)).mobLocations.get(j)).getX() - 5) < 10 && Math.abs(((Point)((SingleMap)mapList.get(i)).mobLocations.get(j)).getY() - 5) < 10 ){
	    				//Mobs are too close to each other
	    				((SingleMap)mapList.get(i)).fitness -= 1;
	    			}
	    			else{
	    				((SingleMap)mapList.get(i)).fitness += 1;
	    			}
	    		}
    	}
    }
    //--------------------------------------------------------------------------------
    public void CreateNewGen(){
    	//Swap Rows and Columns of the 'good' maps
    	//occasionaly Mutate a map
    	ArrayList newMapList = new ArrayList();
    	Random o = new Random();
    	int prob = o.nextInt(100);
    	for(int i = 0; i < MaxGen; i++){ //loop through the maps and breed the good ones
    		int best = 0;
    		for(int k = 0; k < MaxGen; k++){
		    	//nested for loops to search for the two best fitnesses
		    	if(  ((SingleMap)mapList.get(i)).fitness > best ){
		    		best = ((SingleMap)mapList.get(i)).fitness;
		    	}
		    	
    		}
    		if(prob < 5){ //mutation
				SingleMap m = new SingleMap(WIDTH,LENGTH,numMobs,numTraps,WT);
		    	newMapList.add(m);
		    }
		    else if(prob < 75){ //crossover rows of the two maps
		    int wee = 0, yup = 60;
		  		for(int k = 0; k < 1080; k++){
		  			for(int j = wee; j < yup; j++){
		  				//swap all points found between 1080x60
		  				if( ((SingleMap)mapList.get(i)).wallPoints.contains(new Point(k,j))){
		  					newMapList.add(((SingleMap)mapList.get(j)).wallPoints.add(new Point(k,j)));
		  				}
		  				if(wee < 780)
		  					wee += 60;
		  				if(yup < 780)
		  					yup += 60;
		  			}
		  		}		
		    }
		  	else{ //do nothing
		  			
		  	}
    	}
    		
    }
    //--------------------------------------------------------------------------------
    public ArrayList getMaps(){
    	return mapList;
    }
    
    
}