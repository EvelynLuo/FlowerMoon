package bupt.FirstGroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Button _startBtn;
    private Button _highscoreBtn;
    private Button _aboutBtn,login_btn;
    private ImageButton strat_btn;
    private ImageButton highscore_btn;
    private ImageButton about_btn;
    private ImageButton level1_btn;
    private ImageButton level2_btn;
    private ImageButton level3_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = (Button)findViewById(R.id.login_btn);
        strat_btn=(ImageButton)findViewById(R.id.main_start_button);
        highscore_btn=(ImageButton)findViewById(R.id.main_highscore_button);
        about_btn=(ImageButton)findViewById(R.id.main_about_button);
        level1_btn=(ImageButton)findViewById(R.id.main_first_button);
        level2_btn=(ImageButton)findViewById(R.id.main_second_button);
        level3_btn=(ImageButton)findViewById(R.id.main_third_button);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                MainActivity.this.startActivity(i);
            }
        });
        strat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,DifficultySelectionActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        highscore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,HighscoreActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,AboutActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        level1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,ImageAnimation.class);
                MainActivity.this.startActivity(i);
            }
        });

        strat_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.start_btn2));
                }
                else if (event.getAction()==MotionEvent.ACTION_UP){//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.start_btn1));
                }
                return false;
            }
        });

        highscore_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.score2));
                }
                else if (event.getAction()==MotionEvent.ACTION_UP){//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.score1));
                }
                return false;
            }
        });

        about_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.about2));
                }
                else if (event.getAction()==MotionEvent.ACTION_UP){//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.about1));
                }
                return false;
            }
        });

        level1_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.level1unclicked));
                }
                else if (event.getAction()==MotionEvent.ACTION_UP){//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.level1clicked));
                }
                return false;
            }
        });

        level2_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.level2unclicked));
                }
                else if (event.getAction()==MotionEvent.ACTION_UP){//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.level2clicked));
                }
                return false;
            }
        });

        level3_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){//点击按钮
                    //重新设置按下去时的按钮图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.level3unclicked));
                }
                else if (event.getAction()==MotionEvent.ACTION_UP){//松开按钮
                    //再修改为正常抬起时的图片
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.level3clicked));
                }
                return false;
            }
        });

    }
}
