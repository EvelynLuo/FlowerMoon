package bupt.FirstGroup;



import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcUtil {
    private static JdbcUtil instance;
    private static final String TAG = "mysql";
    final String driver = "com.mysql.jdbc.Driver";
    final String url = "jdbc:mysql://124.70.152.7:3306/androidgame";
    final String user = "root";
    final String pwd = "nbuser";


    public static JdbcUtil getInstance(){
        if (instance ==null){
            instance = new JdbcUtil();
        }
        return instance;
    }
    public Connection getConnection(String url,String name,String password) {
        try {
            Class.forName(driver);

            Log.i(TAG,"DriverManager.getConnection(url,name,password)");
            return DriverManager.getConnection(url,name,password);
        } catch (Exception e) {
            return null;
        }
    }

    public Connection getConnection(String file){
        File f = new File(file);
        if(!f.exists()){
            return null;
        }else {
            Properties pro = new Properties();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                pro.load(new FileInputStream(f));
                String url = pro.getProperty("url");
                String name = pro.getProperty("name");
                String password = pro.getProperty("password");
                return DriverManager.getConnection(url,name,password);
            }catch (Exception e){
                return null;
            }
        }
    }
}

