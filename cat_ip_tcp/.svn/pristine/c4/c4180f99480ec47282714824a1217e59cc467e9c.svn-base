package com.cgltech.cat_ip_tcp.handle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.cgltech.cat_ip_tcp.test.Database_connection_test;

/**
 * JDBC线程安全的Druid数据库连接池工具类.<br>
 * 
 * @author hkb <br>
 */
public class DruidJdbcUtils {

    /** 日志连接对象 */
    private static final Logger log = LoggerFactory.getLogger(DruidJdbcUtils.class);
    /** 数据源 */
    private static DruidDataSource dataSource = new DruidDataSource();
    /** 放置数据库连接的局部线程变量 */
    private static ThreadLocal<Connection> container = new ThreadLocal<Connection>();

    static {
        // 初始化加载数据源配置
        initDs();
        
        //单独写个方法测试启动时连接数据库并执行查询语句。
        Database_connection_test.connnectionTest();
    }

    /**
     * 初始化
     */
    private static void initDs() {
        try {
        	
// 读取配置文件
//            String url = PropertyUtils.getProperty("db.jdbcUrl");
//            String user = PropertyUtils.getProperty("db.user");
//            String password = PropertyUtils.getProperty("db.password");
//            String driverClass = PropertyUtils.getProperty("db.driverClass");
        	
        	SingletonProperty sp = SingletonProperty.getInstance(); 	
            String url = sp.getPropertyValue("systemConfig.mysql.url");
            String user = sp.getPropertyValue("systemConfig.mysql.user");
            String password = sp.getPropertyValue("systemConfig.mysql.password");
            String driverClass = sp.getPropertyValue("systemConfig.mysql.driver");
            
            dataSource.setDriverClassName(driverClass);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            // 连接数配置
            dataSource.setInitialSize(3);
            dataSource.setMinIdle(1);
            dataSource.setMaxActive(5);
            dataSource.setTestOnBorrow(true);
            dataSource.setTestOnReturn(true);
            // destory线程检测时间,隔多久检测一次连接有效性(单位:毫秒)
            dataSource.setTimeBetweenEvictionRunsMillis(30000);
            // 连接生存最小时间(单位 :毫秒)
            dataSource.setMinEvictableIdleTimeMillis(1500000);
            // for mysql
            dataSource.setPoolPreparedStatements(false);
            // 使用心跳语句检测空闲连接
            dataSource.setTestWhileIdle(true);
            dataSource.setValidationQuery("show status like '%Service_Status%';");
            // 断开重连时间,不要设置过短
            dataSource.setTimeBetweenConnectErrorMillis(10000);
            // 启用监控统计功能
            dataSource.setFilters("stat");
        } catch (Exception e) {
            log.error("连接池初始化异常，异常信息：" + e.getMessage());
        }

    }

    /**
     * 获取连接方法
     * 
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            container.set(conn);
        } catch (Exception e) {
            log.error("获取数据库连接异常，异常信息：" + e.getMessage());
        }

        return conn;
    }

    /**
     * 获取当前线程上的连接开启事务
     */
    public static void startTransaction() {
        // 首先获取当前线程的连接
        Connection conn = container.get();
        // 如果连接为空
        if (conn == null) {
            // 从连接池中获取连接
            conn = getConnection();
            // 将此连接放在当前线程上
            container.set(conn);
            log.info(Thread.currentThread().getName() + "-空连接从dataSource获取连接");
        } else {
            log.info(Thread.currentThread().getName() + "-从缓存中获取连接");
        }
        try {
            // 开启事务
            conn.setAutoCommit(false);
        } catch (Exception e) {
            log.error("开启事务异常！异常信息：" + e.getMessage());
        }
    }

    /**
     * 提交事务
     */
    public static void commit() {
        try {
            Connection conn = container.get();
            // 从当前线程上获取连接，如果连接为空，则不做处理
            if (null != conn) {
                // 提交事务
                conn.commit();
                log.info(Thread.currentThread().getName() + "-事务已经提交......");
            }
        } catch (Exception e) {
            log.error("提交事务异常！异常信息：" + e.getMessage());
        }
    }

    /*** 回滚事务 */
    public static void rollback() {
        try {
            // 检查当前线程是否存在连接
            Connection conn = container.get();
            if (conn != null) {
                // 回滚事务
                conn.rollback();
                // 如果回滚了，就移除这个连接
                container.remove();
            }
        } catch (Exception e) {
            log.error("回滚事务异常！异常信息：" + e.getMessage());
        }
    }

    /**
     * 关闭连接
     */
    public static void close() {
        try {
            Connection conn = container.get();
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            log.error("关闭连接异常，异常信息：" + e.getMessage());
        } finally {
            try {
                container.remove();
            } catch (Exception e2) {
                log.error("移除共享变量失败！异常信息：" + e2.getMessage());
            }
        }
    }

    /**
     * 查询结果集
     * 
     * @param sql
     * @param params
     * @return
     */
    public static List<Object> executeQuery(String sql, Object[] params) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Object> resultList = new ArrayList<Object>();
        try {
            preparedStatement = getConnection().prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int i = 0, len = params.length; i < len; i++) {
                    preparedStatement.setObject(i + 1, params[i] + "");
                }
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = resultSetMetaData.getColumnName(i + 1);
                    Object columnValue = resultSet.getObject(columnName);
                    resultMap.put(columnName, columnValue);
                }
                resultList.add(resultMap);
            }
            return resultList;
        } catch (Exception e) {
            log.error("查询数据库失败！异常信息：" + e.getMessage());
        } finally {
            // 如果这里不关闭，就手动关闭
            close();
        }
        return resultList;
    }
    /**
	 * 根据指定Sql语句和Sql语句中所需要的参数，修改记录
	 * 
	 * @param dsName
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */

	public static int executeUpdate(String sql, Object[] params) {
		Connection conn = null;
//		ResultSet rs = null;
		PreparedStatement psmt = null;
		
		int  key = -1 ;
		try {
			//创建连接
			conn = getConnection();
		 		
			psmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					psmt.setObject(i + 1, params[i]);
				}
			}
			psmt.executeUpdate();
			//获取数据库生成的主键值
			
			ResultSet rs = psmt.getGeneratedKeys();
			if  (rs.next())
			{
				key = rs.getInt(1);
			}
        } catch (Exception e) {
        	log.error("sql语句是：" + sql);
        	log.error("params：" + params);
            log.error("更新数据库失败！异常信息：",e);
            
        } finally {
            // 如果这里不关闭，就手动关闭
            close();
        }
		return  key;
		
	}
    public static void main(String[] args) {
        final String sql = "select count(1) cn from test";
        // 事务测试
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    Connection conn = DruidJdbcUtils.getConnection();
                    DruidJdbcUtils.startTransaction();
                    System.out.println(conn);
                    System.out.println(Thread.currentThread().getName() + "-执行事务操作......");
                    DruidJdbcUtils.commit();
                    DruidJdbcUtils.close();
                }
            }).start();
        }

        // 查询测试
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @SuppressWarnings("unchecked")
                public void run() {
                    List<Object> list = executeQuery(sql, null);
                    Map<String, Object> map = (Map<String, Object>) list.get(0);
                    System.out.println("查询结果：" + map.get("cn").toString());
                }
            }).start();
        }
    }

}
