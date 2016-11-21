/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static offline2.Offline2.DocumentsName;
//import static offline2.Offline2.Features;
//import static offline2.Offline2.NumberOfTopics;
//import static offline2.Offline2.TestDocumentProcessing;
//import static offline2.Offline2.TrainDocumentProcessing;
import static offline2.Offline2.parse;

/**
 *
 * @author masud
 */
public class KNN {
    ArrayList< ArrayList<String> > Topics = new ArrayList<ArrayList<String>>();
    ArrayList< Document > trainData = new ArrayList< Document >();
    //static ArrayList< HashMap<String, Integer> > trainData = new ArrayList< HashMap<String, Integer> >();
    ArrayList<Integer>wordNumber=new ArrayList<Integer>();
    ArrayList<String> testDocuments = new ArrayList<String>();
    ArrayList<String>DocumentsName=new ArrayList<String>();
    ArrayList<String>Features=new ArrayList<String>();
    ArrayList<String>TopicsName=new ArrayList<String>();
    int sizeOfData = 500;
    int NumberOfTopics;
    int totalTestNumber=0;
    int totalCorrect=0;
    //static int Kvalue=2;
    int startOfData=0;
    int EndofData=0;
    int startOftestData=0;
    int EndofTestData=0;
    
    public class Distance {
        String topicName;
        int dist;
        public Distance(String S,int n){
            this.dist=n;
            this.topicName=S;
        }
        
        public String getTopicName() {
            return this.topicName;
        }
        
        public int getDistance(){
            return this.dist;
        }
    }
    
    public void TrainDocumentProcessing(String files) throws FileNotFoundException, IOException {
        /*Test Document Processing */
            Features.add(files);
            //System.out.println(files);
            BufferedReader br = new BufferedReader(new FileReader("Training/"+files+".xml"));
            String line;
            try {
                line = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Offline2.class.getName()).log(Level.SEVERE, null, ex);
            }
            int count=0;
            ArrayList<String> documents = new ArrayList<String>();
            while((line = br.readLine()) !=null)
            {
                if(line.contains("<row"))
                {
                    //ParseString p = new ParseString(line);
                    if(count>=startOfData){
                        String parsed = parse(line);
                        //System.out.println(parsed);
                        documents.add(parsed);
                    }
                    count++;
                    if(count==EndofData)
                        break;
                }
//                for(int i=0;i<documents.size();i++){
//                    System.err.println(documents.get(i));
//                }

            }
            Topics.add(documents);
    }
    
    public void TestDocumentProcessing(String files) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("Test/"+files+".xml"));
            String line = br.readLine();
            int count=0;
            while((line = br.readLine()) !=null)
            {
                if(line.contains("<row"))
                {
                    //ParseString p = new ParseString(line);
                    if(count>=startOftestData){
                        String parsed = parse(line);
                        //System.out.println(parsed);
                        testDocuments.add(parsed);
                        DocumentsName.add(files);
                    }
                    count++;
                    if(count==EndofTestData)
                        break;
                }
            }
    }
    
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
				+ " Value : " + entry.getValue());
        }
    }
    
    public double getRow(int Kvalue,int start, int end,int teststart,int testend) throws FileNotFoundException, IOException{
        totalTestNumber=0;
        totalCorrect=0;
        startOfData=start;
        EndofData=end;
        startOftestData=teststart;
        EndofTestData=testend;
        BufferedReader topics = new BufferedReader(new FileReader("topics.txt"));
        String files;
        while((files = topics.readLine()) !=null)
        {
            //System.out.println("files "+files);
            TrainDocumentProcessing(files);
            TestDocumentProcessing(files);
        }
        NumberOfTopics = Topics.size();
        //System.out.println(NumberOfTopics);
        int Sum=0;
        for(int i=0;i<NumberOfTopics;i++)
        {
            ArrayList<String> temp = Topics.get(i);
            //System.out.println("Temp Size"+temp.size());
            for(int j=0;j<temp.size();j++){
                String tempString=temp.get(j);
                Document document=new Document(tempString,Features.get(i));
                trainData.add(document);
            }

        }
        
        
        for(int i=0;i<testDocuments.size();i++){
            String S=testDocuments.get(i);
            //System.out.println("Test String "+S);
            List<Distance> distances= new ArrayList<Distance>(); // Sorting
            for(int j=0;j<trainData.size();j++){
                Document temp=trainData.get(j);
                int Dist=temp.getDistance(S);
                Distance distClass;
                distClass = new Distance(temp.getTopic(),Dist);
                distances.add(distClass);
                //System.out.println("Dist "+Dist);
            }
            Collections.sort(distances, new Comparator<Distance>() {
                @Override
                public int compare(Distance obj1, Distance obj2)
                {
                    return obj1.getDistance() - obj2.getDistance();
                }
            });
            HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
            for(int I=0;I<Kvalue;I++){
                Distance distClass;
                distClass=distances.get(I);
                //System.out.print(distClass.getDistance()+" ");
                //System.out.println(distClass.getTopicName()+" ");
                if(tempMap.containsKey(distClass.getTopicName())){
                    int Key=tempMap.get(distClass.getTopicName());
                    tempMap.put(distClass.getTopicName(),Key+1);
                }
                else{
                    tempMap.put(distClass.getTopicName(),1);
                }
            
            
            }
            Map<String, Integer> treeMap = new TreeMap<String, Integer>(tempMap);
            Map.Entry<String,Integer> entry=treeMap.entrySet().iterator().next();
            String key= entry.getKey();
            //System.out.println("");
            totalTestNumber++;
            if(key.equals(DocumentsName.get(i)))totalCorrect++;
            //printMap(treeMap);
        }
        
        //System.out.println("TOTAL NUMBER "+totalTestNumber);
        //System.out.println("CORRECT "+totalCorrect);
        
        double Accuracy=(double)totalCorrect/totalTestNumber;
        System.out.println("K value "+Kvalue+ " Accuracy "+Accuracy);
        return Accuracy;
        
    }
}
