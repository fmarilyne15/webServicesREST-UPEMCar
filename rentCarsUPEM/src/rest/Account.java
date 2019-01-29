package rest;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.StringUtils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import rmi.Vehicle;

public class Account {
	
	private String email;
	private String password;
	private double amount;
	private final long carNumber;
	private final int cvc;
	private final JsonArray basket;
	private final Set<Vehicle> vehiclesInBasket;
	private final Set<Vehicle> vehiclesPurchased;
	
	public Account(long carNumber,int cvc) {
		this.carNumber = carNumber;
		this.cvc = cvc;
		this.basket = new JsonArray();
		this.vehiclesInBasket = new HashSet<>();
		this.vehiclesPurchased = new HashSet<>();
	}
	
	public final String getEmail() {
		return email;
	}
	
	public final void setEmail(String email) {
		this.email = email;
	}
	
	public final String getPassword() {
		return password;
	}
	
	public final void setPassword(String password) {
		this.password = password;
	}

	public final double getAmount() {
		return amount;
	}

	public final void setAmount(double amount) {
		this.amount = amount;
	}

	public final long getCarNumber() {
		return carNumber;
	}

	public final int getCvc() {
		return cvc;
	}	
	

	public final JsonArray getBasket() throws RemoteException {
		
		Set<Vehicle> myBaskek = vehiclesInBasket.stream().filter(v -> {
			try {
				return !v.isBought();
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
		}).collect(Collectors.toSet());
		
		JsonArray basket = new JsonArray(); 
		for (Vehicle vehicle : myBaskek) {
			basket.add(new JsonObject().put("kind", StringUtils.capitalise(vehicle.getKind())).put("mat", vehicle.getNumber().toUpperCase()).put("mark", StringUtils.capitalise(vehicle.getMark()))
					.put("circulated", vehicle.getYearCirculated()).put("fuel", StringUtils.capitalise(vehicle.getFuelType())).put("price", vehicle.getSalePrice())
					.put("delivery", vehicle.getCurrentRent().isEmpty() ? 
							"Sans attente" : vehicle.getCurrentRent().get(0).getReturnDate()));
		}
		return basket;
	}

	public final JsonArray getBasket(double currencyValue) throws RemoteException {
		
		Set<Vehicle> myBaskek = vehiclesInBasket.stream().filter(v -> {
			try {
				return !v.isBought();
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
		}).collect(Collectors.toSet());
		
		JsonArray basket = new JsonArray(); 
		for (Vehicle vehicle : myBaskek) {
			basket.add(new JsonObject().put("kind", StringUtils.capitalise(vehicle.getKind())).put("mat", vehicle.getNumber().toUpperCase()).put("mark", StringUtils.capitalise(vehicle.getMark()))
					.put("circulated", vehicle.getYearCirculated()).put("fuel", StringUtils.capitalise(vehicle.getFuelType())).put("price", Math.round((vehicle.getSalePrice() * currencyValue) * 100.0) / 100.0)
					.put("delivery", vehicle.getCurrentRent().isEmpty() ? 
							"Sans attente" : vehicle.getCurrentRent().get(0).getReturnDate()));
		}
		return basket;
	}
	
	public final void addToBasket(Vehicle vehicle) throws RemoteException {
		Objects.requireNonNull(vehicle);
		vehiclesInBasket.add(vehicle);
		
		for (int i = 0; i < basket.size(); i++) {
			if (basket.getJsonObject(i).getString("mat").equalsIgnoreCase(vehicle.getNumber())) {
				return;
			}
		}
		
		basket.add(new JsonObject().put("kind", StringUtils.capitalise(vehicle.getKind())).put("mat", vehicle.getNumber().toUpperCase()).put("mark", StringUtils.capitalise(vehicle.getMark()))
				.put("circulated", vehicle.getYearCirculated()).put("fuel", StringUtils.capitalise(vehicle.getFuelType())).put("price", vehicle.getSalePrice())
				.put("delivery", vehicle.getCurrentRent().isEmpty() ? 
						"Sans attente" : vehicle.getCurrentRent().get(0).getReturnDate()));
	}
	
	public Set<Vehicle> getPurchase() {
		return vehiclesPurchased;
	}
	
	public final void addToPurchase(Vehicle vehicle) throws RemoteException {
		Objects.requireNonNull(vehicle);
		vehiclesPurchased.add(vehicle);
	}
	
	/*public void getPurchases(RoutingContext rc, double currencyValue) throws IOException {
		JsonArray purchases = new JsonArray();
		for (Vehicle vehicle : vehiclesPurchased) {
			purchases.add(new JsonObject()
					.put("kind", StringUtils.capitalise(vehicle.getKind()))
					.put("mat", vehicle.getNumber().toUpperCase())
					.put("mark", StringUtils.capitalise(vehicle.getMark()))
					.put("circulated", vehicle.getYearCirculated())
					.put("fuel", StringUtils.capitalise(vehicle.getFuelType()))
					.put("price", Math.round((vehicle.getSalePrice() * currencyValue) * 100.0) / 100.0)
					.put("delivery", vehicle.getCurrentRent().isEmpty() ? "Sans attente"
							: vehicle.getCurrentRent().get(0).getReturnDate()));
		}
		rc.response().end(purchases.encodePrettily());
	}*/
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Account)) {
			return false;
		}
		
		Account c = (Account) o;
		return Objects.equals(email, c.email) && Objects.equals(carNumber, c.carNumber) && Objects.equals(cvc, c.cvc);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email, carNumber, cvc);
	}
}
