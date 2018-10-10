package client;
import subclass.Faculty;
public class UniversityClient{
	
	public static void main(String [] args){
		System.out.println("FF");

		/* Task: create a faculty object using its default constructor*/
		Faculty janitor = new Faculty();
		
		/*Task: print out the department of faculty object (private attribute in Faculty)*/
		System.out.println(janitor.getAcademicDept());
		
		
		/* Task: Faculty inherits public attributes and methods of it superclass*/
        /* print out a public attribute of CommunityMember*/		
		System.out.println(janitor.ID);
		
		
		/*Task: Create a non-default Constructor for Faculty which takes in the dept name and an empty vector containing classes he is teaching*/
		/*Task: Create a Faculty object using the non-default Constructor*/
		Faculty mathsprof = new Faculty("math");
		
		
		/*Task: what is the ID of this object*/
		System.out.println(mathsprof.ID);
		
		/*Task: Add another non-default constructor for the Faculty class to take name, age and salary. Force it to use the non-default constructor of community member*/
		/*Task: create a faculty object using this non-default constructor*/
		Faculty owner = new Faculty("John Smith", 65, 2938231.25f);
		
		/*Task: Print out the name attribute (private attribute in CommunityMember) of this object*/
		System.out.println(owner.getName());
		
		/*Task: make ID protected in CommunityMember. Can you directly access and print out the value of ID from the client class (which is in a different package)?*/
		
		/*Task: Now Faculty member's ID has to start with a letter "F", change the Faculty class constructor appropriately/
		
		/*Task: Create an accessor method for ID in CommunityMember and print it out*/
		
		/*Task: Create a toString() method for Faculty class to print out its attributes*/

		/*Task: Comment out the default contructor in CommunityMember. Does it work? Now modify the Faculty class to make it work*/
		
		
	}
}
