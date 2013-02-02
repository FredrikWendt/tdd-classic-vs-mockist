package se.wendt.tdd.classic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import org.junit.Test;

import se.wendt.tdd.mockist.BeanType;
import se.wendt.tdd.mockist.CoffeeDao;
import se.wendt.tdd.mockist.DatabaseThingException;
import se.wendt.tdd.mockist.PersistenceException;

public class CoffeeDao_ClassicApproach_Test {

	CoffeeDao testee;

	private DatabaseThingWithState dbWithEnoughPubliclyAccessibleState = new DatabaseThingWithState();
	private DatabaseThingThrowingExceptions dbThatThrowsExceptions = new DatabaseThingThrowingExceptions();

	private void setupTesteeWithDbThatThrowsException() {
		testee = new CoffeeDao(dbThatThrowsExceptions);
	}

	private void setupTesteeWithDbThatAllowsIntrospectionOfItsState() {
		testee = new CoffeeDao(dbWithEnoughPubliclyAccessibleState);
	}

	@Test
	public void typical_flow_with_saving_new_bean_type() throws Exception {
		// arrange
		setupTesteeWithDbThatAllowsIntrospectionOfItsState();
		assertThat(dbWithEnoughPubliclyAccessibleState.isEmpty(), is(true));

		// act
		testee.saveNewCoffeeBeanType("Name");

		// assert
		assertThat(dbWithEnoughPubliclyAccessibleState.isEmpty(), is(false));
	}

	@Test
	public void save_causing_exception_in_underlying_layer_is_wrapped_with_more_context() throws Exception {
		// arrange
		setupTesteeWithDbThatThrowsException();

		// act & assert in catch clause
		try {
			testee.saveNewCoffeeBeanType("Name");
			fail("Should've thrown some exception");
		} catch (PersistenceException e) {
			assertThat(e.getMessage(), containsString(": Name"));
			assertThat(e.getCause(), is(instanceOf(DatabaseThingException.class)));
		}
	}

	@Test
	public void typical_flow_with_reading_a_bean_type() throws Exception {
		// arrange
		setupTesteeWithDbThatAllowsIntrospectionOfItsState();
		// either this: testee.saveNewCoffeeBeanType("Name");
		// or
		dbWithEnoughPubliclyAccessibleState.save(new BeanType("Name"));

		// act
		BeanType beanType = testee.getCoffeeBeanTypeByName("Name");

		// assert
		assertThat(beanType, equalTo(new BeanType("Name")));
	}

	@Test
	public void read_causing_exception_in_lower_layer_is_wrapped_to_provide_more_context() throws Exception {
		// arrange
		setupTesteeWithDbThatThrowsException();

		// act & assert in catch clause
		try {
			testee.getCoffeeBeanTypeByName("Name");
			fail("We expected interaction with the db to throw an exception");
		} catch (PersistenceException e) {
			assertThat(e.getMessage(), containsString(": Name"));
			assertThat(e.getCause(), is(instanceOf(DatabaseThingException.class)));
		}
	}

}
