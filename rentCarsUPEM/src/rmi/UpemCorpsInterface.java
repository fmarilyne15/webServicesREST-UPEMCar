package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface UpemCorpsInterface extends Remote {

	public void addEmploye(EmployeInterface employe) throws RemoteException;
	public Set<EmployeInterface> getListEmployes() throws RemoteException;
	public EmployeInterface searchEmploye(String numPermis, String password) throws RemoteException;
}
