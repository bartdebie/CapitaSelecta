
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bmcdb
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

    public static double RBO(ArrayList<Model> ranking1, ArrayList<Model> ranking2, double p){        
               
        ArrayList<String> temp1 = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();
        
        int i = 0;
        double RBO = 0.0;
        double w_dSum = 0.0;
        
        int maxDepth = Math.max(ranking1.size(), ranking2.size());
        
        while (i < maxDepth) {// extend to look at smallest set of models
            int d = i+1;
            double w_d = (1-p)*Math.pow(p, d-1);
            w_dSum += w_d;
            for (int j = 0; j < ranking1.size(); j++){
                if (ranking1.get(j).rank == d){
                    temp1.add(ranking1.get(j).name);
                }
            }
            for (int j = 0; j < ranking2.size(); j++){
                if (ranking2.get(j).rank == d){
                    temp2.add(ranking2.get(j).name);
                }
            }
            double agreement = 2.0*intersection(temp1, temp2).size()/(temp1.size()+temp2.size());
            RBO += w_d * agreement;
            i++;

        }
        
//        System.out.println("RBO: " + RBO);
//        System.out.println("Sum of weights: " + w_dSum);
//        System.out.println("Scaled RBO: " + RBO / w_dSum);
        return RBO / w_dSum;
    }
    
    
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