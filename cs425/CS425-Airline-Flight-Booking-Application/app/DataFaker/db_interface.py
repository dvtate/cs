import psycopg2

conn = psycopg2.connect(dbname='flights425', user='postgres', password='space')
conn.autocommit = True

# don't forget to conn.close() and cursor.close()
def newConnection():
    return psycopg2.connect(dbname='flights425', user='postgres', password='space')


def checkEmail(email):  # Returns True if email not in DB, False if it is.
    c = conn.cursor()
    c.execute("SELECT email FROM customers WHERE email LIKE %s;", (email,))
    if c.fetchone():
        c.close()
        return False
    c.close()
    return True

def addCustomerAddress(customerId, line1, line2, postalCode, city, state, country):
    c = conn.cursor()
    c.execute("INSERT INTO customers VALUES (%s, %s, %s, %s, %s, %s, %s)", (
        customerId,
        line1,
        line2,
        postalCode,
        city,
        state,
        country)
    )
    c.close()
    return True


def getCustomerAddresses(customerId):
    c = conn.cursor()
    ret = list(c.execute("SELECT * FROM customerAddresses WHERE customerId = %s", customerId))
    c.close()
    return ret



def deleteCustomerAddress(addressId, customerId):
    c = conn.cursor()
    c.execute("DELETE FROM customerAddresses WHERE addressId = %s AND customerId = %s", (addressId, customerId))
    rows_deleted = c.rowcount
    return bool(rows_deleted)


def createBooking(flightId, customerId):
    c = conn.cursor()
    c.execute(" INSERT INTO bookings VALUE(%s,%s)", (
            customerId,
            flightId)
    )
    c.close()
    return True


def getBooking(customerId):
    c = conn.cursor().execute("SELECT * FROM bookings WHERE customerId = %s", customerId)
    ret = [x for x in c]
    c.close()
    return ret


def deleteBooking(bookingId):
    c = conn.cursor()
    c.execute("DELETE FROM bookings WHERE bookingId = %s", bookingId)
    rows_deleted = c.rowcount
    c.close()
    return bool(rows_deleted)


# Airports, Flights, Routing
def getAirports():
    c = conn.cursor()
    c.execute("SELECT * FROM airports")
    ret = [x for x in c]
    c.close()
    return ret

def getAirlines():
    c = conn.cursor()
    c.execute("SELECT * FROM airlines")
    ret = [x for x in c]
    c.close()
    return ret


# Get flights coming out of an airport:
def getFlights(departAirportId, departTime, latestDepart):
    print(departAirportId)
    # Finding within 1 hour
    c = conn.cursor()  # Not getting the date filtering right.
    c.execute("""
        SELECT *
        FROM flights
        WHERE departAirportId = %s
        AND departTime BETWEEN %s AND %s
        ORDER BY arriveTime ASC""", (departAirportId, departTime, latestDepart))

    ret = [x for x in c]
    print(ret)
    c.close()
    return ret

# Insert dummy data:

def addAirport(airportId, name, country, state):
    c = conn.cursor()
    c.execute("INSERT INTO airports VALUES (%s, %s, %s, %s)", (airportId, name, country, state))
    c.close()

def addAirline(airlineId, name, country):
    c = conn.cursor()
    c.execute("INSERT INTO airlines VALUES (%s, %s, %s)", (airlineId, name, country))
    c.close()

def addFlight(flightId, airlineId, departAirportId, arriveAirportId, flightNumber, departTime, arriveTime, economySeats, firstClassSeats):
    c = conn.cursor()
    c.execute("INSERT INTO flights VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)", (flightId, airlineId, departAirportId, arriveAirportId, flightNumber, departTime, arriveTime, economySeats, firstClassSeats))
    c.close()

def addPrice(flightId, economyPrice, firstClassPrice, ts):
    c = conn.cursor()
    c.execute("INSERT INTO prices VALUES (%s, %s, %s, %s)", (flightId, economyPrice, firstClassPrice, ts))
    c.close()
