package com.news.model;

import java.util.ArrayList;
import java.util.List;

public class ContenidoPI {

	 String id;
	  String texto;
	  String periodico;
	   String tags;
	   
	  

	  final List<ContenidoPI> entries = new ArrayList<ContenidoPI>();

	   
	  public ContenidoPI(String id, String texto, String periodico, String tags) {
		this.id = id;
	    this.texto = texto;
	    this.tags = tags;
	    this.periodico = periodico;
	    
	  }

	  

	public List<ContenidoPI> getMessages() {
	    return entries;
	  }

	  public String getTexto1() {
	    return texto;
	  }
	  
	  public String getId() {
			return id;
		}

	  public String getPeriodico() {
	    return periodico;
	  }

	  public String getTags() {
	    return tags;
	  }
	
}
