package com.news.model;

public class NewsSemEval {
	String id;
	String idnoticia1;
	String idnoticia2;
	
	public NewsSemEval(String idnoticia1,String idnoticia2,String id ){
		this.id = id;
		this.idnoticia1 = idnoticia1;
		this.idnoticia2 = idnoticia2;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIdnoticia1() {
		return idnoticia1;
	}


	public void setIdnoticia1(String idnoticia1) {
		this.idnoticia1 = idnoticia1;
	}


	public String getIdnoticia2() {
		return idnoticia2;
	}


	public void setIdnoticia2(String idnoticia2) {
		this.idnoticia2 = idnoticia2;
	}

	
}
