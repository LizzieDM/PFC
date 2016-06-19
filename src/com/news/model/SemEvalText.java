package com.news.model;

import java.util.ArrayList;
import java.util.List;

public class SemEvalText {

	 String id;
	  String texto1;
	   String tags;
	   String texto2;
	   String size;
	   String size2;
	  

	  final List<SemEvalText> entries = new ArrayList<SemEvalText>();

	   
	  public SemEvalText(String id, String texto1, String tags, String texto2,String size, String size2 ) {
		this.id = id;
	    this.texto1 = texto1;
	    this.tags = tags;
	    this.texto2 = texto2;
	    this.size = size;
	    this.size2 = size2;
	  }

	  

	public List<SemEvalText> getMessages() {
	    return entries;
	  }

	  public String getTexto1() {
	    return texto1;
	  }
	  
	  public String getId() {
			return id;
		}

	  public String getTexto2() {
	    return texto2;
	  }

	  public String getTags() {
	    return tags;
	  }
	
}
