package com.example.bachelorthesisclient.ui.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.model.EventObject;
import com.example.bachelorthesisclient.model.mapobject.ExitMapObject;
import com.example.bachelorthesisclient.model.mapobject.MapObject;
import com.example.bachelorthesisclient.model.mapobject.StageMapObject;
import com.example.bachelorthesisclient.model.mapobject.WcMapObject;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.eventdetails.EventDetailsRepository;
import com.example.bachelorthesisclient.ui.EventObjectToMapObjectMapper;
import com.example.bachelorthesisclient.util.EventDetailsPersistenceUtil;
import com.example.bachelorthesisclient.util.MapUtil;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends ViewModel {
    private final GeoPoint eventLocation = EventDetailsPersistenceUtil.getEventDetails().getLocation();
    private EventDetailsRepository eventDetailsRepository;

    private MutableLiveData<List<MapObject>> mapObjects;
    private MutableLiveData<GeoPoint> userLocation;
    private MutableLiveData<MapObject> feed;
    private MutableLiveData<Polyline> escapeRoute;

    private LocationCallback locationCallback;

    public MapViewModel() {
        super();

        eventDetailsRepository = (EventDetailsRepository) RepositoryFactory.get(RepositoryFactory.EVENT_DETAILS_REPOSITORY);

        List<MapObject> startList = new ArrayList<>();
        mapObjects = new MutableLiveData<>(startList);

        userLocation = new MutableLiveData<>();
        feed = new MutableLiveData<>(null);
        escapeRoute = new MutableLiveData<>(null);

        FusedLocationProviderWrapper.getInstance()
                .getLocationUpdates(setLocationCallback());

        getObjects();
    }

    private void getObjects() {
        eventDetailsRepository.getEventObjects()
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<EventObject>, List<MapObject>>() {
                    @Override
                    public List<MapObject> apply(List<EventObject> eventObjects) throws Exception {
                        List<MapObject> mapObjects = new ArrayList<>();

                        for (EventObject eventObject : eventObjects) {
                            MapObject mappedObject = EventObjectToMapObjectMapper.map(eventObject);

                            if (mappedObject != null) {
                                mapObjects.add(mappedObject);
                            } else {
                                Log.i("MapViewModel", String.format("There is no type %s for event object", eventObject.getType()));
                            }
                        }

                        return mapObjects;
                    }
                })
                .subscribe(new SingleObserver<List<MapObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<MapObject> mapObjects) {
                        setMapObjects(mapObjects);
                    }

                    @Override
                    public void onError(Throwable e) {
                        int a = 5;
                    }
                });

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

    public void setMapObjects(List<MapObject> mapObjects) {
        this.mapObjects.setValue(mapObjects);
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
