package rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.joda.time.DateTime;

abstract class AbstractVehicle extends UnicastRemoteObject implements Vehicle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String number;
	private final String condition;
	private final String mark;
	private final String fuelType;
	private final double rentalPrice;
	private final double salePrice;
	private final int yearCirculated;
	private final Set<LeasingInterface> listLeasing;
	private final List<LeasingInterface> currentLeasing;
	private final Set<EmployeInterface> waitingList;
	private final List<String> listNotes;
	private boolean state;
	private boolean bought;

	public AbstractVehicle(String condition, String mark, String fuelType, double rentalPrice, String number, double salePrice) throws RemoteException {
		super();
		if (condition.isBlank() || fuelType.isBlank() || mark.isBlank() || number.isBlank() || rentalPrice <= 0) {
			throw new IllegalArgumentException("Invalid input");
		}
		
		this.number = number;
		this.condition = condition;
		this.fuelType = fuelType;
		this.mark = mark;
		this.rentalPrice = rentalPrice;
		this.salePrice = salePrice;
		this.yearCirculated = new DateTime().getYear();
		this.state = true;
		this.bought = false;
		this.listLeasing = new HashSet<>();
		this.waitingList = new HashSet<>();
		this.currentLeasing = new ArrayList<>(1);
		this.listNotes = new LinkedList<>();
	}


	@Override
	public final String getNumber() throws RemoteException {
		return number;
	}
	
	@Override
	public final double getRentalPrice() throws RemoteException {
		return rentalPrice;
	}

	@Override
	public final String getCondition() throws RemoteException {
		return condition;
	}

	@Override
	public final String getMark() throws RemoteException {
		return mark;
	}

	@Override
	public final String getFuelType() throws RemoteException {
		return fuelType;
	}

	@Override
	public double getSalePrice() throws RemoteException {
		return salePrice;
	}
	
	@Override
	public final void setState()  throws RemoteException {
		this.state = !state;
	}
	
	@Override
	public final boolean isAvailable() throws RemoteException  {
		return state;
	}
	
	@Override
	public void setBought() throws RemoteException {
		this.bought = true;
	}
	
	@Override
	public boolean isBought() throws RemoteException{
		return bought;
	}
	
	@Override
	public final void addToWaitingList(EmployeInterface employe) throws RemoteException {
		Objects.requireNonNull(employe);
		waitingList.add(employe);
	}
	
	@Override
	public final Set<EmployeInterface> getWaitingList() throws RemoteException {
		return waitingList;
	}

	@Override
	public String[][] getListNote() throws RemoteException {
		String[][] notes = new String[listNotes.size()][3];
		int count = 0;
		for(String noteEmploye : listNotes) {
			String[] tab = noteEmploye.split("_");
			String employeName = tab[0];
			String comment = String.join("_", tab[1]);
			String note = tab[2];

			notes[count][0] = employeName;
			notes[count][1] = comment;
			notes[count][2] = note;
			
			count++;
		}
		return notes;
	}

	@Override
	public Set<LeasingInterface> getListLeasing() throws RemoteException {
		return listLeasing;
	}

	@Override
	public List<String> getCharacteristics() throws RemoteException {
		List<String> characteristics = new ArrayList<>(7);
		characteristics.add(("" + getKind().charAt(0)).toUpperCase() + getKind().substring(1));
		characteristics.add(("" + getMark().charAt(0)).toUpperCase() + getMark().substring(1));
		characteristics.add(getNumber().toUpperCase());
		characteristics.add(("" + getFuelType().charAt(0)).toUpperCase() + getFuelType().substring(1));
		characteristics.add( isAvailable() ? "Dispo" : "Indispo");
		characteristics.add("" + getRentalPrice());
		characteristics.add(("" + getCondition().charAt(0)).toUpperCase() + getCondition().substring(1));

		return characteristics;
	}
	
	@Override
	public final void addNote(String note) throws RemoteException {
		listNotes.add(note);
	}

	@Override
	public void clearCurrentRent() throws RemoteException {
		currentLeasing.clear();
	}

	@Override
	public void removeToWait(EmployeInterface employe) throws RemoteException{
		Objects.requireNonNull(employe, "Employe can't null");
		waitingList.remove(employe);
	}
	
	@Override
	public void addLeasing(LeasingInterface leasing) throws RemoteException {
		Objects.requireNonNull(leasing, "Employe can't null");
		listLeasing.add(leasing);
	}
	
	@Override
	public final void notifyEmployes() throws RemoteException {
		if (waitingList.isEmpty()) {
			System.out.println("empty list...");
			return;
		}

		waitingList.forEach(employe -> {
			try {
				employe.getNotification(this);
			} catch (RemoteException e) {
				System.out.println(e);
				return;
			}
		});
	}
	
	@Override
	public List<LeasingInterface> getCurrentRent() throws RemoteException{
		return currentLeasing;
	}
	
	@Override
	public void addToCurrentRent(LeasingInterface leasing) throws RemoteException{
		Objects.requireNonNull(leasing);
		currentLeasing.add(leasing);
	}
	
	@Override
	public int getYearCirculated() throws RemoteException{
		return yearCirculated;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vehicle)) {
			return false;
		}
		AbstractVehicle c = (AbstractVehicle) o;
		return Objects.equals(number, c.number);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
}
