/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package offline2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Masudul Hasan 
 */
public class DataFormation {
    
    ArrayList<String> data;
    HashMap<String, Integer> DataMap;
    
    DataFormation(ArrayList<String> data)
    {
        this.data = data;
        DataMap = new HashMap<String, Integer>();
    }
    
    HashMap<String, Integer> ReFormatData()
    {
        for(int i=0;i<data.size();i++){
            //System.out.println("I "+i+" "+data.get(i));
            String S=data.get(i);
            for (String retval: S.split(" ")) {
                //System.out.println(retval);
                if(DataMap.containsKey(retval)){
                    int Key=DataMap.get(retval);
                    DataMap.put(retval,Key+1);
                }
                else{
                    DataMap.put(retval,1);
                }
            }
//            System.err.println("\n\n\n\n");
//            Set set = DataMap.entrySet();
//            Iterator I = set.iterator();
//
//            // Display elements
//            while(I.hasNext()) {
//               Map.Entry me = (Map.Entry)I.next();
//               System.out.print(me.getKey() + ": ");
//               System.out.println(me.getValue());
//            }
//            System.out.println();

            
        }
        int Sum=0;
        Set set = DataMap.entrySet();
        Iterator I = set.iterator();
         while(I.hasNext()) {
           Map.Entry me = (Map.Entry)I.next();
           //System.out.print(me.getKey() + ": ");
           //System.out.println(me.getValue());
           Sum+=(int)me.getValue();
        }
        //System.err.println("Sum "+ Sum);
        //System.err.println("SIze: "+DataMap.size());
        return DataMap;
    }
}
