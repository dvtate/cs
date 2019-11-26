from app import app
from flask import Flask, request, send_from_directory,redirect


@app.route('/web/<path:path>')
def static_route(path):
    return send_from_directory('../frontend', path)

@app.route('/')
def homepage():
    return redirect('/web/index.html')
