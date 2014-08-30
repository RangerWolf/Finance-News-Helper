package controller;

import java.util.List;
import java.util.Map;

import model.BlockPattern;
import model.Keyword;
import model.News;
import model.RestfulResponse;
import model.User;
import notifier.EmailNotifier;
import notifier.Notifier;
import utils.BlockPatternVerifyUtils;
import utils.Constants;
import utils.MiscUtils;
import validator.AdminActionValidator;
import validator.LoginStatusValidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.jfinal.aop.Before;

import crawler.Crawler;
import crawler.impl.GoogNewsCrawler;
import dao.BlockPatternDAO;
import dao.KeywordDAO;
import dao.NewsDAO;
import dao.UserDAO;
import dao.mysql.MySQLBlockPatternDAO;
import dao.mysql.MySQLKeywordDAO;
import dao.mysql.MySQLNewsDAO;
import dao.mysql.MySQLUserDAO;

@Before(LoginStatusValidator.class)
public class UserController extends JsonController {
	private UserDAO uDao = new MySQLUserDAO();
	private NewsDAO nDao = new MySQLNewsDAO();
	private KeywordDAO kDao = new MySQLKeywordDAO();
	private BlockPatternDAO bpDao = new MySQLBlockPatternDAO();
	
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
		List<String> allWords = kDao.query();
		List<String> newWords = Lists.newArrayList();
		
		for(String word : words) {
			Keyword kw = new Keyword(word);
			wordList.add(kw);
			if(!allWords.contains(word)) {
				newWords.add(word);
			}
		}
		kDao.save(wordList);
		
		// query news for new words
		newWords = MiscUtils.mergeKeywordList(newWords);
		Crawler crawler = new GoogNewsCrawler();
		Map<String, Boolean> map = Maps.newHashMap();
		for(String word : newWords) {
			String url = crawler.buildURL(word);
			List<News> list = crawler.parse(url);
			if(list.size() > 0) {
				map.put(word, crawler.save(list));
			} else {
				map.put(word, false);
			}
		}
		if(newWords.size() > 0) {
			System.out.println("Crawl news for new words:");
			System.out.println(new Gson().toJson(map));
		}
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
		List<News> finalList = Lists.newArrayList();
		List<BlockPattern> blockPatternList = bpDao.query();
		
		if(user.getKeywords().trim().length() > 0) {
			String[] keywords = user.getKeywords().split(",");
			
			List<News> list = nDao.query(keywords);
			List<String> rawNewsTitleList = Lists.newArrayList();
			List<String> rawNewsDescList = Lists.newArrayList();
			
			for(News news: list) {
				if( !MiscUtils.hasSimilarStr(news.getTitle(), rawNewsTitleList) &&
					!MiscUtils.hasSimilarStr(news.getDescription(), rawNewsDescList) &&
					!BlockPatternVerifyUtils.verify(blockPatternList, news.getNewsUrl()) &&
					!BlockPatternVerifyUtils.verify(blockPatternList, news.getTitle())
				  ) {
					rawNewsTitleList.add(news.getTitle());
					rawNewsDescList.add(news.getDescription());
					finalList.add(news);
				}
			}
		}
		renderGson(finalList);
	}
	
	public void notifyMe() {
		Notifier emailNotifier = new EmailNotifier(getSessionAttr(Constants.ATTR_LOGIN_EMAIL).toString());
		emailNotifier.run();
		renderText("done");
	}
	
}
