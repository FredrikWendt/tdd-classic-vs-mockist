package se.wendt.tdd.classic;

import java.util.HashMap;
import java.util.Map;

import se.wendt.tdd.mockist.DatabaseThingException;
import se.wendt.tdd.mockist.DatabaseThingPerhapsJdbc;

public class DatabaseThingWithState implements DatabaseThingPerhapsJdbc {

	private Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public void save(Object thingToSave) throws DatabaseThingException {
		map.put("" + thingToSave.getClass().toString() + thingToSave.toString(), thingToSave);
	}

	@Override
	public Object getObjectByTypeAndId(Class<?> type, Object id) throws DatabaseThingException {
		Object object = map.get("" + type + id);
		if (object != null) {
			return object;
		}
		throw new DatabaseThingException("Failed to find object");
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

}
