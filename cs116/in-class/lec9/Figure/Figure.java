package superclass;
public abstract class Figure{
	private int x;
	private int y;
	
	public Figure(){
		this.x=1;
		this.y=1;
	}
	
	public Figure(int xcor, int ycor){
		this.x=xcor;
		this.y=ycor;
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	
	public void setX(int xcor){
		this.x=xcor;
	}
	public void setY(int ycor){
		 this.y=ycor;
	}
	public double distance(int x1, int y1){
		double dist=0;
		dist=Math.sqrt((Math.pow((double)(this.x-x1),(double)2)+Math.pow((double)(this.y-y1),(double)2)));
		return dist;
	}
	public String toString(){
		String retString=new String();
		retString="toString() in Figure Class\n";
		retString = retString + "X Coordinate: " + this.getX() + "\n";
		retString = retString + "Y Coordinate: " + this.getY() + "\n";
		return retString;
	}
		//define abstract methods for
	public abstract double area();
	public abstract double perimeter();

};
