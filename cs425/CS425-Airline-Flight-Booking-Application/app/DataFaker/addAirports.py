import re
import db_interface

file = open("airports.csv")
airports_added = 0
try:
    for line in file:
        if airports_added <= 15:
            vals = line.split(",")
            airportId = vals[0].strip()
            airportCity = vals[1].strip()
            if len(vals[2].strip()) == 2:
                country = "United States"
                state = vals[2].strip()
            else:
                continue
            name = vals[3].strip()
            # print(airportId, name, country, state)
            if len(name) < 50:
                airports_added += 1
                db_interface.addAirport(airportId, name, country, state)
except Exception as E:
    print(E)
