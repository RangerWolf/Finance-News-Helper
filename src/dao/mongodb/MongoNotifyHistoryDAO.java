package dao.mongodb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import model.News;
import model.NotifyHistory;

import org.apache.commons.lang.StringUtils;

import utils.MiscUtils;
import utils.db.MongoDBUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dao.NotifyHistoryDAO;

public class MongoNotifyHistoryDAO implements NotifyHistoryDAO {

	public static final String COLLECTION_NAME = "notify_hist";
	MongoDBUtils mgoUtils = null;
	public MongoNotifyHistoryDAO() {
		mgoUtils = new MongoDBUtils(COLLECTION_NAME);
		if(!MiscUtils.getHostName().startsWith("appid")) {
			try {
				mgoUtils.createUniqueIndex(COLLECTION_NAME, "id");
			} catch(Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Set unique index error!");
			}
		}
	}
	
	@Override
	public NotifyHistory query(String id) {
		Map<String, Object> queryCondition = Maps.newHashMap();
		queryCondition.put("id", id);
		DBCursor cursor = mgoUtils.query(queryCondition, 1, "id", 1);
		if(cursor.hasNext()) {
			NotifyHistory hist = new Gson().fromJson(cursor.next().toString(), NotifyHistory.class);
			return hist;
		}
		return null;
	}

	@Override
	public boolean saveOrUpdate(NotifyHistory history) {
		if(history == null || StringUtils.isEmpty(history.getId()))
			return false;
		
		NotifyHistory oldHistory = query(history.getId());
		if(oldHistory == null) {
			mgoUtils.save(history);
		} else {
			DBObject updateCondition=new BasicDBObject();
			updateCondition.put("id", history.getId());
			
			DBObject updatedValue=new BasicDBObject();
			Long lastNotifyTime = history.getLastNotifyTime();
			if(lastNotifyTime != null)
				updatedValue.put("lastNotifyTime", lastNotifyTime);
			else {
				updatedValue.put("lastNotifyTime", new Date().getTime());
			}
			updatedValue.put("titleList", history.getTitleList());
			updatedValue.put("lastNotifyResult", history.getLastNotifyResult());
			DBObject updateSetValue=new BasicDBObject("$set",updatedValue);
			mgoUtils.update(updateCondition, updateSetValue);
		}
		return true;
	}
}
