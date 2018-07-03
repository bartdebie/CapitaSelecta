
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
        int metric = -1;
        ArrayList<ArrayList<Model>> models = new ArrayList<ArrayList<Model>>();
        while ((line = br.readLine()) != null) {
            String[] model = line.split(csvSplitBy);
            if (!(model[0].equals(currentMetric))){
                currentMetric = model[0];
                metric++;
                models.add(new ArrayList<Model>());
            }
            models.get(metric).add(new Model(model[0], model[1], Integer.parseInt(model[2])));            
        }

        // do RBO
        double p = 0.5;
        RBO(models.get(0), models.get(1), p);
        
        
    }

    public static void RBO(ArrayList<Model> ranking1, ArrayList<Model> ranking2, double p){        
               
        ArrayList<String> temp1 = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();
        
        int i = 0;
        double RBO = 0.0;
        double w_dSum = 0.0;
        
        if(ranking1.size()!=ranking2.size()){
            System.out.println("different size rankings!");
        }
        
        while (i < ranking2.size()) {// extend to look at smallest set of models
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
        
        System.out.println("RBO: " + RBO);
        System.out.println("Sum of weights: " + w_dSum);
        System.out.println("Scaled RBO: " + RBO / w_dSum);
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