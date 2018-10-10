package superclass;
public class CommunityMember{
   private static int counter=10000;//static variable
   public String ID; // ID is kept public for illustrating the inheritance of public members
   private String name;
   private int age;   
   private float salary;
   /*
   public CommunityMember(){//default constructor
	   this.name="no-name";	   
	   this.ID=""+this.counter;
	   this.counter++;
   }*/
   public CommunityMember(String name, int age, float salary){//non-default contructor
	   this.name=name;
	   this.age=age;
	   this.salary=salary;
	   this.ID="" + this.counter;
	   this.counter++;
   }
   //accessor for name
   public String getName(){
	   return this.name;
   }
   //accessor for age
   public int getAge(){
	   return this.age;
   }
   //accessor for salary
   public float getSalary(){
	   return this.salary;	   
   }
   public String getID(){ return this.ID; }
   
   //mutator for name
   public void setName(String name){
	   this.name=name;
   }
   //mutator for age
   public void setAge(int age){
	   this.age=age;
   }
   //mutator for salary
   public void setSalary(float salary){
	   this.salary=salary;
   }
   public String toString(){
	   String retString;
	   retString="==============================\n";
	   retString=retString + "Name: " + this.getName() +"\n";
	   retString=retString + "Name: " + this.ID +"\n";
	   retString=retString + "==============================";
	   return retString;
   }   
}
