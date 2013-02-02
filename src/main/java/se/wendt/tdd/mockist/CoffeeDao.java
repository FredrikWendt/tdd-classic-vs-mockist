package se.wendt.tdd.mockist;

/*
 * this is the unit under test, probably implements some interface ...
 */
public class CoffeeDao {

	private DatabaseThingPerhapsJdbc db;

	public CoffeeDao(DatabaseThingPerhapsJdbc db) {
		this.db = db;
	}

	public void saveNewCoffeeBeanType(String name) throws PersistenceException {
		BeanType type = new BeanType(name);
		try {
			this.db.save(type);
		} catch (DatabaseThingException e) {
			throw new PersistenceException("Failed to save bean with name: " + name, e);
		}
	}
	
	/*
	 * Using two collaborators.
	public void saveNewCoffeeBeanTypes(String... names) throws PersistenceException {
		transactionManager.execute(new OneAtomicTransaction() {
			@Override
			public void with(TransactionContext context) {
				for (String name : names)  {
					saveNewCoffeeBeanType(name);
				}
			}
		});
	}
	 */

	public BeanType getCoffeeBeanTypeByName(String name) throws PersistenceException {
		try {
			return (BeanType) this.db.getObjectByTypeAndId(BeanType.class, name);
		} catch (DatabaseThingException e) {
			throw new PersistenceException("Failed to load bean with name: " + name, e);
		}
	}
}
