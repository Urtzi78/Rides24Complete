package domain;

import java.util.Date;

public class AddRideParameter {
	public String from;
	public String to;
	public Date date;
	public int nPlaces;
	public float price;

	public AddRideParameter(String from, String to, Date date, int nPlaces, float price) {
		this.from = from;
		this.to = to;
		this.date = date;
		this.nPlaces = nPlaces;
		this.price = price;
	}
}