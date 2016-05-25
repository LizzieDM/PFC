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

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/webservicedb",
					"postgres", "omairapostgres");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
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
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/webservicedb",
					"postgres", "omairapostgres");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return connection;
		}
		return connection;

	}

	public  List<NewsPaper> getNewsPapers() {
		Connection conn = null;
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
		System.out.println("Table created successfully");
		return newsPapers;
	}
	
	public void insert_feed(String description, String tittle, String author, String pub_date, int id_newspaper ) throws SecurityException, IOException{
		
		fileTxt = new FileHandler(
				"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
		  Connection conn = null;
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
		
		  Connection conn = null;
	      Statement stmt = null;
	      try {
	    	 conn = getConnection();
	         conn.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = conn.createStatement();
	         String sql = "INSERT INTO news_comparacion (id_noticia1,id_noticia2,comparacion) "
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
	
	public List<Comparacion> getComparaciones(){
		Connection conn = null;
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
		System.out.println("News read successfully");
		return listaComparaciones;
	}
	
	public Feed getOneNew(String idNoticia){
		Connection conn = null;
		PreparedStatement query = null;
		ResultSet result = null;
		Feed Noticia = null;
		try {
			conn = getConnection();

			String sql = ("Select * from news_info where id="+ idNoticia +";");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Noticia = new Feed(result.getString(3),result.getString(4),result.getString(2),"", result.getString(5),result.getString(6), result.getString(1));
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
		Connection conn = null;
		PreparedStatement query = null;
		ResultSet result = null;
		List<Feed> listNews = new ArrayList<Feed>();
		try {
			conn = getConnection();

			String sql = ("Select * from news_info where id_newspaper="+ periodico +";");
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			while (result.next()) {
				Feed new_notice = new Feed(result.getString(3),result.getString(4),result.getString(2),"", result.getString(5),result.getString(6), result.getString(1));
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
	
	private String parseQuotes(String texto){
		return  texto.replaceAll("'", "''");	
	}

}
