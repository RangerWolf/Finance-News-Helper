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
		validateRegex(Constants.ATTR_USER_STOCK, "/^[,0-9]*$/", "errMsg", "股票代码错误");
		validateRequiredString(Constants.ATTR_USER_KEYWORD, "errMsg", "缺少关键字");
		validateRegex(Constants.ATTR_USER_KEYWORD, "/^[,a-zA-z0-9\u4E00-\u9FA5]*$/", "errMsg", "只能中文、英文或者数字，不支持标点符号");
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		RestfulResponse rr = new RestfulResponse();
		rr.setRet(false);
		rr.setDesc("Error");
		c.renderText(new Gson().toJson(rr));
	}

	
	
}
