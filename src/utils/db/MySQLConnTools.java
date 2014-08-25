package utils.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/** 
* 随便写个数据库连接获取工具，凑合着把例子跑起来 
* 
* @author leizhimin 2009-11-6 14:48:22 
*/ 
public class MySQLConnTools { 
        private static String dirverClassName = "com.mysql.jdbc.Driver"; 
        private static String url = "jdbc:mysql://127.0.0.1:3306/fin_news_helper?useUnicode=true&characterEncoding=utf8"; 
        private static String user = "root"; 
        private static String password = ""; 

        public static DataSource getMySQLDataSource() {
            MysqlDataSource mysqlDS = null;
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(url);
            mysqlDS.setUser(user);
            mysqlDS.setPassword(password);
            return mysqlDS;
        }
        
        public static Connection makeConnection() { 
                Connection conn = null; 
                try { 
                        Class.forName(dirverClassName); 
                } catch (ClassNotFoundException e) { 
                        e.printStackTrace(); 
                } 
                try { 
                        conn = DriverManager.getConnection(url, user, password); 
                } catch (SQLException e) { 
                        e.printStackTrace(); 
                } 
                return conn; 
        } 
}