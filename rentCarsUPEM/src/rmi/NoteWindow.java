package rmi;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class NoteWindow extends JDialog  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String[] header = { "Empolye", "Commentaire", "Note/10" };
	private  String[][] dataValues;
	private final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private final Model noteModel;
	private final JTable tableNote;
	private final JScrollPane scrollNote;
	private final int x;
	private final int y;

	public NoteWindow(Vehicle vehicle) throws IOException {
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		x = width /2;
		y = height / 4;
		
		this.dataValues = vehicle.getListNote();
		this.noteModel = new Model();
		this.tableNote = new JTable(noteModel);

		this.scrollNote = new JScrollPane(tableNote);
		scrollNote.setPreferredSize(new Dimension(x - 100, y - 100));
		
		JPanel mainPanel = new JPanel();
		
		JPanel panelNotes = new JPanel();
		panelNotes.add(scrollNote);
		panelNotes.setPreferredSize(new Dimension(x,y));
		
		mainPanel.add(panelNotes);
		add(mainPanel);
		
		setTitle("Note du véhicule ".toUpperCase() + vehicle.getNumber().toUpperCase());
		setModal(true);
		Image image = ImageIO.read(new File("parking.png"));
		setIconImage(image);
		setSize(x, y);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private class Model extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Model() {
			super(dataValues, header);
		}

		public boolean isCellEditable(int row, int cols) {
			return false;
		}
	}
}
