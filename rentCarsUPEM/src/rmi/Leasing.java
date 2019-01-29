package rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class Leasing extends UnicastRemoteObject implements LeasingInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String beginingDate;
	private String returnDate;
	private final Vehicle vehicle;
	private final EmployeInterface employe;

	public Leasing(String beginingDate, String returnDate, Vehicle vehicle, EmployeInterface employe) throws RemoteException {
		super();
		Objects.requireNonNull(vehicle);
		Objects.requireNonNull(employe);
		if (returnDate.isBlank()) {
			throw new IllegalArgumentException("La date de retour est obligatoire");
		}
		
		if (beginingDate.equals(returnDate)) {
			throw new IllegalArgumentException("La date de retour doit être supérieur à la date de location...");
		}
		
		this.beginingDate = beginingDate;
		this.returnDate = returnDate;
		this.vehicle = vehicle;
		this.employe = employe;
	}

	@Override
	public final String getBeginingDate() {
		return beginingDate;
	}

	@Override
	public final String getReturnDate() {
		return returnDate;
	}

	@Override
	public final Vehicle getVehicle() {
		return vehicle;
	}

	@Override
	public final EmployeInterface getEmploye() {
		return employe;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Leasing)) {
			return false;
		}
		Leasing l = (Leasing) o;
		return Objects.equals(beginingDate, l.beginingDate) && Objects.equals(returnDate, l.returnDate)
				&& Objects.equals(employe, l.employe) && Objects.equals(vehicle, l.vehicle);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(beginingDate, returnDate, employe, vehicle);
	}
}
