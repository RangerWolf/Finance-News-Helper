package dao.impl;

import java.util.List;

import model.Keyword;
import utils.MiscUtils;
import utils.MongoDBUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.DBCursor;

import dao.KeywordDAO;

public class MongoKeywordDAO implements KeywordDAO{

	public static final String COLLECTION_NAME = "keyword";
	MongoDBUtils mgoUtils = null;
	public MongoKeywordDAO() {
		mgoUtils = new MongoDBUtils(COLLECTION_NAME);
		if(!MiscUtils.getHostName().startsWith("appid")) {
			try {
				mgoUtils.createUniqueIndex(COLLECTION_NAME, "word");
			} catch(Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Set unique index error!");
			}
		}
	}
	
	@Override
	public List<String> query() {
		List<String> list = Lists.newArrayList();
		DBCursor cursor = mgoUtils.queryAll("updateTime", -1);
		try {
		    while (cursor.hasNext()) {
		    	Keyword word = new Gson().fromJson(cursor.next().toString(), Keyword.class);
		    	list.add(word.getWord());
		    }
		} finally {
		    cursor.close();
		}
		return list;
	}

	@Override
	public int save(List<Keyword> list) {
		List<Keyword> insertedList = Lists.newArrayList();
		List<String> all = query();
		for(Keyword word : list) {
			if(!all.contains(word.getWord())) {
				insertedList.add(word);
			}
		}
		return mgoUtils.save(insertedList.toArray());
	}

	@Override
	public boolean clear() {
//		return mgoUtils.dropCollection(COLLECTION_NAME);
		return mgoUtils.clearAllDocument(COLLECTION_NAME);
	}

}
