package com.androidstartup.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.location.Address;


public class Dinner extends MapBookmark {
	private Date eventDate = new Date();
	private String hostedName = "";
	private String contactInfo = "";
	private String address = "";
	private String country = "";
	private final List<RSVP> attendees = new ArrayList<RSVP>();
	
	public Dinner(){
		
	}
			
	public Dinner(String title, double d, double e, String description,
			String hostedBy) {
		super(title, d, e);
		this.hostedName = hostedBy;
		this.description = description;
	}
	
	public Dinner(Address address) {
		this.name = address.getAddressLine(0);
		this.latitude = (int)(1E6 * address.getLatitude());
		this.longitude = (int)(1E6 * address.getLongitude());
		this.zoomLevel = 15;
	}

	public Dinner(String title) {
		super(title);
	}

	public String getTitle() {
		return getName();
	}

	public void setTitle(String title) {
		setName(title);
	}

	public Date getEventDate() {
		return new Date(eventDate.getTime());
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public String getHostedName() {
		return hostedName;
	}

	public void setHostedName(String hostedName) {
		this.hostedName = hostedName;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<RSVP> getAttendees() {
		return Collections.unmodifiableList(attendees);
	}

	public void addAttendee(RSVP rsvp) {
		attendees.add(rsvp);
	}

	public RSVP createRSVP() {
		RSVP rsvp = new RSVP(this);
		addAttendee(rsvp);
		return rsvp;
	}

	public void removeRSVP(RSVP currentRSVP) {
		this.attendees.remove(currentRSVP);
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
