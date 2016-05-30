package com.news.model;

import java.util.ArrayList;
import java.util.List;



public class Feed {
	 String id;
	  String title;
	   String link;
	   String description;
	   String language;
	   String copyright;
	   String pubDate;
	   String tagsEntities;
	   String periodico;

	  final List<FeedMessage> entries = new ArrayList<FeedMessage>();

	   
	  public Feed(String title, String link, String description, String language, String copyright, String pubDate, String idnoticia, String tags, String periodico) {
		  this.id = idnoticia;
	    this.title = title;
	    this.link = link;
	    this.description = description;
	    this.language = language;
	    this.copyright = copyright;
	    this.pubDate = pubDate;
	    this.tagsEntities= tags;
	    this.periodico = periodico;
	  }

	  

	public List<FeedMessage> getMessages() {
	    return entries;
	  }

	  public String getTitle() {
	    return title;
	  }
	  
	  public String getId() {
			return id;
		}

	  public String getLink() {
	    return link;
	  }

	  public String getDescription() {
	    return description;
	  }

	  public String getLanguage() {
	    return language;
	  }

	  public String getCopyright() {
	    return copyright;
	  }

	  public String getPubDate() {
	    return pubDate;
	  }
	  
	  public String getTags() {
		    return tagsEntities;
		  }

	  public String getIdPeriodico() {
		    return periodico;
		  }
	  
	  @Override
	  public String toString() {
	    return "Feed [copyright=" + copyright + ", description=" + description
	        + ", language=" + language + ", link=" + link + ", pubDate="
	        + pubDate + ", title=" + title + "]";
	  }

}
