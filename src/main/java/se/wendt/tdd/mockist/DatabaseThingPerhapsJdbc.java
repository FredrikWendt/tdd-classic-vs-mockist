package se.wendt.tdd.mockist;

/**
 * This level of the persistence layer doesn't know what it stores.
 */
public interface DatabaseThingPerhapsJdbc {

	void save(Object thingToSave) throws DatabaseThingException;

	/**
	 * Returns the requested object, or throws exception. Will never return null.
	 */
    Object getObjectByTypeAndId(Class<?> type, Object id) throws DatabaseThingException;

}
