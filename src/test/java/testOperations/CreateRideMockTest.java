package testOperations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.CreateRideParameter;
import dataAccess.DataAccess;
import domain.Discount;
import domain.Driver;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class CreateRideMockTest {

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
	public void test4() {
		String driverUsername = "Driver Test";
		String driverPassword = "123";
		String rideFrom = "Donostia";
		String rideTo = "Zarautz";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Driver driver = new Driver(driverUsername, driverPassword);
			driver.addRide(rideFrom, rideTo, rideDate, 2, 10);
			// configure the state through mocks
			Mockito.when(db.find(Driver.class, driverUsername)).thenReturn(driver);
			// equivalent to
			// Mockito.doReturn(driver).when (db).find (Driver.class, driverUsername);
			// invoke System Under Test (sut)
			sut.open();
			sut.createRide(new CreateRideParameter(rideFrom, rideTo, rideDate, 0, 0, driverUsername));
			sut.close();
			fail();
		} catch (RideAlreadyExistException e) {
			// verify the results
			sut.close();
			assertTrue(true);
		} catch (RideMustBeLaterThanTodayException e) {
			fail();
		}
	}

	@Test
	public void test5() {
		String driverUsername = "Driver Test";
		String driverPassword = "123";
		String rideFrom = "Donostia";
		String rideTo = "Zarautz";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Driver driver = new Driver(driverUsername, driverPassword);
			// driver.addRide(rideFrom, rideTo, rideDate, 2, 10);
			// configure the state through mocks
			Mockito.when(db.find(Driver.class, driverUsername)).thenReturn(driver);
			// equivalent to
			// Mockito.doReturn(driver).when (db).find (Driver.class, driverUsername);
			// invoke System Under Test (sut)
			sut.open();
			sut.createRide(new CreateRideParameter(rideFrom, rideTo, rideDate, 0, 0, driverUsername));
			sut.close();
			assertTrue(true);
		} catch (RideAlreadyExistException e) {
			// verify the results
			sut.close();
			fail();
		} catch (RideMustBeLaterThanTodayException e) {
			fail();
		}
	}

	@Test
	public void testCreateDiscount() {
		try {
			// test params
			String code = "free10";
			double percent = 10;
			boolean active = true;
			Discount d = new Discount(code, percent, active);
			// call sut
			sut.createDiscount(d);

			// Mockito.verify(db, Mockito.times(1)).persist(d);
			
			// define Argument captors
			ArgumentCaptor<Discount> discountCaptor = ArgumentCaptor.forClass(Discount.class);
			// verify call numbers and capture parameters
			Mockito.verify(db, Mockito.times(1)).persist(discountCaptor.capture());
			// verify parameter values as usual using JUnit asserts
			assertEquals(code, ((Discount) discountCaptor.getValue()).getKodea());
			assertEquals(percent, discountCaptor.getValue().getPortzentaia(), 0.001);
			assertEquals(active, discountCaptor.getValue().isActive());
			// test results ???
			System.out.println(discountCaptor); //hau ez dakit ondo dagoen
		} catch (Exception e) {
			fail("If any exception is thrown, it is not working properly");
		}
	}

	public static void main(String[] args) {

	}

}
