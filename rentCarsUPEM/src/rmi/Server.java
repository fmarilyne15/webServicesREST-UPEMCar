package rmi;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static void main(String[] args) throws UnknownHostException, RemoteException, MalformedURLException {
		Registry registry = LocateRegistry.createRegistry(1099);
		
		Parking parking = new Parking();
		parking.addVehicle(new Car("Permis B", "peugeot", "essence", 100, "ch 001 pa 95", 10000));
		parking.addVehicle(new Car("5 experience", "peugeot", "diesel", 70, "ch 002 pa 75", 8000));
		parking.addVehicle(new Car("Permis B", "mercedes", "diesel", 60, "ch 003 pa 94",9000));
		parking.addVehicle(new Moto("Permis C", "renault", "essence", 150, "ch 004 pa 75", 6000));
		parking.addVehicle(new Moto("BSR", "renault", "diesel", 40, "ch 005 pa 93", 7000));

		UpemCorps corps = new UpemCorps();
		corps.addEmploye(new Employe("Landry", "Mpati", "rue bla bla", "93", "Neuilly", "001b", "lm"));
		corps.addEmploye(new Employe("Marilyne", "Ferreira", "rue bla bla", "93", "Neuilly", "002b","mf"));
		corps.addEmploye(new Employe("Olivier", "Boyer", "rue bla bla", "93", "Neuilly", "003b","ob"));
		corps.addEmploye(new Employe("Rudy", "Mohamed", "rue bla bla", "92", "Bagnolet", "004b", "rm"));
		corps.addEmploye(new Employe("Issoufou", "Moussa", "rue bla bla", "75", "Paris", "005b", "imo"));

		String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/project";
		registry.rebind(url+"/vehicles", parking);
		registry.rebind(url+"/employes", corps);
		
		System.out.println(url);
	}
}
