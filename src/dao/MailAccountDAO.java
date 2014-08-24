package dao;

import java.util.List;

import model.MailAccount;

public interface MailAccountDAO {

	public int addNewAccount(String email, String password);
	public MailAccount getActiveMailAccount();
	public int deActiveMailAccount(String email);
	public int activeAllAccounts();
	public List<MailAccount> getAllAccounts();
	public boolean clear();
}
