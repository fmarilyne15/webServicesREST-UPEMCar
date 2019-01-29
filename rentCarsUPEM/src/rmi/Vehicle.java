package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface Vehicle extends Remote {
	
	public String getNumber() throws RemoteException;
	public double getRentalPrice() throws RemoteException;
	public String[][] getListNote() throws RemoteException;
	public String getCondition() throws RemoteException;
	public String getMark() throws RemoteException;
	public String getFuelType() throws RemoteException;
	public String getKind() throws RemoteException;
	public double getSalePrice() throws RemoteException;
	public void setState() throws RemoteException;
	public boolean isAvailable() throws RemoteException;
	public Set<LeasingInterface> getListLeasing() throws RemoteException;
	public Set<EmployeInterface> getWaitingList() throws RemoteException;
	public List<LeasingInterface> getCurrentRent() throws RemoteException;
	public List<String> getCharacteristics() throws RemoteException;
	public void addToCurrentRent(LeasingInterface leasing) throws RemoteException;
	public void addToWaitingList(EmployeInterface employe) throws RemoteException;
	public void setBought() throws RemoteException;
	public boolean isBought() throws RemoteException;
	public void addLeasing(LeasingInterface leasing) throws RemoteException;
	public void addNote(String note) throws RemoteException;
	public void removeToWait(EmployeInterface employe) throws RemoteException;
	public void notifyEmployes() throws RemoteException;
	public int getYearCirculated() throws RemoteException;
	public void clearCurrentRent() throws RemoteException;
}
