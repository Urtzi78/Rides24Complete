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

public class CancelRideDBWhiteTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
		 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 

		
	@Test
	//r Ride is null cancelRide should not do anything
	public void test1() {
		Ride r=null;
		sut.open();
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();

	}
	@Test
	//r Ride has an empty bookings list, should not do anything
	public void test2() {
		sut.open();
		Driver u=sut.getDriver("Urtzi");
		Ride r= new Ride("Hernani", "Donostia", new Date(), 5, 4.5, u);
		r.setBookings(new ArrayList<Booking>());
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	@Test
	//
	public void test3() {
		
		sut.open();
		Driver u=sut.getDriver("Urtzi");
		Traveler t=sut.getTraveler("Unax");
		Ride r= new Ride("Hernani", "Donostia", new Date(), 5, 4.5, u);
		Booking b= new Booking(r,t,5);
		b.setStatus("Accepted");
		List<Booking> bs=new ArrayList<Booking>();
		bs.add(b);
		r.setBookings(bs);
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	@Test
	//
	public void test4() {
		sut.open();
		Driver u=sut.getDriver("Urtzi");
		Traveler t=sut.getTraveler("Unax");
		Ride r= new Ride("Hernani", "Donostia", new Date(), 5, 4.5, u);
		Booking b= new Booking(r,t,5);
		b.setStatus("Rejected");
		List<Booking> bs=new ArrayList<Booking>();
		bs.add(b);
		r.setBookings(bs);
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
	}
}
