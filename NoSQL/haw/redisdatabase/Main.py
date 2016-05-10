import json, redis
from flask import Flask, request, session, g, redirect, url_for, \
    abort, render_template, flash

from haw.mongodb.Mongo_DB import Mongo_DB
from haw.redisdatabase.Redis_db import Redis_db

redis_db = Redis_db()
mongo_db = Mongo_DB()

app = Flask(__name__)

@app.route('/entries')
def show_database_entries():
    return redis_db.show_entries()


@app.route('/redisdb/postalcode')
def show_city_and_state_with_redisdb():
    postalcode = request.args['postalcode']
    citys = dict()
    city = redis_db.search_city_by_postalcode(postalcode)
    citys.__setitem__("City", city[0])
    citys.__setitem__("State", city[1])
    city_and_state = [citys]
    return render_template('index.html', city=city_and_state)


@app.route('/redisdb/city')
def show_postalcode_with_redisdb():
    city = request.args['city']
    postalcodes = dict()
    list_with_postalcodes = []
    pc = redis_db.search_postalcode_by_city(city)
    # First put the decoded elements into a list, after that put them into the dict
    for elem in pc:
        decoded_pc = str(elem, "utf-8")
        list_with_postalcodes.append(decoded_pc)
    postalcodes.__setitem__("Postalcode", list_with_postalcodes)
    postalcode = [postalcodes]
    return render_template("index.html", postalcode=postalcode)

@app.route('/mongodb/postalcode')
def show_city_and_state_with_mongodb():
    postalcode = request.args['postalcode-mongodb']
    citys = dict()
    city_and_state = mongo_db.search_city_and_state_by_postalcode(postalcode)
    citys.__setitem__("City", city_and_state[0])
    citys.__setitem__("State", city_and_state[1])
    result = [citys]
    return render_template('index.html', city=result)

@app.route('/mongodb/city')
def show_postalcode_with_mongodb():
    city = request.args['city-mongodb']
    postalcodes = dict()
    list_with_postalcodes = []
    pc = mongo_db.search_postalcode_by_city(city)
    for elem in pc:
        list_with_postalcodes.append(elem)
    postalcodes.__setitem__("Postalcode", list_with_postalcodes)
    result = [postalcodes]
    return render_template('index.html', postalcode=result)

@app.route('/import')
def import_data():
    redis_db.import_data_to_redis()
    mongo_db.import_data_to_mongodb()
    return render_template('index.html')


if __name__ == "__main__":
    app.run(debug=True)