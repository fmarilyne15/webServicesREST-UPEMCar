package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface EmployeInterface extends Remote {
	public  String getFirstName() throws RemoteException;
	public  String getLastName() throws RemoteException;
	public  String getAddress() throws RemoteException;
	public  String getZipCode() throws RemoteException;
	public  String getCity() throws RemoteException;
	public  String getLicenceDrive() throws RemoteException;
	public  String getPassword() throws RemoteException;
	public  Set<Vehicle> getListLeasing(Set<Vehicle> vehicle, EmployeInterface employe) throws RemoteException;
	public  Set<Vehicle> getListVehicleWait(Set<Vehicle> vehicles, EmployeInterface employe) throws RemoteException;
	public  String getNotification(Vehicle vehicle) throws RemoteException;
}
