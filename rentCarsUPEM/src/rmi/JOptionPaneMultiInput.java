package rmi;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class JOptionPaneMultiInput {

	public List<Object> login(UpemCorpsInterface upemCorp) throws RemoteException{
		JTextField permis = new JTextField(20);
		JPasswordField password = new JPasswordField(20);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Numéro permis :"));
		panel.add(permis);
		panel.add(Box.createVerticalStrut(15));
		panel.add(new JLabel("Mot de passe : "));
		panel.add(password);

		panel.setPreferredSize(new Dimension(400, 50));
		int result = JOptionPane.showConfirmDialog(null, panel, "Connectez-vous", JOptionPane.OK_CANCEL_OPTION);

		List<Object> status = new LinkedList<>();

		if (result == JOptionPane.OK_OPTION) {
			EmployeInterface employe = upemCorp.searchEmploye(permis.getText(), new String(password.getPassword()));
			status.add(JOptionPane.OK_OPTION);
			if (employe != null) {
				status.add(employe);
			}else {
				JOptionPane.showMessageDialog(null, "Identifiants incorrects...");
			}
		}

		if (result == JOptionPane.CANCEL_OPTION) {
			status.add(JOptionPane.CANCEL_OPTION);
		}

		if (result == JOptionPane.CLOSED_OPTION) {
			status.add(JOptionPane.CLOSED_OPTION);
		}

		return status;
	}
	
	public int note(EmployeInterface employe, Vehicle vehicle) throws RemoteException {
		JTextField note = new JTextField(20);
		JTextArea comment = new JTextArea(20,20);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Note :"));
		panel.add(note);
		panel.add(Box.createHorizontalStrut(15));
		panel.add(new JLabel("Commentaire : "));
		panel.add(comment);

		panel.setPreferredSize(new Dimension(500, 400));
		int result = JOptionPane.showConfirmDialog(null, panel, "Voulez-vous noter ce véhicule ?", JOptionPane.OK_CANCEL_OPTION);

		String not = note.getText();
		if (result == JOptionPane.OK_OPTION) {
			if (isNumber(not)) {
				if (Integer.parseInt(not) > 10) {
					JOptionPane.showMessageDialog(null, "La note doit être inférieur ou égal à 10");
					note(employe, vehicle);
				}else {
					String emplNoteAndComment = employe.getFirstName() + " " + employe.getLastName() + "_" + comment.getText() + "_" + not ;
					vehicle.addNote(emplNoteAndComment);
				}
			}else {
				JOptionPane.showMessageDialog(null, "La note doit être un nombre.");
				note(employe, vehicle);
			}
		}
		return result;
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
