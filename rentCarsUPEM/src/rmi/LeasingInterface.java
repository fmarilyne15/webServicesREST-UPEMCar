package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LeasingInterface extends Remote {

	public String getBeginingDate() throws RemoteException;
	public String getReturnDate() throws RemoteException;
	public Vehicle getVehicle() throws RemoteException;
	public EmployeInterface getEmploye() throws RemoteException;
}
