package main;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;

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
//		PropertyConfigurator.configure(loadPropertyFile("log4j.properties"));
		
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
