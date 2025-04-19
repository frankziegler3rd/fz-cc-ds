/*
 * Frank Ziegler, Calen Cuesta -- Assignment 5
 */
package com.Assignment5Server.Assignment5Server;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pokemon{

	@JsonProperty("name")
	private String name;
	@JsonProperty("shiny")
	private boolean shiny;
	@JsonProperty("type")
	private String type;
	
	public Pokemon(String name, boolean shiny, String type){
		this.name = name;
		this.shiny = shiny;
		this.type = type;
	}
	
	
	public String getName(){
		return this.name;
	}
	
	public boolean getShiny(){
		return this.shiny;
	}
	
	public String getType(){
		return this.type;
	}
}
