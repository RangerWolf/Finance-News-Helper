package utils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.google.gson.Gson;

import dao.mysql.MySQLMailAccountDAO;

public class MySQLUtils {
	
//	private static Logger logger = LogManager.getLogger(MySQLUtils.class.getName());
	//---------------- public methods -----------------
	
	// ----------------- private methods --------------
	public static Boolean truncate(String tablename) {
		QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource()); 
        try {
        	int result = qRunner.update("truncate " + tablename);
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
	
	public static Boolean batchInsertWithBeans(String sql,String[] propertyName, Object... list) {
		Connection conn = MySQLConnTools.makeConnection(); 
        //创建SQL执行工具 
        QueryRunner qRunner = new QueryRunner(); 
        try {
        	PreparedStatement ps = conn.prepareStatement(sql);
        	for(Object o : list) {
        		qRunner.fillStatementWithBean(ps, o, propertyName);
        		ps.addBatch();
        	}
        	System.out.println(new Gson().toJson(ps.executeBatch()));
			return true;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
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
	
	public static Map query(String sql, Object... params) {
		try {
            QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource());
			Map obj = qRunner.query(sql, new MapHandler(), params);
			return obj;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	@SuppressWarnings("unchecked")
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
	public static List queryAll(String sql, Class cls) {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List queryList(String sql, Class cls, Object... params) {
		//创建连接 
        List list = null;
        try {
            //创建SQL执行工具 
        	QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource());
            //执行SQL查询，并获取结果
			list = qRunner.query(sql, new BeanListHandler(cls), params);
			return list;
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
		return list;
	}
	
	public static List<Map<String, Object>> queryList(String sql, Object... params) {
		List<Map<String, Object>> list = null;
		try {
            QueryRunner qRunner = new QueryRunner(MySQLConnTools.getMySQLDataSource());
            list = qRunner.query(sql, new MapListHandler(), params);
		} catch (SQLException e) {
//			logger.error(e.getMessage());
			e.printStackTrace();
		} 
		return list;
	}
	
	
	public static void main(String[] args) {
		MySQLMailAccountDAO dao = new MySQLMailAccountDAO();
		System.out.println(new Gson().toJson(dao.getActiveMailAccount()));;
	}
}
