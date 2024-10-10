package dataAccess;

import java.util.Date;

import domain.Booking;

public class ErreklamazioaBidaliParameter {
	public String nor;
	public String nori;
	public Date gaur;
	public Booking booking;
	public String textua;
	public boolean aurk;

	public ErreklamazioaBidaliParameter(String nor, String nori, Date gaur, Booking booking, String textua,
			boolean aurk) {
		this.nor = nor;
		this.nori = nori;
		this.gaur = gaur;
		this.booking = booking;
		this.textua = textua;
		this.aurk = aurk;
	}
}