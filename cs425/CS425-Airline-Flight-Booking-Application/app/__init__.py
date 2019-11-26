from flask import Flask
from flask_cors import CORS

app = Flask(__name__, static_url_path='')
CORS(app)

# by default this is set to 12hrs or something stupid
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 0
from app.routes import booking_routes, customer_routes, flights_routes, static_routes
