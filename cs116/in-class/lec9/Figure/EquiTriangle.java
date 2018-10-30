package subclass;
import superclass.Figure;
public class EquiTriangle extends Polygon{
	
	public EquiTriangle(int xcor, int ycor, double len){//
		super(xcor,ycor,len,3);	
	}
	public double area(){// change the return to float
		double retVal=0;
		retVal=(Math.sqrt(3)/(double)4)*this.getLenght()*this.getLenght();
		return retVal;
	}
	public String toString(){
		String retString=super.toString();		
		return retString;
	}
}