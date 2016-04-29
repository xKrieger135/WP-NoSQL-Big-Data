import json
from pymongo import MongoClient

class Mongo_DB():

    mongodb_client = MongoClient()
    database = mongodb_client.get_database("nosql")
    mongodb_collection = None

    def read_data(self):
        with open("/home/nosql/Downloads/plz.data", "r") as file:
            lines = []
            for line in file:
                lines.append(line)
        return lines

    def import_data_to_mongodb(self):
        if(not "nosql" in self.database.collection_names()):
            self.database.create_collection("nosql")
        self.database.drop_collection("nosql")
        postalcode_collection = self.database.get_collection("nosql")

        for elem in self.read_data():
            data = json.loads(elem)
            postalcode_collection.insert(data)

    def search_city_and_state_by_postalcode(self, postalcode):
        results = []
        if("nosql" in self.database.collection_names()):
            db_collection = self.database.get_collection("nosql")
            data = db_collection.find_one({'_id' : postalcode})
            city = data['city']
            state = data['state']
            results.append(city)
            results.append(state)
        return results

    def search_postalcode_by_city(self, city):
        results = []
        if("nosql" in self.database.collection_names()):
            db_collection = self.database.get_collection("nosql")
            data = db_collection.find({'city' : city})
            for elem in data:
                postalcode = elem['_id']
                results.append(postalcode)
        return results