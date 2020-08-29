package com.example.bachelorthesisclient.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.bachelorthesisclient.ui.activity.ImagePreviewActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {
    public static void previewImage(Bitmap bitmap, Context context) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent i = new Intent(context, ImagePreviewActivity.class);
        i.putExtra("bitmapByteArray", byteArray);
        context.startActivity(i);

        try {
            stream.close();
        } catch (IOException ex) {
            Log.e("Ticket adapter", "Something went wrong while closing stream");
        }

    }

    public static String makeImageUrl(int postId, String imageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/").append(postId);
        stringBuilder.append("/").append(imageName);

        return stringBuilder.toString();
    }
}
