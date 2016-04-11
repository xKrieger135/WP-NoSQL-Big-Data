import json, redis
from flask import Flask, request, session, g, redirect, url_for, \
    abort, render_template, flash

class redis_db:

    redis_connection = redis.StrictRedis(host='localhost', port='6379', db=0)

    def readData(self):
        with open("/home/nosql/Downloads/plz.data", "r") as file:
            lines = []
            for line in file:
                lines.append(line)
        return lines

    def importDataToRedis(self):
        for object in self.readData():
            data = json.loads(object)
            key = data['_id']
            value = data['city']
            self.redis_connection.set(key, value)
            # rpush adds values to the same town
            self.redis_connection.rpush(value, key)

    def searchTownByPLZ(self, plz):
        town = self.redis_connection.get(plz)
        return town

    def searchPLZByTown(self, city):
        listWithPLZ = []
        for elem in self.redis_connection.lrange(city, 0, -1):
            listWithPLZ.append(elem)
        return listWithPLZ

    def show_entries(self):
        z = self.redis_connection.keys()
        entries = [dict(ID=item) for item in z]
        return render_template('entries.html', entries=entries)

app = Flask(__name__)

@app.route('/entries')
def show_database_entries():
    db = redis_db()
    return db.show_entries()

@app.route('/plz')
def show_city():
    db = redis_db()
    plz = request.args['plz']
    city = [dict(CITY=db.searchTownByPLZ(plz))]
    print city
    return render_template('entries.html', city=city)

@app.route('/city')
def show_plz():
    db = redis_db()
    city = request.args['city']
    plz = [dict(PLZ=db.searchPLZByTown(city))]
    return render_template("entries.html", plz=plz)

if __name__ == "__main__":
    app.run(debug = True)

