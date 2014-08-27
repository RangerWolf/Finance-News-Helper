package main;

import org.apache.log4j.PropertyConfigurator;

import utils.MiscUtils;
import validator.AdminActionValidator;

import com.jfinal.aop.Before;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;

import controller.CrondController;
import controller.IndexController;
import controller.KeywordController;
import controller.MailAccountController;
import controller.MiscController;
import controller.NewsController;
import controller.NotifierController;
import controller.StockController;
import controller.UserController;

public class DefaultConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(true);
		String hostName = MiscUtils.getHostName();
		if(hostName.contains("wenjun") || hostName.contains("win7")) {
			PropertyConfigurator.configure(loadPropertyFile("log4j-local.properties"));
		} 
		
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
		me.add("/misc", MiscController.class);
		me.add("/news", NewsController.class);
		me.add("/keyword", KeywordController.class);
		me.add("/user", UserController.class);
		me.add("/ma", MailAccountController.class);
		me.add("/notify", NotifierController.class);
		me.add("/stock", StockController.class);
		me.add("/crond", CrondController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		me.add(new SessionInViewInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("base"));
	}

}
