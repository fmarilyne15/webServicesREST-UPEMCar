package rmi;


import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class AppWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private JScrollPane scrollAllVehicles;
	private JScrollPane scrollBasket;
	private JScrollPane scrollLeasing;
	private final int x;
	private final int y;
	private EmployeInterface employe;
	private final ParkingInterface parking;
	
	public AppWindow(ParkingInterface parking, UpemCorpsInterface corps) throws IOException, NotBoundException {
		this.parking = parking;
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		x = width/2;
		y = height/2;
		
		
		List<Object> connectionStatus = null;
		while (connectionStatus == null) {
			connectionStatus = new JOptionPaneMultiInput().login(corps);
			if (connectionStatus.size() == 2) {
				employe = (EmployeInterface) connectionStatus.get(1);
				
				this.scrollAllVehicles = new JScrollPane(new ListVehicles(parking, employe));
				scrollAllVehicles.setPreferredSize(new Dimension(x-100,y-100));
				
				this.scrollLeasing = new JScrollPane(new RentedVehicle(parking, employe));
				scrollLeasing.setPreferredSize(new Dimension(x-100,y-100));
				
				this.scrollBasket = new JScrollPane(new Basket(parking, employe));
				scrollBasket.setPreferredSize(new Dimension(x-100,y-100));
				
				setTitle("Location de voiture");
				Image image = ImageIO.read(new File("parking.png"));
				setIconImage(image);			
				setSize(x, y);
				setLocationRelativeTo(null);
				setVisible(true);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}else {
				if ((int)connectionStatus.get(0) == JOptionPane.OK_OPTION) {
					connectionStatus = null;
				}else {
					break;
				}
			}
		}
		
		getWindow();
		startNotification();
	}
	
	public void getWindow() throws RemoteException {		
		JPanel mainPanel = new JPanel();
		JTabbedPane containerTab = new JTabbedPane();
		if (scrollAllVehicles != null) {
			JPanel listVehicles = new JPanel();
			listVehicles.add(scrollAllVehicles);
			listVehicles.setPreferredSize(new Dimension(x,y));
			containerTab.addTab("Liste véhicule", listVehicles);
			
			JPanel myRents = new JPanel();
			myRents.add(scrollLeasing);	
			myRents.setPreferredSize(new Dimension(x,y));
			containerTab.addTab("Mes locations", myRents);
			
			JPanel myBasket = new JPanel();
			myBasket.add(scrollBasket);		
			myBasket.setPreferredSize(new Dimension(x,y));
			containerTab.addTab("Mon panier", myBasket);
			
			containerTab.setOpaque(true);
			mainPanel.add(containerTab);
			
			getContentPane().add(mainPanel);
		}
	}
	
	private void startNotification() {	
		new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					for(Vehicle v : parking.getListVehicle()) {
						if (v.getWaitingList().contains(employe) && v.isAvailable()) {
							String notification = employe.getNotification(v);
							v.removeToWait(employe);
							JOptionPane.showMessageDialog(null, notification);
						}
					}
					Thread.sleep(300);
				} catch (RemoteException | InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}
}
