import json
from py2neo import neo4j, Graph, Path, Node, Relationship
from neo4jrestclient.client import GraphDatabase

neoDB = GraphDatabase("http://localhost:7474", username="neo4j", password="master")
modulList = []
relationshipList = []
nodeList = []
def readData():
    # with open("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/aimodules.graph", "r") as file:
    with open("D:\Uni\WP-NoSQL-Big-Data\Doc\\aimodules.graph", "r") as file:
        lines = []
        for line in file:
            lines.append(line)
    return lines

def createLabels():
    label = neoDB.labels.create("teeebu")
    return label

def createNodes():
    # label = createLabels()
    for module in readData():
        data = json.loads(module)

        modulename = data['Modulname']
        relationship = data['Vorraussetzung']
        node = Node("Module", modulename)

        nodeList.append(node)
        # m = neoDB.nodes.create(name=modulename)
        # label.add(m)
    print nodeList
print  createNodes()

# TODO: Vielleicht eine Klasse für Nodes, um die jeweiligen nodes speichern zu können um dann darauf zugreifen zu können!