import json
from py2neo import Graph, Path
from neo4jrestclient.client import GraphDatabase

neoDB = GraphDatabase("http://localhost:7474", username="neo4j", password="master")
modulList = []
relationshipList = []

def readData():
    with open("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/aimodules.graph", "r") as file:
        lines = []
        for line in file:
            lines.append(line)
    return lines

def createLabels():
    label = neoDB.labels.create("Modul")
    return label

def createNodes():
    i = 0
    for module in readData():
        data = json.loads(module)

        modulename = data['Modulname']
        modulList.append(modulename)
        print modulList
    print modulList
print  createNodes()

# def creategraph():
#     graph = Graph()
#     cipher = graph.cypher.begin()
#
#     for module in readData():
#         data = json.loads(module)
#
#         modul = data['Modulname']
#         vorraussetzung = data['Vorraussetzung']
#
#         cipher.append("CREATE (modul:Modul {modulname:{modul}})")
