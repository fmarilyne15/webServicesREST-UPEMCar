package rest;

import java.io.IOException;

import io.vertx.core.Vertx;

public class MainVerticle {
	
	public static void main(String[] args) throws IOException {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new UpemCarsServiceVerticle());
	}	
}
