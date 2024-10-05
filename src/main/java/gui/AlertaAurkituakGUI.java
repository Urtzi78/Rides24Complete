package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Alert;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AlertaAurkituakGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static BLFacade appFacadeInterface;
	private JTable table;
	private JButton closeButton;
	private final String etiketa="Etiqueta";

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}

	public AlertaAurkituakGUI(String username) {
		
		setBussinessLogic(TravelerGUI.getBusinessLogic());
		this.setTitle(ResourceBundle.getBundle(etiketa).getString("AlertGUI.Alert"));
		setSize(new Dimension(600, 400));
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());

		List<Alert> alertList = appFacadeInterface.getAlertsByUsername(username);
		DefaultTableModel model = new DefaultTableModel(
				new Object[] { ResourceBundle.getBundle(etiketa).getString("AlertGUI.Zenbakia"),
						ResourceBundle.getBundle(etiketa).getString("CreateRideGUI.LeavingFrom"),
						ResourceBundle.getBundle(etiketa).getString("CreateRideGUI.GoingTo"),
						ResourceBundle.getBundle(etiketa).getString("CreateRideGUI.RideDate"),
						ResourceBundle.getBundle(etiketa).getString("AlertGUI.Aurkitua"),
						ResourceBundle.getBundle(etiketa).getString("AlertGUI.Aktibo") },
				0);
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		table.getTableHeader().setReorderingAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setDefaultEditor(Object.class, null);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		if (alertList != null) {
			for (Alert alert : alertList) {
				if (alert.isFound() && alert.isActive()) {
					String formattedDate = dateFormat.format(alert.getDate());
					Object[] rowData = { alert.getAlertNumber(), alert.getFrom(), alert.getTo(), formattedDate,
							alert.isFound(), alert.isActive() };
					model.addRow(rowData);
				}
			}
		}

		closeButton = new JButton(ResourceBundle.getBundle(etiketa).getString("Close"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeButtonActionPerformed();
			}
		});
		getContentPane().add(closeButton, BorderLayout.SOUTH);
	}

	private void closeButtonActionPerformed() {
		this.setVisible(false);
	}
}
