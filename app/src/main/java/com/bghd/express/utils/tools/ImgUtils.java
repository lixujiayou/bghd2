package com.bghd.express.utils.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by lixu on 2018/1/29.
 */

public class ImgUtils {
    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static File addWaterMarker(String imageFilePath, String name, List<String> waterMarkerList) {
        File srcImageFile = new File(imageFilePath);
        if (!srcImageFile.exists()) {
            return null;
        }


        String imageFileName = srcImageFile.getName();


        Bitmap srcBitmap = BitmapFactory.decodeFile(imageFilePath).copy(Bitmap.Config.RGB_565, true);

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        float realTextMargin;
        float realTextSize;
        if (srcHeight > srcWidth) {
            realTextSize = (float) (srcWidth * 1.0D / 720.0D * 29.0D);
            realTextMargin = (float) (srcWidth * 1.0D / 720.0D * 34.0D);
        } else {
            realTextSize = (float) (srcHeight * 1.0D / 960.0D * 29.0D);
            realTextMargin = (float) (srcHeight * 1.0D / 960.0D * 34.0D);
        }

        Canvas canvas = new Canvas(srcBitmap);
        Paint paint = new Paint();

        Typeface font = Typeface.create(Typeface.SERIF, Typeface.BOLD);
        paint.setTypeface(font);
        paint.setTextSize(realTextSize);
        paint.setColor(-16453272);
        for (int i = 0; i < waterMarkerList.size(); i++) {
            canvas.drawText((String) waterMarkerList.get(i), 5.0F, srcHeight - (waterMarkerList.size() - i) * realTextMargin, paint);
        }

        File parentDirFile = srcImageFile.getParentFile();


        File dstImageFile = null;
        int i = 0;
        do {
            dstImageFile = new File(parentDirFile, name);
            i++;
        } while (dstImageFile.exists());

        FileOutputStream fos = null;
        try {
            dstImageFile.createNewFile();
            fos = new FileOutputStream(dstImageFile);

            fos.write(bitmapToByte(srcBitmap));
            fos.close();
            srcBitmap.recycle();
            return dstImageFile;
        } catch (IOException e) {
            Log.d("lixu", "异常66" + e.toString());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.d("lixu", "异常662" + e.toString());
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }
}







