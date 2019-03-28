package cn.pconline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class DBManager {
	private static Configuration conf;
    static {
        Properties properties=new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf=new Configuration();
        conf.setDriver(properties.getProperty("driver"));
        conf.setPoPackage(properties.getProperty("poPackage"));
        conf.setSrcPath(properties.getProperty("srcPath"));
        conf.setPwd(properties.getProperty("pwd"));
        conf.setUser(properties.getProperty("user"));
        conf.setUrl(properties.getProperty("url"));
        conf.setUsingDB(properties.getProperty("usingDB"));
        conf.setTableprefix(properties.getProperty("tableprefix"));
        conf.setServicePackage(properties.getProperty("servicePackage"));
        conf.setControllerPackage(properties.getProperty("controllerPackage"));
        
    /*    System.out.println(properties.getProperty("driver"));
        System.out.println(properties.getProperty("poPackage"));
        System.out.println(properties.getProperty("srcPath"));
        System.out.println(properties.getProperty("pwd"));
        System.out.println(properties.getProperty("user"));
        System.out.println(properties.getProperty("url"));
        System.out.println(properties.getProperty("usingDB"));*/
    }

    public static Connection getConn(){
        try {
            //要求JVM查找并加载指定的数据库驱动
            Class.forName(conf.getDriver());
            return  DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Configuration getConf(){
        return conf;
    }
}
