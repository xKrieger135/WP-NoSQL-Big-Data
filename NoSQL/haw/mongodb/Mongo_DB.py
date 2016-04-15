import json
from pymongo import MongoClient

class Mongo_DB():

    mongodb_client = MongoClient()
    database = mongodb_client.get_database("nosql")
    mongodb_collection = None

    def read_data(self):
        # with open("/home/nosql/Downloads/plz.data", "r") as file:
        with open("/home/nosql/Downloads/sinndeslebens.txt", "r") as file:
            lines = []
            for line in file:
                lines.append(line)
            self.get_import_data(lines)
        return lines

    def get_import_data(self, data):
        for insert_data in data:
            test = "".join(insert_data)
            i = 0
            x = ""
            while(i <= len(test) - 1):
                if(test[i] == "{"):
                    while(i != len(test) - 3):
                        x += test[i]
                        i = i + 1
                i = i + 1
            print(x)




    def import_data_to_mongodb(self):
        if(not "nosql" in self.database.collection_names()):
            self.mongodb_collection = self.database.create_collection("nosql")
        self.database.drop_collection("nosql")
        db_collection = self.database.get_collection("nosql")
        for elem in self.read_data():
            data = json.loads(elem)
            db_collection.insert(data)

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
mdb = Mongo_DB()
mdb.read_data()