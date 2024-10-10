package dataAccess;

import java.util.Date;

public class CreateRideParameter {
	public String from;
	public String to;
	public Date date;
	public int nPlaces;
	public float price;
	public String driverName;

	public CreateRideParameter(String from, String to, Date date, int nPlaces, float price, String driverName) {
		this.from = from;
		this.to = to;
		this.date = date;
		this.nPlaces = nPlaces;
		this.price = price;
		this.driverName = driverName;
	}
}