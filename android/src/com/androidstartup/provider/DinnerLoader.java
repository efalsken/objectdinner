package com.androidstartup.provider;

import android.content.Context;
import android.util.Log;

import com.androidstartup.model.Dinner;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DinnerLoader {

	private static final String TAG = "DinnerLoader";

	public static void load(Context ctx, ObjectContainer db) {

		ObjectSet<Dinner> objSet = db.query(Dinner.class);

		if (objSet.isEmpty()) {
			Dinner dinner = new Dinner("FishyNerd Dinner",
					37.8091011047, -122.416000366, "You are all invited",
					"German Viscuso");
			db.store(dinner);
			Log.i(TAG, "Load Dinner " + dinner.getName());
			
			dinner = new Dinner("Beach Dinner", 37.799800872802734,
					-122.40699768066406, "You are all invited",
					"German Viscuso");		
			db.store(dinner);
			Log.i(TAG, "Load Dinner " + dinner.getName());
			
			dinner = new Dinner("NerdChina Dinner", 37.792598724365234,
					-122.40599822998047, "You are all invited",
					"German Viscuso");
			db.store(dinner);
			Log.i(TAG, "Load Dinner " + dinner.getName());
			
			dinner = new Dinner("NerdyMoney Dinner", 37.79410171508789,
					-122.4010009765625, "You are all invited", "German Viscuso");
			db.store(dinner);
			Log.i(TAG, "Load Dinner " + dinner.getName());
			
			dinner = new Dinner("Versant Dinner", 37.524393,
					-122.256527255, "You are all invited", "German Viscuso");
			db.store(dinner);
			Log.i(TAG, "Load Dinner " + dinner.getName());
		}
	}
}
