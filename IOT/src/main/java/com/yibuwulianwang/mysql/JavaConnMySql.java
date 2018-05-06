package com.yibuwulianwang.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yibuwulianwang.handle.Handle;

public class JavaConnMySql {
	static Connection conn; // 与特定数据库的连接（会话）的变量
	final static String driver = "com.mysql.jdbc.Driver"; // jdbc驱动
	final static String url = "jdbc:mysql://yibuwulianwang.com:3306/yibuwulianwang"; // 指向要访问的数据库！注意后面跟的是数据库名称
	final static String user = "root"; // navicat for sql配置的用户名
	final static String password = "root"; // navicat for sql配置的密码
	final static String sql_insert = "insert into iot_temp_hum(date,temperature,humidity) values(?,?,?)";

	static {
		try {
			Class.forName(driver); // 用class加载动态链接库——驱动程序？？？
			conn = DriverManager.getConnection(url, user, password); // 利用信息链接数据库
			if (!conn.isClosed())
				Handle.printMysqlLog("JavaConnMySql", "Succeeded connecting to the Database!");

			// insertdata(conn); //插入数据

			// conn.close();
		} catch (ClassNotFoundException e) { // catch不同的错误信息，并报错
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Handle.printMysqlLog("JavaConnMySql", "数据库各项操作顺利进行！");
		}
	}

	public static void insertdata(String temp, String hum) // 插入数据函数
	{
		try {
			if (conn.isClosed()) {
				Handle.printMysqlLog("JavaConnMySql","数据库连接中断！正在重连");
				Class.forName(driver); // 用class加载动态链接库——jdbc驱动
				conn = DriverManager.getConnection(url, user, password); // 利用信息链接数据库
			}
			Handle.printMysqlLog("JavaConnMySql",sql_insert);
			PreparedStatement psql = conn.prepareStatement(sql_insert); // 用preparedStatement预处理来执行sql语句
			psql.setString(1, Handle.getDate());// 给其五个参量分别“赋值”
			psql.setString(2, temp);
			psql.setString(3, hum);
			psql.executeUpdate(); // 参数准备后执行语句
			psql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Handle.printMysqlLog("JavaConnMySql","数据库数据插入成功！");
		}
	}

}
