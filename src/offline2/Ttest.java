/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author masud
 */
public class Ttest {
    public static ArrayList<Double> NBarr=new ArrayList<>();
    public static ArrayList<Double> KNNarr=new ArrayList<>();
   
    
    public static double getMean(int N,int algo){
        double Sum=0;
        System.out.println("N "+N);
        if(algo==1){
            for(int i=0;i<N;i++){
                Sum+=(NBarr.get(i));
                //System.out.println("Sum "+Sum);
            }
        }
        else{
            for(int i=0;i<N;i++){
                Sum+=(KNNarr.get(i));
            }
        }
        double mean=Sum/N;
        
        return mean;
    }
    
    public static double getStd(int N,int algo,double mean){
        double Sum=0;
        if(algo==1){
            for(int i=0;i<N;i++){
                Sum+=((mean-(NBarr.get(i)))*(mean-(NBarr.get(i))));
                //System.out.println("STD SUm "+Sum);
            }
        }
        else{
            for(int i=0;i<N;i++){
                Sum+=((mean-(KNNarr.get(i)))*(mean-(KNNarr.get(i))));
                //System.out.println("STD SUm "+Sum);
            }
        }
        
        Sum=Sum/(N-1);
        //System.out.println("Final SUm "+Sum);
        //double STD=Math(Sum);
        //System.out.println("Final SUm "+STD);
        
        return Sum;
    }
//    static void calculateT_Test()
//    {
//        double n = NBarr.size();
//        double d=50;
//        System.out.println("N "+n);
//        double nsum=0,ksum=0,navg,kavg,nstd=0,kstd=0;
//        for(int i=0;i<NBarr.size();i++)
//        {
//            nsum += NBarr.get(i);
//            ksum += KNNarr.get(i);
//        }
//        navg = nsum/n;
//        kavg = ksum/n;
//        
//        System.out.println("Mean "+navg);
//        System.out.println("Mean1 "+kavg);
//        
//        for(int i=0;i<NBarr.size();i++)
//        {
//            nstd += (NBarr.get(i)-navg)*(NBarr.get(i)-navg);
//            kstd += (KNNarr.get(i)-kavg)*(KNNarr.get(i)-kavg);
//        }
//        nstd = (nstd/(n-1));
//        kstd = (kstd/(n-1));
//       
//        System.out.println("STD "+nstd);
//        System.out.println("STD1 "+kstd);
//        
//        double sqr = (nstd/n)+(kstd/n);
//        System.out.println("Sqr "+sqr);
//        double t = (navg - kavg - d)/Math.sqrt(sqr);
//        double dof = sqr * sqr;
//        dof = (n*n*(n-1)) * ( dof / (nstd*nstd+kstd*kstd));
//        System.out.println("T-value : "+ t+"\t "+dof);
//    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader NBfile = new BufferedReader(new FileReader("NB2.txt"));
        BufferedReader KNNfile = new BufferedReader(new FileReader("KNN1.txt"));
        FileOutputStream fos = new FileOutputStream("Ttest.txt");
        DataOutputStream os = new DataOutputStream(fos);
        String Lines;
        int Line=1;
        int N=0;
        while((Lines = NBfile.readLine()) !=null)
        {
            double t1=Double.parseDouble(Lines);
            NBarr.add(t1*100);
            System.out.println(t1);
            N++;
                
        }
        String KNNlines;
        while((KNNlines = KNNfile.readLine()) !=null)
        {
            double t1=Double.parseDouble(KNNlines);
            KNNarr.add(t1*100);
            System.out.println(t1);
        }
        
        double NBmean=getMean(N, 1);
        double KNNmean=getMean(N, 2);
        double NBstd=getStd(N, 1, NBmean);
        double KNNstd=getStd(N, 2, KNNmean);
        
        os.writeBytes("NBmean "+NBmean+"\n");
        os.writeBytes("KNNmean "+KNNmean+"\n");
        os.writeBytes("NBstd "+NBstd+"\n");
        os.writeBytes("KNNstd "+KNNstd+"\n");
        
        
        
        System.out.println("NBmean "+NBmean);
        System.out.println("KNNmean "+KNNmean);
        System.out.println("NBstd "+NBstd);
        System.out.println("KNNstd "+KNNstd);
        
        double d=50;
        
        double upper=(KNNstd+NBstd)/N;
        System.out.println("Upper "+upper);
        double sd=Math.sqrt(upper);
        System.out.println(sd);
        double t=(-KNNmean+NBmean-d)/sd;
        
        
        System.out.println("T "+t);
        os.writeBytes("T value "+(t)+"\n");
        
        double KNN4=KNNstd*KNNstd;
        double NB4=NBstd*NBstd;
        double total=KNN4+NB4;
        double Lower=total/(N*N*(N-1));
        double DOF=(upper*upper)/Lower;
        
        System.out.println("DOF "+DOF);
        os.writeBytes("Degree of freedom "+DOF+"\n");
        os.writeBytes("Tcritical for alpha=.005 and DOF "+DOF+" is "+ 2.639+"\n");
        os.writeBytes("Tcritical for alpha=.01 and DOF "+DOF+" is "+ 2.374+"\n");
        os.writeBytes("Tcritical for alpha=.05 and DOF "+DOF+" is "+ 1.664+"\n");
        
        //calculateT_Test();
        
    }
}
