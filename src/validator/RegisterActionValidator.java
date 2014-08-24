package validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import model.User;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import dao.UserDAO;
import dao.impl.MongoUserDAO;

public class RegisterActionValidator extends Validator {

	UserDAO userDao = new MongoUserDAO();
	
	@Override
	protected void validate(Controller c) {
		validateEmail("email", "errMsg", "邮件地址错误");
		validateRequiredString("password", "errMsg", "登录密码不能为空");
		
		List<User> allUser = userDao.queryAll();
		//Verify if email exists or not
		for(User user : allUser) {
			
			if(user.getEmail().trim().equals(c.getPara("email"))) {
				System.out.println("!!" + user.getEmail());
				addError("errMsg", "该邮箱已被使用");
				break;
			}
			
		}
	}

	@Override
	protected void handleError(Controller c) {
		c.keepPara("email");
		if(StringUtils.equals("index", c.getPara("from")))
			c.forwardAction("/");
		else if(StringUtils.equals("regpage", c.getPara("from"))) {
			c.forwardAction("/regpage");
		} else {
			c.forwardAction("/regpage");
		}
	}

}
