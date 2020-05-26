package bupt.FirstGroup;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageAnimation extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        //获取旋转加消失的动画资源
        final Animation rotate= AnimationUtils.loadAnimation(ImageAnimation.this,R.anim.rotate);
        //获取展示动画效果的ImageView控件
        final ImageView imgv1=(ImageView)findViewById(R.id.image_test1);
        Button btn1=(Button)findViewById(R.id.btn_test1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgv1.startAnimation(rotate);
            }
        });

        //触碰效果
        Button btn2=(Button)findViewById(R.id.btn_test2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView2 = findViewById(R.id.image_test2);
                imageView2.setBackgroundResource(R.drawable.touch);
                AnimationDrawable animaition = (AnimationDrawable)imageView2.getBackground();
                animaition.setOneShot(false);   //设置是否只播放一次，和上面xml配置效果一致,true为只播放一次，false为循环播放
                animaition.start();             //启动动画
            }
        });

        //完美击中效果
        Button btn3=(Button)findViewById(R.id.btn_test3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView3 = findViewById(R.id.image_test3);
                imageView3.setBackgroundResource(R.drawable.perfecthit);
                AnimationDrawable animaition = (AnimationDrawable)imageView3.getBackground();
                animaition.setOneShot(false);   //设置是否只播放一次，和上面xml配置效果一致
                animaition.start();             //启动动画
            }
        });

        //优秀击中效果
        final Button btn4=(Button)findViewById(R.id.btn_test4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView4 = findViewById(R.id.image_test4);
                imageView4.setBackgroundResource(R.drawable.greathit);
                AnimationDrawable animaition = (AnimationDrawable)imageView4.getBackground();
                animaition.setOneShot(false);   //设置是否只播放一次，和上面xml配置效果一致
                animaition.start();             //启动动画
            }
        });

        //消失特效
        final Button btn5=(Button)findViewById(R.id.btn_test5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView5 = findViewById(R.id.image_test5);
                imageView5.setBackgroundResource(R.drawable.disappeatflower);
                AnimationDrawable animaition = (AnimationDrawable)imageView5.getBackground();
                animaition.setOneShot(false);   //设置是否只播放一次，和上面xml配置效果一致
                animaition.start();             //启动动画
            }
        });
    }
}
