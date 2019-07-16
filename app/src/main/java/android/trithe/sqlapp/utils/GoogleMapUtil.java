package android.trithe.sqlapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.AppConfig;
import android.util.Log;
import android.widget.Toast;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapUtil {

    public static double getDistance(LatLng a, LatLng b) {
        return SphericalUtil.computeDistanceBetween(a, b);
    }

    public static void addPolyline(Context context, GoogleMap map, LatLng currentLatLng, LatLng newLatLong) {
        try {
            List<LatLng> lst = new ArrayList<>();
            lst.add(0, currentLatLng);
            lst.add(1, newLatLong);
            Bitmap defaultWaypointBitmap =
                    MapUtil.getBitmapFromDrawable(ContextCompat.getDrawable(context, R.drawable.waypoint));

            if (!CollectionUtil.isEmpty(lst)) {
                PolylineOptions options = new PolylineOptions()
                        .addAll(lst)
                        .color(ContextCompat.getColor(context, R.color.route_primary_color))
                        .width(AppConfig.MapConfig.ROUTE_WIDTH);
                LatLng startPoint, stopPoint;

                startPoint = lst.get(0);
                stopPoint = lst.get(lst.size() - 1);
                if (startPoint != null) {
                    GoogleMapUtil.addPoint(map,
                            startPoint,
                            MarkerType.NOTHING,
                            MarkerType.NOTHING,
                            defaultWaypointBitmap);
                }
                if (stopPoint != null) {
                    GoogleMapUtil.addPoint(map,
                            stopPoint,
                            MarkerType.NOTHING,
                            MarkerType.NOTHING,
                            defaultWaypointBitmap);
                }

               map.addPolyline(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void polyLine(Context context, GoogleMap mMap, LatLng from, LatLng to) {
        GoogleDirection.withServerKey("AIzaSyCxhGZa3IAYKvJCX-hw5uDGRPU27MkLUAc")
                .from(from)
                .to(to)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            addPolyline(context, mMap, from, to);
                        } else {
                            Toast.makeText(context, direction.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("gsadasdasd", direction.getErrorMessage());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });
    }


    public static Marker addPoint(GoogleMap map, LatLng point, String title, String snippet,
                                  Bitmap bitmap) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet)
                .anchor(0.5f, 0.5f)
                .icon(bitmapDescriptor));
    }


}
