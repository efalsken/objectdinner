package com.androidstartup.provider;

import java.util.List;

import android.content.Context;

import com.androidstartup.model.Dinner;
import com.db4o.android.Db4oHelper;
import com.db4o.query.Constraint;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.google.android.maps.GeoPoint;

public class DinnerProvider extends Db4oHelper<Dinner> {

	private static DinnerProvider provider = null;

	public DinnerProvider(Context ctx) {
		super(ctx);
	}

	public static DinnerProvider getInstance(Context ctx) {
		if (provider == null)
			provider = new DinnerProvider(ctx);
		return provider;
	}	
	
	public List<Dinner> getAll() {
		return super.getAll(Dinner.class);
	}
	
	public int size() {
		return getAll(Dinner.class).size();
	}
	
	public Dinner get(String title) {
		//return get(new Dinner(title));
		return get(Dinner.class, "name", title);
	}
	
	public void delete(String title){
		//delete(new Dinner(title));
		delete(Dinner.class, "name", title);
	}

	@SuppressWarnings("serial")
	public List<Dinner> getNearby(final GeoPoint mapCenter,
			final int latitudeSpan, final int longitudeSpan) {
		return db().query(new Predicate<Dinner>() {
			public boolean match(Dinner candidate) {
				boolean inLatitude = (candidate.latitude <= (mapCenter
						.getLatitudeE6() + latitudeSpan / 2))
						&& (candidate.latitude >= (mapCenter.getLatitudeE6() - latitudeSpan / 2));
				boolean inLongitude = (candidate.longitude <= (mapCenter
						.getLongitudeE6() + longitudeSpan / 2))
						&& (candidate.longitude >= (mapCenter.getLongitudeE6() - longitudeSpan / 2));
				return inLatitude && inLongitude;
			}
		});
	}

	@SuppressWarnings("serial")
	public List<Dinner> getNearby(final GeoPoint mapCenter,
			final int tolerance) {
		return db().query(new Predicate<Dinner>() {
			public boolean match(Dinner candidate) {
				boolean inLatitude = (candidate.latitude <= (mapCenter
						.getLatitudeE6() + tolerance))
						&& (candidate.latitude >= (mapCenter.getLatitudeE6() - tolerance));
				boolean inLongitude = (candidate.longitude <= (mapCenter
						.getLongitudeE6() + tolerance))
						&& (candidate.longitude >= (mapCenter.getLongitudeE6() - tolerance));
				return inLatitude && inLongitude;
			}
		});
	}

	//@SuppressWarnings("serial")
	public List<Dinner> getByKeyword(final String keyword) {
		Query q = db().query();
		q.constrain(Dinner.class);
		Constraint first = q.descend("name").constrain(keyword).contains();
		Constraint second = q.descend("description").constrain(keyword).contains().or(first);
		q.descend("hostedName").constrain(keyword).or(second);
		return q.execute();
		/*return db().query(new Predicate<Dinner>() {
			public boolean match(Dinner candidate) {
				return candidate.getName().toLowerCase().contains(keyword)
						|| candidate.getDescription().toLowerCase().contains(
								keyword);
			}
		});*/
	}
}
