package subclass;
import superclass.Figure;
public class Square extends Polygon{		
		
	public Square(int xcor, int ycor, double len){//
		super(xcor,ycor,len,4);
		
	}
	public double area(){// change the return to float
		double retVal=0;
		retVal=this.getLenght()*this.getLenght();
		return retVal;
	}
	public String toString(){
		String retString = "toString() in Square class \n";
		retString=retString + super.toString();		
		return retString;
	}
}