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
    # print importDataToRedis()

    def searchTownByPLZ(self, plz):
        town = self.redis_connection.get(plz)
        return town
    # print searchTownByPLZ()

    def searchPLZByTown(self):
        listWithPLZ = []
        for elem in self.redis_connection.lrange("HAMBURG", 0, -1):
            listWithPLZ.append(elem)
        print listWithPLZ
    # print searchPLZByTown()

    def show_entries(self):
        z = self.redis_connection.keys()
        entries = [dict(ID=item) for item in z]
        print entries
        return render_template('entries.html', entries=entries)

app = Flask(__name__)

@app.route('/entries')
def show_database_entries():
    db = redis_db()
    return db.show_entries()

# @app.route('/city')
# def show_city():
#     city = [dict(CITY=db.searchTownByPLZ(request.form['plz']))]
#     # db.searchTownByPLZ(request.form['plz'])
#     return render_template('entries.html', city=city)


if __name__ == "__main__":
    app.run(debug = True)

