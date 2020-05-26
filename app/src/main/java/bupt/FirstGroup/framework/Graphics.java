package bupt.FirstGroup.framework;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;

import bupt.FirstGroup.framework.impl.ButtonImage;

/**
 * Created by Peter on 23.01.2017.
 */
public interface Graphics {

    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Image newImage(String fileName, ImageFormat format);

//    public ButtonImage newButtonImage(String fileName, ImageFormat format, int x, int y, Animator animator_hit_1, Animator animator_hit_2, Animator animator_disapear);

    public void clearScreen(int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawImage(Image image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight);

    public void drawImage(Image Image, int x, int y);

    public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight);

//    void drawString(String text, int x, int y, Paint paint, Context context);

    void drawString(String text, int x, int y, Paint paint);

    public int getWidth();

    public int getHeight();

    public void drawARGB(int i, int j, int k, int l);
}
