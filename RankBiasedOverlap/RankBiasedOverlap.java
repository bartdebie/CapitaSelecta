
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author bmcdb
 *
 * the RankBiasedOverlap class can be used to calculate the Rank-Biased Overlap between precision values from different metrics.
 *
 */
public class RankBiasedOverlap {
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        if (args.length != 3){
            System.out.println("Improper usage, check the Readme file on https://github.com/bartdebie/CapitaSelecta to see how to use this tool.");
            System.exit(0);
        }
        
        String filename = args[0];
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = "";
        String csvSplitBy = ",";
        
        String currentMetric = "";
        int metricNr = -1;
        ArrayList<ArrayList<Model>> models = new ArrayList<ArrayList<Model>>();
        while ((line = br.readLine()) != null) {
            String[] model = line.split(csvSplitBy);
            if (!(model[1].equals(currentMetric))){
                currentMetric = model[1];
                metricNr++;
                models.add(new ArrayList<Model>());
            }
            models.get(metricNr).add(new Model(model[0], model[1], Double.parseDouble(model[2])));
        }
       
        boolean bottomUpRanked = Boolean.parseBoolean(args[1]); // determines whether ranking is optimistic or pessimistic
        double p = Double.parseDouble(args[2]);
        
        // sort models and add rankings
        for (ArrayList<Model> modelList: models){
            Collections.sort(modelList);
            RankModels(modelList, bottomUpRanked);
        }
        
        // do RBO for every combination of metrics
        double[][] RBOvalues = new double[models.size()][models.size()];
        for (int i = 0; i < models.size(); i++){
            ArrayList<Model> metric1 = models.get(i);
            for (int j = 0; j < models.size(); j++){
                ArrayList<Model> metric2 = models.get(j);
                RBOvalues[i][j] = RBO(metric1, metric2, p);
            }
        }
        
        DecimalFormat dm = new DecimalFormat("#.####");
        File file = new File("RBOvalues.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        writer.write("RBOvalues\t");
        for (int i = 0; i < models.size(); i++){
            writer.write(models.get(i).get(0).metric + "\t");
        }
        
        for (int i = 0; i<RBOvalues.length; i++){
            writer.newLine();
            writer.write(models.get(i).get(0).metric+"\t");
            for (int j =0; j<RBOvalues.length; j++){
                writer.write(dm.format(RBOvalues[i][j])+"\t");
                writer.write(" \t");
            }
        }
        
        writer.close();
        System.out.println("Execution succesfully completed");
    }
    
    
    /**
     * @param ranking1 First ranking to compare, ArrayList of models
     * @param ranking2 Second ranking to compare, ArrayList of models
     * @param p between 0 and 1, the value for p in the Rank Biased Overlap calculation.
     * @return Rank-Biased Overlap between the two rankings.
     */
    public static double RBO(ArrayList<Model> ranking1, ArrayList<Model> ranking2, double p){
        
        // initialise temporary ArrayLists
        ArrayList<String> temp1 = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();
        
        int i = 0;
        double RBO = 0.0;
        double totalWeight = 0.0;
        
        int maxDepth = Math.max(ranking1.size(), ranking2.size());
        
        //loop until both rankings are fully explored
        while (i < maxDepth) {
            int depth = i+1;
            
            // calculate the weight associated with the current depth
            double depthWeight = (1-p)*Math.pow(p, depth-1);
            totalWeight += depthWeight;
            
            // for both rankings, check if new members exist at the current depth.
            for (int j = 0; j < ranking1.size(); j++){
                if (ranking1.get(j).rank == depth){
                    temp1.add(ranking1.get(j).name);
                }
            }
            for (int j = 0; j < ranking2.size(); j++){
                if (ranking2.get(j).rank == depth){
                    temp2.add(ranking2.get(j).name);
                }
            }
            
            double agreement;
            // find the agreement between the two rankings at the current depth.
            if (temp1.size()+temp2.size()>0){
                agreement = 2.0*intersection(temp1, temp2).size()/(temp1.size()+temp2.size());
            } else {
                agreement = 0.0;
            }
            // add the agreement multiplied by the weight of the current depth to the RBO value.
            RBO += depthWeight * agreement;
            i++;
        }
        
    return RBO / totalWeight;
    }
    
    
    /**
     * @param list1 first list in the comparison
     * @param list2 second list in the comparison
     * @return list of elements existing in both input lists
     */
    public static ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2){
        ArrayList<String> list = new ArrayList<>();
        for (String t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
    
    /**
     * 
     * @param models list of models to be ranked
     * @param bottomUpRanked determines whether the ranking is pessimistic or optimistic
     * bottomUpRanked == true: pessimistic ranking, ties are ranked at the lowest rank among them
     * bottomUpRanked == false: optimistic ranking, ties are ranked at the highest rank among them
     */
    public static void RankModels(ArrayList<Model> models, boolean bottomUpRanked){
        int i = 0;
        int j = 0;
        while (i < models.size()){
            boolean equalValue = true;
            while(equalValue){
                if (j<models.size() && models.get(i).getValue()==models.get(j).getValue()){
                    j++;
                } else {
                    equalValue = false;
                    for (int k = i; k < j; k++){
                        if (bottomUpRanked){
                            models.get(k).setRank(j);
                        } else {
                            models.get(k).setRank(i+1);
                        }
                    }
                    i = j;
                }
            }
        }
    }
}