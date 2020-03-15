# -*- coding: utf-8 -*-
"""
@author: ramgautam
DATA SCIENCE

"""

import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
import sklearn.model_selection as sk
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, f1_score, precision_score, recall_score

import string
import csv
import os
import sys
import pathlib

#function to create the run file ---
def create_run_file(y_pred, death_row_name,filename):

    #opening the file to write
    #f = open("../../../runFiles/logRegression1.run", "w")

    f = open("../../../runFiles/" + filename + ".run", "w")
    #print(y_pred)

    for i in range(len(death_row_name)):
        #print (i+1 , " ", death_row_name[i], " ", y_pred[i])
        #f.write(str(i+1) + " " + str(*death_row_name[i]) + " " + str(*y_pred[i]) + "\n")

        death_char = str(*death_row_name[i]).replace(" ", "_")
        death_char = death_char.replace("-", "_")

        if y_pred[i] == 0:
            f.write("0" + " " + "0" + " " + death_char + " " + str(i+1) + " " +  "1" + " " + "team8"  + " " + "\n")


    f.close()


def main():

    count = 0
    while (count < 10):

        print("Creating Run File Based on Machine Learning Algorithms")

        subset = str(count)

        #taking the data file from here
        #if you reached up to here - the availability of all files must have been checked
        data_death="../../../common-group-data/characters_features_Death" + subset+ ".csv"
        features_death_df = pd.read_csv(data_death)

        #print(features_death_df.columns)
        #print(features_death_df.head(10))

        data_gender="../../../common-group-data/characters_features_Gender"+ subset + ".csv"
        features_gender_df = pd.read_csv(data_gender)

        #print(features_gender_df.columns)
        #print(features_gender_df.head(10))

        data_nobility="../../../common-group-data/characters_features_Nobility"+ subset + ".csv"
        features_nobility_df = pd.read_csv(data_nobility)

        #print(features_nobility_df.columns)
        #print(features_nobility_df.head(10))

        data_centra="../../../common-group-data/degree_of_centrality" + subset + ".csv"
        features_centra_df = pd.read_csv(data_centra)

        #print(features_centra_df.columns)
        #print(features_centra_df.head(10))

        character_features_df = pd.DataFrame ({'Name' : features_death_df['Name'],
                                        'Dead_Alive' : features_death_df['dead_alive'],
                                        'Gender' : features_gender_df['Gender'],
                                        'Nobility'  :features_nobility_df['Nobility'],
                                        'DegreeOfCentrality' :features_centra_df['DegreeofCent']})

        #print(character_features_df.columns)
        #print(character_features_df.head(5))

    #------------ COMPUTE LOGISTIC REGRESSION ---------
        X = character_features_df[['Gender', 'Nobility', 'DegreeOfCentrality','Dead_Alive']]
        y = character_features_df['Dead_Alive']

        #splitting the data into training and testing dataset - 70% and 30%
        X_train, X_test, y_train, y_test = sk.train_test_split(X, y, test_size = 0.3, random_state=30)

        #defining the logistic model
        logModel = LogisticRegression()

        #train the model
        logModel.fit(X_train, y_train)

        y_pred = logModel.predict(X)

        #converting it into row column
        #y_pred = y_pred.reshape((-1, 1))

        #predicted characters
        death_row_name = (character_features_df['Name'])
        death_row_name = death_row_name.values.reshape(-1,1)

        #get the file tag from here
        filename = "LogisticRegression" + subset
        create_run_file(y_pred, death_row_name,filename)

        #count = count + 1

    #---------- LOGISTIC REGRESSION END-----------

    #---------- SVM START -------------------
        from sklearn.svm import SVC

        SVM_model = SVC(gamma = "auto")
        SVM_model.fit(X_train, y_train)

        svm_pred = SVM_model.predict(X)
        #print(svm_pred)

        #get the file tag from here
        filename = "SVM" + subset
        create_run_file(svm_pred, death_row_name,filename)
    #---------- SVM END -----------------------


    #---------- Gaussian Naive Bayes START -----------
        from sklearn.naive_bayes import GaussianNB

        # Creating the Gaussian Model
        Gaus_Model= GaussianNB()

        # training the model
        Gaus_Model.fit(X_train, y_train)

        # predicting the model values
        gaus_pred = Gaus_Model.predict(X)

        #get the file tag from here and create the run file
        filename = "GaussianNaiveBayes" + subset
        create_run_file(gaus_pred, death_row_name,filename)
    #---------- Gaussian Naive Bayes END -----------


    #---------- Decision Tree  START  -----------
        from sklearn.tree import DecisionTreeClassifier

        # Creating the decision tree model
        decision_tree = DecisionTreeClassifier()

        # training the decision tree
        decision_tree.fit(X_train, y_train)

        # predicting the model values
        tree_pred = decision_tree.predict(X)

        #get the file tag from here and create the run file
        filename = "DecisionTree" + subset
        create_run_file(tree_pred, death_row_name,filename)

        count = count + 1
    #---------- Decision Tree END  -----------

if __name__ == "__main__":
    main()