package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface ParkingInterface extends Remote {
	public void addVehicle(Vehicle vehicle) throws RemoteException;
	public Set<Vehicle> getListVehicle() throws RemoteException;
	public String rentVehicle(Vehicle vehicle,EmployeInterface employeInterface,  int hoursNumber) throws RemoteException;
	public String returnVehicle(Vehicle vehicle, EmployeInterface employe) throws RemoteException;
	public List<Vehicle> searchVehicle(String number, String mark, String price) throws RemoteException;
	public void remove(Vehicle vehicle) throws RemoteException;
}
