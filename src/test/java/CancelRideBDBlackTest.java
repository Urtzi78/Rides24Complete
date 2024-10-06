import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class CancelRideBDBlackTest {
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 

	@Test
	//Ondo egin behar du erreserbak edukita
	public void test1() {
		System.out.println("-------------------------------------1.Testa--------------------------------");
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
	//Exception printeatu behar du, ez dituelako erreserbak
	public void test2() {
		System.out.println("-------------------------------------2.Testa--------------------------------");
		testDA.open();
		Traveler a=testDA.createTravelerWithMoney("Aimar", "1234", 500.0);
		Driver d=testDA.createDriver("Dani", "a");
		testDA.addDriverWithRide("Dani", "Hernani", "Lasarte", new Date(), 7, 9);
		List<Ride>r=testDA.getDriversRides(d);
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
	//Ez du ondo egin behar null pasatzen diogu
	public void test3() {
		System.out.println("-------------------------------------3.Testa--------------------------------");
		Ride r=null;
		sut.open();
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	@Test
	//Ez du ondo egin behar bidaia ez dago 
	public void test4() {
		System.out.println("-------------------------------------4.Testa--------------------------------");
		Driver d= new Driver("Aimar","a");
		Ride r=new Ride("Hernani","Donostia",new Date(),3,4.3,d);
		sut.open();
		try {
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
			sut.close();
		}
		
		
	}
	
}
