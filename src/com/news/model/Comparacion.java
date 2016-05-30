package com.news.model;

import java.util.ArrayList;
import java.util.List;

public class Comparacion {
	String idNoticia1;
	String idNoticia2;
	String valor;
	String tagsEntidades;
	
	 final List<Comparacion> comparaciones = new ArrayList<Comparacion>();

	  public Comparacion(String idNoticia1, String idNoticia2, String Valor) {
		this.idNoticia1 = idNoticia1;
		this.idNoticia2 = idNoticia2;
		this.valor = Valor;
	  }

	  public Comparacion(String idNoticia1, String idNoticia2, String Valor, String tagsEntidades) {
			this.idNoticia1 = idNoticia1;
			this.idNoticia2 = idNoticia2;
			this.valor = Valor;
			this.tagsEntidades= tagsEntidades;
		  }
	  
	  public List<Comparacion> getMessages() {
	    return comparaciones;
	  }

	
	
	public String getIdNoticia1() {
		return idNoticia1;
	}
	public void setIdNoticia1(String idNoticia1) {
		this.idNoticia1 = idNoticia1;
	}
	public String getIdNoticia2() {
		return idNoticia2;
	}
	public void setIdNoticia2(String idNoticia2) {
		this.idNoticia2 = idNoticia2;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getTagsEntidades() {
		return tagsEntidades;
	}
	public void setTagsEntidades(String tagsEntidades) {
		this.tagsEntidades = tagsEntidades;
	}
	

}
