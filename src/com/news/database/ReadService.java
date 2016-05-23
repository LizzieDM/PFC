package com.news.database;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.news.model.Feed;
import com.news.model.NewsPaper;
import com.news.model.RSSFeedParser;


public class ReadService {
	
	private final static Logger log = Logger.getLogger(ReadService.class
			.getName());
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	
	
	public void readBdInsert() throws SecurityException, IOException{
		fileTxt = new FileHandler(
				"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
		DBconnection conexionDB = new DBconnection();
		try {
			conexionDB.test_connection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<NewsPaper> newspaperUrls = conexionDB.getNewsPapers();
		for (int i = 0; i < newspaperUrls.size(); i++) {

			RSSFeedParser parser = new RSSFeedParser(newspaperUrls.get(i)
					.getUrl());
			Feed feed = parser.readFeed();

			for (int i1 = 0; i1 < feed.getMessages().size(); i1++) {

				if (feed.getMessages().get(i1).getPubDate() != null) {
					System.out.println("La fecha no es nula");
					conexionDB.insert_feed(feed.getMessages().get(i1)
							.getDescription(), feed.getMessages().get(i1)
							.getTitle(),
							feed.getMessages().get(i1).getAuthor(), feed
									.getMessages().get(i1).getPubDate(),
							newspaperUrls.get(i).getId());
				}

			}

		}

		
	}

}
