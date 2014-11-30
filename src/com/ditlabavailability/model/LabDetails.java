package com.ditlabavailability.model;

/**
 * Model class for a lab with a room, location and ID. Used primarily for
 * setting up the initial lab databases without specific lab data like
 * timestamps and availability
 * 
 * @author Alan Haverty
 *
 */
public class LabDetails {

	int id;
	String room;
	String Location;

	public LabDetails() {
	}

	public LabDetails(String room, String location) {
		this.room = room;
		this.Location = location;
	}

	public String getRoom() {
		return room;
	}

	public String getLocation() {
		return Location;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public void setLocation(String location) {
		Location = location;
	}

}