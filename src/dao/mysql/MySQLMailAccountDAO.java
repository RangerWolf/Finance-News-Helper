package dao.mysql;

import java.util.List;

import utils.Constants;
import utils.db.MySQLUtils;
import model.MailAccount;
import dao.MailAccountDAO;

public class MySQLMailAccountDAO implements MailAccountDAO {

	@Override
	public int addNewAccount(String email, String password) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MailAccount getActiveMailAccount() {
		return (MailAccount) MySQLUtils.query(MailAccount.class, "select * from mail_account where isActive = ?", Constants.LABEL_SUCCESS);
	}

	@Override
	public int deActiveMailAccount(String email) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int activeAllAccounts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MailAccount> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean clear() {
		// TODO Auto-generated method stub
		return false;
	}

}