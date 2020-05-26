package bupt.FirstGroup.framework.impl;


import java.io.IOException;
import java.io.InputStream;

import android.animation.Animator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import bupt.FirstGroup.R;
import bupt.FirstGroup.framework.Graphics;
import bupt.FirstGroup.framework.Image;

public class RTGraphics implements Graphics {
    private AssetManager assets;
    private Bitmap frameBuffer;
    private Canvas canvas;
    private Paint paint;
    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();

    public RTGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Image newImage(String fileName, ImageFormat format) {
        Config config = null;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;
        if (fileName=="img/key_cut.png"|fileName=="img/scale_1.png") {
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            //设置想要的大小
            int newWidth=180;
            int newHeight=180;

            //计算压缩的比率
            float scaleWidth=((float)newWidth)/width;
            float scaleHeight=((float)newHeight)/height;

            //获取想要缩放的matrix
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth,scaleHeight);

            //获取新的bitmap
            bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        }
        Log.i("lalala",fileName+":"+String.valueOf(bitmap.getWidth()));
        return new RTImage(bitmap, format);
    }

//    @Override

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }


    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

//    @Override
//    public void drawString(String text, int x, int y, Paint paint, Context context){
//        canvas.drawText(text, x, y, paint);
//    }

    @Override
    public void drawString(String text, int x, int y, Paint paint){
        canvas.drawText(text, x, y, paint);
    }


    public void drawImage(Image Image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((RTImage) Image).bitmap, srcRect, dstRect,
                null);
    }

    @Override
    public void drawImage(Image Image, int x, int y) {
        canvas.drawBitmap(((RTImage)Image).bitmap, x, y, null);
    }

    /**/
    public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight){


        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;


        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;



        canvas.drawBitmap(((RTImage) Image).bitmap, srcRect, dstRect, null);

    }

    @Override
    public int getWidth() {
        Log.i("game", "width:"+String.valueOf(frameBuffer.getWidth()));
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        Log.i("game", "height:"+String.valueOf(frameBuffer.getHeight()));
        return frameBuffer.getHeight();
    }
}