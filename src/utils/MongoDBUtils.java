package utils;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.News;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class MongoDBUtils {
	
	private DB db = null;
	private String collection = null;
	private static final int MAX_QUERY_COUNT = 10000;
	private static final Gson gson = new Gson();
	
	public MongoDBUtils(String collName) {
		db = getDB();
		this.collection = collName;
		if(!db.getCollectionNames().contains(collection)) {
			db.createCollection(collection, 
					new BasicDBObject("capped", false)
			.append("size", new Long(99999999)));
		}
	}
	
	private DB getDB() {
		String hostName = MiscUtils.getHostName();
		DB db = null;
		try {
			if(StringUtils.contains(hostName, "wenjun") || StringUtils.contains(hostName, "win7")) {	
				// 说明在我的笔记本或者我的win7服务器上
				MongoClient mongoClient = new MongoClient();
				db = mongoClient.getDB("local");
			} else if(hostName.startsWith("appid")){  // 说明在BAE上
				// BAE上的hostname= ${APP ID}
				MongoClient mongoClient = new MongoClient(new ServerAddress("mongo.duapp.com:8908"),
				Arrays.asList(MongoCredential.createMongoCRCredential("INGNrdymi7y9yhDwMgxpsqQt", "QwcAxUFGwMARPbxgHFQt", "r3awv58TswHNiDYRCESSkiLtuwXttUbC".toCharArray())),
				new MongoClientOptions.Builder().cursorFinalizerEnabled(false).build());
				db = mongoClient.getDB("QwcAxUFGwMARPbxgHFQt");
				db.authenticate("INGNrdymi7y9yhDwMgxpsqQt", "r3awv58TswHNiDYRCESSkiLtuwXttUbC".toCharArray());
			} else {  //此时应该是JAE的环境了
				MongoClient mongoClient = new MongoClient(Arrays.asList(
                        new ServerAddress("10.0.31.20", 27017),
                        new ServerAddress("10.0.31.21", 27017)));
				db = mongoClient.getDB("angerolf_mongo_gghtwptb");
				db.authenticate("8YryfrqT", "1dPk42q5Y88u".toCharArray());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return db;
	}
	
	/**
	 * 更新数据库记录，当目标值不存在的时候则保存
	 * @param updateCondition 设置更新的条件，比如 email=wenjun_yang@gmail.com
	 * @param updateSetValue 设置更新的值 比如 keyword=1,2,3 username=mengfan
	 * @return
	 */
	public int update(DBObject updateCondition, DBObject updateSetValue) {
		DBCollection coll = db.getCollection(collection);
		db.requestStart();
		try {
			db.requestEnsureConnection();
			WriteResult wr = coll.update(updateCondition, updateSetValue, true, true);
			return wr.getN();
		} finally {
		    db.requestDone();
		}
		
	}
	
	public int save(Object... objs) {
		DBCollection coll = db.getCollection(collection);
		List<DBObject> list = Lists.newArrayList();
		for(Object obj : objs) {
			String json = gson.toJson(obj);
			BasicDBObject dbObj = (BasicDBObject) JSON.parse(json);
			list.add(dbObj);
		}
		
		
		db.requestStart();
		try {
			db.requestEnsureConnection();
//			WriteResult wr = coll.insert(list);
//			if(wr != null) {
//				return wr.getN();
//			} else {
//				return -1;
//			}
			for(DBObject obj : list) {
				try {
					coll.save(obj);
				} catch(MongoException e) {
					System.out.println("!!!" + e.getMessage());
				}
				
			}
			return 0;
		} finally {
			   db.requestDone();
		}
	}
	
	/**
	 * 查询所有数据
	 * @param orderBy 排序的字段名
	 * @param order   排序 1:正序 -1:逆序
	 * @return
	 */
	public DBCursor queryAll(String orderBy, int order) {
		Map<String, Object> queryCondition = Maps.newHashMap();
		return query(queryCondition, MAX_QUERY_COUNT, orderBy, -1);
	}
	
	
	/**
	 * 多条件精确\范围查找<br>
	 * query = new BasicDBObject("j", new BasicDBObject("$ne", 3)) <br>
	 *  .append("k", new BasicDBObject("$gt", 10));<br>
	 *  .append("name", Pattern("James"));<br>
	 * @param queryCondition 查询条件
	 * @param limit 查询的记录数目限制
	 * @param orderBy g根据哪一列进行排序
	 * @param order 顺序 1:升序 -1：降序
	 */
	public DBCursor query(Map<String, Object> queryCondition, int limit, String orderBy, int order) {
		DBCursor cursor = null;
		try {
			if(order != 1 && order != -1) return cursor;
			DBCollection coll = db.getCollection(this.collection);
			BasicDBObject query = new BasicDBObject();
			for(String key: queryCondition.keySet()) {
				query.append(key, queryCondition.get(key));
			}
			int limitNumber = MAX_QUERY_COUNT;
			if(limit > 0) {
				limitNumber = limit;
			} 
			if(query.size() > 0) {
				cursor = coll.find(query).sort(new BasicDBObject(orderBy,order)).limit(limitNumber);
			} else { 
				cursor = coll.find().sort(new BasicDBObject(orderBy,order)).limit(limitNumber);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}
	

	public boolean dropCollection(String collection) {
		Set<String> set = db.getCollectionNames();
		if(set.contains(collection)) {
			DBCollection targetColl = db.getCollection(collection);
			targetColl.drop();
		}
		
		set = db.getCollectionNames();
		return set.contains(collection);
	}
	
	public boolean clearAllDocument(String collection) {
		DBCollection coll = db.getCollection(this.collection);
		coll.remove(new BasicDBObject());
		if(coll.find().size() != 0) return false;
		return true;
	}
	
	public void createUniqueIndex(String collection, String fieldName) {
		db.getCollection(collection).createIndex(new BasicDBObject(fieldName, 1), new BasicDBObject("unique", true));
	}
	
}
