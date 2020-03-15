#!/usr/bin/env python3
import os.path
import csv
import numpy as np
import pandas as pd

import networkx as nx

#import matplotlib.pyplot as plt

#---------------------------
#initializing class
class FilePaths:
    def __init__(self,DeathFile,GenderFile, NobilityFile, NodeEdgeFile,character_features):
        self.DeathFile = DeathFile
        self.GenderFile = GenderFile
        self.NobilityFile = NobilityFile
        self.NodeEdgeFile = NodeEdgeFile
        self.characters_features = character_features


#-----------------------------
#method that computes the degree of centrality
def compute_degree_of_centrality(fileNodeEdge, graph, listName,subset):
    deg = nx.degree_centrality(graph)
    count = 0

    list_keys = []
    list_keys = deg.keys()

    #degree_centralityFile = "../../../common-group-data/degree_of_centrality.csv"
    degree_centralityFile = "../../../common-group-data/degree_of_centrality" + subset + ".csv"
    with open(degree_centralityFile, 'w') as file:
        writer = csv.writer(file)
        writer.writerow(["Name", "DegreeofCent"])

        for name in listName:
            value = 0
            for key in deg:
                if (key == name):
                    #count = count + 1
                    #print (count, " ", name ," :", deg[name])
                    value = deg[name]
                    break

            print (name , " ", value)
            count = count + 1
            #write degree of centrality value in characters_features_NodeEdge file
            writer.writerow([name, value])

    print("total count " , count)
    print("name counts needed ", len(listName))
    print(len(list_keys))


#method that for creating graph
def create_graph (fileNodeEdge, graph, subset):

    edgeList = []
    count = 0

    #opening the csv file
    nodeCount = 0
    listName = []

    with open(fileNodeEdge) as csvfile:
        readCSV = csv.reader(csvfile, delimiter=',')
        print("Able to open to CSV \n")

        for row in readCSV:

            edge= row[1]
            name = row [0]

            startNode = ""
            endNode = ""

            #if (count > 0) and (count < 10):
            if (count > 0):
                listName.append(name)

                #print (edge)
                edgeList = edge.split(')')

                #print each element of edge list
                for elem in edgeList:

                    if(len(elem) > 0):
                        pairs = elem.split('-')

                        #print("length of pairs " , len(pairs))

                        #looks like there is some unwanted braces ()

                        if(len(pairs)%2 == 0):

                            startNode = pairs[0]
                            startNode = startNode.replace('(', '')

                            endNode = pairs[1]

                            if(startNode.lower() != endNode):

                                #adding nodes to the graph here
                                graph.add_edge(startNode,endNode)

                            #print (startNode)
                            #print (endNode)

            count = count + 1

    #print(edgeList)
    #plotting the graph
    #nx.draw(graph, node_color = "Red",with_labels = True)
    #nx.draw_networkx_edges(graph, pos=nx.spring_layout(graph), node_color = "yellow",with_labels = True)
    #plt.savefig("../../../common-group-data/GOT_Network.png")
    #plt.show()

    #calling the function to measure centrality
    compute_degree_of_centrality(fileNodeEdge, graph, listName, subset)

    #graph = nx.balanced_tree(3, 5)
    #pos = graphviz_layout(graph, prog="twopi", args="")
    #plt.figure(figsize=(8, 8))
    #nx.draw(graph, pos, node_size=20, alpha=0.5, node_color="blue", with_labels=False)
    #plt.axis("equal")
    #plt.show()

#calling python file from main
def main():

    #the availability of all these files are checked in java other than features file
    #featuresFileGender = "../../../common-group-data/characters_features_Gender.csv"
    #featuresFileDeath = "../../../common-group-data/characters_features_Death.csv"
    #featuresFileNobility = "../../../common-group-data/characters_features_Nobility.csv"
    #featuresFileNodeEdge = "../../../common-group-data/characters_features_NodeEdge.csv"

    featuresFile = "../../../common-group-data/characters_features.csv"

    #checking if the features file exists
    if os.path.isfile(featuresFile):
        print ("File exist")
    else:
        print ("File does not exist")

        #opening new file
        try:
            featuresFile = open("../../../common-group-data/characters_features.csv", "w")

        except IOError:
            print("File not accessible")

        finally:
            featuresFile.close()


    count = 0
    while count < 10:
        subset = str(count)

        featuresFileGender = "../../../common-group-data/characters_features_Gender" + subset + ".csv"
        featuresFileDeath = "../../../common-group-data/characters_features_Death" +subset+".csv"
        featuresFileNobility = "../../../common-group-data/characters_features_Nobility" + subset + ".csv"
        featuresFileNodeEdge = "../../../common-group-data/characters_features_NodeEdge" + subset + ".csv"

        #initializing class with file paths
        files = FilePaths(featuresFileGender, featuresFileDeath, featuresFileNobility, featuresFileNodeEdge, featuresFile)

        #defining graph
        graph = nx.Graph()
        #graph.add_edge(1,2)

        create_graph (files.NodeEdgeFile, graph, subset)

        count = count + 1

    '''
    #initializing class with file paths
    files = FilePaths(featuresFileGender, featuresFileDeath, featuresFileNobility, featuresFileNodeEdge, featuresFile)

    #defining graph
    graph = nx.Graph()
    #graph.add_edge(1,2)

    create_graph (files.NodeEdgeFile, graph)
    '''

if __name__== "__main__":
    main()