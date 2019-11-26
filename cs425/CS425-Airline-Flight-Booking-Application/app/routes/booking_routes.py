from app import app
from app.flightBookerDB import db_interface
from app.routes import flights_routes
from flask import request
from app.auth import authUser


@app.route('/bookings/add', methods=['POST'])  # WORKING
def createBooking():
    customerId = authUser(request.headers)
    if not customerId:
        return "unauthorized", 401

    data = request.json
    route = data['route']
    routeClass = data['routeClass']
    cc = data['cc']
    address = data['address']

    if len(routeClass) != len(route):
        return {
            "Error": "Bad request. Supply a class for each flight."
        }, 400

    route = list(zip(route, routeClass))
    bookingId = db_interface.getLastBookingNumber() + 1

    db_interface.createBooking(bookingId, customerId, cc, address, route)

    return {
        'bookingId': bookingId,
        'result': 'Success'
    }, 200


'''
Only argument is Authentication token in header.
'''
@app.route('/bookings/get', methods=['GET'])
def getBooking(customerId=None):
    if not customerId:
        customerId = authUser(request.headers)
        if not customerId:
            return "unauthorized", 401

    bookings = db_interface.getBookings(customerId)
    output = {'bookings': []}
    for booking in bookings:
        output['bookings'].append(booking[0])
    return output



@app.route('/bookings/list', methods=['GET'])
def bookingInfo():
    customerId = authUser(request.headers)
    if not customerId:
        return "unauthorized", 401

    def getBookings():
        bookings = db_interface.getBookings(customerId)
        output = []
        for booking in bookings:
            output.append(booking[0])
        return output
    customerBookings = getBookings()

    def mapFxn(bid):
        info = db_interface.bookingInfo(bid)['flights']
        ret = []
        print("info: ", info, flush=True)
        for flight in info:
            del flight['bookingId']
            ret.append(flight)
        return ret

    customerBookings = dict(map(lambda bid: [ bid, mapFxn(bid) ], customerBookings))

    return {
        "data": customerBookings
    }, 200


@app.route('/bookings/delete', methods=['POST'])  # WORKING
def deleteBooking():
    customerId = authUser(request.headers)
    if not customerId:
        return "unauthorized", 401
    data = request.json
    bookingId = data['bookingId']
    if db_interface.deleteBooking(bookingId, customerId):
        return {
            "message": "Booking removed."
        }
    return {
        "message": "Failed to remove booking."
    }, 400
