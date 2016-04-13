import json, redis
from flask import Flask, request, session, g, redirect, url_for, \
    abort, render_template, flash

from haw.redisdatabase.Redis_db import Redis_db

db = Redis_db()

app = Flask(__name__)

@app.route('/entries')
def show_database_entries():
    return db.show_entries()


@app.route('/postalcode')
def show_city_and_state():
    postalcode = request.args['postalcode']
    citys = dict()
    city = db.search_city_by_postalcode(postalcode)
    citys.__setitem__("City", city[0])
    citys.__setitem__("State", city[1])
    city_with_state = [citys]
    return render_template('index.html', city=city_with_state)


@app.route('/city')
def show_postalcode():
    city = request.args['city']
    postalcodes = dict()
    list_with_postalcodes = []
    pc = db.search_postalcode_by_city(city)
    # First put the decoded elements into a list, after that put them into the dict
    for elem in pc:
        decoded_pc = str(elem, "utf-8")
        list_with_postalcodes.append(decoded_pc)
    postalcodes.__setitem__("Postalcode", list_with_postalcodes)
    postalcode = [postalcodes]
    return render_template("index.html", postalcode=postalcode)


@app.route('/import')
def import_data():
    db.import_data_to_redis()
    return render_template('index.html')


if __name__ == "__main__":
    app.run(debug=True)
