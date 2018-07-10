
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author bmcdb
 * 
 * the RankBiasedOverlap class can be used to calculate the Rank-Biased Overlap between different rankings. 
 * 
 */
public class RankBiasedOverlap {

    public static void main(String[] args) throws FileNotFoundException, IOException{
        String filename = "C:\\Users\\bmcdb\\Dropbox\\_Master algemeen\\17-18 Q4\\Capita selecta\\testrankings.csv";
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
            models.get(metricNr).add(new Model(model[1], model[0], Integer.parseInt(model[2])));            
        }

        // do RBO
        double[][] RBOvalues = new double[models.size()][models.size()];
        double p = 0.5;
        for (int i = 0; i < models.size(); i++){
            ArrayList<Model> metric1 = models.get(i);
            for (int j = 0; j < models.size(); j++){
                ArrayList<Model> metric2 = models.get(j);
                RBOvalues[i][j] = RBO(metric1, metric2, p);
            }
        }
        
        DecimalFormat dm = new DecimalFormat("#.####"); 
        File file = new File("C:\\Users\\bmcdb\\Dropbox\\_Master algemeen\\17-18 Q4\\Capita selecta\\RBOvalues.txt");
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
            }
        }
                
        writer.close();
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
            
            // find the agreement between the two rankings at the current depth.
            double agreement = 2.0*intersection(temp1, temp2).size()/(temp1.size()+temp2.size());
            
            // add the agreement multiplied by the weight of the current depth to the RBO value.
            RBO += depthWeight * agreement;
            i++;
        }
        
//        System.out.println("RBO: " + RBO);
//        System.out.println("Sum of weights: " + w_dSum);
//        System.out.println("Scaled RBO: " + RBO / w_dSum);
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

    
}