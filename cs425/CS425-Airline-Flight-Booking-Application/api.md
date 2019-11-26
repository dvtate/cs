# API Refernece


Auth'd APIs are called with their user ID as an authentication field request headers. 

Backend APIs:

* Customer
	* Create Customer
	* Customer Address: Justin
		* Create
		* Delete
		* Get
	* Customer Payment: Justin
		* Create
		* Delete
		* Get
	* Authenticate Customer - Tate 
		* Sends customer ID as authentication token
	* Flight Bookings - Tian
		* Create Booking
		* Get Bookings
		* Delete Booking
* Search for Flights


CHECK (poorPrice < richPrice) 

# Documentation 

## **POST** `/booking/create` 
### Expecting post body like:
```json
{
	"flightIds": [
		34123412341234123,421341243132412,1434213412
	]
}
```

