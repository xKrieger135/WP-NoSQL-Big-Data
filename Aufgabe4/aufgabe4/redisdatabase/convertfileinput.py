import json, redis

redis_connection = redis.StrictRedis(host='localhost', port='6379', db=0)

def readData():
    with open("/home/nosql/Downloads/plz.data", "r") as file:
        lines = []
        for line in file:
            lines.append(line)
    return lines

def importDataToRedis():
    for object in readData():
        data = json.loads(object)
        key = data['_id']
        value = data['city']
        redis_connection.set(key, value)
        # rpush adds values to the same town
        redis_connection.rpush(value, key)
print importDataToRedis()

def searchTownByPLZ():
    town = redis_connection.get('07419')
    print "Die gesuchte Stadt ist: " + town
print searchTownByPLZ()

def searchPLZByTown():
    listWithPLZ = []
    for elem in redis_connection.lrange("HAMBURG", 0, -1):
        listWithPLZ.append(elem)
    print listWithPLZ
print searchPLZByTown()