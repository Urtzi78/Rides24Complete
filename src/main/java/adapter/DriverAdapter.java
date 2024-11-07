package domain;

import javax.swing.table.AbstractTableModel;

import dataAccess.DataAccess;
import domain.Driver;

public class DriverAdapter extends AbstractTableModel {
	protected Driver driver;
	protected String[] columnNames= new String [] {"from","to","date","places","price"};
	protected DataAccess da =new DataAccess();
	
	public DriverAdapter(Driver driver) {
		this.driver=driver;
	}
	public int getColumnCount() {
		return columnNames.length ;
	}
	public int getRowCount() {
		int i=da.getRidesByDriver(driver.getUsername()).size();
		return i;
	}
	public Object getValueAt(int row, int col) {
		return null;
	}
}
