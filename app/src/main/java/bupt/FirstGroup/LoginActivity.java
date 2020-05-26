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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TRY";
    private EditText name;
    private EditText password,phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phone);
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
                        String phone = phoneNumber.getText().toString().trim();      //添加inputText
                        DBConnection db = new DBConnection();
                        String result = db.logUp(n,phone,psw);
                        Looper.prepare();
                        Toast toast = Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();
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
                        DBConnection db = new DBConnection();
                        String result = db.logIn(n,psw);
                        Looper.prepare();
                        Toast toast = Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();
                        if(result.equals("登录成功")){
                        //一下代码为跳转界面
                         Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        //intent.putExtra("name",n);
                        startActivity(intent);
                        }
                        //以上为jdbc登录
                    }
                }).start();

        }

    }
}
