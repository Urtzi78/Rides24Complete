import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;
import domain.Driver;

public class BookRideBDWhiteTest {

	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();

	@Test
	// sut.bookRide: Ride null da. Testak false itzuli behar du.
	public void test1() {
		String username = "TestTraveler";
        Ride ride = null;
        int seats = 3;
        double desk = 0;
        boolean travelerCreated = false;

        try {
            // traveler sortu
            testDA.open();
            testDA.createTravelerWithMoney(username, "password", 1000.0);
            travelerCreated = true;
            testDA.close();

            // Ride null bat erreserbatzen saiatu
            sut.open();
            boolean emaitza = sut.bookRide(username, ride, seats, desk);
            sut.close();

            assertFalse(emaitza);
        } catch (Exception e) {
            fail();
        } finally {
            // DB garbitu
            if (travelerCreated) {
                testDA.open();
                testDA.removeTraveler(username);
                testDA.close();
            }
        }
	}

	@Test
	// sut.bookRide: Traveler ez da existitzen DBan. Testak false itzuli behar du.
	public void test2() {
		String nonExistentUsername = "NonExistentTraveler";
		Driver driver = null;
		Ride ride = null;

		try {
			// Sortu driver eta ride
			testDA.open();
			driver = testDA.createDriver("TestDriver", "password");
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());

			// ride erreserbatzen saiatu existitzen ez den erabiltzaile izenarekin
			boolean emaitza = sut.bookRide(nonExistentUsername, ride, 2, 0);

			assertFalse(emaitza);
		} catch (Exception e) {
			fail();
		} finally {
			sut.close();
			// DB garbitu
			testDA.open();
			if (driver != null)
				testDA.removeDriver(driver.getUsername());
			if (ride != null)
				testDA.removeRide(ride);
			testDA.close();
		}
	}

	@Test
	// sut.bookRide: Erreserbatu daitezkeen eserlekuak baina gehiago erreserbatzen
	// saiatzen ari da erabiltzailea. Testak false itzuli behar du.
	public void test3() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;

		try {
			// Sortu traveler eta driver
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 1000.0);
			travelerCreated = true;
			driver = testDA.createDriver("TestDriver", "password");
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 3, 50, driver.getUsername());

			// Daitezkeenak baina gehiago erreserbatzen saiatzen da
			boolean result = sut.bookRide(username, ride, 4, 0);

			assertFalse(result);
		} catch (Exception e) {
			fail();
		} finally {
			sut.close();
			// DB garbitu
			testDA.open();
			if (travelerCreated)
				testDA.removeTraveler(username);
			if (driver != null)
				testDA.removeDriver(driver.getUsername());
			if (ride != null)
				testDA.removeRide(ride);
			testDA.close();
		}
	}

	@Test
	// sut.bookRide: Travelerak ez dauka erreserba egiteko nahikoa diru.
	// Testak false itzuli behar du.
	public void test4() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;

		try {
			// traveler diru gutxitekin eta driver sortu
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 10.0);
			travelerCreated = true;
			driver = testDA.createDriver("TestDriver", "password");
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbao", rideDate, 5, 100, driver.getUsername());

			// travelerrak ordaindu ezin duen ride bat erreserbatzen saiatu
			boolean result = sut.bookRide(username, ride, 2, 0);

			assertFalse(result);
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		} finally {
			sut.close();
			// DB garbitu
			testDA.open();
			if (travelerCreated)
				testDA.removeTraveler(username);
			if (driver != null)
				testDA.removeDriver(driver.getUsername());
			if (ride != null)
				testDA.removeRide(ride);
			testDA.close();
		}
	}

	@Test
	// sut.bookRide: ride ez da null, erabiltzailea DBan existitzen da, erreserbatu
	// daitezkeen eserlekuak baino gutxiago erreserbatu nahi dira eta travelerrak
	// nahikoa diru du erreserba ordaintzeko. Testak true itzuli behar du.

	public void test5() {
		boolean result = false;
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;
		boolean driverCreated = false;

		try {
			// traveler eta driver sortu
			testDA.open();
			if (!testDA.existTraveler(username)) {
				testDA.createTravelerWithMoney(username, "password", 1000.0);
				travelerCreated = true;
			}

			String driverUsername = "TestDriver";
			if (!testDA.existDriver(driverUsername)) {
				testDA.createDriver(driverUsername, "password");
				driverCreated = true;
			}
			driver = testDA.getDriver(driverUsername);
			testDA.close();

			// Create a ride
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driverUsername);

			// Book the ride
			int seats = 2;
			double desk = 5.0;
			result = sut.bookRide(username, ride, seats, desk);
			sut.close();

			// Verify the results
			assertTrue(result);

			// Additional verifications
			testDA.open();
			Ride updatedRide = testDA.getRide(ride.getRideNumber());
			assertEquals(3, updatedRide.getnPlaces()); // 5 eserleku original - 2 erreserbatutako eserleku

			double expectedBalance = 1000.0 - ((50.0 - desk) * seats);
			assertEquals(expectedBalance, testDA.getTraveler(username).getMoney(), 0.01);

			boolean bookingExists = testDA.existBooking(username, ride.getRideNumber());
			assertTrue(bookingExists);
			testDA.close();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			// DB garbitu
			testDA.open();
			if (travelerCreated)
				testDA.removeTraveler(username);
			if (driverCreated)
				testDA.removeDriver(driver.getUsername());
			if (ride != null)
				testDA.removeRide(ride);
			testDA.close();
		}
	}
}
