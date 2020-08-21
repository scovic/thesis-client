package com.example.bachelorthesisclient.wrapper;

import android.location.Location;

import androidx.annotation.NonNull;

import com.example.bachelorthesisclient.util.AppContext;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class FusedLocationProviderWrapper {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static FusedLocationProviderWrapper instance;

    public static FusedLocationProviderWrapper getInstance() {
        if (instance == null) {
            instance = new FusedLocationProviderWrapper();
        }

        return instance;
    }

    private FusedLocationProviderWrapper() {
        fusedLocationProviderClient = new FusedLocationProviderClient(AppContext.getAppContext());
    }

    public Single<Location> getLastLocation() {
        return Single.create(new SingleOnSubscribe<Location>() {
            @Override
            public void subscribe(final SingleEmitter<Location> emitter) throws Exception {
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                emitter.onSuccess(task.getResult());
                            }
                        });
            }
        })
                .subscribeOn(Schedulers.io());
    }
}

