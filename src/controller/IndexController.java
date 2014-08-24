package controller;

import java.util.List;

import model.User;
import utils.Constants;
import validator.AdminActionValidator;
import validator.LoginActionValidator;
import validator.LoginStatusValidator;
import validator.RegisterActionValidator;

import com.jfinal.aop.Before;

import dao.UserDAO;
import dao.impl.MongoUserDAO;

public class IndexController extends JsonController {
	UserDAO uDao = new MongoUserDAO();
	
	public void index() {
		if(	getSessionAttr(Constants.ATTR_LOGIN_EMAIL) == null || 
				getSessionAttr(Constants.ATTR_LOGIN_EMAIL).toString().length() == 0	) {
			// 没有登录的状态
			renderFreeMarker("/portal/index.html");
		} else {
			forwardAction("/user/main");
		}
		
	}
	
	@Before(LoginActionValidator.class)
	public void login() {
		// login success
		getSession().setAttribute(Constants.ATTR_LOGIN_EMAIL, getPara("email"));
		redirect("/user/main", true);
	}
	
	@Before(LoginStatusValidator.class)
	public void logout() {
		getSession().removeAttribute(Constants.ATTR_LOGIN_EMAIL);
		redirect("/");
	}
	
	@Before(RegisterActionValidator.class)
	public void register() {
		String email = getPara("email");
		String password = getPara("password");
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setKeywords("");
		
		uDao.saveOrUpdate(user);
		getSession().setAttribute(Constants.ATTR_LOGIN_EMAIL, email);
		forwardAction("/user/main");
	}
	
	@Before(AdminActionValidator.class)
	public void allusers() {
		List<User> list = uDao.queryAll();
		renderGson(list);
	}
	
	
	public void regpage() {
		renderFreeMarker("/portal/register.html");
	}
	
}
