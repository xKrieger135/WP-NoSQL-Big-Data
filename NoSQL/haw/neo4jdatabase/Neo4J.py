import json
from py2neo import neo4j, Graph, Path, Node, Relationship
from neo4jrestclient.client import GraphDatabase

neoDB = GraphDatabase("http://localhost:7474", username="neo4j", password="master")
nodes = []
rels = []

def readData():
    # with open("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/aimodules.graph", "r") as file:
    with open("D:\Uni\WP-NoSQL-Big-Data\Doc\\aimodules.graph", "r") as file:
        lines = []
        for line in file:
            lines.append(line)
    return lines

def create_nodes():
    for module in readData():
        data = json.loads(module)

        modulename = data['Modulname']
        relationship = data['Vorraussetzung']
        rels.append(relationship)
        n = neoDB.nodes.create(name=modulename)
        nodes.append(n)

    index_of_rels = 0
    for n in nodes:
        relationships = rels[index_of_rels]
        index_for_relationships = 0
        while (index_for_relationships !=  len(relationships) - 1):
            for node in nodes:
                actual_node = node.properties.values().__getitem__(0)
                if(relationships[index_for_relationships] == actual_node):
                    n.relationships.create("vorraussetzung", node)
                    if(len(relationships) - 1 == index_for_relationships):
                        break
                    else:
                        index_for_relationships = index_for_relationships + 1
        index_of_rels = index_of_rels + 1