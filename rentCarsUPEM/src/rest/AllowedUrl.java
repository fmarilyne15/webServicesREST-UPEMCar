package rest;

public enum AllowedUrl {
	frontend("http://localhost:3000");
	
	private final String value;
	private AllowedUrl(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
