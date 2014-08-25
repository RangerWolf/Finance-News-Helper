package controller;

import java.util.List;

import validator.AdminActionValidator;
import validator.MailAccountAddValidator;

import com.jfinal.aop.Before;

import model.MailAccount;
import dao.MailAccountDAO;
import dao.mongodb.MongoMailAccoutDAO;

public class MailAccountController extends JsonController{
	
	MailAccountDAO maDao = new MongoMailAccoutDAO();
	
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
