package com.db4o.android;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.androidstartup.model.Dinner;
import com.androidstartup.model.RSVP;
import com.androidstartup.provider.DinnerLoader;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

/**
 * @author German Viscuso
 * 
 */
public class Db4oHelper<T> {

	private static ObjectContainer oc = null;
	private Context context;
	private final String dbName = "nerddinner.db4o";

	public Db4oHelper(Context ctx) {
		context = ctx;
	}

	public ObjectContainer db() {
		try {
			if (oc == null || oc.ext().isClosed()) {
				//new java.io.File(db4oDBFullPath(context)).delete();
				oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
				DinnerLoader.load(context, oc);
			}
			return oc;
		} catch (Exception e) {
			Log.e(Db4oHelper.class.getName(), e.toString());
			return null;
		}
	}
	
	public void setDb(ObjectContainer container){
		oc = container;
	}
	
	private EmbeddedConfiguration dbConfig() throws IOException {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.common().objectClass(Dinner.class).callConstructor(true);
		configuration.common().objectClass(RSVP.class).callConstructor(true);
		//configuration.common().updateDepth(2);
		configuration.common().objectClass(Dinner.class).objectField("name").indexed(true);
		configuration.common().objectClass(Dinner.class).objectField("eventDate").indexed(true);
		configuration.common().objectClass(RSVP.class).objectField("name").indexed(true);
		configuration.common().objectClass(Dinner.class).cascadeOnUpdate(true);
		configuration.common().objectClass(Dinner.class).cascadeOnDelete(true);				
		configuration.common().objectClass(Dinner.class).cascadeOnActivate(true);
		return configuration;
	}

	private String db4oDBFullPath(Context ctx) {
		return ctx.getDir("data", 0) + "/" + dbName;
	}
	
	/**
	 * Generic operations
	 */
	
	public void store(T object) {
		db().store(object);	
	}
	
	public T get(T prototype) {
		T result = null;
		ObjectSet<T> objSet = db().queryByExample(prototype);
		if (objSet.hasNext()) {
			result = objSet.next();
		}
		return result;
	}
	
	public T get(Class<T> c, String fieldName, String constraint){
		Query q = db().query();
		q.constrain(c);
		q.descend(fieldName).constrain(constraint);
		ObjectSet<T> result = q.execute();
		if(result.hasNext())
			return result.next();
		return null;
	}
	
	public List<T> getAll(Class<T> type) {
		return db().queryByExample(type);
	}
	
	public void deleteAll(Class<T> c) {
		ObjectSet<T> set = db().queryByExample(c);
		while (set.hasNext()) {
			db().delete(set.next());
		}
	}
	
	public void delete(T prototype) {
		T object = get(prototype);
		if (object != null) {
			db().delete(object);
		}
	}
	
	public void delete(Class<T> c, String fieldName, String constraint) {
		T object = get(c, fieldName, constraint);
		if (object != null) {
			db().delete(object);
		}
	}
	
	public int size(Class<T> type) {
		return getAll(type).size();
	}
	
	public void close() {
		if (oc != null)
			oc.close();
	}	

}
