package com.example.bachelorthesisclient.wrapper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.bachelorthesisclient.util.AppContext;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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

    public boolean askForLocationPermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(AppContext.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return true;
        }

        return false;
    }

    public void getLocationUpdates(LocationCallback locationCallback) {
        fusedLocationProviderClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                Looper.getMainLooper()
        );
    }

    public void removeLocationUpdates(LocationCallback locationCallback) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
}

