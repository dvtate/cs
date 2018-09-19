public class Persons
{
	String firstName;
	String lastName;
	int age;
	static int id;
	int currentid;

	public Persons(){
	       this.firstName="John";
		   this.lastName="Doe";
		   this.age = 21;
	       Persons.id++;
	       this.currentid=id;
	}
	public Persons(String fName, String lName, int age){
	       this.firstName=fName;
		   this.lastName=lName;
		   this.age=age;
	       Persons.id++;
	       this.currentid=id;
	}
	//accessor methods
	public String getFirstName(){
		return this.firstName;
	}
	public String getLastName(){
		return this.lastName;
	}
	public int getAge(){
		return this.age;
	}
	public int getId(){
		return this.currentid;
	}
	public String toString()
	{
		return "Last name: " + this.getLastName() + " First name: " + this.getFirstName() + " Age: " + this.getAge();
	}
}
