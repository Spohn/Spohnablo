//-----------------------------------------------------------------
//Jon Spohn (Mob.java)
//This class will represent a Mob object. It will
//run a classifier to decide how the mob should act
//and it will gather in conditions from its surrounding
//environment and pick a action if one exist. If none exist it will
//do an action and try to decide how well that action performed
//to judge if that action was a good one or not
//-----------------------------------------------------------------

//Default Rules for every Mob
//Health Good | Mob Str Good | Hero Near | Hero Str Good | Hero Health   |  Exit near   ->   Attack | Flee | Wander | Go to waypoint
//     1            1             1             #               #               #       ->      1       0       0          0
//     0			#			  1				#				0				0 		-> 		0		1		0		   0
//	   #            #             #             #               #               1       ->      1       0       0          0 


import java.util.*;
import java.awt.Point;

public class Mob implements Runnable{
	
	Point p, exit;
	int health, str;
	ArrayList cList;
	SingleMap map;
	Random r = new Random();
	Hero hero;
	boolean combat;
	
    public Mob(int x, int y,SingleMap map,Hero h,Point e) {
    	this.p = new Point(x,y);
    	this.map = map;
    	this.health = 15;
    	this.hero = h;
    	this.combat = false;
    	this.exit = e;
    	this.str = 0;
    }
    public Mob(Mob m){
    	this.p = new Point(m.p);
    	this.health = 15;
    	this.map = map;
    	this.hero = m.hero;
    	this.combat = m.combat;
    	this.exit = m.exit;
    	this.str = m.str;
    }
    public void Combat(){
    	this.health -= 5;
    	combat = true;
    	if(this.health <= 0)
    	{
    		hero.str += 1;
    		hero.health += 15;
    	}
    	try {Thread.sleep(50);}
    	catch(InterruptedException e){}	
    	combat = false;
    }
    
    public void run(){ //Run the classifier for the mob
    	Point noPath = new Point(-1,-1);
    	int c1 = 0;
    	cList = new ArrayList();
    	
    	//create the first classifiers that are good ones
    	cList.add(new Classifier('1','1','1','#','#','#','1','0','0','0')); 
    	cList.add(new Classifier('0','#','1','#','0','0','0','1','0','0'));
    	cList.add(new Classifier('#','#','#','#','#','1','1','0','0','0'));
    	//create the actions 
    	ArrayList Wander = new ArrayList();
    	Wander.add('0');
    	Wander.add('0');
    	Wander.add('1');
    	Wander.add('0');
    	ArrayList Flee = new ArrayList();
    	Flee.add('0');
    	Flee.add('1');
    	Flee.add('0');
    	Flee.add('0');
    	ArrayList Attack = new ArrayList();
    	Attack.add('1');
    	Attack.add('0');
    	Attack.add('0');
    	Attack.add('0');
    	ArrayList Waypoint = new ArrayList();
    	Waypoint.add('0');
    	Waypoint.add('0');
    	Waypoint.add('0');
    	Waypoint.add('1');
    	
    	//create classifier population of random rules
    	for(int i = 0; i < 17; i++){
    		Classifier c = new Classifier();
    		cList.add(c);
    	}
    	
    	while(true){
    		while(health <= 0){
    			//wait for next level
    		}
    		
	    	//We want to generate the condition and see if that rule exist, if not make a rule for it.
		    ArrayList condition = new ArrayList();
		    
		    //What is my Health?---------
		    if(health >= 10){
		    	condition.add('1');
		    }
		    else if(health <= 5){
		    	condition.add('0');
		    }
		    else{
		    	condition.add('0');
		    }
		    	
		    //What is my str?------
		    if(str >= 2){
		    	condition.add('1'); //Mob is hardened veteran
		    }else{
		    	condition.add('0'); //mob is still weak
		    }
		    
		    //What is the average flying velocity of a swallow? (African or European)
		    
		    //Is the hero near?------
		    if(  Math.abs(hero.p.getX() - p.getX()) < 90  && Math.abs(hero.p.getY() - p.getY()) < 65 ){
		    	condition.add('1');
		    }
		    else{
		    	condition.add('0');
		    }
		    
		    //Is the Hero Str good?--------
		    if(hero.str >= 60){
		    	condition.add('1'); //hero is a hardened veteran
		    }else{
		    	condition.add('0'); //hero is still weak
		    }
		    
		    //What is the Hero's Health?--------
		    if(hero.health <= 50){
		    	condition.add('1');
		    }
		    else{
		    	condition.add('0');
		    }
		    
		    //Is the hero near the Exit?------
		    if(  Math.abs(hero.p.getX() - exit.getX()) < 120  && Math.abs(hero.p.getY() - exit.getY()) < 90 ){
		    	condition.add('1');
		    }
		    else{
		    	condition.add('0');
		    }
		    //create a AStar object
	    	AStar astar = new AStar();
	    	
	    	//match the condition generated to a classifier
	    	int cl = matchClassifiers(condition);
		    
		    double weight = 0.0;
	    	//Execute the action of the classifier matched to the condition
	    	//then consider condition and action -> results and determine rating for the action
	    	//Did I do something 'good'? given the condition was my action beneficial?
	    	
	    	//Wander
	    	if(    ((Classifier)cList.get(cl)).action.equals(Wander)    )
	    	{
	    		moveRandom();
	    		//Hero not near you, this is good
	    		if(!(Math.abs(hero.p.getX() - p.getX()) < 90  && Math.abs(hero.p.getY() - p.getY()) < 65 )){
	    			weight += 3.0;
	    		}
	    		else{
	    			weight -= 5.0;
	    		}
	    	}
	    	
	    	//Flee - Move away from the Hero ************
	    	else if(((Classifier)cList.get(cl)).action.equals(Flee) ){
	    		//Move in opposite direction of Hero
	    		int directionx = (int)hero.p.getX() - (int)p.getX();
	    		int directiony = (int)hero.p.getY() - (int)p.getY();
	    		
	    		moveRandom(directionx, directiony);

	    		//If health is high and your fleeing - BAD
	    		if(health > 6){
	    			weight -= 5.0; //bad decision
	    		}
	    		else{ //good decision
	    			weight += 2.0;
	    		}
	    		//If hero is not near you and mob is fleeing - BAD
	    		if(!(Math.abs(hero.p.getX() - p.getX()) < 90  && Math.abs(hero.p.getY() - p.getY()) < 65 )){
	    			weight -= 5.0;
	    		}
	    		else{ //good decision
	    			weight += 2.0;
	    		}
	    	}
	    	
	    	//Attack************
	    	else if(((Classifier)cList.get(cl)).action.equals(Attack) ){
	    		//Use A* to get to the Hero
	    		Point next = astar.findPath(p,hero.p,map);
	    		if(next.getX() == -1 && next.getY() == -1 || next.getX() == 0 && next.getY() == 0 ){
	    			//no path exist from mob to Hero, so do nothing
	    		}
	    		else{
	    			//path exist to Hero, move to the next position
	    			p.setLocation(next.getY(),next.getX());
	    		}
	    		
	    		//If you are far away from the hero and try to attack - BAD
	    		if(Math.abs(hero.p.getX() - p.getX()) > 500  && Math.abs(hero.p.getY() - p.getY()) > 500){
	    			weight -= 5.0;
	    		}
	    		else{
	    			weight += 5.0; //else good
	    		}
	    		//boost to increase attacking
	    		if(hero.str < 10 && str > 4){
	    			weight += 5.0;
	    		}		
	    		if(str > 5){
	    			weight += 2.0;
	    		}
	    		if(hero.str > 20){
	    			weight -= 1.0;
	    		}
	    	}
	    	//Go to Waypoint************ (waypoints removed... waypoint action will now just wander
	    	else if(((Classifier)cList.get(cl)).action.equals(Waypoint) ){
	    		
	    		//Testing Has revealed that waypoints are very taxing on the system in the current
	    		//way they are implemented. It takes too much time for a Mob to figure a way to 
	    		//a waypoint across this entire map. This is thusly an optional action
	    		
	    		//Use A* to move to Waypoint that is randomly generated
	    		/*Point way = randomWayPoint();
	    		Point next = astar.findPath(p,way,map);
	    		if( (next.getX() == -1 && next.getY() == -1) || (next.getX() == 0 && next.getY() == 0) ){
	    			//no path exist from mob to waypoint, so do nothing
	    			weight -= 5.0;
	    		}
	    		else{
	    			//path exist to waypoint, move to the next position
	    			p.setLocation(next.getY(),next.getX());
	    			weight += 5.0;
	    		}*/
	    		moveRandom();
	    		weight -= 1.0;
	    	}	    		
	    	
	    	//increase classifier's strength who participated with the episode by rating
	    	((Classifier)(cList.get(c1))).strength += weight;
	    	if(((Classifier)(cList.get(c1))).strength < 0.0){
    			((Classifier)(cList.get(c1))).strength = 0.0;
    		}
    		
    		//Sleep for a little to slow down movement
    		try {Thread.sleep(100);}
    		catch(InterruptedException e){}	
    	}
    }
    //-----------------------------------------------------------------------------------
    //Function to generate a random waypoint - either somewhere in middle or at one of the corners
    public Point randomWayPoint(){
    	ArrayList ways = new ArrayList();
    	Point p1 = new Point(100,100);
    	Point p2 = new Point(800,100);
    	Point p3 = new Point(100,600);
    	Point p4 = new Point(500,500);
    	Point p5 = new Point(600,600);
    	Point p6 = new Point(900,450);
    	Point p7 = new Point(400,450);
    	ways.add(p1);
    	ways.add(p2);
    	ways.add(p3);
    	ways.add(p4);
    	ways.add(p5);
    	ways.add(p6);
    	ways.add(p7);
    	int ra = r.nextInt(7);
    	return (Point)ways.get(ra);
    }
    //------------------------------------------------------------------------------------
    //Function to move mob in random direction
    //Pick a random spot to move too
    public void moveRandom(){
    	int i = r.nextInt(4);
    	if(i == 0)
    	{
    		//move down if no wall is there
    		if( !map.wallPoints.contains(new Point((int)p.getX(),(int)p.getY() + 5)) && (int)p.getY() + 5 <= 775)
    			p.setLocation((int)p.getX(),(int)p.getY() + 5);
    	}
    	else if(i == 1)
    	{
    		//move up if no wall is there
    		if( !map.wallPoints.contains(new Point((int)p.getX(),(int)p.getY() - 5)) && (int)p.getY() + 5 >= 10)
    			p.setLocation((int)p.getX(),(int)p.getY() - 5);
    	}
    	else if(i == 2)
    	{
    		//move right if no wall is there
    		if( !map.wallPoints.contains(new Point((int)p.getX() + 5,(int)p.getY())) && (int)p.getX() + 5 <= 1035)
    			p.setLocation((int)p.getX() + 5,(int)p.getY());
    	}
    	else if(i == 3)
    	{
    		//move left if no wall is there
    		if( !map.wallPoints.contains(new Point((int)p.getX() - 5,(int)p.getY())) && (int)p.getX() + 5 >= 10)
    			p.setLocation((int)p.getX() - 5,(int)p.getY());
    	}
    }
    //move randomly is a certain direction
    public void moveRandom(int x, int y){
    	if(x < 0){ //hero is below mob - move up
	   		if( !map.wallPoints.contains(new Point((int)p.getX(),(int)p.getY() - 5)) && (int)p.getY() + 5 >= 10)
    			p.setLocation((int)p.getX(),(int)p.getY() - 5);;
	   	}else{ //hero is above mob
	   		if( !map.wallPoints.contains(new Point((int)p.getX(),(int)p.getY() + 5)) && (int)p.getY() + 5 <= 775)
    			p.setLocation((int)p.getX(),(int)p.getY() + 5);
	   	}
	   	if(y < 0){ //hero is right mob
	    	if( !map.wallPoints.contains(new Point((int)p.getX() - 5,(int)p.getY())) && (int)p.getX() + 5 >= 10)
    			p.setLocation((int)p.getX() - 5,(int)p.getY());
	   	}else{ //hero is left mob
	    	if( !map.wallPoints.contains(new Point((int)p.getX() + 5,(int)p.getY())) && (int)p.getX() + 5 <= 1035)
    			p.setLocation((int)p.getX() + 5,(int)p.getY());
	   	}
    }
    //------------------------------------------------------------------------------------
    //function to find and return a random char of 0,1
    public char get01RandomCharacter(){
    	int n = r.nextInt(2);
    	char[] c = {'0','1'};
    	return c[n];
    }
    //------------------------------------------------------------------------------------
    //function to find and return a random char of 0,1,#
    public char getRandomCharacter(){
    	int n = r.nextInt(3);
    	char[] c = {'1','0','#'};
    	return c[n];
    }
    //------------------------------------------------------------------------------------
    //function to find and return a random char of 0,#
    public char getNot1RandomCharacter(){
    	int n = r.nextInt(2);
    	char[] c = {'0','#'};
    	return c[n];
    }
    //------------------------------------------------------------------------------------
    //Match classifiers
    public int matchClassifiers(ArrayList condition){
    	int c, i;
    	double bidSum = 0.0;
    	int matchFound = 0;
    	double bestBid = 0.0;
    	int loc = 0;
    	
    	for(c = 0; c < 20; c++){
    		((Classifier)cList.get(c)).match = 0;
    	
	    	for(i = 0; i < 6; i++){
	    		if(!match((Character)((Classifier)cList.get(c)).condition.get(i),(Character)condition.get(i) )){
	    			 break;
	    		}
	    	}
	    	
	    	if(i == 6){
	    		((Classifier)cList.get(c)).bid = 0.5 * ((Classifier)cList.get(c)).strength * ((Classifier)cList.get(c)).specificity;
	    		if(((Classifier)cList.get(c)).bid > bestBid){
	    			bestBid = ((Classifier)cList.get(c)).bid;
	    			loc = c;
	    		}
	    		matchFound = 1;
	    	}
    	}
    	
    	if(matchFound != 1){
    		loc = createCoveringClassifier(condition);
    	}
    	//slighlty increase the strength of a chosen classifier to have it avoid being overwritten
    	//before the day is complete by one of the others
    	//((Classifier)cList.get(loc)).strength += 0.1; 
    	
    	//return the location with the match with highest bid, or covering classifier
    	return loc; 
    }
    //------------------------------------------------------------------------------------
    //create a covering classifier if neccessary
    public int createCoveringClassifier(ArrayList condition){
    	int c, act, loc = 0;
    	double minStrength = 99.0;
    	
    	//find lowest strength classifer
    	for(c = 0; c < 20; c++){
    		if( ((Classifier)cList.get(c)).strength <= minStrength  ){
    			minStrength = ((Classifier)cList.get(c)).strength; 
    			loc = c;
    		}
    	}
    	
    	//replace lowest strength classifier with covering classifier
    	((Classifier)cList.get(loc)).condition = new ArrayList(condition);
    	((Classifier)cList.get(loc)).action = new ArrayList();	
    	int num = r.nextInt(4);
    	for(act = 0; act < 4; act++){
    		if(act == num){
    			((Classifier)cList.get(loc)).action.add('1');
    		}
    		else{
    			((Classifier)cList.get(loc)).action.add('0');
    		}
    	}
    	
    	((Classifier)cList.get(loc)).specificity = 0.0;
    	for(int i = 0; i < 6; i++){
    		if((Character)((Classifier)cList.get(loc)).condition.get(i) != '#'){
    			((Classifier)cList.get(loc)).specificity++;
    		}
    	}
    	((Classifier)cList.get(loc)).specificity /= 6.0;
    	
    	((Classifier)cList.get(loc)).strength = 1.0;
    	
    	//return location of the covering classifier
    	return loc; 
    }
    //------------------------------------------------------------------------------------
    public boolean match(char cl, char cn){
    	if      (cl == '#') return true;
    	else if (cl == cn) return true;
    	
    	return false;
    }
}