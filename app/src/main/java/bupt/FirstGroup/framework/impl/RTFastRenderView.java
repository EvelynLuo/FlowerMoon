package bupt.FirstGroup.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RTFastRenderView extends SurfaceView implements Runnable {
    RTGame game;
    Bitmap framebuffer;
    //渲染线程
    Thread renderThread = null;
    //？？？
    SurfaceHolder holder;
    volatile boolean running = false;

    public RTFastRenderView(RTGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();

    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();

    }

    public void run() {
//        Rect dstRect = new Rect(0,0,2357,1312);
        Rect dstRect=new Rect();
        long startTime = System.nanoTime();
        while(running) {
            if(!holder.getSurface().isValid())
                continue;

            //deltaTime每个间隔是10ms
            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;
            startTime = System.nanoTime();

            if (deltaTime > 3.15){
                deltaTime = (float) 3.15;
            }

            //调用当前Screen的update和paint函数
            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(deltaTime);


            /*将当前framebuffer这张图放在每一帧的view上面*/
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);


        }
    }

    public void pause() {
        running = false;
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }

        }
    }


}