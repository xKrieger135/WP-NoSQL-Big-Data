# Not Possible at the Moment, becuase happybase framework does not completely works.
import json, requests

class HBase:

    def searchCityByPostalcode(self, postalcode):
        url = "http://localhost:8080/search/city/" + postalcode
        response = requests.get(url).json()
        json.dumps(response)
        print(response)
hbase = HBase()
print(hbase.searchCityByPostalcode("99950"))


