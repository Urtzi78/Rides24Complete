package adapter;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import businessLogic.BLFacade;
import businessLogic.FacadeFactory;
import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;

public class DriverAdapter extends AbstractTableModel {
	protected Driver driver;
	protected String[] columnNames= new String [] {"from","to","date","places","price"};
	
	private BLFacade blFacade;
	//private static BLFacade appFacadeInterface;
	private List<Ride> rides;
	
	public DriverAdapter(Driver driver,DataAccess da) {
		this.driver=driver;
		blFacade= new FacadeFactory().createBLFacade(da);
		rides=blFacade.getRidesByDriver(driver.getUsername());
		
	}
	public int getColumnCount() {
		return columnNames.length ;
	}
	public int getRowCount() {
		return rides.size();
	}
	public String getColumnName(int i) {
	    // Challenge!
		  return columnNames[i];
	  }
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return rides.get(row).getFrom();
		case 1:
			return rides.get(row).getTo();
		case 2:
			return rides.get(row).getDate();
		case 3:
			return rides.get(row).getnPlaces();
		case 4:
			return rides.get(row).getPrice();
		default:
			return "Error";
		}
				
	}
}
