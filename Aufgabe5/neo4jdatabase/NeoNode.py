from py2neo import neo4j, Graph, Path, Node, Relationship
from neo4jrestclient.client import GraphDatabase

class NeoNode():

    name = ""
    relationships = []

    def set_name(self, name):
        self.name = name

    def get_name(self):
        return self.name

    def set_relationship(self, relationship):
        self.relationships.append(relationship)

    def get_relationship(self, index):
        return self.relationships[index]

    def get_relationships(self):
        return self.relationships