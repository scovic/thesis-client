package com.example.bachelorthesisclient.wrapper;

import android.content.Context;
import android.widget.ImageView;

import com.example.bachelorthesisclient.BuildConfig;
import com.example.bachelorthesisclient.util.AppContext;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PicassoWrapper {
    private Picasso picasso;
    private static PicassoWrapper instance;

    public static PicassoWrapper getInstance() {
        if (instance == null) {
            instance = new PicassoWrapper(AppContext.getAppContext());
        }
        return instance;
    }

    private PicassoWrapper(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        try {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", String.format("Bearer %s", LoggedInUserPersistenceUtil.getToken()))
                                    .build();
                            return chain.proceed(newRequest);
                        } catch (Exception ex) {
                            ex.printStackTrace();

                            return null;
                        }
                    }
                }).build();

        this.picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(okHttpClient)).build();
    }

    public void loadImage(String imageUrl, ImageView imageView) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BuildConfig.BACKEND_BASE_URL);
        stringBuilder.append("/post-service");

        if (imageUrl.charAt(0) == '/') {
            stringBuilder.append(imageUrl);
        } else {
            stringBuilder.append("/").append(imageUrl);
        }

        picasso.load(stringBuilder.toString())
                .resize(500, 500)
                .into(imageView);
    }

}
