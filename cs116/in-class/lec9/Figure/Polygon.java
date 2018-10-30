package subclass;
import superclass.Figure;
public abstract class Polygon extends Figure{
	private double lenght;
	private int noSides;
	
	public Polygon(){
		this.lenght=5;
		this.noSides=3;
	}
	
	public Polygon(int xcor, int ycor, double len, int s){//
		super(xcor,ycor);
		this.lenght=len;
		this.noSides=s;
	}
	public double getLenght(){
		return this.lenght;
	}
	
	public void setLenght(double len){
		this.lenght=len;
	}
	public int getSides(){
		return this.noSides;
	}
	public void setSides(int s){
		this.noSides=s;
	}
	public double perimeter(){// change the return to float
		double retVal=0;
		retVal=(double)this.lenght*(double)this.noSides;
		return retVal;
	}
	public String toString(){
		String retString=super.toString();
		retString=retString + "toString() in Polygon Class\n";
		retString = retString + "Length: " + this.getLenght() + "\n";
		retString = retString + "noSides: " + this.getSides() + "\n";
		return retString;
	}
}