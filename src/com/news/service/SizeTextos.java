package com.news.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.news.database.DBconnection;
import com.news.model.Feed;
import com.news.model.SemEvalText;


@Path("/SizeTextos")
public class SizeTextos {
	private final static Logger log = Logger.getLogger(ComparadorTopics.class
			.getName());
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	
	@GET
	@Produces({ MediaType.TEXT_XML })
	public String textSize() throws IOException, InstantiationException,
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
		
		/* 1_ Traemos el listado de textos de semEval y guardamos el size de los textos.
		 */
		List<SemEvalText> listAllSemEval = conexionDB.getAllSemEval();
		for(int i= 0; i < listAllSemEval.size(); i++){
			//Si la noticia ya tiene tag almacenados los pasamos aun array
			SemEvalText textoSemEval = listAllSemEval.get(i);
				
			String [] tam1 = textoSemEval.getTexto1().split("\\s+");
			String [] tam2 = textoSemEval.getTexto2().split("\\s+");
			
			conexionDB.UpdateSizeSemEval(textoSemEval.getId(), Integer.toString(tam1.length), Integer.toString(tam2.length));
		}
		
		/* 1_ Traemos el listado de textos de noticias y guardamos el size de los textos.
		 */
		
		List<Feed> listNews = conexionDB.getAllNews();
		for(int i= 0; i < listNews.size(); i++){
			//Si la noticia ya tiene tag almacenados los pasamos aun array
			Feed Noticia = listNews.get(i);
			String [] tam1 = Noticia.getDescription().split("\\s+");
			conexionDB.UpdateSizeNews(Noticia.getId(),Integer.toString(tam1.length));
		}
		
		return "Actualizados los campos size de los textos";
	}

}
