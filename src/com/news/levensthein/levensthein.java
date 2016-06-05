package com.news.levensthein;

import java.io.IOException;
import java.util.ArrayList;

import de.linguatools.disco.DISCO;

public class levensthein {
	
	static String discoDir = "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\data\\es-general-20080720";

	public static float computeSoftLevenshteinDistance(ArrayList<String> strA, ArrayList<String> strB) {
    	
    	DISCO disco = null;
		try {
			disco = new DISCO(discoDir, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        float [][]distance = new float[strA.size()+1][strB.size()+1];
        float costo;
        int []visitadosA = new int[strA.size()];
        int []visitadosB = new int[strB.size()];
        int k=0;
        
        String []T1 = new String[strA.size() * strB.size()];
        String [] T2 = new String[strA.size() * strB.size()];
        float [] T3 =  new float[strA.size() * strB.size()]; 
        float [][]sim =  new float[strA.size()+1][strB.size()+1];
        
        //Inicializaci�n de los visitados
        for(int i=0;i < strA.size();i++){
        	visitadosA[i] = 1;
        	for(int j=1;j< strB.size();j++){
        		visitadosB[j] = 1;
        		T1[k] = strA.get(i);
        		T2[k] = strB.get(j);
        		try {
        			if (disco.secondOrderSimilarity(strA.get(i), strB.get(j)) != -1){
        				T3[k]  = disco.secondOrderSimilarity(strA.get(i), strB.get(j));
        			}else{
        				T3[k] = 0;
        			}
        			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		k+=1;
        	}
        }
        
        //Ordenamos tabla 
        for(int i=0;i< T3.length;i++){
        	if(T1[i] != null && T2[i] != null){
        		//System.out.println("Indice se visitadosA :"+ findIndex(strA, T1[i]) + "Indices de visitadosB:" + findIndex(strB, T2[i]));
        		if((visitadosA[findIndex(strA, T1[i])] == 1 ) && (visitadosB[findIndex(strB, T2[i])] == 1 )){
        			visitadosA[findIndex(strA, T1[i])] = 0;
        			visitadosB[findIndex(strB, T2[i])] = 1;
        			sim[findIndex(strA, T1[i])][findIndex(strB, T2[i])] = 1- T3[i];
        		}
        	}
        }
        
        for(int i=0;i<=strA.size();i++){
        	distance[i][0] = i;
        }
        for(int i=0;i<=strB.size();i++){
        	distance[0][i] = i;
        }
        for(int i=1;i< strA.size();i++){
            for(int j=1;j< strB.size();j++){
            	if (strA.get(i).equals(strB.get(j))){
            		costo = 0; 
            	}else{
            		costo = sim[i][j];
            	}
            	distance[i][j] = Minimum((distance[i-1][j]) + 1, (distance[i][j-1]) + 1, (distance[i-1][j-1]) + costo);
            }
        }
        
        float resultado = distance[strA.size()-1][strB.size() -1]/ Maximun(strA.size(), strB.size());
        // float resultado = distance[strA.size()-1][strB.size() -1];
        return resultado;
        
    }
	
	public static int computeLevenshteinDistance(String str1, String str2) {
        return computeLevenshteinDistance(str1.toCharArray(),
                                          str2.toCharArray());
    }
	

    private static int computeLevenshteinDistance(char [] str1, char [] str2) {
        int [][]distance = new int[str1.length+1][str2.length+1];

        for(int i=0;i<=str1.length;i++)
        {
                distance[i][0]=i;
        }
        for(int j=0;j<=str2.length;j++)
        {
                distance[0][j]=j;
        }
        for(int i=1;i<=str1.length;i++)
        {
            for(int j=1;j<=str2.length;j++)
            { 
                  distance[i][j]= Minimum(distance[i-1][j]+1,
                                        distance[i][j-1]+1,
                                        distance[i-1][j-1]+
                                        ((str1[i-1]==str2[j-1])?0:1));
            }
        }
        return distance[str1.length][str2.length];
        
    }
	 
    private static int Maximun(int a, int b){
    	if (a>b)
    		return a;
    	return b;
    }

    private static float Minimum(float a, float b, float c) {
    	  float mi;

    	    mi = a;
    	    if (b < mi) {
    	      mi = b;
    	    }
    	    if (c < mi) {
    	      mi = c;
    	    }
    	    return mi;
    }


	private static int Minimum (int a, int b, int c) {
    	  int mi;

    	    mi = a;
    	    if (b < mi) {
    	      mi = b;
    	    }
    	    if (c < mi) {
    	      mi = c;
    	    }
    	    return mi;

    	  }
	
    private static int findIndex(ArrayList<String> str, String word ){
    	
    	for(int i=0;i<=str.size();i++){
    		if (str.get(i).equals(word) == true)
    			return i;
    	}
    			    	
    	return -1;
    }
	
}
