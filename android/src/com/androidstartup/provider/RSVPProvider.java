package com.androidstartup.provider;

import java.util.List;

import android.content.Context;

import com.androidstartup.model.Dinner;
import com.androidstartup.model.RSVP;
import com.db4o.android.Db4oHelper;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

public class RSVPProvider extends Db4oHelper<RSVP> {
	
	private static RSVPProvider provider = null;

	public RSVPProvider(Context ctx) {
		super(ctx);		
	}
	
	public static RSVPProvider getInstance(Context ctx) {
		if (provider == null)
			provider = new RSVPProvider(ctx);
		return provider;
	}

	public void  store(String name, Dinner dinner) {
		RSVP rsvp = get(name);
		if (rsvp == null)
			rsvp = new RSVP(name, dinner);
		db().store(rsvp);
	}
	
	public List<RSVP> getAll() {
		return super.getAll(RSVP.class);
	}
	
	public int size() {
		return getAll(RSVP.class).size();
	}
	
	public RSVP get(String name) {
		return get(new RSVP(name));
	}
	
	public void delete(String name){
		delete(new RSVP(name));
	}
	
	@SuppressWarnings("serial")
	public List<RSVP> getByDinner(final String dinnerName) {
		return db().query(new Predicate<RSVP>(){
			@Override
			public boolean match(RSVP rsvp) {	
				if(rsvp.getDinner().getName().equalsIgnoreCase(dinnerName))
					return true;					
				return false;
			}},new QueryComparator<RSVP>() {
			public int compare(RSVP o, RSVP o1) {
				return o.getName().compareTo(o1.getName());
			}
		});
	}
}
