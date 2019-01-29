package rmi;
import java.awt.Component;
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

public class RentedVehicle extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	final String[] header = {"Genre", "Marque", "Matricule", "Carburant", "Etat", "Prix","Condition","Action"};
	private	final String[][] dataValues;
	private final Model model;
	private final ParkingInterface parking;
	private final EmployeInterface employe;
	
	public RentedVehicle(ParkingInterface parking, EmployeInterface employe) throws RemoteException {
		Set<Vehicle> listLeasing = employe.getListLeasing(parking.getListVehicle(), employe);
		this.dataValues = convertListToArray(listLeasing);
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
			setText((value == null) ? "Retourner" : value.toString());
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
			label = (value == null) ? "Retourner" : value.toString();
			JButton button = new JButton(label);
			String matricule = (String) table.getModel().getValueAt(row, 2);
			
			button.addActionListener(action -> {
				List<Vehicle> vehicles;
				try {
					vehicles = parking.searchVehicle(matricule, "", "");
					if (vehicles.isEmpty()) {
						JOptionPane.showMessageDialog(null,"Vous n'avez pas loué ce véhicule");
					}else {
						int result = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment retouner ce véhicule? : " + matricule);
						Vehicle vehicule = vehicles.get(0);						
						
						if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION ) {
							return;
						}
						
						if (result == JOptionPane.YES_OPTION && !vehicule.isAvailable()) {
							int resul = new JOptionPaneMultiInput().note(employe, vehicule);
							if (resul == JOptionPane.OK_OPTION) {
								String res = parking.returnVehicle(vehicule, employe);
								if (!res.isBlank()) {
									JOptionPane.showMessageDialog(null,res);
								}
							}
						}else {
							if (result == JOptionPane.YES_OPTION) {
								JOptionPane.showMessageDialog(null,"Vous n'avez pas loué ce véhicule");
							}
						}
					}
				} catch (RemoteException e) {
					throw new IllegalArgumentException("Erreur lors de la recherche du véhicule");
				}
			});
			
			return button;
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
