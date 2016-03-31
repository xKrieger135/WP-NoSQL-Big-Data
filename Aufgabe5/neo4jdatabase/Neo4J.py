import json
from py2neo import Graph, Path
from neo4jrestclient.client import GraphDatabase

neoDB = GraphDatabase("http://localhost:7474", username="neo4j", password="master")
graph = Graph()
modulList = []
relationshipList = []

def readData():
    # with open("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/aimodules.graph", "r") as file:
    with open("D:\Uni\WP-NoSQL-Big-Data\Doc\\aimodules.graph", "r") as file:
        lines = []
        for line in file:
            lines.append(line)
    return lines

def createLabels():
    label = neoDB.labels.create("teeeeeghfgeset")
    return label

def createNodes():
    label = createLabels()
    c = graph.cypher.begin()
    for module in readData():
        data = json.loads(module)

        modulename = data['Modulname']
        relationship = data['Vorraussetzung']
        m = neoDB.nodes.create(name=modulename)
        label.add(m)

    for vorraussetzung in readData():
        data = json.loads(vorraussetzung)

        modul = data['Modulname']
        vorraussetzungen = data['Vorraussetzung']



print  createNodes()