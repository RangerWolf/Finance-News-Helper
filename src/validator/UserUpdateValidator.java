package validator;

import model.RestfulResponse;
import utils.Constants;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class UserUpdateValidator extends Validator {

	
	@Override
	protected void validate(Controller c) {
		validateRequiredString(Constants.ATTR_USER_STOCK, "errMsg", "缺少股票代码");
		validateRequiredString(Constants.ATTR_USER_KEYWORD, "errMsg", "缺少关键字");
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		RestfulResponse rr = new RestfulResponse();
		rr.setRet(false);
		rr.setDesc("缺少相关信息");
		c.renderText(new Gson().toJson(rr));
	}

	
	
}
