#%% Carga de librerías.
import pandas as pd
from sklearn import preprocessing 
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt
import numpy as np
import sklearn.cluster
import distance


name = open("C:\\Users\\MIKEL1\\git\\CMap_creator_assistant\\CMap_creator_assistant\\forCluster.txt", "r")
names = name.readline()
words = names.split(" ")
words = np.asarray(words)
lev_similarity = -1*np.array([[distance.levenshtein(w1,w2) for w1 in words] for w2 in words])

affprop = sklearn.cluster.AffinityPropagation(affinity="precomputed", damping=0.75)
affprop.fit(lev_similarity)

fitx = open("C:\\Users\\MIKEL1\\git\\CMap_creator_assistant\\CMap_creator_assistant\\clusterDone.txt", 'w')


for cluster_id in np.unique(affprop.labels_):
    exemplar = words[affprop.cluster_centers_indices_[cluster_id]]
    cluster = np.unique(words[np.nonzero(affprop.labels_==cluster_id)])
    cluster_str = ", ".join(cluster)
    fitx.write("*%s*: %s \n" % (exemplar, cluster_str))

fitx.close()


'''
#%% Carga del dataframe.
df = pd.read_csv('C:\\Users\\MIKEL1\\Downloads\compound-word-splitter-master\\splitter\\output.txt', sep=" ", header=None, error_bad_lines=False)
print (df)'''