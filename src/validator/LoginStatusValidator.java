package validator;

import main.Constants;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * 暂时还不会用拦截器，使用验证器来代替进行登录状态验证
 * @author wenjun_yang
 *
 */
public class LoginStatusValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		// TODO Auto-generated method stub
		Object loginEmail = c.getSession().getAttribute(Constants.ATTR_LOGIN_EMAIL);
		if(loginEmail == null) {
			addError("errMsg", "需要先登录!");
		}
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		c.forwardAction("/login");
	}

}
