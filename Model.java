
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author bmcdb
 */
public class Model implements Comparable<Model>{
    String metric;
    String name;
    double value;
    int rank;
    
    public Model(String name, String metric, double value){
        this.metric = metric;
        this.name = name;
        this.value = value;
        this.rank = 0;
    }
    
    public void setRank(int rank){
        this.rank = rank;
    }
    
    public double getValue(){
        return value;
    }
    
    public void printModel(){
        System.out.println("metric: " + metric + ", name: " + name + ", value: "+ value+ ", rank: " + rank);
    }
    
    @Override
    public int compareTo(Model m){
        return Double.compare(m.getValue(), getValue());
    }
    
//    public static void main(String[] args) throws FileNotFoundException, IOException{
//        
//        ArrayList<Model> models = new ArrayList<>();
//        String Directory = "C:\\Users\\bmcdb\\Dropbox\\_Master algemeen\\17-18 Q4\\Capita selecta\\Logs and models\\Road traffic fine management80\\trace models\\";
//        String filename = "test rankings.txt";
//        BufferedReader br = new BufferedReader(new FileReader(Directory + filename));
//        String line = "";
//        String csvSplitBy = ",";
//        
//        String currentMetric = "";
//        while ((line = br.readLine()) != null) {
//            String[] model = line.split(csvSplitBy);
//            models.add(new Model(model[0], model[1], Double.parseDouble(model[2])));
//        }
//        
//        Collections.sort(models);
//        
//        RankModels(models);
//        
//        
//        for (int i = 0; i < models.size(); i++){
//            models.get(i).printModel();
//        }
//        
//    }
}
