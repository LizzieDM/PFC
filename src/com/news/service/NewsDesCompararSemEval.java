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
import com.news.levensthein.levensthein;
import com.news.meaning.MeaningEntities;
import com.news.model.Comparacion;
import com.news.model.Feed;
import com.news.model.NewsSemEval;
import com.news.model.SemEvalText;

@Path("/NewsDesCompararSemEval")
public class NewsDesCompararSemEval {

		private final static Logger log = Logger.getLogger(ComparadorTopics.class
				.getName());
		static private FileHandler fileTxt;
		static private SimpleFormatter formatterTxt;
		ArrayList<String> conjuntoVacias = null;
//		static String discoDir = "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\data\\es-general-20080720";

		
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
			try {
				conexionDB.test_connection();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("[DONE] Leidos feeds desde bbdd - Leemos palabras vacï¿½as");
			conjuntoVacias = new ArrayList<String>();
			conjuntoVacias = leerConjuntoVacias();
			System.out.println("[DONE] Leido el fichero de vacï¿½as");
			
			//All News
			List<NewsSemEval> listAllSemEval = conexionDB.getAllSemEvalNews();
			List<String> listaEntities = new ArrayList<String>();
			String textoTag = null;
			for(int i= 0; i < listAllSemEval.size(); i++){
				//Si la noticia ya tiene tag almacenados los pasamos aun array
				NewsSemEval textoSemEval = listAllSemEval.get(i);
				Feed noticia1 = conexionDB.getOneNew(textoSemEval.getIdnoticia1());
				Feed noticia2 = conexionDB.getOneNew(textoSemEval.getIdnoticia2());
			
					String texto1 =limpiarTextoVacias(noticia1.getDescription());
					String texto2 =limpiarTextoVacias(noticia2.getDescription());
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
//					 float c = valor * 4;
//					 float result = 4 -c;
					conexionDB.insert_news_comparacion_semEval(textoSemEval.getId(), Float.toString(valor));
					System.out.println("[DONE] Insertada comparacion");
					System.out.println("Valor de la comparacion Soft " + valor);						
					System.out.println("Valor de la comparacion Soft " + comparador.computeLevenshteinDistance(texto1, texto2));
				
			}
			
			String archivo = "<item>Finalizada la comparación</item>";
			
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
//						new FileReader(
//								"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\resources\\vacias.txt"));
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

	
		
	

	
}
