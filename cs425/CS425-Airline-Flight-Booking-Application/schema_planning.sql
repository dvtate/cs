


CREATE TABLE customers (
	customerId SERIAL PRIMARY KEY,
	email VARCHAR(50) UNIQUE NOT NULL,
	name VARCHAR(65) NOT NULL,
	password CHAR(128) NOT NULL,
	authToken VARCHAR(64) UNIQUE NOT NULL
);
CREATE TABLE customerAddresses (
	addressId BIGINT PRIMARY KEY;
	customerId BIGINT REFERENCES customers,
	line1 VARCHAR(120) NOT NULL,
	line2 VARCHAR(120) NOT NULL,
	postalCode CHAR(5) NOT NULL,
	city VARCHAR(85) NOT NULL,
	state VARCHAR(40) NOT NULL,
	country VARCHAR(45) NOT NULL
);

CREATE TABLE customerCreditCards (
	cardId BIGINT PRIMARY KEY,
	customerId BIGINT REFERENCES customers,
	addressId BIGINT REFERENCES customerAddresses,
	cardNumber VARCHAR(20) NOT NULL,
	expiration CHAR(5) NOT NULL,
	nameOnCard VARCHAR(40) NOT NULL,
	cvcCode CHAR(4) NOT NULL
);

CREATE TABLE airports (
	airportId CHAR(3) PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	country VARCHAR(35) NOT NULL,
	state VARCHAR(35)
);

CREATE TABLE airlines (
	airlineId CHAR(2) PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	country VARCHAR(35) NOT NULL
);

CREATE TABLE flights (
	flightId BIGINT PRIMARY KEY,
	airlineId CHAR(2) REFERENCES airlines,
	departAirportId CHAR(3) REFERENCES airports,
	arriveAirportId CHAR(3) REFERENCES airports,
	flightNumber INT NOT NULL,
	departTime TIMESTAMP NOT NULL,
	arriveTime TIMESTAMP NOT NULL,
	economySeats INT NOT NULL,
	firstClassSeats INT NOT NULL
	-- UNIQUE(flightDate, flightNumber) | Need to replace this with something.
);

CREATE TABLE prices (
	flightId BIGINT REFERENCES flights,
	economyPrice NUMERIC(8,2) NOT NULL,
	firstClassPrice NUMERIC(8,2) NOT NULL,
	ts TIMESTAMP NOT NULL,
	CHECK (economyPrice < firstClassPrice)
);

CREATE TABLE bookings (
	bookingId BIGINT PRIMARY KEY,
	customerId BIGINT REFERENCES customers,
	customerCreditCard BIGINT REFERENCES customerCreditCards,
	customerAddress BIGINT REFERENCES customerAddresses
);

CREATE TABLE bookingFlights (
	bookingId BIGINT REFERENCES bookings,
	flightId BIGINT REFERENCES flights,
	routeClass VARCHAR(7) CHECK (routeClass IN ('first', 'economy')),
	PRIMARY KEY(bookingId, flightId)
);
