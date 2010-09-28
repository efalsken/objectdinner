package info.gamlor.icoodb.web.model;

import info.gamlor.icoodb.web.db.AutoIncrement;

public abstract class IDHolder {
    @AutoIncrement
	private int id;

	public int getId() {
		return id;
	}
}
