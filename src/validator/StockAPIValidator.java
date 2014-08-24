package validator;

import model.RestfulResponse;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class StockAPIValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if(actionKey.endsWith("search")) {
			validateRequiredString("q", "errMsg", "确实查询关键字");
		} else if(actionKey.endsWith("query")) {
			validateRequiredString("stocks", "errMsg", "确实查询关键字");
		}
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		RestfulResponse rr = new RestfulResponse();
		rr.setDesc("缺少查询关键字");
		rr.setRet(false);
		c.renderText(new Gson().toJson(rr));
	}

}
