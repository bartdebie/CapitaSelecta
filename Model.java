/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bmcdb
 */
public class Model {
    String metric;
    String name;
    int rank;
    
    public Model(String metric, String name, int rank){
        this.metric = metric;
        this.name = name;
        this.rank = rank;
    }
    
    public void printModel(){
        System.out.println("metric: " + metric + ", name: " + name + ", rank: " + rank);
    }
    
}
