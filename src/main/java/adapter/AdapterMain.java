package adapter;

import businessLogic.BLFacade;
import businessLogic.FacadeFactory;
import dataAccess.DataAccess;
import domain.Driver;

public class AdapterMain {

	public static void main(String[] args) {
		DataAccess da = new DataAccess();
		BLFacade blFacade =	new FacadeFactory().createBLFacade(da);
		Driver d= blFacade.getDriver("Urtzi");	
		DriverTable	dt=new DriverTable(d,da);	
		dt.setVisible(true);	

	}

}
