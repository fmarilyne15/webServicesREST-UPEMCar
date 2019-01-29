package rest;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Bank {

	/*private long carNumber = 00000004;
	private int cvc = 004;*/
	private final Set<Account> listAccount;
	
	public Bank() {
		this.listAccount = new HashSet<>();
		Account lm = new Account(00000001, 001);
		lm.setEmail("lm@gmail.com");
		lm.setPassword("00");
		lm.setAmount(10000);

		Account mf = new Account(00000002, 002);
		mf.setEmail("mf@gmail.com");
		mf.setPassword("01");
		mf.setAmount(11000);
		
		Account mo = new Account(00000003, 003);
		mo.setEmail("mo@gmail.com");
		mo.setPassword("02");
		mo.setAmount(12000);
		listAccount.add(lm);
		listAccount.add(mf);
		listAccount.add(mo);
	}
	
	public final Set<Account> getListAccount(){
		return listAccount;
	}

	public final Account searchAccountByLoginAndPassword(String email, String password){
		Objects.requireNonNull(email);
		Objects.requireNonNull(password);
		
		Account account = null;
		for (Account c : listAccount) {
			if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
				account = c;
				break;
			}
		}
		
		return account;
	}
	
	public final Account searchAccountByCardAndCvc(String cardNumber, String cvc){
		Objects.requireNonNull(cardNumber);
		Objects.requireNonNull(cvc);
		
		if (!isNumber(cardNumber) || !isNumber(cvc)) {
			return null;
		}
		
		Account account = null;
		for (Account c : listAccount) {
			if (c.getCarNumber() == Long.parseLong(cardNumber) && c.getCvc() == Long.parseLong(cvc)) {
				account = c;
				break;
			}
		}
		
		return account;
	}
	
	private final boolean isNumber(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/*public final void createAccount(String email, String password){
		Objects.requireNonNull(email);
		Objects.requireNonNull(password);
		
		Account account = new Account(carNumber, cvc);
		account.setEmail(email);
		account.setPassword(password);
		account.setAmount(100000);
		listAccount.add(account);
	}*/
	
}
