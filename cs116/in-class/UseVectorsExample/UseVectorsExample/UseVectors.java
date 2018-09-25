import java.util.*;
public class UseVectors
{
	public static void main(String[] args)
	{
		Vector v=new Vector();
		VehicleA car1=new VehicleA();
		VehicleA car2=new VehicleA();
		v.addElement(car1);
		v.addElement(car2);

		System.out.println("The first object out of the vector:"+" "+v.get(0).toString());

		Object o1=v.elementAt(0);
		VehicleA veh1=(VehicleA)o1;
		veh1.setAcceleration(2);
		veh1.setInitD(5.0);
		veh1.setInitV(2.0);
		veh1.setTime(1);
		veh1.calculateV();
		veh1.calculateD();

		System.out.println("The first object at index 0 out of the vector after we set its initial distance and initial velocity"+" "+v.get(0).toString());


		VehicleA v1=(VehicleA)v.get(1);
		v1.setAcceleration(3.0);
		v1.setTime(2);
		v1.calculateV();
		v1.calculateD();

		System.out.println("The second object at index 1 out of the vector after we set its initial distance and initial velocity"+" "+v.get(1).toString());

		//Now lat us exchage positions of teh two objects within teh vector
		v.set(0,v1);
		v.set(1,veh1);
       // Add a new object in the vector index 2

		  v.addElement(new VehicleA());
		 //iterate throughteh vector

		 for (int j=0; j<v.size(); j++)
		 {

			  System.out.println("Object at index"+" "+j+"is"+" "+v.get(j).toString());
		 }

	//task1: Add an object v2 of VehicleA whose intial velocity is 2, initial distance is 6 acceleration is 3 and time is 2.
	    VehicleA v2 = new VehicleA();
		v2.setAcceleration(3);
		v2.setInitV(2);
		v2.setInitD(6);
		v2.setTime(2);
	//task2: Add an object v3 of VehicleA whose initial velocity is 3, initial distance is 7 acceleration is 4 and time is 3.
		VehicleA v3 = new VehicleA();
		v3.setAcceleration(4);
		v3.setInitV(3);
		v3.setInitD(7);
		v3.setTime(3);
		//task3: For object v3, calculate the velocity and the distance.
		v3.calculateV();
		v3.calculateD();

		//task4: Enter the new objects v2 and v3 in the vector.
		v.add(v2);
		v.add(v3);


	//task5: Iterate through the vector and display the values of the attributes of each of the 5 objects in the vector.
		for (Object c : v)
			System.out.println(((VehicleA)c).toString());

	//task6: Remove the object at index 2 of the vector.
		v.remove(2);

	//task7: Display the size of the vector now (v.size())
		System.out.println(v.size());

	//task8: Clear the contents of the vector.
		v.removeAllElements();

	//task9: Display the size of the vector again.
		System.out.println(v.size());


	}
}
