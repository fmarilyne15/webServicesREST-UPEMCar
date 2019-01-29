package rmi;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UpemCorps implements UpemCorpsInterface, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Set<EmployeInterface> listEmployes = new HashSet<>();
	
	@Override
	public void addEmploye(EmployeInterface employe) throws RemoteException {
		Objects.requireNonNull(listEmployes);
		listEmployes.add(employe);
	}

	@Override
	public Set<EmployeInterface> getListEmployes() throws RemoteException {
		return listEmployes;
	}

	@Override
	public EmployeInterface searchEmploye(String numPermis, String password) throws RemoteException {
		EmployeInterface employe = null;
		
		for (EmployeInterface e : listEmployes) {
			if (e.getLicenceDrive().equals(numPermis) && e.getPassword().equals(password)) {
				employe = e;
				break;
			}
		}
		return employe;
	}
}
