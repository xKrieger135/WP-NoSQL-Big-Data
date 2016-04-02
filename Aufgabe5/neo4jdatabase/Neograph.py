from py2neo import neo4j, Graph, Path, Node, Relationship
from neo4jrestclient.client import GraphDatabase

class NeoGraph():


    nodes = []

    def __init__(self):
        print "Successful Initialized!"

    def add_node(self, node):
        self.nodes.append(node)

    def get_all_nodes(self):
        return self.nodes

    def get_node(self, index):
        return self.nodes[index]

