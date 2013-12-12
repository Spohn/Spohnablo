/**
 * @(#)Hero.java
 *
 *
 * @author 
 * @version 1.00 2008/12/7
 */

import java.util.*;
import java.awt.Point;

public class Hero {
	
	Point p;
	int health;
	int str;

    public Hero(int x, int y) {
    	p = new Point(x,y);
    	health = 150;
    	str = 0;
    }
    public void trapDamage(){
    	health -= 1;	
    }
    public void mobCombat(){
    	health -= 10;	
    }
    
    
}