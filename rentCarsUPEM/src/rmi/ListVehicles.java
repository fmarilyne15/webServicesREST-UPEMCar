package rmi;
import java.awt.Component;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class ListVehicles extends JTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	final String[] header = {"Genre", "Marque", "Matricule", "Carburant", "Etat", "Prix","Condition","Action","Note"};
	private	final String[][] dataValues;
	private final Model listVehicles;
	private final ParkingInterface parking;
	private final EmployeInterface employe;
	
	public ListVehicles(ParkingInterface parking, EmployeInterface employe) throws RemoteException {
		this.parking = parking;
		this.employe = employe;
		
		Set<Vehicle> vehicles = parking.getListVehicle().stream().filter(v -> {
			try {
				return !v.isBought();
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
		}).collect(Collectors.toSet());
		
		this.dataValues = convertListToArray(vehicles);
		this.listVehicles = new Model();
		setModel(listVehicles);
		
		getColumn("Action").setCellRenderer(new ButtonRenderer());
		getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
		
		getColumn("Note").setCellRenderer(new ButtonRendererNote());
		getColumn("Note").setCellEditor(new ButtonEditorNote(new JCheckBox()));
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
							EmployeInterface empl = vehicule.getCurrentRent().get(0).getEmploye();
							int result = JOptionPane.showConfirmDialog(null, "Le véhicule [" + matricule + "] n'est pas dispo. Voulez-vous être sur la liste d'attente ?");
							
							if (result == JOptionPane.YES_OPTION && !empl.equals(employe)) {
								parking.rentVehicle(vehicule, employe, 0);
							}else {
								if (result == JOptionPane.YES_OPTION ) {
									JOptionPane.showMessageDialog(null,"Vous pouvez pas être sur la liste car vous le louez actuellement.");
								}
							}
							return;
						}
						
						if (vehicule.isBought()) {
							JOptionPane.showMessageDialog(null, "Le véhicule [" + matricule + "] est vendu.");
							return;
						}
						
						int result = JOptionPane.showConfirmDialog(null, "Souhaitez-vous louer : " + matricule);
						if (result == JOptionPane.YES_OPTION) {
							String hoursNumber = JOptionPane.showInputDialog(null, "Entrer nombre heures");
							if (isNumber(hoursNumber)) {
								if (Integer.parseInt(hoursNumber) > 0) {
									parking.rentVehicle(vehicule, employe, Integer.parseInt(hoursNumber));
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

		public Object getCellEditorValue() {
			return new String(label);
		}
		
		private boolean isNumber(String value) {
			try {
				Integer.parseInt(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
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

		public boolean isCellEditable(int row, int cols){
			if (cols == 7 || cols == 8) {
				return true;
			}
			return false;
		}
	}
	
	private class ButtonRendererNote extends JButton implements TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ButtonRendererNote() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			setText((value == null) ? "Voir" : value.toString());
			return this;
		}
	}

	private class ButtonEditorNote extends DefaultCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String label;
		private ButtonEditorNote(JCheckBox checkBox) {
			super(checkBox);
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) {
			label = (value == null) ? "Voir" : value.toString();
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
						new NoteWindow(vehicule);
					}
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			});
			
			return button;
		}

		public Object getCellEditorValue() {
			return new String(label);
		}
	}
}
