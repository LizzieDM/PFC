package com.news.service;

import java.io.BufferedReader;
import java.io.File;
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
import com.news.database.ReadService;
import com.news.model.Comparacion;
import com.news.model.Feed;

import de.linguatools.disco.DISCO;

@Path("/leerFeeds")
public class LeerFeeds {

	

	// This method is called if XML is request
//		  @GET
//		  @Produces(MediaType.TEXT_XML)
//		  public String sayXMLHello() {
//		    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
//		  }
		
		// This can be used to test the integration with the browser
			private final static Logger log = Logger.getLogger(Servicio.class
					.getName());
			static private FileHandler fileTxt;
			static private SimpleFormatter formatterTxt;
			ArrayList<String> conjuntoVacias = null;
			static String discoDir = "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\data\\es-general-20080720";

			@GET
			@Produces({ MediaType.TEXT_PLAIN })
			public String getFeeds() throws IOException, InstantiationException,
					IllegalAccessException, InterruptedException, SQLException {
				
				fileTxt = new FileHandler("C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
				formatterTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterTxt);
				log.addHandler(fileTxt);
				String archivo_prueba = null;
				File file = new File(
						"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\articles.rss");
				if (!file.exists()) {
					file.createNewFile();
				}
				
				/* 1_ Leer desde el rss las noticias dependiendo de los periodicos que haya en la BBDD 
				 * Tabla: NewsPaper_info
				 */
				
				ReadService read = new ReadService();
				read.readBdInsert();
				System.out.println("Insertados a BBDD");
				return "Feeds Leidos con Exito";
			}



}
