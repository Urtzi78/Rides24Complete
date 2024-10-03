package testOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class TestDataAccess {
	protected EntityManager db;
	protected EntityManagerFactory emf;

	ConfigXML c = ConfigXML.getInstance();

	public TestDataAccess() {

		System.out.println("TestDataAccess created");

		// open();

	}

	public void open() {

		String fileName = c.getDbFilename();

		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);

			db = emf.createEntityManager();
		}
		System.out.println("TestDataAccess opened");

	}

	public void close() {
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String name) {
		System.out.println(">> TestDataAccess: removeDriver");
		Driver d = db.find(Driver.class, name);
		if (d != null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else
			return false;
	}
	
	public Driver createDriver(String name, String pass) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver = null;
		db.getTransaction().begin();
		try {
			driver = new Driver(name, pass);
			db.persist(driver);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	public boolean existDriver(String email) {
		return db.find(Driver.class, email) != null;

	}

	public Driver addDriverWithRide(String name, String from, String to, Date date, int nPlaces, float price) {
		System.out.println(">> TestDataAccess: addDriverWithRide");
		Driver driver = null;
		db.getTransaction().begin();
		try {
			driver = db.find(Driver.class, name);
			if (driver == null) {
				System.out.println("Entra en null");
				driver = new Driver(name, null);
				db.persist(driver);
			}
			driver.addRide(from, to, date, nPlaces, price);
			db.getTransaction().commit();
			System.out.println("Driver created " + driver);

			return driver;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Ride> getDriversRides(Driver d){
		if(d!=null) {
			return d.getCreatedRides();
		}
		else {
			return null;
		}
	}
	public Booking bookRide(String username, Ride ride, int seats, double desk) {
		try {
			db.getTransaction().begin();

			Traveler traveler = getTraveler(username);
			if (traveler == null) {
				return null;
			}

			if (ride.getnPlaces() < seats) {
				return null;
			}

			double ridePriceDesk = (ride.getPrice() - desk) * seats;
			double availableBalance = traveler.getMoney();
			if (availableBalance < ridePriceDesk) {
				return null;
			}

			Booking booking = new Booking(ride, traveler, seats);
			booking.setTraveler(traveler);
			booking.setDeskontua(desk);
			db.persist(booking);

			ride.setnPlaces(ride.getnPlaces() - seats);
			traveler.addBookedRide(booking);
			traveler.setMoney(availableBalance - ridePriceDesk);
			traveler.setIzoztatutakoDirua(traveler.getIzoztatutakoDirua() + ridePriceDesk);
			List<Booking> b= new ArrayList<Booking>();
			b.add(booking);
			ride.setBookings(b);
			db.merge(ride);
			db.merge(traveler);
			db.getTransaction().commit();
			return booking;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}
	public void setBookStatus(Booking b, String s) {
		b.setStatus(s);
	}

	public boolean existRide(String name, String from, String to, Date date) {
		System.out.println(">> TestDataAccess: existRide");
		Driver d = db.find(Driver.class, name);
		if (d != null) {
			return d.doesRideExists(from, to, date);
		} else
			return false;
	}

	public Ride removeRide(String name, String from, String to, Date date) {
		System.out.println(">> TestDataAccess: removeRide");
		Driver d = db.find(Driver.class, name);
		if (d != null) {
			db.getTransaction().begin();
			Ride r = d.removeRide(from, to, date);
			db.getTransaction().commit();
			System.out.println("created rides" + d.getCreatedRides());
			return r;

		} else
			return null;

	}

	public boolean existTraveler(String username) {
		return db.find(Traveler.class, username) != null;
	}

	public Traveler createTravelerWithMoney(String username, String password, double money) {
		System.out.println(">> TestDataAccess: createTraveler");
		Traveler traveler = null;
		db.getTransaction().begin();
		try {
			traveler = new Traveler(username, password);
			traveler.setMoney(money);
			db.persist(traveler);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
		return traveler;
	}

	public void removeTraveler(String username) {
		System.out.println(">> TestDataAccess: removeTraveler");
		Traveler traveler = db.find(Traveler.class, username);
		if (traveler != null) {
			db.getTransaction().begin();
			db.remove(traveler);
			db.getTransaction().commit();
		}
	}

	public Driver getDriver(String username) {
		return db.find(Driver.class, username);
	}

	public Ride getRide(Integer rideNumber) {
		return db.find(Ride.class, rideNumber);
	}

	public Traveler getTraveler(String username) {
		return db.find(Traveler.class, username);
	}

	public boolean existBooking(String username, Integer rideNumber) {
		Traveler traveler = getTraveler(username);
		if (traveler != null) {
			for (Booking booking : traveler.getBookedRides()) {
				if (booking.getRide().getRideNumber().equals(rideNumber)) {
					return true;
				}
			}
		}
		return false;
	}

	public void removeRide(Ride ride) {
		if (ride != null) {
			db.getTransaction().begin();
			Ride managedRide = db.merge(ride);
			db.remove(managedRide);
			db.getTransaction().commit();
		}
	}
	

}