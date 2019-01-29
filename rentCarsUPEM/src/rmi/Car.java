package rmi;
import java.rmi.RemoteException;

public class Car extends AbstractVehicle implements Vehicle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String kind;

	public Car(String condition, String mark, String fuelType, double rentalPrice, String number, double salePrice) throws RemoteException {
		super(condition, mark, fuelType, rentalPrice, number, salePrice);
		this.kind = "voiture";
	}

	@Override
	public String getKind() throws RemoteException {
		return kind;
	}

}
