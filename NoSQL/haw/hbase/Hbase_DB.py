# Not Possible at the Moment, becuase happybase framework does not completely works.
import json, requests

class Hbase_DB:

    def searchCityByPostalcode(self, postalcode):
        url = "http://localhost:8080/search/city/" + postalcode
        response = requests.get(url).json()
        print(response)

        city = dict()
        city.__setitem__("Postalcode", response['Postalcode'])
        city.__setitem__("City", response['City'])
        locations = response['Location']
        coordinates = []
        for k, v in locations.items():
            coordinates.append(float(k))
            coordinates.append(v)
        city.__setitem__("Location", coordinates)
        city.__setitem__("Population", response['Population'])
        city.__setitem__("State", response['State'])
        return city

    def searchPostalcodeByCity(self, city):
        url = "http://localhost:8080/search/postalcode/" + city
        response = requests.get(url).json()
        postalcodes = dict()
        postalcodes.__setitem__("Postalcodes", response['Postalcodes'])
        return postalcodes


