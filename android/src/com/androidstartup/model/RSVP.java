package com.androidstartup.model;

import com.androidstartup.model.Dinner;

public class RSVP {
    private Dinner dinner;
    private String name;
    
    public RSVP(){
		
	}
    
    public RSVP(String name) {
        this.name = name;
    }

    public RSVP(Dinner dinner) {
        this.dinner = dinner;
    }
    
    public RSVP(String name, Dinner dinner) {
    	this.name = name;
        this.dinner = dinner;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	@Override
	public String toString() {		
		return name;
	}    

}