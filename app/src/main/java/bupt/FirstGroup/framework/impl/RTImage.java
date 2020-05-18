package bupt.FirstGroup.framework.impl;


import android.graphics.Bitmap;

import bupt.FirstGroup.framework.Image;
import bupt.FirstGroup.framework.Graphics.ImageFormat;

public class RTImage implements Image {
    Bitmap bitmap;
    ImageFormat format;

    public RTImage(Bitmap bitmap, ImageFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}