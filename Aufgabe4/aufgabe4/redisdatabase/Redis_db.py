import json, redis
from flask import Flask, request, session, g, redirect, url_for, \
    abort, render_template, flash

class Redis_db():

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
            if '_id' in data:
                del data['_id']
            value = data['city']
            self.redis_connection.set(key, data)
            self.redis_connection.rpush(value, key)


    def searchTownByPLZ(self, plz):
        # TODO: Try to find another solution for decoding bytes from redis to the correct JSON object!
        redis_data = self.redis_connection.get(plz)
        decoded_data = str(redis_data, 'utf-8')
        correct_decoded_data = decoded_data.replace("'", '"')
        decoded_json_data = json.dumps(correct_decoded_data)
        json_data = json.loads(decoded_json_data)
        correct_json_data = json.loads(json_data)
        results = []
        results.append(correct_json_data['city'])
        results.append(correct_json_data['state'])
        return results

    def searchPLZByTown(self, city):
        listWithPLZ = []
        for elem in self.redis_connection.lrange(city, 0, -1):
            listWithPLZ.append(elem)
        return listWithPLZ

    def show_entries(self):
        z = self.redis_connection.keys()
        entries = [dict(ID=item) for item in z]
        return render_template('index.html', entries=entries)