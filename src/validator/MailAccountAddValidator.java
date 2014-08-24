package validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class MailAccountAddValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		// TODO Auto-generated method stub
		validateRequiredString("email", "emailErr", "缺少邮件地址");
		validateEmail("email", "emailErr", "邮件格式错误");
		validateRequiredString("password", "passErr", "缺少邮件密码");
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		c.renderText("Lack of parameter");
	}

	
	
}
