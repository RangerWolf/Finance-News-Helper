package controller;

import java.util.List;
import java.util.Set;

import model.Keyword;
import model.User;

import org.apache.commons.lang.StringUtils;

import validator.AdminActionValidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jfinal.aop.Before;

import dao.KeywordDAO;
import dao.UserDAO;
import dao.impl.MongoKeywordDAO;
import dao.impl.MongoUserDAO;

@Before(AdminActionValidator.class)
public class KeywordController extends JsonController {
	private static KeywordDAO kDao = new MongoKeywordDAO();
	private static UserDAO uDao = new MongoUserDAO();
	public void index() {
		renderFreeMarker("/templates/subscribe.html");
	}
	
	public void update() {
		try {
			kDao.clear();
			List<User> userList = uDao.queryAll();
			Set<String> wordSet = Sets.newHashSet();
			for(User user: userList ) {
				
				String keyword = user.getKeywords();
				String[] wordArr = keyword.split(",");
				for(String word : wordArr) {
					wordSet.add(word);
				}
			}
			List<Keyword> wordList = Lists.newArrayList();
			for(String word : wordSet) {
				if(word.trim().length() > 0)
					wordList.add(new Keyword(word));
			}
			kDao.save(wordList);
			renderGson(kDao.query());
		} catch(Exception e) {
			renderText("keyword/update:" + e.getMessage());
		}
		
	}
	
	public void clear() {
		kDao.clear();
		renderGson(kDao.query().size());
	}
	
	public void show() {
		renderGson(kDao.query());
	}
	
	public void add() {
		String keyword = getPara("keyword");
		if(StringUtils.isEmpty(keyword)) renderText("Error");
		else {
			String[] keywordList = keyword.split(",");
			List<Keyword> list = Lists.newArrayList();
			for(String word : keywordList) {
				if(word.trim().length() > 0 && 
						word.trim().length() < 10) {
					list.add(new Keyword(word));
				}
			}
			KeywordDAO kDao = new MongoKeywordDAO();
			int ret = kDao.save(list);
			renderGson(ret);
		}
	}
	
}
