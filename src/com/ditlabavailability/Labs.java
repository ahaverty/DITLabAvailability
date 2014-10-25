package com.ditlabavailability;

public class Labs {
	
	int id;
	String room;
	String Location;
	
	// constructors
	public Labs() {
	}
	
	public Labs(String room, String location){
		this.room = room;
		this.Location = location;
	}

	// getters	
	public String getRoom() {
		return room;
	}
	
	public String getLocation() {
		return Location;
	}
	
	// setters

	public void setRoom(String room) {
		this.room = room;
	}

	public void setLocation(String location) {
		Location = location;
	}
	
	
	
}