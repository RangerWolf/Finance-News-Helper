package dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.News;
import utils.MiscUtils;
import utils.MongoDBUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import dao.NewsDAO;

public class MongoNewsDAO implements NewsDAO{

	public static final String COLLECTION_NAME = "news_record";
	MongoDBUtils mgoUtils = null;
	public MongoNewsDAO() {
		mgoUtils = new MongoDBUtils(COLLECTION_NAME);
		if(!MiscUtils.getHostName().startsWith("appid")) {
			try {
				mgoUtils.createUniqueIndex(COLLECTION_NAME, "newsUrl");
			} catch(Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Set unique index error!");
			}
		}
	}
	
	@Override
	public int save(List<News> newsList) {
		List<News> existsList = query();
		List<String> existsNewsUrlList = Lists.newArrayList();
		for(News news : existsList) {
			existsNewsUrlList.add(news.getNewsUrl());
		}
		List<News> insertedList = Lists.newArrayList();
		for(News news : newsList) {
			if(!existsNewsUrlList.contains(news.getNewsUrl()))
				insertedList.add(news);
		}
		return mgoUtils.save(insertedList.toArray());
	}
	
	@Override
	public List<News> query(String... keywords) {
		List<News> list = Lists.newArrayList();
		Map<String, Object> queryCondition = Maps.newHashMap();
		BasicDBList values = new BasicDBList();  
		if(keywords != null && keywords.length > 0) {
			for(String word: keywords) {
				if(word == null || word.trim().length() == 0) continue;
				values.add(new BasicDBObject("title", Pattern.compile(word)));
				values.add(new BasicDBObject("description", Pattern.compile(word)));
			}
			queryCondition.put("$or", values);
		}
		
		Long now = new Date().getTime();
		Long before = now - 3600L * 1000L * 10;
		queryCondition.put("dateTime", new BasicDBObject("$gt", before));
		
		DBCursor cursor = mgoUtils.query(queryCondition, 100, "dateTime", -1);
		System.out.println(cursor.count());
		try {
		    while (cursor.hasNext()) {
		    	News news = new Gson().fromJson(cursor.next().toString(), News.class);
		    	list.add(news);
		    }
		} finally {
		    cursor.close();
		}
		System.out.println(new Gson().toJson(list));
		return list;
	}
	
	@Override
	public List<News> query() {
		return query(null);
	}

	@Override
	public boolean clear() {
		return mgoUtils.clearAllDocument(COLLECTION_NAME);
	}
}
