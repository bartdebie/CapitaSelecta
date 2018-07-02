/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author bmcdb
 */
public class TestingCombination {
    
    public static void main(String[] args) {
        int[] ranking1 = {1, 2, 3, 4, 5};
        int[] ranking2 = {1, 2, 3, 4, 5};
        double[] p_i = {1.0, 0.5, 0.75}; //TODO: implement calculation of this based on ranking
        
        double nrConcordant = 0;
        double nrDiscordant = 0;
        double nrTiedX = 0;
        double nrTiedY = 0;
        double nrTiedXY = 0;
        
        for (int i = 0; i < ranking1.length; i++){
            for (int j = i+1; j < ranking1.length; j++){
                //rankings agree
                if ((ranking1[i]<ranking1[j] && ranking2[i]<ranking2[j]) || (ranking1[i]>ranking1[j] && ranking2[i]>ranking2[j])){
                    nrConcordant += p_i[i]*p_i[j];
                } //rankings disagree
                else if((ranking1[i]>ranking1[j]&&ranking2[i]<ranking2[j]) || (ranking1[i]<ranking1[j]&&ranking2[i]>ranking2[j])){
                    nrDiscordant += p_i[i]*p_i[j];
                } //rankings are both tied
                else if (ranking1[i]==ranking1[j] && ranking2[i]==ranking2[j]){
                    nrTiedXY += p_i[i]*p_i[j];
                } //ranking 1 is tied
                else if (ranking1[i]==ranking1[j] && ranking2[i]!=ranking2[j]){
                    nrTiedX += p_i[i]*p_i[j];
                } //ranking 2 is tied
                else if (ranking1[i]!=ranking1[j] && ranking2[i]==ranking2[j]){
                    nrTiedY += p_i[i]*p_i[j];
                } else {
                    System.out.println("should never happen");
                }
                
            }
        }
        
        double tauB = (nrConcordant-nrDiscordant)/(Math.sqrt((nrConcordant+nrDiscordant+nrTiedX)*(nrConcordant+nrDiscordant+nrTiedY)));
        System.out.println(tauB);
        
    }
}
