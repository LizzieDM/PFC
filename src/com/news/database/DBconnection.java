package com.news.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.news.model.Comparacion;
import com.news.model.Feed;
import com.news.model.NewsPaper;
import com.news.service.Servicio;



public class DBconnection {
	private final static Logger log = Logger.getLogger(DBconnection.class
			.getName());
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	Connection conn = null;
	
	public void test_connection() throws InterruptedException {

		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;
		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		conn = null;

		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/webservicedb",
					"postgres", "omairapostgres");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (conn != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	public Connection getConnection() throws ClassNotFoundException {
		try {
			Class.forName("org.postgresql.Driver");
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		conn = null;

		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/webservicedb",
					"postgres", "omairapostgres");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return conn;
		}
		return conn;

	}
	
	public void closeConnection (){
		try{
			conn.close();
		}catch(Exception e) {
			System.out.println("No se ha podido cerrar la conexión correctamente");
			e.printStackTrace();
			
		}
	}

	public  List<NewsPaper> getNewsPapers() {
		
		PreparedStatement query = null;
		ResultSet result = null;
		List<NewsPaper> newsPapers = new ArrayList<NewsPaper>();
		try {
			conn = getConnection();

			String sql = ("Select * from newspaper_info;");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				NewsPaper newsPaper = new NewsPaper();
				newsPaper.setId(result.getInt(1));
				newsPaper.setUrl(result.getString(2));
				newsPaper.setName(result.getString(3));
				newsPapers.add(newsPaper);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("Table newspaper readed successfully");
		return newsPapers;
	}
	
	public  String getNewsPapersDescription(String idNewsPaper) {
		
		PreparedStatement query = null;
		ResultSet result = null;
		NewsPaper newsPaper = new NewsPaper();
		try {
			conn = getConnection();

			String sql = ("Select * from newspaper_info where id='"+ idNewsPaper +"';");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				
				newsPaper.setId(result.getInt(1));
				newsPaper.setUrl(result.getString(2));
				newsPaper.setName(result.getString(3));
				
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("Table newspaper readed successfully");
		return newsPaper.getName();
	}
	
	
	public void insert_feed(String description, String tittle, String author, String pub_date, int id_newspaper ) throws SecurityException, IOException{
		
		fileTxt = new FileHandler(
				"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
		  
	      Statement stmt = null;
	      try {
	    	 conn = getConnection();
	         conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = conn.createStatement();
	         //log.info("[DESCRIPTION]" + description);
	         //log.info("[TITTLE]" + tittle);
	         //log.info("Date:" + pub_date.substring(16, pub_date.length()));
	         String descrip_parse = parseQuotes(description);
	         String tittle_parse = parseQuotes(tittle);
	         String sql = "INSERT INTO news_info (description,title,author,pub_date,id_newspaper) "
	               + "VALUES ('" + description + "','" + tittle + "','" + author + "','" + pub_date + "','" + id_newspaper + "');";
	         
	         stmt.executeUpdate(sql);
	         //log.info("[Sentencia]" + sql);
	         stmt.close();
	         conn.commit();
	         conn.close();
	      } catch (Exception e) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }
	    
	}
	
	public void insert_comparacion(String string, String string2, String string3){
		try {
			fileTxt = new FileHandler(
					"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		} catch (SecurityException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
	      Statement stmt = null;
	      try {
	    	 conn = getConnection();
	         conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = conn.createStatement();
	         String sql = "INSERT INTO news_comparacion (idnoticia1,idnoticia2,comparacion) "
	               + "VALUES ('" + string + "','" + string2 + "','" + string3 + "');";
	         
	         stmt.executeUpdate(sql);
	         //log.info("[Sentencia]" + sql);
	         stmt.close();
	         conn.commit();
	         conn.close();
	      } catch (Exception e) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }
	}
	
	public void insert_comparacion_titulos(String string, String string2, String string3){
		try {
			fileTxt = new FileHandler(
					"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		} catch (SecurityException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
	      Statement stmt = null;
	      try {
	    	 conn = getConnection();
	         conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = conn.createStatement();
	         String sql = "INSERT INTO news_comparacion_titulos (idnoticia1,idnoticia2,comparacion) "
	               + "VALUES ('" + string + "','" + string2 + "','" + string3 + "');";
	         
	         stmt.executeUpdate(sql);
	         //log.info("[Sentencia]" + sql);
	         stmt.close();
	         conn.commit();
	         conn.close();
	      } catch (Exception e) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }
	}
	
	public void insert_comparacion_entidades(String string, String string2, String string3, String tagsEntidades){
		try {
			fileTxt = new FileHandler(
					"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		} catch (SecurityException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
	      Statement stmt = null;
	      try {
	    	 conn = getConnection();
	         conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = conn.createStatement();
	         String sql = "INSERT INTO news_comparacion_entidades (idnoticia1,idnoticia2,comparacion,tagsentidades) "
	               + "VALUES ('" + string + "','" + string2 + "','" + string3 + "','" + tagsEntidades + "');";
	         
	         stmt.executeUpdate(sql);
	         //log.info("[Sentencia]" + sql);
	         stmt.close();
	         conn.commit();
	         conn.close();
	      } catch (Exception e) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	      }
	}
	
	public boolean existComparison(String idNoticia1, String idNoticia2){
		try{
			conn = getConnection();
			PreparedStatement pstat = conn.prepareStatement("Select id from news_comparacion_entidades where idnoticia1='"+idNoticia1+"' and idnoticia2='"+idNoticia2+"'");        
		 
	        ResultSet rs = pstat.executeQuery();
	        int rowCount=0;

	        while(rs.next())
	        {
	            rowCount++;
	            int typeID = rs.getInt(1);
	        }
	       
	        	Connection con2 = null;
	        	con2 = getConnection();
				PreparedStatement pstat2 = con2.prepareStatement("Select id from news_comparacion_entidades where idnoticia1='"+idNoticia2+"' and idnoticia2='"+idNoticia1+"'");        
			 
		        ResultSet rs2 = pstat2.executeQuery();
		        int rowCount2=0;

		        while(rs2.next())
		        {
		            rowCount2++;
		            int typeID = rs2.getInt(1);
		        }
		
		        if(!(rowCount > 0 ) && !(rowCount2 > 0)){
		        	conn.close();
		        	con2.close();
		        	return false;
		        }else{
		        	conn.close();
		        	con2.close();
		        	return true;
		        }

		}catch(Exception e) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
			return true;
		}
	}
	
	public boolean existComparisonDescripcion(String idNoticia1, String idNoticia2){
		try{
			
			conn = getConnection();
			PreparedStatement pstat = conn.prepareStatement("Select idcomparacion from news_comparacion where idnoticia1='"+idNoticia1+"' and idnoticia2='"+idNoticia2+"'");        
		 
	        ResultSet rs = pstat.executeQuery();
	        int rowCount=0;

	        while(rs.next())
	        {
	            rowCount++;
	            int typeID = rs.getInt(1);
	        }
	       
	        	Connection con2 = null;
	        	con2 = getConnection();
				PreparedStatement pstat2 = con2.prepareStatement("Select idcomparacion from news_comparacion where idnoticia1='"+idNoticia2+"' and idnoticia2='"+idNoticia1+"'");        
			 
		        ResultSet rs2 = pstat2.executeQuery();
		        int rowCount2=0;

		        while(rs2.next())
		        {
		            rowCount2++;
		            int typeID = rs2.getInt(1);
		        }
		
		        if(!(rowCount > 0 ) && !(rowCount2 > 0)){
		        	conn.close();
		        	con2.close();
		        	return false;
		        }else{
		        	conn.close();
		        	con2.close();
		        	return true;
		        }
		
		}catch(Exception e) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
			return true;
		}
	}
	
	public boolean existComparisonTitulos(String idNoticia1, String idNoticia2){
		try{
			conn = getConnection();
			PreparedStatement pstat = conn.prepareStatement("Select idcomparacion from news_comparacion_titulos where idnoticia1='"+idNoticia1+"' and idnoticia2='"+idNoticia2+"'");        
		 
	        ResultSet rs = pstat.executeQuery();
	        int rowCount=0;

	        while(rs.next())
	        {
	            rowCount++;
	            int typeID = rs.getInt(1);
	        }
	       
	        	Connection con2 = null;
	        	con2 = getConnection();
				PreparedStatement pstat2 = con2.prepareStatement("Select idcomparacion from news_comparacion_titulos where idnoticia1='"+idNoticia2+"' and idnoticia2='"+idNoticia1+"'");        
			 
		        ResultSet rs2 = pstat2.executeQuery();
		        int rowCount2=0;

		        while(rs2.next())
		        {
		            rowCount2++;
		            int typeID = rs2.getInt(1);
		        }
		
		        if(!(rowCount > 0 ) && !(rowCount2 > 0)){
		        	conn.close();
		        	con2.close();
		        	return false;
		        }else{
		        	conn.close();
		        	con2.close();
		        	return true;
		        }
		
		}catch(Exception e) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
			return true;
		}
	}
	
	public List<Comparacion> getComparaciones(){
		PreparedStatement query = null;
		ResultSet result = null;
		List<Comparacion> listaComparaciones = new ArrayList<Comparacion>();
		try {
			conn = getConnection();

			String sql = ("Select * from news_comparacion order by comparacion ASC;");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Comparacion comparacion = new Comparacion(result.getString(1),result.getString(2),result.getString(3));
				System.out.print(result.getInt(1));
				System.out.print(": ");
				System.out.println(result.getString(2));
				System.out.print(": ");
				System.out.println(result.getString(3));
			
				listaComparaciones.add(comparacion);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("Comparisons read successfully");
		return listaComparaciones;
	}
	
	public List<Comparacion> getComparacionesTitulos(){
	
		PreparedStatement query = null;
		ResultSet result = null;
		List<Comparacion> listaComparaciones = new ArrayList<Comparacion>();
		try {
			conn = getConnection();

			String sql = ("Select * from news_comparacion_titulos order by comparacion ASC;");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Comparacion comparacion = new Comparacion(result.getString(1),result.getString(2),result.getString(3));
				System.out.print(result.getInt(1));
				System.out.print(": ");
				System.out.println(result.getString(2));
				System.out.print(": ");
				System.out.println(result.getString(3));
			
				listaComparaciones.add(comparacion);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("Comparisons read successfully");
		return listaComparaciones;
	}
	
	public List<Comparacion> getComparacionesEntidades(String entidades){
		PreparedStatement query = null;
		ResultSet result = null;
		List<Comparacion> listaComparaciones = new ArrayList<Comparacion>();
		try {
			conn = getConnection();

			String sql = ("Select * from news_comparacion_entidades where tagsentidades='"+ entidades + "'order by comparacion ASC;");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Comparacion comparacion = new Comparacion(result.getString(2),result.getString(3),result.getString(5),result.getString(4));
				System.out.print(result.getInt(1));
				System.out.print(": ");
				System.out.println(result.getString(2));
				System.out.print(": ");
				System.out.println(result.getString(3));
			
				listaComparaciones.add(comparacion);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("Comparisons read successfully");
		return listaComparaciones;
	}

	public List<String> getEntidades(){
		PreparedStatement query = null;
		ResultSet result = null;
		List<String> listaEntidades = new ArrayList<String>();
		try {
			conn = getConnection();

			String sql = ("Select distinct tagsentidades from news_comparacion_entidades;");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				
				System.out.print(result.getString(1));
			
				listaEntidades.add(result.getString(1));
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("News read successfully");
		return listaEntidades;
	}
	
	public void UpdateTags(String idNoticia, String tags){
		
	      Statement stmt = null;
	      try {
	    	 conn = getConnection();
	         conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = conn.createStatement();
	         
	         String sql = (" update news_info set tagsentidades='"+ tags +"'where id='"+ idNoticia +"';");
	         
	         stmt.executeUpdate(sql);
	        
	         stmt.close();
	         conn.commit();
	         conn.close();
					
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("News update successfully");
		
	}
	
	public Feed getOneNew(String idNoticia){
		
		PreparedStatement query = null;
		ResultSet result = null;
		Feed Noticia = null;
		try {
			conn = getConnection();

			String sql = ("Select * from news_info where id="+ idNoticia +";");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Noticia = new Feed(result.getString(3),result.getString(4),result.getString(2),"", result.getString(5),result.getString(6), result.getString(1), result.getString(9), result.getString(7));
				System.out.print(result.getInt(1));
				System.out.print(": ");
				System.out.println(result.getString(2));
				System.out.print(": ");
				System.out.println(result.getString(3));
				System.out.print(": ");
				System.out.println(result.getString(4));
				System.out.print(": ");
				System.out.println(result.getString(5));
				System.out.print(": ");
				System.out.println(result.getString(6));
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("News read successfully");
		return Noticia;
	}
	
	public  List<Feed> getNews(int periodico) {

		PreparedStatement query = null;
		ResultSet result = null;
		List<Feed> listNews = new ArrayList<Feed>();
		try {
			conn = getConnection();

			String sql = ("Select * from news_info where id_newspaper="+ periodico +";");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Feed new_notice = new Feed(result.getString(3),result.getString(4),result.getString(2),"", result.getString(5),result.getString(6), result.getString(1),  result.getString(9), result.getString(7));
				System.out.print(result.getInt(1));
				System.out.print(": ");
				System.out.println(result.getString(2));
				System.out.print(": ");
				System.out.println(result.getString(3));
				System.out.print(": ");
				System.out.println(result.getString(4));
				System.out.print(": ");
				System.out.println(result.getString(5));
				System.out.print(": ");
				System.out.println(result.getString(6));
				listNews.add(new_notice);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("News read successfully");
		return listNews;
	}
	
	public  List<Feed> getAllNews() {
		Connection conn = null;
		PreparedStatement query = null;
		ResultSet result = null;
		List<Feed> listNews = new ArrayList<Feed>();
		try {
			conn = getConnection();

			String sql = ("Select * from news_info;");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Feed new_notice = new Feed(result.getString(3),result.getString(4),result.getString(2),"", result.getString(5),result.getString(6), result.getString(1), result.getString(9), result.getString(7));
				System.out.print(result.getInt(1));
				System.out.print(": ");
				System.out.println(result.getString(2));
				System.out.print(": ");
				System.out.println(result.getString(3));
				System.out.print(": ");
				System.out.println(result.getString(4));
				System.out.print(": ");
				System.out.println(result.getString(5));
				System.out.print(": ");
				System.out.println(result.getString(6));
				System.out.print(": ");
				System.out.println(result.getString(9));
				listNews.add(new_notice);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {

			try {

				if (query != null) {
					query.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DBconnection.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		System.out.println("News read successfully");
		return listNews;
	}
	
	
	private String parseQuotes(String texto){
		return  texto.replaceAll("'", "''");	
	}

}
