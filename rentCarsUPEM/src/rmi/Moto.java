package rmi;
import java.rmi.RemoteException;

public class Moto extends AbstractVehicle implements Vehicle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String kind;
	
	public Moto(String condition, String mark, String fuelType, double rentalPrice, String number,double salePrice) throws RemoteException {
		super(condition, mark, fuelType, rentalPrice, number, salePrice);	
		this.kind = "moto";
	}

	@Override
	public final String getKind() throws RemoteException {
		return kind;
	}
}
