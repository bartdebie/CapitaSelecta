/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/


/**
 *
 * @author bmcdb
 */
public class GeneralizedKendallsTau {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] ranking1 = {1, 2, 3};
        int[] ranking2 = {3, 2, 1};
        double[] p_i = {1.0, 0.5, 0.75};
        
        double Tau = 0.0;
        
        for (int i = 0; i < ranking1.length; i++){
            for (int j = i+1; j < ranking1.length; j++){
                if((ranking1[i]>ranking1[j]&&ranking2[i]<ranking2[j]) || (ranking1[i]<ranking1[j]&&ranking2[i]>ranking2[j])){
                    Tau += 1.0*p_i[i]*p_i[j];
                }   
            }
        }
        
        //double tauB = (nrConcordant-nrDiscordant)/(Math.sqrt((nrConcordant+nrDiscordant+nrTiedX)*(nrConcordant+nrDiscordant+nrTiedY)));
        System.out.println(Tau);
        
    }
    
}
