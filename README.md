# CapitaSelecta
This repo contains the .java files for calculating the scaled RBO value between different rankings.
It also contains a RankBiasedOverlap.jar file which can be run from the command line.
The tool first creates a ranking of models for every metric based on the precision values and then calculates scaled RBO for every combination of metrics.


The .jar file requires three input arguments:
1) A csv file with model-metric combinations and their precision value, 
2) A true/false value which determines the way ties are handled.
3) A value for p in the RBO calculation, a double between 0 and 1.

The csv file has to be comma separated, three inputs per line (model name,metric name,precision value) in that order. For an example see input.csv.
The boolean determines the way ties are handled, true: ties are ranked at the lowest shared ranking, false: ties are ranked at the highest shared ranking. The recommended value is true.
The value for p determines how heavily the weights are slanted towards the upper elements of rankings.

Running the tool properly returns a RBOValues.txt file which contains a matrix of RBO values for all combinations of metrics in the input.csv file.

An example of running the tool:
java -jar RankBiasedOverlap.jar input.csv true 0.75