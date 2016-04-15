import json, yaml, demjson, ast
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
        return lines

    def get_import_data(self, data):
        result = []
        cleaned_data = ""
        for insert_data in data:
            insert_data_as_string = "".join(insert_data)
            i = 0
            while(i <= len(insert_data_as_string) - 1):
                if(insert_data_as_string[i] == "{"):
                    while(i != len(insert_data_as_string) - 3):
                        cleaned_data += insert_data_as_string[i]
                        i = i + 1
                i = i + 1
            result.append(cleaned_data)
        return result

    def import_data_to_mongodb(self):
        if(not "nosql" in self.database.collection_names()):
            self.database.create_collection("nosql")
        if(not "fussball" in self.database.collection_names()):
            self.database.create_collection("fussball")
        self.database.drop_collection("nosql")
        self.database.drop_collection("fussball")
        postalcode_collection = self.database.get_collection("nosql")
        fussball_collection = self.database.get_collection("fussball")

        if(postalcode_collection.full_name == "nosql"):
            for elem in self.read_data():
                data = json.loads(elem)
                postalcode_collection.insert(data)
        if(fussball_collection.name == "fussball"):
            for elem in self.get_import_data(self.read_data()):
                # To have automatically read from file and insert its neccessary to pull every data as an extra elem!
                # This is only for automation processes (read {...} from method from sinndeslebens.txt
                data = {'fussball_data' : elem}
                fussball_collection.insert(data)

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