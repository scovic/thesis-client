package com.example.bachelorthesisclient.ui.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.mapobject.BiggerStateMapObject;
import com.example.bachelorthesisclient.model.mapobject.ExitMapObject;
import com.example.bachelorthesisclient.model.mapobject.MapObject;
import com.example.bachelorthesisclient.model.mapobject.StageMapObject;
import com.example.bachelorthesisclient.model.mapobject.WcMapObject;
import com.example.bachelorthesisclient.util.EventDetailsPersistenceUtil;
import com.example.bachelorthesisclient.util.MapUtil;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends ViewModel {
    private final GeoPoint eventLocation = EventDetailsPersistenceUtil.getEventDetails().getLocation();

    private MutableLiveData<List<MapObject>> mapObjects;
    private MutableLiveData<GeoPoint> userLocation;
    private MutableLiveData<MapObject> feed;
    private MutableLiveData<Polyline> escapeRoute;

    private LocationCallback locationCallback;

    public MapViewModel() {
        super();

        mapObjects = new MutableLiveData<>(getObjects());
        userLocation = new MutableLiveData<>();
        feed = new MutableLiveData<>(null);
        escapeRoute = new MutableLiveData<>(null);

        FusedLocationProviderWrapper.getInstance()
                .getLocationUpdates(setLocationCallback());
    }

    private List<MapObject> getObjects() {
        List<MapObject> objects = new ArrayList<>();

        objects.add(new ExitMapObject("Exit 1", new GeoPoint(43.323208, 21.895265)));
        objects.add(new ExitMapObject("Exit 2", new GeoPoint(43.328052, 21.893086)));
        objects.add(new ExitMapObject("Exit 3", new GeoPoint(43.327731, 21.897362)));
        objects.add(new BiggerStateMapObject("Main Stage", new GeoPoint(43.325676, 21.892801)));
        objects.add(new StageMapObject("Little Jazz Stage", new GeoPoint(43.323581, 21.895090)));
        objects.add(new StageMapObject("Rock Stage", new GeoPoint(43.325800, 21.895313)));
        objects.add(new StageMapObject("Letnja pozornica", new GeoPoint(43.323981, 21.896102)));
        objects.add(new WcMapObject("WC", new GeoPoint(43.324052, 21.895425)));

        return objects;
    }

    private LocationCallback setLocationCallback() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                setUserLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }
        };

        return this.locationCallback;
    }

    public GeoPoint getEventLocation() {
        return eventLocation;
    }

    public MutableLiveData<List<MapObject>> getMapObjects() {
        return mapObjects;
    }

    public MutableLiveData<GeoPoint> getUserLocation() {
        return userLocation;
    }

    public MutableLiveData<MapObject> getFeed() {
        return feed;
    }

    public MutableLiveData<Polyline> getEscapeRoute() {
        return escapeRoute;
    }

    public void setUserLocation(GeoPoint userLocation) {
        this.userLocation.setValue(userLocation);
    }

    public void setFeed(MapObject feed) {
        this.feed.setValue(feed);
    }

    public void setEscapeRoute(Polyline escapeRoute) {
        this.escapeRoute.setValue(escapeRoute);
    }

    public List<MapObject> getExists() {
        List<MapObject> exits = new ArrayList<>();

        for (MapObject mapObject : getMapObjects().getValue()) {
            if (mapObject instanceof ExitMapObject) {
                exits.add(mapObject);
            }
        }

        return exits;
    }

    public void findEscapeRoute() {
        getEscapeRoutes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Polyline>() {
                    private Polyline escapeRoutePolyline;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Polyline polyline) {
                        if (escapeRoutePolyline == null) {
                            escapeRoutePolyline = polyline;
                        } else if (escapeRoutePolyline.getDistance() > polyline.getDistance()) {
                            escapeRoutePolyline = polyline;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Map View Model", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        setEscapeRoute(MapUtil.prepareRoute(escapeRoutePolyline));
                    }
                });
    }

    private Observable<Polyline> getEscapeRoutes() {
        return FusedLocationProviderWrapper.getInstance()
                .getLastLocation()
                .toObservable()
                .map(new Function<Location, GeoPoint>() {
                    @Override
                    public GeoPoint apply(Location location) throws Exception {
                        return new GeoPoint(location.getLatitude(), location.getLongitude());
                    }
                })
                .flatMap(new Function<GeoPoint, Observable<Polyline>>() {
                    @Override
                    public Observable<Polyline> apply(GeoPoint geoPoint) throws Exception {
                        return getRoutePolyline(geoPoint);
                    }
                });
    }

    private Observable<Polyline> getRoutePolyline(final GeoPoint userLocation) {
        return Observable.fromArray(getExists().toArray())
                .map(new Function<Object, MapObject>() {
                    @Override
                    public MapObject apply(Object o) throws Exception {
                        return (MapObject) o;
                    }
                })
                .flatMap(new Function<MapObject, Observable<Polyline>>() {
                    @Override
                    public Observable<Polyline> apply(MapObject object) throws Exception {
                        return MapUtil.getPolyline(userLocation, object.getLocation()).toObservable();
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (this.locationCallback != null) {
            FusedLocationProviderWrapper.getInstance()
                    .removeLocationUpdates(this.locationCallback);
        }
    }
}
