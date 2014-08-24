package controller;

import java.util.List;

import model.Keyword;
import model.News;
import model.RestfulResponse;
import model.User;
import notifier.EmailNotifier;
import notifier.Notifier;
import utils.Constants;
import validator.AdminActionValidator;
import validator.LoginStatusValidator;

import com.google.common.collect.Lists;
import com.jfinal.aop.Before;

import dao.KeywordDAO;
import dao.NewsDAO;
import dao.UserDAO;
import dao.impl.MongoKeywordDAO;
import dao.impl.MongoNewsDAO;
import dao.impl.MongoUserDAO;

@Before(LoginStatusValidator.class)
public class UserController extends JsonController {
	private UserDAO uDao = new MongoUserDAO();
	private NewsDAO nDao = new MongoNewsDAO();
	private KeywordDAO kDao = new MongoKeywordDAO();
	
	public void index() {
		keepPara();
		forwardAction("/user/main");
	}
	
	public void main() {
		String currentUserEmail = getSessionAttr(Constants.ATTR_LOGIN_EMAIL);
		User user = uDao.query(currentUserEmail);
		setAttr("user", user);
		renderFreeMarker("/portal/main.html");
	}
	
	public void updateUserSetting() {
		String stockValue = getPara(Constants.ATTR_USER_STOCK);	
		String keywordVal = getPara(Constants.ATTR_USER_KEYWORD);
		String currentUserEmail = getSessionAttr(Constants.ATTR_LOGIN_EMAIL);
		User user = uDao.query(currentUserEmail);
		user.setStocks(stockValue);
		user.setKeywords(keywordVal);
		uDao.saveOrUpdate(user);
		
		// 同步的将keyword保存到数据库之中
		String[] words = keywordVal.trim().split(",");
		List<Keyword> wordList = Lists.newArrayList();
		for(String word : words) {
			Keyword kw = new Keyword(word);
			wordList.add(kw);
		}
		kDao.save(wordList);
		
		RestfulResponse rr = new RestfulResponse();
		rr.setRet(true);
		rr.setDesc("done");
		renderGson(rr);
	}
	
	public void size() {
		List<User> list = uDao.queryAll();
		renderText(list.size() + "");
	}
	
	public void all() {
		List<User> list = uDao.queryAll();
		renderGson(list);
	}
	
	@Before(AdminActionValidator.class)
	public void clear() {
		uDao.clear();
		redirect("/user");
	}
	
	public void latestNews() {
		User user = uDao.query(getSessionAttr(Constants.ATTR_LOGIN_EMAIL).toString());
		String[] keywords = user.getKeywords().split(",");
		List<News> list = nDao.query(keywords);
		renderGson(list);
	}
	
	public void notifyMe() {
		Notifier emailNotifier = new EmailNotifier(getSessionAttr(Constants.ATTR_LOGIN_EMAIL).toString());
		emailNotifier.run();
		renderText("done");
	}
	
}
