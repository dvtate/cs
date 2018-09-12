
public class House {
	public int rooms;
	public double price, area;
	
	// default constructor
	public House() {
		rooms = 1;
		price = 0.0;
		area = 0.0;
	}

	// 
	public House(int inrooms, double inprice, double inarea) {
		rooms = inrooms;
		price = inprice; 
		area = inarea;
	}
	
	// accessor methods
	public int getRooms() {
		return rooms;
	}
	public double getPrice() {
		return price;
	}
	public double getArea() {
		return area;
	}

	// mutilator methods
	public void setRooms(int newRooms) {
		rooms = newRooms;
	}
	public void setPrice(double newPrice) {
		price = newPrice;
	}
	public void setArea(double newArea) {
		area = newArea;
	}

	// convert to a string
	public String toString() {
		return "{ rooms: " + rooms 
				+ ", price: " + price 
				+ ", area: " + area + " }"; 
	}
	
};
