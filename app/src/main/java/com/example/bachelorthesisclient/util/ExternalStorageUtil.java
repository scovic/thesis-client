package com.example.bachelorthesisclient.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExternalStorageUtil {
    public static void deleteFile(String path) {
        File file = new File(path);

        if (file.exists()) {
            file.delete();
        }
    }

    public static String saveBitmap(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(AppContext.getAppContext());

        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);

        String tempFileName = String.format("temp_%s.jpg", RandomNumberUtil.getInstance().getRandomNumber());
        File image = new File(dir, tempFileName);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception ex) {

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                }
            }
        }

        return image.getAbsolutePath();
    }
}
