//-----------------------------------------------------------
//Jon Spohn (WallTemplates.java)
//This class will generate all the templates to be used in 
//the maps.
//------------------------------------------------------------

import java.util.*;
import java.awt.Point;

public class WallTemplates {
	
	ArrayList TemplateList;
	private int width, length;
	
    public WallTemplates(int w, int l) {
    	TemplateList = new ArrayList();
    	this.width = w;
    	this.length = l;
    	loadTemplates();
    }
    private void loadTemplates(){
    	//Template 1
    	ArrayList temp = new ArrayList();
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 10; i <= 70; i++){
    		temp.add(new Point(i,30));
    	}	
    	TemplateList.add(temp);
    	
    	//Template 2
    	temp = new ArrayList();
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 10; i <= 40; i++){
    		temp.add(new Point(i,10));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 3
    	temp = new ArrayList();
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(60,i));
    	}
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(20,i));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 4
    	temp = new ArrayList();
    	for(int i = 10; i <= 70; i++){
    		temp.add(new Point(i,30));
    	}
    	for(int i = 10; i <= 70; i++){
    		temp.add(new Point(i,10));
    	}
    	for(int i = 10; i <= 70; i++){
    		temp.add(new Point(i,50));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 5
    	temp = new ArrayList();
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 10; i <= 40; i++){
    		temp.add(new Point(i,10));
    	}
    	for(int i = 10; i <= 40; i++){
    		temp.add(new Point(i,50));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 6
    	temp = new ArrayList();
    	for(int i = 10; i <= 70; i++){
    		temp.add(new Point(i,10));
    	}
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(10,i));
    	}
    	for(int i = 10; i <= 50; i++){
    		temp.add(new Point(70,i));
    	}
    	TemplateList.add(new ArrayList(temp));
    	//-----------------------------------
    	//Template 1
    	temp = new ArrayList();
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 1; i <= 80; i++){
    		temp.add(new Point(i,30));
    	}	
    	TemplateList.add(temp);
    	
    	//Template 2
    	temp = new ArrayList();
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 1; i <= 40; i++){
    		temp.add(new Point(i,10));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 3
    	temp = new ArrayList();
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(60,i));
    	}
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(20,i));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 4
    	temp = new ArrayList();
    	for(int i = 1; i <= 80; i++){
    		temp.add(new Point(i,30));
    	}
    	for(int i = 1; i <= 80; i++){
    		temp.add(new Point(i,10));
    	}
    	for(int i = 1; i <= 80; i++){
    		temp.add(new Point(i,50));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 5
    	temp = new ArrayList();
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(40,i));
    	}
    	for(int i = 1; i <= 40; i++){
    		temp.add(new Point(i,10));
    	}
    	for(int i = 1; i <= 40; i++){
    		temp.add(new Point(i,50));
    	}
    	TemplateList.add(new ArrayList(temp));
    	
    	//Template 6
    	temp = new ArrayList();
    	for(int i = 1; i <= 80; i++){
    		temp.add(new Point(i,10));
    	}
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(10,i));
    	}
    	for(int i = 1; i <= 60; i++){
    		temp.add(new Point(70,i));
    	}
    	TemplateList.add(new ArrayList(temp));
    	//-----------------------------------
    	/*
    	//Template 7
    	temp = new ArrayList();
    	TemplateList.add(new ArrayList(temp));
    	//Template 8
    	temp = new ArrayList();
    	TemplateList.add(new ArrayList(temp));
    	//Template 9
    	temp = new ArrayList();
    	TemplateList.add(new ArrayList(temp));
    	//Template 10
    	temp = new ArrayList();
    	TemplateList.add(new ArrayList(temp));*/
    }
    
    
    
}