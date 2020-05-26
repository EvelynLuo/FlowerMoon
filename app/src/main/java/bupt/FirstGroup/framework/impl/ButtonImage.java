package bupt.FirstGroup.framework.impl;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

import bupt.FirstGroup.framework.Graphics.ImageFormat;
import bupt.FirstGroup.framework.Image;

public class ButtonImage {
    Image image;
    int x;//坐标
    int y;
    Animator animator_appear;//出现效果
    Animator animator_hit;//击中效果，长音符击中
    Animator animator_disapear;//消失效果，旋转消失

    public ButtonImage(Image image, ImageFormat format,int x, int y, Animator animator_hit_1,Animator animator_hit_2, Animator animator_disapear) {
            this.image=image;
            this.x=x;
            this.y=y;
            this.animator_appear=animator_hit_1;
            this.animator_hit=animator_hit_2;
            this.animator_disapear=animator_disapear;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Animator getAnimator_hit_1() {
        return animator_appear;
    }

    public Animator getAnimator_hit_2() {
        return animator_hit;
    }

    public Animator getAnimator_disapear() {
        return animator_disapear;
    }

    public int getWidth() {
            return image.getWidth();
    }

    public int getHeight() {
            return image.getHeight();
    }

    public ImageFormat getFormat() {
            return image.getFormat();
    }

    //    public ButtonImage newButtonImage(String fileName, ImageFormat format, int x, int y, Animator animator_hit_1, Animator animator_hit_2, Animator animator_disapear){
//        Bitmap.Config config = null;
//        if (format == ImageFormat.RGB565)
//            config = Bitmap.Config.RGB_565;
//        else if (format == ImageFormat.ARGB4444)
//            config = Bitmap.Config.ARGB_4444;
//        else
//            config = Bitmap.Config.ARGB_8888;
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = config;
//
//        InputStream in = null;
//        Bitmap bitmap = null;
//        try {
//            in = assets.open(fileName);
//            bitmap = BitmapFactory.decodeStream(in, null, options);
//            if (bitmap == null)
//                throw new RuntimeException("Couldn't load bitmap from asset '"
//                        + fileName + "'");
//        } catch (IOException e) {
//            throw new RuntimeException("Couldn't load bitmap from asset '"
//                    + fileName + "'");
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//
//        if (bitmap.getConfig() == Bitmap.Config.RGB_565)
//            format = ImageFormat.RGB565;
//        else if (bitmap.getConfig() == Bitmap.Config.ARGB_4444)
//            format = ImageFormat.ARGB4444;
//        else
//            format = ImageFormat.ARGB8888;
//
//        return new ButtonImage(bitmap, format,x,y,animator_hit_1,animator_hit_2,animator_disapear);
//    }
}
