package controller;

import java.util.List;

import model.MailAccount;
import validator.AdminActionValidator;
import validator.MailAccountAddValidator;

import com.jfinal.aop.Before;

import dao.MailAccountDAO;
import dao.mysql.MySQLMailAccountDAO;

public class MailAccountController extends JsonController{
	
	MailAccountDAO maDao = new MySQLMailAccountDAO();
	
	public void index() {
		renderText("under construction");
	}
	
	public void all() {
		List<MailAccount> accounts = maDao.getAllAccounts();
		renderGson(accounts);
	}
	
	@Before(MailAccountAddValidator.class)
	public void add() {
		String email = getPara("email");
		String password = getPara("password");
		int ret = maDao.addNewAccount(email, password);
		renderGson(ret);
	}
	
	@Before(AdminActionValidator.class)
	public void clear() {
		renderGson(maDao.clear());
	}

	
	
}
