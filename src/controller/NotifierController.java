package controller;

import model.User;
import notifier.EmailNotifier;
import notifier.Notifier;

import org.apache.commons.lang.StringUtils;

import validator.AdminActionValidator;

import com.jfinal.aop.Before;

import dao.UserDAO;
import dao.mongodb.MongoUserDAO;

public class NotifierController extends JsonController {

	UserDAO userDao = new MongoUserDAO();
	
	public void index() {
		renderGson("under contruction");
	}
	
	public void sendNotify() {
		try {
			String email = getPara("email");
			if(StringUtils.isEmpty(email)) renderText("need specify email");
			else {
				User user = userDao.query(email);
				if(user == null) renderText("Wrong email");
				else {
					Notifier emailNotifier = new EmailNotifier(email);
					emailNotifier.run();
					renderText("done");
				}
			} 
		} catch (Exception e) {
			renderText(e.getMessage());
		}
	}
	
}
