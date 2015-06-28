
import numpy
from pylab import *
from scipy.spatial import distance
from numpy import linalg as LA

def get_feature_space(fname,classes):
    A = set()
    counter=0
    with open(fname) as feat_file:
        for line in feat_file:
            counter=0
            for w in line.strip().split():
                if "-" in w and counter==0 and w not in classes:
                    classes.append(w)
                A.add(w)
                counter+=1
    return A
# function to vactorize the input values (reviews)
# taken from KNN code provided in the lab session
def get_feat_vects(fname, D, feat_index, label):
    feat_vects = []
    with open(fname) as feat_file:
        for line in feat_file:
            x = numpy.zeros(D, dtype=float)
            for w in line.strip().split():
                fid = feat_index[w]
                x[fid] = 1.0
            feat_vects.append((label, x))
    return feat_vects
# l2 normalization
def normalizationOfVector(reviewMatrix):
    for i in range(len(reviewMatrix)):
        x=LA.norm(reviewMatrix[i][1], 2)*1.0
        reviewMatrix[i]=(reviewMatrix[i][0],numpy.divide(reviewMatrix[i][1],x))
# most appeared item in a cluster
def updateCluster(transposeMatrix):
    clusterLabels=[]
    for i in range(len(transposeMatrix)):
        clusterLabels.insert(i,argmax(transposeMatrix[i],axis=0))
    for i in range(len(clusterLabels)):
        for j in range(i+1,len(clusterLabels)):
            if(clusterLabels[i]==clusterLabels[j] and transposeMatrix[i][0]!=-1):
                transposeMatrix[i]=numpy.add(transposeMatrix[i],transposeMatrix[j])
                transposeMatrix[j]= -1
    return  transposeMatrix

def clusterEvaluation(reviewMatrix,classes,precisionValues,recallValues,feat_space):

    clusterReviewCounter=numpy.zeros(((k),), dtype=numpy.int)
    labelCounts=numpy.zeros(((len(classes)),), dtype=numpy.int)

    for i in range(len(classes)):
        for j in range(len(reviewMatrix)):
            if(reviewMatrix[j][1][feat_space.index(classes[i])]>0):
                precisionValues[i][reviewMatrix[j][0]]+=1
                clusterReviewCounter[reviewMatrix[j][0]]+=1
                labelCounts[i]+=1

    transposeMatrix=np.asarray(precisionValues)
    transposeMatrix=transposeMatrix.transpose()
    transposeMatrix=updateCluster(transposeMatrix)
    clusterLabels=[]

    # updating review counts after merging
    for i in range(len(transposeMatrix)):
        if(transposeMatrix[i][0]!=-1):
            clusterLabels.insert(i,argmax(transposeMatrix[i],axis=0))
        else:
            clusterLabels.insert(i,-1)
    precisionValues=[]
    recallValues=[]

    for i in range(len(clusterLabels)):
        if(clusterLabels[i]!=-1 and numpy.sum(transposeMatrix[i])>0):
           precisionValues.append(transposeMatrix[i][clusterLabels[i]]/(numpy.sum(transposeMatrix[i]))*1.0)
           recallValues.append(transposeMatrix[i][clusterLabels[i]]/(labelCounts[clusterLabels[i]])*1.0)

    return (precisionValues,recallValues)

def computeNewCentroids(reviewMatrix,k,iterationCount):
    Centroids=[]
    clusterReviewCounter=[]
    for i in range(k):
        Centroids.append(numpy.zeros((len(reviewMatrix[0][1]),), dtype=numpy.float))
        clusterReviewCounter.append(0.0)

    for j in range(len(reviewMatrix)):
        Centroids[reviewMatrix[j][0]]=numpy.add(reviewMatrix[j][1],Centroids[reviewMatrix[j][0]])
        clusterReviewCounter[(reviewMatrix[j][0])]+=1.0
    testCounter=clusterReviewCounter.count(0.0)

    for l in range(len(Centroids)):
        # Threshold
        #  each cluster must have at least (number of reviews)/ ((k)*number of classes) number of
        #  reviews assigned to it otherwise its considered as an outlier.
        #  In case of outlier return the code -1 which eventually restart the algorithm so that new good seeds
        #  are chosen as centroids.
        if(clusterReviewCounter[l]>=(len(reviewMatrix)/(k*8.0))and iterationCount==0):
            Centroids[l]=numpy.divide(Centroids[l],clusterReviewCounter[l])
        elif(clusterReviewCounter[l]>0 and iterationCount!=0):
            Centroids[l]=numpy.divide(Centroids[l],clusterReviewCounter[l])
        elif(clusterReviewCounter[l]==0 and iterationCount!=0):
             Centroids[l]= Centroids[l]
        # test counter represents how many clusters are empty initially
        elif((testCounter/(k*1.0))>0.10 and iterationCount==0):
            Centroids[l]=Centroids[l]
            print("Restarted with threshold condition")
            return -1
        else:
           # indicates that the initial random centroid was not good
            return -1

    return Centroids

def kMeans(reviewMatrix,k):
    centroids=[]
    # inserted initial k random centroids
    for i in range(k):
        centroids.append(reviewMatrix[randint(0,len(reviewMatrix))][1])
    reAssignments=1
    iterationCounter=0

    while(reAssignments>0):
        reAssignments=0
        for i in range(len(reviewMatrix)):
            distances=[]
            for j in range(len(centroids)):
                distances.append(distance.euclidean(reviewMatrix[i][1],centroids[j]))
            shortestDistance=distances.index(min(distances))
            if(reviewMatrix[i][0]!=shortestDistance):
                reAssignments+=1
                reviewMatrix[i]=(shortestDistance,reviewMatrix[i][1])
        centroids=computeNewCentroids(reviewMatrix,k,iterationCounter)
        if(centroids==-1):
            return -1
        iterationCounter+=1
        print ("Working on iteration number: ",iterationCounter)
    print()
    print("----------Status-----------")
    print()
    print ("Completed in ", iterationCounter, " Iterations for ",k," Clusters")
    print()
    print("---------------------------")
    return

def plotGraph(k,precision,recall,fScore):
    with plt.style.context('fivethirtyeight'):
        plt.plot(k, recall,  marker='o',color='r', label='recall')
    plt.xlabel('k')
    plt.title('Recall Values for K Means')
    plt.show()

    with plt.style.context('fivethirtyeight'):
        plt.plot(k, precision,  marker='o',color='y', label='precision')
    plt.xlabel('k')
    plt.title('Precision Values for K Means')
    plt.show()

    with plt.style.context('fivethirtyeight'):
        plt.plot(k, fScore,  marker='o',color='g', label='Fscore')
    plt.xlabel('k')
    plt.title('F-Score Values for K Means')
    plt.show()

    with plt.style.context('fivethirtyeight'):
        plt.plot(k, precision, marker='o', label='precision')
        plt.plot(k, recall,  marker='o', label='recall')
        plt.plot(k, fScore, marker='o',label='F-Score')
    plt.xlabel('k')
    plt.title('Precision / Recall / F-Score for K Means')
    plt.show()



if __name__ == "__main__":

    classes=[]
    feat_space = get_feature_space("data.txt",classes)
    print ("Dimensionality of the feat space =", len(feat_space))
    D = len(feat_space)
    feat_space = list(feat_space)
    feat_index = {}
    for (fid, fval) in enumerate(feat_space):
        feat_index[fval] = fid

    reviewMatrix = get_feat_vects("data.txt", D, feat_index,0)
    normalizationOfVector(reviewMatrix)
    k=2
    value=[]
    kValues=[]
    precisionValues=[]
    recallValues=[]
    fScoreValues=[]
    flag=-1
    for i in range(10):
        flag=-1
        while(flag==-1):
            print()
            print ("Starting for k = ",k," Clusters...")
            print()
            flag=kMeans(reviewMatrix,k)
            if(flag==-1):
                print()
                print("Restarted Algorithm due to bad seeds...")
                print()

        precisionArray=[[0 for x in range(k)] for x in range(len(classes))]
        recallArray=[[0 for x in range(k)] for x in range(len(classes))]
        output=clusterEvaluation(reviewMatrix,classes,precisionArray,recallArray,feat_space)
        precisionValues.append(numpy.sum(output[0])/(len(output[0]))*1.0)
        recallValues.append(numpy.sum(output[1])/(len(output[1]))*1.0)
        precision=output[0]
        precision=numpy.sum(precision)
        recall=numpy.sum(output[1])
        fScoreValues.append((2.0*(precision*recall))/(precision+recall)/(len(output[1]))*1.0)
        kValues.append(k)
        k+=2
    plotGraph(kValues,precisionValues,recallValues,fScoreValues)
