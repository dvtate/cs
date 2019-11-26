from app import app
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4250, threaded=False, processes=1, debug=True)
    #app.config['SECRET_KEY'] = 'hunter2'
