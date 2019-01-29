package rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Parking extends UnicastRemoteObject implements ParkingInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Set<Vehicle> listVehicles = new LinkedHashSet<>();
	
	public Parking() throws RemoteException {
		super();
	}

	@Override
	public final void addVehicle(Vehicle vehicle) throws RemoteException {
		Objects.requireNonNull(vehicle);
		listVehicles.add(vehicle);
	}
	

	@Override
	public void remove(Vehicle vehicle) throws RemoteException {
		Objects.requireNonNull(vehicle);
		listVehicles.remove(vehicle);
	}
	
	@Override
	public Set<Vehicle> getListVehicle() throws RemoteException {
		return listVehicles;
	}

	@Override
	public String rentVehicle(Vehicle vehicle, EmployeInterface employe, int hoursNumber) throws RemoteException {
		Objects.requireNonNull(vehicle);
		Objects.requireNonNull(employe);

		if (!vehicle.isAvailable()) {
			vehicle.addToWaitingList(employe);
			return "La " +  vehicle.getKind() + " matricule " + "["+ vehicle.getNumber() + "]" + " marque " + vehicle.getMark() + " n'est pas dispo... ";
		}
		
		vehicle.setState();
		
		LocalDateTime time = LocalDateTime.now();
		LocalDateTime retour = time.plusHours(hoursNumber);

		String rentDate = time.getDayOfMonth() + "-" + time.getMonthValue() + "-"  + time.getYear() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
		String returnDate = retour.getDayOfMonth() + "-" + retour.getMonthValue() + "-"  + retour.getYear() +  " " + (retour.getHour()+1) + ":" + retour.getMinute() + ":" + retour.getSecond();
		
		vehicle.addLeasing(new Leasing(rentDate, returnDate, vehicle, employe));
		vehicle.addToCurrentRent(new Leasing(rentDate, returnDate, vehicle, employe));
		
		return "Vous venez de reservé la " + vehicle.getKind() + " matricule " + "["+ vehicle.getNumber() + "]";
	}

	@Override
	public String returnVehicle(Vehicle vehicle, EmployeInterface employe) throws RemoteException {
		Objects.requireNonNull(vehicle);
		if (vehicle.isAvailable() || !vehicle.getCurrentRent().get(0).getEmploye().equals(employe)) {
			return "Vous ne pouvez pas retourner un véhicule non loué...";
		}
		
		vehicle.setState();
		vehicle.clearCurrentRent();
		vehicle.notifyEmployes();
		
		return "";
	}

	@Override
	public List<Vehicle> searchVehicle(String number, String mark, String price) throws RemoteException {
		if (mark.isBlank() && price.isBlank()) {
			return listVehicles.stream().filter(v -> {
				try {
					return v.getNumber().equalsIgnoreCase(number);
				} catch (RemoteException e) {
					e.printStackTrace();
					return false;
				}
			}).collect(Collectors.toList());
		}
		
		if (number.isBlank() && price.isBlank()) {
			return listVehicles.stream().filter(v -> {
				try {
					return v.getMark().equalsIgnoreCase(mark);
				} catch (RemoteException e) {
					e.printStackTrace();
					return false;
				}
			}).collect(Collectors.toList());
		}
		
		if (number.isBlank() && mark.isBlank()) {
			return listVehicles.stream().filter(v -> {
				try {
					return v.getRentalPrice() == Integer.parseInt(price);
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
					return false;
				}
			}).collect(Collectors.toList());
		}
		
		return listVehicles.stream().filter(v -> {
			try {
				return v.getNumber().equalsIgnoreCase(number) && v.getMark().equalsIgnoreCase(mark) && v.getRentalPrice() == Integer.parseInt(price);
			} catch (NumberFormatException | RemoteException e) {
				e.printStackTrace();
				return false;
			}
		}).collect(Collectors.toList());
		
	}
}
