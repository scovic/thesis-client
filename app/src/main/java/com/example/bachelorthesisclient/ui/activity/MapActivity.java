package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.example.bachelorthesisclient.BuildConfig;
import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.mapobject.FeedMapObject;
import com.example.bachelorthesisclient.model.mapobject.MapObject;
import com.example.bachelorthesisclient.model.mapobject.StageMapObject;
import com.example.bachelorthesisclient.service.NotificationsMessagingService;
import com.example.bachelorthesisclient.ui.viewmodel.MapViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    MapViewModel viewModel;
    Marker userLocationMarker;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViewModel();
        setUpMap();
        handleDataFromIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getUserLocation().observe(this, this.handleUserLocationChanged());
        viewModel.getMapObjects().observe(this, this.handleMapObjectsChange());
        viewModel.getFeed().observe(this, this.handleFeedMapObjectChange());
        viewModel.getEscapeRoute().observe(this, this.handleEscapeRouteChange());
    }

    private Observer<GeoPoint> handleUserLocationChanged() {
        return new Observer<GeoPoint>() {
            @Override
            public void onChanged(GeoPoint geoPoint) {
                Marker marker = new Marker(mapView);
                marker.setTitle("You are here");
                marker.setPosition(geoPoint);

                if (userLocationMarker != null) {
                    userLocationMarker.remove(mapView);
                }

                Bundle extras = getIntent().getExtras();
                if (extras != null && extras.getString("tag", "").equals(NotificationsMessagingService.WARNING_TAG)) {
                    viewModel.findEscapeRoute();
                }

                userLocationMarker = marker;
                mapView.getOverlays().add(userLocationMarker);
                refreshMap();
            }
        };
    }

    private Observer<List<MapObject>> handleMapObjectsChange() {
        return new Observer<List<MapObject>>() {
            @Override
            public void onChanged(List<MapObject> mapObjects) {
                displayObjects(mapObjects);
            }
        };
    }

    private Observer<MapObject> handleFeedMapObjectChange() {
        return new Observer<MapObject>() {
            @Override
            public void onChanged(MapObject feedMapObject) {
                if (feedMapObject != null) {
                    addAnObjectToMap(feedMapObject, getOnFeedMarkerClickListener());
                    centerLocationOnMap(feedMapObject.getLocation(), 18.0);
                    refreshMap();
                }
            }
        };
    }

    private Observer<Polyline> handleEscapeRouteChange() {
        return new Observer<Polyline>() {
            @Override
            public void onChanged(Polyline polyline) {
                if (polyline != null) {
                    redrawObjectsOnMap();
//                    if (viewModel.getEscapeRoute().getValue() != null) {
//                        mapView.getOverlays().remove(viewModel.getEscapeRoute().getValue());
//                    }
//
//                    mapView.getOverlays().add(polyline);
//                    refreshMap();
                }
            }
        };
    }

    private void displayObjects(List<MapObject> list) {
        for (MapObject object : list) {
            if (object instanceof StageMapObject) {
                this.addAnObjectToMap(object, getOnStageClickListener(object));
            } else {
                this.addAnObjectToMap(object);
            }
        }

        mapView.invalidate();
    }

    private void addAnObjectToMap(MapObject object) {
        Marker marker = new Marker(mapView);
        marker.setPosition(object.getLocation());
        marker.setIcon(getResources().getDrawable(object.getDrawableId()));
        marker.setTitle(object.getName());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
    }

    private void addAnObjectToMap(MapObject object, Marker.OnMarkerClickListener clickListener) {
        Marker marker = new Marker(mapView);
        marker.setPosition(object.getLocation());
        marker.setIcon(getResources().getDrawable(object.getDrawableId()));
        marker.setTitle(object.getName());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setOnMarkerClickListener(clickListener);
        mapView.getOverlays().add(marker);
    }

    private Marker.OnMarkerClickListener getOnFeedMarkerClickListener() {
        final int postId = getIntent().getIntExtra("postId", 0);
        return new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                if (postId > 0) {
                    Intent i = new Intent(MapActivity.this, FeedPreviewActivity.class);
                    i.putExtra("postId", postId);
                    startActivity(i);
                    return true;
                }

                return false;
            }
        };
    }

    private Marker.OnMarkerClickListener getOnStageClickListener(final MapObject stage) {
        return new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Intent i = new Intent(MapActivity.this, StagePerformersActivity.class);
                i.putExtra("stageId", stage.getId());
                i.putExtra("stageName", stage.getName());
                startActivity(i);
                return true;
            }
        };
    }

    private void setUpMap() {
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.setMultiTouchControls(true);

        centerLocationOnMap(viewModel.getEventLocation(), 18.0);
        mapView.invalidate();
    }

    private void centerLocationOnMap(GeoPoint location, double zoomLevel) {
        IMapController mapController = mapView.getController();
        mapController.setZoom(zoomLevel);
        mapController.setCenter(location);
    }

    private void refreshMap() {
        mapView.invalidate();
    }

    private void handleDataFromIntent() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String tag = extras.getString("tag");

            if (tag.equals(NotificationsMessagingService.INFO_TAG) || tag.equals("show_feed")) {
                double latitude = extras.getDouble("latitude", 0);
                double longitude = extras.getDouble("longitude", 0);
                String content = extras.getString("content");

                viewModel.setFeed(new FeedMapObject(content, new GeoPoint(latitude, longitude)));
            } else {
                viewModel.findEscapeRoute();
            }
        }
    }

    private void redrawObjectsOnMap() {
        mapView.getOverlays().clear();
        mapView.getOverlays().add(userLocationMarker);

        for (MapObject objects : viewModel.getMapObjects().getValue()) {
            addAnObjectToMap(objects);
        }

        if (viewModel.getEscapeRoute().getValue() != null) {
            mapView.getOverlays().add(viewModel.getEscapeRoute().getValue());
        }

        refreshMap();

    }
}
