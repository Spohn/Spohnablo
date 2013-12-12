//-------------------------------------------------------------
//Jon Spohn (node_s.java)
//This is our node class for the A* Porgram. When instantiated 
//it will represent a node.
//-------------------------------------------------------------

public class node_s{
	node_s parentNode; //Reference to the nodes parent
	double g,h,f;
	int x,y;
	public node_s()
	{
		g = h = f = 0;
		x = y = 0;
		parentNode = null;
	}
	public node_s(int x, int y){
		this.x = x;
		this.y = y;
		g = h = f = 0;
		parentNode = null;
	}
	public void setParentNode(node_s n){
		this.parentNode = n;
	}
}