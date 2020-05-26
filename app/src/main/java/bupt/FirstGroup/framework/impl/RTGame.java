package bupt.FirstGroup.framework.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import bupt.FirstGroup.framework.Audio;
import bupt.FirstGroup.framework.FileIO;
import bupt.FirstGroup.framework.Game;
import bupt.FirstGroup.framework.Graphics;
import bupt.FirstGroup.framework.Input;
import bupt.FirstGroup.framework.Screen;

public class RTGame extends Activity implements Game {
    RTFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;

    @Override
    public void goToActivity(Class<?> activity) {
        Intent i = new Intent(this, activity);
        // add flag, when activity already runs,
        // use it instead of launching a new instance
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取窗口大小
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //获取方向
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //???
        Log.i("game", String.valueOf(isPortrait));

        int frameBufferWidth = getWindowManager().getDefaultDisplay().getWidth();
        int frameBufferHeight = getWindowManager().getDefaultDisplay().getHeight();

        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);

        Log.i("game","getWidth:"+String.valueOf(getWindowManager().getDefaultDisplay().getWidth()));
        Log.i("game","getHeight:"+String.valueOf(getWindowManager().getDefaultDisplay().getHeight()));

        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();

        Log.i("game","scalx:"+String.valueOf(scaleX));
        Log.i("game","scaly:"+String.valueOf(scaleY));


        /**迭代1： 渲染视图和画布**/
        //渲染view视图
        renderView = new RTFastRenderView(this, frameBuffer);
        //渲染画布
        graphics = new RTGraphics(getAssets(), frameBuffer);
        //文件读写
        fileIO = new RTFileIO(this);
        //音频
        audio = new RTAudio(this);
        //触屏事件（很肝的感觉
        input = new RTInput(this, renderView, scaleX, scaleY);
        //当前活动screen，在子类中重写了，调用的是game.LoadingScreen
        screen = getInitScreen();
        //设置当前视图,set之后相当于正式run和update
        setContentView(renderView);

        //？？？
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "RhythmTapper:wake");
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Vibrator getVibrator() {
        return (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public Screen getInitScreen() {
        return null;
    }
}
