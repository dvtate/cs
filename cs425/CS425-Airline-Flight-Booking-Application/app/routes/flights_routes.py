from app import app
from app.flightBookerDB import db_interface
from flask import request
from datetime import datetime, timedelta
from app.auth import authUser
from dateutil.parser import parse as parse_date


# we could require authentication for these... but that just makes debugging harder

@app.route('/airports', methods=['GET'])  # Working
def getAirports():
    return {
        "airports": db_interface.getAirports()
    }

# Taking two airportIds:
def scoreFlight(route, departTime):
    # Time of Flight
    if not route:
        data = request.json
        route = data['route']
        departTime = datetime.strptime(data['departTime'], "%Y-%m-%d %H:%M:%S")

    flightTime = route[-1][6] - departTime
    # Cost of flight
    ePrice, fPrice = 0, 0
    for flight in route:
        ep, fp = db_interface.getFlightPrice(flight[0])[0]
        ePrice, fPrice = ePrice + float(ep), fPrice + float(fp)

    return (flightTime, ePrice, fPrice)

@app.route('/flights/search', methods=['GET', 'POST'])  # Working
def routeFlight():
    def routeFlight_rec(cur_airport, arriveTime, arriveAirportId, tokens=2, waitTime=1):
        if tokens == 0:
            return []
        latestDepart = arriveTime + timedelta(days=waitTime)
        subpaths = [[x] for x in db_interface.getFlights(cur_airport, arriveTime, latestDepart)]
        # For each possible flight that could be taken first:
        completed_routes = []
        # List of lists (routes)
        for subpath in subpaths:
            if subpath[0][3] == arriveAirportId:
                completed_routes.append(subpath)
            else:
                subsubpaths = routeFlight_rec(subpath[0][3], subpath[0][6], arriveAirportId, tokens-1, waitTime)
                # Subsubpaths is a list of routes, each leading to the destination.
                for subsubpath in subsubpaths:
                    try:
                        if subsubpath is not None:  # If the last entry in the subsubpath isn't an empty route:
                            completed_routes.append(subpath.copy() + subsubpath) # Then it must lead there
                    except TypeError as E:
                        print(E)
                        pass  # I don't think we should ever hit this.

        return completed_routes

    data = request.json
    departAirportId = data['departAirportId']  # Departing airport
    arriveAirportId = data['arriveAirportId']  # Target airport
    tokens = int(data['tokens'])  # Maximum number of transfers
    waitTime = float(data['waitTime'])  # Maximum number of days between flights

    departTime = parse_date(data['departTime']);

    routes = routeFlight_rec(
        departAirportId, departTime, arriveAirportId, tokens, waitTime
    )

    outputRoutes = {'unsortedRoutes': []}
    for route in routes:
        routeDataTuple = scoreFlight(route, departTime)
        routeData = {
            'routeTime': routeDataTuple[0].total_seconds(),
            'routeEcoCost': routeDataTuple[1],
            'routeFirstCost': routeDataTuple[2]
        }
        completeRoute = {
            'routeData': routeData,
            'route': route
        }
        outputRoutes['unsortedRoutes'].append(completeRoute)

    routeSpeed = {}
    routeEcoPrice = {}
    routeFirstPrice = {}

    for route in outputRoutes['unsortedRoutes']:
        routeSpeed[route['routeData']['routeTime']] = route
        routeEcoPrice[route['routeData']['routeEcoCost']] = route
        routeFirstPrice[route['routeData']['routeFirstCost']] = route

    speedSortedFlights = []
    for routeTime in sorted(routeSpeed.keys()):
        speedSortedFlights.append(routeSpeed[routeTime])

    ecoSortedFlights = []
    for price in sorted(routeEcoPrice.keys()):
        ecoSortedFlights.append(routeEcoPrice[price])

    firstSortedFlights = []
    for price in sorted(routeFirstPrice.keys()):
        firstSortedFlights.append(routeFirstPrice[price])

    # 4.3.1 - Skyline Query
    skylineFlights = []
    for testFlight in ecoSortedFlights:
        testPrice = testFlight['routeData']['routeEcoCost']
        testTime = testFlight['routeData']['routeTime']
        skyline = True
        for comparisonFlight in speedSortedFlights:
            comparisonTime = comparisonFlight['routeData']['routeTime']
            comparisonPrice = comparisonFlight['routeData']['routeTime']

            if (testPrice <= comparisonPrice) and (testTime <= comparisonTime):
                if (testPrice < comparisonPrice) or (testTime < comparisonTime):
                    continue
            skyline = False

        if skyline:
            skylineFlights.append(testFlight)



    outputRoutes["speedSortedFlights"] = speedSortedFlights
    outputRoutes["ecoSortedFlights"] = ecoSortedFlights
    outputRoutes["firstSortedFlights"] = firstSortedFlights
    outputRoutes["skylineFlights"] = skylineFlights

    return outputRoutes



@app.route("/flights/<flightId>")
def describeFlight(flightId):
    c = db_interface.conn.cursor()
    c.execute("SELECT airlineId, departAirportId, arriveAirportId, flightNumber, departTime, arriveTime, economySeats, firstClassSeats FROM flights WHERE flightId=%s", (flightId, ))
    r = c.fetchone()

    c.execute("SELECT economyPrice, firstClassPrice FROM prices WHERE flightId=%s \
    ORDER BY ABS(DATE(NOW()) - DATE(ts)) ASC;", (flightId,))
    p = c.fetchone();

    c.execute("SELECT name FROM airlines WHERE airlineId=%s", (r[0], ))
    alName = c.fetchone();

    c.execute("SELECT airportId, name FROM airports WHERE airportId = %s OR airportId = %s;", (r[1], r[2]));
    apName = c.fetchall()
    if len(apName) == 1:
        apName.append(apName[0])
    c.close()

    if not r:
        return 'not found', 404

    return {
        'airline' : r[0],
        'airlineName' : alName[0],
        'departAirport' : r[1],
        'departAirportName' : apName[0][1],
        'arriveAirport' : r[2],
        'arriveAirportName' : apName[1][1],
        'flightNumber' : r[3],
        'departTime' : r[4],
        'arriveTime' : r[5],
        'economySeats' : r[6],
        'firstClassSeats' : r[7],
        'economyPrice' : float(p[0]),
        'firstClassPrice' : float(p[1]),
    }, 200
