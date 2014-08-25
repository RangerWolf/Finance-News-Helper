package utils.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class MySQLUtils {
	
//	private static Logger logger = LogManager.getLogger(MySQLUtils.class.getName());
	//---------------- public methods -----------------
	
	// ----------------- private methods --------------
	public static Boolean truncate(String tablename) {
		QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource()); 
        try {
        	int result = qRunner.update("tuncate ?", tablename);
			return true;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
        return false;
	}
	
	public static Boolean update(String sql, Object... params) {
		Connection conn = MySQLConnTools.makeConnection();
		QueryRunner qRunner = new QueryRunner();
		try {
			int result = qRunner.update(conn, sql, params);
			return true;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			 DbUtils.closeQuietly(conn);
		}
		return false;
	}
	
	public static Boolean batchInsert(String sql, Object[][] objects) {
        Connection conn = MySQLConnTools.makeConnection(); 
        //创建SQL执行工具 
        QueryRunner qRunner = new QueryRunner(); 
        try {
			System.out.println(new Gson().toJson(qRunner.batch(conn, sql, objects)));
			return true;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			//关闭数据库连接 
	        DbUtils.closeQuietly(conn);
		}
        return false;
	}
	
	public static Boolean insert(String sql, Object... params) {
        //创建SQL执行工具 
        QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource()); 
        try {
        	int result = qRunner.update(sql, params);
			return true;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
        return false;
	}
	
	public static Object query(Class cls, String sql, Object... params) {
        try {
            QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource());
			Object obj = qRunner.query(sql, new BeanHandler(cls), params);
			return obj;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List query(String sql, Class cls) {
		//创建连接 
        List list = null;
        try {
            //创建SQL执行工具 
        	QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource());
            //执行SQL查询，并获取结果
			list = qRunner.query(sql, new BeanListHandler(cls));
			return list;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
		return list;
	}
	
	
	
	public static void main(String[] args) {
		
//		System.out.println(isMailSentBefore("[propertyweek@ubm.com]"));;
//		Feed feed = getNotUsedFeed();
//		System.out.println(feed == null);
//		System.out.println(new Gson().toJson(feed));
//		addNewFeeds("test1");
//		setFeedUsed("test1");
		
//		APKInfo info = new APKInfo();
//		info.setId("11");
//		info.setTitle("test2");
//		
//		APKInfo info2 = new APKInfo();
//		info2.setId("123");
//		info2.setTitle("aaaa"
//				);
//		List<APKInfo> list = new ArrayList<APKInfo>();
//		list.add(info);
//		list.add(info2);
		
		
	}
}
