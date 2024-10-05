import static org.junit.Assert.*;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;

import dataAccess.DataAccess;
import domain.Ride;
import domain.Driver;
import testOperations.TestDataAccess;

public class BookRideBDBlackTest {

	static DataAccess sut = new DataAccess();
	static TestDataAccess testDA = new TestDataAccess();

	@Test
	// 1. Denak ondo funtzionatu du, arazorik gabe. (WhiteTesteko test5 bezela)
	public void test1() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;
		boolean driverCreated = false;

		try {
			testDA.open();
			if (!testDA.existTraveler(username)) {
				testDA.createTravelerWithMoney(username, "password", 1000.0);
				travelerCreated = true;
			}
			if (!testDA.existDriver("TestDriver")) {
				driver = testDA.createDriver("TestDriver", "password");
				driverCreated = true;
			}
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());
			boolean result = sut.bookRide(username, ride, 2, 5.0);
			assertTrue(result);

			assertEquals(3, ride.getnPlaces());
			double expectedBalance = 1000.0 - ((50.0 - 5.0) * 2);
			assertEquals(expectedBalance, sut.getTraveler(username).getMoney(), 0.01);
			assertFalse(sut.getTraveler(username).getBookedRides().isEmpty());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			sut.close();
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

	@Test
	// 2. username hori duen User-a ez da existitzen datu basean
	public void test2() {
		String username = "NonExistentUser";
		Driver driver = null;
		Ride ride = null;
		boolean driverCreated = false;

		try {
			testDA.open();
			driver = testDA.createDriver("TestDriver", "password");
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());
			boolean result = sut.bookRide(username, ride, 2, 0);
			assertFalse(result);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			sut.close();
			testDA.open();
			if (driverCreated)
				testDA.removeDriver(driver.getUsername());
			if (ride != null)
				testDA.removeRide(ride);
			testDA.close();
		}
	}

	@Test
	// 3. username null da
	public void test3() {
		Driver driver = null;
		Ride ride = null;
		boolean driverCreated = false;

		try {
			testDA.open();
			driver = testDA.createDriver("TestDriver", "password");
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());
			boolean result = sut.bookRide(null, ride, 2, 0);
			assertFalse(result);
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		} finally {
			sut.close();
			testDA.open();
			if (driverCreated)
				testDA.removeDriver(driver.getUsername());
			if (ride != null)
				testDA.removeRide(ride);
			testDA.close();
		}
	}

	@Test
	// 4. ride ez dago datu basean
	public void test4() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;
		boolean driverCreated = false;

		try {
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 1000.0);
			travelerCreated = true;
			driver = testDA.createDriver("TestDriver", "password");
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			ride = new Ride("Donostia", "Bilbo", rideDate, 5, 50, driver); // Hau ez dago datu basean

			sut.open();
			boolean result = sut.bookRide(username, ride, 2, 0);
			assertFalse(result);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			sut.close();
			testDA.open();
			if (travelerCreated)
				testDA.removeTraveler(username);
			if (driverCreated)
				testDA.removeDriver(driver.getUsername());
			testDA.close();
		}
	}

	@Test
	// 5. ride null da
	public void test5() {
		String username = "TestTraveler";
		boolean travelerCreated = false;

		try {
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 1000.0);
			travelerCreated = true;
			testDA.close();

			sut.open();
			boolean result = sut.bookRide(username, null, 2, 0);
			assertFalse(result);
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		} finally {
			sut.close();
			testDA.open();
			if (travelerCreated)
				testDA.removeTraveler(username);
			testDA.close();
		}
	}

	@Test
	// 6. seats 0 edo txikiagoa da
	public void test6() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;
		boolean driverCreated = false;

		try {
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 1000.0);
			travelerCreated = true;
			driver = testDA.createDriver("TestDriver", "password");
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());
			boolean result = sut.bookRide(username, ride, 0, 0);
			assertFalse(result);

			result = sut.bookRide(username, ride, -1, 0);
			assertFalse(result);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			sut.close();
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

	@Test
	// 7. desk negatiboa da
	public void test7() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;
		boolean driverCreated = false;

		try {
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 1000.0);
			travelerCreated = true;
			driver = testDA.createDriver("TestDriver", "password");
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());
			boolean result = sut.bookRide(username, ride, 2, -10);
			assertFalse(result);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			sut.close();
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

	@Test
	// 8. desk ride-ren prezioa baino handiagoa da
	public void test8() {
		String username = "TestTraveler";
		Driver driver = null;
		Ride ride = null;
		boolean travelerCreated = false;
		boolean driverCreated = false;

		try {
			testDA.open();
			testDA.createTravelerWithMoney(username, "password", 1000.0);
			travelerCreated = true;
			driver = testDA.createDriver("TestDriver", "password");
			driverCreated = true;
			testDA.close();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate = sdf.parse("30/11/2024");

			sut.open();
			ride = sut.createRide("Donostia", "Bilbo", rideDate, 5, 50, driver.getUsername());
			boolean result = sut.bookRide(username, ride, 2, 100);
			assertFalse(result);
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		} finally {
			sut.close();
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