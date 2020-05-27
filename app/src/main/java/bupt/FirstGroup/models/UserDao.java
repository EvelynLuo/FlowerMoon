package bupt.FirstGroup.models;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bupt.FirstGroup.JdbcUtil;


public class UserDao {
    private static final String TAG = "mysql";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://124.70.152.7:3306/flowermoon?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String user = "root";
    private final String pwd = "nbuser";


    //注册
    public boolean register(String name, String password) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName(driver).newInstance();
        conn = DriverManager.getConnection(url, user, pwd);
        Statement st = (Statement) conn.createStatement();
        if (conn == null) {
            Log.i(TAG, "register:conn is null");
            return false;
        } else {
            //进行数据库操作
            String sql = "insert into user(name,password) values(?,?)";
            try {
                PreparedStatement pre = conn.prepareStatement(sql);
                pre.setString(1, name);
                pre.setString(2, password);

                return pre.execute();
            } catch (SQLException e) {
                Log.i(TAG, "insert sql wrong");
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //登录
    public boolean login(String name, String password) {
        try {

            Connection conn = null;
            PreparedStatement stmt = null;
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, user, pwd);
            Statement st = (Statement) conn.createStatement();

            if (conn == null) {
                Log.i(TAG, "register:conn is null");
                return false;
            } else {
                String sql = "select * from user where name=? and password=?";
                try {
                    PreparedStatement pres = conn.prepareStatement(sql);
                    pres.setString(1, name);
                    pres.setString(2, password);
                    ResultSet res = pres.executeQuery();
                    boolean t = res.next();
                    conn.close();//一定要关闭
                    st.close();
                    res.close();
                    return t;
                } catch (SQLException e) {
                    return false;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return false;
        }
    }
}

