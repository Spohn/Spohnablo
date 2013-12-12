/**
 * @(#)AStar.java
 *
 *
 * @author 
 * @version 1.00 2008/12/7
 */

import java.util.*;
import java.awt.Point;

public class AStar {
	
	String mapFileName,barberFileName;
	char[][] mapArray;
	int startX,startY,goalX,goalY,mapX,mapY;
	int timeStep;
	node_s successor, moveToNode;
	ArrayList openList, closedList;
	Point next;
	
    public AStar() {
    	
    }
    public Point findPath(Point start, Point end, SingleMap mapPoints){
    	successor = new node_s();
    	openList = new ArrayList();
    	closedList = new ArrayList();
		startX = (int)start.getX();
		startY = (int)start.getY();
		goalX = (int)end.getX();
		goalY = (int)end.getY();
		next = new Point();
		
		//Variables used within the main method
		//timeStep = 0;
		//BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		boolean cont = false, pathFound;
		moveToNode = new node_s();

		//Create the map
		CreateMap(mapPoints);
		
		//find the quickest path
    	pathFound = FindPath();
    	
		if(pathFound == true)
    		next = new Point(moveToNode.y,moveToNode.x);
    	else
    		next = new Point(-1,-1);
    		
    	//System.out.println(next);
    		
    	return next;
    }
    
    public void findNextSpace(node_s walker)
	{
		
		//walker = walker.parentNode;
		//moveToNode = walker;
		while(walker.parentNode != null){
			//System.out.println(walker.y + " | " + walker.x);
			//mapArray[walker.y][walker.x] = '.';
			moveToNode = walker;
			walker = walker.parentNode;

		}
		
		
		/*
		//while not the start node
		while(walker.parentNode != null){
			walker = walker.parentNode;
			//if next node is start node you win else stop at the next node and break
			if(walker.parentNode == null){
				//next node is the hero, move on to him
				moveToNode = walker;
				break;
			}
			else if(walker.parentNode.parentNode == null)
			{
				moveToNode = walker;
				break;
			}
			//System.out.println(moveToNode.x + " " + moveToNode.y);
			
		}
		*/
	}
	//----------------------------------------------------------------------------
	//Finds the path from the starting node to the goal node
	public boolean FindPath()
	{
		//variables
		node_s currentNode = new node_s();
		openList = new ArrayList();
		closedList = new ArrayList();
		
		node_s startNode = new node_s(startX,startY);
		
		openList.add(startNode);
		
		//while open list is not empty
		while(!(openList.isEmpty())){
			//set the current node and add to the closed list
			currentNode = listFindBest(openList);
			node_s temporary = listGet(openList,currentNode.x,currentNode.y,true);
			closedList.add(currentNode);
			
			//if we hit our goal node
			if(currentNode.x == goalX && currentNode.y == goalY){
				//System.out.println("we hit our goal node");
				//showBestPath(currentNode); //show the best path

				findNextSpace(currentNode); //find the next space move the start node
				
				return true;
			}
			else{ 
				//System.out.println("trying to hit goal node!!!!!!!!!!!!!!!!!!!!!!");
				int i;
				successor = new node_s();
				node_s temp = new node_s();
				
				//loop for each possible successor
				for(i = 0; i < 4; i++){
					//System.out.println("loop for each possivble successor");
					//set the successor node
					successor = findSuccessorNode(currentNode,i);
					//System.out.println(successor.x + " | " + successor.y);
					//if not null
					if(successor != null){
						//System.out.println("successor != null");
						//System.out.println(successor.x + " | " + successor.y);
						//calculate hueristics
						successor.h = calcH(successor);
						successor.g = currentNode.g + calcG(currentNode);
						successor.f = successor.g + successor.h;
						
						//if already in open list stop
						if(listPresent(openList,successor.x,successor.y)){
							temp = listGet(openList,successor.x,successor.y,false);
							if(temp.f < successor.f){
								successor = null;
								continue;
							}
						}
						//if already in closed list stop
						if(listPresent(closedList,successor.x,successor.y)){
							temp = listGet(closedList,successor.x,successor.y,false);
							if(temp.f < successor.f){
								successor = null;
								continue;
							}
						}
						//cleanup
						temp = listGet(openList,successor.x,successor.y,true);
						if(temp != null)
						{
							temp = null;
						}
						temp = listGet(closedList,successor.x,successor.y,true);
						if(temp != null)
						{
							temp = null;
						}
						//set the parent node of the successor
						successor.parentNode = currentNode;
						//add the successor to the open list
						openList.add(successor);
					}
				}
			}
		}
		//no possible path to the goal node
		//System.out.println("Solution not found.\n");
		return false;	
	}
	//----------------------------------------------------------------------------
	//finds the successor node to the current node
	public node_s findSuccessorNode(node_s currentNode, int i){
		
		//create a set of points to represent the way to check for successors
		//and store in an ArrayList
		point p1 = new point(0,-5);
		point p2 = new point(0,5);
		point p3 = new point(5,0);
		point p4 = new point(-5,0);
		ArrayList succ = new ArrayList();
		succ.add(p1);
		succ.add(p2);
		succ.add(p3);
		succ.add(p4);
		
		successor = null;
		int tempx,tempy;
		
		//determine where the successor is and make sure we do not go out of array bounds
		tempx = currentNode.x + ((point)succ.get(i)).x;
		if(tempx < 0)
			return successor;
		if(tempx >= 1040)
			return successor;
		tempy = currentNode.y + ((point)succ.get(i)).y;
		if(tempy < 0)
			return successor;
		if(tempy >= 780)
			return successor;
		
		//if map is not a wall or barber then we have a valid successor
		if(mapArray[tempy][tempx] != 'W'){
			successor = new node_s(tempx,tempy);
		}
			
		return successor;
	}
	//----------------------------------------------------------------------------
	//Calcs the hueristic H
	public double calcH( node_s node )
	{
  		double h;

 		h = (double)1.0 * (Math.abs(node.x - (double)goalX) + Math.abs(node.y - (double)goalY));

 		return h;
	}

	//----------------------------------------------------------------------------
	//Calcs the huertistic G
	public double calcG( node_s node )
	{
	  double g;
	
	  g = 1.0 + 0.5 * (node.g - 1.0);
	
	  return g;
	}
	//----------------------------------------------------------------------------
	//Finds the best node based on F value in a list by looping through the list and
	//comparing each value
	public node_s listFindBest(ArrayList al){
		int k = 0;
		double best = ((node_s)al.get(0)).f;
		for(int i = 1; i < al.size(); i++){
			if((((node_s)al.get(i)).f) < best){
				best = (((node_s)al.get(i)).f);
				k = i;
			}
		}
		
		return (node_s)al.get(k);
	}
    //----------------------------------------------------------------------------
    //determines if a node is already in a list
	public boolean listPresent( ArrayList list, int x, int y )
	{
	  int i;
	
	  for (i = 0 ; i < list.size() ; i++) {
	      if ((((node_s)list.get(i)).x == x) && ((((node_s)list.get(i)).y == y))) 
	      	return true;
	  }
	  return false;
	}
	//----------------------------------------------------------------------------
	//retrieves and sometimes removes a node from a list
	public node_s listGet( ArrayList list, int x, int y, boolean r )
	{
	  int i;
	  node_s node = new node_s();
	  node = null;
	
	  for (i = 0 ; i < list.size() ; i++) {
	      if ((((node_s)list.get(i)).x == x) && (((node_s)list.get(i)).y) == y) {
	        node = (node_s)list.get(i);
	        if (r) {
	          list.remove(i);
	        }
	        break; 
	      }
	  }
	  return node;
	}
 	//----------------------------------------------------------------------------
 	public void CreateMap(SingleMap mapList)
    {
    	mapArray = new char[785][1045];
   		for(int i = 0; i < mapList.wallPoints.size(); i++)
   		{
   				//System.out.println((int)((Point)((SingleMap)mapList.get(1)).wallPoints.get(i)).getY() + " " + (int)((Point)((SingleMap)mapList.get(1)).wallPoints.get(i)).getX());
   				mapArray[(int)((Point)mapList.wallPoints.get(i)).getY()][(int)((Point)mapList.wallPoints.get(i)).getX()] = 'W';
   			
   		}
   		//mapArray[startY][startX] = 'S';
   		//mapArray[goalX][goalY] = 'G';
    }
    
}