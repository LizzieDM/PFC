package com.news.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.news.database.DBconnection;
import com.news.database.ReadService;
import com.news.model.Feed;

import de.linguatools.disco.DISCO;



@Path("/hello")
public class Servicio {

// This method is called if XML is request
//	  @GET
//	  @Produces(MediaType.TEXT_XML)
//	  public String sayXMLHello() {
//	    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
//	  }
	
	// This can be used to test the integration with the browser
		private final static Logger log = Logger.getLogger(Servicio.class
				.getName());
		static private FileHandler fileTxt;
		static private SimpleFormatter formatterTxt;
		ArrayList<String> conjuntoVacias = null;
		static String discoDir = "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\data\\es-general-20080720";

		@GET
		@Produces({ MediaType.TEXT_XML })
		public String getFeeds() throws IOException, InstantiationException,
				IllegalAccessException, InterruptedException, SQLException {

//			fileTxt = new FileHandler(
//					"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
//			formatterTxt = new SimpleFormatter();
//			fileTxt.setFormatter(formatterTxt);
//			log.addHandler(fileTxt);
//
			String archivo = null;
//			String archivo_prueba = null;
//			File file = new File(
//					"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\articles.rss");
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//
//			ReadService read = new ReadService();
//			read.readBdInsert();
//			System.out.println("Insertados a BBDD");
//
//			DBconnection conexionDB = new DBconnection();
//			try {
//				conexionDB.test_connection();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
			archivo = "<?xml version=\"1.0\" encoding=\"utf-8\"?><feeds>";
//	/*
//			// Diario de avisos
//			List<Feed> listNews2 = conexionDB.getNews(2);
//			for (int j = 0; j < listNews2.size(); j++) {
//				archivo += "<item><title>" + listNews2.get(j).getTitle()
//						+ "</title>";
//				archivo += "<link>" + listNews2.get(j).getLink() + "</link>";
//				archivo += "<description>" + listNews2.get(j).getDescription()
//						+ "</description>";
//				archivo += "<encoded>" + listNews2.get(j).getLanguage()
//						+ "</encoded>";
//				archivo += "<pubDate>" + listNews2.get(j).getPubDate()
//						+ "</pubDate></item>";
//			}*/
//			archivo += "</feeds>";
//
//			// Escribimos en el archivo
//
//			System.out.println("Done");
//
//			FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(archivo);
//			bw.close();

			System.out.println("Done File");
			conjuntoVacias = new ArrayList<String>();
			conjuntoVacias = leerConjuntoVacias();
			System.out.println("Leido el fichero de vacías");
			
			String texto1 =limpiarTextoVacias("No cabe la restitución de un vehículo afectado por el fraude de emisiones al concesionario, ni exigir la devolución del precio de compra, según el Tribunal Regional Bochum, estado alemán de Renania del Norte-Westfalia.");
			//String texto2 =limpiarTextoVacias("Entre los motivos del fallo, al que ha tenido acceso EL PAÍS, está que el comprador no demuestra tener una conciencia ecológica especial y que la reparación del vehículo que tiene el software malitencionado puede hacerse “en términos razonables”.");
			//String texto1 =limpiarTextoVacias("El terremoto acabó con todo a su paso");
			String texto2 =limpiarTextoVacias("El temblor destrozó todo a su paso");
			
			System.out.println("Texto1 sin vacias " + texto1);		
			System.out.println("Texto2 sin vacias " + texto2);		
			
			String[] str1 = texto1.split(" ");
			String[] str2 = texto2.split(" ");
			
			ArrayList<String> strLimpio1 = new ArrayList<>();
			ArrayList<String> strLimpio2 = new ArrayList<>();
			
			strLimpio1 = limpiarBlancos(str1);
			strLimpio2 = limpiarBlancos(str2);
			
			float valor = computeSoftLevenshteinDistance(strLimpio1, strLimpio2);
			
			System.out.println("Valor de la comparacion Soft " + valor);
			
			System.out.println("Valor de la comparacion Soft " + computeLevenshteinDistance(texto1, texto2));
			// ElDia
//			List<Feed> listNews = conexionDB.getNews(1);
//			for (int i = 0; i < listNews.size(); i++) {
//				 String texto1 = limpiarTextoVacias(listNews.get(i).getDescription());
//				 System.out.println("Texto sin vacias" + texto1);		
//			}
//			
			
			DISCO disco = null;
//			try {
//				disco = new DISCO(discoDir, false);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		try {
//			System.out.println("Vivienda y hogar se parecen en ...:" + disco.firstOrderSimilarity("hogar", "vivienda"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
			return archivo;
		}

		
		private ArrayList<String> limpiarBlancos(String[] vector){
			ArrayList<String> vectorSinBlancos = new ArrayList<>();
			for(int i = 0; i< vector.length; i++){
				if ((vector[i] != null) && (!vector[i].equals("")) ){
					vectorSinBlancos.add(vector[i]);
				}
			}
			
			return vectorSinBlancos;
		}
		
		private String limpiarTextoVacias(String description) {
			 System.out.println("Limpieza de " + description);
			for (int i = 0; i < conjuntoVacias.size(); i++){
				description = description.replaceAll("\\b" + conjuntoVacias.get(i) + "\\b", "");
				System.out.println("Descripcion: " + description);
			}
			
			return description;
					
		}

		private ArrayList<String> leerConjuntoVacias() {

			BufferedReader br = null;
			String sCurrentLine;
			ArrayList<String> vacias = new ArrayList<String>();

			try {
				br = new BufferedReader(
						new FileReader(
								"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\resources\\vacias.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
					vacias.add(sCurrentLine);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return vacias;
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

		    
		    private static int findIndex(ArrayList<String> str, String word ){
		    	
		    	for(int i=0;i<=str.size();i++){
		    		if (str.get(i).equals(word) == true)
		    			return i;
		    	}
		    			    	
		    	return -1;
		    }

		    private static float computeSoftLevenshteinDistance(ArrayList<String> strA, ArrayList<String> strB) {
		    	
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
		        
		        //Inicialización de los visitados
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
		        		System.out.println("Indice se visitadosA :"+ findIndex(strA, T1[i]) + "Indices de visitadosB:" + findIndex(strB, T2[i]));
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
		        return resultado;
		        
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

	
}

