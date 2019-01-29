package rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Employe extends UnicastRemoteObject implements EmployeInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String firstName;
	private final String lastName;
	private final String address;
	private final String zipCode;
	private final String city;
	private final String licenceDrive;
	private final String password;
	
	public Employe(String firstName, String lastName, String address, String zipCode, String city, String licenceDrive, String password) throws RemoteException {
		super();
		if (firstName.isBlank() || lastName.isBlank() || address.isBlank() || zipCode.isBlank() || city.isBlank() || licenceDrive.isBlank()) {
			throw new IllegalArgumentException("Invalid input");
		}
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.zipCode = zipCode;
		this.city = city;
		this.licenceDrive = licenceDrive;
		this.password = password;
		//this.listLeasing = new ArrayList<>();
		//this.listVehicleWait = new LinkedList<>();
	}

	@Override
	public final String getFirstName() throws RemoteException {
		return firstName;
	}

	@Override
	public final String getLastName() throws RemoteException {
		return lastName;
	}

	@Override
	public final String getAddress() throws RemoteException {
		return address;
	}

	@Override
	public final String getZipCode() throws RemoteException {
		return zipCode;
	}

	@Override
	public final String getCity() throws RemoteException {
		return city;
	}

	@Override
	public final String getLicenceDrive() throws RemoteException {
		return licenceDrive;
	}

	@Override
	public final String getPassword() {
		return password;
	}
	
	@Override
	public final Set<Vehicle> getListLeasing(Set<Vehicle> vehicles, EmployeInterface employe) throws RemoteException {
		HashSet<Vehicle> listLeasing = new HashSet<>();
		for (Vehicle v : vehicles) {
			for (LeasingInterface l : v.getListLeasing()) {
				if (l.getEmploye().equals(employe)) {
					listLeasing.add(v);
				}
			}
		}
		return listLeasing;
	}
	
	@Override
	public final Set<Vehicle> getListVehicleWait(Set<Vehicle> vehicles, EmployeInterface employe) throws RemoteException {
		Set<Vehicle> list = new HashSet<>();
		for (Vehicle v : vehicles) {
			for (EmployeInterface e : v.getWaitingList()) {
				if (e.equals(employe)) {
					list.add(v);
				}
			}
		}
		return list;
	}
	
	@Override
	public final String getNotification(Vehicle vehicle) throws RemoteException {
		return "La " + vehicle.getKind()  + " matricule " + "["+ vehicle.getNumber() + "]" + " marque : " + "[" + vehicle.getMark() + "]" + " vient de se libérer...";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Employe)) {
			return false;
		}
		Employe e = (Employe) o;
		return Objects.equals(firstName, e.firstName) && Objects.equals(lastName, e.lastName) && 
				Objects.equals(address, e.address) && Objects.equals(zipCode, e.zipCode) && Objects.equals(city, e.city);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, address, zipCode, city);
	}
}
