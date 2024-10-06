import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.Date;
import java.util.Collections;

public class BookRideMockBlackTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;

	@Mock
	protected EntityManager db;

	@Mock
	protected EntityTransaction et;

	@Mock
	protected TypedQuery<Traveler> typedQueryTraveler;

	@Mock
	protected TypedQuery<Ride> typedQueryRide;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);

		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();

		sut = new DataAccess(db);
	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}

	@Test
	// 1. Dena ondo funtzionatu du, arazorik gabe.
	public void test1() {
		String username = "TestTraveler";
		Driver driver = new Driver("TestDriver", "password");
		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, driver);
		Traveler traveler = new Traveler(username, "password");
		traveler.setMoney(1000.0);

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(traveler));

		Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.id = :rideId", Ride.class))
				.thenReturn(typedQueryRide);
		Mockito.when(typedQueryRide.setParameter("rideId", ride.getRideNumber())).thenReturn(typedQueryRide);
		Mockito.when(typedQueryRide.getResultList()).thenReturn(Collections.singletonList(ride));

		Mockito.doNothing().when(db).persist(Mockito.any());

		sut.open();
		boolean result = sut.bookRide(username, ride, 2, 5.0);
		sut.close();

		assertTrue(result);
		assertEquals(3, ride.getnPlaces());
		assertEquals(910.0, traveler.getMoney(), 0.01);
		assertFalse(traveler.getBookedRides().isEmpty());
	}

	@Test
	// 2. username hori duen User-a ez da existitzen datu basean
	public void test2() {
		String username = "NonExistentUser";
		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, new Driver("TestDriver", "password"));

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.emptyList());

		sut.open();
		boolean result = sut.bookRide(username, ride, 2, 0);
		sut.close();

		assertFalse(result);
	}

	@Test
	// 3. username null da
	public void test3() {
		Driver driver = new Driver("TestDriver", "password");
		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, driver);

		sut.open();
		boolean result = sut.bookRide(null, ride, 2, 50.0);
		sut.close();

		assertFalse(result);
	}

	@Test
	// 4. ride ez dago datu basean
	public void test4() {
		String username = "TestTraveler";
		Traveler traveler = new Traveler(username, "password");
		traveler.setMoney(1000.0);

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(traveler));

		Mockito.when(db.createQuery("SELECT r FROM Ride r WHERE r.id = :rideId", Ride.class))
				.thenReturn(typedQueryRide);
		Mockito.when(typedQueryRide.setParameter("rideId", 1L)).thenReturn(typedQueryRide);
		Mockito.when(typedQueryRide.getResultList()).thenReturn(Collections.emptyList());

		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, new Driver("TestDriver", "password"));
		sut.open();
		boolean result = sut.bookRide(username, ride, 2, 50.0);
		sut.close();

		assertFalse(result);
	}

	@Test
	// 5. ride null da
	public void test5() {
		String username = "TestTraveler";
		Traveler traveler = new Traveler(username, "password");
		traveler.setMoney(1000.0);

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(traveler));

		sut.open();
		boolean result = sut.bookRide(username, null, 2, 50.0);
		sut.close();

		assertFalse(result);
	}

	@Test
	// 6. seats 0 edo txikiagoa da
	public void test6() {
		String username = "TestTraveler";
		Driver driver = new Driver("TestDriver", "password");
		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, driver);
		Traveler traveler = new Traveler(username, "password");
		traveler.setMoney(1000.0);

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(traveler));

		sut.open();
		boolean resultZeroSeats = sut.bookRide(username, ride, 0, 50.0);
		boolean resultNegativeSeats = sut.bookRide(username, ride, -2, 50.0);
		sut.close();

		assertFalse(resultZeroSeats);
		assertFalse(resultNegativeSeats);
	}

	@Test
	// 7. desk negatiboa da
	public void test7() {
		String username = "TestTraveler";
		Driver driver = new Driver("TestDriver", "password");
		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, driver);
		Traveler traveler = new Traveler(username, "password");
		traveler.setMoney(1000.0);

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(traveler));

		sut.open();
		boolean result = sut.bookRide(username, ride, 2, -5.0);
		sut.close();

		assertFalse(result);
	}

	@Test
	// 8. desk ride-ren prezioa baino handiagoa da
	public void test8() {
		String username = "TestTraveler";
		Driver driver = new Driver("TestDriver", "password");
		Ride ride = new Ride("Donostia", "Bilbo", new Date(), 5, 50, driver);
		Traveler traveler = new Traveler(username, "password");
		traveler.setMoney(1000.0);

		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
		Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(traveler));

		sut.open();
		boolean result = sut.bookRide(username, ride, 2, 100.0);
		sut.close();

		assertFalse(result);
	}
}