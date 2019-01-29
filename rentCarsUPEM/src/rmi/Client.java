package rmi;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

	public static void main(String[] args) throws NotBoundException, IOException {
		String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/project";
		Registry registry = LocateRegistry.getRegistry();
		
		ParkingInterface parking = (ParkingInterface) registry.lookup(url + "/vehicles");
		UpemCorpsInterface corps = (UpemCorpsInterface) registry.lookup(url + "/employes");
		
		new AppWindow(parking, corps);
	}
}
