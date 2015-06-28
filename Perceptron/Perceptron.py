# -*- coding: utf-8 -*-
"""
Created on Mon Mar  2 20:26:13 2015

@author: MOS
"""

import numpy 
from pylab import *
# get_feature_space and get_feat_vects is adopted from KNN source code provided
# by the instructor
def get_feature_space(fname):
    A = set()
    with open(fname) as feat_file:
        for line in feat_file:
            for w in line.strip().split():
                A.add(w)
    return A

def get_feat_vects(fname, D, feat_index, label):
    feat_vects = []
    with open(fname) as feat_file:
        for line in feat_file:
            x = numpy.zeros(D, dtype=float)
            for w in line.strip().split():
                fid = feat_index[w]
                x[fid] = 1
            feat_vects.append((label,x))
    return feat_vects
# This function takes array of features and their labels,array of weights,bias value
# and iteration count. The first loop is to iterate the number of times we want the 
# the algorithm to run, the second loop is to iterate through entire features array.
def perceptronAlgorithmTraining(train_data,b,weights,iterationCount):
    a=0    
    for i in range(iterationCount):
        for j in range(len(train_data)):
            a=numpy.dot(weights,train_data[j][1])+b[0]
            if((train_data[j][0]*a<=0)):
                weights=numpy.add(weights,numpy.multiply(train_data[j][0],train_data[j][1]))
                b[0]=b[0]+train_data[j][0]
                
    return weights
# This function update the weights by running the perceptron algorithm "Iteration Count"
# number of times 
def updateWeights(trainingData,testingData,b,iterationCount,weights,mmrTrain,mmrTest,jumps,accuracyTrain,accuracyTest):
    iterations=0    
    for i in range(iterationCount):
        iterations+=1        
        weights=perceptronAlgorithmTraining(trainingData,b,weights,1)
        mmrTrain.append(perceptronAlgorithmTesting(weights,b,trainingData,len(trainingData),"Train",accuracyTrain,accuracyTest))
        mmrTest.append(perceptronAlgorithmTesting(weights,b,testingData,len(testingData),"Test",accuracyTrain,accuracyTest))
        jumps.append(iterations)
        
        
# this function plots the graph        
def plotGraph(jumps,Train,Test,label):
    plot(jumps,Train)
    plot(jumps,Test)
    ylabel(label)
    xlabel("Number of Iterations")
    title(" Blue = Train ; Green = Test;")
    show()
  # this function is the implementation of perceptron testing algorithm, in addition  
  # to the normal algorithm it also returns the mismatch rate and accuracy rate which
  # is later used for plotting
def perceptronAlgorithmTesting(weights,b,matrix,length,case,accuracyTrain,accuracyTest):
    matchCase=0
    mismatchCase=0    
    for i in range(length):
        a=numpy.dot(weights,matrix[i][1])+b[0]
        # if the multiplication results in a positive number, it means the provided label and the
        # calculated have the same sign indicating correct classification        
        if(matrix[i][0]*a>=0):
            matchCase+=1
        else:
            mismatchCase+=1

    totalCase=(matchCase+mismatchCase)*1.0
    print()
    print("---------------------------------")
    if(case=="Train"):
        print ("Training Status:")
        accuracyTrain.append((((matchCase*1.0)/(totalCase*1.0))*100.0))
    else:
        print ("Testing Status:")
        accuracyTest.append((((matchCase*1.0)/(totalCase*1.0))*100.0))
        
    print()
    print ("Match Percentage ",((((matchCase*1.0)/(totalCase*1.0))*100.0)),"%")
    print ("Mismatch Percentage ",((((mismatchCase*1.0)/(totalCase*1.0))*100.0)),"%")  
    print()
    
    return ((((mismatchCase*1.0)/(totalCase*1.0))*100.0))
    
if __name__ == "__main__":

    feat_space = get_feature_space("train.positive")
    feat_space = feat_space.union(get_feature_space("train.negative"))
    feat_space = feat_space.union(get_feature_space("test.positive"))
    feat_space = feat_space.union(get_feature_space("test.negative"))
    print ("Dimensionality of the feat space =", len(feat_space))
   
    D = len(feat_space)
    feat_space = list(feat_space)
    feat_index = {}
    for (fid, fval) in enumerate(feat_space):
        feat_index[fval] = fid
   
    trainingData = get_feat_vects("train.positive", D, feat_index, 1)
    trainingData.extend(get_feat_vects("train.negative", D, feat_index, -1)) 
    testingData = get_feat_vects("test.positive", D, feat_index, 1)
    testingData.extend(get_feat_vects("test.negative", D, feat_index, -1))
   
    b=[0]
    weights=numpy.zeros((D,), dtype=numpy.int)
    mmrTrain=[]
    jumps=[]
    iterations=0
    mmrTest=[]
    accuracyTest=[]
    accuracyTrain=[]
    updateWeights(trainingData,testingData,b,100,weights,mmrTrain,mmrTest,jumps,accuracyTrain,accuracyTest)

    plotGraph(jumps,mmrTrain,mmrTest,"Error Rate")
    plotGraph(jumps,accuracyTrain,accuracyTest,"Accuracy Rate")



    