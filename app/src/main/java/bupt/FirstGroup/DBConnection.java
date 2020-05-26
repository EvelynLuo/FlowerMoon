package bupt.FirstGroup;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {
    private static final String driver = "com.mysql.jdbc.Driver";
    //private static final String url="jdbc:mysql://云端公网ip:3306/数据库名称";
    private static final String url="jdbc:mysql://124.70.152.7:3306/flowermoon";
    private static final String pwd="nbuser";
    private static final String user="root";

    public static Connection linkMysql(){
        Connection conn=null;
        PreparedStatement stmt = null;
        try{
            Class.forName(driver).newInstance();
            Log.i("mysql","驱动加载成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            conn = DriverManager.getConnection(url,user,pwd);
            Log.i("mysql","连接数据库成功");

        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
   //用户注册
    public static String logUp(String name, String phone,String password){
        PreparedStatement stmt = null;
        Connection conn = linkMysql();
        String resultString = "";
        //判断手机号是否已经注册，判断用户名是否重复
        try{
            String sql = "select * from user where phone = ? ";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,phone);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                System.out.println("the phone is wrong");
                resultString = "该手机号已经被注册，请直接登录";
            }else{
                System.out.println(rs.getRow());
                System.out.println("the phone is right");
                sql = "select * from user where name = ? ";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,name);
                rs = stmt.executeQuery();
                System.out.println(stmt);
                if(rs.next()){
                    System.out.println("the name is wrong");
                    resultString = "该用户名已经被注册，请更换";
                }else{
                    System.out.println("the name is right");
                    sql = "insert into user(name,phone,password) values(?,?,?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1,name);
                    stmt.setString(2,phone);
                    stmt.setString(3,password);
                    stmt.executeUpdate();
                    resultString="注册成功，请登录";
                    Log.i("mysql","add user");

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(conn!=null){
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return resultString;
    }
    // 用户登录
    public static String logIn(String name,String password){
        String resultString = "";
        PreparedStatement stmt = null;
        Connection conn = linkMysql();
        //用户是否注册
        try{
            String sql = "select * from user where name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()){
                resultString = "用户名错误";
            }else if(rs.getString("password").equals(password)){
                resultString = "登录成功";
            }else{
                resultString="密码错误";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultString;
    }
}
