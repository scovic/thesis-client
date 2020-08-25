package com.example.bachelorthesisclient.util;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.location.Location;

import com.example.bachelorthesisclient.BuildConfig;
import com.example.bachelorthesisclient.R;

import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapUtil {
    private final static String MAPS_API = BuildConfig.MAPS_API;

    public static Single<Polyline> getPolyline(final GeoPoint start, final GeoPoint end) {
        return getRoadManager()
                .map(new Function<RoadManager, Polyline>() {
                    @Override
                    public Polyline apply(RoadManager roadManager) throws Exception {
                        ArrayList<GeoPoint> waypoints = new ArrayList<>();
                        waypoints.add(start);
                        waypoints.add(end);

                        return RoadManager.buildRoadOverlay(roadManager.getRoad(waypoints));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Polyline prepareRoute(Polyline polyline) {
        polyline.getOutlinePaint().setColor(AppContext.getAppContext().getResources().getColor(R.color.colorPrimary));
        polyline.getOutlinePaint().setColor(Color.parseColor("#ff0000"));
        polyline.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
        polyline.usePath(true);
        polyline.getOutlinePaint().setPathEffect(new DashPathEffect(new float[]{10, 35}, 0));

        polyline.getOutlinePaint();
        polyline.getOutlinePaint().setStrokeWidth(30.0f);

        return polyline;
    }

    private static Single<RoadManager> getRoadManager() {
        return Single.create(new SingleOnSubscribe<RoadManager>() {
            @Override
            public void subscribe(SingleEmitter<RoadManager> emitter) throws Exception {
                RoadManager roadManager = new MapQuestRoadManager(MAPS_API);

                roadManager.addRequestOption("routeType=pedestrian");
                roadManager.addRequestOption("unit=k");
                roadManager.addRequestOption("shapeFormat=raw");

                emitter.onSuccess(roadManager);
            }
        });
    }
}
