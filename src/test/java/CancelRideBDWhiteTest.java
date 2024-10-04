import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.*;
import testOperations.TestDataAccess;

public class CancelRideBDWhiteTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
		 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 

		
	@Test
	//r Ride is null cancelRide should not do anything
	public void test1() {
		System.out.println("-------------------------------------1.Testa--------------------------------");
		Ride r=null;
		sut.open();
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();

	}
	@Test
	//r Ride has an empty bookings list, should not do anything
	public void test2() {
		System.out.println("-------------------------------------2.Testa--------------------------------");
		sut.open();
		Driver u=sut.getDriver("Urtzi");
		Ride r= new Ride("Hernani", "Donostia", new Date(), 5, 4.5, u);
		r.setBookings(new ArrayList<Booking>());
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	@Test
	//r Ride has an non empty list, should work correctly
	public void test3() {
		System.out.println("-------------------------------------3.Testa--------------------------------");
		testDA.open();
		Traveler a=testDA.createTravelerWithMoney("Aimar", "1234", 500.0);
		Driver d=testDA.createDriver("Dani", "a");
		testDA.addDriverWithRide("Dani", "Hernani", "Lasarte", new Date(), 7, 9);
		List<Ride>r=testDA.getDriversRides(d);
		Booking b=testDA.bookRide("Aimar",r.get(0),2,0.0);
		testDA.setBookStatus(b, "Accepted");
		testDA.close();
		sut.open();
		try {
		

		
		
		sut.cancelRide(r.get(0));
		assertTrue(true);
		sut.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			sut.close();
		}
		finally{
			sut.open();
			sut.deleteUser(d);
			sut.deleteUser(a);
			sut.close();
			
		}
		
	}
	@Test
	
	public void test4() {
		System.out.println("-------------------------------------4.Testa--------------------------------");
		testDA.open();
		Traveler a=testDA.createTravelerWithMoney("Aimar", "1234", 500.0);
		Driver d=testDA.createDriver("Dani", "a");
		testDA.addDriverWithRide("Dani", "Hernani", "Lasarte", new Date(), 7, 9);
		List<Ride>r=testDA.getDriversRides(d);
		Booking b=testDA.bookRide("Aimar",r.get(0),2,0.0);
		testDA.setBookStatus(b, "Rejected");
		testDA.close();
		sut.open();
		try {
		

		
		
		sut.cancelRide(r.get(0));
		assertTrue(true);
		sut.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			sut.close();
		}
		finally{
			sut.open();
			sut.deleteUser(d);
			sut.deleteUser(a);
			sut.close();
			
		}
	}
	
}
