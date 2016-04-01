import json
from py2neo import neo4j, Graph, Path, Node, Relationship
from neo4jrestclient.client import GraphDatabase

neoDB = GraphDatabase("http://localhost:7474", username="neo4j", password="master")
graph = Graph()
modulList = []
relationshipList = []
nodeList = []
def readData():
    # with open("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/aimodules.graph", "r") as file:
    with open("D:\GitHub\WP-NoSQL-Big-Data\Doc\\aimodules.graph", "r") as file:
        lines = []
        for line in file:
            lines.append(line)
    return lines

def createLabels():
    label = neoDB.labels.create("teeebu")
    return label

def createNodes():
    #TODO Vielleicht eine Klasse fuer Nodes, um die jeweiligen nodes speichern zu koennen um dann darauf zugreifen zu koennen
    label = createLabels()
    for module in readData():
        data = json.loads(module)

        modulename = data['Modulname']
        relationship = data['Vorraussetzung']
        node = Node("Module", modulename)

        nodeList.append(node)
        # m = neoDB.nodes.create(name=modulename)
        # label.add(node)

    for m in readData():
        data = json.loads(m)
        name = data['Modulname']
        rel = data['Vorraussetzung']
        print nodeList.__contains__(Node("Modulname", name))
        print "HI"
        for r in rel:
            print r
            x = nodeList.__getitem__(Node("Modulname", name))
            y = nodeList.__getitem__(Node("Modulname", r))
            Node.cast(x)
            relationship = Relationship(x, "needs", y)
            if(graph.__contains__(Node("Modulname", x))):
                graph.create(y)
                graph.create(relationship)
                print "Y"
            else:
                graph.create(x)
                graph.create(relationship)
                print "X"


    print nodeList.__contains__(Node("Module", "GI"))
    print nodeList
print  createNodes()

