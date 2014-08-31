package validator;

import main.Constants;
import model.RestfulResponse;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class UserUpdateValidator extends Validator {

	private final String ERROR_MSG_KEY = "errMsg";
	
	@Override
	protected void validate(Controller c) {
		validateRequiredString(Constants.ATTR_USER_STOCK, ERROR_MSG_KEY, "缺少股票代码");
		validateRegex(Constants.ATTR_USER_STOCK, "/^[,0-9]*$/", ERROR_MSG_KEY, "股票代码错误");
		validateRequiredString(Constants.ATTR_USER_KEYWORD, ERROR_MSG_KEY, "缺少关键字");
		validateRegex(Constants.ATTR_USER_KEYWORD, "/^[,a-zA-z0-9\u4E00-\u9FA5]*$/", ERROR_MSG_KEY, "只能中文、英文或者数字，不支持标点符号");
		
		// keyword的长度不能超过20个关键字
		String keyword = c.getPara(Constants.ATTR_USER_KEYWORD);
		String[] words = keyword.split(","); 
		if(words.length > 20) {
			addError(ERROR_MSG_KEY, "不能超过20个关键字");
		} else {
			boolean flagOfLengthVarify = true;
			for(String word : words ) {
				if(word.trim().length() > 7 || word.trim().length() < 2) {
					flagOfLengthVarify = false;
					break;
				}
			}
			if(!flagOfLengthVarify) {
				addError(ERROR_MSG_KEY, "关键字的长度应该在2~7之间");
			}
		}
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
