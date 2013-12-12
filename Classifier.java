//--------------------------------------------------
//Jon Spohn (Classifier.java)
//This is the class used to represent an individual 
//classifier that a Mob object will use to determine
//its actions intelligently
//---------------------------------------------------


import java.util.*;

public class Classifier {

    Random r;
	double strength, specificity, bid;
	int match, episode;
	ArrayList condition, action;
	
	//-----------------------------------------------------------------------------------------------
	public Classifier(char a, char b, char c, char d, char e, char f, char g, char h, char i,char j){
		r = new Random();
    	condition = new ArrayList();
    	action = new ArrayList();
    	strength = 150.0;
    	episode = 0;
    	match = 0;
		
		//hardcode condition
		condition.add(a);
		condition.add(b);
		condition.add(c);
		condition.add(d);
		condition.add(e);
		condition.add(f);
		
		//hardcode action
		action.add(g);
		action.add(h);
		action.add(i);
		action.add(j);
		
		//get specificity
		doSpecificity();
	}
	//-----------------------------------------------------
    public Classifier() {
    	r = new Random();
    	condition = new ArrayList();
    	action = new ArrayList();
    	strength = 1.0;
    	episode = 0;
    	match = 0;
    	
    	//generate random condition
    	for(int cond = 0; cond < 6; cond++){
    		char c = getRandomCharacter();
    		condition.add(c);
    	}
    	
    	//generate random action
    	int num = r.nextInt(4);
    	for(int act = 0; act < 4; act++){
    		if(act == num){
    			action.add('1');
    		}
    		else{
    			action.add('0');
    		}
    	}
    	
    	//determine specificity
    	doSpecificity();
    }
    //--------------------------------------------------------
    //Function determines the specificity
    public void doSpecificity(){
    	specificity = 0.0;
    	for(int i = 0; i < 6; i++){
    		if((Character)condition.get(i) != '#'){
    			specificity++;
    		}
    	}
    	specificity /= 6.0;
    }
    //function to find and return a random char of 0,1,#
    public char getRandomCharacter(){
    	int n = r.nextInt(3);
    	char[] c = {'1','0','#'};
    	return c[n];
    }
    
    //function to find and return a random char of 0,#
    public char getNot1RandomCharacter(){
    	int n = r.nextInt(2);
    	char[] c = {'0','#'};
    	return c[n];
    }
    
    //function to find and return a random char of 0,1
    public char get01RandomCharacter(){
    	int n = r.nextInt(2);
    	char[] c = {'0','1'};
    	return c[n];
    }
    
    
}  
   