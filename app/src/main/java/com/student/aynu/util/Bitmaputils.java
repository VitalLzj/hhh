package com.student.aynu.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片压缩
 */
public class Bitmaputils {

    public Bitmaputils() {

    }

    public Bitmap getCompressBitmap(String filepath,
                                    int requestHeight, int requestWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = GetinSampleSize(options, requestWidth,
                requestHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static int GetinSampleSize(BitmapFactory.Options options,
                                      int requestWidth, int requestHeight) {
        // 获取原图大小
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        // 定义压缩比例
        int inSampleSize = 1;

        if (imageHeight > requestHeight || imageWidth > requestWidth) {

            int HeightRation = (int) Math.round((double) imageHeight
                    / (double) requestHeight);
            int WidthRation = (int) Math.round((double) imageWidth
                    / (double) requestWidth);

            inSampleSize = HeightRation < WidthRation ? HeightRation
                    : WidthRation;
        }

        return inSampleSize;

    }

}
