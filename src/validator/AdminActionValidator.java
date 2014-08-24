package validator;

import org.apache.commons.lang.StringUtils;

import utils.MiscUtils;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;


public class AdminActionValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		// TODO Auto-generated method stub
		String password = c.getPara();
		if( StringUtils.isEmpty(password) ||
			!StringUtils.equals(MiscUtils.MD5(password),"FA246D0262C3925617B0C72BB20EEB1D")) {
			addError("cause", "需要密码才能继续");
		}
	}

	@Override
	protected void handleError(Controller c) {
		// TODO Auto-generated method stub
		//c.renderFreeMarker("/templates/error.html");
		c.renderText("Error");
	}

}
