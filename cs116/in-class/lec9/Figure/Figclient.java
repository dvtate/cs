package client;
import superclass.Figure;
import subclass.Square;
import subclass.EquiTriangle;
import subclass.Circle;
import subclass.Polygon;
import java.util.Vector;
public class Figclient{

	public static void main(String []args){
		
		/*
			Task: Create a toString() method in Square class to print out the attributes x & y coordinates, length and noSides
			Create toString() methods in the superclass as needed
			The method should print out the class name 
			For example, the toString() method in polygon should print out "toString in Polygon class"			
		*/	
		/*
			Task: Similarly create a toString() for the Circle class
		*/
		
		/*
			Task: instantiate an object s1 of class square with the parameters (x=2,y=2,length=10) and print out the area and perimeter
		*/
		
		
		/*
			Task: instantiate an object t1 of class EquiTriangle with the parameters (x=2,y=2,length=10) and print out the area and perimeter
		*/
		
		/*
			Task: instantiate an object c1 of class Circle with the parameters (x=2,y=2,radius=10) and print out the area and perimeter
		*/
		
				
		/*Task: Put s1, t1 and c1 in a vector v1.	
		
		*/
		
		
		/*
		Task: Now use a for loop to grab elements from the vector and invoke the toString() method and print it out 
		*/
	
				
		/*
			We see that a subclass object can be assigned to a superclass reference (even if it is an abstract superclass)
			We used the superclass reference to call the toString() method. Which one of the overridden toString() method was invoked?
			
		*/
		
		/* Task: now you need to insert s1, t1 amd c1 into an array
			Remember that arrays can contain objects of only one type. How will you declare and instantiate this array?
			Print out the array by invoking the toString() method of  each object.
		*/
				
		/*
			Task: Can you use the subclass references such as Square to refer to other subclass objects such as EquiTriangle? Try. Declare and instantiate an array of type Square amd try by putting s1, t1 amd c1 into this array. Print out the array by invoking the toString() method of  each object. Does it work?
		*/
		
		
		
		/* Task: Fine! How about using an array of type Polygon to store s1 and t1. Polygon is a superclass of s1 and t1
		Can I also also store c1?
		*/	
	}
}