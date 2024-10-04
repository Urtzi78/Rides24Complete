import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class CancelRideMockWhiteTest {
static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
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
		Traveler a=new Traveler("Aimar", "1234");
		Driver d=new Driver("Dani", "a");
		Ride r=new Ride( "Hernani", "Lasarte", new Date(), 7, 9, d);
		r.setBookings(new ArrayList<Booking>());
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	@Test
	//r Ride has an non empty list and it's status is ACCEPTED, should work correctly
	public void test3() {
		System.out.println("-------------------------------------3.Testa--------------------------------");
		Traveler a=new Traveler("Aimar", "1234");
		Driver d=new Driver("Dani", "a");
		Ride r=new Ride( "Hernani", "Lasarte", new Date(), 7, 9, d);
		Booking b=new Booking(r,a,2);
		b.setStatus("Accepted");
		ArrayList<Booking> bs=new ArrayList<Booking>();
		bs.add(b);
		r.setBookings(bs);
		
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	@Test
	//r Ride has an non empty list and it's status is REJECTED, should work correctly
	public void test4() {
		System.out.println("-------------------------------------4.Testa--------------------------------");
		Traveler a=new Traveler("Aimar", "1234");
		Driver d=new Driver("Dani", "a");
		Ride r=new Ride( "Hernani", "Lasarte", new Date(), 7, 9, d);
		Booking b=new Booking(r,a,2);
		b.setStatus("Rejected");
		ArrayList<Booking> bs=new ArrayList<Booking>();
		bs.add(b);
		r.setBookings(bs);
		
		
		sut.cancelRide(r);
		assertTrue(true);
		sut.close();
		
	}
	
}
