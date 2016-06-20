package com.news.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.news.database.DBconnection;
import com.news.meaning.MeaningEntities;
import com.news.model.Comparacion;
import com.news.model.ContenidoPI;
import com.news.model.Feed;
import com.news.levensthein.*;



@Path("/CompararContenidoPI")
public class CompararPI {

	private final static Logger log = Logger.getLogger(CompararPI.class
			.getName());
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	ArrayList<String> conjuntoVacias = null;
//	static String discoDir = "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\data\\es-general-20080720";

	
	@GET
	@Produces({ MediaType.TEXT_XML })
	public String getFeeds() throws IOException, InstantiationException,
			IllegalAccessException, InterruptedException, SQLException {
		long time_start, time_end;
		time_start = System.currentTimeMillis();
		
		
		fileTxt = new FileHandler("C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		String archivo_prueba = null;
		
		/* 1_ Leer desde BBDD las noticias de los periodicos, guardando en Listas  
		 * Tabla: News_info
		 */
		DBconnection conexionDB = new DBconnection();
//		try {
//			conexionDB.test_connection();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("[DONE] Leidos feeds desde bbdd - Leemos palabras vacï¿½as");
		conjuntoVacias = new ArrayList<String>();
		conjuntoVacias = leerConjuntoVacias();
		System.out.println("[DONE] Leido el fichero de vacï¿½as");
		
		//All News
		List<ContenidoPI> listAllNews = conexionDB.getAllContenidoPI();
		List<String> listaEntities = new ArrayList<String>();
		String textoTag = null;
		for(int i= 0; i < listAllNews.size(); i++){
			//Si la noticia ya tiene tag almacenados los pasamos aun array
			ContenidoPI noticia1 = listAllNews.get(i);
			if((noticia1.getTags() != null) && ((noticia1.getTags() != ""))){
				String tags = noticia1.getTags();
				textoTag = tags;
				listaEntities = Arrays.asList(tags.split(","));
			}else{ //Sino buscamos en Meaning Cloud las entities
				MeaningEntities claseEntidades = new MeaningEntities();
//				String text = StringUtils.stripAccents(noticia1.getDescription());
//				byte ptext[] = noticia1.getDescription().getBytes(); 
//				String value = new String(ptext, "UTF-8"); 
				listaEntities = claseEntidades.getMeaningEntities(noticia1.getTexto1());
				
				if((!listaEntities.isEmpty()) && (listaEntities.size() > 0) && (listaEntities != null)){
					textoTag = listaEntities.get(0);
					if(listaEntities.size() > 1){
						for(int j= 0; j < listaEntities.size(); j++){
							textoTag += "," + listaEntities.get(j).replaceAll("'", "''");
						}
					}
				}
				if(listaEntities != null){
					conexionDB.UpdateTagsContenidoPI(noticia1.getId(), textoTag); // Guardamos la entities de la noticia en bbdd
				}else{
					System.out.println("La noticia" + noticia1.getId() + "No tiene entidades" );
				}
			}//Fin else de buscar las entities
			if(listaEntities != null){
			for(int j= 0; j < listAllNews.size(); j++){
				ContenidoPI noticia2 = listAllNews.get(j);
				if(noticia1.getId() != noticia2.getId()){
					if(!conexionDB.existComparison(noticia1.getId(), noticia2.getId())){
						List<String> contieneEntidades = new ArrayList<String>();
						for(int k= 0; k < listaEntities.size(); k++){
							if(noticia2.getTexto1().contains(listaEntities.get(k))){
								contieneEntidades.add(listaEntities.get(k));
							}
						}
						if(contieneEntidades.size() > 1){
							String texto1 =limpiarTextoVacias(noticia1.getTexto1());
							String texto2 =limpiarTextoVacias(noticia2.getTexto1());
							System.out.println("Texto1 sin vacias " + texto1);		
							System.out.println("Texto2 sin vacias " + texto2);		
						
							String[] str1 = texto1.split(" ");
							String[] str2 = texto2.split(" ");
							ArrayList<String> strLimpio1 = new ArrayList<>();
							ArrayList<String> strLimpio2 = new ArrayList<>();
						
							strLimpio1 = limpiarBlancos(str1);
							strLimpio2 = limpiarBlancos(str2);
							
							levensthein comparador = new levensthein();
							
							float valor = comparador.computeSoftLevenshteinDistance(strLimpio1, strLimpio2);
							conexionDB.insert_comparacion_contenidoPI(noticia1.getId(), noticia2.getId(),limpiarTextoTags(textoTag),Float.toString(valor));
							System.out.println("[DONE] Insertada comparacion");
							System.out.println("Valor de la comparacion Soft " + valor);						
							System.out.println("Valor de la comparacion Soft " + comparador.computeLevenshteinDistance(texto1, texto2));
						
						}
					}
				}
			}
			}
		}
		
		
		/* 1_ Construimos el XML para devolver el resultado de las similares 
		 * 
		 */
		String archivo = null;
		archivo = "<?xml version=\"1.0\" encoding=\"utf-8\"?><feeds>";
		
//		List<String> tagsEntidades = conexionDB.getEntidades();
//		for (int i = 0; i < tagsEntidades.size(); i++) {
//			
//			List<Comparacion> comparaciones = conexionDB.getComparacionesEntidades(tagsEntidades.get(i));
//		
//			for (int j = 0; j < comparaciones.size(); j++) {
//				Feed noticia1 =conexionDB.getOneNew(comparaciones.get(j).getIdNoticia1());
//				Feed noticia2 =conexionDB.getOneNew(comparaciones.get(j).getIdNoticia2());
//				archivo += "<similar>";
//				archivo += "<tagsEntidades>"+ tagsEntidades.get(i) +"</tagsEntidades>";
//				archivo += "<item><title>" + noticia1.getTitle()
//					+ "</title>";
//				archivo += "<link>" + noticia1.getLink() + "</link>";
//				archivo += "<description>" + noticia1.getDescription()
//					+ "</description>";
//				archivo += "<encoded>" + noticia1.getLanguage()
//					+ "</encoded>";
//				archivo += "<pubDate>" + noticia1.getPubDate()
//					+ "</pubDate>";
//				archivo += "<periodico>" + conexionDB.getNewsPapersDescription(noticia1.getIdPeriodico())
//						+ "</periodico></item>";
//				archivo += "<item><title>" + noticia2.getTitle()
//					+ "</title>";
//				archivo += "<link>" + noticia2.getLink() + "</link>";
//				archivo += "<description>" + noticia2.getDescription()
//					+ "</description>";
//				archivo += "<encoded>" + noticia2.getLanguage()
//					+ "</encoded>";
//				archivo += "<pubDate>" + noticia2.getPubDate()
//					+ "</pubDate>";
//				archivo += "<periodico>" + conexionDB.getNewsPapersDescription(noticia2.getIdPeriodico())
//						+ "</periodico></item>";
//				archivo += "<valorSimilitud>"+ comparaciones.get(j).getValor() +"</valorSimilitud></similar>";
//			
//			}
//		}
		archivo += "Leidos los feeds";
		archivo += "</feeds>";
		time_end = System.currentTimeMillis();
		System.out.println("La tarea ha tomado "+ ( time_end - time_start ) +" milisegundos");
		conexionDB.closeConnection();
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
	
	
	private String limpiarTextoTags(String description) {
		 //System.out.println("Limpieza de " + description);
		for (int i = 0; i < conjuntoVacias.size(); i++){
			description = description.replaceAll("\\b" + conjuntoVacias.get(i) + "\\b", "");
			//System.out.println("Descripcion: " + description);
		}
		description = description.replaceAll("[\n\r]","");
		description = description.replaceAll("\"","");
		description = description.replaceAll("'","");
		description = description.replaceAll("\\.","");
		description = description.replaceAll(",","");
		description = description.replaceAll("[\n]","");
		description = description.replaceAll(":","");
		description = description.replaceAll("\\(","");
		description = description.replaceAll("\\)","");
		description = description.replaceAll("\\[","");
		description = description.replaceAll("\\]","");
		description = description.replaceAll("-","");
		description = description.replaceAll("\\{","");
		description = description.replaceAll("\\}","");
		description = description.replaceAll("¿","");
		description = description.replaceAll("\\?","");
		description = description.replaceAll("!","");
		description = description.replaceAll("¡","");
		
		return description;
				
	}
	
	private String limpiarTextoVacias(String description) {
		 //System.out.println("Limpieza de " + description);
		for (int i = 0; i < conjuntoVacias.size(); i++){
			description = description.replaceAll("\\b" + conjuntoVacias.get(i) + "\\b", "");
			//System.out.println("Descripcion: " + description);
		}
		description = description.replaceAll("[\n\r]","");
		description = description.replaceAll("\"","");
		description = description.replaceAll("'","");
		description = description.replaceAll("\\.","");
		description = description.replaceAll(",","");
		description = description.replaceAll("[\n]","");
		description = description.replaceAll(":","");
		description = description.replaceAll("\\(","");
		description = description.replaceAll("\\)","");
		description = description.replaceAll("\\[","");
		description = description.replaceAll("\\]","");
		description = description.replaceAll("-","");
		description = description.replaceAll("\\{","");
		description = description.replaceAll("\\}","");
		description = description.replaceAll("¿","");
		description = description.replaceAll("\\?","");
		description = description.replaceAll("!","");
		description = description.replaceAll("¡","");
		
		return description;
				
	}

	private ArrayList<String> leerConjuntoVacias() {

		BufferedReader br = null;
		String sCurrentLine;
		ArrayList<String> vacias = new ArrayList<String>();

		try {
			br = new BufferedReader(
					new FileReader(
							"C:\\Users\\ASUS\\Webservice\\PFC\\src\\resources\\vacias.txt"));
//					new FileReader(
//							"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\resources\\vacias.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				vacias.add(sCurrentLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vacias;
	}

	
	
		
//		public static int computeLevenshteinDistance(String str1, String str2) {
//	        return computeLevenshteinDistance(str1.toCharArray(),
//	                                          str2.toCharArray());
//	    }
//		
//
//	    private static int computeLevenshteinDistance(char [] str1, char [] str2) {
//	        int [][]distance = new int[str1.length+1][str2.length+1];
//
//	        for(int i=0;i<=str1.length;i++)
//	        {
//	                distance[i][0]=i;
//	        }
//	        for(int j=0;j<=str2.length;j++)
//	        {
//	                distance[0][j]=j;
//	        }
//	        for(int i=1;i<=str1.length;i++)
//	        {
//	            for(int j=1;j<=str2.length;j++)
//	            { 
//	                  distance[i][j]= Minimum(distance[i-1][j]+1,
//	                                        distance[i][j-1]+1,
//	                                        distance[i-1][j-1]+
//	                                        ((str1[i-1]==str2[j-1])?0:1));
//	            }
//	        }
//	        return distance[str1.length][str2.length];
//	        
//	    }

	    
//	    private static int findIndex(ArrayList<String> str, String word ){
//	    	
//	    	for(int i=0;i<=str.size();i++){
//	    		if (str.get(i).equals(word) == true)
//	    			return i;
//	    	}
//	    			    	
//	    	return -1;
//	    }
//
//	    private static float computeSoftLevenshteinDistance(ArrayList<String> strA, ArrayList<String> strB) {
//	    	
//	    	DISCO disco = null;
//			try {
//				disco = new DISCO(discoDir, false);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	        float [][]distance = new float[strA.size()+1][strB.size()+1];
//	        float costo;
//	        int []visitadosA = new int[strA.size()];
//	        int []visitadosB = new int[strB.size()];
//	        int k=0;
//	        
//	        String []T1 = new String[strA.size() * strB.size()];
//	        String [] T2 = new String[strA.size() * strB.size()];
//	        float [] T3 =  new float[strA.size() * strB.size()]; 
//	        float [][]sim =  new float[strA.size()+1][strB.size()+1];
//	        
//	        //Inicializaciï¿½n de los visitados
//	        for(int i=0;i < strA.size();i++){
//	        	visitadosA[i] = 1;
//	        	for(int j=1;j< strB.size();j++){
//	        		visitadosB[j] = 1;
//	        		T1[k] = strA.get(i);
//	        		T2[k] = strB.get(j);
//	        		try {
//	        			if (disco.secondOrderSimilarity(strA.get(i), strB.get(j)) != -1){
//	        				T3[k]  = disco.secondOrderSimilarity(strA.get(i), strB.get(j));
//	        			}else{
//	        				T3[k] = 0;
//	        			}
//	        			
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	        		k+=1;
//	        	}
//	        }
//	        
//	        //Ordenamos tabla 
//	        for(int i=0;i< T3.length;i++){
//	        	if(T1[i] != null && T2[i] != null){
//	        		//System.out.println("Indice se visitadosA :"+ findIndex(strA, T1[i]) + "Indices de visitadosB:" + findIndex(strB, T2[i]));
//	        		if((visitadosA[findIndex(strA, T1[i])] == 1 ) && (visitadosB[findIndex(strB, T2[i])] == 1 )){
//	        			visitadosA[findIndex(strA, T1[i])] = 0;
//	        			visitadosB[findIndex(strB, T2[i])] = 1;
//	        			sim[findIndex(strA, T1[i])][findIndex(strB, T2[i])] = 1- T3[i];
//	        		}
//	        	}
//	        }
//	        
//	        for(int i=0;i<=strA.size();i++){
//	        	distance[i][0] = i;
//	        }
//	        for(int i=0;i<=strB.size();i++){
//	        	distance[0][i] = i;
//	        }
//	        for(int i=1;i< strA.size();i++){
//	            for(int j=1;j< strB.size();j++){
//	            	if (strA.get(i).equals(strB.get(j))){
//	            		costo = 0; 
//	            	}else{
//	            		costo = sim[i][j];
//	            	}
//	            	distance[i][j] = Minimum((distance[i-1][j]) + 1, (distance[i][j-1]) + 1, (distance[i-1][j-1]) + costo);
//	            }
//	        }
//	        
//	        float resultado = distance[strA.size()-1][strB.size() -1]/ Maximun(strA.size(), strB.size());
//	        // float resultado = distance[strA.size()-1][strB.size() -1];
//	        return resultado;
//	        
//	    }
//
//	    private static int Maximun(int a, int b){
//	    	if (a>b)
//	    		return a;
//	    	return b;
//	    }
//
//	    private static float Minimum(float a, float b, float c) {
//	    	  float mi;
//
//	    	    mi = a;
//	    	    if (b < mi) {
//	    	      mi = b;
//	    	    }
//	    	    if (c < mi) {
//	    	      mi = c;
//	    	    }
//	    	    return mi;
//	    }
//
//
//		private static int Minimum (int a, int b, int c) {
//	    	  int mi;
//
//	    	    mi = a;
//	    	    if (b < mi) {
//	    	      mi = b;
//	    	    }
//	    	    if (c < mi) {
//	    	      mi = c;
//	    	    }
//	    	    return mi;
//
//	    	  }


	
}
