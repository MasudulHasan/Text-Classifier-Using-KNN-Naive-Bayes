/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author masud
 */
public class Document {
    
        String S;
        String Topic;
        public HashMap<String, Integer> DataMap;
        public Document(String s,String topic) {
            //System.out.println("String "+s);
            this.S=s;
            this.Topic=topic;
            DataMap = new HashMap<String, Integer>();
            this.getHashMap();
        }
        
        public void getHashMap(){
            //System.out.println("here Working ");
            for (String retval: this.S.split(" ")) {
                //System.out.println(retval);
                if(DataMap.containsKey(retval)){
                    int Key=DataMap.get(retval);
                    DataMap.put(retval,Key+1);
                }
                else{
                    DataMap.put(retval,1);
                }
            }
            
        }
        
        public String getTopic(){
            return this.Topic;
        }
        
        public int getDistance(String s){
            //System.out.println("String Size "+s.length());
            
            //String[]parts=s.split(" ");
            HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
            for (String retval: s.split(" ")) {
                //System.out.println(retval);
                if(tempMap.containsKey(retval)){
                    int Key=tempMap.get(retval);
                    tempMap.put(retval,Key+1);
                }
                else{
                    tempMap.put(retval,1);
                }
            }
            int distance=0;
            Set set = tempMap.entrySet();
            Iterator I = set.iterator();
            while(I.hasNext()) {
                Map.Entry me = (Map.Entry)I.next();
                //System.out.print(me.getKey() + ": ");
                //System.out.println(me.getValue());
                if(!this.DataMap.containsKey(me.getKey())){
                    distance++;
                }
            }
//            for(int i=0;i<tempMap.size();i++){
//                if(!this.DataMap.containsKey(tempMap.get(i))){
//                    distance++;
//                }
//            }
//            System.out.println("Train String "+this.S);
//            System.out.println("String Size "+DataMap.size());
//            System.out.println("New String Size "+tempMap.size());
            Set set1 = DataMap.entrySet();
            Iterator I1 = set1.iterator();
            while(I1.hasNext()) {
                Map.Entry me = (Map.Entry)I1.next();
                //System.out.print(me.getKey() + ": ");
                //System.out.println(me.getValue());
                if(!tempMap.containsKey(me.getKey())){
                    distance++;
                }
            }
            //System.out.println("Distance "+distance);
            return distance;
            
            
        }
        
        
}
