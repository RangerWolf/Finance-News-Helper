package dao.impl;

import java.util.List;
import java.util.Map;

import model.MailAccount;
import utils.Constants;
import utils.MiscUtils;
import utils.MongoDBUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dao.MailAccountDAO;

public class MongoMailAccoutDAO implements MailAccountDAO{

	public static final String COLLECTION_NAME = "mail_account";
	MongoDBUtils mgoUtils = null;
	public MongoMailAccoutDAO() {
		mgoUtils = new MongoDBUtils(COLLECTION_NAME);
		if(!MiscUtils.getHostName().startsWith("appid")) {
			try {
				mgoUtils.createUniqueIndex(COLLECTION_NAME, "mailAddr");
			} catch(Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Set unique index error!");
			}
		}
	}
	
	@Override
	public MailAccount getActiveMailAccount() {
		MailAccount ma = null;
		Map<String, Object> queryCondition = Maps.newHashMap();
		queryCondition.put("isActive", Constants.ACCOUNT_ACTIVE);
		DBCursor cursor = mgoUtils.query(queryCondition, 1, "mailAddr", 1);
		try {
			if(cursor.hasNext())
				ma = new Gson().fromJson(cursor.next().toString(), MailAccount.class);
		} finally {
		    cursor.close();
		}
		return ma;
	}

	@Override
	public int deActiveMailAccount(String email) {
		MailAccount ma = getMailAccountByEmail(email);
		// update
		DBObject updateCondition=new BasicDBObject();
//		updateCondition.put("email", new BasicDBObject("$ne", null));
		updateCondition.put("mailAddr", email);
		
		DBObject updatedValue=new BasicDBObject();
		updatedValue.put("isActive", Constants.ACCOUNT_DEACTIVE);
		DBObject updateSetValue=new BasicDBObject("$set",updatedValue);
		
		return mgoUtils.update(updateCondition, updateSetValue);
	}

	@Override
	public int activeAllAccounts() {
		// update
		DBObject updateCondition=new BasicDBObject();
		updateCondition.put("mailAddr", new BasicDBObject("$ne", null));
		
		DBObject updatedValue=new BasicDBObject();
		updatedValue.put("isActive", Constants.ACCOUNT_DEACTIVE);
		DBObject updateSetValue=new BasicDBObject("$set",updatedValue);
		
		return mgoUtils.update(updateCondition, updateSetValue);
	}

	@Override
	public int addNewAccount(String email, String password) {
		if(getMailAccountByEmail(email) != null) {
			return -1; // account exists
		} else {
			// TODO Auto-generated method stub
			MailAccount account = new MailAccount();
			account.setIsActive(true);
			account.setMailAddr(email);
			account.setPassword(password);
			account.setUsedTimes(0);
			account.setUsername(email);
			
			return mgoUtils.save(account);
		}
	}
	
	private MailAccount getMailAccountByEmail(String email) {
		MailAccount ma = null;
		List<MailAccount> list = Lists.newArrayList();
		Map<String, Object> queryCondition = Maps.newHashMap();
		queryCondition.put("mailAddr", email);
		DBCursor cursor = mgoUtils.query(queryCondition, 1, "mailAddr", 1);
		try {
			if(cursor.hasNext())
				ma = new Gson().fromJson(cursor.next().toString(), MailAccount.class);
		} finally {
		    cursor.close();
		}
		return ma;
	}

	@Override
	public List<MailAccount> getAllAccounts() {
		List<MailAccount> list = Lists.newArrayList();
		DBCursor cursor = mgoUtils.queryAll("mailAddr", 1);
		try {
		    while (cursor.hasNext()) {
		    	MailAccount obj = new Gson().fromJson(cursor.next().toString(), MailAccount.class);
		    	list.add(obj);
		    }
		} finally {
		    cursor.close();
		}
		return list;
	}

	@Override
	public boolean clear() {
		// TODO Auto-generated method stub
		return mgoUtils.clearAllDocument(COLLECTION_NAME);
	}

}
