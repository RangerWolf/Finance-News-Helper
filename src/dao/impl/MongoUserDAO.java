package dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import model.User;
import utils.MiscUtils;
import utils.MongoDBUtils;
import validator.AdminActionValidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.jfinal.aop.Before;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dao.UserDAO;

public class MongoUserDAO implements UserDAO{

	public static final String COLLECTION_NAME = "userinfo";
	MongoDBUtils mgoUtils = null;
	Gson gson = new Gson();
	public MongoUserDAO() {
		mgoUtils = new MongoDBUtils(COLLECTION_NAME);
		if(!MiscUtils.getHostName().startsWith("appid")) {
			try {
				mgoUtils.createUniqueIndex(COLLECTION_NAME, "email");
			} catch(Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Set unique index error!");
			}
		}
	}
	
	@Override
	public void saveOrUpdate(User user) {
		User u = query(user.getEmail());
		if(u != null) {
			// update 
			// 更新条件
			DBObject updateCondition=new BasicDBObject();
			updateCondition.put("email", user.getEmail());
			
			// 设置的新值
			DBObject updatedValue=new BasicDBObject();
			updatedValue.put("keywords", user.getKeywords());
			updatedValue.put("password", user.getPassword());
			updatedValue.put("stocks", user.getStocks());
			DBObject updateSetValue=new BasicDBObject("$set",updatedValue);
			
			mgoUtils.update(updateCondition, updateSetValue);
		} else {
			mgoUtils.save(user);
		}
		
	}

	@Override
	public List<User> queryAll() {
		List<User> list = Lists.newArrayList();
		DBCursor cursor = mgoUtils.queryAll("email", 1);
		try {
		    while (cursor.hasNext()) {
		    	User u = gson.fromJson(cursor.next().toString(), User.class);
		    	list.add(u);
		    }
		} finally {
		    cursor.close();
		}
		System.out.println(new Gson().toJson(list));
		return list;
	}

	@Override
	public User query(String email) {
		Map<String, Object> queryCondition = Maps.newHashMap();
		if(!StringUtils.isEmpty(email))
			queryCondition.put("email", email);
		DBCursor cursor = mgoUtils.query(queryCondition, 1, "email", 1);
		if(cursor.count() == 0) {
			return null;
		} else {
			User u = gson.fromJson(cursor.next().toString(), User.class);
			return u;
		}
	}

	@Override
	@Before(AdminActionValidator.class)
	public void clear() {
		// TODO Auto-generated method stub
		mgoUtils.clearAllDocument(COLLECTION_NAME);
	}

}
