public class Model implements Comparable<Model>{
    String metric;
    String name;
    double value;
    int rank;
    
/**
 * 
 * @param name model name
 * @param metric metric name
 * @param value precision value of the metric-model combination
 */    
    public Model(String name, String metric, double value){
        this.metric = metric;
        this.name = name;
        this.value = value;
        this.rank = 0;
    }
    
    /**
     * 
     * @param rank set the rank for the model-metric combination
     */
    public void setRank(int rank){
        this.rank = rank;
    }
    
    public double getValue(){
        return value;
    }
    
    /**
     * print a summary of the model-metric combination
     */
    public void printModel(){
        System.out.println("metric: " + metric + ", name: " + name + ", value: "+ value+ ", rank: " + rank);
    }
    
    @Override
    public int compareTo(Model m){
        return Double.compare(m.getValue(), getValue());
    }
}
