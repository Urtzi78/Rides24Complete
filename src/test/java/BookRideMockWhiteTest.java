import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class BookRideMockWhiteTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;

	@Before
	public void init() {
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
	// sut.bookRide: Ride null da. Testak false itzuli behar du.
	public void test1() {
		String username = "TestTraveler";
		Ride ride = null;
		int seats = 3;
		double desk = 0;

		sut.open();
		boolean result = sut.bookRide(username, ride, seats, desk);
		sut.close();

		assertFalse(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	// sut.bookRide: Traveler ez da existitzen DBan. Testak false itzuli behar du.
	public void test2() {
		String nonExistentUsername = "NonExistentTraveler";
		Ride ride = Mockito.mock(Ride.class);
		int seats = 2;
		double desk = 0;

		TypedQuery<Traveler> query = Mockito.mock(TypedQuery.class);
		Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("username", nonExistentUsername)).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenThrow(new javax.persistence.NoResultException());

		sut.open();
		boolean result = sut.bookRide(nonExistentUsername, ride, seats, desk);
		sut.close();

		assertFalse(result);
	}

	@Test
	// sut.bookRide: Erreserbatu daitezkeen eserlekuak baina gehiago erreserbatzen
	// saiatzen ari da erabiltzailea. Testak false itzuli behar du.
	public void test3() {
		String username = "TestTraveler";
		Ride ride = Mockito.mock(Ride.class);
		int seats = 4;
		double desk = 0;

		Traveler traveler = Mockito.mock(Traveler.class);
		Mockito.when(db.find(Traveler.class, username)).thenReturn(traveler);
		Mockito.when(ride.getnPlaces()).thenReturn(3);

		sut.open();
		boolean result = sut.bookRide(username, ride, seats, desk);
		sut.close();

		assertFalse(result);
	}

	@Test
	// sut.bookRide: Travelerak ez dauka erreserba egiteko nahikoa diru.
	// Testak false itzuli behar du.
	public void test4() {
		String username = "TestTraveler";
		Ride ride = Mockito.mock(Ride.class);
		int seats = 2;
		double desk = 0;

		Traveler traveler = Mockito.mock(Traveler.class);
		Mockito.when(db.find(Traveler.class, username)).thenReturn(traveler);
		Mockito.when(ride.getnPlaces()).thenReturn(5);
		Mockito.when(ride.getPrice()).thenReturn(100.0);
		Mockito.when(traveler.getMoney()).thenReturn(10.0);

		sut.open();
		boolean result = sut.bookRide(username, ride, seats, desk);
		sut.close();

		assertFalse(result);
	}

	@Test
	// sut.bookRide: ride ez da null, erabiltzailea DBan existitzen da, erreserbatu
	// daitezkeen eserlekuak baino gutxiago erreserbatu nahi dira eta travelerrak
	// nahikoa diru du erreserba ordaintzeko. Testak true itzuli behar du.
	public void test5() {
		String username = "TestTraveler";
		Ride ride = Mockito.mock(Ride.class);
		int seats = 2;
		double desk = 5.0;

		Traveler traveler = Mockito.mock(Traveler.class);
		Mockito.when(db.find(Traveler.class, username)).thenReturn(traveler);
		Mockito.when(ride.getnPlaces()).thenReturn(5);
		Mockito.when(ride.getPrice()).thenReturn(50.0);
		Mockito.when(traveler.getMoney()).thenReturn(1000.0);

		sut.open();
		boolean result = sut.bookRide(username, ride, seats, desk);
		sut.close();

		assertTrue(result);
		Mockito.verify(ride).setnPlaces(3);
		Mockito.verify(traveler).setMoney(910.0);

	}
}