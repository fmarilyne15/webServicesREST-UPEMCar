package rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import rmi.ParkingInterface;
import rmi.Vehicle;

public class UpemCarsServiceVerticle extends AbstractVerticle {

	private final Bank bank = new Bank();

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/project";

		Registry registry = LocateRegistry.getRegistry();
		ParkingInterface parking = (ParkingInterface) registry.lookup(url + "/vehicles");

		Router route = Router.router(vertx);

		route.route("/").handler(CorsHandler.create(AllowedUrl.frontend.toString()).allowCredentials(true)
				.allowedMethod(HttpMethod.GET));
		route.route("/").handler(rc -> {
			try {
				String currency = rc.request().getParam("currency");
				JsonArray vehiclesAndCurrencies = new JsonArray();

				if (currency == null) {
					vehiclesAndCurrencies.add(vehiclesForSale(parking));
				} else {
					if (currency.equalsIgnoreCase("USD") || currency.isBlank()) {
						vehiclesAndCurrencies.add(vehiclesForSale(parking));
					} else {
						vehiclesAndCurrencies.add(vehiclesForSaleWithCurrencySelected(parking, currency));
					}
				}

				vehiclesAndCurrencies.add(getCurrencies());

				rc.response().end(vehiclesAndCurrencies.encodePrettily());
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		});

		route.route("/basket").handler(CorsHandler.create(AllowedUrl.frontend.toString()).allowCredentials(true)
				.allowedMethod(HttpMethod.GET));
		route.route("/basket").handler(rc -> {
			try {

				HttpServerRequest request = rc.request();
				String mail = request.getParam("mail");
				String password = request.getParam("password");
				String currency = request.getParam("currency");

				double currencyValue = getCurrencyValue(currency);
				Account account = bank.searchAccountByLoginAndPassword(mail, password);
				if (account != null) {
					rc.response().end(account.getBasket(currencyValue).encodePrettily());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		});

		route.route("/getPurchase").handler(CorsHandler.create(AllowedUrl.frontend.toString()).allowCredentials(true)
				.allowedMethod(HttpMethod.GET));
		route.route("/getPurchase").handler(rc -> {
			try {

				HttpServerRequest request = rc.request();
				String mail = request.getParam("mail");
				String password = request.getParam("password");
				String currency = request.getParam("currency");

				getPurchases(rc, mail, password, currency);

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		});

		route.route("/priceConversion").handler(CorsHandler.create(AllowedUrl.frontend.toString())
				.allowCredentials(true).allowedMethod(HttpMethod.POST).allowedHeader("Content-Type"));
		route.route("/priceConversion").handler(BodyHandler.create());
		route.route("/priceConversion").handler(rc -> {
			try {

				JsonObject data = rc.getBodyAsJson();
				String currency = data.getString("currency");
				rc.response().end(vehiclesForSaleWithCurrencySelected(parking, currency).encodePrettily());

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		});

		route.route("/addToBasket").handler(CorsHandler.create(AllowedUrl.frontend.toString()).allowCredentials(true)
				.allowedMethod(HttpMethod.POST).allowedHeader("Content-Type"));
		route.route("/addToBasket").handler(BodyHandler.create());
		route.route("/addToBasket").handler(rc -> {
			addToBasket(rc, parking);
		});

		route.route("/purchase").handler(CorsHandler.create(AllowedUrl.frontend.toString()).allowCredentials(true)
				.allowedMethod(HttpMethod.POST).allowedHeader("Content-Type"));
		route.route("/purchase").handler(BodyHandler.create());
		route.route("/purchase").handler(rc -> {
			buyVehicle(rc, parking);
		});

		route.route("/connection").handler(CorsHandler.create(AllowedUrl.frontend.toString()).allowCredentials(true)
				.allowedMethod(HttpMethod.POST).allowedHeader("Content-Type"));
		route.route("/connection").handler(BodyHandler.create());
		route.route("/connection").handler(rc -> {
			connection(rc);
		});

		vertx.createHttpServer().requestHandler(route::accept).listen(8080, res -> {
			if (res.succeeded()) {
				startFuture.complete();
				return;
			}
			startFuture.fail(res.cause());
		});
	}

	private void connection(RoutingContext rc) {

		JsonObject request = rc.getBodyAsJson();
		String login = request.getString("login");
		String password = request.getString("password");

		Account account = bank.searchAccountByLoginAndPassword(login, password);
		if (account == null) {
			rc.response().end("ko");
			return;
		}

		rc.response().end("ok");
	}

	private void buyVehicle(RoutingContext rc, ParkingInterface parking) {
		try {
			JsonObject request = rc.getBodyAsJson();

			String cardNumber = request.getString("cardNumber");
			String cvc = request.getString("cvc");
			String matVehicle = request.getString("matVehicle");

			Vehicle vehicle = parking.searchVehicle(matVehicle, "", "").get(0);

			Account account = bank.searchAccountByCardAndCvc(cardNumber, cvc);

			if (account == null) {
				rc.response().end("Carte non valide!");
				return;
			}

			if (checkSolde(account, vehicle)) {
				if (vehicle.isBought()) {
					rc.response().end("Véhicule déjà vendu");
					return;
				}
				account.setAmount(account.getAmount() - vehicle.getSalePrice());
				vehicle.setBought();
				account.addToPurchase(vehicle);
				rc.response().end("Achat effectué avec succès...");
				return;
			}
			rc.response().end("Solde insuffisant");
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
	}

	private boolean checkSolde(Account account, Vehicle vehicle) throws RemoteException {
		if (account.getAmount() < vehicle.getSalePrice()) {
			return false;
		}
		return true;
	}

	private void addToBasket(RoutingContext rc, ParkingInterface parking) {
		try {
			JsonObject request = rc.getBodyAsJson();

			String mail = request.getString("mail");
			String password = request.getString("password");
			String matVehicle = request.getString("matVehicle");

			Vehicle vehicle = parking.searchVehicle(matVehicle, "", "").get(0);

			Account account = bank.searchAccountByLoginAndPassword(mail, password);
			account.addToBasket(vehicle);

			rc.response().end(account.getBasket().encodePrettily());
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
	}

	private JsonArray vehiclesForSale(ParkingInterface parking) throws RemoteException {
		JsonArray vehicles = new JsonArray();
		for (Vehicle v : parking.getListVehicle()) {
			if (!v.getListLeasing().isEmpty() && !v.isBought()) {
				String kind = ("" + v.getKind().charAt(0)).toUpperCase() + v.getKind().substring(1);
				String mark = ("" + v.getMark().charAt(0)).toUpperCase() + v.getMark().substring(1);
				String fuel = ("" + v.getFuelType().charAt(0)).toUpperCase() + v.getFuelType().substring(1);
				vehicles.add(new JsonObject().put("kind", kind).put("mat", v.getNumber().toUpperCase())
						.put("mark", mark).put("fuel", fuel).put("circulated", v.getYearCirculated())
						.put("price", v.getSalePrice()).put("delivery", v.getCurrentRent().isEmpty() ? "Sans attente"
								: v.getCurrentRent().get(0).getReturnDate()));
			}
		}

		return vehicles;
	}

	private JsonArray vehiclesForSaleWithCurrencySelected(ParkingInterface parking, String currency) throws Exception {
		JsonArray vehicles = new JsonArray();
		double currencyValue = getCurrencyValue(currency);

		for (Vehicle v : parking.getListVehicle()) {
			if (!v.getListLeasing().isEmpty() && !v.isBought()) {
				String kind = StringUtils.capitalise(v.getKind());
				String mark = StringUtils.capitalise(v.getMark());
				String fuel = StringUtils.capitalise(v.getFuelType());
				vehicles.add(new JsonObject().put("kind", kind).put("mat", v.getNumber().toUpperCase())
						.put("mark", mark).put("fuel", fuel).put("circulated", v.getYearCirculated())
						.put("price", Math.round((v.getSalePrice() * currencyValue) * 100.0) / 100.0)
						.put("delivery", v.getCurrentRent().isEmpty() ? "Sans attente"
								: v.getCurrentRent().get(0).getReturnDate()));
			}
		}

		return vehicles;
	}

	private JsonArray getCurrencies() throws IOException {
		JsonArray allCurrencies = new JsonArray();

		String url_str = "http://www.apilayer.net/api/live?access_key=8196614c5b8da0ada863aaff5c3aee02";
		URL url = new URL(url_str);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		JsonParser parser = new JsonParser();
		JsonElement responseContent = parser.parse(new InputStreamReader((InputStream) request.getContent()));
		com.google.gson.JsonObject responseContentAsJson = responseContent.getAsJsonObject();

		JsonElement currency = responseContentAsJson.get("quotes");
		currency.getAsJsonObject().entrySet().forEach(entry -> {
			allCurrencies.add(entry.getKey().substring(3));
		});

		return allCurrencies;
	}

	private double getCurrencyValue(String currencySelected) throws IOException {

		String url_str = "http://www.apilayer.net/api/live?access_key=8196614c5b8da0ada863aaff5c3aee02";
		URL url = new URL(url_str);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		JsonParser parser = new JsonParser();
		JsonElement responseContent = parser.parse(new InputStreamReader((InputStream) request.getContent()));
		com.google.gson.JsonObject responseContentAsJson = responseContent.getAsJsonObject();

		JsonElement currency = responseContentAsJson.get("quotes");
		for (Entry<String, JsonElement> entry : currency.getAsJsonObject().entrySet()) {
			String key = entry.getKey().substring(3);
			if (key.equalsIgnoreCase(currencySelected)) {
				return Double.parseDouble(entry.getValue().getAsString());
			}
		}

		return 1000000;
	}

	private void getPurchases(RoutingContext rc, String mail, String password, String currency) throws IOException {
		Account account = bank.searchAccountByLoginAndPassword(mail, password);
		if (account == null) {
			System.out.println("Ce compte n'existe pas");
			return;
		}

		Set<Vehicle> myPurchases = account.getPurchase();
		double currencyValue = getCurrencyValue(currency);
		JsonArray purchases = new JsonArray();

		for (Vehicle vehicle : myPurchases) {
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
	}
}
