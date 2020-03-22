package com.example.guesstheceleberity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Celebrity {

    private String imageSrc;
    private String name;

    public Celebrity(String imageSrc, String name) {
        this.imageSrc = imageSrc;
        this.name = name;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        ImageHelper imageHelper = new ImageHelper();
        return imageHelper.getRoundedCornerBitmap(new DownloadImage().getBitmap(imageSrc), 10);
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "imageSrc='" + imageSrc + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
