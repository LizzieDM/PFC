package com.news.meaning;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MeaningEntities {
		ArrayList<String> entities = null;
	
		public ArrayList<String> getMeaningEntities(String texto){
			entities = new ArrayList<String>();
			try {
				// We define the variables needed to call the API
			      String api = "http://api.meaningcloud.com/topics-2.0";
			      String key = "ef725bb46cf4a8143250a6bffee56067";//<<<your license key>>>
			      //String txt = "Este martes se ha inaugurado la tercera edición del Salón Gastronómico de Canarias, un evento especializado que incluye el campeonato regional absoluto de cocineros de Canarias y actividades paralelas";
			      String lang = "es"; // es/en/fr/it/pt/ca

			      Post post = new Post (api);
			      post.addParameter("key", key);
			      post.addParameter("txt", texto);
			      post.addParameter("lang", lang);
			      post.addParameter("tt", "a");
			      post.addParameter("of", "xml");
			      String response = post.getResponse();
			      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			      Document doc = docBuilder.parse(new ByteArrayInputStream(response.getBytes("UTF-8")));
			      doc.getDocumentElement().normalize();
			      Element response_node = doc.getDocumentElement();
			      System.out.println("\nInformation:");
			      System.out.println("----------------\n");
			      try {
			        NodeList status_list = response_node.getElementsByTagName("status");
			        Node status = status_list.item(0);
			        NamedNodeMap attributes = status.getAttributes();
			        Node code = attributes.item(0);
			        if(!code.getTextContent().equals("0")) {
			          System.out.println("Not found");
			        } else {
			        	if(printInfoEntityConcept(response_node, "entity") != null){
			        		entities.addAll(printInfoEntityConcept(response_node, "entity"));
			        	}
//			        	if(printInfoEntityConcept(response_node, "concept") != null){
//			        		entities.addAll(printInfoEntityConcept(response_node, "concept"));
//			        	}
			        	if(printInfoGeneral(response_node, "time_expression") != null){
			        		entities.addAll(printInfoGeneral(response_node, "time_expression"));
			        	}
			        	if(printInfoGeneral(response_node, "money_expression") != null){
			        		entities.addAll(printInfoGeneral(response_node, "money_expression"));
			        	}
			        	if(printInfoGeneral(response_node, "quantity_expression") != null){
			        		entities.addAll(printInfoGeneral(response_node, "quantity_expression"));
			        	}
//			        	if(printInfoGeneral(response_node, "other_expression") != null){
//			        		entities.addAll(printInfoGeneral(response_node, "other_expression"));
//			        	}
			        	
			        	if(printInfoGeneral(response_node, "relation_list") != null){
			        		entities.addAll(printInfoGeneral(response_node, "relation_list"));
			        	}
			        }
			        return entities;
			      }catch (Exception e) {
						System.out.println("Error obteniendo las Entities 1");
				  }
			      
			} catch (Exception e) {
				System.out.println("Error obteniendo las Entities 2");
			}
			
			      return null;
		}
		
			  /**
		     * print the information of the entities and concepts
		     * @param response main element of the response
		     * @param nameNode name of the node to process (entity or concept)
		     * @return the information of the nodes
		     */
		    public static ArrayList<String> printInfoEntityConcept(Element response, String nameNode) {
		    	ArrayList<String> output = new ArrayList<String>();
		      NodeList topic_list = response.getElementsByTagName(nameNode+"_list");
		      if(topic_list.getLength()>0){
		        Node topics = topic_list.item(0);
		        NodeList topic = topics.getChildNodes();
		        for(int i=0; i<topic.getLength(); i++) {
		          Node topic_item = topic.item(i);
		          NodeList child_topic = topic_item.getChildNodes();
		          String form = "";
		          for(int j=0; j<child_topic.getLength(); j++){
		            Node info_topic = child_topic.item(j);
		            String name = info_topic.getNodeName();
		            if(name.equals("form"))
		              form = info_topic.getTextContent();
		            else if(name.equals("sementity")) { 
		              NodeList child_sementity = info_topic.getChildNodes();
		              for(int l=0; l<child_sementity.getLength(); l++){
		                Node info_sementity = child_sementity.item(l);
		              }
		            }
		          }
		          
					byte[] texto_bytes = form.getBytes();
					String texto_decode = decodeUTF8(texto_bytes);
					System.out.println("Texto decodificado:" + texto_decode);
		          output.add(texto_decode);
		        }
		      }
		      if(output.isEmpty())
		        output = null;
		      return output;
		    } //printInfoEntityConcept
		    
		    
		    
		    /**
		     * print the general information of a topic
		     * @param response main element of the response
		     * @param nameNode name of the node to process
		     * @return the information of the nodes
		     */
		    public static ArrayList<String> printInfoGeneral(Element response, String nameNode) {
		    	ArrayList<String> output = new ArrayList<String>();
		      NodeList topic_list = response.getElementsByTagName(nameNode+"_list");
		      if(topic_list.getLength()>0){
		        Node topics = topic_list.item(0);
		        NodeList topic = topics.getChildNodes();
		        for(int i=0; i<topic.getLength(); i++) {
		          Node topic_item = topic.item(i);
		          NodeList child_topic = topic_item.getChildNodes();
		          String form = "";
		          String type = "";
		          for(int j=0; j<child_topic.getLength(); j++){
		            Node info_topic = child_topic.item(j);
		            if(info_topic.getNodeName().equals("form"))
		              form = info_topic.getTextContent();
		            else if(info_topic.getNodeName().equals("type")) 
		              type = info_topic.getTextContent();
		          }
		          byte[] texto_bytes = form.getBytes();
					String texto_decode = decodeUTF8(texto_bytes);
					System.out.println("Texto decodificado:" + texto_decode);
		          output.add(texto_decode);
		          
		        }
		      }
		      if(output.isEmpty())
		        output = null;
		      return output;
		    } //printInfoGeneral
		    
		    private final static Charset UTF8_CHARSET = Charset.forName("UTF-8");

			static String decodeUTF8(byte[] bytes) {
			    return new String(bytes, UTF8_CHARSET);
			}

			byte[] encodeUTF8(String string) {
			    return string.getBytes(UTF8_CHARSET);
			}
			
			
}
