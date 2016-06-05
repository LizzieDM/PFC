package com.news.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import com.news.levensthein.levensthein;
import com.news.model.Comparacion;
import com.news.model.Feed;

import de.linguatools.disco.DISCO;

@Path("/CompararDescripciones")
public class CompararDescripciones {

	

	// This method is called if XML is request
//		  @GET
//		  @Produces(MediaType.TEXT_XML)
//		  public String sayXMLHello() {
//		    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
//		  }
		
		// This can be used to test the integration with the browser
			private final static Logger log = Logger.getLogger(CompararDescripciones.class
					.getName());
			static private FileHandler fileTxt;
			static private SimpleFormatter formatterTxt;
			ArrayList<String> conjuntoVacias = null;
			static String discoDir = "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\data\\es-general-20080720";

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
				// 
				List<Feed> listNews2 = conexionDB.getAllNews();
				//
				List<Feed> listNews1 = conexionDB.getAllNews();
				
				System.out.println("[DONE] Leidos feeds desde bbdd - Leemos palabras vacías");
				conjuntoVacias = new ArrayList<String>();
				conjuntoVacias = leerConjuntoVacias();
				System.out.println("[DONE] Leido el fichero de vacías");
				
				
				/* 1_ Para cada news en la lista1 hacer para cada news de la lista 2 comparacion de 
				 * Levenstein (No semantica o semantica). 
				 * 2_ Guardar en BBDD la comparación junto con los id´s de las noticias.
				 * Tabla: News_info
				 */
				
					for(int i = 0; i < listNews1.size(); i++){
						for (int j = 0; j < listNews2.size(); j++){

							if(listNews1.get(i).getId() != listNews2.get(j).getId()){
								if(! conexionDB.existComparisonDescripcion(listNews1.get(i).getId(), listNews2.get(j).getId())){
							
									String texto1 =limpiarTextoVacias(listNews1.get(i).getDescription());
									String texto2 =limpiarTextoVacias(listNews2.get(j).getDescription());
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
									conexionDB.insert_comparacion(listNews1.get(i).getId(), listNews2.get(j).getId(), Float.toString(valor));
									System.out.println("[DONE] Insertada comparacion");
									System.out.println("Valor de la comparacion Soft " + valor);						
									System.out.println("Valor de la comparacion Soft " + comparador.computeLevenshteinDistance(texto1, texto2));
							
								}
							}
						}
						
					}
				
				/* 1_ Construimos el XML para devolver el resultado de las similares 
				 * 
				 */
				String archivo = null;
				archivo = "<?xml version=\"1.0\" encoding=\"utf-8\"?><feeds>";
				List<Comparacion> comparaciones = conexionDB.getComparaciones();
				
				for (int j = 0; j < comparaciones.size(); j++) {
					
					Feed noticia1 =conexionDB.getOneNew(comparaciones.get(j).getIdNoticia1());
					Feed noticia2 =conexionDB.getOneNew(comparaciones.get(j).getIdNoticia2());
					archivo += "<similar>";
					archivo += "<item><title>" + noticia1.getTitle()
							+ "</title>";
					archivo += "<link>" + noticia1.getLink() + "</link>";
					archivo += "<description>" + noticia1.getDescription()
							+ "</description>";
					archivo += "<encoded>" + noticia1.getLanguage()
							+ "</encoded>";
					archivo += "<pubDate>" + noticia1.getPubDate()
							+ "</pubDate>";
					archivo += "<periodico>" + conexionDB.getNewsPapersDescription(noticia1.getIdPeriodico())
							+ "</periodico></item>";
					archivo += "<item><title>" + noticia2.getTitle()
							+ "</title>";
					archivo += "<link>" + noticia2.getLink() + "</link>";
					archivo += "<description>" + noticia2.getDescription()
							+ "</description>";
					archivo += "<encoded>" + noticia2.getLanguage()
							+ "</encoded>";
					archivo += "<pubDate>" + noticia2.getPubDate()
							+ "</pubDate>";
					archivo += "<periodico>" + conexionDB.getNewsPapersDescription(noticia2.getIdPeriodico())
							+ "</periodico></item>";
					archivo += "<valorSimilitud>"+ comparaciones.get(j).getValor() +"</valorSimilitud></similar>";
					
				}
				archivo += "</feeds>";
				
				time_end = System.currentTimeMillis();
				System.out.println("La tarea ha tomado"+ ( time_end - time_start ) +" milisegundos");
				
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

