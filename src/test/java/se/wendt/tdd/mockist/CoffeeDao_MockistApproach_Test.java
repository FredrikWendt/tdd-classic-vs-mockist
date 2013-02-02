package se.wendt.tdd.mockist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CoffeeDao_MockistApproach_Test {

	@InjectMocks
	CoffeeDao testee;

	@Mock
	DatabaseThingPerhapsJdbc db;

	@Test
	public void typical_flow_with_saving_new_bean_type() throws Exception {
		testee.saveNewCoffeeBeanType("Name");

		verify(db).save(eq(new BeanType("Name")));
	}

	@Test
	public void save_causing_exception_in_underlying_layer_is_wrapped_with_more_context() throws Exception {
		DatabaseThingException exception = new DatabaseThingException("Bean type name may not be unique");

		doThrow(exception).when(db).save(any(BeanType.class));

		try {
			testee.saveNewCoffeeBeanType("Name");
			fail("Should've thrown some exception");
		} catch (PersistenceException e) {
			assertThat(e.getMessage(), containsString(": Name"));
			assertThat(e.getCause(), sameInstance((Throwable) exception));
		}
	}
	
	/*
	@Test
	public void bean_type_names_must_be_unique() throws Exception {
		// involve a transaction, make sure rollback happens after second call to collaborator B throws exception
	}
	*/

	@Test
	public void typical_flow_with_reading_a_bean_type() throws Exception {
		given(db.getObjectByTypeAndId(BeanType.class, "Name")).willReturn(new BeanType("Name"));

		BeanType beanType = testee.getCoffeeBeanTypeByName("Name");

		assertThat(beanType, equalTo(new BeanType("Name")));
		verify(db).getObjectByTypeAndId(BeanType.class, "Name");
	}

	@Test
	public void read_causing_exception_in_lower_layer_is_wrapped_to_provide_more_context() throws Exception {
		DatabaseThingException dbException = new DatabaseThingException("Bean type may be unrecognized");
		given(db.getObjectByTypeAndId(BeanType.class, "Name")).willThrow(dbException);

		try {
			testee.getCoffeeBeanTypeByName("Name");
			fail("We expected interaction with the db to throw an exception");
		} catch (PersistenceException e) {
			assertThat(e.getMessage(), containsString(": Name"));
			assertThat(e.getCause(), is(sameInstance((Throwable) dbException)));
		}
	}

}
