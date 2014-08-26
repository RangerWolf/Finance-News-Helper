package validator;

import model.User;

import org.apache.commons.lang.StringUtils;

import utils.Constants;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import dao.UserDAO;
import dao.mysql.MySQLUserDAO;

public class LoginActionValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		System.out.println("~~~~LoginEmail=" + c.getSessionAttr(Constants.ATTR_LOGIN_EMAIL));
		System.out.println("~~~~SessionID=" + c.getSession().getId());
		
		if(	c.getSession().getAttribute(Constants.ATTR_LOGIN_EMAIL) == null || 
			c.getSession().getAttribute(Constants.ATTR_LOGIN_EMAIL).toString().length() == 0	) {
			if(c.getPara("email") != null) {
				validateEmail("email", "errMsg", "邮件地址错误");
				validateRequiredString("password", "errMsg", "登录密码不能为空");
				
				// varify exists
				UserDAO userDao = new MySQLUserDAO();
				User user = userDao.query(c.getPara("email"));
				if(user == null || !StringUtils.equals(user.getPassword(), c.getPara("password"))) {
					addError("errMsg", "登录信息有误");
				}
			}  else {
				validateRequiredString("email", "errMsg", "");
			}
		}
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		c.keepPara("email");
		c.renderFreeMarker("/portal/login.html");
	}

}
