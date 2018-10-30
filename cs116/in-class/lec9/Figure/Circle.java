package subclass;
import superclass.Figure;
public class Circle extends Figure{
	private double radius;
	
	public Circle(){
		this.radius=5; //
	}
	public Circle(int xcor, int ycor, double rad){//
		super(xcor,ycor);
		this.radius=rad;
	}
	public double getRadius(){
		return this.radius;
	}
	public double area(){// change the return to float
		double retVal=0;
		retVal=(22d/7d)*this.radius*this.radius;
		return retVal;
	}
	
	public double perimeter(){// change the return to float
		double retVal=0;
		retVal=(22d/7d)*2d*this.radius;
		return retVal;
	}
	public String toString(){
		String retString=super.toString();
		retString = retString + "toString() in Circle Class\n";
		retString = retString + "Radius: " + this.getRadius() + "\n";		
		return retString;
	}
}