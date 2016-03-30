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
        redis_connection.rpush(value, key)

def searchTown():
    town = redis_connection.get('01081')
    print "Die gesuchte Stadt ist: " + town
print searchTown()