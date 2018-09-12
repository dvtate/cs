


public class HousingAgent {
	public static void main(String args[]) {
		House house1 = new House(); // cardboardbox
		House house2 = new House(50, 999999.99, 10000.0); // mansion

		int rooms = house1.getRooms();
		System.out.println("h1 has " + rooms + "rooms");
		System.out.println("h1 cost $" + house1.getPrice());
		System.out.println("h2 has " + house2.getRooms() + "rooms");
		System.out.println("h1 cost $" + house1.getPrice());
	
		house1.setRooms(10);
		System.out.println("House1: " + house1);
		System.out.println("House2: " + house2);
		

		
	}

}
