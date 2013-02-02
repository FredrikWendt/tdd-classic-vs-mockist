package se.wendt.tdd.classic;

import se.wendt.tdd.mockist.DatabaseThingException;
import se.wendt.tdd.mockist.DatabaseThingPerhapsJdbc;

public class DatabaseThingThrowingExceptions implements DatabaseThingPerhapsJdbc {

	@Override
	public void save(Object thingToSave) throws DatabaseThingException {
		throw new DatabaseThingException("Failed to save " + thingToSave);
	}

	@Override
	public Object getObjectByTypeAndId(Class<?> type, Object id) throws DatabaseThingException {
		throw new DatabaseThingException("Failed to read " + type.getName() + " with id " + id);
	}

}
