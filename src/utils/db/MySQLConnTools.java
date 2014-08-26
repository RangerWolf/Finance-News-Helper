package utils.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import utils.MiscUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/** 
* 随便写个数据库连接获取工具，凑合着把例子跑起来 
* 
* @author leizhimin 2009-11-6 14:48:22 
*/ 
public class MySQLConnTools { 
         
		private static final String dirverClassName = "com.mysql.jdbc.Driver";
        public static DataSource getMySQLDataSource() {
        	String hostName = MiscUtils.getHostName();
        	
            MysqlDataSource mysqlDS = null;
            mysqlDS = new MysqlDataSource();
            
            // local, my laptop, default
             
            
            String url = "jdbc:mysql://127.0.0.1:3306/fin_news_helper?useUnicode=true&characterEncoding=utf8"; 
            String user = "root"; 
            String password = "";
            
            if(hostName.startsWith("appid")){  // 说明在BAE上
				// BAE上的hostname= ${APP ID}
            	url = "jdbc:mysql://sqld.duapp.com:4050/nCgXDCznnIguFddwUgYA?useUnicode=true&characterEncoding=utf8";
            	user = "INGNrdymi7y9yhDwMgxpsqQt";
            	password = "r3awv58TswHNiDYRCESSkiLtuwXttUbC";
			} 
            
            mysqlDS.setURL(url);
            mysqlDS.setUser(user);
            mysqlDS.setPassword(password);
            
            
            
            return mysqlDS;
        }
        
        public static Connection makeConnection() { 
            Connection conn = null; 
            try { 
                conn = getMySQLDataSource().getConnection();
            } catch (SQLException e) {
				e.printStackTrace();
			} 
            return conn; 
        } 
}