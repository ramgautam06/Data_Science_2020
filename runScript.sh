#!/bin/sh

cd Submission2

mvn clean compile assembly:single

java -Xmx50g -cp target/DataScience-1.0-SNAPSHOT-jar-with-dependencies.jar Main

cd src
cd main
cd java

python AddingDegreeofCentrality.py
python MachineLearningMethods.py

cd ..
cd ..
cd ..

cd runFiles


trec_eval -m set_F OLDdeath.qrels logRegression1.run
trec_eval -m set_F deaths.qrels logRegression1.run


echo "--- F1 SCORE: LOGISTIC REGRESSION START ---"
trec_eval -m set_F deaths.qrels LogisticRegression0.run
trec_eval -m set_F deaths.qrels LogisticRegression1.run
trec_eval -m set_F deaths.qrels LogisticRegression2.run
trec_eval -m set_F deaths.qrels LogisticRegression3.run
trec_eval -m set_F deaths.qrels LogisticRegression4.run
trec_eval -m set_F deaths.qrels LogisticRegression5.run
trec_eval -m set_F deaths.qrels LogisticRegression6.run
trec_eval -m set_F deaths.qrels LogisticRegression7.run
trec_eval -m set_F deaths.qrels LogisticRegression8.run
trec_eval -m set_F deaths.qrels LogisticRegression9.run
echo "--- F1 SCORE: LOGISTIC REGRESSION END ---"


echo "--- F1 SCORE: SVM START ---"
trec_eval -m set_F deaths.qrels SVM0.run
trec_eval -m set_F deaths.qrels SVM1.run
trec_eval -m set_F deaths.qrels SVM2.run
trec_eval -m set_F deaths.qrels SVM3.run
trec_eval -m set_F deaths.qrels SVM4.run
trec_eval -m set_F deaths.qrels SVM5.run
trec_eval -m set_F deaths.qrels SVM6.run
trec_eval -m set_F deaths.qrels SVM7.run
trec_eval -m set_F deaths.qrels SVM8.run
trec_eval -m set_F deaths.qrels SVM9.run
echo "--- F1 SCORE: SVM END ---"


echo "--- F1 SCORE: GaussianNaiveBayes START ---"
trec_eval -m set_F deaths.qrels GaussianNaiveBayes0.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes1.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes2.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes3.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes4.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes5.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes6.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes7.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes8.run
trec_eval -m set_F deaths.qrels GaussianNaiveBayes9.run
echo "--- F1 SCORE: GaussioanNaiveBayes END ---"


echo "--- F1 SCORE: Decision Tree START ---"
trec_eval -m set_F deaths.qrels DecisionTree0.run
trec_eval -m set_F deaths.qrels DecisionTree1.run
trec_eval -m set_F deaths.qrels DecisionTree2.run
trec_eval -m set_F deaths.qrels DecisionTree3.run
trec_eval -m set_F deaths.qrels DecisionTree4.run
trec_eval -m set_F deaths.qrels DecisionTree5.run
trec_eval -m set_F deaths.qrels DecisionTree6.run
trec_eval -m set_F deaths.qrels DecisionTree7.run
trec_eval -m set_F deaths.qrels DecisionTree8.run
trec_eval -m set_F deaths.qrels DecisionTree9.run
echo "--- F1 SCORE: Decision Tree END ---"

