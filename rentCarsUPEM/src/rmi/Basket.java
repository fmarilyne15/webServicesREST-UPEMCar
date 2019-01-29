package rmi;
import java.awt.Component;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class Basket extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	final String[] header = {"Genre", "Marque", "Matricule", "Carburant", "Etat", "Prix","Condition","Action"};
	private	final String[][] dataValues;
	private final Model model;
	private final ParkingInterface parking;
	private final EmployeInterface employe;
	
	public Basket(ParkingInterface parking, EmployeInterface employe) throws RemoteException {
		Set<Vehicle> listWaiting = employe.getListVehicleWait(parking.getListVehicle(), employe);
		this.dataValues = convertListToArray(listWaiting);
		this.parking = parking;
		this.employe = employe;
		model= new Model();
		setModel(model);
		
		getColumn("Action").setCellRenderer(new ButtonRenderer());
		getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
	}
	
	private String[][] convertListToArray(Set<Vehicle> vehicles) throws RemoteException {
		List<Vehicle> list = new LinkedList<>(vehicles);		
		if (list.isEmpty()) {
			return new String[vehicles.size()][0];
		}
		
		String[][] dataValues = new String[vehicles.size()][list.get(0).getCharacteristics().size()];	
		for (int i = 0; i < dataValues.length; i++) {
			List<String> vehicleCharacteristics = list.get(i).getCharacteristics();
			for (int j = 0; j < vehicleCharacteristics.size(); j++) {
				dataValues[i][j] = vehicleCharacteristics.get(j);
			}
		}
		
		return dataValues;
	}
	
	private class ButtonRenderer extends JButton implements TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			setText((value == null) ? "Louer" : value.toString());
			return this;
		}
	}

	private class ButtonEditor extends DefaultCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String label;
		private ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) {
			label = (value == null) ? "Louer" : value.toString();
			JButton button = new JButton(label);
			String matricule = (String) table.getModel().getValueAt(row, 2);
			
			button.addActionListener(action -> {
				List<Vehicle> vehicles;
				try {
					vehicles = parking.searchVehicle(matricule, "", "");
					
					if (vehicles.isEmpty()) {
						JOptionPane.showMessageDialog(null,"Ce véhicule n'existe pas ");
					}else {
						Vehicle vehicule = vehicles.get(0);
						if (!vehicule.isAvailable()) {
							JOptionPane.showMessageDialog(null,"Ce véhicule n'est pas encore dispo ");
							return;
						}
						
						int result = JOptionPane.showConfirmDialog(null, "Souhaitez-vous louer : " + matricule);
						if (result == JOptionPane.YES_OPTION) {
							String hoursNumber = JOptionPane.showInputDialog(null, "Entrer nombre heures");
							if (isNumber(hoursNumber)) {
								if (Integer.parseInt(hoursNumber) > 0) {
									parking.rentVehicle(vehicule, employe, Integer.parseInt(hoursNumber));
									vehicule.getWaitingList().remove(employe);
								}else {
									JOptionPane.showMessageDialog(null, "Le nombre d'heures doit être supérieur à 0.");
								}
							}else {
								JOptionPane.showMessageDialog(null, "Le nombre d'heures doit être un nombre.");
							}
						}
					}
				} catch (IOException e) {
					throw new IllegalArgumentException("Erreur lors de la recherche du véhicule");
				}
			});
			
			return button;
		}

		private boolean isNumber(String value) {
			try {
				Integer.parseInt(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		public Object getCellEditorValue() {
			return new String(label);
		}
	}
	
	private class Model extends DefaultTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Model(){
			super(dataValues, header);
		}
		
		public boolean isCellEditable(int row, int cols) {
			if (cols == 7) {
				return true;
			}
			return false;
		}
	}
}
