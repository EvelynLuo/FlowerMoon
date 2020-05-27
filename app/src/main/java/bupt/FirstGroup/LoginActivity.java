package bupt.FirstGroup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Looper;
import android.util.Log;

import java.sql.SQLException;

import bupt.FirstGroup.models.UserDao;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "mysql";
    private EditText name;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
    }

    //用户根据点击事件来找到相应的功能
    public void fun(View v) {
        switch (v.getId()) {
            case R.id.register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String n = name.getText().toString().trim();
                        String psw = password.getText().toString().trim();
                        UserDao ud = new UserDao();
                        boolean result = false;
                        try {
                            result = ud.register(n, psw);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (!result) {
                            Looper.prepare();
                            Toast toast = Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_SHORT);
                            toast.show();
                            Looper.loop();
                        }
                        Log.i(TAG, "fun" + result);

                        //以上为jdbc注册
                    }
                }).start();
                break;
            case R.id.login:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String n = name.getText().toString().trim();
                        String psw = password.getText().toString().trim();
                        if (n.equals("") || psw.equals("")) {
                            Looper.prepare();
                            Toast toast = Toast.makeText(LoginActivity.this, "输入不能为空！", Toast.LENGTH_SHORT);
                            toast.show();
                            Looper.loop();
                        }
                        UserDao ud = new UserDao();
                        Boolean result = ud.login(n, psw);
                        if (!result) {
                            Looper.prepare();
                            Toast toast = Toast.makeText(LoginActivity.this, "用户名不存在或密码错误！", Toast.LENGTH_SHORT);
                            toast.show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast toast = Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT);
                            toast.show();

                            //一下代码为跳转界面
                            Intent intent=new Intent(LoginActivity.this, HighscoreActivity.class);
                            //intent.putExtra("name",n);
                            startActivity(intent);
                            Looper.loop();
                        }

                        //以上为jdbc登录
                    }
                }).start();

        }

    }
}
