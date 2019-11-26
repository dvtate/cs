from faker import Faker
import random
from datetime import timedelta
import db_interface
faker = Faker()

# faker.date_time_between(start_date='now', end_date='+1y')
# Python datetime objects can be inserted directly into a postgres DB.

def gen_flight_timestamp():
    departTime = faker.date_time_between(start_date='now', end_date='+1y')
    endPoint = departTime + timedelta(days=1)
    arriveTime = faker.date_time_between(start_date=departTime, end_date=endPoint)
    return (departTime, arriveTime)

airports = [x[0] for x in db_interface.getAirports()]
airlines = [x[0] for x in db_interface.getAirlines()]
routes = []
# Route: (flightNumber, depart, arrive, economySeats, firstClassSeats)

for i in range(1000):
    flightNumber = i
    airlineId = random.choice(airlines)
    departAirportId = random.choice(airports)
    arriveAirportId = random.choice(airports)
    economySeats = random.choice([40, 80, 120, 240])
    firstClassSeats = random.choice([5, 10, 15, 20])
    routes.append((flightNumber, airlineId, departAirportId, arriveAirportId, flightNumber, economySeats, firstClassSeats))


for i in range(1000000):
    route = random.choice(routes)
    flightNumber, airlineId, departAirportId, arriveAirportId, flightNumber, economySeats, firstClassSeats = route
    (departTime, arriveTime) = gen_flight_timestamp()
    flightId = i
    db_interface.addFlight(flightId, airlineId, departAirportId, arriveAirportId, flightNumber, departTime, arriveTime, economySeats, firstClassSeats)
    if i % 1000 == 0:
        print(i)
