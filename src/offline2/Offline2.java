/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package offline2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Masudul Hasan
 */
public class Offline2 {

    /**
     * @param args the command line arguments
     */
    static ArrayList< ArrayList<String> > Topics = new ArrayList<ArrayList<String>>();
    //static ArrayList< ArrayList<String> > testData = new ArrayList<ArrayList<String>>();
    static ArrayList< HashMap<String, Integer> > trainData = new ArrayList< HashMap<String, Integer> >();
    static ArrayList<Integer>wordNumber=new ArrayList<Integer>();
    static ArrayList<String> testDocuments = new ArrayList<String>();
    static ArrayList<String>DocumentsName=new ArrayList<String>();
    static ArrayList<String>Features=new ArrayList<String>();
    static ArrayList<String>StopWord=new ArrayList<String>();
    static int sizeOfData = 500;
    static int startOfData=0;
    static int EndofData=100;
    static int startOftestData=0;
    static int EndofTestData=50;
    static int NumberOfTopics;
    static int totalTestNumber=0;
    static int totalCorrect=0;
    
    public static String parse(String S) throws IOException
    {
        String tempString="";
        int index = S.indexOf("Body=\"");
        int end = S.indexOf("OwnerUserId");
        for(int i=index+15;i<end-18;i++)
        {
            char ch = S.charAt(i);
            if(!(ch=='.'||ch==','||ch==';'||ch=='?'||ch=='('||ch==')'||(ch>='0' && ch<='9')))tempString += S.charAt(i);
        }
        Document document = Jsoup.parse(tempString);
        tempString = extractString(document.text());
        tempString = tempString.toLowerCase();
        tempString = removeStopWord(tempString);
        return tempString;
    }
    public static String removeStopWord(String S) throws FileNotFoundException, IOException
    {
        BufferedReader topics = new BufferedReader(new FileReader("stopword.txt"));
        String files;
        while((files = topics.readLine()) !=null)
        {
            StopWord.add(files);
        }
        String str[] = S.split(" ");
        String tempString = "";
        for(int i=0;i<str.length;i++)
        {
//            if(!(str[i].matches("then||he||his||she||we||us||her||am||is||are||i||i'm||i'd||i've||me||my||you||our||they||their||them||than||was||were||have||had||do||did||does||done||don't||doesn't||didn't||not||no||be||been||can||could||will||would||won't||shall||should||may||might||some||just||new||it||its||it's||this||that||those||there||here||a||an||the||or||and||but||when||where||which||why||how||what||while||between||both||to||of||in||over||also||only||often||any||with||by||if||at||as||on||for||from||up||about")))
//            {
//                tempString += str[i]+" ";
//            }
            if(!StopWord.contains(str[i]))tempString += str[i]+" ";
            
        }
        return tempString;
    }
    public static String extractString(String S)
    {
        String str[] = S.split(" ");
        String st="";
        for(int i=0;i<str.length;i++)
        {
            if(!str[i].startsWith("&")&&(!str[i].matches("href.*") && !str[i].matches("rel.*") && !str[i].matches("alt.*") && !str[i].matches("src.*") && !str[i].matches("class.*") && !str[i].matches("title.*") && str[i].length()>1))
            {
                String tempString = str[i].replaceAll("<p>"," "); tempString = tempString.replaceAll("</p>"," ");
                tempString = tempString.replaceAll("<sub>"," "); tempString = tempString.replaceAll("</sub>"," ");
                tempString = tempString.replaceAll("<s>"," "); tempString = tempString.replaceAll("</s>"," ");
                tempString = tempString.replaceAll("<ol>"," "); tempString = tempString.replaceAll("</ol>"," ");
                tempString = tempString.replaceAll("<li>"," "); tempString = tempString.replaceAll("</li>"," ");
                tempString = tempString.replaceAll("<em>"," "); tempString = tempString.replaceAll("</em>"," ");
                tempString = tempString.replaceAll("<code>"," ");   tempString = tempString.replaceAll("</code>"," ");
                tempString = tempString.replaceAll("<pre>"," ");    tempString = tempString.replaceAll("</pre>"," ");
                tempString = tempString.replaceAll("<h2>"," ");    tempString = tempString.replaceAll("</h2>"," ");
                tempString = tempString.replaceAll("<h3>"," ");    tempString = tempString.replaceAll("</h3>"," ");
                tempString = tempString.replaceAll("<a"," ");   tempString = tempString.replaceAll("</a>"," ");
                tempString = tempString.replaceAll("<strong>"," "); tempString = tempString.replaceAll("</strong>"," ");
                tempString = tempString.replaceAll("<blockquote>"," "); tempString = tempString.replaceAll("</blockquote>"," ");
                tempString = tempString.replaceAll("<br>"," ");     tempString = tempString.replaceAll("&ltbr>"," ");
                tempString = tempString.replaceAll("<ul>"," "); tempString = tempString.replaceAll("</ul>"," ");
                tempString = tempString.replaceAll("<sup>"," ");  tempString = tempString.replaceAll("</sup>"," ");  
                tempString = tempString.replaceAll("&ltsup>"," "); tempString = tempString.replaceAll("&lt/sup>"," ");
                tempString = tempString.replaceAll("<h>"," ");    tempString = tempString.replaceAll("</h>"," ");
                tempString = tempString.replaceAll("\">"," ");  tempString = tempString.replaceAll("/>"," ");
                tempString = tempString.replaceAll(">"," ");    tempString = tempString.replaceAll("<"," ");
                st += tempString+" ";
            }
        }
        st = st.replaceAll("\\s+", " ");
        //System.out.println(st);
        return st;
    }
    
    
    public static void NaiveBayes(double alpha){
        int TotalWordNumber=0;
        HashMap<String, Integer> tempMap;
        for(int i=0;i<trainData.size();i++){
            tempMap=trainData.get(i);
            TotalWordNumber+=tempMap.size();
        }
        
        for(int i=0;i<testDocuments.size();i++){
            String S=testDocuments.get(i);
            double MAXIMUM=-99999999.0;
            String Category = null;
            for(int j=0;j<NumberOfTopics;j++){
                tempMap=trainData.get(j);
                double Prob=0;
                for (String retval: S.split(" ")) {
                    //System.out.println(retval);
                    int Key=0;
                    if(tempMap.containsKey(retval)){
                        Key=tempMap.get(retval);
                    }
//                    System.err.println("KEY "+Key);
//                    System.err.println("wordNumber.get(j) "+wordNumber.get(j));
//                    System.err.println("TotalWordNumber "+ TotalWordNumber);
                    double TempP=((double)(Key+alpha)/(double)((double)wordNumber.get(j)+(double)((double)TotalWordNumber*alpha)));
                    Prob+=log(TempP);
//                    System.err.println("Temp "+TempP);
//                    System.err.println("Prob "+Prob);
                }
                //System.out.println("PROB"+Prob);
                if(Prob>MAXIMUM){
                    //System.err.println("HERE");
                    MAXIMUM=Prob;
                    Category=Features.get(j);
                }
            }
//            System.out.println("String: "+S);
//            System.out.println("Original "+DocumentsName.get(i));
//            System.out.println("Predict "+Category);
            totalTestNumber++;
            if(DocumentsName.get(i).equals(Category))totalCorrect++;
            
            
        }
    }
    
    public static void TrainDocumentProcessing(String files) throws FileNotFoundException, IOException {
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
    
    public static void TestDocumentProcessing(String files) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("Test/"+files+".xml"));
            String line = br.readLine();
            int count=0;
            while((line = br.readLine()) !=null)
            {
                if(line.contains("<row"))
                {
                    //ParseString p = new ParseString(line);
                    //if(count>=startOftestData){
                        String parsed = parse(line);
                        //System.out.println(parsed);
                        testDocuments.add(parsed);
                        DocumentsName.add(files);
                        //System.out.println("Count "+count);
                    //}
                    count++;
                    if(count==EndofTestData)
                        break;
                }
            }
    }
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        //OutputStream os = new FileOutputStream("test.txt");
        FileOutputStream fos = new FileOutputStream("NB.txt");
        DataOutputStream os = new DataOutputStream(fos);
        FileOutputStream fos1 = new FileOutputStream("KNN.txt");
        DataOutputStream os1 = new DataOutputStream(fos1);
        int Loop=0;
        while(Loop<=5000){
            totalCorrect=0;
            totalTestNumber=0;
            BufferedReader topics = new BufferedReader(new FileReader("topics.txt"));
            String files;
            while((files = topics.readLine()) !=null)
            {
                //System.out.println("files "+files);
                TrainDocumentProcessing(files);
                TestDocumentProcessing(files);
            }
            NumberOfTopics = Topics.size();
//            System.out.println("SIze "+testDocuments.size());
//            System.out.println("TrainSIze "+Topics.get(0).size());
            
            int Sum=0;
            for(int i=0;i<NumberOfTopics;i++)
            {
                ArrayList<String> temp = Topics.get(i);
                DataFormation data = new DataFormation(temp);
                HashMap<String, Integer> trainMap = data.ReFormatData();
                //System.err.println("SIze: "+trainMap.size());
                Sum=0;
                Set set = trainMap.entrySet();
                Iterator I = set.iterator();
                 while(I.hasNext()) {
                   Map.Entry me = (Map.Entry)I.next();
                   //System.out.print(me.getKey() + ": ");
                   //System.out.println(me.getValue());
                   Sum+=(int)me.getValue();
                }
                //System.err.println("SUMMation "+ Sum);
                wordNumber.add(Sum);
                trainData.add(trainMap);
            }

            NaiveBayes(0.4900000000000003);
            Double Accuracy=(double)totalCorrect/totalTestNumber;
//            System.out.println("TOTAL NUMBER "+totalTestNumber);
//            System.out.println("CORRECT "+totalCorrect);
            System.out.println(Accuracy);
            String s=Accuracy.toString()+"\n";
            os.writeBytes(s);
            //os.writeChar('\n');
            totalCorrect=0;
            totalTestNumber=0;
            startOfData+=100;
            EndofData+=100;
            //startOftestData+=50;
            //EndofTestData+=50;
            Topics.clear();
            trainData.clear();
            wordNumber.clear();
            testDocuments.clear();
            DocumentsName.clear();
            Features.clear();

            KNN knn=new KNN();
            Double KNNaccuracy =knn.getRow(1,startOfData,EndofData,0,50);
            //writerKNN.write(String.valueOf(KNNaccuracy));
            String s1=KNNaccuracy.toString()+"\n";
            os1.writeBytes(s1);
            Loop+=100;
        }
        os.close();
        os1.close();
        
       
    }
    
}
